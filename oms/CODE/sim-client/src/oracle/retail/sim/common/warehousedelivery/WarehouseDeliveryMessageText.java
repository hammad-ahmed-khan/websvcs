package oracle.retail.sim.common.warehousedelivery;

import oracle.retail.sim.common.business.MessageText;

public enum WarehouseDeliveryMessageText implements MessageText {
  CANCELED_STATE_NOT_RECEIVABLE("Canceled deliveries may not be received."),
  CARTONS_NOT_RECEIVED("One or more containers not received yet. Their line items will be updated with 0 quantity. Do you want to receive the delivery?"),
  CONFIRM_REOPEN("Are you sure you want to re-open the delivery?"),
  DELIVERY_INVALID_STATUS("Invalid delivery status."),
  DESTINATION_CHANGED("Destination may not be changed on an existing delivery."),
  DUPLICATE_CARTON("This carton already exists for this delivery."),
  DUPLICATE_ITEM("This item already exists as a line item."),
  FULFILLMENT_ORDER_RELATED("This delivery contains customer order items."),
  FULFILLMENT_ORDER_UNAVAILABLE("Customer order information is unavailable for this delivery."),
  ID_REQUIRED("Delivery ID field is required."),
  IN_PROGRESS_TO_RECEIVE_ERROR("Only in progress deliveries may be received."),
  QUICK_RECEIPT_ADJUST_DATE_EXPIRED("Container cannot be received - delivery has passed its allowed receipt date. Please scan the next container."),
  QUICK_RECEIPT_CARTON_MISSING("Container is listed as Missing. Please scan the next container."),
  QUICK_RECEIPT_DELIVERY_IS_LOCKED("Container cannot be received. The delivery is currently locked."),
  QUICK_RECEIPT_NO_DELIVERY("No delivery found for container."),
  QUICK_RECEIPT_UINS_REQUIRED("Container has UIN items and detailed warehouse receiving must be used."),
  RECEIVE_ALL_CARTONS("Would you like to receive all containers on the delivery?"),
  RECEIVE_CONFIRM("This delivery will be received and cannot be changed. Do you want to receive it?"),
  RECEIVED_ERROR("Received deliveries may not change status."),
  UIN_DISPATCH_QTY_RECEIVED_MISMATCH("The number of {0} captured for the Item {1} does not match the quantity expected. If you continue, the received quantity will be updated to {2}. Continue?"),
  UIN_RECEIVE_WITH_CONFIRMATION("Item(s) in Container {0} contains items that require UINs to be captured. Continuing will set the Received Quantity for theses items to 0. Do you wish to continue?"),
  UIN_VALIDATE_FOR_FINISHER("This item was not originally sent to the finisher. Do you want to receive it?"),
  UNRECEIVE_ALL_CARTONS("Would you like to un-receive all containers on the delivery?"),
  WAREHOUSE_DELIVERY_PRINTED("The warehouse delivery printed successfully.");
  
  private final String message;
  
  WarehouseDeliveryMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */