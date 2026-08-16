package oracle.retail.sim.common.directdelivery;

import oracle.retail.sim.common.core.SimEnum;

public enum DirectDeliveryInvoiceEntryType implements SimEnum<Integer> {
  ENABLED(0, "Enabled"),
  DISABLED(1, "Disabled"),
  UNIQUE(2, "Unique");
  
  private final int code;
  
  private final String description;
  
  DirectDeliveryInvoiceEntryType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static DirectDeliveryInvoiceEntryType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (DirectDeliveryInvoiceEntryType directDeliveryInvoiceEntryType : values()) {
        if (directDeliveryInvoiceEntryType.code == paramInteger.intValue())
          return directDeliveryInvoiceEntryType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryInvoiceEntryType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */