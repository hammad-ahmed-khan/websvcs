package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsCoAwbHead.findAll", query = "select o from OmsCoAwbHead o") 
                 ,@NamedQuery(name = "OmsCoAwbHead.findByKey", query = "select o from OmsCoAwbHead o where o.awbUpdReqId=:awbUpdReqId") })
@Table(name = "OMS_CO_AWB_HEAD")
public class OmsCoAwbHead implements Serializable {
    @Id
    @Column(name = "AWB_UPD_REQ_ID", nullable = false, length = 20)
    private String awbUpdReqId;

    @Column(name = "CREATE_DATETIME")
    private Timestamp createDatetime;
    @Column(name = "RECORD_COUNT", nullable = false)
    private BigDecimal recordCount;

    public OmsCoAwbHead() {
    }

    public OmsCoAwbHead(String awbUpdReqId, Timestamp createDatetime, BigDecimal recordCount) {
        this.awbUpdReqId = awbUpdReqId;
        this.createDatetime = createDatetime;
        this.recordCount = recordCount;
    }

    public String getAwbUpdReqId() {
        return awbUpdReqId;
    }

    public void setAwbUpdReqId(String awbUpdReqId) {
        this.awbUpdReqId = awbUpdReqId;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(BigDecimal recordCount) {
        this.recordCount = recordCount;
    }
}
