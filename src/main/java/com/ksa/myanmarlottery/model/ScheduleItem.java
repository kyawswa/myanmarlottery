/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ksa.myanmarlottery.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author Kyawswa
 */
@Entity
@NamedQueries({
    @NamedQuery(
        name = "scheduleItem.findAll",
        query = "from ScheduleItem s"
    )}
)
public class ScheduleItem implements Serializable {
    
    @Id
    private String recipientID;
    
    // 500 or 200
    private int lotteryType;
    
    // query by range or specific query
    private int queryType;
    
    @ElementCollection
    @CollectionTable(name="param_table", joinColumns=@JoinColumn(name="recipient_id"))
    @Column(name="params")
    @OrderColumn(name="param_index")
    private List<String> param = new ArrayList<>();
    
    private String originParam;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    public ScheduleItem() {}

    public ScheduleItem(String recipientID, int lotteryType, int queryType, String originParam) {
        this.recipientID = recipientID;
        this.lotteryType = lotteryType;
        this.queryType = queryType;
        this.originParam = originParam;
    }

    public String getOriginParam() {
        return originParam;
    }

    public void setOriginParam(String originParam) {
        this.originParam = originParam;
    }
    
    public String getRecipientID() {
        return recipientID;
    }

    public void setRecipientID(String recipientID) {
        this.recipientID = recipientID;
    }

    public int getLotteryType() {
        return lotteryType;
    }

    public void setLotteryType(int lotteryType) {
        this.lotteryType = lotteryType;
    }

    public int getQueryType() {
        return queryType;
    }

    public void setQueryType(int queryType) {
        this.queryType = queryType;
    }

    public List<String> getParam() {
        return param;
    }

    public void setParam(List<String> param) {
        this.param = param;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

}
