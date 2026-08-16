package oracle.retail.sim.common.tranhistory;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.business.Quantity;

public class TransactionHistoryVO implements Serializable {
  private static final long serialVersionUID = -8692067309601980251L;
  
  private Date timestamp;
  
  private TransactionType type;
  
  private String transactionId;
  
  private String username;
  
  private String itemId;
  
  private String itemDescription;
  
  private String reasonCode;
  
  private String description;
  
  private Quantity stockOnHandMovement;
  
  private Quantity nonSellableMovement;
  
  public Date getTimestamp() {
    return this.timestamp;
  }
  
  public void doSetTimestamp(Date paramDate) {
    this.timestamp = paramDate;
  }
  
  public TransactionType getType() {
    return this.type;
  }
  
  public void doSetType(TransactionType paramTransactionType) {
    this.type = paramTransactionType;
  }
  
  public String getTransactionId() {
    return this.transactionId;
  }
  
  public void doSetTransactionId(String paramString) {
    this.transactionId = paramString;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public void doSetUsername(String paramString) {
    this.username = paramString;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public String getItemDescription() {
    return this.itemDescription;
  }
  
  public void doSetItemDescription(String paramString) {
    this.itemDescription = paramString;
  }
  
  public String getReasonCode() {
    return this.reasonCode;
  }
  
  public void doSetReasonCode(String paramString) {
    this.reasonCode = paramString;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void doSetDescription(String paramString) {
    this.description = paramString;
  }
  
  public Quantity getStockOnHandMovement() {
    return this.stockOnHandMovement;
  }
  
  public void doSetStockOnHandMovement(Quantity paramQuantity) {
    this.stockOnHandMovement = paramQuantity;
  }
  
  public Quantity getNonSellableMovement() {
    return this.nonSellableMovement;
  }
  
  public void doSetNonSellableMovement(Quantity paramQuantity) {
    this.nonSellableMovement = paramQuantity;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\tranhistory\TransactionHistoryVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */