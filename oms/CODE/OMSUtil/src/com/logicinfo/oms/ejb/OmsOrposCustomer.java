package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@NamedQueries( { @NamedQuery(name = "OmsOrposCustomer.findAll", query = "select o from OmsOrposCustomer o"), 
                 @NamedQuery(name = "OmsOrposCustomer.findByOmsOrposCustOrdId",
                                    query = "select o from OmsOrposCustomer o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposCustomer.findByOmsOrposCustOrdIdAndCustomerId",
                                    query = "select o from OmsOrposCustomer o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.customerId=:customerId")})
@Table(name = "OMS_ORPOS_CUSTOMER")
public class OmsOrposCustomer implements Serializable {
    @Column(name = "BIRTH_DATE")
    private Timestamp birthDate;
    @Column(name = "CONTACT_BY_EMAIL", length = 1)
    private String contactByEmail;
    @Column(name = "CONTACT_BY_MAIL", length = 1)
    private String contactByMail;
    @Column(name = "CONTACT_BY_PHONE", length = 1)
    private String contactByPhone;
    
    @Column(name = "CUSTOMER_ID", nullable = false, length = 20)
    private String customerId;
    @Column(name = "CUSTOMER_TYPE", length = 8)
    private String customerType;
    @Column(name = "EMPLOYEE_ID", length = 10)
    private String employeeId;
    @Column(name = "GENDER_TYPE", length = 11)
    private String genderType;
    @Column(name = "JOB_TITLE", length = 100)
    private String jobTitle;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;
    @Column(name = "PRICING_GROUP_ID")
    private BigDecimal pricingGroupId;
    @Column(name = "REASON_CODE", length = 20)
    private String reasonCode;
    @Column(name = "RECEIPT_PREFERENCE", length = 5)
    private String receiptPreference;
    @Column(name = "TAX_CERTIFICATE", length = 230)
    private String taxCertificate;
    @Column(name = "TAX_ID", length = 1000)
    private String taxId;

    public OmsOrposCustomer() {
    }

    public OmsOrposCustomer(Timestamp birthDate, String contactByEmail, String contactByMail, String contactByPhone,
                            String customerId, String customerType, String employeeId, String genderType,
                            String jobTitle, BigDecimal omsOrposCustOrderId, BigDecimal pricingGroupId,
                            String reasonCode, String receiptPreference, String taxCertificate, String taxId) {
        this.birthDate = birthDate;
        this.contactByEmail = contactByEmail;
        this.contactByMail = contactByMail;
        this.contactByPhone = contactByPhone;
        this.customerId = customerId;
        this.customerType = customerType;
        this.employeeId = employeeId;
        this.genderType = genderType;
        this.jobTitle = jobTitle;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
        this.pricingGroupId = pricingGroupId;
        this.reasonCode = reasonCode;
        this.receiptPreference = receiptPreference;
        this.taxCertificate = taxCertificate;
        this.taxId = taxId;
    }

    public Timestamp getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Timestamp birthDate) {
        this.birthDate = birthDate;
    }

    public String getContactByEmail() {
        return contactByEmail;
    }

    public void setContactByEmail(String contactByEmail) {
        this.contactByEmail = contactByEmail;
    }

    public String getContactByMail() {
        return contactByMail;
    }

    public void setContactByMail(String contactByMail) {
        this.contactByMail = contactByMail;
    }

    public String getContactByPhone() {
        return contactByPhone;
    }

    public void setContactByPhone(String contactByPhone) {
        this.contactByPhone = contactByPhone;
    }
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getGenderType() {
        return genderType;
    }

    public void setGenderType(String genderType) {
        this.genderType = genderType;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public BigDecimal getPricingGroupId() {
        return pricingGroupId;
    }

    public void setPricingGroupId(BigDecimal pricingGroupId) {
        this.pricingGroupId = pricingGroupId;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReceiptPreference() {
        return receiptPreference;
    }

    public void setReceiptPreference(String receiptPreference) {
        this.receiptPreference = receiptPreference;
    }

    public String getTaxCertificate() {
        return taxCertificate;
    }

    public void setTaxCertificate(String taxCertificate) {
        this.taxCertificate = taxCertificate;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }
}
