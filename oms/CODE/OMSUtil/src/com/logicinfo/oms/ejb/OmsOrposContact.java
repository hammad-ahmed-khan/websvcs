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
@NamedQueries( { @NamedQuery(name = "OmsOrposContact.findAll", query = "select o from OmsOrposContact o"),
                 @NamedQuery(name = "OmsOrposContact.findByOmsOrposCustOrderId",  query = "select o from OmsOrposContact o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                  @NamedQuery(name = "OmsOrposContact.findByOmsOrposCustOrderIdandCustomerId", query = "select o from OmsOrposContact o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.customerId=:customerId"),
                  @NamedQuery(name = "OmsOrposContact.findByCustomerId", query = "select o from OmsOrposContact o where o.customerId=:customerId and o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposContact.findByFulSeqNo", query = "select o from OmsOrposContact o where o.custOrdFulSeqNo=:custOrdFulSeqNo and o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposContact.findByPaymentSeqNo", query = "select o from OmsOrposContact o where o.paymentSeqNo=:paymentSeqNo and o.omsOrposCustOrderId=:omsOrposCustOrderId")})
@Table(name = "OMS_ORPOS_CONTACT")
@IdClass(OmsOrposContactPK.class)
public class OmsOrposContact implements Serializable {
    @Column(name = "ADDRBOOK_ENTRY_SEQ")
    private BigDecimal addrbookEntrySeq;
    @Column(name = "COMPANY_NAME", length = 250)
    private String companyName;
    @Id
    @Column(name = "CONTACT_SEQ", nullable = false)
    @SequenceGenerator( name = "omsOrposContactSeq", sequenceName = "OMS_ORPOS_CONTACT_SEQ", allocationSize = 1, initialValue = 1 ) 
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsOrposContactSeq" )
    private BigDecimal contactSeq;
    @Column(name = "CUST_ORD_FUL_SEQ_NO")
    private BigDecimal custOrdFulSeqNo;
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerId;
    @Column(name = "FIRST_NAME", length = 120)
    private String firstName;
    @Column(name = "FULL_NAME", length = 250)
    private String fullName;
    @Column(name = "LAST_NAME", length = 120)
    private String lastName;
    @Column(name = "MIDDLE_NAME", length = 120)
    private String middleName;
    @Column(name = "NAME_PREFIX", length = 120)
    private String namePrefix;
    @Column(name = "NAME_SUFFIX", length = 120)
    private String nameSuffix;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PAYMENT_SEQ_NO")
    private BigDecimal paymentSeqNo;
    @Column(name = "PHONETIC_FIRST", length = 120)
    private String phoneticFirst;
    @Column(name = "PHONETIC_LAST", length = 120)
    private String phoneticLast;
    @Column(name = "PREFERRED_NAME", length = 120)
    private String preferredName;

    public OmsOrposContact() {
    }

    public OmsOrposContact(BigDecimal addrbookEntrySeq, String companyName, BigDecimal contactSeq,
                           BigDecimal custOrdFulSeqNo, BigDecimal customerId, String firstName, String fullName,
                           String lastName, String middleName, String namePrefix, String nameSuffix,
                           BigDecimal omsOrposCustOrderId, BigDecimal paymentSeqNo, String phoneticFirst,
                           String phoneticLast, String preferredName) {
        this.addrbookEntrySeq = addrbookEntrySeq;
        this.companyName = companyName;
        this.contactSeq = contactSeq;
        this.custOrdFulSeqNo = custOrdFulSeqNo;
        this.customerId = customerId;
        this.firstName = firstName;
        this.fullName = fullName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.namePrefix = namePrefix;
        this.nameSuffix = nameSuffix;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.paymentSeqNo = paymentSeqNo;
        this.phoneticFirst = phoneticFirst;
        this.phoneticLast = phoneticLast;
        this.preferredName = preferredName;
    }

    public BigDecimal getAddrbookEntrySeq() {
        return addrbookEntrySeq;
    }

    public void setAddrbookEntrySeq(BigDecimal addrbookEntrySeq) {
        this.addrbookEntrySeq = addrbookEntrySeq;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getContactSeq() {
        return contactSeq;
    }

    public void setContactSeq(BigDecimal contactSeq) {
        this.contactSeq = contactSeq;
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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public String getNameSuffix() {
        return nameSuffix;
    }

    public void setNameSuffix(String nameSuffix) {
        this.nameSuffix = nameSuffix;
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

    public String getPhoneticFirst() {
        return phoneticFirst;
    }

    public void setPhoneticFirst(String phoneticFirst) {
        this.phoneticFirst = phoneticFirst;
    }

    public String getPhoneticLast() {
        return phoneticLast;
    }

    public void setPhoneticLast(String phoneticLast) {
        this.phoneticLast = phoneticLast;
    }

    public String getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }
}
