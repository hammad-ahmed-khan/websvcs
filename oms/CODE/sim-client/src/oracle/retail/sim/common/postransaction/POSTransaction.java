package oracle.retail.sim.common.postransaction;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;

public class POSTransaction extends BusinessObject {
  private static final long serialVersionUID = -393370025185808817L;
  
  private Long id;
  
  private Long storeId;
  
  private String itemId;
  
  private POSTransactionItemType itemIdType;
  
  private Long requestId;
  
  private String externalId;
  
  private Date transactionTimestamp;
  
  private POSTransactionType type;
  
  private Quantity quantity;
  
  private String unitOfMeasure;
  
  private POSTransactionSourceType sourceType;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private String orderComments;
  
  private FulfillmentOrderType orderType;
  
  private String comments;
  
  private String uin;
  
  private Integer reasonCode;
  
  private boolean isDropShip;
  
  private POSTransactionStatus status = POSTransactionStatus.NEW;
  
  private Date fileCreateDate;
  
  private Boolean isResaCreated;
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getRequestId() {
    return this.requestId;
  }
  
  public void setRequestId(Long paramLong) {
    this.requestId = paramLong;
  }
  
  public Integer getReasonCode() {
    return this.reasonCode;
  }
  
  public void setReasonCode(Integer paramInteger) {
    this.reasonCode = paramInteger;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void setItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public POSTransactionItemType getItemIdType() {
    return this.itemIdType;
  }
  
  public void setItemIdType(POSTransactionItemType paramPOSTransactionItemType) {
    this.itemIdType = paramPOSTransactionItemType;
  }
  
  public POSTransactionType getType() {
    return this.type;
  }
  
  public void setType(POSTransactionType paramPOSTransactionType) {
    this.type = paramPOSTransactionType;
  }
  
  public Quantity getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(Quantity paramQuantity) {
    this.quantity = paramQuantity;
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void setComments(String paramString) {
    this.comments = paramString;
  }
  
  public String getUin() {
    return this.uin;
  }
  
  public void setUin(String paramString) {
    this.uin = paramString;
  }
  
  public Boolean isDropShip() {
    return Boolean.valueOf(this.isDropShip);
  }
  
  public void setDropShip(Boolean paramBoolean) {
    this.isDropShip = paramBoolean.booleanValue();
  }
  
  public String getUnitOfMeasure() {
    return this.unitOfMeasure;
  }
  
  public void setUnitOfMeasure(String paramString) {
    this.unitOfMeasure = paramString;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public String getExternalId() {
    return this.externalId;
  }
  
  public void setExternalId(String paramString) {
    this.externalId = paramString;
  }
  
  public POSTransactionSourceType getSourceType() {
    return this.sourceType;
  }
  
  public void setSourceType(POSTransactionSourceType paramPOSTransactionSourceType) {
    this.sourceType = paramPOSTransactionSourceType;
  }
  
  public FulfillmentOrderType getOrderType() {
    return this.orderType;
  }
  
  public void setOrderType(FulfillmentOrderType paramFulfillmentOrderType) {
    this.orderType = paramFulfillmentOrderType;
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void setCustomerOrderId(String paramString) {
    this.customerOrderId = paramString;
  }
  
  public String getFulfillmentOrderExternalId() {
    return this.fulfillmentOrderExternalId;
  }
  
  public void setFulfillmentOrderExternalId(String paramString) {
    this.fulfillmentOrderExternalId = paramString;
  }
  
  public String getOrderComments() {
    return this.orderComments;
  }
  
  public void setOrderComments(String paramString) {
    this.orderComments = paramString;
  }
  
  public Date getTransactionTimestamp() {
    return this.transactionTimestamp;
  }
  
  public void setTransactionTimestamp(Date paramDate) {
    this.transactionTimestamp = paramDate;
  }
  
  public POSTransactionStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(POSTransactionStatus paramPOSTransactionStatus) {
    this.status = paramPOSTransactionStatus;
  }
  
  public Date getFileCreateDate() {
    return this.fileCreateDate;
  }
  
  public void setFileCreateDate(Date paramDate) {
    this.fileCreateDate = paramDate;
  }
  
  public Boolean isResaCreated() {
    return this.isResaCreated;
  }
  
  public void setResaCreated(Boolean paramBoolean) {
    this.isResaCreated = paramBoolean;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\postransaction\POSTransaction.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */