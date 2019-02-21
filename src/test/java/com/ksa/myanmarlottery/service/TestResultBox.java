/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.service;

import com.ksa.myanmarlottery.model.Prize;
import com.ksa.myanmarlottery.model.Result;
import com.ksa.myanmarlottery.util.ResultBox;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Kyawswa
 */
//@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestResultBox {
    
    @Autowired
    private ResultBox resultBox;
    
    @Test
    public void testInsertCache() {
        Result result = new Result();
        result.setCreatedDate(new Date());
        result.setNumberOfTimes(336);
        result.setResultFor(new Date());
        
        List<Prize> prizeList = new ArrayList<Prize>();
        Prize prize1 = new Prize();
        prize1.setTitle("1000");
        
        
        Prize prize2 = new Prize();
        prize1.setTitle("500");
        
        prizeList.add(prize1);
        prizeList.add(prize2);
        result.setPrizes(prizeList);
        
        String json1 = resultBox.getJsonValue(result.getType());
        String json2 = resultBox.getJsonValue(result.getType());
        Assert.assertNotNull(json1);
        Assert.assertNotNull(json2);
    }
    
    @Test
    public void testEvictCache() {
        Result result = new Result();
        result.setCreatedDate(new Date());
        result.setNumberOfTimes(336);
        result.setResultFor(new Date());
        
        
        List<Prize> prizeList = new ArrayList<Prize>();
        Prize prize1 = new Prize();
        prize1.setTitle("1000");
        
        
        Prize prize2 = new Prize();
        prize1.setTitle("500");
        
        prizeList.add(prize1);
        prizeList.add(prize2);
        result.setPrizes(prizeList);
        
        String json1 = resultBox.getJsonValue(result.getType());
        Result r = new Result();
        r.setType(2);
        resultBox.removeJsonValue(r.getType());
        String json2 = resultBox.getJsonValue(result.getType());
        Assert.assertNotNull(json1);
        Assert.assertNotNull(json2);
    }
    
    @Test
    public void testFindKeyCache() {
        Result result = new Result();
        result.setType(1);
        result.setCreatedDate(new Date());
        result.setNumberOfTimes(336);
        result.setResultFor(new Date());
        
        
        List<Prize> prizeList = new ArrayList<Prize>();
        Prize prize1 = new Prize();
        prize1.setTitle("1000");
        
        
        Prize prize2 = new Prize();
        prize1.setTitle("500");
        
        prizeList.add(prize1);
        prizeList.add(prize2);
        result.setPrizes(prizeList);
        
        String json1 = resultBox.getJsonValue(result.getType());
        Result r = new Result();
        r.setType(1);
        String json2 = resultBox.getJsonValue(r.getType());
        Assert.assertNotNull(json1);
        Assert.assertNotNull(json2);
    }
    
//    @Test
    public void testFindNullKeyCache() {
        Result result = new Result();
        result.setType(1);
        result.setCreatedDate(new Date());
        result.setNumberOfTimes(336);
        result.setResultFor(new Date());
        
        List<Prize> prizeList = new ArrayList<Prize>();
        Prize prize1 = new Prize();
        prize1.setTitle("1000");
        
        Prize prize2 = new Prize();
        prize1.setTitle("500");
        
        prizeList.add(prize1);
        prizeList.add(prize2);
        result.setPrizes(prizeList);
        
        String json1 = resultBox.getJsonValue(result.getType());
        Assert.assertNotNull(json1);
        
        Result r = new Result();
        r.setType(2);
        r.setNumberOfTimes(0);
        String json2 = resultBox.getJsonValue(r.getType());
        Assert.assertNull(json2);
    }
}
