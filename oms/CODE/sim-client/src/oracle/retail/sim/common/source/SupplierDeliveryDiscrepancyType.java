package oracle.retail.sim.common.source;

import oracle.retail.sim.common.core.SimEnum;

public enum SupplierDeliveryDiscrepancyType implements SimEnum<Integer> {
  ALLOW(0, "Allow any discrepancy"),
  OVERAGE(1, "Allow overages but not short receipts"),
  NOT_ALLOW(2, "Do not allow any discrepancy");
  
  private final int code;
  
  private final String description;
  
  SupplierDeliveryDiscrepancyType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static SupplierDeliveryDiscrepancyType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (SupplierDeliveryDiscrepancyType supplierDeliveryDiscrepancyType : values()) {
        if (supplierDeliveryDiscrepancyType.code == paramInteger.intValue())
          return supplierDeliveryDiscrepancyType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\SupplierDeliveryDiscrepancyType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */