package oracle.retail.sim.common.fulfillmentorder;

import oracle.retail.sim.common.core.SimEnum;

public enum CustomerAddressType implements SimEnum<String> {
  BILLING("Billing Address"),
  DELIVERY("Delivery Address");
  
  private String description;
  
  CustomerAddressType(String paramString1) {
    this.description = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String toString() {
    return this.description;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\CustomerAddressType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */