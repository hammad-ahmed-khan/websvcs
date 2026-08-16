package oracle.retail.sim.common.postransaction;

import java.util.EnumSet;
import java.util.Set;
import oracle.retail.sim.common.core.SimEnum;

public enum POSTransactionType implements SimEnum<Integer> {
  SALE(1, "Sale"),
  RETURN(2, "Return"),
  VOID_SALE(3, "Void Sale"),
  VOID_RETURN(4, "Void Return"),
  ORDER_CANCEL(5, "Cancel Reservation"),
  ORDER_NEW(6, "New"),
  ORDER_CANCEL_FULFILL(7, "Cancel Fulfill"),
  ORDER_FULFILL(8, "Fulfill");
  
  private final int code;
  
  private final String description;
  
  POSTransactionType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static POSTransactionType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (POSTransactionType pOSTransactionType : values()) {
        if (pOSTransactionType.code == paramInteger.intValue())
          return pOSTransactionType; 
      }  
    return null;
  }
  
  public static Set<POSTransactionType> getValidTypesForInventoryAdjustment() {
    return EnumSet.of(RETURN, VOID_SALE);
  }
  
  public static Set<POSTransactionType> getSaleTransactionTypes() {
    return EnumSet.of(SALE, RETURN, VOID_RETURN, VOID_SALE);
  }
  
  public static Set<POSTransactionType> getOrderTransactionTypes() {
    return EnumSet.of(ORDER_CANCEL, ORDER_NEW, ORDER_CANCEL_FULFILL, ORDER_FULFILL);
  }
  
  public static Set<POSTransactionType> getNoWastageTransactionTypes() {
    return EnumSet.of(RETURN);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\postransaction\POSTransactionType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */