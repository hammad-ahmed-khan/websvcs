package com.logicinfo.oms.beans;


import com.logicinfo.oms.ejb.OmsTempCoFo;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class ReturnPartialResponseObject {
    public ReturnPartialResponseObject() {
        super();
    }
    public long orderQty;
    public int maxFulfilOrderNo;
    public Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap;
    public long pendingQty;
    public TreeMap<BigDecimal,String> storeFulFillMap;
    public void setOrderQty(long orderQty) {
        this.orderQty = orderQty;
    }

    public long getOrderQty() {
        return orderQty;
    }

    public void setMaxFulfilOrderNo(int maxFulfilOrderNo) {
        this.maxFulfilOrderNo = maxFulfilOrderNo;
    }

    public int getMaxFulfilOrderNo() {
        return maxFulfilOrderNo;
    }

    public void setSohMap(Map<BigDecimal, ArrayList<OmsTempCoFo>> sohMap) {
        this.sohMap = sohMap;
    }

    public Map<BigDecimal, ArrayList<OmsTempCoFo>> getSohMap() {
        return sohMap;
    }

    public void setPendingQty(long pendingQty) {
        this.pendingQty = pendingQty;
    }

    public long getPendingQty() {
        return pendingQty;
    }

    public void setStoreFulFillMap(TreeMap<BigDecimal, String> storeFulFillMap) {
        this.storeFulFillMap = storeFulFillMap;
    }

    public TreeMap<BigDecimal, String> getStoreFulFillMap() {
        return storeFulFillMap;
    }
}
