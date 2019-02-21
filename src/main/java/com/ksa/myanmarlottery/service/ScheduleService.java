/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.service;

import com.ksa.myanmarlottery.dto.GetPrizeDTO;
import com.ksa.myanmarlottery.model.ScheduleItem;
import java.util.List;
import java.util.concurrent.Future;

/**
 *
 * @author Kyawswa
 */
public interface ScheduleService {
    List<ScheduleItem> findAll();
    ScheduleItem insertOrUpdateItem(ScheduleItem item);
    ScheduleItem findItemBySenderID(String senderID);
    public void deleteScheduleItem(String id);
    public Future<List<GetPrizeDTO>> scheduleItems(ScheduleItem item) throws InterruptedException;
}
