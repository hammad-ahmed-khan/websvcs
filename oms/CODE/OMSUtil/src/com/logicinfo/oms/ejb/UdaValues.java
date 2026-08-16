package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "UdaValues.findAll", query = "select o from UdaValues o"),
                 @NamedQuery(name = "UdaValues.findUdaValueDesc", query = "select o.udaValueDesc from UdaValues o where o.udaId=:udaId and o.udaValue=:udaValue")})
@Table(name = "UDA_VALUES")
@IdClass(UdaValuesPK.class)
public class UdaValues implements Serializable {
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Date createDatetime;
    @Column(name = "CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Id
    @Column(name = "UDA_ID", nullable = false)
    private BigDecimal udaId;
    @Id
    @Column(name = "UDA_VALUE", nullable = false)
    private BigDecimal udaValue;
    @Column(name = "UDA_VALUE_DESC", nullable = false, length = 250)
    private String udaValueDesc;

    public UdaValues() {
    }

    public UdaValues(Date createDatetime, String createId, BigDecimal udaId, BigDecimal udaValue,
                     String udaValueDesc) {
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.udaId = udaId;
        this.udaValue = udaValue;
        this.udaValueDesc = udaValueDesc;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public BigDecimal getUdaId() {
        return udaId;
    }

    public void setUdaId(BigDecimal udaId) {
        this.udaId = udaId;
    }

    public BigDecimal getUdaValue() {
        return udaValue;
    }

    public void setUdaValue(BigDecimal udaValue) {
        this.udaValue = udaValue;
    }

    public String getUdaValueDesc() {
        return udaValueDesc;
    }

    public void setUdaValueDesc(String udaValueDesc) {
        this.udaValueDesc = udaValueDesc;
    }
}
