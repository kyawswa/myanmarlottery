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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by genki.furumi on 4/14/16.
 */
public class Payload {
    @SerializedName("template_type")
    final private String templateType;
    final private String text;
    final private String url;
    final private List<Element> elements;
    final private List<Button> buttons;

    private Payload(String type, String text, String url, List<Element> elements, List<Button> buttons){
        this.templateType = type;
        this.text = text;
        this.url = url;
        this.elements = elements;
        this.buttons = buttons;
    }

    public static Payload Button(String text){
        return new Payload("button", text, null, null, new ArrayList<>());
    }
    
    public static Payload Generic(){
        return new Payload("generic", null, null, new ArrayList<>(), null);
    }

    public static Payload Image(String url) {
        return new Payload(null, null, url, null, null);

    }

    public boolean addButton(Button button) {
        if (buttons != null) {
            return buttons.add(button);
        }else {
            return false;
        }
    }

    public boolean addElement(Element element) {
        if (elements != null) {
            return elements.add(element);
        }else {
            return false;
        }
    }
}
