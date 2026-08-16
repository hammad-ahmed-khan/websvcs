package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class PartnerOrgUnitPK implements Serializable {
    public BigDecimal orgUnitId;
    public BigDecimal partner;
    public String partnerType;

    public PartnerOrgUnitPK() {
    }

    public PartnerOrgUnitPK(BigDecimal orgUnitId, BigDecimal partner, String partnerType) {
        this.orgUnitId = orgUnitId;
        this.partner = partner;
        this.partnerType = partnerType;
    }

    public boolean equals(Object other) {
        if (other instanceof PartnerOrgUnitPK) {
            final PartnerOrgUnitPK otherPartnerOrgUnitPK = (PartnerOrgUnitPK)other;
            final boolean areEqual =
                (otherPartnerOrgUnitPK.orgUnitId.equals(orgUnitId) && otherPartnerOrgUnitPK.partner.equals(partner) &&
                 otherPartnerOrgUnitPK.partnerType.equals(partnerType));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
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
}
