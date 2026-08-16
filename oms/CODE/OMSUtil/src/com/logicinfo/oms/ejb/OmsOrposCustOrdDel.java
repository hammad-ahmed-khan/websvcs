package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposCustOrdDel.findAll", query = "select o from OmsOrposCustOrdDel o") ,
                 @NamedQuery(name = "OmsOrposCustOrdDel.findByOmsOrposCustOrdId", query = "select o from OmsOrposCustOrdDel o where o.omsOrposCustOrderId=:omsOrposCustOrderId")})
@Table(name = "OMS_ORPOS_CUST_ORD_DEL")
@IdClass(OmsOrposCustOrdDelPK.class)
public class OmsOrposCustOrdDel implements Serializable {
    @Column(name = "CARRIER_CODE", length = 4)
    private String carrierCode;
    @Column(name = "CARRIER_SERVICE_CODE", length = 6)
    private String carrierServiceCode;
    @Column(name = "CARRIER_TRACKING_NO", length = 120)
    private String carrierTrackingNo;
    @Id
    @Column(name = "CUST_ORD_DEL_ID", nullable = false)
    private BigDecimal custOrdDelId;
    @Id
    @Column(name = "CUST_ORD_DEL_SEQ_NO", nullable = false)
    private BigDecimal custOrdDelSeqNo;
    @Column(name = "CUSTOMER_ID", length = 20)
    private String customerId;
    @Column(name = "CUSTOMER_SIGNATURE", length = 500)
    private String customerSignature;
    @Column(name = "DELIVERY_DATE", nullable = false)
    private Timestamp deliveryDate;
    @Column(name = "DELIVERY_LOC_ID", nullable = false)
    private BigDecimal deliveryLocId;
    @Column(name = "DELIVERY_LOC_TYPE", length = 1)
    private String deliveryLocType;
    @Column(name = "DELIVERY_TIME")
    private Timestamp deliveryTime;
    @Column(name = "DELIVERY_TYPE", length = 1)
    private String deliveryType;
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID")
    private BigDecimal omsOrposCustOrderId;

    public OmsOrposCustOrdDel() {
    }

    public OmsOrposCustOrdDel(String carrierCode, String carrierServiceCode, String carrierTrackingNo,
                              BigDecimal custOrdDelId, BigDecimal custOrdDelSeqNo, String customerId,
                              String customerSignature, Timestamp deliveryDate, BigDecimal deliveryLocId,
                              String deliveryLocType, Timestamp deliveryTime, String deliveryType,
                              BigDecimal omsOrposCustOrderId) {
        this.carrierCode = carrierCode;
        this.carrierServiceCode = carrierServiceCode;
        this.carrierTrackingNo = carrierTrackingNo;
        this.custOrdDelId = custOrdDelId;
        this.custOrdDelSeqNo = custOrdDelSeqNo;
        this.customerId = customerId;
        this.customerSignature = customerSignature;
        this.deliveryDate = deliveryDate;
        this.deliveryLocId = deliveryLocId;
        this.deliveryLocType = deliveryLocType;
        this.deliveryTime = deliveryTime;
        this.deliveryType = deliveryType;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }

    public String getCarrierServiceCode() {
        return carrierServiceCode;
    }

    public void setCarrierServiceCode(String carrierServiceCode) {
        this.carrierServiceCode = carrierServiceCode;
    }

    public String getCarrierTrackingNo() {
        return carrierTrackingNo;
    }

    public void setCarrierTrackingNo(String carrierTrackingNo) {
        this.carrierTrackingNo = carrierTrackingNo;
    }

    public BigDecimal getCustOrdDelId() {
        return custOrdDelId;
    }

    public void setCustOrdDelId(BigDecimal custOrdDelId) {
        this.custOrdDelId = custOrdDelId;
    }

    public BigDecimal getCustOrdDelSeqNo() {
        return custOrdDelSeqNo;
    }

    public void setCustOrdDelSeqNo(BigDecimal custOrdDelSeqNo) {
        this.custOrdDelSeqNo = custOrdDelSeqNo;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerSignature() {
        return customerSignature;
    }

    public void setCustomerSignature(String customerSignature) {
        this.customerSignature = customerSignature;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public BigDecimal getDeliveryLocId() {
        return deliveryLocId;
    }

    public void setDeliveryLocId(BigDecimal deliveryLocId) {
        this.deliveryLocId = deliveryLocId;
    }

    public String getDeliveryLocType() {
        return deliveryLocType;
    }

    public void setDeliveryLocType(String deliveryLocType) {
        this.deliveryLocType = deliveryLocType;
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
