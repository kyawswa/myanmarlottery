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
import com.ksa.myanmarlottery.util.CharacterMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Kyawswar
 */
@Repository
@Transactional
public class ResultRepositoryImpl implements ResultRepository {

    @Autowired
    private EntityManager em;
    
    @Autowired
    private CharacterMap charMap;
    
    private Log log = LogFactory.getLog(ResultRepositoryImpl.class);
    
    @Override
    public Result insert(Result result) {
        log.info("insert..");
        getSession().save(result);
        for(Prize p : result.getPrizes()) {
            String index = charMap.get(p.getCode());
            log.info("index.." + index);
            log.info("codepoint.." + p.getCodePoint());
            p.setCodeIndex(Integer.parseInt(index));
            p.setResult(result);
            getSession().save(p);
        }
        return result;
    }

    @Override
    public void deleteByType(int type) {
        log.info("deleteByType..");
        Result r = getSession().get(Result.class, type);
        if(r != null)
            getSession().delete(r);
    }

    @Override
    public Result findLatestResultSummary(int type) {
        log.info("findByNumberOfTimes..");
        return (Result) getSession()
                .getNamedQuery("result.findLatestResultSummary")
                .setInteger("type", type)
                .setResultTransformer(Transformers.aliasToBean(Result.class)).uniqueResult();
    }
    
    @Override
    public List<Prize> findPrizeByResultType(Map<String,List<Integer>> codePoints, int type) {
        log.info("findLatestResult..");
        List<Prize> prizes = new ArrayList<Prize>();
        for(String code : codePoints.keySet()) {
            List<Integer> points = codePoints.get(code);
            List<Prize> prize = (List<Prize>) getSession()
                        .getNamedQuery("prize.findByLatestResult")
                        .setInteger("type", type)
                        .setParameter("code", code)
                        .setParameterList("points", points)
                        .list();
            prizes.addAll(prize);
        }
        return prizes;
    }
    
    @Override
    public List<Prize> findPrizeByCode(int minIndex, int maxIndex, List<Integer> points, int type) {
        log.info("findLatestResult By Code between " + minIndex + " and "+ maxIndex);
        return (List<Prize>) getSession()
                .getNamedQuery("prize.findByCode")
                .setInteger("type", type)
                .setInteger("maxIndex", maxIndex)
                .setInteger("minIndex", minIndex)
                .setParameterList("points", points)
                .list();
    }
    
    @Override
    public List<Prize> findPrizeByPoints(int codeIndex, int startPoint, int endPoint,
                                        int startFivePt, int endFivePt, int startFourPt, 
                                        int endFourPt, int startThreePt, int endThreePt,
                                        int startTwoPt, int endTwoPt, int type) {
        log.info("findLatestResult By Point "+ codeIndex +" between " + startPoint + " and "+ endPoint);
        return (List<Prize>) getSession()
                .getNamedQuery("prize.findByPoints")
                .setInteger("type", type)
                .setInteger("codeIndex", codeIndex)
                .setInteger("startPoint", startPoint)
                .setInteger("endPoint", endPoint)
                .setInteger("sFivePoint", startFivePt)
                .setInteger("eFivePoint", endFivePt)
                .setInteger("sFourPoint", startFourPt)
                .setInteger("eFourPoint", endFourPt)
                .setInteger("sThreePoint", startThreePt)
                .setInteger("eThreePoint", endThreePt)
                .setInteger("sTwoPoint", startTwoPt)
                .setInteger("eTwoPoint", endTwoPt)
                .list();
    }
    
    @Override
    public Result findResultByType(int type) {
        log.info("findResultByType..");
        return (Result)getSession()
                .getNamedQuery("result.findByType")
                .setInteger("type", type)
                .uniqueResult();
    }
    
    private Session getSession() {
        return em.unwrap(Session.class);
    }
}
