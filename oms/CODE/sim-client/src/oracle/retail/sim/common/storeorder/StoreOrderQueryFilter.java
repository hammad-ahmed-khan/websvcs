package oracle.retail.sim.common.storeorder;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.store.Store;

public class StoreOrderQueryFilter implements Serializable {
  static final long serialVersionUID = -1316977183191885714L;
  
  private Long storeOrderNumber;
  
  private Source fromLocation;
  
  private Store toLocation;
  
  private StoreOrderStatus status;
  
  private Date notAfterDate;
  
  private Date notBeforeDate;
  
  private SourceType sourceType;
  
  private String itemId;
  
  public Source getFromLocation() {
    return this.fromLocation;
  }
  
  public void setFromLocation(Source paramSource) {
    this.fromLocation = paramSource;
  }
  
  public Store getToLocation() {
    return this.toLocation;
  }
  
  public void setToLocation(Store paramStore) {
    this.toLocation = paramStore;
  }
  
  public StoreOrderStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(StoreOrderStatus paramStoreOrderStatus) {
    this.status = paramStoreOrderStatus;
  }
  
  public Long getStoreOrderNumber() {
    return this.storeOrderNumber;
  }
  
  public void setStoreOrderNumber(Long paramLong) {
    this.storeOrderNumber = paramLong;
  }
  
  public Date getNotAfterDate() {
    return this.notAfterDate;
  }
  
  public void setNotAfterDate(Date paramDate) {
    this.notAfterDate = paramDate;
  }
  
  public Date getNotBeforeDate() {
    return this.notBeforeDate;
  }
  
  public void setNotBeforeDate(Date paramDate) {
    this.notBeforeDate = paramDate;
  }
  
  public SourceType getSourceType() {
    return this.sourceType;
  }
  
  public void setSourceType(SourceType paramSourceType) {
    this.sourceType = paramSourceType;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void setItemId(String paramString) {
    this.itemId = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storeorder\StoreOrderQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */