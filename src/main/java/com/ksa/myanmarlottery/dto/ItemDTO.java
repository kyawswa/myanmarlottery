/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.dto;

import com.ksa.myanmarlottery.util.ConstantUtil;
import java.util.List;

/**
 *
 * @author Kyawswar
 */
public class ItemDTO {
    
    public List<String> codePoints;
    public int queryType;
    public int type = ConstantUtil.OLD_LOTTERY_TYPE;

    public ItemDTO(List<String> codePoint) {
        this.codePoints = codePoint;
    }

    @Override
    public String toString() {
        return "ItemDTO{" + ", character=" + codePoints.size() + ", type=" + type + ", queryType="+ queryType+ '}';
    }
    
    
}