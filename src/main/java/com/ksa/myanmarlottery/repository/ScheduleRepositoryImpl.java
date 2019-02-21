/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.repository;

import com.ksa.myanmarlottery.model.ScheduleItem;
import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kyawswa
 */
@Repository
@Transactional
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final Log log = LogFactory.getLog(ScheduleRepositoryImpl.class);
    
    @Autowired
    private EntityManager em;
    
    @Override
    public List<ScheduleItem> findAll() {
        log.info("Find All Schdedule Items.");
        return getSession().getNamedQuery("scheduleItem.findAll").list();
    }

    @Override
    public ScheduleItem insertOrUpdateItem(ScheduleItem item) {
        log.info("insertOrUpdateItem.");
        getSession().saveOrUpdate(item);
        return item;
    }
    
    @Override
    public ScheduleItem findItemByID(String senderID) {
        log.info("findItemBySenderID.");
        return getSession().get(ScheduleItem.class, senderID);
    }
    
    @Override
    public void deleteItem(String senderID) {
        log.info("deleteItem:"+ senderID);
        ScheduleItem item = findItemByID(senderID);
        if(item != null) {
            getSession().delete(item);
        }
    }
    
    private Session getSession() {
        return em.unwrap(Session.class);
    }
}
