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
@NamedQueries( { @NamedQuery(name = "OmsVirtualStrMatrix.findAll", query = "select o from OmsVirtualStrMatrix o"),
                 @NamedQuery(name = "OmsVirtualStrMatrix.findVirtualLocID",
                             query = "select o.virtualLocId from OmsVirtualStrMatrix o where o.locId=:locID and o.locType=:locationType") })
@Table(name = "OMS_VIRTUAL_STR_MATRIX")
public class OmsVirtualStrMatrix implements Serializable {
    @Column(name = "CHANNEL_ID", nullable = false)
    private BigDecimal channelId;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Id
    @Column(name = "LOC_ID")
    private BigDecimal locId;
    @Column(name = "LOC_TYPE", length = 2)
    private String locType;
    @Column(name = "VIRTUAL_LOC_ID", nullable = false)
    private BigDecimal virtualLocId;

    public OmsVirtualStrMatrix() {
    }

    public OmsVirtualStrMatrix(BigDecimal channelId, Timestamp createDatetime, BigDecimal locId, String locType,
                               BigDecimal virtualLocId) {
        this.channelId = channelId;
        this.createDatetime = createDatetime;
        this.locId = locId;
        this.locType = locType;
        this.virtualLocId = virtualLocId;
    }

    public BigDecimal getChannelId() {
        return channelId;
    }

    public void setChannelId(BigDecimal channelId) {
        this.channelId = channelId;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getLocId() {
        return locId;
    }

    public void setLocId(BigDecimal locId) {
        this.locId = locId;
    }

    public String getLocType() {
        return locType;
    }

    public void setLocType(String locType) {
        this.locType = locType;
    }

    public BigDecimal getVirtualLocId() {
        return virtualLocId;
    }

    public void setVirtualLocId(BigDecimal virtualLocId) {
        this.virtualLocId = virtualLocId;
    }
}
