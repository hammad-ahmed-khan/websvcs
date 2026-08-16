package oracle.retail.sim.common.postransaction;

import oracle.retail.sim.common.business.MessageText;

public enum POSTransactionMessageText implements MessageText {
  INVALID_ITEM("Invalid Item."),
  INVALID_UNIT_OF_MEASURE("Invalid Unit Of Measure."),
  DUPLICATE_TRANSACTION("Unable to insert duplicate transactions."),
  ORDER_FULFILL_AUDIT("Order fullfill transaction Audit is not allowed."),
  UIN_PROBLEM("UIN problem records exist for the uins in this transaction"),
  NO_PROCESSED_RECORD("Unable to find valid processed transaction.");
  
  private final String message;
  
  POSTransactionMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\postransaction\POSTransactionMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */