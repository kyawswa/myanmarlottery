/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
