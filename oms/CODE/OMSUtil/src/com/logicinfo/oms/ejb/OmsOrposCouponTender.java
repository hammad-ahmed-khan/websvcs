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
@NamedQueries( { @NamedQuery(name = "OmsOrposCouponTender.findAll", query = "select o from OmsOrposCouponTender o") })
@Table(name = "OMS_ORPOS_COUPON_TENDER")
public class OmsOrposCouponTender implements Serializable {
    @Id
    @Column(name = "COUPON_NUMBER", nullable = false, length = 15)
    private String couponNumber;
    @Column(name = "COUPON_TYPE", length = 12)
    private String couponType;
    @Column(name = "ENTRY_METHOD", length = 7)
    private String entryMethod;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;

    public OmsOrposCouponTender() {
    }

    public OmsOrposCouponTender(String couponNumber, String couponType, String entryMethod,
                                BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo) {
        this.couponNumber = couponNumber;
        this.couponType = couponType;
        this.entryMethod = entryMethod;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
    }

    public String getCouponNumber() {
        return couponNumber;
    }

    public void setCouponNumber(String couponNumber) {
        this.couponNumber = couponNumber;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public String getEntryMethod() {
        return entryMethod;
    }

    public void setEntryMethod(String entryMethod) {
        this.entryMethod = entryMethod;
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
}
