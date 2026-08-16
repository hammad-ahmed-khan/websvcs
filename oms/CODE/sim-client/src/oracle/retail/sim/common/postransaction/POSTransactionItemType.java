package oracle.retail.sim.common.postransaction;

import oracle.retail.sim.common.core.SimEnum;

public enum POSTransactionItemType implements SimEnum<Integer> {
  ITEM(1, "Item"),
  UPC(2, "UPC");
  
  private final int code;
  
  private final String description;
  
  POSTransactionItemType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static POSTransactionItemType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (POSTransactionItemType pOSTransactionItemType : values()) {
        if (pOSTransactionItemType.code == paramInteger.intValue())
          return pOSTransactionItemType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\postransaction\POSTransactionItemType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */