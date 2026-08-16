package oracle.retail.sim.common.fulfillmentorder;

import java.util.EnumSet;
import java.util.Set;
import oracle.retail.sim.common.core.SimEnum;

public enum FulfillmentOrderType implements SimEnum<Integer> {
  LAYAWAY(1, "Layaway"),
  PICKUP_AND_DELIVERY(2, "Pickup and Delivery Orders"),
  CUSTOMER_ORDER(3, "Customer Order"),
  PENDING_PURCHASE(4, "Pending Purchase"),
  SPECIAL_ORDER(5, "Special Order"),
  WEB_ORDER(6, "Web Order");
  
  private final int code;
  
  private final String description;
  
  FulfillmentOrderType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static FulfillmentOrderType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (FulfillmentOrderType fulfillmentOrderType : values()) {
        if (fulfillmentOrderType.code == paramInteger.intValue())
          return fulfillmentOrderType; 
      }  
    return null;
  }
  
  public static Set<FulfillmentOrderType> getOrderTypes() {
    return EnumSet.of(LAYAWAY, PICKUP_AND_DELIVERY, CUSTOMER_ORDER, SPECIAL_ORDER, WEB_ORDER);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */