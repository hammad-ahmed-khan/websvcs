package com.logicinfo.oms.ejb;

import java.io.Serializable;

public class OmsSparePartConfirmDtlPK implements Serializable {
    public String item;
    public String serviceConfirmId;

    public OmsSparePartConfirmDtlPK() {
    }

    public OmsSparePartConfirmDtlPK(String item, String serviceConfirmId) {
        this.item = item;
        this.serviceConfirmId = serviceConfirmId;
    }

    public boolean equals(Object other) {
        if (other instanceof OmsSparePartConfirmDtlPK) {
            final OmsSparePartConfirmDtlPK otherOmsSparePartConfirmDtlPK = (OmsSparePartConfirmDtlPK)other;
            final boolean areEqual =
                (otherOmsSparePartConfirmDtlPK.item.equals(item) && otherOmsSparePartConfirmDtlPK.serviceConfirmId.equals(serviceConfirmId));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getServiceConfirmId() {
        return serviceConfirmId;
    }

    public void setServiceConfirmId(String serviceConfirmId) {
        this.serviceConfirmId = serviceConfirmId;
    }
}
