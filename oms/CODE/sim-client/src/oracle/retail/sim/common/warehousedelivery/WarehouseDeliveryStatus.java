package oracle.retail.sim.common.warehousedelivery;

import java.util.EnumSet;
import java.util.Set;
import oracle.retail.sim.common.core.SimEnum;

public enum WarehouseDeliveryStatus implements SimEnum<Integer> {
  NEW(0, "New"),
  IN_PROGRESS(1, "In Progress"),
  RECEIVED(2, "Received"),
  MISSING(3, "Missing"),
  CANCELED(4, "Canceled"),
  DAMAGED(5, "Damaged"),
  ACTIVE(16, "Active");
  
  private final int code;
  
  private final String description;
  
  WarehouseDeliveryStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static WarehouseDeliveryStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (WarehouseDeliveryStatus warehouseDeliveryStatus : values()) {
        if (warehouseDeliveryStatus.code == paramInteger.intValue())
          return warehouseDeliveryStatus; 
      }  
    return null;
  }
  
  public static Set<WarehouseDeliveryStatus> getClosedSet() {
    return EnumSet.of(CANCELED, RECEIVED);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */