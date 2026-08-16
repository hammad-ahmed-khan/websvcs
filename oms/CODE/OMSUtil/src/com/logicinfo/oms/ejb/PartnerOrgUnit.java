package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@NamedQueries( { @NamedQuery(name = "PartnerOrgUnit.findAll", query = "select o from PartnerOrgUnit o") ,
                 @NamedQuery(name = "PartnerOrgUnit.findOrgUnitId", query = "select o.orgUnitId from PartnerOrgUnit o where o.partner=:partner") })
@Table(name = "PARTNER_ORG_UNIT")
@IdClass(PartnerOrgUnitPK.class)
public class PartnerOrgUnit implements Serializable {
    @Temporal(TemporalType.DATE)
    @Column(name = "CREATE_DATETIME", nullable = false)
    private Date createDatetime;
    @Column(name = "CREATE_ID", nullable = false, length = 30)
    private String createId;
    @Id
    @Column(name = "ORG_UNIT_ID", nullable = false)
    private BigDecimal orgUnitId;
    @Id
    @Column(nullable = false)
    private BigDecimal partner;
    @Id
    @Column(name = "PARTNER_TYPE", nullable = false, length = 1)
    private String partnerType;
    @Column(name = "PRIMARY_PAY_SITE", length = 1)
    private String primaryPaySite;

    public PartnerOrgUnit() {
    }

    public PartnerOrgUnit(Date createDatetime, String createId, BigDecimal orgUnitId, BigDecimal partner,
                          String partnerType, String primaryPaySite) {
        this.createDatetime = createDatetime;
        this.createId = createId;
        this.orgUnitId = orgUnitId;
        this.partner = partner;
        this.partnerType = partnerType;
        this.primaryPaySite = primaryPaySite;
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

    public BigDecimal getOrgUnitId() {
        return orgUnitId;
    }

    public void setOrgUnitId(BigDecimal orgUnitId) {
        this.orgUnitId = orgUnitId;
    }

    public BigDecimal getPartner() {
        return partner;
    }

    public void setPartner(BigDecimal partner) {
        this.partner = partner;
    }

    public String getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(String partnerType) {
        this.partnerType = partnerType;
    }

    public String getPrimaryPaySite() {
        return primaryPaySite;
    }

    public void setPrimaryPaySite(String primaryPaySite) {
        this.primaryPaySite = primaryPaySite;
    }
}
