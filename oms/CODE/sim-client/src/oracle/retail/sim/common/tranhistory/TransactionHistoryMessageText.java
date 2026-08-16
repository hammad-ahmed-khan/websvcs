package oracle.retail.sim.common.tranhistory;

import oracle.retail.sim.common.business.MessageText;

public enum TransactionHistoryMessageText implements MessageText {
  MISSING_FILTER_DATES("From and To Date are required."),
  MISSING_TRANSACTION("The transaction history record does not have an original transaction.");
  
  private final String message;
  
  TransactionHistoryMessageText(String paramString1) {
    this.message = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String getText() {
    return this.message;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\tranhistory\TransactionHistoryMessageText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */