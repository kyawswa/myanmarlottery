/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.controller;

import com.ksa.myanmarlottery.dto.GetPrizeDTO;
import com.ksa.myanmarlottery.model.Result;
import com.ksa.myanmarlottery.service.ResultService;
import java.util.ArrayList;
import java.util.Date;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author Admin
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
//@WebMvcTest(ResultController.class)
public class ResultControllerTest {
    
    private MockMvc mvc;

    @Mock
    private ResultService resultService ;
    
    @InjectMocks
    ResultController controller;

//     @Mock
//    private LogRepository logRepository ;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

//    @Test
    public void testAdd() throws Exception {
//        when(resultService.findByNoOfTimes(336)).thenReturn(new Result(1, 336, new Date(), new Date()));

        this.mvc.perform(MockMvcRequestBuilders.get("/lottery/add")
                .param("numberOfTimes", "336")
                .param("codeNo", "123456")
                )
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
