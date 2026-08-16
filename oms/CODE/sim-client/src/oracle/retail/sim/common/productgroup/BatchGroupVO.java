package oracle.retail.sim.common.productgroup;

import java.io.Serializable;

public class BatchGroupVO implements Serializable {
  private static final long serialVersionUID = 4599926802983669336L;
  
  private Long scheduleId = null;
  
  private Long stockCountId = null;
  
  private boolean autoAuthorize = false;
  
  private Long storeId = null;
  
  public Long getScheduleId() {
    return this.scheduleId;
  }
  
  public void setScheduleId(Long paramLong) {
    this.scheduleId = paramLong;
  }
  
  public Long getStockCountId() {
    return this.stockCountId;
  }
  
  public void setStockCountId(Long paramLong) {
    this.stockCountId = paramLong;
  }
  
  public boolean isAutoAuthorize() {
    return this.autoAuthorize;
  }
  
  public void setAutoAuthorize(boolean paramBoolean) {
    this.autoAuthorize = paramBoolean;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("Batch Group VO[ Schedule Id=");
    stringBuilder.append(this.scheduleId);
    stringBuilder.append(" Auto Authorize=");
    stringBuilder.append(this.autoAuthorize);
    stringBuilder.append(" Store Id=");
    stringBuilder.append(this.storeId);
    stringBuilder.append("]");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\productgroup\BatchGroupVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */