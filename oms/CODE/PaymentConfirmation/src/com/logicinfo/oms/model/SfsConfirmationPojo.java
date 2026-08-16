package com.logicinfo.oms.model;

import java.math.BigDecimal;

public class SfsConfirmationPojo {
    public SfsConfirmationPojo() {
        super();
    }
    
    
    private String omscustOrderNo;
    
    private BigDecimal storeNo;

    public String getOmscustOrderNo() {
        return omscustOrderNo;
    }

    public void setOmscustOrderNo(String omscustOrderNo) {
        this.omscustOrderNo = omscustOrderNo;
    }

    public BigDecimal getStoreNo() {
        return storeNo;
    }

    public void setStoreNo(BigDecimal storeNo) {
        this.storeNo = storeNo;
    }
}
