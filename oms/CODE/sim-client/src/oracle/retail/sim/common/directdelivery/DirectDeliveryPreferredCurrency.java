package oracle.retail.sim.common.directdelivery;

import oracle.retail.sim.common.core.SimEnum;

public enum DirectDeliveryPreferredCurrency implements SimEnum<Integer> {
  STORE(0, "Store Currency"),
  SUPPLIER(1, "Supplier Currency");
  
  private final int code;
  
  private final String description;
  
  DirectDeliveryPreferredCurrency(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static DirectDeliveryPreferredCurrency toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (DirectDeliveryPreferredCurrency directDeliveryPreferredCurrency : values()) {
        if (directDeliveryPreferredCurrency.code == paramInteger.intValue())
          return directDeliveryPreferredCurrency; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryPreferredCurrency.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */