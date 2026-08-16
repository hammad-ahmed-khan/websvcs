package oracle.retail.sim.common.fulfillmentorderpick;

import oracle.retail.sim.common.core.SimEnum;

public enum FulfillmentOrderPickAreaType implements SimEnum<Integer> {
  NO_LOCATION(0, "No Location"),
  SHOPFLOOR(1, "Shopfloor"),
  BACKROOM(2, "Backroom"),
  DELIVERY_BAY(3, "Delivery Bay");
  
  private final int code;
  
  private final String description;
  
  FulfillmentOrderPickAreaType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static FulfillmentOrderPickAreaType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (FulfillmentOrderPickAreaType fulfillmentOrderPickAreaType : values()) {
        if (fulfillmentOrderPickAreaType.code == paramInteger.intValue())
          return fulfillmentOrderPickAreaType; 
      }  
    return NO_LOCATION;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPickAreaType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */