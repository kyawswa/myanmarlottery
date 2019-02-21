/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
