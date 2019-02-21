/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.dto;

/**
 *
 * @author Kyawswa
 */
public class GetPrizeDTO {
 
    private int numberOfTimes;
    private String result;
    private String code;
    private String description;

    public GetPrizeDTO() {
    }

    public GetPrizeDTO(int numberOfTimes, String result, String code, String description) {
        this.numberOfTimes = numberOfTimes;
        this.result = result;
        this.code = code;
        this.description = description;
    }
    
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getNumberOfTimes() {
        return numberOfTimes;
    }

    public void setNumberOfTimes(int numberOfTimes) {
        this.numberOfTimes = numberOfTimes;
    }

    @Override
    public String toString() {
        return "GetPrizeDTO{" + "result=" + result + ", code=" + code + ", description=" + description + '}';
    }
}
