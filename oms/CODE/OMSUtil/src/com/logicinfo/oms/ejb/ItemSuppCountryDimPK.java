package com.logicinfo.oms.ejb;

import java.io.Serializable;

import java.math.BigDecimal;

public class ItemSuppCountryDimPK implements Serializable {
    public String dimObject;
    public String item;
    public String originCountry;
    public BigDecimal supplier;

    public ItemSuppCountryDimPK() {
    }

    public ItemSuppCountryDimPK(String dimObject, String item, String originCountry, BigDecimal supplier) {
        this.dimObject = dimObject;
        this.item = item;
        this.originCountry = originCountry;
        this.supplier = supplier;
    }

    public boolean equals(Object other) {
        if (other instanceof ItemSuppCountryDimPK) {
            final ItemSuppCountryDimPK otherItemSuppCountryDimPK = (ItemSuppCountryDimPK)other;
            final boolean areEqual =
                (otherItemSuppCountryDimPK.dimObject.equals(dimObject) && otherItemSuppCountryDimPK.item.equals(item) &&
                 otherItemSuppCountryDimPK.originCountry.equals(originCountry) &&
                 otherItemSuppCountryDimPK.supplier.equals(supplier));
            return areEqual;
        }
        return false;
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String getDimObject() {
        return dimObject;
    }

    public void setDimObject(String dimObject) {
        this.dimObject = dimObject;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public BigDecimal getSupplier() {
        return supplier;
    }

    public void setSupplier(BigDecimal supplier) {
        this.supplier = supplier;
    }
}
