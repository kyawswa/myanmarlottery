/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.model;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Kyawswar
 */
@Entity
public class LogInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userID;
    private String userType; // facebook user or other.
    private String parameter;
    private String url;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public static final String FACEBOOK_USER = "Facebook User";
    public static final String WEB_CLIENT_USER = "Web Client User";
    
    public LogInfo() {}
    
    public LogInfo(Date date, String userID, String userType, String parameter, String url) {
        this.userID = userID;
        this.userType = userType;
        this.parameter = parameter;
        this.date = date;
        this.url = url;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
