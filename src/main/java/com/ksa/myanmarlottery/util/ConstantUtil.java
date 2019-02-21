/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.util;

import com.google.common.primitives.Ints;
import okhttp3.MediaType;
import org.springframework.util.NumberUtils;
import spark.utils.StringUtils;

/**
 *
 * @author Kyawswa
 */
public class ConstantUtil {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    @Deprecated
    public static final int OLD_LOTTERY_TYPE=  1; // for 500 ks lottery
    public static final int NEW_LOTTERY_TYPE = 2; // for 1k ks lottery
    
    public static final String ACTION_TYPING_ON = "typing_on";
    public static final String ACTION_TYPING_OFF = "typing_off";
    
    public static final String WEB = "web";
    public static final String FACEBOOK = "Facebook";
   
    public static final int NORMAL_QUERY = 0;
    public static final int CODE_RANGE_QUERY = 1;
    public static final int POINT_RANGE_QUERY = 2;
    
    public static boolean isCodePointValid(String[] codeNo) {
//        if(codeNo == null || codeNo.isEmpty()) {
//            return false;
//        }
        
//        String[] arr = codeNo.split(" ");
//        if(arr.length != 2) {
//            return false;
//        }
        
        String character = codeNo[0];
        if(character.isEmpty() || character.length() > 2 || character.length() < 1) {
            return false;
        }
        
        // check digit or not.
        String number = codeNo[1];
        if(number.isEmpty() || number.length() != 6) 
            return false;
        
        for(int i=0; i< number.length(); i++) {
            if(!Character.isDigit(number.charAt(i)))
                return false; // assume it is character.
        }
        return true;
    }
    
    /**
     * 0= false, greater than 0=true
     * 1 is findPrizeByCodeRange
     * 2 is findPrizeByPointRange
     * @param codeNo
     * @return 
     */
    public static int getCodePointRangeType(String[] codeNo) {
        String character = codeNo[0];
        if(character.isEmpty() || character.length() > 2 || character.length() < 1) {
            return 0;
        }
        
        String secondChar = codeNo[1];
        String thirdChar = codeNo[2];
        // check second element is digit
        if(!secondChar.isEmpty() && secondChar.length() == 6) { 
            for(int i=0; i< secondChar.length(); i++) {
                if(!Character.isDigit(secondChar.charAt(i)))
                   return 0; // assume it is character.
            }
            // check third part
            if(thirdChar.isEmpty() || thirdChar.length() != 6) // length should not be greater than 6 or less than 1.
                return 0;
            
            // third part must be digit
            for(int i=0; i< thirdChar.length(); i++) {
                if(!Character.isDigit(thirdChar.charAt(i)))
                   return 0; // assume it is character.
            }
            return POINT_RANGE_QUERY; // 2 for findPrizeByPointRange: point mean number
            
            // check second element is character
        } else if(!secondChar.isEmpty() && secondChar.length() <= 2 && secondChar.length() >= 1){
            for(int i=0; i< secondChar.length(); i++) {
                if(Character.isDigit(secondChar.charAt(i)))
                   return 0; // assume it is digit.
            }
            // check third part
            if(thirdChar.isEmpty() || thirdChar.length() != 6)
                return 0;
            
            // third part must be digit
            for(int i=0; i< thirdChar.length(); i++) {
                if(!Character.isDigit(thirdChar.charAt(i)))
                   return 0; // assume it is character.
            }
            return CODE_RANGE_QUERY; // 1 for findPrizeByCodeRange: code means character
        }
        return 0;
    }
}
