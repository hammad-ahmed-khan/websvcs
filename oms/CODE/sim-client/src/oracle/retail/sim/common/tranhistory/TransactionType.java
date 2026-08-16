package oracle.retail.sim.common.tranhistory;

import java.util.EnumSet;
import java.util.Set;
import oracle.retail.sim.common.core.SimEnum;

public enum TransactionType implements SimEnum<Integer> {
  DIRECT_DELIVERY(1, "Direct Delivery"),
  INVENTORY_ADJUSTMENT(2, "Inventory Adjustment"),
  LATE_SALES(3, "Late Sales"),
  POS_TRANSACTION(4, "POS Transaction"),
  RETURN_TO_FINISHER(5, "Return To Finisher"),
  RETURN_TO_VENDOR(6, "Return To Vendor"),
  RETURN_TO_WAREHOUSE(7, "Return To Warehouse"),
  STOCK_COUNT(8, "Stock Count"),
  STORE_TRANSFER(9, "Store Transfer"),
  WAREHOUSE_DELIVERY(10, "Warehouse Delivery"),
  WASTAGE(11, "Wastage"),
  DATA_SEEDING(12, "Data Seeding"),
  FULFILLMENT_ORDER(13, "Customer Order");
  
  private final int code;
  
  private final String description;
  
  TransactionType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static TransactionType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (TransactionType transactionType : values()) {
        if (transactionType.code == paramInteger.intValue())
          return transactionType; 
      }  
    return null;
  }
  
  public static Set<TransactionType> getValidTypesForTransactionHistory() {
    return EnumSet.of(POS_TRANSACTION, STOCK_COUNT, INVENTORY_ADJUSTMENT);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\tranhistory\TransactionType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */