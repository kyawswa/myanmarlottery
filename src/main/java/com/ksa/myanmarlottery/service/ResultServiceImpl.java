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
import com.ksa.myanmarlottery.model.Prize;
import com.ksa.myanmarlottery.repository.ResultRepository;
import com.ksa.myanmarlottery.model.Result;
import com.ksa.myanmarlottery.util.CharacterMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Kyawswar
 */
@Service
@Transactional
public class ResultServiceImpl implements ResultService {

    @Autowired
    private ResultRepository repositoy;
    
    @Autowired
    private CharacterMap charMap;
    
    private final Log log = LogFactory.getLog(ResultServiceImpl.class);
    
    @Override
    public Result insert(Result result) {
        log.info("insert..");
        repositoy.insert(result);
        return result;
    }
    
    public void deleteByType(int type) {
        log.info("deleteByType..");
        repositoy.deleteByType(type);
    }
    
    private List<Integer> getPointList(String point) {
        List<Integer> points = new ArrayList<>();
        points.add(Integer.parseInt(point.substring(0, point.length()-1)));
        points.add(Integer.parseInt(point.substring(0, point.length()-2)));
        points.add(Integer.parseInt(point.substring(0, point.length()-3)));
        points.add(Integer.parseInt(point.substring(0, point.length()-4)));
        points.add(Integer.parseInt(point));
        log.debug("getPointList: "+points);
        return points;
    }
    
    
    @Override
    public List<GetPrizeDTO> findPrizeByResultType(int type, String... codeNoArr) {
        log.info("findPrizeByLatestResult..");
        Map<String,List<Integer>> codePoints = new HashMap<String, List<Integer>>();
        for(String code: codeNoArr) {
            String[] ar = code.split("-");
            codePoints.put(ar[0], getPointList(ar[1]));
        }
        return convertDTO(repositoy.findPrizeByResultType(codePoints, type));
    }
    
    @Override
    public List<GetPrizeDTO> findPrizesByCode(String startCode, String endCode, String point, int type) {
        log.info("findPrizesByCode..");
        String sIndex = charMap.get(startCode);
        String eIndex = charMap.get(endCode);
        if(sIndex == null || eIndex == null) {
            return new ArrayList<>();
        }
        int startIndex = Integer.parseInt(sIndex);
        int endIndex = Integer.parseInt(eIndex);
        if(startIndex > endIndex) { // should not be startIndex is greater than endIndex.
            return new ArrayList<>();
        }
        return convertDTO(repositoy.findPrizeByCode(startIndex, endIndex, getPointList(point), type));
    }
    
    @Override
    public List<GetPrizeDTO> findPrizesByPoints(String code, String startPoint, String endPoint, int type) {
        log.info("findPrizesByPoints..");
        String codeIndex = charMap.get(code);
        if(codeIndex == null) {
            return new ArrayList<>();
        }
        int sPoint = Integer.parseInt(startPoint);
        int ePoint = Integer.parseInt(endPoint);
        if(sPoint > ePoint)
            return new ArrayList<>();
        
        int startFivePoints = Integer.parseInt(startPoint.substring(0, startPoint.length()-1));
        int startFourPoints = Integer.parseInt(startPoint.substring(0, startPoint.length()-2));
        int startThreePoints = Integer.parseInt(startPoint.substring(0, startPoint.length()-3));
        int startTwoPoints = Integer.parseInt(startPoint.substring(0, startPoint.length()-4));
                
        int endFivePoints = Integer.parseInt(endPoint.substring(0, endPoint.length()-1));
        int endFourPoints = Integer.parseInt(endPoint.substring(0, endPoint.length()-2));
        int endThreePoints = Integer.parseInt(endPoint.substring(0, endPoint.length()-3));
        int endTwoPoints = Integer.parseInt(endPoint.substring(0, endPoint.length()-4));
        
        return convertDTO(repositoy.findPrizeByPoints(Integer.parseInt(codeIndex), sPoint, ePoint, startFivePoints, endFivePoints, startFourPoints, endFourPoints, startThreePoints, endThreePoints, startTwoPoints, endTwoPoints, type));
    }
    
    private List<GetPrizeDTO> convertDTO(List<Prize> prizes) {
        List<GetPrizeDTO> list = new ArrayList<>();
        for(Prize p : prizes) {
            list.add(new GetPrizeDTO(p.getResult().getNumberOfTimes(), p.getTitle(), p.getCodePoint(), p.getDescription()));
        }
        return list;
    }
    
    @Override
    public Result findResultByType(int type) {
        log.info("findResultByType..");
        return repositoy.findResultByType(type);
    }
    
    @Override
    public Result findLatestResultSummary(int type) {
        log.info("findLatestResultSummary..");
        return repositoy.findLatestResultSummary(type);
    }
}
