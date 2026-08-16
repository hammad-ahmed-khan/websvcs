package oracle.retail.sim.common.productgroup;

import oracle.retail.sim.common.core.SimEnum;

public enum ProductGroupType implements SimEnum<Integer> {
  STOCK_COUNT_UNIT(1, "Unit"),
  STOCK_COUNT_UNIT_AMOUNT(2, "Unit and Amount"),
  STOCK_COUNT_PROBLEM_LINE(3, "Problem Line"),
  STOCK_COUNT_WASTAGE(4, "Wastage"),
  STOCK_COUNT_RMS_SYNC(5, "RMS Inventory Sync"),
  SHELF_REPLENISHMENT(6, "Shelf Replenishment"),
  ITEM_REQUEST(7, "Item Request");
  
  private final int code;
  
  private final String description;
  
  ProductGroupType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static ProductGroupType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (ProductGroupType productGroupType : values()) {
        if (productGroupType.code == paramInteger.intValue())
          return productGroupType; 
      }  
    return null;
  }
  
  public boolean isStockCount() {
    return (this == STOCK_COUNT_UNIT || this == STOCK_COUNT_UNIT_AMOUNT || this == STOCK_COUNT_PROBLEM_LINE);
  }
  
  public boolean isStockCountUnit() {
    return (this == STOCK_COUNT_UNIT);
  }
  
  public boolean isStockCountUnitAmount() {
    return (this == STOCK_COUNT_UNIT_AMOUNT);
  }
  
  public boolean isStockCountProblemLine() {
    return (this == STOCK_COUNT_PROBLEM_LINE);
  }
  
  public boolean isWastage() {
    return (this == STOCK_COUNT_WASTAGE);
  }
  
  public boolean isStockCountRmsSync() {
    return (this == STOCK_COUNT_RMS_SYNC);
  }
  
  public boolean isShelfReplenishment() {
    return (this == SHELF_REPLENISHMENT);
  }
  
  public boolean isItemRequest() {
    return (this == ITEM_REQUEST);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\ProductGroupType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */