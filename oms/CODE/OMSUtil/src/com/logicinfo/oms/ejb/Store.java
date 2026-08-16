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
@NamedQueries( { @NamedQuery(name = "Store.findAll", query = "select o from Store o"),
                 @NamedQuery(name = "Store.findOrgUnit", query = "select o.orgUnitId from Store o where o.store=:store"),
                 @NamedQuery(name = "Store.findChannelId", query = "select o.channelId from Store o where o.store=:store")})
@Table(name = "STORE")
public class Store implements Serializable {
    public Store() {
    }
    @Id
    @Column(name = "STORE", nullable = false)
    BigDecimal store;
   
    @Column(name = "ORG_UNIT_ID", nullable = false)
    private BigDecimal orgUnitId;
    @Column(name = "CHANNEL_ID", nullable = false)
      private BigDecimal channelId;

    public void setStore(BigDecimal store) {
        this.store = store;
    }

    public BigDecimal getStore() {
        return store;
    }

    public void setOrgUnitId(BigDecimal orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public BigDecimal getOrgUnitId() {
        return orgUnitId;
    }

    public void setChannelId(BigDecimal channelId) {
        this.channelId = channelId;
    }

    public BigDecimal getChannelId() {
        return channelId;
    }
}
