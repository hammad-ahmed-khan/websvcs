package com.logicinfo.awb.entites;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

public class OmsCustOrdHead implements Serializable {

    private String applicationId;

    private Timestamp cancelDatetime;

    private Timestamp closeDatetime;

    private String comments;

    private Timestamp consumerDlyTime;

    private Timestamp createDatetime;

    private String custFirstName;

    private String custId;

    private String custLastName;

    private String custOrderNo;

    private String custOrderType;

    private String custPhoneNo;

    private String customerLang;

    private String deliveryType;

    private String entityId;

    private Timestamp lastUpdateDatetime;




    private BigDecimal omsCustOrdNo;

    private String ordPaymentStatus;

    private String orderCreateReserveInd;

    private BigDecimal orderRequestorId;

    private String payInStore;

    private BigDecimal pickLoc;

    private String status;

    private String subCustOrderNo;

    public OmsCustOrdHead() {
    }

    public OmsCustOrdHead(String applicationId, Timestamp cancelDatetime, Timestamp closeDatetime, String comments,
                          Timestamp consumerDlyTime, Timestamp createDatetime, String custFirstName, String custId,
                          String custLastName, String custOrderNo, String custOrderType, String custPhoneNo,
                          String customerLang, String deliveryType, String entityId, Timestamp lastUpdateDatetime,
                          BigDecimal omsCustOrdNo, String ordPaymentStatus, String orderCreateReserveInd,
                          BigDecimal orderRequestorId, String payInStore, BigDecimal pickLoc, String status,
                          String subCustOrderNo) {
        this.applicationId = applicationId;
        this.cancelDatetime = cancelDatetime;
        this.closeDatetime = closeDatetime;
        this.comments = comments;
        this.consumerDlyTime = consumerDlyTime;
        this.createDatetime = createDatetime;
        this.custFirstName = custFirstName;
        this.custId = custId;
        this.custLastName = custLastName;
        this.custOrderNo = custOrderNo;
        this.custOrderType = custOrderType;
        this.custPhoneNo = custPhoneNo;
        this.customerLang = customerLang;
        this.deliveryType = deliveryType;
        this.entityId = entityId;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.omsCustOrdNo = omsCustOrdNo;
        this.ordPaymentStatus = ordPaymentStatus;
        this.orderCreateReserveInd = orderCreateReserveInd;
        this.orderRequestorId = orderRequestorId;
        this.payInStore = payInStore;
        this.pickLoc = pickLoc;
        this.status = status;
        this.subCustOrderNo = subCustOrderNo;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public Timestamp getCancelDatetime() {
        return cancelDatetime;
    }

    public void setCancelDatetime(Timestamp cancelDatetime) {
        this.cancelDatetime = cancelDatetime;
    }

    public Timestamp getCloseDatetime() {
        return closeDatetime;
    }

    public void setCloseDatetime(Timestamp closeDatetime) {
        this.closeDatetime = closeDatetime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Timestamp getConsumerDlyTime() {
        return consumerDlyTime;
    }

    public void setConsumerDlyTime(Timestamp consumerDlyTime) {
        this.consumerDlyTime = consumerDlyTime;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCustFirstName() {
        return custFirstName;
    }

    public void setCustFirstName(String custFirstName) {
        this.custFirstName = custFirstName;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustLastName() {
        return custLastName;
    }

    public void setCustLastName(String custLastName) {
        this.custLastName = custLastName;
    }

    public String getCustOrderNo() {
        return custOrderNo;
    }

    public void setCustOrderNo(String custOrderNo) {
        this.custOrderNo = custOrderNo;
    }

    public String getCustOrderType() {
        return custOrderType;
    }

    public void setCustOrderType(String custOrderType) {
        this.custOrderType = custOrderType;
    }

    public String getCustPhoneNo() {
        return custPhoneNo;
    }

    public void setCustPhoneNo(String custPhoneNo) {
        this.custPhoneNo = custPhoneNo;
    }

    public String getCustomerLang() {
        return customerLang;
    }

    public void setCustomerLang(String customerLang) {
        this.customerLang = customerLang;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Timestamp getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Timestamp lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public BigDecimal getOmsCustOrdNo() {
        return omsCustOrdNo;
    }

    public void setOmsCustOrdNo(BigDecimal omsCustOrdNo) {
        this.omsCustOrdNo = omsCustOrdNo;
    }

    public String getOrdPaymentStatus() {
        return ordPaymentStatus;
    }

    public void setOrdPaymentStatus(String ordPaymentStatus) {
        this.ordPaymentStatus = ordPaymentStatus;
    }

    public String getOrderCreateReserveInd() {
        return orderCreateReserveInd;
    }

    public void setOrderCreateReserveInd(String orderCreateReserveInd) {
        this.orderCreateReserveInd = orderCreateReserveInd;
    }

    public BigDecimal getOrderRequestorId() {
        return orderRequestorId;
    }

    public void setOrderRequestorId(BigDecimal orderRequestorId) {
        this.orderRequestorId = orderRequestorId;
    }

    public String getPayInStore() {
        return payInStore;
    }

    public void setPayInStore(String payInStore) {
        this.payInStore = payInStore;
    }

    public BigDecimal getPickLoc() {
        return pickLoc;
    }

    public void setPickLoc(BigDecimal pickLoc) {
        this.pickLoc = pickLoc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubCustOrderNo() {
        return subCustOrderNo;
    }

    public void setSubCustOrderNo(String subCustOrderNo) {
        this.subCustOrderNo = subCustOrderNo;
    }
}
