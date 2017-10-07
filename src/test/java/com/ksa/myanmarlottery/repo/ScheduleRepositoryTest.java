/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.repo;

import com.ksa.myanmarlottery.model.ScheduleItem;
import com.ksa.myanmarlottery.repository.ResultRepository;
import com.ksa.myanmarlottery.repository.ScheduleRepository;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Kyawswa
 */
@Ignore
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({"com.ksa.myanmarlottery"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ScheduleRepositoryTest {
    
    @Autowired
    private ScheduleRepository repository;
    
    @Test
    public void testInsert() {
        ScheduleItem item = new ScheduleItem("01", 1, 1, "200 u 123456");
        item.getParam().add("u");
        item.getParam().add("123456");
        
        ScheduleItem result = repository.insertOrUpdateItem(item);
        
        assertNotNull(result);
        assertEquals(result.getRecipientID(), "01");
        assertEquals(result.getOriginParam(), "200 u 123456");
        assertEquals(result.getParam().get(0), "u");
        assertEquals(result.getParam().get(1), "123456");
    }
    
    @Test
    public void testFindById() {
        ScheduleItem item = new ScheduleItem("01", 1, 1, "200 u 123456");
        item.getParam().add("u");
        item.getParam().add("123456");
        
        ScheduleItem item2 = new ScheduleItem("02", 1, 1, "200 au 123456 123459");
        item2.getParam().add("au");
        item2.getParam().add("123456");
        item2.getParam().add("123459");
        
        repository.insertOrUpdateItem(item);
        repository.insertOrUpdateItem(item2);
        
        ScheduleItem result = repository.findItemByID(item2.getRecipientID());
        
        assertNotNull(result);
        assertEquals(result.getRecipientID(), "02");
        assertEquals(result.getOriginParam(), "200 au 123456 123459");
        assertEquals(result.getParam().get(0), "au");
        assertEquals(result.getParam().get(1), "123456");
        assertEquals(result.getParam().get(2), "123459");
    }
    
    @Test
    public void testFindAll() {
        ScheduleItem item = new ScheduleItem("01", 1, 1, "200 u 123456");
        item.getParam().add("u");
        item.getParam().add("123456");
        
        ScheduleItem item2 = new ScheduleItem("02", 1, 1, "200 au 123456 123459");
        item2.getParam().add("au");
        item2.getParam().add("123456");
        item2.getParam().add("123459");
        
        repository.insertOrUpdateItem(item);
        repository.insertOrUpdateItem(item2);
        
        List<ScheduleItem> result = repository.findAll();
        
        assertNotNull(result);
        assertEquals(result.size(), 2);
        assertEquals(result.get(0).getOriginParam(), "200 u 123456");
    }
}
