package oms.logicinfo.com.model;

import java.math.BigDecimal;

import java.util.Date;

public class ItemListBean 
{
    String customerOrderId;
    BigDecimal lineNo;
    String item;
    int deliveryId;
    int quantity;
    BigDecimal unitCostValue;
    String unitCostCurrency;
    String description;

    public void setItem(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setUnitCostValue(BigDecimal unitCostValue) {
        this.unitCostValue = unitCostValue;
    }

    public BigDecimal getUnitCostValue() {
        return unitCostValue;
    }

    public void setUnitCostCurrency(String unitCostCurrency) {
        this.unitCostCurrency = unitCostCurrency;
    }

    public String getUnitCostCurrency() {
        return unitCostCurrency;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public String getCustomerOrderId() {
        return customerOrderId;
    }

    public void setLineNo(BigDecimal lineNo) {
        this.lineNo = lineNo;
    }

    public BigDecimal getLineNo() {
        return lineNo;
    }
}
