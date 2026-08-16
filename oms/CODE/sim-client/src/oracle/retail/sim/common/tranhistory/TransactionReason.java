package oracle.retail.sim.common.tranhistory;

import oracle.retail.sim.common.core.SimEnum;

public enum TransactionReason implements SimEnum<String> {
  DAMAGES("Damages"),
  DELIVERY("Delivery"),
  DISPATCH("Dispatch"),
  LATE_ADJUSTMENT("Late Adjustment"),
  LATE_SALE("Late Sale"),
  RECEIPT("Receipt"),
  RETURN("Return"),
  RETURN_UNAVAILABLE("Return Unavailable"),
  RUA_RECEIPT("RUA Receipt"),
  RUA_DAMAGES("RUA Damages");
  
  private final String description;
  
  TransactionReason(String paramString1) {
    this.description = paramString1;
  }
  
  public String getCode() {
    return name();
  }
  
  public String toString() {
    return this.description;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\tranhistory\TransactionReason.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */