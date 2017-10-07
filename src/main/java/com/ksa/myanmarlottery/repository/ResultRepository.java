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
package com.ksa.myanmarlottery.repository;

import com.ksa.myanmarlottery.model.Prize;
import com.ksa.myanmarlottery.model.Result;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kyawswar
 */
public interface ResultRepository {
    Result insert(Result result);
    void deleteByType(int type);    

    /**
     * 
     * @param minIndex
     * @param maxIndex
     * @param points
     * @param type
     * @return 
     */
    public List<Prize> findPrizeByCode(int minIndex, int maxIndex, List<Integer> points, int type);

    /**
     * 
     * @param codeIndex
     * @param startPoint
     * @param endPoint
     * @param startFivePt
     * @param endFivePt
     * @param startFourPt
     * @param endFourPt
     * @param startThreePt
     * @param endThreePt
     * @param startTwoPt
     * @param endTwoPt
     * @param type
     * @return 
     */
    public List<Prize> findPrizeByPoints(int codeIndex, int startPoint, int endPoint,
                                        int startFivePt, int endFivePt, int startFourPt, 
                                        int endFourPt, int startThreePt, int endThreePt,
                                        int startTwoPt, int endTwoPt, int type);
        
    public Result findResultByType(int type);
    /**
     * find prize on latest result by codeNo and type
     * @param codePoints
     * @param type
     * @return 
     */
    public List<Prize> findPrizeByResultType(Map<String,List<Integer>> codePoints, int type);

    /**
     * find only latest result information WITHOUT prize list.
     * @param type
     * @return 
     */
    public Result findLatestResultSummary(int type);
}
