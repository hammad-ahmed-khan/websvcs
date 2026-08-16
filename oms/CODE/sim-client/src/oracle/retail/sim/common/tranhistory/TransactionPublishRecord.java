package oracle.retail.sim.common.tranhistory;

import java.util.Date;
import oracle.retail.sim.common.business.Quantity;

public class TransactionPublishRecord {
  private Long id;
  
  private Long storeId;
  
  private String itemId;
  
  private Integer reasonCode;
  
  private String username;
  
  private Date timestamp;
  
  private Quantity quantity;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void doSetReasonCode(Integer paramInteger) {
    this.reasonCode = paramInteger;
  }
  
  public Integer getReasonCode() {
    return this.reasonCode;
  }
  
  public void doSetUsername(String paramString) {
    this.username = paramString;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public void doSetTimestamp(Date paramDate) {
    this.timestamp = paramDate;
  }
  
  public Date getTimestamp() {
    return this.timestamp;
  }
  
  public void doSetQuantity(Quantity paramQuantity) {
    this.quantity = paramQuantity;
  }
  
  public Quantity getQuantity() {
    return this.quantity;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\tranhistory\TransactionPublishRecord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */