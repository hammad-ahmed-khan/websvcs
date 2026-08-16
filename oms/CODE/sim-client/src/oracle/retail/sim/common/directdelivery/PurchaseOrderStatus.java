package oracle.retail.sim.common.directdelivery;

import java.util.EnumSet;
import java.util.Set;
import oracle.retail.sim.common.core.SimEnum;

public enum PurchaseOrderStatus implements SimEnum<Integer> {
  UNKNOWN(0, "Unknown"),
  APPROVED(100, "Approved"),
  CLOSED(1000, "Closed"),
  ACTIVE(10000, "Active");
  
  private final int code;
  
  private final String description;
  
  PurchaseOrderStatus(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static PurchaseOrderStatus toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (PurchaseOrderStatus purchaseOrderStatus : values()) {
        if (purchaseOrderStatus.code == paramInteger.intValue())
          return purchaseOrderStatus; 
      }  
    return null;
  }
  
  public static Set<PurchaseOrderStatus> getClosedSet() {
    return EnumSet.of(CLOSED, UNKNOWN);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\PurchaseOrderStatus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */