package com.logicinfo.oms.ejb;
import java.io.Serializable;

import java.math.BigDecimal;

// OMSTAXDESC PK EJB
public class OmsTaxDescPK implements Serializable {
    public String vatCode;
    public BigDecimal vatRegion;

    public OmsTaxDescPK() {
    }

    public OmsTaxDescPK(String vatCode, BigDecimal vatRegion) {
        this.vatCode = vatCode;
        this.vatRegion = vatRegion;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsTaxDescPK) {
            final OmsTaxDescPK otherOmsTaxDescPK = (OmsTaxDescPK)other;
            final boolean areEqual =
                (otherOmsTaxDescPK.vatCode.equals(vatCode) && otherOmsTaxDescPK.vatRegion.equals(vatRegion));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getVatCode() {
        return vatCode;
    }

    public void setVatCode(String vatCode) {
        this.vatCode = vatCode;
    }

    public BigDecimal getVatRegion() {
        return vatRegion;
    }

    public void setVatRegion(BigDecimal vatRegion) {
        this.vatRegion = vatRegion;
    }
}
