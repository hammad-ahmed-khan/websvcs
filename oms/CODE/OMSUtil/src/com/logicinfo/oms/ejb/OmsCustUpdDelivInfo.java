package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@NamedQueries( { @NamedQuery(name = "OmsCustUpdDelivInfo.findAll", query = "select o from OmsCustUpdDelivInfo o") })
@Table(name = "OMS_CUST_UPD_DELIV_INFO")
public class OmsCustUpdDelivInfo implements Serializable {
    @Column(name = "APPLICATION_ID", nullable = false, length = 30)
    private String applicationId;
    @Column(name = "BILL_ADD1", length = 240)
    private String billAdd1;
    @Column(name = "BILL_ADD2", length = 240)
    private String billAdd2;
    @Column(name = "BILL_ADD3", length = 240)
    private String billAdd3;
    @Column(name = "BILL_COMPANY_NAME", length = 120)
    private String billCompanyName;
    @Column(name = "BILL_COUNTRY_CODE", length = 3)
    private String billCountryCode;
    @Column(name = "BILL_LAST_NAME", length = 120)
    private String billLastName;
    @Column(name = "BILL_PHONE", length = 20)
    private String billPhone;
    @Column(name = "BILL_POST", length = 30)
    private String billPost;
    @Column(name = "BILL_STATE", length = 3)
    private String billState;
    @Column(name = "CARRIER_CODE", length = 4)
    private String carrierCode;
    @Column(name = "CARRIER_SERVICE_CODE", length = 6)
    private String carrierServiceCode;
    @Column(name = "CONSUMER_DELIVERY_DATE")
    private Timestamp consumerDeliveryDate;
    @Column(name = "CONSUMER_DELIVERY_TIME")
    private Timestamp consumerDeliveryTime;
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name = "CUST_NO", nullable = false)
    private BigDecimal custNo;
    @Column(name = "CUSTOMER_ORDER_NO", nullable = false, length = 48)
    private String customerOrderNo;
    @Column(name = "DELIVERY_ADD1", length = 240)
    private String deliveryAdd1;
    @Column(name = "DELIVERY_ADD2", length = 240)
    private String deliveryAdd2;
    @Column(name = "DELIVERY_ADD3", length = 240)
    private String deliveryAdd3;
    @Column(name = "DELIVERY_APPT", length = 250)
    private String deliveryAppt;
    @Column(name = "DELIVERY_AREA", length = 120)
    private String deliveryArea;
    @Column(name = "DELIVERY_CITY", length = 120)
    private String deliveryCity;
    @Column(name = "DELIVERY_COMMENTS", length = 250)
    private String deliveryComments;
    @Column(name = "DELIVERY_COMPANY_NAME", length = 120)
    private String deliveryCompanyName;
    @Column(name = "DELIVERY_COUNTRY_CODE", length = 3)
    private String deliveryCountryCode;
    @Column(name = "DELIVERY_FIRST_NAME", length = 120)
    private String deliveryFirstName;
    @Column(name = "DELIVERY_LANDMARK", length = 250)
    private String deliveryLandmark;
    @Column(name = "DELIVERY_LAST_NAME", length = 120)
    private String deliveryLastName;
    @Column(name = "DELIVERY_NBR", length = 120)
    private String deliveryNbr;
    @Column(name = "DELIVERY_PHONE", length = 20)
    private String deliveryPhone;
    @Column(name = "DELIVERY_POST", length = 30)
    private String deliveryPost;
    @Column(name = "DELIVERY_SLOT", length = 120)
    private String deliverySlot;
    @Column(name = "DELIVERY_STATE", length = 3)
    private String deliveryState;
    @Column(name = "DELIVERY_WINDOW", length = 120)
    private String deliveryWindow;
    @Column(name = "DELIVERY_ZONE", length = 120)
    private String deliveryZone;
    @Column(name = "FULFILL_ORDER_NO", nullable = false)
    private BigDecimal fulfillOrderNo;
    @Id
    @Column(name = "OMS_DELIVERY_ID", nullable = false)
    @SequenceGenerator( name = "omsDeliveryIdSeq", sequenceName = "OMS_DELIVERY_ID_SEQ", allocationSize = 1, initialValue = 1 ) 
         @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsDeliveryIdSeq" )
    private BigDecimal omsDeliveryId;
    @Column(name = "PUB_IND", nullable = false)
    private BigDecimal pubInd;
    @Column(name = "SUB_CUSTOMER_ORDER_NO", nullable = false, length = 3)
    private String subCustomerOrderNo;

    public OmsCustUpdDelivInfo() {
    }

    public OmsCustUpdDelivInfo(String applicationId, String billAdd1, String billAdd2, String billAdd3,
                               String billCompanyName, String billCountryCode, String billLastName, String billPhone,
                               String billPost, String billState, String carrierCode, String carrierServiceCode,
                               Timestamp consumerDeliveryDate, Timestamp consumerDeliveryTime,
                               Timestamp createDatetime, BigDecimal custNo, String customerOrderNo,
                               String deliveryAdd1, String deliveryAdd2, String deliveryAdd3, String deliveryAppt,
                               String deliveryArea, String deliveryCity, String deliveryComments,
                               String deliveryCompanyName, String deliveryCountryCode, String deliveryFirstName,
                               String deliveryLandmark, String deliveryLastName, String deliveryNbr,
                               String deliveryPhone, String deliveryPost, String deliverySlot, String deliveryState,
                               String deliveryWindow, String deliveryZone, BigDecimal fulfillOrderNo,
                               BigDecimal omsDeliveryId, BigDecimal pubInd, String subCustomerOrderNo) {
        this.applicationId = applicationId;
        this.billAdd1 = billAdd1;
        this.billAdd2 = billAdd2;
        this.billAdd3 = billAdd3;
        this.billCompanyName = billCompanyName;
        this.billCountryCode = billCountryCode;
        this.billLastName = billLastName;
        this.billPhone = billPhone;
        this.billPost = billPost;
        this.billState = billState;
        this.carrierCode = carrierCode;
        this.carrierServiceCode = carrierServiceCode;
        this.consumerDeliveryDate = consumerDeliveryDate;
        this.consumerDeliveryTime = consumerDeliveryTime;
        this.createDatetime = createDatetime;
        this.custNo = custNo;
        this.customerOrderNo = customerOrderNo;
        this.deliveryAdd1 = deliveryAdd1;
        this.deliveryAdd2 = deliveryAdd2;
        this.deliveryAdd3 = deliveryAdd3;
        this.deliveryAppt = deliveryAppt;
        this.deliveryArea = deliveryArea;
        this.deliveryCity = deliveryCity;
        this.deliveryComments = deliveryComments;
        this.deliveryCompanyName = deliveryCompanyName;
        this.deliveryCountryCode = deliveryCountryCode;
        this.deliveryFirstName = deliveryFirstName;
        this.deliveryLandmark = deliveryLandmark;
        this.deliveryLastName = deliveryLastName;
        this.deliveryNbr = deliveryNbr;
        this.deliveryPhone = deliveryPhone;
        this.deliveryPost = deliveryPost;
        this.deliverySlot = deliverySlot;
        this.deliveryState = deliveryState;
        this.deliveryWindow = deliveryWindow;
        this.deliveryZone = deliveryZone;
        this.fulfillOrderNo = fulfillOrderNo;
        this.omsDeliveryId = omsDeliveryId;
        this.pubInd = pubInd;
        this.subCustomerOrderNo = subCustomerOrderNo;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getBillAdd1() {
        return billAdd1;
    }

    public void setBillAdd1(String billAdd1) {
        this.billAdd1 = billAdd1;
    }

    public String getBillAdd2() {
        return billAdd2;
    }

    public void setBillAdd2(String billAdd2) {
        this.billAdd2 = billAdd2;
    }

    public String getBillAdd3() {
        return billAdd3;
    }

    public void setBillAdd3(String billAdd3) {
        this.billAdd3 = billAdd3;
    }

    public String getBillCompanyName() {
        return billCompanyName;
    }

    public void setBillCompanyName(String billCompanyName) {
        this.billCompanyName = billCompanyName;
    }

    public String getBillCountryCode() {
        return billCountryCode;
    }

    public void setBillCountryCode(String billCountryCode) {
        this.billCountryCode = billCountryCode;
    }

    public String getBillLastName() {
        return billLastName;
    }

    public void setBillLastName(String billLastName) {
        this.billLastName = billLastName;
    }

    public String getBillPhone() {
        return billPhone;
    }

    public void setBillPhone(String billPhone) {
        this.billPhone = billPhone;
    }

    public String getBillPost() {
        return billPost;
    }

    public void setBillPost(String billPost) {
        this.billPost = billPost;
    }

    public String getBillState() {
        return billState;
    }

    public void setBillState(String billState) {
        this.billState = billState;
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

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public BigDecimal getCustNo() {
        return custNo;
    }

    public void setCustNo(BigDecimal custNo) {
        this.custNo = custNo;
    }

    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    public String getDeliveryAdd1() {
        return deliveryAdd1;
    }

    public void setDeliveryAdd1(String deliveryAdd1) {
        this.deliveryAdd1 = deliveryAdd1;
    }

    public String getDeliveryAdd2() {
        return deliveryAdd2;
    }

    public void setDeliveryAdd2(String deliveryAdd2) {
        this.deliveryAdd2 = deliveryAdd2;
    }

    public String getDeliveryAdd3() {
        return deliveryAdd3;
    }

    public void setDeliveryAdd3(String deliveryAdd3) {
        this.deliveryAdd3 = deliveryAdd3;
    }

    public String getDeliveryAppt() {
        return deliveryAppt;
    }

    public void setDeliveryAppt(String deliveryAppt) {
        this.deliveryAppt = deliveryAppt;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryComments() {
        return deliveryComments;
    }

    public void setDeliveryComments(String deliveryComments) {
        this.deliveryComments = deliveryComments;
    }

    public String getDeliveryCompanyName() {
        return deliveryCompanyName;
    }

    public void setDeliveryCompanyName(String deliveryCompanyName) {
        this.deliveryCompanyName = deliveryCompanyName;
    }

    public String getDeliveryCountryCode() {
        return deliveryCountryCode;
    }

    public void setDeliveryCountryCode(String deliveryCountryCode) {
        this.deliveryCountryCode = deliveryCountryCode;
    }

    public String getDeliveryFirstName() {
        return deliveryFirstName;
    }

    public void setDeliveryFirstName(String deliveryFirstName) {
        this.deliveryFirstName = deliveryFirstName;
    }

    public String getDeliveryLandmark() {
        return deliveryLandmark;
    }

    public void setDeliveryLandmark(String deliveryLandmark) {
        this.deliveryLandmark = deliveryLandmark;
    }

    public String getDeliveryLastName() {
        return deliveryLastName;
    }

    public void setDeliveryLastName(String deliveryLastName) {
        this.deliveryLastName = deliveryLastName;
    }

    public String getDeliveryNbr() {
        return deliveryNbr;
    }

    public void setDeliveryNbr(String deliveryNbr) {
        this.deliveryNbr = deliveryNbr;
    }

    public String getDeliveryPhone() {
        return deliveryPhone;
    }

    public void setDeliveryPhone(String deliveryPhone) {
        this.deliveryPhone = deliveryPhone;
    }

    public String getDeliveryPost() {
        return deliveryPost;
    }

    public void setDeliveryPost(String deliveryPost) {
        this.deliveryPost = deliveryPost;
    }

    public String getDeliverySlot() {
        return deliverySlot;
    }

    public void setDeliverySlot(String deliverySlot) {
        this.deliverySlot = deliverySlot;
    }

    public String getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(String deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDeliveryWindow() {
        return deliveryWindow;
    }

    public void setDeliveryWindow(String deliveryWindow) {
        this.deliveryWindow = deliveryWindow;
    }

    public String getDeliveryZone() {
        return deliveryZone;
    }

    public void setDeliveryZone(String deliveryZone) {
        this.deliveryZone = deliveryZone;
    }

    public BigDecimal getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    public void setFulfillOrderNo(BigDecimal fulfillOrderNo) {
        this.fulfillOrderNo = fulfillOrderNo;
    }

    public BigDecimal getOmsDeliveryId() {
        return omsDeliveryId;
    }

    public void setOmsDeliveryId(BigDecimal omsDeliveryId) {
        this.omsDeliveryId = omsDeliveryId;
    }

    public BigDecimal getPubInd() {
        return pubInd;
    }

    public void setPubInd(BigDecimal pubInd) {
        this.pubInd = pubInd;
    }

    public String getSubCustomerOrderNo() {
        return subCustomerOrderNo;
    }

    public void setSubCustomerOrderNo(String subCustomerOrderNo) {
        this.subCustomerOrderNo = subCustomerOrderNo;
    }
}
