package oracle.retail.sim.common.storeorder;

import oracle.retail.sim.common.core.SimEnum;

public enum ItemSaleType implements SimEnum<String> {
  PROMOTION("P", "Promotional"),
  CLEARANCE("C", "Clearance"),
  REGULAR("R", "Regular");
  
  private String code;
  
  private String description;
  
  ItemSaleType(String paramString1, String paramString2) {
    this.code = paramString1;
    this.description = paramString2;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ItemSaleType toValue(String paramString) {
    if (paramString != null)
      for (ItemSaleType itemSaleType : values()) {
        if (itemSaleType.code.equals(paramString))
          return itemSaleType; 
      }  
    return REGULAR;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storeorder\ItemSaleType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */