package oracle.retail.sim.common.fulfillmentorderpick;

import java.util.EnumSet;
import java.util.Set;
import oracle.retail.sim.common.core.SimEnum;

public enum FulfillmentOrderPickStatus implements SimEnum<Integer> {
  NEW(0, "New"),
  IN_PROGRESS(1, "In Progress"),
  COMPLETED(2, "Completed"),
  CANCELED(3, "Canceled"),
  ACTIVE(4, "Active");
  
  private final int code;
  
  private final String description;
  
  FulfillmentOrderPickStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static Set<FulfillmentOrderPickStatus> getClosedSet() {
    return EnumSet.of(CANCELED, COMPLETED);
  }
  
  public static FulfillmentOrderPickStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (FulfillmentOrderPickStatus fulfillmentOrderPickStatus : values()) {
        if (fulfillmentOrderPickStatus.code == paramInteger.intValue())
          return fulfillmentOrderPickStatus; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPickStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */