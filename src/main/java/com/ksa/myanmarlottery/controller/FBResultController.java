/** 
 * The MIT License
 *
 * Copyright 2017 kyawswaraung
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
 * It handles the RESTful request from Facebook.
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
