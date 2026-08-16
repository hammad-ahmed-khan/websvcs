// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.store;

public class InActiveTrackingOrders
{
    private String CustomerOrderNo;
    private String trackingId;
    private Integer carrierId;
    private String processInd;
    private Integer deliveryId;
    private String carrierCode;
    
    public InActiveTrackingOrders() {
    }
    
    public InActiveTrackingOrders(final String customerOrderNo, final String trackingId, final Integer carrierId, final String processInd, final Integer deliveryId, final String carrierCode) {
        this.CustomerOrderNo = customerOrderNo;
        this.trackingId = trackingId;
        this.carrierId = carrierId;
        this.processInd = processInd;
        this.deliveryId = deliveryId;
        this.carrierCode = carrierCode;
    }
    
    public String getCarrierCode() {
        return this.carrierCode;
    }
    
    public void setCarrierCode(final String carrierCode) {
        this.carrierCode = carrierCode;
    }
    
    public String getCustomerOrderNo() {
        return this.CustomerOrderNo;
    }
    
    public void setCustomerOrderNo(final String customerOrderNo) {
        this.CustomerOrderNo = customerOrderNo;
    }
    
    public String getTrackingId() {
        return this.trackingId;
    }
    
    public void setTrackingId(final String trackingId) {
        this.trackingId = trackingId;
    }
    
    public Integer getCarrierId() {
        return this.carrierId;
    }
    
    public void setCarrierId(final Integer carrierId) {
        this.carrierId = carrierId;
    }
    
    public String getProcessInd() {
        return this.processInd;
    }
    
    public void setProcessInd(final String processInd) {
        this.processInd = processInd;
    }
    
    public Integer getDeliveryId() {
        return this.deliveryId;
    }
    
    public void setDeliveryId(final Integer deliveryId) {
        this.deliveryId = deliveryId;
    }
    
    @Override
    public String toString() {
        return "InActiveTrackingOrders [CustomerOrderNo=" + this.CustomerOrderNo + ", trackingId=" + this.trackingId + ", carrierId=" + this.carrierId + ", processInd=" + this.processInd + ", deliveryId=" + this.deliveryId + ", carrierCode=" + this.carrierCode + "]";
    }
}
