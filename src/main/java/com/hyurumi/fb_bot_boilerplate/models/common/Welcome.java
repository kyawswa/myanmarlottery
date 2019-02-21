/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyurumi.fb_bot_boilerplate.models.common;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Kyawswa
 */
public class Welcome {
    @SerializedName("setting_type")
    public String settingType = "greeting";
    @SerializedName("greeting")
    public Greeting greeting;

    public Welcome(String settingType, Greeting greeting) {
        this.settingType = settingType;
        this.greeting = greeting;
    }
    
    
}
