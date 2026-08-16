package org.extra.deliveryUpdate;

import java.math.BigDecimal;

public class DeliveryRequest
{
  public String orderNo;
  public String classification;
  public int omsCustOrdNo;
  public String firstName;
  public String lastName;
  public String address;
  public String mobileNo;
  
  public String getOrderNo()
  {
    return orderNo;
  }
  public void setOrderNo(String orderNo)
  {
    this.orderNo = orderNo;
  }
  public String getClassification()
  {
    return classification;
  }
  public void setClassification(String classification)
  {
    this.classification = classification;
  }
  public int getOmsCustOrdNo()
  {
    return omsCustOrdNo;
  }
  public void setOmsCustOrdNo(int omsCustOrdNo)
  {
    this.omsCustOrdNo = omsCustOrdNo;
  }
  
  public String getFirstName()
  {
    return firstName;
  }
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }
  public String getLastName()
  {
    return lastName;
  }
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }
  public String getAddress()
  {
    return address;
  }
  public void setAddress(String address)
  {
    this.address = address;
  }
  public String getMobileNo()
  {
    return mobileNo;
  }
  public void setMobileNo(String mobileNo)
  {
    this.mobileNo = mobileNo;
  }
  
}
