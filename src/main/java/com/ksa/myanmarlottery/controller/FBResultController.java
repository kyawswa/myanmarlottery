/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.controller;

import com.ksa.myanmarlottery.service.fb.FBResultService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Kyawswa
 */
@Controller
public class FBResultController {
    
    @Autowired
    private Environment env;
    
    @Autowired
    private FBResultService fbResultService;

    private Log log = LogFactory.getLog(FBResultController.class);

    @GetMapping(path = "/webhook")
    public ResponseEntity<String> challenge(@RequestParam(name = "hub.verify_token") String verify_token, 
                                            @RequestParam(name = "hub.challenge") String challenge) {
        log.info("challenge request "+ verify_token);
        if (verify_token.equals(env.getProperty("facebook.hub.verify_token"))) {
            return new ResponseEntity<>(challenge, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    
    @PostMapping(path = "/webhook")
    public ResponseEntity getResponse(@RequestBody String body) {
        log.info("getResponse..");
        fbResultService.manageRequst(body);
        return new ResponseEntity(HttpStatus.OK);
    }
}
