/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.repository;

import com.ksa.myanmarlottery.model.ScheduleItem;
import java.util.List;

/**
 *
 * @author Kyawswa
 */
public interface ScheduleRepository {
    List<ScheduleItem> findAll();
    ScheduleItem insertOrUpdateItem(ScheduleItem item);
    ScheduleItem findItemByID(String senderID);
    void deleteItem(String id);
}
