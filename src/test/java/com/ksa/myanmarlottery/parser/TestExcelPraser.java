/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.parser;

import com.google.common.io.Files;
import com.ksa.myanmarlottery.model.Result;
import com.ksa.myanmarlottery.service.parser.FileParser;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Admin
 */
//@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestExcelPraser {
    
    private static final String FILE_NAME = "D:\\Myanma Lottery\\project\\MyanmarLotteryV1.4\\Copy of Lottery_result (1).xlsx";

    @Autowired
    private FileParser excelFileParser;
    
    @Test
    public void testParser() throws Exception {
        System.out.println(">>>"+Files.getFileExtension(FILE_NAME));
        FileInputStream excelFile = new FileInputStream(new File(FILE_NAME));
        List<Result> resultList = excelFileParser.getResult(excelFile);
        Assert.assertNotNull(resultList);
        Assert.assertTrue(resultList.size()==2);
           
    }
}
