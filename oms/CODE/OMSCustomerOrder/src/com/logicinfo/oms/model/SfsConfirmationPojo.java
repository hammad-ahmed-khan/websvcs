package com.logicinfo.oms.model;

import java.math.BigDecimal;

public class SfsConfirmationPojo {
  private String omscustOrderNo;
  
  private BigDecimal storeNo;
  
  public String getOmscustOrderNo() {
    return this.omscustOrderNo;
  }
  
  public void setOmscustOrderNo(String omscustOrderNo) {
    this.omscustOrderNo = omscustOrderNo;
  }
  
  public BigDecimal getStoreNo() {
    return this.storeNo;
  }
  
  public void setStoreNo(BigDecimal storeNo) {
    this.storeNo = storeNo;
  }
}
