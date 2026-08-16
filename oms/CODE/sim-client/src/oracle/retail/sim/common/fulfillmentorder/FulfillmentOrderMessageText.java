package oracle.retail.sim.common.fulfillmentorder;

import oracle.retail.sim.common.business.MessageText;

public enum FulfillmentOrderMessageText implements MessageText {
  ACTUAL_CANNOT_EXCEED_SUGGESTED("Actual pick quantity cannot exceed the suggested pick quantity."),
  ACTUAL_REVERSE_PICK_CANNOT_EXCEED_SUGGESTED("It is not allowed to reverse more then what is suggested."),
  ACTUAL_REVERSE_PICK_CANNOT_EXCEED_PHYSICALLY_PICKED("It is not possible to reverse more than what is physically picked."),
  ACTUAL_REVERSE_PICK_CANNOT_EXCEED_REMAINING("The reverse pick quantity is larger than the remaining quantity."),
  ALL_QUANTITIES_ZERO_CANCEL_REVERSE_PICK("All quantities are zero. Are you sure you want to cancel the reverse pick?"),
  BIN_ID_IN_USE("{0} is already in use on an open pick. Please select a different bin id."),
  BIN_LABELS_PRINTED("Customer Order Bin Labels printed"),
  CANCEL_SUBMIT_CONFIRM("Are you sure you want to go back to In Progress?"),
  CONFIRM_DELIVERY_QUANTITY("The quantity is greater than the available stock on hand. Is the quantity correct?"),
  CONFIRM_PICK_CONFIRM("Are you sure you want to confirm the pick?"),
  CONFIRM_REVERSE_PICK_CONFIRM("Are you sure you want to confirm the reverse pick?"),
  CUSTOMER_ORDER_DOES_NOT_HAVE_ANY_ITEM_TO_REVERSE_PICK("The customer order does not have any items to reverse pick."),
  CUSTOMER_ORDER_QUANTITY_EXCEEDED("The Total Actual Pick Quantity cannot exceed the customer order quantity for the item."),
  DELIVERY_DELETE_CONFIRM("Are you sure you want to delete the selected customer order deliveries?"),
  DELIVERY_REPORT_PRINTED("Customer Order Delivery Report printed."),
  DISPATCH_CONFIRM("Are you sure you want to dispatch the delivery now?"),
  DUPLICATE_BIN_ID("Bin {0} cannot be entered more than once."),
  EMPTY_DELIVERY_CONFIRM("All quantities are zero. Are you sure you want to cancel the delivery?"),
  EMPTY_PICK_WARNING("This pick does not contain any items and will be canceled."),
  EMPTY_BIN_QUANTITY("The Bin Quantity cannot be left blank."),
  INVALID_BINS_QUANTITY("You must enter {0} bin(s)."),
  INVALID_CUSTOMER_ORDER_ID("Invalid customer order ID."),
  INVALID_CUSTOMER_ORDER_STATUS("Customer order is closed."),
  INVALID_CUSTOMER_ORDER_STATUS_DELIVERY_CREATE("Only 'New' or 'In Progress' customer orders can be delivered."),
  INVALID_CUSTOMER_ORDER_STATUS_PICK_CREATE("Only 'New' or 'In Progress' customer orders can be picked."),
  INVALID_CUSTOMER_ORDER_STATUS_REVERSE_PICK_CREATE("Only 'New' or 'In Progress' customer orders can be reverse picked."),
  INVALID_DELIVERY_ID("Invalid customer order delivery id."),
  INVALID_DELIVERY_STATUS_TO_CANCEL_SUBMIT("Customer Order Deliveries must be Submitted in order to cancel submit."),
  INVALID_DELIVERY_STATUS_TO_DELETE("Customer Order Deliveries must be In Progress status in order to delete."),
  INVALID_DELIVERY_STATUS_TO_DISPATCH("Customer Order Deliveries must be In Progress or Submitted status in order to dispatch."),
  INVALID_DELIVERY_STATUS_TO_SUBMIT("Customer Order Deliveries must be In Progress in order to submit."),
  INVALID_DELIVERY_TYPE_REVERSE_PICK("Only Web Orders can have customer order reverse pick lists created."),
  INVALID_PICK_ID("Invalid customer order pick id."),
  INVALID_PICK_STATUS_TO_CONFIRM("Customer Order Pick must be New or In Progress status in order to confirm."),
  INVALID_PICK_STATUS_TO_DELETE("Customer Order Pick must be New or In Progress status in order to delete."),
  INVALID_REVERSE_PICK_ID("Invalid customer order reverse pick id."),
  INVALID_REVERSE_PICK_STATUS_TO_DELETE("Customer Order Reverse Pick must be New or In Progress status in order to delete."),
  INVALID_REVERSE_PICK_STATUS_TO_CONFIRM("Customer Order Reverse Pick must be New or In Progress status in order to confirm."),
  INVALID_STATUS_FOR_UPDATE_ORDER("Customer Orders must be New or In Progress status in order to update."),
  INVALID_STATUS_FOR_DELIVERY_DISPATCH("Customer Order is no longer in the proper status for delivery."),
  INVALID_STATUS_FOR_PICK_CONFIRM("SIM Customer Order {0} is no longer in the proper status for picking."),
  INVALID_STATUS_FOR_REVERSE_PICK_CONFIRM("Customer Order is no longer in the proper status for reverse picking."),
  INVALID_UNIT_OF_MEASURE("Invalid unit of measure"),
  ITEM_EXCEEDS_ALLOWED_QUANTITY("Item {0} on the delivery exceeds the allowed delivery quantity. Please refresh your data."),
  ITEM_SUBSTITUTION_NOT_ALLOWED("This item does not allow for substitution."),
  MISSING_ROW("Please select a row."),
  NO_DELIVERY_SELECTED("You must select a customer order to enter the customer order delivery dialogue."),
  NO_DELIVERY_SELECTED_REVERSE_PICKING("You must select a customer order to reverse pick."),
  NO_OUTSTANDING_QUANTITIES_ALLOWED("The delivery has outstanding quantities. Please add those before dispatching."),
  NO_QUANTITIES_TO_PICK("There are no items to pick at this time."),
  NO_REMAINING_QUANTITY_MUST_REVERSE_ALL("Item does not have a remaining quantity and must have entire pick quantity removed."),
  OPEN_REVERSE_PICKS_CONFIRM("Open reverse picks exist, are you sure you want to dispatch the delivery now?"),
  OPEN_REVERSE_PICKS_SUBMIT_CONFIRM("Open reverse picks exist, are you sure you want to submit the delivery now?"),
  OUTSTANDING_QUANTITIES_CONFIRM("The delivery has outstanding quantities. Are you sure you want to dispatch?"),
  PICK_DELETE_CONFIRM("Are you sure you want to delete the selected customer order picks?"),
  PICKED_EXCEEDS_SUGGESTED("Actual pick quantity cannot exceed the suggested pick quantity."),
  PICK_REPORT_PRINTED("Customer Order Pick Report printed."),
  REVERSE_PICK_REPORT_PRINTED("Customer Order Reverse Pick Report printed."),
  PICKING_REQUIRED("Customer Order requires picking in order to be delivered."),
  PICKING_REQUIRED_PARTIAL_DELIVERY("This order cannot be delivered unless all items have been picked."),
  QUANTITY_CANNOT_EXCEED_PICKED("Quantity being delivered cannot exceed the picked quantity."),
  QUANTITY_CANNOT_EXCEED_REMAINING("Quantity being delivered cannot exceed the remaining order quantity."),
  REMAINING_PICK_QUANTITY_CHANGED("The remaining order quantity for Item {0} has changed since the pick was created. The actual pick quantity cannot exceed the remaining order quantity."),
  REPORT_PRINTED("Customer Order Report printed."),
  REVERSE_PICK_DELETE_CONFIRM("Are you sure you want to delete the selected customer order reverse picks?"),
  SELECT_DELIVERY_ITEM("Item exists on Customer Order Delivery more than once. Please select the item."),
  SELECT_PICK_ITEM("Item exists on Customer Order Pick List more than once. Please select the item."),
  SUBSTITUTION_UOM_MISMATCH("Please select an item with a UOM of {0}."),
  TOLERANCE_VALUES_EXCEEDED("The Total Actual Pick quantity cannot exceed the defined tolerance values for the original item."),
  UIN_CANNOT_BE_DISPATCHED("{0} is currently in {1} and cannot be dispatched."),
  UIN_DISPATCH_QTY_MISMATCH("The number of {0} captured for the Item {1} does not match the quantity expected. If you continue, the shipped quantity will be updated to {2}. Continue?"),
  UIN_STATUS_INVALID("{0} {1} is currently in {2} and cannot be added to the customer order delivery."),
  UIN_STORE_MISMATCH("{0} {1} cannot be added to the delivery as it is currently not assigned to your store."),
  UNABLE_TO_LOCK_ORDER_UPDATE("Unable to lock customer order for update."),
  UNABLE_TO_DEFAULT_SUBSTITUTES_EXIST("Quantities cannot be defaulted when substitute items exist."),
  WEB_INVALID_CUSTOMER_ORDER("Invalid customer order ID or invalid customer order state for intended action."),
  WEB_INVALID_ITEM_ACTION("Invalid action to perform on pos transaction item"),
  WEB_INVALID_QUANTITY("A positive quantity is required"),
  WEB_INVALID_RESERVATION_TYPE("Reservation Type is required"),
  WEB_INVALID_TIMESTAMP("A timestamp is required");
  
  private final String message;
  
  FulfillmentOrderMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */