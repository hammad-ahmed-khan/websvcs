package oracle.retail.sim.common.postransaction;

import oracle.retail.sim.common.core.SimEnum;

public enum POSTransactionLogType implements SimEnum<Integer> {
  ERROR(1, "Error"),
  WARNING(2, "Warning"),
  INFO(3, "Info");
  
  private final int code;
  
  private final String description;
  
  POSTransactionLogType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static POSTransactionLogType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (POSTransactionLogType pOSTransactionLogType : values()) {
        if (pOSTransactionLogType.code == paramInteger.intValue())
          return pOSTransactionLogType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\postransaction\POSTransactionLogType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */