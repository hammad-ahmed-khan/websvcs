// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.store;

public class InActiveTrackingOrdersShipmentCarrier
{
    private String code;
    private String description;
    
    public InActiveTrackingOrdersShipmentCarrier() {
    }
    
    public InActiveTrackingOrdersShipmentCarrier(final String code, final String description) {
        this.code = code;
        this.description = description;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "InActiveTrackingOrdersShipmentCarrier [code=" + this.code + ", description=" + this.description + "]";
    }
}
