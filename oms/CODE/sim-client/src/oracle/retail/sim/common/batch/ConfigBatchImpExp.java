package oracle.retail.sim.common.batch;

import oracle.retail.sim.common.business.BusinessObject;

public class ConfigBatchImpExp extends BusinessObject {
  private static final long serialVersionUID = 456647214223795745L;
  
  private String id;
  
  private String description;
  
  private int operation;
  
  private int transactionRecordsLimit;
  
  public String getId() {
    return this.id;
  }
  
  public void setId(String paramString) {
    this.id = paramString;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String paramString) {
    this.description = paramString;
  }
  
  public int getOperation() {
    return this.operation;
  }
  
  public void setOperation(int paramInt) {
    this.operation = paramInt;
  }
  
  public int getTransactionRecordsLimit() {
    return this.transactionRecordsLimit;
  }
  
  public void setTransactionRecordsLimit(int paramInt) {
    this.transactionRecordsLimit = paramInt;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\batch\ConfigBatchImpExp.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */