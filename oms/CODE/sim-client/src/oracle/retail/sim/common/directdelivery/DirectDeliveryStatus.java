package oracle.retail.sim.common.directdelivery;

import java.util.EnumSet;
import java.util.Set;
import oracle.retail.sim.common.core.SimEnum;

public enum DirectDeliveryStatus implements SimEnum<Integer> {
  NEW(0, "New"),
  IN_PROGRESS(1, "In Progress"),
  RECEIVED(2, "Received"),
  MISSING(3, "Missing"),
  CANCELED(4, "Canceled"),
  DAMAGED(5, "Damaged"),
  DEXNEX(10, "DEX/NEX"),
  REJECTED(17, "Rejected"),
  ACTIVE(16, "Active");
  
  private final int code;
  
  private final String description;
  
  DirectDeliveryStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static DirectDeliveryStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (DirectDeliveryStatus directDeliveryStatus : values()) {
        if (directDeliveryStatus.code == paramInteger.intValue())
          return directDeliveryStatus; 
      }  
    return null;
  }
  
  public static Set<DirectDeliveryStatus> getClosedSet() {
    return EnumSet.of(CANCELED, RECEIVED, REJECTED);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */