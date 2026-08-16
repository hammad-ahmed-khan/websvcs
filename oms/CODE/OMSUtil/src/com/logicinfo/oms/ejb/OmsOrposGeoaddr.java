package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

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
@NamedQueries( { @NamedQuery(name = "OmsOrposGeoaddr.findAll", query = "select o from OmsOrposGeoaddr o"),
                 @NamedQuery(name = "OmsOrposGeoaddr.findByOmsOrposCustOrdId", query = "select o from OmsOrposGeoaddr o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposGeoaddr.findByFulSeqNo", query = "select o from OmsOrposGeoaddr o where o.custOrdFulSeqNo=:custOrdFulSeqNo and o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposGeoaddr.findByPaymentSeqNo", query = "select o from OmsOrposGeoaddr o where o.paymentSeqNo=:paymentSeqNo and o.omsOrposCustOrderId=:omsOrposCustOrderId")})
@Table(name = "OMS_ORPOS_GEOADDR")
@IdClass(OmsOrposGeoaddrPK.class)
public class OmsOrposGeoaddr implements Serializable {
    @Column(name = "ADDRBOOK_ENTRY_SEQ")
    private BigDecimal addrbookEntrySeq;
    @Column(name = "ADDRESS_1", nullable = false, length = 240)
    private String address1;
    @Column(name = "ADDRESS_2", length = 240)
    private String address2;
    @Column(name = "ADDRESS_3", length = 240)
    private String address3;
    @Column(name = "ADDRESS_4", length = 240)
    private String address4;
    @Column(name = "ADDRESS_5", length = 240)
    private String address5;
    @Column(name = "ADDRESS_ALIAS", length = 120)
    private String addressAlias;
    @Column(length = 120)
    private String city;
    @Column(name = "COUNTRY_CODE", length = 3)
    private String countryCode;
    @Column(name = "COUNTRY_NAME", length = 120)
    private String countryName;
    @Column(length = 250)
    private String county;
    @Column(name = "CUST_ORD_FUL_SEQ_NO")
    private BigDecimal custOrdFulSeqNo;
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerId;
    @Id
    @Column(name = "GEOADDR_SEQ", nullable = false)
    @SequenceGenerator( name = "omsOrposGeoAddSeq", sequenceName = "OMS_ORPOS_GEOADDR_SEQ", allocationSize = 1, initialValue = 1 ) 
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsOrposGeoAddSeq" )    
    private BigDecimal geoaddrSeq;
    @Column(name = "JURISDICTION_CODE", length = 10)
    private String jurisdictionCode;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;
    @Column(name = "POSTAL_CODE", length = 30)
    private String postalCode;
    @Column(name = "STATE_CODE", length = 3)
    private String stateCode;
    @Column(name = "STATE_NAME", length = 120)
    private String stateName;

    public OmsOrposGeoaddr() {
    }

    public OmsOrposGeoaddr(BigDecimal addrbookEntrySeq, String address1, String address2, String address3,
                           String address4, String address5, String addressAlias, String city, String countryCode,
                           String countryName, String county, BigDecimal custOrdFulSeqNo, BigDecimal customerId,
                           BigDecimal geoaddrSeq, String jurisdictionCode, BigDecimal omsOrposCustOrderId,
                           BigDecimal paymentSeqNo, String postalCode, String stateCode, String stateName) {
        this.addrbookEntrySeq = addrbookEntrySeq;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;
        this.address5 = address5;
        this.addressAlias = addressAlias;
        this.city = city;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.county = county;
        this.custOrdFulSeqNo = custOrdFulSeqNo;
        this.customerId = customerId;
        this.geoaddrSeq = geoaddrSeq;
        this.jurisdictionCode = jurisdictionCode;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
        this.postalCode = postalCode;
        this.stateCode = stateCode;
        this.stateName = stateName;
    }

    public BigDecimal getAddrbookEntrySeq() {
        return addrbookEntrySeq;
    }

    public void setAddrbookEntrySeq(BigDecimal addrbookEntrySeq) {
        this.addrbookEntrySeq = addrbookEntrySeq;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }

    public String getAddress5() {
        return address5;
    }

    public void setAddress5(String address5) {
        this.address5 = address5;
    }

    public String getAddressAlias() {
        return addressAlias;
    }

    public void setAddressAlias(String addressAlias) {
        this.addressAlias = addressAlias;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public BigDecimal getCustOrdFulSeqNo() {
        return custOrdFulSeqNo;
    }

    public void setCustOrdFulSeqNo(BigDecimal custOrdFulSeqNo) {
        this.custOrdFulSeqNo = custOrdFulSeqNo;
    }

    public BigDecimal getCustomerId() {
        return customerId;
    }

    public void setCustomerId(BigDecimal customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getGeoaddrSeq() {
        return geoaddrSeq;
    }

    public void setGeoaddrSeq(BigDecimal geoaddrSeq) {
        this.geoaddrSeq = geoaddrSeq;
    }

    public String getJurisdictionCode() {
        return jurisdictionCode;
    }

    public void setJurisdictionCode(String jurisdictionCode) {
        this.jurisdictionCode = jurisdictionCode;
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

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
