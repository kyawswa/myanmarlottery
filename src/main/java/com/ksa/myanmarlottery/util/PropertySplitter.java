/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.util;

import com.google.common.base.Splitter;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kyawswa
 */
@Component("PropertySplitter")
public class PropertySplitter {
    /**
     * Example: one.example.property = KEY1:VALUE1,KEY2:VALUE2
     */
    public Map<String, String> map(String property) {
        return this.map(property, ",");
    }

    private Map<String, String> map(String property, String splitter) {
        return Splitter.on(splitter).omitEmptyStrings().trimResults().withKeyValueSeparator(":").split(property);
    }

}
