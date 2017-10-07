/** 
 * The MIT License
 *
 * Copyright 2017 kyawswaraung
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
