package oracle.retail.sim.common.fulfillmentorder;

import java.util.EnumSet;
import java.util.Set;
import oracle.retail.sim.common.core.SimEnum;

public enum FulfillmentOrderStatus implements SimEnum<Integer> {
  NEW(0, "New"),
  IN_PROGRESS(1, "In Progress"),
  COMPLETED(2, "Completed"),
  CANCELED(3, "Canceled"),
  ACTIVE(4, "Active");
  
  private final int code;
  
  private final String description;
  
  FulfillmentOrderStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static FulfillmentOrderStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (FulfillmentOrderStatus fulfillmentOrderStatus : values()) {
        if (fulfillmentOrderStatus.code == paramInteger.intValue())
          return fulfillmentOrderStatus; 
      }  
    return null;
  }
  
  public static Set<FulfillmentOrderStatus> getClosedSet() {
    return EnumSet.of(CANCELED, COMPLETED);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */