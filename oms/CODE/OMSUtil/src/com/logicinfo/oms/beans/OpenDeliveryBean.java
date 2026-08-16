package com.logicinfo.oms.beans;

public class OpenDeliveryBean {
    public OpenDeliveryBean() {
        super();
    }
    int fulfillOrdNo;
    String item;
    int quantity;
    int noOfRows;

    public void setFulfillOrdNo(int fulfillOrdNo) {
        this.fulfillOrdNo = fulfillOrdNo;
    }

    public int getFulfillOrdNo() {
        return fulfillOrdNo;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public int getNoOfRows() {
        return noOfRows;
    }
}
