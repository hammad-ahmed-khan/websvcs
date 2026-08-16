package oracle.retail.sim.common.directdelivery;

import oracle.retail.sim.common.business.MessageText;

public enum DirectDeliveryMessageText implements MessageText {
  APPLY_ITEM_CONFIRM("Are you sure you want to apply all items and received quantities from the delivery?"),
  ASN_NOT_SELECTED("Please select ASN."),
  ASN_SINGLE_OPEN_QUESTION("An open ASN exists for the purchase order. Would you like to apply the ASN?"),
  ASN_MULTIPLE_OPEN_QUESTION("Multiple open ASNs exist for the purchase order. Would you like to choose one?"),
  ASN_RECEIVE_ALL_ERROR("Receive All is only valid when receiving against existing purchase orders."),
  ASN_REOPEN_DELIVERY("Are you sure you want to re-open the delivery?"),
  CANCEL_STATUS_ERROR("Closed delivery cannot be canceled."),
  CANCEL_ASN_ERROR("ASN delivery cannot be canceled."),
  CANCELED_STATE_NOT_RECEIVABLE("Canceled deliveries may not be received."),
  DELIVERY_CREATE_NOT_ALLOWED("User is not authorized to create this Delivery."),
  DELIVERY_INVALID_STATUS("Invalid delivery status."),
  DELIVERY_NOT_FOUND("No delivery found for this ID."),
  DESTINATION_CHANGED("Destination may not be changed on an existing delivery."),
  DIRECT_DELIVERY_PRINTED("Direct Delivery Report printed."),
  DSD_REMOVE_DAMAGES_OVER_RECEIVE("Any damaged or over received quantities will be removed from this delivery."),
  DSD_REMOVE_OVER_RECEIVE("Any quantities received above the expected quantity will be removed from this delivery."),
  DSD_REMOVE_DAMAGES("Any damaged quantities are removed from the delivery."),
  DSD_REMOVE_UIN_DAMAGES_ERROR("Damaged UIN quantity exists. Please remove them from the delivery before confirming."),
  DSD_REMOVE_UIN_OVER_RECEIVE_ERROR("Over received UIN quantity exists (including damages). Please remove UIN quantities that exceed the expected quantity."),
  DUPLICATE_CARTON("This carton already exists for this delivery."),
  DUPLICATE_INVOICE_NUMBER("Invoice number already exists for this supplier."),
  DUPLICATE_ITEM("This item already exists as a line item."),
  FULFILLMENT_ORDER_RELATED("This delivery contains customer order items."),
  ID_REQUIRED("Delivery ID field is required."),
  IN_PROGRESS_TO_RECEIVE_ERROR("Only in progress deliveries may be received."),
  IN_PROGRESS_TO_REJECT_ERROR("Only in progress deliveries may be rejected."),
  INVALID_VENDOR("Vendor does not source this item."),
  INVOICE_DATE_REQUIRED("Invoice date is required."),
  OVER_RECEIVING_NOT_ALLOWED("The total quantity received (including damages) cannot be greater than the remaining quantity on the purchase order. Please correct the values."),
  PO_INVALID_ENTRY("Invalid PO entered."),
  PO_INVALID_STATUS("Invalid PO status."),
  PO_NO_NEW_ORDERS("No new orders can be created for this supplier."),
  PO_NO_OPEN_ASNS("No open ASNs exist for this PO."),
  PO_NONE_SELECTED("A purchase order must be selected."),
  PO_NOT_FOUND("No Purchase Order found for this ID."),
  PO_NOT_RECEIVED("The PO does not have any item tickets because it has not been received."),
  PO_NUM_REQUIRED("PO field is required."),
  RECEIVE_CONFIRM("This delivery will be received and cannot be changed. Do you want to receive it?"),
  RECEIVED_ERROR("Received deliveries may not change status."),
  REJECT_CONFIRM("Are you sure you want to reject this delivery?"),
  REJECT_ERROR("Only deliveries with an ASN for a PO may be rejected."),
  SUPPLIER_DISCREPANCY_NOT_ALLOWED("Discrepancies are not allowed for this supplier."),
  SUPPLIER_DISCREPANCY_OVERRIDE("Discrepancies are not allowed for this supplier. Do you wish to override?"),
  UIN_DISPATCH_QTY_RECEIVED_MISMATCH("The number of {0} captured for the Item {1} does not match the quantity expected. If you continue, the received quantity will be updated to {2}. Continue?");
  
  private final String message;
  
  DirectDeliveryMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */