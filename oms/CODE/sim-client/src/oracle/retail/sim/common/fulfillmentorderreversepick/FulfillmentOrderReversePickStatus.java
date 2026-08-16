package oracle.retail.sim.common.fulfillmentorderreversepick;

import java.util.EnumSet;
import java.util.Set;
import oracle.retail.sim.common.core.SimEnum;

public enum FulfillmentOrderReversePickStatus implements SimEnum<Integer> {
  NEW(0, "New"),
  IN_PROGRESS(1, "In Progress"),
  COMPLETED(2, "Completed"),
  CANCELED(3, "Canceled");
  
  private final int code;
  
  private final String description;
  
  FulfillmentOrderReversePickStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static FulfillmentOrderReversePickStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (FulfillmentOrderReversePickStatus fulfillmentOrderReversePickStatus : values()) {
        if (fulfillmentOrderReversePickStatus.code == paramInteger.intValue())
          return fulfillmentOrderReversePickStatus; 
      }  
    return null;
  }
  
  public static Set<FulfillmentOrderReversePickStatus> getActiveSet() {
    return EnumSet.of(NEW, IN_PROGRESS);
  }
  
  public static Set<FulfillmentOrderReversePickStatus> getClosedSet() {
    return EnumSet.of(CANCELED, COMPLETED);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderreversepick\FulfillmentOrderReversePickStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */