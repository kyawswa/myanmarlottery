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
package com.ksa.myanmarlottery.service;

import com.ksa.myanmarlottery.dto.GetPrizeDTO;
import com.ksa.myanmarlottery.model.ScheduleItem;
import com.ksa.myanmarlottery.repository.ScheduleRepository;
import com.ksa.myanmarlottery.util.ConstantUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import javax.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kyawswa
 */
@Service
@Transactional
public class ScheduleServiceImpl implements ScheduleService {

    private final Log log = LogFactory.getLog(ScheduleServiceImpl.class);
    
    @Autowired
    private ScheduleRepository repository;
    
    @Autowired
    private ResultService resultService;
    
    @Override
    public List<ScheduleItem> findAll() {
        log.info("findAll Schedule items.");
        return repository.findAll();
    }

    @Override
    public ScheduleItem insertOrUpdateItem(ScheduleItem item) {
        log.info("insertOrUpdateItem :" + item.getRecipientID());
        return repository.insertOrUpdateItem(item);
    }
    
    @Override
    public ScheduleItem findItemBySenderID(String senderID) {
        log.info("findBySenderID:" + senderID);
        return repository.findItemByID(senderID);
    }
    
    @Async
    @Override
    public Future<List<GetPrizeDTO>> scheduleItems(ScheduleItem item) throws InterruptedException {
        log.info("Start Schedule with : " +item.getRecipientID());
        log.info("query Type " + item.getQueryType());
        Future<List<GetPrizeDTO>> result = new AsyncResult<>(new ArrayList<>());
        if(item.getQueryType() == ConstantUtil.NORMAL_QUERY) {
            result = new AsyncResult<>(resultService.findPrizeByResultType(item.getLotteryType(), item.getParam().toArray(new String[]{})));
        } else if(item.getQueryType() == ConstantUtil.CODE_RANGE_QUERY) {
            result = new AsyncResult<>(resultService.findPrizesByCode(item.getParam().get(0), item.getParam().get(1), item.getParam().get(2), item.getLotteryType()));
        } else if(item.getQueryType() == ConstantUtil.POINT_RANGE_QUERY) {
            result = new AsyncResult<>(resultService.findPrizesByPoints(item.getParam().get(0), item.getParam().get(1), item.getParam().get(2), item.getLotteryType()));
        }
        // remove from db after finding result.
        deleteScheduleItem(item.getRecipientID());
        return result;
    }
    
    @Override
    public void deleteScheduleItem(String id) {
        repository.deleteItem(id);
    }
}
