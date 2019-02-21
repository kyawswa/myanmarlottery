/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.util;

import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 *
 * @author Kyawswa
 */
@Component
@PropertySource("classpath:code_point.properties")
public class CharacterMap {
    @Value("#{PropertySplitter.map('${character.codes}')}")
    private Map<String, String> characterAsMap;
    // get myanmar character from map which is already load from resource.
    public String get(String key) {
        return characterAsMap.get(key);
    }
    
    public int getSize() {
        return characterAsMap.size();
    }
    
    public Map<String, String> getMap() {
        return characterAsMap;
    }
}
