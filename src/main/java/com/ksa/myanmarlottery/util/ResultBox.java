/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.util;

import com.hyurumi.fb_bot_boilerplate.FBMessageSender;
import com.ksa.myanmarlottery.dto.ResultDTO;
import com.ksa.myanmarlottery.model.Prize;
import com.ksa.myanmarlottery.model.Result;
import com.ksa.myanmarlottery.service.ResultService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * keep the json string derive from result.
 * 
 * @author Kyawswa
 */
@Component
public class ResultBox {
    
    @Autowired
    private FBMessageSender messageSender;
    
    @Autowired
    private ResultService resultService;
    
    private Log log = LogFactory.getLog(ResultBox.class);
    
    private ResultDTO getResultDTO(Result result) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setDate(result.getResultFor());
        resultDTO.setNoOfTimes(result.getNumberOfTimes());
        for (Prize p : result.getPrizes()) {
            resultDTO.put(p.getTitle(), p.getCodePoint());
        }
        resultDTO.setDataProvider(result.getDataProvider());
        resultDTO.setBannerText(result.getCompanyName());
        return resultDTO;
    }
    
    @Cacheable(cacheNames = "json_result", key = "#type")
    public String getJsonValue(int type) {
        log.info("getJsonValue by : " + type);
        // to know the result is mock data or not.
        // nature of caching, it will save into cache if there is no key.
        Result result = resultService.findResultByType(type);
        String json = null;
        if(result != null) {
            json = messageSender.getGson().toJson(getResultDTO(result));
        }
        return json;
    }
    
    @CacheEvict(cacheNames = "json_result",key = "#type")
    public void removeJsonValue(int type) {
        log.info("removeJsonValue:");
    }
}
