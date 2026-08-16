package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@NamedQueries( { @NamedQuery(name = "Ordcust.findAll", 
                             query = "select o from Ordcust o"),
                 @NamedQuery(name = "Ordcust.findByFulfilOrdNo", 
                             query = "select o from Ordcust o where o.customerOrderNo=:customerOrderNo and o.fulfillOrderNo=:fulfillOrderNo and o.sourceLocId is not NULL and o.sourceLocId=:sourceLocId and o.fulfillLocId=:fulfillLocId"),
                 @NamedQuery(name = "Ordcust.count", 
                             query = "select count(o) from Ordcust o where o.customerOrderNo=:customerOrderNo"),
                 @NamedQuery(name = "Ordcust.findmaxfulfilordNo", 
                             query = "select max(o.fulfillOrderNo) from Ordcust o where o.customerOrderNo=:customerOrderNo"),
                 @NamedQuery (name = "Ordcust.findBillPhoneNo", 
                              query = "select o.billPhone from Ordcust o where o.customerOrderNo=:customerOrderNo and o.billPhone is not null group by o.customerOrderNo, o.billPhone"),
                 @NamedQuery(name = "Ordcust.findBillPhone", 
                             query = "select o.billPhone from Ordcust o where o.customerOrderNo=:customerOrderNo and o.billPhone is not null"),
                 @NamedQuery(name = "Ordcust.findByCustomerOrderNoandStatus", 
                             query = "select o from Ordcust o where o.customerOrderNo=:customerOrderNo and o.status in ('C','P')")
                 })
public class Ordcust implements Serializable
{
    @Column(name = "BILL_ADD1", length = 240)
    private String billAdd1;
    @Column(name = "BILL_ADD2", length = 240)
    private String billAdd2;
    @Column(name = "BILL_ADD3", length = 240)
    private String billAdd3;
    @Column(name = "BILL_CITY", length = 120)
    private String billCity;
    @Column(name = "BILL_COMPANY_NAME", length = 120)
    private String billCompanyName;
    @Column(name = "BILL_COUNTRY_ID", length = 3)
    private String billCountryId;
    @Column(name = "BILL_COUNTY", length = 250)
    private String billCounty;
    @Column(name = "BILL_FIRST_NAME", length = 120)
    private String billFirstName;
    @Column(name = "BILL_JURISDICTION", length = 10)
    private String billJurisdiction;
    @Column(name = "BILL_LAST_NAME", length = 120)
    private String billLastName;
    @Column(name = "BILL_PHONE", length = 20)
    private String billPhone;
    @Column(name = "BILL_PHONETIC_FIRST", length = 120)
    private String billPhoneticFirst;
    @Column(name = "BILL_PHONETIC_LAST", length = 120)
    private String billPhoneticLast;
    @Column(name = "BILL_POST", length = 30)
    private String billPost;
    @Column(name = "BILL_PREFERRED_NAME", length = 120)
    private String billPreferredName;
    @Column(name = "BILL_STATE", length = 3)
    private String billState;
    @Column(name = "CARRIER_CODE", length = 4)
    private String carrierCode;
    @Column(name = "CARRIER_SERVICE_CODE", length = 6)
    private String carrierServiceCode;
    @Column(length = 2000)
    private String comments;
    @Temporal(TemporalType.DATE)
    @Column(name = "CONSUMER_DELIVERY_DATE")
    private Date consumerDeliveryDate;
    @Temporal(TemporalType.DATE)
    @Column(name = "CONSUMER_DELIVERY_TIME")
    private Date consumerDeliveryTime;
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Date createDatetime;
    @Column(name = "CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Column(name = "CUSTOMER_NO", length = 14)
    private String customerNo;
    @Column(name = "CUSTOMER_ORDER_NO", nullable = false, unique = true, length = 48)
    private String customerOrderNo;
    @Column(name = "DELIVER_ADD1", length = 240)
    private String deliverAdd1;
    @Column(name = "DELIVER_ADD2", length = 240)
    private String deliverAdd2;
    @Column(name = "DELIVER_ADD3", length = 240)
    private String deliverAdd3;
    @Column(name = "DELIVER_CHARGE")
    private BigDecimal deliverCharge;
    @Column(name = "DELIVER_CHARGE_CURR", length = 3)
    private String deliverChargeCurr;
    @Column(name = "DELIVER_CITY", length = 120)
    private String deliverCity;
    @Column(name = "DELIVER_COMPANY_NAME", length = 120)
    private String deliverCompanyName;
    @Column(name = "DELIVER_COUNTRY_ID", length = 3)
    private String deliverCountryId;
    @Column(name = "DELIVER_COUNTY", length = 250)
    private String deliverCounty;
    @Column(name = "DELIVER_FIRST_NAME", length = 120)
    private String deliverFirstName;
    @Column(name = "DELIVER_JURISDICTION", length = 10)
    private String deliverJurisdiction;
    @Column(name = "DELIVER_LAST_NAME", length = 120)
    private String deliverLastName;
    @Column(name = "DELIVER_PHONE", length = 20)
    private String deliverPhone;
    @Column(name = "DELIVER_PHONETIC_FIRST", length = 120)
    private String deliverPhoneticFirst;
    @Column(name = "DELIVER_PHONETIC_LAST", length = 120)
    private String deliverPhoneticLast;
    @Column(name = "DELIVER_POST", length = 30)
    private String deliverPost;
    @Column(name = "DELIVER_PREFERRED_NAME", length = 120)
    private String deliverPreferredName;
    @Column(name = "DELIVER_STATE", length = 3)
    private String deliverState;
    @Column(name = "DELIVERY_TYPE", length = 1)
    private String deliveryType;
    @Column(name = "FULFILL_LOC_ID", nullable = false, unique = true)
    private BigDecimal fulfillLocId;
    @Column(name = "FULFILL_LOC_TYPE", nullable = false, unique = true, length = 1)
    private String fulfillLocType;
    @Column(name = "FULFILL_ORDER_NO", nullable = false, unique = true, length = 48)
    private String fulfillOrderNo;
    @Temporal(TemporalType.DATE)
    @Column(name = "LAST_UPDATE_DATETIME", nullable = false)
    private Date lastUpdateDatetime;
    @Column(name = "LAST_UPDATE_ID", nullable = false, length = 30)
    private String lastUpdateId;
    @Id
    @Column(name = "ORDCUST_NO", nullable = false)
    private BigDecimal ordcustNo;
    @Column(name = "ORDER_NO")
    private BigDecimal orderNo;
    @Column(name = "PARTIAL_DELIVERY_IND", nullable = false, length = 1)
    private String partialDeliveryInd;
    @Column(name = "SOURCE_LOC_ID", unique = true)
    private BigDecimal sourceLocId;
    @Column(name = "SOURCE_LOC_TYPE", unique = true, length = 2)
    private String sourceLocType;
    @Column(nullable = false, length = 1)
    private String status;
    @Column(name = "TSF_NO")
    private BigDecimal tsfNo;

    public Ordcust() {
    }

    public Ordcust(String billAdd1, String billAdd2, String billAdd3, String billCity, String billCompanyName,
                   String billCountryId, String billCounty, String billFirstName, String billJurisdiction,
                   String billLastName, String billPhone, String billPhoneticFirst, String billPhoneticLast,
                   String billPost, String billPreferredName, String billState, String carrierCode,
                   String carrierServiceCode, String comments, Date consumerDeliveryDate, Date consumerDeliveryTime,
                   Date createDatetime, String createId, String customerNo, String customerOrderNo, String deliverAdd1,
                   String deliverAdd2, String deliverAdd3, BigDecimal deliverCharge, String deliverChargeCurr,
                   String deliverCity, String deliverCompanyName, String deliverCountryId, String deliverCounty,
                   String deliverFirstName, String deliverJurisdiction, String deliverLastName, String deliverPhone,
                   String deliverPhoneticFirst, String deliverPhoneticLast, String deliverPost,
                   String deliverPreferredName, String deliverState, String deliveryType, BigDecimal fulfillLocId,
                   String fulfillLocType, String fulfillOrderNo, Date lastUpdateDatetime, String lastUpdateId,
                   BigDecimal ordcustNo, BigDecimal orderNo, String partialDeliveryInd, BigDecimal sourceLocId,
                   String sourceLocType, String status, BigDecimal tsfNo) {
        this.billAdd1 = billAdd1;
        this.billAdd2 = billAdd2;
        this.billAdd3 = billAdd3;
        this.billCity = billCity;
        this.billCompanyName = billCompanyName;
        this.billCountryId = billCountryId;
        this.billCounty = billCounty;
        this.billFirstName = billFirstName;
        this.billJurisdiction = billJurisdiction;
        this.billLastName = billLastName;
        this.billPhone = billPhone;
        this.billPhoneticFirst = billPhoneticFirst;
        this.billPhoneticLast = billPhoneticLast;
        this.billPost = billPost;
        this.billPreferredName = billPreferredName;
        this.billState = billState;
        this.carrierCode = carrierCode;
        this.carrierServiceCode = carrierServiceCode;
        this.comments = comments;
        this.consumerDeliveryDate = consumerDeliveryDate;
        this.consumerDeliveryTime = consumerDeliveryTime;
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.customerNo = customerNo;
        this.customerOrderNo = customerOrderNo;
        this.deliverAdd1 = deliverAdd1;
        this.deliverAdd2 = deliverAdd2;
        this.deliverAdd3 = deliverAdd3;
        this.deliverCharge = deliverCharge;
        this.deliverChargeCurr = deliverChargeCurr;
        this.deliverCity = deliverCity;
        this.deliverCompanyName = deliverCompanyName;
        this.deliverCountryId = deliverCountryId;
        this.deliverCounty = deliverCounty;
        this.deliverFirstName = deliverFirstName;
        this.deliverJurisdiction = deliverJurisdiction;
        this.deliverLastName = deliverLastName;
        this.deliverPhone = deliverPhone;
        this.deliverPhoneticFirst = deliverPhoneticFirst;
        this.deliverPhoneticLast = deliverPhoneticLast;
        this.deliverPost = deliverPost;
        this.deliverPreferredName = deliverPreferredName;
        this.deliverState = deliverState;
        this.deliveryType = deliveryType;
        this.fulfillLocId = fulfillLocId;
        this.fulfillLocType = fulfillLocType;
        this.fulfillOrderNo = fulfillOrderNo;
        this.lastUpdateDatetime = lastUpdateDatetime;
        this.lastUpdateId = lastUpdateId;
        this.ordcustNo = ordcustNo;
        this.orderNo = orderNo;
        this.partialDeliveryInd = partialDeliveryInd;
        this.sourceLocId = sourceLocId;
        this.sourceLocType = sourceLocType;
        this.status = status;
        this.tsfNo = tsfNo;
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

    public String getBillCity() {
        return billCity;
    }

    public void setBillCity(String billCity) {
        this.billCity = billCity;
    }

    public String getBillCompanyName() {
        return billCompanyName;
    }

    public void setBillCompanyName(String billCompanyName) {
        this.billCompanyName = billCompanyName;
    }

    public String getBillCountryId() {
        return billCountryId;
    }

    public void setBillCountryId(String billCountryId) {
        this.billCountryId = billCountryId;
    }

    public String getBillCounty() {
        return billCounty;
    }

    public void setBillCounty(String billCounty) {
        this.billCounty = billCounty;
    }

    public String getBillFirstName() {
        return billFirstName;
    }

    public void setBillFirstName(String billFirstName) {
        this.billFirstName = billFirstName;
    }

    public String getBillJurisdiction() {
        return billJurisdiction;
    }

    public void setBillJurisdiction(String billJurisdiction) {
        this.billJurisdiction = billJurisdiction;
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

    public String getBillPhoneticFirst() {
        return billPhoneticFirst;
    }

    public void setBillPhoneticFirst(String billPhoneticFirst) {
        this.billPhoneticFirst = billPhoneticFirst;
    }

    public String getBillPhoneticLast() {
        return billPhoneticLast;
    }

    public void setBillPhoneticLast(String billPhoneticLast) {
        this.billPhoneticLast = billPhoneticLast;
    }

    public String getBillPost() {
        return billPost;
    }

    public void setBillPost(String billPost) {
        this.billPost = billPost;
    }

    public String getBillPreferredName() {
        return billPreferredName;
    }

    public void setBillPreferredName(String billPreferredName) {
        this.billPreferredName = billPreferredName;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getConsumerDeliveryDate() {
        return consumerDeliveryDate;
    }

    public void setConsumerDeliveryDate(Date consumerDeliveryDate) {
        this.consumerDeliveryDate = consumerDeliveryDate;
    }

    public Date getConsumerDeliveryTime() {
        return consumerDeliveryTime;
    }

    public void setConsumerDeliveryTime(Date consumerDeliveryTime) {
        this.consumerDeliveryTime = consumerDeliveryTime;
    }

    public Date getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Date createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    public String getCustomerOrderNo() {
        return customerOrderNo;
    }

    public void setCustomerOrderNo(String customerOrderNo) {
        this.customerOrderNo = customerOrderNo;
    }

    public String getDeliverAdd1() {
        return deliverAdd1;
    }

    public void setDeliverAdd1(String deliverAdd1) {
        this.deliverAdd1 = deliverAdd1;
    }

    public String getDeliverAdd2() {
        return deliverAdd2;
    }

    public void setDeliverAdd2(String deliverAdd2) {
        this.deliverAdd2 = deliverAdd2;
    }

    public String getDeliverAdd3() {
        return deliverAdd3;
    }

    public void setDeliverAdd3(String deliverAdd3) {
        this.deliverAdd3 = deliverAdd3;
    }

    public BigDecimal getDeliverCharge() {
        return deliverCharge;
    }

    public void setDeliverCharge(BigDecimal deliverCharge) {
        this.deliverCharge = deliverCharge;
    }

    public String getDeliverChargeCurr() {
        return deliverChargeCurr;
    }

    public void setDeliverChargeCurr(String deliverChargeCurr) {
        this.deliverChargeCurr = deliverChargeCurr;
    }

    public String getDeliverCity() {
        return deliverCity;
    }

    public void setDeliverCity(String deliverCity) {
        this.deliverCity = deliverCity;
    }

    public String getDeliverCompanyName() {
        return deliverCompanyName;
    }

    public void setDeliverCompanyName(String deliverCompanyName) {
        this.deliverCompanyName = deliverCompanyName;
    }

    public String getDeliverCountryId() {
        return deliverCountryId;
    }

    public void setDeliverCountryId(String deliverCountryId) {
        this.deliverCountryId = deliverCountryId;
    }

    public String getDeliverCounty() {
        return deliverCounty;
    }

    public void setDeliverCounty(String deliverCounty) {
        this.deliverCounty = deliverCounty;
    }

    public String getDeliverFirstName() {
        return deliverFirstName;
    }

    public void setDeliverFirstName(String deliverFirstName) {
        this.deliverFirstName = deliverFirstName;
    }

    public String getDeliverJurisdiction() {
        return deliverJurisdiction;
    }

    public void setDeliverJurisdiction(String deliverJurisdiction) {
        this.deliverJurisdiction = deliverJurisdiction;
    }

    public String getDeliverLastName() {
        return deliverLastName;
    }

    public void setDeliverLastName(String deliverLastName) {
        this.deliverLastName = deliverLastName;
    }

    public String getDeliverPhone() {
        return deliverPhone;
    }

    public void setDeliverPhone(String deliverPhone) {
        this.deliverPhone = deliverPhone;
    }

    public String getDeliverPhoneticFirst() {
        return deliverPhoneticFirst;
    }

    public void setDeliverPhoneticFirst(String deliverPhoneticFirst) {
        this.deliverPhoneticFirst = deliverPhoneticFirst;
    }

    public String getDeliverPhoneticLast() {
        return deliverPhoneticLast;
    }

    public void setDeliverPhoneticLast(String deliverPhoneticLast) {
        this.deliverPhoneticLast = deliverPhoneticLast;
    }

    public String getDeliverPost() {
        return deliverPost;
    }

    public void setDeliverPost(String deliverPost) {
        this.deliverPost = deliverPost;
    }

    public String getDeliverPreferredName() {
        return deliverPreferredName;
    }

    public void setDeliverPreferredName(String deliverPreferredName) {
        this.deliverPreferredName = deliverPreferredName;
    }

    public String getDeliverState() {
        return deliverState;
    }

    public void setDeliverState(String deliverState) {
        this.deliverState = deliverState;
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

    public String getFulfillOrderNo() {
        return fulfillOrderNo;
    }

    public void setFulfillOrderNo(String fulfillOrderNo) {
        this.fulfillOrderNo = fulfillOrderNo;
    }

    public Date getLastUpdateDatetime() {
        return lastUpdateDatetime;
    }

    public void setLastUpdateDatetime(Date lastUpdateDatetime) {
        this.lastUpdateDatetime = lastUpdateDatetime;
    }

    public String getLastUpdateId() {
        return lastUpdateId;
    }

    public void setLastUpdateId(String lastUpdateId) {
        this.lastUpdateId = lastUpdateId;
    }

    public BigDecimal getOrdcustNo() {
        return ordcustNo;
    }

    public void setOrdcustNo(BigDecimal ordcustNo) {
        this.ordcustNo = ordcustNo;
    }

    public BigDecimal getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(BigDecimal orderNo) {
        this.orderNo = orderNo;
    }

    public String getPartialDeliveryInd() {
        return partialDeliveryInd;
    }

    public void setPartialDeliveryInd(String partialDeliveryInd) {
        this.partialDeliveryInd = partialDeliveryInd;
    }

    public BigDecimal getSourceLocId() {
        return sourceLocId;
    }

    public void setSourceLocId(BigDecimal sourceLocId) {
        this.sourceLocId = sourceLocId;
    }

    public String getSourceLocType() {
        return sourceLocType;
    }

    public void setSourceLocType(String sourceLocType) {
        this.sourceLocType = sourceLocType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTsfNo() {
        return tsfNo;
    }

    public void setTsfNo(BigDecimal tsfNo) {
        this.tsfNo = tsfNo;
    }
}
