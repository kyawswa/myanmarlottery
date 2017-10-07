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
package com.ksa.myanmarlottery.service;

import com.ksa.myanmarlottery.dto.GetPrizeDTO;
import com.ksa.myanmarlottery.model.Result;
import java.util.List;

/**
 *
 * @author Kyawswar
 */
public interface ResultService {
    Result insert(Result result);
    public void deleteByType(int type);
    public Result findResultByType(int type);
    /**
     * find only latest result information WITHOUT prize list.
     * @param type
     * @return 
     */
    public Result findLatestResultSummary(int type);    
    
    List<GetPrizeDTO> findPrizeByResultType(int type, String... codeNo);
    
    /**
     * 
     * @param startCode
     * @param endCode
     * @param point
     * @param type
     * @return 
     */
    public List<GetPrizeDTO> findPrizesByCode(String startCode, String endCode, String point, int type);
    
    /**
     * 
     * @param code
     * @param startPoint
     * @param endPoint
     * @param type
     * @return 
     */
    public List<GetPrizeDTO> findPrizesByPoints(String code, String startPoint, String endPoint, int type);
}
