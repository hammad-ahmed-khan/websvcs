package com.logicinfo.oms.util;

import javax.xml.datatype.XMLGregorianCalendar;

public class OmsOrderDetailStatus {
    
    private long orderDetailId;
    
    private String productSku;

    private int quantity;

    private String sourceType;

    private int sourceId;

    private String fulfillType;

    private int fulfillId;

    private String eventId;

    private String eventComment;

    private long eventReferenceId;

    private XMLGregorianCalendar updateDate;

    public OmsOrderDetailStatus() {
        super();
    }

    public void setOrderDetailId(long orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public long getOrderDetailId() {
        return orderDetailId;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductSku() {
        return productSku;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setSourceType(String sourceType) {
        this.sourceType = sourceType;
    }

    public String getSourceType() {
        return sourceType;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setFulfillType(String fulfillType) {
        this.fulfillType = fulfillType;
    }

    public String getFulfillType() {
        return fulfillType;
    }

    public void setFulfillId(int fulfillId) {
        this.fulfillId = fulfillId;
    }

    public int getFulfillId() {
        return fulfillId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventComment(String eventComment) {
        this.eventComment = eventComment;
    }

    public String getEventComment() {
        return eventComment;
    }

    public void setEventReferenceId(long eventReferenceId) {
        this.eventReferenceId = eventReferenceId;
    }

    public long getEventReferenceId() {
        return eventReferenceId;
    }

    public void setUpdateDate(XMLGregorianCalendar updateDate) {
        this.updateDate = updateDate;
    }

    public XMLGregorianCalendar getUpdateDate() {
        return updateDate;
    }
}
