package oracle.retail.sim.common.postransaction;

import oracle.retail.sim.common.core.SimEnum;

public enum POSTransactionRequestType implements SimEnum<Integer> {
  SALE(1, "Sale"),
  ORDER(2, "Order"),
  RESA(3, "ReSA");
  
  private final int code;
  
  private final String description;
  
  POSTransactionRequestType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static POSTransactionRequestType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (POSTransactionRequestType pOSTransactionRequestType : values()) {
        if (pOSTransactionRequestType.code == paramInteger.intValue())
          return pOSTransactionRequestType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\postransaction\POSTransactionRequestType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */