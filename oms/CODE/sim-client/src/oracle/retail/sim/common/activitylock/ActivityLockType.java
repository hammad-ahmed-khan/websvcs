package oracle.retail.sim.common.activitylock;

import oracle.retail.sim.common.core.SimEnum;

public enum ActivityLockType implements SimEnum<String> {
  DIRECT_DELIVERY, PURCHASE_ORDER, DIRECT_DELIVERY_INVOICE, FULFILLMENT_ORDER, FULFILLMENT_ORDER_DELIVERY, FULFILLMENT_ORDER_PICK, FULFILLMENT_ORDER_REVERSE_PICK, INVENTORY_ADJUSTMENT, INVENTORY_ADJUSTMENT_REASON, INVENTORY_ADJUSTMENT_TEMPLATE, ITEM_BASKET, ITEM_REQUEST, ITEM_TICKET, SHELF_REPLENISHMENT, PRICE_CHANGE, PRODUCT_GROUP, PRODUCT_GROUP_SCHEDULE, RETURN, RETURN_REASON, STOCK_COUNT_CHILD, STORE_SEQUENCE, STORE_SEQUENCE_ALL, TRANSFER, WAREHOUSE_DELIVERY, UNKNOWN;
  
  public String getCode() {
    return name();
  }
  
  public static ActivityLockType toValue(String paramString) {
    if (paramString != null)
      try {
        return Enum.<ActivityLockType>valueOf(ActivityLockType.class, paramString);
      } catch (Throwable throwable) {
        return null;
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\activitylock\ActivityLockType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */