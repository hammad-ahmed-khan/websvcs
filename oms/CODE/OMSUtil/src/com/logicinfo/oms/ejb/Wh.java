package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "WH")
@NamedQueries( { @NamedQuery(name = "Wh.findAll", query = "select o from Wh o"),
                 @NamedQuery(name = "Wh.findByPhysicalWH",
                             query = "select o.wh from Wh o where o.physicalWH=:physicalWH"),
                @NamedQuery(name = "Wh.findPhysicalWH",
                             query = "select o.physicalWH,o.channelId from Wh o where o.wh=:wh"),
               @NamedQuery(name = "Wh.findVirtualWh",
                             query = "select o.wh from Wh o where o.physicalWH=:physicalWH and o.channelId=:channelId"),
                                @NamedQuery(name = "Wh.findPhyWhForVirtualWh",
                             query = "select o.physicalWH from Wh o where o.wh=:wh ")})
public class Wh implements Serializable {
    public Wh() {
    }
    @Id
    @Column(name = "WH", nullable = false)
    BigDecimal wh;
    @Column(name = "PHYSICAL_WH", nullable = false)
    BigDecimal physicalWH;
    @Column(name = "CHANNEL_ID", nullable = false)
    BigDecimal channelId;

    public void setWh(BigDecimal wh) {
        this.wh = wh;
    }

    public BigDecimal getWh() {
        return wh;
    }

    public void setPhysicalWH(BigDecimal physicalWH) {
        this.physicalWH = physicalWH;
    }

    public BigDecimal getPhysicalWH() {
        return physicalWH;
    }

    public void setChannelId(BigDecimal channelId) {
        this.channelId = channelId;
    }

    public BigDecimal getChannelId() {
        return channelId;
    }
}
