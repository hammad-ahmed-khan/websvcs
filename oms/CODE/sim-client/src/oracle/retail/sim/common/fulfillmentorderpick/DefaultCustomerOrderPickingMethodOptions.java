package oracle.retail.sim.common.fulfillmentorderpick;

import oracle.retail.sim.common.core.SimEnum;

public enum DefaultCustomerOrderPickingMethodOptions implements SimEnum<Integer> {
  SIM_CUSTOMER_ORDER(0, "SIM Customer Order"),
  BIN(1, "Bin");
  
  private final int code;
  
  private final String description;
  
  DefaultCustomerOrderPickingMethodOptions(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static DefaultCustomerOrderPickingMethodOptions toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (DefaultCustomerOrderPickingMethodOptions defaultCustomerOrderPickingMethodOptions : values()) {
        if (defaultCustomerOrderPickingMethodOptions.code == paramInteger.intValue())
          return defaultCustomerOrderPickingMethodOptions; 
      }  
    return null;
  }
  
  public static DefaultCustomerOrderPickingMethodOptions toValue(String paramString) {
    if (paramString != null)
      for (DefaultCustomerOrderPickingMethodOptions defaultCustomerOrderPickingMethodOptions : values()) {
        if (defaultCustomerOrderPickingMethodOptions.description.equals(paramString))
          return defaultCustomerOrderPickingMethodOptions; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\DefaultCustomerOrderPickingMethodOptions.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */