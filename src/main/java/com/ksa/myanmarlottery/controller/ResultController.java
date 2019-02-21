/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.controller;

import com.ksa.myanmarlottery.dto.GetPrizeDTO;
import com.ksa.myanmarlottery.model.LogInfo;
import com.ksa.myanmarlottery.model.Prize;
import com.ksa.myanmarlottery.model.Result;
import com.ksa.myanmarlottery.repository.LogRepository;
import com.ksa.myanmarlottery.service.ResultService;
import com.ksa.myanmarlottery.util.ConstantUtil;
import com.ksa.myanmarlottery.util.ResultBox;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Kyawswar
 */
@RestController
public class ResultController {

    @Autowired
    private ResultBox resultBox;

    private Log log = LogFactory.getLog(ResultController.class);
    
    @Autowired
    private LogRepository logRepository;
    
    @Autowired
    private ResultService resultService;

    @GetMapping(path = "/result", params = {"type"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getResultByType(@RequestParam int type, HttpServletRequest request) {
        log.info("Find latest Result By type.");
        //logRepository.inserLog(new LogInfo(new Date(), getRemoteAddress(request), 
        //        ConstantUtil.WEB, null, null));
        Result r = new Result();
        r.setType(type);
        String json = resultBox.getJsonValue(type);
        log.debug("Result " + json);
        if (json == null || json.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }
    
    private String getRemoteAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        log.debug("Remote Address " + request.getRemoteAddr());
        return ipAddress;
    }

    @GetMapping(path = "/result", params = {"type", "code"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<GetPrizeDTO>> getResultByType(@RequestParam int type, @RequestParam String code, HttpServletRequest request) {
        log.info("Find latest Result By type.");
        logRepository.inserLog(new LogInfo(new Date(), getRemoteAddress(request), 
                ConstantUtil.WEB, null, null));
        String[] str = code.split("-");
        Integer integer = new Integer(str[1]);
        log.info("integer " + integer);
        code=str[0]+"-"+integer;
        List<GetPrizeDTO> prize = resultService.findPrizeByResultType(type, code);
        return new ResponseEntity<>(prize, HttpStatus.OK);
    }
    
    @GetMapping("/greeting")
    public ResponseEntity greeting() {
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
}
