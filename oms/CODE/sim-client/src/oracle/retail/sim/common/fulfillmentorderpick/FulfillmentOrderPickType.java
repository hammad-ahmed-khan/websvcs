package oracle.retail.sim.common.fulfillmentorderpick;

import oracle.retail.sim.common.core.SimEnum;

public enum FulfillmentOrderPickType implements SimEnum<Integer> {
  ORDER(0, "SIM Customer Order"),
  BIN(1, "Bin");
  
  private final int code;
  
  private final String description;
  
  FulfillmentOrderPickType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static FulfillmentOrderPickType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (FulfillmentOrderPickType fulfillmentOrderPickType : values()) {
        if (fulfillmentOrderPickType.code == paramInteger.intValue())
          return fulfillmentOrderPickType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPickType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */