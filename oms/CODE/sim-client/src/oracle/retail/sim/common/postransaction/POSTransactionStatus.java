package oracle.retail.sim.common.postransaction;

import oracle.retail.sim.common.core.SimEnum;

public enum POSTransactionStatus implements SimEnum<Integer> {
  NEW(0),
  PROCESSED(1),
  FAILED(2),
  RETRY(3),
  REVERTED(4);
  
  private final int code;
  
  POSTransactionStatus(int paramInt1) {
    this.code = paramInt1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public static POSTransactionStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (POSTransactionStatus pOSTransactionStatus : values()) {
        if (pOSTransactionStatus.code == paramInteger.intValue())
          return pOSTransactionStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\postransaction\POSTransactionStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */