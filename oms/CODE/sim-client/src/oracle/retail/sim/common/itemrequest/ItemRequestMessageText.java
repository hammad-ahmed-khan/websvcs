package oracle.retail.sim.common.itemrequest;

import oracle.retail.sim.common.business.MessageText;

public enum ItemRequestMessageText implements MessageText {
  DELETE_CANCELLED("Item requests must be in Pending status in order to delete."),
  DELETE_CONFIRM("Are you sure you want to delete the selected orders?"),
  DELETE_FAILURE("Unable to delete the following item requests:"),
  DELETE_PERMISSION_ERROR("It is not possible to delete item requests that contain timeslots which you don't have permission to access."),
  DISCONTINUED_ITEM("The item is discontinued and cannot be added."),
  EDIT_PERMISSION_ERROR("It is not possible to edit items on requests that have been assigned timeslots which you don't have permission to access."),
  INVALID_ITEM_STATE("The item is {0} and cannot be added."),
  INVALID_REQUEST_DATE("Request Delivery Date must be greater than today. Please enter another date."),
  INVALID_CANCEL_STATE("Orders must be in Pending status in order to delete."),
  INCOMPLETE_LINES("Order contains incomplete line items."),
  INVALID_ITEM("Item entered is not a valid item."),
  ITEM_ALREADY_EXISTS("Item already exists on order."),
  ITEM_REQ_DELETED_ITEM("The item is deleted and cannot be added."),
  ITEM_REQ_INACTIVE_ITEM("The item is inactive and cannot be added."),
  LINE_ALREADY_EXISTS("This item already exists as a line item."),
  LINE_ITEM_NO_QUANTITY("Quantity is required on each line item."),
  LINE_ITEM_DEL_PRIV_ERROR("You do not have the necessary privileges to delete one or more of the selected line items."),
  MAX_ITEM_COUNT_EXCEEDED("You have exceeded the maximum line item limit and cannot add more items."),
  MISSING_LINE_ITEM("At least one line item must be present."),
  MISSING_DELIVERY_DATE_2("The request delivery date is not set."),
  MISSING_DELIVERY_DATE("Request delivery date must be set."),
  MISSING_OPERATOR("An operator must be set."),
  PROMOTION_ITEM_ERROR("This promotion does not contain any store order replenishment items."),
  REPORT_PRINTED("Item Request Report printed."),
  REQUEST_DATE_IN_PAST_ERROR("Request Delivery date must be greater than or equal to today."),
  REQUEST_DATE_ERROR("Item {0} cannot be ordered based on the request by date."),
  REQUEST_ITEM_CONFIRM("{0} is {1}, are you sure you want to use this item?"),
  SUBMIT_CONFIRM("Are you sure you want to submit the request?"),
  SUPPLIER_ITEMS_ERROR("This supplier does not contain any store order replenishment items.");
  
  private final String message;
  
  ItemRequestMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\itemrequest\ItemRequestMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */