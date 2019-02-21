/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyurumi.fb_bot_boilerplate.models.send;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Kyawswa
 */
public class QuickReply {
    @SerializedName("content_type")
    public String contentType;
    @SerializedName("title")
    public String title;
    @SerializedName("payload")
    public String payload;
    @SerializedName("image_url")
    public String imageUrl;

    public QuickReply(String contentType, String title, String payload, String imageUrl) {
        this.contentType = contentType;
        this.title = title;
        this.payload = payload;
        this.imageUrl = imageUrl;
    }
    
    public QuickReply() {}
}
