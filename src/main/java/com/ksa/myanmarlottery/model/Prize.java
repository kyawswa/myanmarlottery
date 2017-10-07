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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author Kyawswa
 */
@Entity
@NamedQueries({
        @NamedQuery(
            name = "prize.findByLatestResult",
            query = "from Prize p where "
                    + "p.result.type = :type and "
                    + "p.code = :code and p.point in (:points))"
        ),
        @NamedQuery(
            name = "prize.findByCode",
            query = "from Prize p where "
                    + "p.result.type = :type and "
                    + "p.codeIndex >= :minIndex and p.codeIndex <= :maxIndex and (p.point in (:points))"
        ),
        @NamedQuery(
            name = "prize.findByPoints",
            query = "from Prize p where "
                    + "p.result.type = :type and "
                    + "p.codeIndex = :codeIndex and "
                    + "((p.point >= :startPoint and p.point <= :endPoint) OR"
                    + "((p.point >= :sFivePoint and p.point <= :eFivePoint)) OR "
                    + "(p.point >= :sFourPoint and p.point <= :eFourPoint) OR"
                    + "(p.point >= :sThreePoint and p.point <= :eThreePoint) OR"
                    + "(p.point >= :sTwoPoint and p.point <= :eTwoPoint))"
        )
    })
public class Prize implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "PRIZE_ID")
    private int id;
    
    private String title;
    
    // ab
    private String code;
    //123456
    private int point;
    
    private int codeIndex;
    
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "ID")
    private Result result;
    
    public Prize() {}

    public Prize(String code, int point, String title, String description) {
        this.title = title;
        this.code = code;
        this.description = description;
        this.point = point;
    }
    
    public Prize(String codePoint, String title, String description) {
        this.title = title;
        this.description = description;
        
        String[] ar = codePoint.split("-");
        this.code = ar[0];
        this.point = new Integer(ar[1]);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public int getCodeIndex() {
        return codeIndex;
    }

    public void setCodeIndex(int codeIndex) {
        this.codeIndex = codeIndex;
    }

    
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getCodePoint() {
        return this.code+"-"+this.point;
    }
    
    @Override
    public String toString() {
        return "Prize{" + "id=" + id + ", title=" + title +  ", description=" + description + ", code=" + code + " point="+ point+ " codeIndex="+ codeIndex +'}';
    }
}
