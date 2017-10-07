/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.controller;

import com.jayway.jsonpath.JsonPath;
import com.ksa.myanmarlottery.dto.GetPrizeDTO;
import com.ksa.myanmarlottery.model.Result;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 *
 * @author Admin
 */
@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ResultControllerIntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

//    @Test
    public void testFindPrize() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("numberOfTimes", "336");
        map.put("character", "ab");
        map.put("codeNo", "123456");
        ResponseEntity<GetPrizeDTO> response = this.restTemplate.getForEntity("/lottery/prize?numberOfTimes=336&codeNo=123456&character=ab", GetPrizeDTO.class);
        Assert.assertTrue(response.getStatusCodeValue() == 200);
    }

//    @Test
    public void testAddResult() {
        String json = "{\n" +
                    "  \"numberOfTimes\": 336,\n" +
                    "  \"createdDate\": 1488962733399,\n" +
                    "  \"type\": 1,\n" +
                    "  \"resultFor\": 1488962733399,\n" +
                    "  \"prizes\": [\n" +
                    "    {\n" +
                    "      \"prizeInfo\": \"1000\",\n" +
                    "      \"items\": [\n" +
                    "        {\n" +
                    "          \"word\": \"abc\",\n" +
                    "          \"code\": \"123456\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"prizeInfo\": \"500\",\n" +
                    "      \"items\": [\n" +
                    "        {\n" +
                    "          \"word\": \"def\",\n" +
                    "          \"code\": \"112233\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "          \"word\": \"ghi\",\n" +
                    "          \"code\": \"223344\"\n" +
                    "        }\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "opensezmi");
        HttpEntity request = new HttpEntity(json, headers);
        
        ResponseEntity<String> response = this.restTemplate
                                            .exchange("/lottery/add", HttpMethod.POST, request, String.class);
        Assert.assertTrue(response.getStatusCodeValue() == 200);
    }
}
