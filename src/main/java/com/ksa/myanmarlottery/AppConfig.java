/** 
 * The MIT License
 *
 * Copyright 2017 kyawswaraung
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.ksa.myanmarlottery;

import com.google.gson.Gson;
import com.hyurumi.fb_bot_boilerplate.FBMessageSender;
import com.ksa.myanmarlottery.service.parser.CSVFileParser;
import java.util.concurrent.Executor;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Kyaw Swar
 */
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan({"com.ksa.myanmarlottery"})
@EnableAsync
@EnableCaching
public class AppConfig extends AsyncConfigurerSupport  {
    
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
}
