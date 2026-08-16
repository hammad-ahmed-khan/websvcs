package com.logicinfo.oms.beans;

import com.oracle.retail.integration.base.bo.custorderref.v1.CustOrderRef;

public class CustOrdCancelBean {
    public CustOrdCancelBean() {
        super();
    }
    public void cancelCustomerOrder(CustOrderRef custOrderRef) {
        custOrderRef.getOrderId();
    }
}
