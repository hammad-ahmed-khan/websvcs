package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposAddrbookEntry.findAll",
                             query = "select o from OmsOrposAddrbookEntry o"),
                 @NamedQuery(name = "OmsOrposAddrbookEntry.findByOmsOrposCustOrdId",
                             query = "select o from OmsOrposAddrbookEntry o where o.omsOrposCustOrderId=:omsOrposCustOrderId")})
@Table(name = "OMS_ORPOS_ADDRBOOK_ENTRY")
@IdClass(OmsOrposAddrbookEntryPK.class)
public class OmsOrposAddrbookEntry implements Serializable {
    @Column(name = "ADDR_ID")
    private BigDecimal addrId;
    @Column(name = "ADDR_TYPE", length = 5)
    private String addrType;
    @Id
    @Column(name = "ADDRBOOK_ENTRY_SEQ", nullable = false)
    private BigDecimal addrbookEntrySeq;
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerId;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PRIMARY_ADDR_IND", length = 1)
    private String primaryAddrInd;

    public OmsOrposAddrbookEntry() {
    }

    public OmsOrposAddrbookEntry(BigDecimal addrId, String addrType, BigDecimal addrbookEntrySeq,
                                 BigDecimal customerId, BigDecimal omsOrposCustOrderId, String primaryAddrInd) {
        this.addrId = addrId;
        this.addrType = addrType;
        this.addrbookEntrySeq = addrbookEntrySeq;
        this.customerId = customerId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.primaryAddrInd = primaryAddrInd;
    }

    public BigDecimal getAddrId() {
        return addrId;
    }

    public void setAddrId(BigDecimal addrId) {
        this.addrId = addrId;
    }

    public String getAddrType() {
        return addrType;
    }

    public void setAddrType(String addrType) {
        this.addrType = addrType;
    }

    public BigDecimal getAddrbookEntrySeq() {
        return addrbookEntrySeq;
    }

    public void setAddrbookEntrySeq(BigDecimal addrbookEntrySeq) {
        this.addrbookEntrySeq = addrbookEntrySeq;
    }

    public BigDecimal getCustomerId() {
        return customerId;
    }

    public void setCustomerId(BigDecimal customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public String getPrimaryAddrInd() {
        return primaryAddrInd;
    }

    public void setPrimaryAddrInd(String primaryAddrInd) {
        this.primaryAddrInd = primaryAddrInd;
    }
}
