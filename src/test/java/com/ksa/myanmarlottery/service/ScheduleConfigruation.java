/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.service;

import com.ksa.myanmarlottery.repository.ScheduleRepository;
import com.ksa.myanmarlottery.service.ResultService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

/**
 *
 * @author Kyawswa
 */
@Profile("test")
@Configuration
public class ScheduleConfigruation {
    @Bean
    @Primary
    public ResultService resultService() {
        return Mockito.mock(ResultService.class);
    }
    
    @Bean
    @Primary
    public ScheduleRepository scheduleRepository() {
        return Mockito.mock(ScheduleRepository.class);
    }
}
