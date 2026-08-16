// 
// Decompiled by Procyon v0.5.36
// 

package com.logicinfo.extra.store;

import java.io.Serializable;

public class AwbCancellationRequest implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String orderNo;
    private String courierTrackingNo;
    private String carrier;
    
    public AwbCancellationRequest(final String orderNo, final String courierTrackingNo, final String carrier) {
        this.orderNo = orderNo;
        this.courierTrackingNo = courierTrackingNo;
        this.carrier = carrier;
    }
    
    public AwbCancellationRequest() {
    }
    
    public String getOrderNo() {
        return this.orderNo;
    }
    
    public void setOrderNo(final String orderNo) {
        this.orderNo = orderNo;
    }
    
    public String getCourierTrackingNo() {
        return this.courierTrackingNo;
    }
    
    public void setCourierTrackingNo(final String courierTrackingNo) {
        this.courierTrackingNo = courierTrackingNo;
    }
    
    public String getCarrier() {
        return this.carrier;
    }
    
    public void setCarrier(final String carrier) {
        this.carrier = carrier;
    }
}
