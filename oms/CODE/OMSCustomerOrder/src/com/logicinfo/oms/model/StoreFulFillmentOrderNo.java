package com.logicinfo.oms.model;

import java.math.BigDecimal;

public class StoreFulFillmentOrderNo 
{
    private String  item;
    private String locationType;
    private BigDecimal location;
    private BigDecimal quantity;
   
    public StoreFulFillmentOrderNo() {
        super();
    }


    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public void setLocation(BigDecimal location) {
        this.location = location;
    }

    public BigDecimal getLocation() {
        return location;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }
}
