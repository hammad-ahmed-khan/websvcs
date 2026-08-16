package oracle.retail.sim.common.shelfreplenishment;

import oracle.retail.sim.common.business.MessageText;

public enum ShelfReplenishmentMessageText implements MessageText {
  BREAK_LOCK_SHELF_REPLENISHMENT("wireless.breaklock.shelfReplenishment"),
  CANCELLED_ERROR("Shelf Replenishment cannot be changed when status is canceled"),
  CLOSED_ERROR("Shelf Replenishment cannot be changed when status is closed"),
  COMPLETE_CONFIRM("Inventory will be updated and the status will be changed to completed. Are you sure you want to complete the shelf replenishment?"),
  CREATE_CONFIRM("Are you sure you want to create the shelf replenishment?"),
  DELETE_CONFIRM("Are you sure you want to delete the selected shelf replenishments now?"),
  MISSING_TYPE("You must select a Shelf Replenishment Type."),
  MUST_EXIST_FOR_UPDATE("Shelf Replenishment must exist prior to update"),
  NO_GROUP_ASSIGNED("Replenishment group must be set."),
  NO_QTY_TO_REPLENISH("Amount to pick must be set."),
  ONLY_PENDED_CAN_CANCEL("Only shelf replenishments in pending status can be deleted."),
  SHELF_REPLENISHMENT_PRINTED("Shelf Replenishment printed."),
  PRINT_CONFIRM("Are you sure you want to print the shelf replenishments?"),
  PRINT_PENDED_CONFIRM("Inventory will be updated and the status will be changed to completed. Are you sure you want to print the Shelf Replenishment?"),
  QUANTITY_ERROR("Cannot enter a quantity greater than the pick amount."),
  REPLENISH_NOT_NEEDED("No items need to be replenished at this time."),
  REOPEN_SHELF_REPLENISHMENT("Are you sure you want to re-open the Shelf Replenishment?"),
  SEQUENCE_ALTERED("The sequencing of this Shelf Replenishment has been modified after it was created."),
  WEB_INVALID_ACTION("The action to be taken to process the Shelf Replenishment is invalid"),
  WEB_INVALID_SHELF_REPLENISHMENT_ID("The Shelf Replenishment specified does not exist"),
  WEB_LOCK_TAKEN_OVER("Lock has been taken over");
  
  private final String message;
  
  ShelfReplenishmentMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shelfreplenishment\ShelfReplenishmentMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */