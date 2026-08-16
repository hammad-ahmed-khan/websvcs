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
@NamedQueries( { @NamedQuery(name = "OmsOrposTravelCheckTender.findAll",
                             query = "select o from OmsOrposTravelCheckTender o") })
@Table(name = "OMS_ORPOS_TRAVEL_CHECK_TENDER")
public class OmsOrposTravelCheckTender implements Serializable {
    @Column(name = "CHECK_COUNT", nullable = false)
    private BigDecimal checkCount;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;
    @Id
    @Column(name = "TRAVEL_CHECK_TENDER_ID", nullable = false)
    private BigDecimal travelCheckTenderId;

    public OmsOrposTravelCheckTender() {
    }

    public OmsOrposTravelCheckTender(BigDecimal checkCount, BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo,
                                     BigDecimal travelCheckTenderId) {
        this.checkCount = checkCount;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
        this.travelCheckTenderId = travelCheckTenderId;
    }

    public BigDecimal getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(BigDecimal checkCount) {
        this.checkCount = checkCount;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getPaymentSeqNo() {
        return paymentSeqNo;
    }

    public void setPaymentSeqNo(BigDecimal paymentSeqNo) {
        this.paymentSeqNo = paymentSeqNo;
    }

    public BigDecimal getTravelCheckTenderId() {
        return travelCheckTenderId;
    }

    public void setTravelCheckTenderId(BigDecimal travelCheckTenderId) {
        this.travelCheckTenderId = travelCheckTenderId;
    }
}
