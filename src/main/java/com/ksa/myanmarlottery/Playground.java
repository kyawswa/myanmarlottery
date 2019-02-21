/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import com.ksa.myanmarlottery.util.ConstantUtil;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Kyawswa
 */
public class Playground {
    public static void main(String[] args) {
//        String are = "ကu  1၂၃၄၅၆";
//        
//        if (Character.UnicodeBlock.of(are.charAt(3)) != Character.UnicodeBlock.BASIC_LATIN) {
//        if (Character.isDigit(are.charAt(1))) {
//            System.out.println("Yes");
//        } else {
//            System.out.println("NO");
//        }
//        String[] ar = are.split(" ");
//        Integer i = new Integer(ar[1]);
//        System.out.println(i);
//        System.out.println(ar.length);
        
//        char[] arr = {'က', 'ခ', 'ဂ', 'ဃ', 'င',
//                        'စ', 'ဆ', 'ဇ', 'ဈ', 'ည',
//                        'ဋ', 'ဌ', 'ဍ', 'ဎ', 'ဏ',
//                        'တ', 'ထ', 'ဒ', 'ဓ', 'န',
//                        'ပ', 'ဖ', 'ဗ', 'ဘ', 'မ',
//                        'ယ', 'ရ', 'လ', 'ဝ', 'သ',
//                        'ဟ', 'ဠ', 'အ'};

//        for(char c : arr) {
//            System.out.println(Integer.toHexString(c));
//            if(c == '\u1017') {
//                System.out.println("found index ");
//            }
//        }
        String str = "@ 200 a 123456, b 123456,c 234569 , a 090909";
        String typeStr = str.substring(0, 4);
        str = str.substring(2);
        int lotteryType = ConstantUtil.NEW_LOTTERY_TYPE;
        if(str.startsWith("200 ")) {
            lotteryType = ConstantUtil.OLD_LOTTERY_TYPE;
        }
        
        System.out.println("str"+str);
        System.out.println("typeStr"+typeStr.length());
        System.out.println("lotteryType "+lotteryType);
    }
    
    private static void test(String... str) {
        for(String s : str) {
            System.out.println(s);
        }
    }
}
