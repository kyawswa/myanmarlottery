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
