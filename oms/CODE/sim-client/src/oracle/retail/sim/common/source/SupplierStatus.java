package oracle.retail.sim.common.source;

import oracle.retail.sim.common.core.SimEnum;

public enum SupplierStatus implements SimEnum<String> {
  ACTIVE("A", "Active"),
  INACTIVE("I", "Inactive");
  
  private final String code;
  
  private final String description;
  
  SupplierStatus(String paramString1, String paramString2) {
    this.code = paramString1;
    this.description = paramString2;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.description;
  }
  
  public static SupplierStatus toValue(String paramString) {
    if (paramString != null)
      for (SupplierStatus supplierStatus : values()) {
        if (supplierStatus.code.equals(paramString))
          return supplierStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\SupplierStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */