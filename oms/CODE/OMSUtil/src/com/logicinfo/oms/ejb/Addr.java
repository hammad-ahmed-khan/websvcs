package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
  @NamedQuery(name = "Addr.findAll", query = "select o from Addr o"),
  @NamedQuery(name = "Addr.findByKeyValue1", query = "select o.countryId from Addr o where o.keyValue1=:keyValue1"),
  @NamedQuery(name = "Addr.findByAddrKeyValue1", query = "select o from Addr o where o.keyValue1=:keyValue1"),
  @NamedQuery(name = "Addr.findByAddrKeyValue1andaddr_type", query = "select o from Addr o where o.keyValue1=:keyValue1 and o.addrType=:addrType")
})
public class Addr implements Serializable {
    @Id
    @Column(name="ADDR_KEY", nullable = false)
    private Long addrKey;
    @Column(name="ADDR_TYPE", nullable = false, length = 2)
    private String addrType;
    @Column(name="ADD_1", nullable = false, length = 240)
    private String add1;
    @Column(name="ADD_2", length = 240)
    private String add2;
    @Column(name="ADD_3", length = 240)
    private String add3;
    @Column(nullable = false, length = 120)
    private String city;
    @Column(name="CONTACT_EMAIL", length = 100)
    private String contactEmail;
    @Column(name="CONTACT_FAX", length = 20)
    private String contactFax;
    @Column(name="CONTACT_NAME", length = 120)
    private String contactName;
    @Column(name="CONTACT_PHONE", length = 20)
    private String contactPhone;
    @Column(name="CONTACT_TELEX", length = 20)
    private String contactTelex;
    @Column(name="COUNTRY_ID", nullable = false, length = 3)
    private String countryId;
    @Column(length = 250)
    private String county;
    @Column(name="CREATE_DATETIME", nullable = false)
    private Timestamp createDatetime;
    @Column(name="CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Column(name="EDI_ADDR_CHG", length = 1)
    private String ediAddrChg;
    @Column(name="EXTERNAL_REF_ID", length = 32)
    private String externalRefId;
    @Column(name="JURISDICTION_CODE", length = 10)
    private String jurisdictionCode;
    @Column(name="KEY_VALUE_1", nullable = false, length = 20)
    private String keyValue1;
    @Column(name="KEY_VALUE_2", length = 20)
    private String keyValue2;
    @Column(nullable = false, length = 4)
    private String module;
    @Column(name="ORACLE_VENDOR_SITE_ID")
    private Long oracleVendorSiteId;
    @Column(length = 30)
    private String post;
    @Column(name="PRIMARY_ADDR_IND", nullable = false, length = 1)
    private String primaryAddrInd;
    @Column(name="PUBLISH_IND", nullable = false, length = 1)
    private String publishInd;
    @Column(name="SEQ_NO", nullable = false)
    private Long seqNo;
    @Column(length = 3)
    private String state;

    public Addr() {
    }

    public Addr(String add1, String add2, String add3, Long addrKey,
                String addrType, String city, String contactEmail,
                String contactFax, String contactName, String contactPhone,
                String contactTelex, String countryId, String county,
                Timestamp createDatetime, String createId, String ediAddrChg,
                String externalRefId, String jurisdictionCode,
                String keyValue1, String keyValue2, String module,
                Long oracleVendorSiteId, String post, String primaryAddrInd,
                String publishInd, Long seqNo, String state) {
        this.add1 = add1;
        this.add2 = add2;
        this.add3 = add3;
        this.addrKey = addrKey;
        this.addrType = addrType;
        this.city = city;
        this.contactEmail = contactEmail;
        this.contactFax = contactFax;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.contactTelex = contactTelex;
        this.countryId = countryId;
        this.county = county;
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.ediAddrChg = ediAddrChg;
        this.externalRefId = externalRefId;
        this.jurisdictionCode = jurisdictionCode;
        this.keyValue1 = keyValue1;
        this.keyValue2 = keyValue2;
        this.module = module;
        this.oracleVendorSiteId = oracleVendorSiteId;
        this.post = post;
        this.primaryAddrInd = primaryAddrInd;
        this.publishInd = publishInd;
        this.seqNo = seqNo;
        this.state = state;
    }

    public Long getAddrKey() {
        return addrKey;
    }

    public void setAddrKey(Long addrKey) {
        this.addrKey = addrKey;
    }

    public String getAddrType() {
        return addrType;
    }

    public void setAddrType(String addrType) {
        this.addrType = addrType;
    }

    public String getAdd1() {
        return add1;
    }

    public void setAdd1(String add1) {
        this.add1 = add1;
    }

    public String getAdd2() {
        return add2;
    }

    public void setAdd2(String add2) {
        this.add2 = add2;
    }

    public String getAdd3() {
        return add3;
    }

    public void setAdd3(String add3) {
        this.add3 = add3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactFax() {
        return contactFax;
    }

    public void setContactFax(String contactFax) {
        this.contactFax = contactFax;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactTelex() {
        return contactTelex;
    }

    public void setContactTelex(String contactTelex) {
        this.contactTelex = contactTelex;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public Timestamp getCreateDatetime() {
        return createDatetime;
    }

    public void setCreateDatetime(Timestamp createDatetime) {
        this.createDatetime = createDatetime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public String getEdiAddrChg() {
        return ediAddrChg;
    }

    public void setEdiAddrChg(String ediAddrChg) {
        this.ediAddrChg = ediAddrChg;
    }

    public String getExternalRefId() {
        return externalRefId;
    }

    public void setExternalRefId(String externalRefId) {
        this.externalRefId = externalRefId;
    }

    public String getJurisdictionCode() {
        return jurisdictionCode;
    }

    public void setJurisdictionCode(String jurisdictionCode) {
        this.jurisdictionCode = jurisdictionCode;
    }

    public String getKeyValue1() {
        return keyValue1;
    }

    public void setKeyValue1(String keyValue1) {
        this.keyValue1 = keyValue1;
    }

    public String getKeyValue2() {
        return keyValue2;
    }

    public void setKeyValue2(String keyValue2) {
        this.keyValue2 = keyValue2;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Long getOracleVendorSiteId() {
        return oracleVendorSiteId;
    }

    public void setOracleVendorSiteId(Long oracleVendorSiteId) {
        this.oracleVendorSiteId = oracleVendorSiteId;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPrimaryAddrInd() {
        return primaryAddrInd;
    }

    public void setPrimaryAddrInd(String primaryAddrInd) {
        this.primaryAddrInd = primaryAddrInd;
    }

    public String getPublishInd() {
        return publishInd;
    }

    public void setPublishInd(String publishInd) {
        this.publishInd = publishInd;
    }

    public Long getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Long seqNo) {
        this.seqNo = seqNo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
