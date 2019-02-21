/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery;

import com.google.gson.Gson;
import com.hyurumi.fb_bot_boilerplate.FBMessageSender;
import com.ksa.myanmarlottery.service.AppPulser;
import com.ksa.myanmarlottery.service.parser.CSVFileParser;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Kyaw Swar Aung
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({"com.ksa.myanmarlottery"})
@EnableAsync
@EnableScheduling
@EnableCaching
public class AppConfig extends AsyncConfigurerSupport implements SchedulingConfigurer {
    
    @Autowired
    private Environment env;
    
    @Bean
    public FBMessageSender messageSender() {
        return new FBMessageSender(new Gson(), new OkHttpClient(), env.getProperty("facebook.hub.access_token"), env.getProperty("facebook.endpoint"));
    }
    
    @Bean
    public CSVFileParser csvParser() {
        return new CSVFileParser();
    }
    
    public static void main(String[] args) {
        
        SpringApplication.run(AppConfig.class, args);
    }
    
    @Bean
   public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
      return new PropertySourcesPlaceholderConfigurer();
   }
    
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("LotteryTask-");
        executor.initialize();
        return executor;
    }
    
    @Bean
    public AppPulser bean() {
        return new AppPulser(new OkHttpClient());
    }
 
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }
 
    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(1);
    }
}
