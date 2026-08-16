package oracle.retail.sim.common.warehousedelivery;

import java.util.Comparator;
import oracle.retail.sim.common.util.NumericOrderStringComparator;

public class WarehouseDeliverySimpleLineItemComparator implements Comparator<WarehouseDeliverySimpleLineItem> {
  private final NumericOrderStringComparator comparator = new NumericOrderStringComparator(true, false);
  
  public int compare(WarehouseDeliverySimpleLineItem paramWarehouseDeliverySimpleLineItem1, WarehouseDeliverySimpleLineItem paramWarehouseDeliverySimpleLineItem2) {
    if (paramWarehouseDeliverySimpleLineItem1 == null && paramWarehouseDeliverySimpleLineItem2 == null)
      return 0; 
    if (paramWarehouseDeliverySimpleLineItem1 == null)
      return -1; 
    if (paramWarehouseDeliverySimpleLineItem2 == null)
      return 1; 
    int i = this.comparator.compare(paramWarehouseDeliverySimpleLineItem1.getStockItem().getId(), paramWarehouseDeliverySimpleLineItem2.getStockItem().getId());
    if (i != 0)
      return i; 
    i = this.comparator.compare(paramWarehouseDeliverySimpleLineItem1.getCustomerOrderId(), paramWarehouseDeliverySimpleLineItem2.getCustomerOrderId());
    if (i != 0)
      return i; 
    i = this.comparator.compare(paramWarehouseDeliverySimpleLineItem1.getFulfillmentOrderExternalId(), paramWarehouseDeliverySimpleLineItem2.getFulfillmentOrderExternalId());
    return (i != 0) ? i : this.comparator.compare(paramWarehouseDeliverySimpleLineItem1.getReceiptDocumentId(), paramWarehouseDeliverySimpleLineItem2.getReceiptDocumentId());
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliverySimpleLineItemComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */