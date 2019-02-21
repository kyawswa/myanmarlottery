/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.repository;

import com.ksa.myanmarlottery.model.LogInfo;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kyawswar
 */
@Repository
@Transactional
public class LogRepositoryImpl implements LogRepository {

    @Autowired
    private EntityManager em;
    
    private Log log = LogFactory.getLog(LogRepositoryImpl.class);
    
    @Override
    public void inserLog(LogInfo logInfo) {
        log.info("inserLog..");
        em.persist(logInfo);
    }
}
