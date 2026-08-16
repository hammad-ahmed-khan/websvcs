package oracle.retail.sim.common.business;

import oracle.retail.sim.common.core.SimEnum;

public enum FunctionalArea implements SimEnum<Integer> {
  WAREHOUSE_DELIVERY_RECEIPT(0, "Warehouse Delivery Receipt"),
  DIRECT_DELIVERY_RECEIPT(1, "Direct Delivery Receipt"),
  CREATE_TRANSFER(2, "Create Transfer"),
  DISPATCH_TRANSFER(3, "Dispatch Transfer"),
  RECEIVE_TRANSFER(4, "Receive Transfer"),
  RECEIPT_ADJUSTMENT(5, "Receipt Adjustment"),
  CREATE_RETURN(6, "Create Return"),
  DISPATCH_RETURN(7, "Dispatch Return"),
  INVENTORY_ADJUSTMENT(8, "Inventory Adjustment"),
  STOCK_COUNT(9, "Stock Count"),
  STOCK_RECOUNT(10, "Stock Re-Count"),
  STOCK_COUNT_AUTHORIZED(11, "Stock Count Authorized"),
  MANUAL(12, "Manual"),
  POS_SALE(13, "POS Sale"),
  POS_RETURN(14, "POS Return"),
  POS_SALES_VOID(15, "POS Sale Void"),
  POS_RETURN_VOID(16, "POS Return Void"),
  UIN_WEB_SERVICE(17, "UIN Web Service"),
  CUSTOMER_ORDER(18, "Customer Order"),
  SPECIAL_ORDER(19, "Special Order");
  
  private final int code;
  
  private final String description;
  
  FunctionalArea(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static FunctionalArea toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (FunctionalArea functionalArea : values()) {
        if (functionalArea.code == paramInteger.intValue())
          return functionalArea; 
      }  
    return null;
  }
  
  public boolean isDirectDelivery() {
    return (this == DIRECT_DELIVERY_RECEIPT);
  }
  
  public boolean isReturn() {
    return (this == CREATE_RETURN || this == DISPATCH_RETURN);
  }
  
  public boolean isTransfer() {
    return (this == CREATE_TRANSFER || this == DISPATCH_TRANSFER || this == RECEIVE_TRANSFER);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\FunctionalArea.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */