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
package com.ksa.myanmarlottery.service.fb;

/**
 * This class handle the Facebook request and return json response to Facebook. 
 * It handles following queries and functions:
 * (1) search by character range with same number. eg. (ka nya 123456)
 * (2) search each character and number. eg. (ka 123456,nya 123457)
 * (3) search by number range with same character. eg. (ka 123456 123459)
 * (4) download the lottery result file from Facebook request and read file and insert into DB.
 * (5) handle the lottery result scheduling. User can register his lottery number before the result come out.
 * (6) start the lottery schedule after lottery result is inserted into DB.
 * 
 * @author Kyawswar
 */
public interface FBResultService {
    void manageRequst(String receiveMessage);
}
