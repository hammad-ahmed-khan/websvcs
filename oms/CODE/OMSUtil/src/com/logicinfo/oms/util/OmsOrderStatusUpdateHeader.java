package com.logicinfo.oms.util;

import javax.xml.datatype.XMLGregorianCalendar;


public class OmsOrderStatusUpdateHeader {

    private String entityId;
    private String applicationId;

    private String orderId;

    private String subOrderId;

    private String omsOrderId;

    private XMLGregorianCalendar deliveryDate;

    private XMLGregorianCalendar updateDate;

    private ArrayOfOmsOrderStatusUpdateDetail orderDetailStatuses;

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setSubOrderId(String subOrderId) {
        this.subOrderId = subOrderId;
    }

    public String getSubOrderId() {
        return subOrderId;
    }

    public void setOmsOrderId(String omsOrderId) {
        this.omsOrderId = omsOrderId;
    }

    public String getOmsOrderId() {
        return omsOrderId;
    }

    public void setDeliveryDate(XMLGregorianCalendar deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public XMLGregorianCalendar getDeliveryDate() {
        return deliveryDate;
    }

    public void setUpdateDate(XMLGregorianCalendar updateDate) {
        this.updateDate = updateDate;
    }

    public XMLGregorianCalendar getUpdateDate() {
        return updateDate;
    }

    public void setOrderDetailStatuses(ArrayOfOmsOrderStatusUpdateDetail orderDetailStatuses) {
        this.orderDetailStatuses = orderDetailStatuses;
    }

    public ArrayOfOmsOrderStatusUpdateDetail getOrderDetailStatuses() {
        return orderDetailStatuses;
    }
}
