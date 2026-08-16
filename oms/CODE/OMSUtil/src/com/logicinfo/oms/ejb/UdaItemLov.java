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
@NamedQueries( { @NamedQuery(name = "UdaItemLov.findAll", query = "select o from UdaItemLov o"),
                 @NamedQuery(name = "UdaItemLov.findItemAndUdaValue", query = "select o.udaValue from UdaItemLov o where o.udaId=:udaId and o.item=:item")})
@Table(name = "UDA_ITEM_LOV")
@IdClass(UdaItemLovPK.class)
public class UdaItemLov implements Serializable {
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME")
    private Date createDatetime;
    @Column(name = "CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Id
    @Column(nullable = false, length = 25)
    private String item;
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_UPDATE_DATETIME", nullable = false)
    private Date lastUpdateDatetime;
    @Column(name = "LAST_UPDATE_ID", nullable = false, length = 30)
    private String lastUpdateId;
    @Id
    @Column(name = "UDA_ID", nullable = false)
    private BigDecimal udaId;
    @Id
    @Column(name = "UDA_VALUE", nullable = false)
    private BigDecimal udaValue;

    public UdaItemLov() {
    }

    public UdaItemLov(Date createDatetime, String createId, String item, Date lastUpdateDatetime, String lastUpdateId,
                      BigDecimal udaId, BigDecimal udaValue) {
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.item = item;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lastUpdateId = lastUpdateId;
        this.udaId = udaId;
        this.udaValue = udaValue;
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

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Date getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Date lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public String getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(String lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
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
}
