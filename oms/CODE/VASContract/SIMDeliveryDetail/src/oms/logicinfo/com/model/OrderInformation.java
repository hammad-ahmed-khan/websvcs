package oms.logicinfo.com.model;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Map;

public class OrderInformation {
    
    
    private  String orderNo;
    
    private BigDecimal orderValue;
    
    private String isCod;
    
    private String carrier;
    
    private Integer storeId;
      
    private String shipmentId;
    
    
    ArrayList<ItemListBean>  itemListBeanDetail;
    
    
    public OrderInformation() {
        super();
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderValue(BigDecimal orderValue) {
        this.orderValue = orderValue;
    }

    public BigDecimal getOrderValue() {
        return orderValue;
    }

    public void setIsCod(String isCod) {
        this.isCod = isCod;
    }

    public String getIsCod() {
        return isCod;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setStoreId(Integer storeId) {
        this.storeId = storeId;
    }

    public Integer getStoreId() {
        return storeId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getShipmentId() {
        return shipmentId;
    }

   

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof OrderInformation)) {
            return false;
        }
        final OrderInformation other = (OrderInformation)object;
        if (!(orderNo == null ? other.orderNo == null : orderNo.equals(other.orderNo))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 1;
        result = PRIME * result + ((orderNo == null) ? 0 : orderNo.hashCode());
        return result;
    }

    public void setItemListBeanDetail(ArrayList<ItemListBean> itemListBeanDetail) {
        this.itemListBeanDetail = itemListBeanDetail;
    }

    public ArrayList<ItemListBean> getItemListBeanDetail() {
        return itemListBeanDetail;
    }
}
