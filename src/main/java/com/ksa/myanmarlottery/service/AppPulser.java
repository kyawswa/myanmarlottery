/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.service;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author Kyawswa
 */
public class AppPulser {
    
    private final Log log = LogFactory.getLog(AppPulser.class);
    
    private OkHttpClient httpClient;
    public AppPulser(OkHttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    @Scheduled(fixedRateString = "${app.pulse.time}")
    public void pulseMessage() {
        try {
            log.info("Keep my finger on app pulse to keep alive on " + new Date());
            Request request = new Request.Builder()
                    .url("https://myanmarlottery.herokuapp.com/greeting")
                    .build();
            okhttp3.Response response = httpClient.newCall(request).execute();
            log.info("response code: " + response.code());
            response.body().close();
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }
}
