package oracle.retail.sim.common.fulfillmentorder;

import oracle.retail.sim.common.core.SimEnum;

public enum FulfillmentOrderMgmtQueryStatus implements SimEnum<Integer> {
  OPEN(0, "Open"),
  CLOSED(1, "Closed");
  
  private final int code;
  
  private final String description;
  
  FulfillmentOrderMgmtQueryStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static FulfillmentOrderMgmtQueryStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (FulfillmentOrderMgmtQueryStatus fulfillmentOrderMgmtQueryStatus : values()) {
        if (fulfillmentOrderMgmtQueryStatus.code == paramInteger.intValue())
          return fulfillmentOrderMgmtQueryStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderMgmtQueryStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */