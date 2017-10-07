/**
 The MIT License (MIT)

Copyright (c) 2016 Furumi Genki

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 */

package com.hyurumi.fb_bot_boilerplate.models.send;

import com.google.gson.annotations.SerializedName;
import com.hyurumi.fb_bot_boilerplate.models.common.Action;

/**
 * Created by genki.furumi on 4/15/16.
 */
public class Button {
    private enum Type {
        @SerializedName("postback")
        Postback,
        @SerializedName("web_url")
        WebUrl
    }
    private final Type type;
    private final String title;
    private final Action payload;
    private final String url;
    private final boolean messenger_extensions;

    private Button(Type type, String title, String url, Action action){
        this.type = type;
        this.title = title;
        this.url = url;
        this.payload = action;
        this.messenger_extensions = false;
    }
    
    private Button(Type type, String title, String url, boolean messenger_extensions){
        this.type = type;
        this.title = title;
        this.url = url;
        this.payload = null;
        this.messenger_extensions = messenger_extensions;
    }

    public static Button Url(String title, String url){
        return new Button(Type.WebUrl, title, url, null);
    }

    public static Button Postback(String title, Action action){
        return new Button(Type.Postback, title, null, action);
    }
    
    public static Button Url(String title, String url, boolean messenger_extensions){
        return new Button(Type.WebUrl, title, url, messenger_extensions);
    }
}
