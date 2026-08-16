package oracle.retail.sim.common.storesequence;

import oracle.retail.sim.common.business.MessageText;

public enum StoreSequenceMessageText implements MessageText {
  DELETE_LOCATION_ERROR("Cannot delete a location that holds items."),
  DUPLICATE_ENTRY("Duplicate entries not allowed."),
  CANNOT_PRINT_NO_LOCATION("You cannot print shelf edge labels for items in 'No Location'."),
  DELETE_ITEM_CONFIRM("Are you sure you want to delete the selected items now?"),
  DELETE_LOCATION_CONFIRM("Are you sure you want to delete the selected locations now?"),
  DUPLICATE_ENTRY_ERROR("Duplicate entries not allowed."),
  DUPLICATE_ITEM_FIX("{0} is a duplicate item. Row will be removed."),
  GENERATE_ITEM_CONFIRM("Are you sure you want to generate all items for the location?"),
  GENERATE_SEQUENCE_CONFIRM("Are you sure you want to generate locations for all classes?"),
  LOCATION_EMPTY_ERROR("Location must have item ID set."),
  MISSING_DESCRIPTION("Location must have description set."),
  MISSING_AREA("Location must have area set."),
  MISSING_LOCATION("Please select a location for the item."),
  NONSELLABLE_ITEM_ERROR("Non-sellable items cannot be placed in SHOPFLOOR locations."),
  NOT_HIERARCHY_CREATED("The location is not a class, no item list exists."),
  NO_LOCATION_DONE_CONFIRM("Only records with all columns entered will be saved. Are you sure you are finished?"),
  NO_LOCATION_SAVE_CONFIRM("Are you sure you want to save this item with no locations?"),
  NO_PRIMARY_LOCATION("Please select one location to be the primary location."),
  PRIMARY_ITEM_AREA_ERROR("Unable to determine primary location for an item."),
  PRINT_LABELS_CONFIRM("Are you sure you want to print shelf edge labels for all items in the locations selected?"),
  SEQUENCE_AREA_LOCKED("Location is currently locked for sequencing."),
  SHELF_LABELS_PRINTED("Shelf Edge Labels printed."),
  SHOPFLOOR_OR_BACKROOM("Would you like to apply the classes to the shopfloor or backroom?"),
  TICKET_PRINT_FAILED("Not all items printed due to missing label formats and/or label quantities."),
  UNSAVED_LOCATION_ERROR("Please save the location before attempting to print.");
  
  private final String message;
  
  StoreSequenceMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storesequence\StoreSequenceMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */