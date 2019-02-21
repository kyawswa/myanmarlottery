/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.service;

import com.ksa.myanmarlottery.dto.GetPrizeDTO;
import com.ksa.myanmarlottery.model.ScheduleItem;
import com.ksa.myanmarlottery.repository.ScheduleRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Kyawswa
 */
@Ignore
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ScheduleServiceTest {
    
    @Autowired
    private ResultService resultService ;
    
    @Autowired
    private ScheduleRepository scheduleRepository ;
    
    @Autowired
    private ScheduleService service;

    @Test
    public void testSchedule() throws Exception {
        
        GetPrizeDTO dto1 = new GetPrizeDTO(1, "result1", "001", "description");
        GetPrizeDTO dto2 = new GetPrizeDTO(2, "result2", "002", "description");
        GetPrizeDTO dto3 = new GetPrizeDTO(3, "result3", "003", "description");
        List<GetPrizeDTO> dtos = new ArrayList<GetPrizeDTO>();
        dtos.add(dto1);
        dtos.add(dto2);
        dtos.add(dto3);
        Mockito.when(resultService.findPrizeByResultType(1, "a-123456")).thenReturn(dtos);
        
        GetPrizeDTO dto12 = new GetPrizeDTO(4, "result1", "001", "description");
        GetPrizeDTO dto22 = new GetPrizeDTO(5, "result2", "002", "description");
        GetPrizeDTO dto32 = new GetPrizeDTO(6, "result3", "003", "description");
        List<GetPrizeDTO> dtos2 = new ArrayList<GetPrizeDTO>();
        dtos2.add(dto12);
        dtos2.add(dto22);
        dtos2.add(dto32);
        Mockito.when(resultService.findPrizesByCode("a", "z", "123456", 2)).thenReturn(dtos2);
        
        GetPrizeDTO dto13 = new GetPrizeDTO(7, "result2", "002", "description");
        GetPrizeDTO dto23 = new GetPrizeDTO(8, "result3", "003", "description");
        GetPrizeDTO dto33 = new GetPrizeDTO(9, "result3", "003", "description");
        List<GetPrizeDTO> dtos3 = new ArrayList<GetPrizeDTO>();
        dtos3.add(dto13);
        dtos3.add(dto23);
        dtos3.add(dto33);
        Mockito.when(resultService.findPrizesByPoints("az", "123450", "123459", 2)).thenReturn(dtos3);
        
        ScheduleItem item = new ScheduleItem("01", 1, 0, "200 a 123456");
        item.getParam().add("a-123456");
        
        ScheduleItem item2 = new ScheduleItem("02", 2, 1, "200 a z 123456");
        item2.getParam().add("a");
        item2.getParam().add("z");
        item2.getParam().add("123456");
        
        ScheduleItem item3 = new ScheduleItem("03", 2, 2, "200 a 123450 123459");
        item3.getParam().add("az");
        item3.getParam().add("123450");
        item3.getParam().add("123459");
        
        List<ScheduleItem> list = new ArrayList<ScheduleItem>();
        list.add(item);
        list.add(item2);
        list.add(item3);
        Mockito.when(scheduleRepository.findAll()).thenReturn(list);
        
        List<ScheduleItem> result = scheduleRepository.findAll();
        for(ScheduleItem  itm: result) {
            Future<List<GetPrizeDTO>> r = service.scheduleItems(itm);
            Assert.assertTrue(!r.get().isEmpty());
            assertEquals(r.get().size(), 3);
        }
        
    }

}
