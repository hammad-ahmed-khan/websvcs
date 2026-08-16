package oms.logicinfo.com.model;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SIMDetailBean {
    public SIMDetailBean() {
        super();
    }
    
    int storeId;
    int deliveryId;
    String customerOrderId;
    String item;
    int quantity;
    BigDecimal unitCostValue;
    String unitCostCurrency;
    String code;
    String description;
    String trackingNumber;
    Date createDate;
    Date updateDate;
    Map<String,ArrayList<ItemListBean>> itemListBeanMap=new HashMap<String,ArrayList<ItemListBean>>();
    

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setDeliveryId(int deliveryId) {
        this.deliveryId = deliveryId;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public void setCustomerOrderId(String customerOrderId) {
        this.customerOrderId = customerOrderId;
    }

    public String getCustomerOrderId() {
        return customerOrderId;
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

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setItemListBeanMap(Map<String, ArrayList<ItemListBean>> itemListBeanMap) {
        this.itemListBeanMap = itemListBeanMap;
    }

    public Map<String, ArrayList<ItemListBean>> getItemListBeanMap() {
        return itemListBeanMap;
    }
}
