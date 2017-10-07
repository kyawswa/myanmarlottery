/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.repo;

import com.ksa.myanmarlottery.model.Prize;
import com.ksa.myanmarlottery.model.Result;
import com.ksa.myanmarlottery.repository.ResultRepository;
import com.ksa.myanmarlottery.util.ConstantUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Kyawswar
 */
@Ignore
@RunWith(SpringRunner.class)
@DataJpaTest
@ComponentScan({"com.ksa.myanmarlottery"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ResultRepositoryTest {
    
    @Autowired
    private ResultRepository repository;

    @Autowired
    private EntityManager em;
    
//    @Test
    public void testInsert() throws Exception {
        Result result = new Result();
        result.setCreatedDate(new Date());
        result.setNumberOfTimes(336);
        result.setResultFor(new Date());
        
        List<Prize> prizeList = new ArrayList<Prize>();
        Prize prize1 = new Prize();
        prize1.setTitle("1000");
        prize1.setCode("abc-123456");
        
        Prize prize2 = new Prize();
        prize2.setTitle("500");
        prize2.setCode("def-112233");
        
        
        prizeList.add(prize1);
        prizeList.add(prize2);
        
        result.setPrizes(prizeList);
        
        Result r = this.repository.insert(result);
        
        assertEquals(r.getNumberOfTimes(), 336);
        assertTrue(r.getPrizes().size() == 2);
    }
    
//    @Test
    public void testDelete() throws Exception {
        Result result = new Result();
        result.setCreatedDate(new Date());
        result.setNumberOfTimes(336);
        result.setResultFor(new Date());
        
        List<Prize> prizeList = new ArrayList<Prize>();
        Prize prize1 = new Prize();
        prize1.setTitle("1000");
        prize1.setCode("abc-123456");
        
        Prize prize2 = new Prize();
        prize2.setTitle("500");
        prize2.setCode("def-112233");
        
        
        prizeList.add(prize1);
        prizeList.add(prize2);
        
        result.setPrizes(prizeList);
        
        Result r = this.repository.insert(result);
        
        repository.deleteByType(r.getType());
        
        assertNotNull(repository.findLatestResultSummary(r.getType()));
    }
    
//    @Test
    public void testFindLatestResult() throws Exception {
        Result result = new Result();
        result.setCreatedDate(new Date());
        result.setNumberOfTimes(3);
        result.setResultFor(new Date());
        result.setType(ConstantUtil.OLD_LOTTERY_TYPE);
        
        
        List<Prize> prizeList = new ArrayList<Prize>();
        Prize prize1 = new Prize();
        prize1.setTitle("1000");
        prize1.setCode("abc-123456");
        
        Prize prize2 = new Prize();
        prize1.setTitle("500");        
        prize1.setCode("def-112233");
        
        prizeList.add(prize1);
        prizeList.add(prize2);
        
        result.setPrizes(prizeList);
        
        em.persist(result);
        
        Result r = this.repository.findLatestResultSummary(ConstantUtil.OLD_LOTTERY_TYPE);
        
        assertEquals(r.getNumberOfTimes(), 3);
        assertTrue(r.getPrizes().size() == 2);
    }
    
//    @Test
    public void testFindPrizeByLatestResult() {
        // String character, List<Integer> codeNo, int type
        Result result1 = new Result();
        result1.setCreatedDate(new Date());
        result1.setNumberOfTimes(337);
        result1.setResultFor(new Date());
        
//        Result result2 = new Result();
//        result2.setNumberOfTimes(336);
//        result2.setResultFor(new Date(2017, 2, 17));
      
        List<Prize> prizeList1 = new ArrayList<Prize>();
        Prize prize1 = new Prize();
        prize1.setCode("abc-123456");
        prizeList1.add(prize1);
        
        List<Prize> prizeList2 = new ArrayList<Prize>();
        Prize prize2 = new Prize();
        prize2.setTitle("500");
        prize2.setCode("def-112233");
        prizeList2.add(prize2);
        
        result1.setPrizes(prizeList1);
//        result2.setPrizes(prizeList2);
        
        repository.insert(result1);
//        repository.insert(result2);
        
        List<Integer> codeNos = new ArrayList<Integer>();
        String codeNo = "ab-123456";
        codeNos.add(Integer.parseInt(codeNo.substring(0, codeNo.length()-1)));
        codeNos.add(Integer.parseInt(codeNo.substring(0, codeNo.length()-2)));
        codeNos.add(Integer.parseInt(codeNo.substring(0, codeNo.length()-3)));
        codeNos.add(Integer.parseInt(codeNo.substring(0, codeNo.length()-4)));
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        map.put(codeNo.split("-")[0], codeNos);
        List<Prize> p = this.repository.findPrizeByResultType(map, ConstantUtil.OLD_LOTTERY_TYPE);
        assertTrue(p.size() == 1);
        assertEquals(p.get(0).getTitle(), "1000");
        assertEquals(p.get(0).getCodePoint(), "abc-123456");
        assertEquals(p.get(0).getResult().getNumberOfTimes(), 337);
    }
    
    @Test
    public void testFindPrizeByCode() {
        // String character, List<Integer> codeNo, int type
        Result result1 = new Result();
        result1.setCreatedDate(new Date());
        result1.setNumberOfTimes(337);
        result1.setResultFor(new Date());
        
        List<Prize> prizeList1 = new ArrayList<Prize>();
        Prize prize1 = new Prize();
        prize1.setTitle("100");
        prize1.setCode("က");
        prize1.setPoint(123456);
        prizeList1.add(prize1);
        
        List<Prize> prizeList2 = new ArrayList<Prize>();
        Prize prize2 = new Prize();
        prize2.setTitle("500");
        prize2.setCode("ကခ");
        prize2.setPoint(112233);
        prizeList2.add(prize2);
        
        result1.setPrizes(prizeList1);
        
        repository.insert(result1);
        
        List<Integer> points = new ArrayList<>();
        String codeNo = "က";
        String point = "123456";
        points.add(Integer.parseInt(point.substring(0, point.length()-1)));
        points.add(Integer.parseInt(point.substring(0, point.length()-2)));
        points.add(Integer.parseInt(point.substring(0, point.length()-3)));
        points.add(Integer.parseInt(point.substring(0, point.length()-4)));
        points.add(Integer.parseInt(point));
        
        List<Prize> p = this.repository.findPrizeByCode(1, 3, points, ConstantUtil.OLD_LOTTERY_TYPE);
        assertTrue(p.size() == 1);
        assertEquals(p.get(0).getTitle(), "100");
        assertEquals(p.get(0).getCodePoint(), codeNo+"-"+point);
        assertEquals(p.get(0).getResult().getNumberOfTimes(), 337);
    }
    
//    @Test
//    public void testFindPrizeByNoRangeInLatestResult() {
//        // String character, int startNo, int endNo, int type
//        Result result1 = new Result();
//        result1.setCreatedDate(new Date());
//        result1.setNumberOfTimes(337);
//        result1.setResultFor(new Date());
//        
//        List<Prize> prizeList1 = new ArrayList<Prize>();
//        Prize prize1 = new Prize();
//        prize1.setTitle("1000");
//        prize1.setAlphabet("abc");
//        prize1.setCodeNo(123456);
//        prizeList1.add(prize1);
//        result1.setPrizes(prizeList1);
//        
//        Result result2 = new Result();
//        result2.setCreatedDate(new Date());
//        result2.setNumberOfTimes(337);
//        result2.setResultFor(new Date());
//        
//        List<Prize> prizeList2 = new ArrayList<Prize>();
//        Prize prize2 = new Prize();
//        prize2.setTitle("500");
//        prize2.setAlphabet("def");
//        prize2.setCodeNo(132);
//        prizeList2.add(prize2);
//        result2.setPrizes(prizeList2);
//        
//        
//        repository.insert(result1);
//        repository.insert(result2);
//        
//        List<Prize> p = this.repository.findPrizeByNoRangeInLatestResult("abc", 121212, 345678, ConstantUtil.OLD_LOTTERY_TYPE);
//        assertTrue(p.size() == 1);
//        assertEquals(p.get(0).getTitle(), "1000");
//        assertEquals(p.get(0).getAlphabet(), "abc");
//        assertEquals(p.get(0).getResult().getNumberOfTimes(), 337);
//        assertEquals(p.get(0).getCodeNo(), 123456);
//        
//    }
}
