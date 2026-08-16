package com.logicinfo.oms.util;

import java.util.ArrayList;
import java.util.List;


public class ArrayOfOmsOrderStatusUpdateDetail {
    
    private List<OmsOrderDetailStatus> orderDetailStatus;

    public List<OmsOrderDetailStatus> getOrderDetailStatus() {
        if (orderDetailStatus == null) {
            orderDetailStatus = new ArrayList<OmsOrderDetailStatus>();
        }
        return orderDetailStatus;
    }
}
