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
 * keep the json string in Cache.
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
