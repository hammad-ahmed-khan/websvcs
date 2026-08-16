package oracle.retail.sim.common.business;

public enum BatchMessageText implements MessageText {
  POS_SALE_RETURN_ERROR("Problem encountered while processing POS Sale Return."),
  POS_UIN_ERROR("Problem encountered while processing POS UIN."),
  POS_SALE_OPEN_COUNT_ERROR("Problem encountered while processing POS Sale which has open count items."),
  SALE_AUDIT_IMPORT_INVALID_RETRY("Invalid Sale audit import retry."),
  SALE_AUDIT_IMPORT_EXIST("Same sale audit import file exists."),
  FILE_ARCHIVE_ERROR("Problem encountered while moving the file to archive location."),
  FILE_DELETE_ERROR("Problem encountered while deleting the file."),
  FILE_IMPORT_EXIST("The same file were executed or failed in the previous execution, please verify the batch import execution table for details."),
  PURGE_ERROR("Problem encountered while processing purging."),
  PURGE_INV_ADJUST_ERROR("Problem encountered while processing purge inventory adjustment records."),
  PURGE_INV_TEMPLATE_ERROR("Problem encountered while processing inventory adjustment template records."),
  PURGE_ITEM_STOCK_HIST_ERROR("Problem encountered while processing purge item stock history records."),
  PURGE_TEMPORARY_UIN_ERROR("Problem encountered while processing tempoary UIN records."),
  INVALID_DATE_FORMAT("Invalid date format.");
  
  private final String message;
  
  BatchMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\BatchMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */