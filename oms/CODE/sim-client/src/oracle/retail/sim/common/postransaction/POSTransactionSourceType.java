package oracle.retail.sim.common.postransaction;

import oracle.retail.sim.common.core.SimEnum;

public enum POSTransactionSourceType implements SimEnum<Integer> {
  RESA(1, "RESA"),
  POS(2, "POS");
  
  private final int code;
  
  private final String description;
  
  POSTransactionSourceType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static POSTransactionSourceType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (POSTransactionSourceType pOSTransactionSourceType : values()) {
        if (pOSTransactionSourceType.code == paramInteger.intValue())
          return pOSTransactionSourceType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\postransaction\POSTransactionSourceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */