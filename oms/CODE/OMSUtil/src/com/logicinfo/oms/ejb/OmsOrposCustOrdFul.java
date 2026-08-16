package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposCustOrdFul.findAll", query = "select o from OmsOrposCustOrdFul o"),
                 @NamedQuery(name = "OmsOrposCustOrdFul.findByOmsOrposCustOrdId", query = "select o from OmsOrposCustOrdFul o where o.omsOrposCustOrderId=:omsOrposCustOrderId")})
@Table(name = "OMS_ORPOS_CUST_ORD_FUL")
@IdClass(OmsOrposCustOrdFulPK.class)
public class OmsOrposCustOrdFul implements Serializable {
    @Column(name = "CARRIER_CODE", length = 4)
    private String carrierCode;
    @Column(name = "CARRIER_SERVICE_CODE", length = 6)
    private String carrierServiceCode;
    @Column(length = 200)
    private String comments;
    @Column(name = "CONSUMER_DELIVERY_DATE")
    private Timestamp consumerDeliveryDate;
    @Column(name = "CONSUMER_DELIVERY_TIME")
    private Timestamp consumerDeliveryTime;
    @Id
    @Column(name = "CUST_ORD_FUL_SEQ_NO", nullable = false)    
   
    private BigDecimal custOrdFulSeqNo;
    @Column(name = "DELIVERY_TYPE", nullable = false, length = 1)
    private String deliveryType;
    @Column(name = "FULFILL_LOC_ID")
    private BigDecimal fulfillLocId;
    @Column(name = "FULFILL_LOC_TYPE", length = 1)
    private String fulfillLocType;
    @Column(name = "FULFILL_ORDER_ID")
    private BigDecimal fulfillOrderId;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PARTIAL_DELIVERY_IND", length = 1)
    private String partialDeliveryInd;
    @Column(name = "SHIP_TO_FULFILL_LOC_FLAG", length = 1)
    private String shipToFulfillLocFlag;

    public OmsOrposCustOrdFul() {
    }

    public OmsOrposCustOrdFul(String carrierCode, String carrierServiceCode, String comments,
                              Timestamp consumerDeliveryDate, Timestamp consumerDeliveryTime,
                              BigDecimal custOrdFulSeqNo, String deliveryType, BigDecimal fulfillLocId,
                              String fulfillLocType, BigDecimal fulfillOrderId, BigDecimal omsOrposCustOrderId,
                              String partialDeliveryInd, String shipToFulfillLocFlag) {
        this.carrierCode = carrierCode;
        this.carrierServiceCode = carrierServiceCode;
        this.comments = comments;
        this.consumerDeliveryDate = consumerDeliveryDate;
        this.consumerDeliveryTime = consumerDeliveryTime;
        this.custOrdFulSeqNo = custOrdFulSeqNo;
        this.deliveryType = deliveryType;
        this.fulfillLocId = fulfillLocId;
        this.fulfillLocType = fulfillLocType;
        this.fulfillOrderId = fulfillOrderId;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.partialDeliveryInd = partialDeliveryInd;
        this.shipToFulfillLocFlag = shipToFulfillLocFlag;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getConsumerDeliveryDate() {
        return consumerDeliveryDate;
    }

    public void setConsumerDeliveryDate(Timestamp consumerDeliveryDate) {
        this.consumerDeliveryDate = consumerDeliveryDate;
    }

    public Timestamp getConsumerDeliveryTime() {
        return consumerDeliveryTime;
    }

    public void setConsumerDeliveryTime(Timestamp consumerDeliveryTime) {
        this.consumerDeliveryTime = consumerDeliveryTime;
    }

    public BigDecimal getCustOrdFulSeqNo() {
        return custOrdFulSeqNo;
    }

    public void setCustOrdFulSeqNo(BigDecimal custOrdFulSeqNo) {
        this.custOrdFulSeqNo = custOrdFulSeqNo;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public BigDecimal getFulfillLocId() {
        return fulfillLocId;
    }

    public void setFulfillLocId(BigDecimal fulfillLocId) {
        this.fulfillLocId = fulfillLocId;
    }

    public String getFulfillLocType() {
        return fulfillLocType;
    }

    public void setFulfillLocType(String fulfillLocType) {
        this.fulfillLocType = fulfillLocType;
    }

    public BigDecimal getFulfillOrderId() {
        return fulfillOrderId;
    }

    public void setFulfillOrderId(BigDecimal fulfillOrderId) {
        this.fulfillOrderId = fulfillOrderId;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public String getPartialDeliveryInd() {
        return partialDeliveryInd;
    }

    public void setPartialDeliveryInd(String partialDeliveryInd) {
        this.partialDeliveryInd = partialDeliveryInd;
    }

    public String getShipToFulfillLocFlag() {
        return shipToFulfillLocFlag;
    }

    public void setShipToFulfillLocFlag(String shipToFulfillLocFlag) {
        this.shipToFulfillLocFlag = shipToFulfillLocFlag;
    }
}
