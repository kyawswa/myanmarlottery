/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hyurumi.fb_bot_boilerplate.models.send;

import com.google.gson.annotations.SerializedName;
import com.hyurumi.fb_bot_boilerplate.models.common.Recipient;

/**
 *
 * @author Kyawswa
 */
public class MessageWrapper {
    public final Recipient recipient;
    public final Message message;
    
    @SerializedName("sender_action")
    public final String senderAction;
    
    public MessageWrapper(String recipientId, Message message) {
        this.recipient = new Recipient(recipientId);
        this.message = message;
        this.senderAction = null;
    }
    
    public MessageWrapper(String recipientId, String senderAction) {
        this.recipient = new Recipient(recipientId);
        this.message = null;
        this.senderAction = senderAction;
    }
}
