package oracle.retail.sim.common.storeorder;

import oracle.retail.sim.common.business.MessageText;

public enum StoreOrderMessageText implements MessageText {
  BAD_CONNECTION("The connection to store purchase order system cannot be made at this time."),
  ENGINE_BUSINESS_ERROR("The store order is being rejected by the ordering engine business rules. Please check the log file for details."),
  COMMUNICATION_FAILURE("Communication problem between SIM and RSL/RMS!"),
  DATE_BEFORE_TODAY_ERROR("Not before date must be greater than or equal to today's date."),
  DEAL_REQUIRES_ITEM("Please select an item from the table. The deals query requires an item."),
  DELETE_FAILED("Error deleting the selected Store Order(s). Please try again."),
  INVALID_ITEM("Item entered is not a valid item."),
  INVALID_DELETE_STATUS("Cannot delete Store Orders that are not in Pending status."),
  ITEM_ALREADY_EXISTS("This item already exists as a line item."),
  ITEM_NOT_AVAILABLE("Item must be available from the selected supplier."),
  LINE_ITEM_RETRIEVE_FAILED("Could not retrieve line items for this Store-Order."),
  MISSING_DEALS("There are no deals associated with this store, supplier and item."),
  MISSING_FROM_LOCATION("From-Location must be set."),
  MISSING_NOT_AFTER_DATE("Not after date must be set."),
  MISSING_NOT_BEFORE_DATE("Not before date must be set."),
  MISSING_LINE_ITEM("Please select an item."),
  MISSING_LINE_ITEMS("At least one line item must be present."),
  MISSING_QUANTITY("A quantity must be entered for each item."),
  MISSING_SOURCE("Please select a Source - either a supplier or a warehouse."),
  MISSING_STORE("Store must be set."),
  NO_SOURCE_FOR_ITEM("Item must be supplied by source."),
  NOT_AFTER_DATE_ERROR("Not after date cannot be earlier than not before date."),
  ORDER_EMPTY("The Store Order is empty."),
  STORE_ORDER_NO_SUPPLIER("Please select a Supplier."),
  STORE_ORDER_NO_WAREHOUSE("Please select a Warehouse."),
  STORE_ORDER_DELETE_CONFIRM("Are you sure you want to delete the selected Store Order(s) now?"),
  STORE_ORDER_NOT_PRINTED("Store order could not be printed!"),
  STORE_ORDER_PRINTED("Store order printed!"),
  STORE_ORDER_RANGE_ERROR("The following items do not belong to the store range: {0}"),
  WAREHOUSE_SAVE_APPROVE_CONFIRM("Store order will be approved as you have selected warehouse. Do you want to continue?");
  
  private final String message;
  
  StoreOrderMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storeorder\StoreOrderMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */