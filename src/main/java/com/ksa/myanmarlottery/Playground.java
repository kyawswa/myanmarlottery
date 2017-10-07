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
