/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kyawswa
 */
public class PrizeDTO {
    private int prizeNo;
    private String title;
    private List<String> list = new ArrayList<>();

    public PrizeDTO(int prizeNo, String title) {
        this.prizeNo = prizeNo;
        this.title = title;
    }

    public PrizeDTO() {}
    
    public int getPrizeNo() {
        return prizeNo;
    }

    public void setPrizeNo(int prizeNo) {
        this.prizeNo = prizeNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
