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

/**
 * Created by genki.furumi on 4/14/16.
 */
public class Attachment {

    public enum Type {
        @SerializedName("template")
        TEMPLATE,
        @SerializedName("image")
        IMAGE
    }

    final private Type type;
    final private Payload payload;

    private Attachment(Type type, Payload payload) {
        this.type = type;
        this.payload = payload;
    }

    public static Attachment Template(Payload payload) {
        return new Attachment(Type.TEMPLATE, payload);
    }

    public static Attachment Image(String url) {
        return new Attachment(Type.IMAGE, Payload.Image(url));
    }

    public boolean addElement(Element element) {
        return payload.addElement(element);
    }

    public boolean addButton(Button button) {
        return payload.addButton(button);
    }
}
