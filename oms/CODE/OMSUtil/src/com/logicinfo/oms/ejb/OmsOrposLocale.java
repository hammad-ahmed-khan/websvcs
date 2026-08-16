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
@NamedQueries( { @NamedQuery(name = "OmsOrposLocale.findAll", query = "select o from OmsOrposLocale o") ,
                 @NamedQuery(name = "OmsOrposLocale.findByOmsOrposCustOrdId",
                            query = "select o from OmsOrposLocale o where o.omsOrposCustOrderId=:omsOrposCustOrderId"),
                 @NamedQuery(name = "OmsOrposLocale.findByOmsOrposCustOrdIdAndLocaleSeq",
                            query = "select o from OmsOrposLocale o where o.omsOrposCustOrderId=:omsOrposCustOrderId and o.localeSeq=:localeSeq")})
@Table(name = "OMS_ORPOS_LOCALE")
@IdClass(OmsOrposLocalePK.class)
public class OmsOrposLocale implements Serializable {
    @Column(length = 2)
    private String country;
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerId;
    @Column(nullable = false, length = 2)
    private String lang;
    @Id
    @Column(name = "LOCALE_SEQ", nullable = false)    
    @SequenceGenerator( name = "omsOrposLocaleSeq", sequenceName = "OMS_ORPOS_LOCALE_SEQ", allocationSize = 1, initialValue = 1 ) 
    @GeneratedValue( strategy = GenerationType.SEQUENCE, generator = "omsOrposLocaleSeq" )
    private BigDecimal localeSeq;
    @Id
    @Column(name = "OMS_ORPOS_CUST_ORDER_ID", nullable = false)
    private BigDecimal omsOrposCustOrderId;

    public OmsOrposLocale() {
    }

    public OmsOrposLocale(String country, BigDecimal customerId, String lang, BigDecimal localeSeq,
                          BigDecimal omsOrposCustOrderId) {
        this.country = country;
        this.customerId = customerId;
        this.lang = lang;
        this.localeSeq = localeSeq;
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getCustomerId() {
        return customerId;
    }

    public void setCustomerId(BigDecimal customerId) {
        this.customerId = customerId;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public BigDecimal getLocaleSeq() {
        return localeSeq;
    }

    public void setLocaleSeq(BigDecimal localeSeq) {
        this.localeSeq = localeSeq;
    }

    public BigDecimal getOmsOrposCustOrderId() {
        return omsOrposCustOrderId;
    }

    public void setOmsOrposCustOrderId(BigDecimal omsOrposCustOrderId) {
        this.omsOrposCustOrderId = omsOrposCustOrderId;
    }
}
