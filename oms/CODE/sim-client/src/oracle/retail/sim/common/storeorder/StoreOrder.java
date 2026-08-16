package oracle.retail.sim.common.storeorder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.rules.core.MaxCommentSizeRule;
import oracle.retail.sim.common.rules.storeorder.StoreOrderIsCoherentRule;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.store.Store;

public class StoreOrder extends BusinessObject {
  static final long serialVersionUID = -7314693737801265950L;
  
  private StoreOrderType type = null;
  
  private String storeOrderNumber = null;
  
  private Source fromLocation = null;
  
  private Store toLocation = null;
  
  private Date notAfterDate = null;
  
  private Date notBeforeDate = null;
  
  private Date creationDate = null;
  
  private String currencyCode = null;
  
  private StoreOrderStatus status = StoreOrderStatus.PENDING;
  
  private String approvalUser = null;
  
  private List<StoreOrderLineItem> lineItems = new ArrayList<>();
  
  private List<StoreOrderLineItem> deletedLineItems = new ArrayList<>();
  
  private String shortDescription = null;
  
  private StoreOrderState orderState = null;
  
  private Quantity quantity;
  
  private String comments = "";
  
  private String creationUser = null;
  
  public StoreOrder(String paramString) {
    this.storeOrderNumber = paramString;
    this.orderState = StoreOrderState.EDIT;
  }
  
  public StoreOrder() {
    this.orderState = StoreOrderState.CREATE;
  }
  
  public StoreOrderState getOrderState() {
    return this.orderState;
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void setComments(String paramString) throws BusinessException {
    MaxCommentSizeRule.execute(paramString);
    executeRule("setComments", new Object[] { paramString });
    doSetComments(paramString);
  }
  
  public Date getCreationDate() {
    return this.creationDate;
  }
  
  public void setCreationDate(Date paramDate) {
    this.creationDate = paramDate;
  }
  
  public String getApprovalUser() {
    return this.approvalUser;
  }
  
  public void setApprovalUser(String paramString) {
    this.approvalUser = paramString;
  }
  
  public String getCreationUser() {
    return this.creationUser;
  }
  
  public void setCreationUser(String paramString) {
    this.creationUser = paramString;
  }
  
  public String getCurrencyCode() {
    return this.currencyCode;
  }
  
  public void setCurrencyCode(String paramString) {
    this.currencyCode = paramString;
  }
  
  public String getShortDescription() {
    return this.shortDescription;
  }
  
  public void setShortDescription(String paramString) {
    this.shortDescription = paramString;
  }
  
  public StoreOrderStatus getStatus() {
    return this.status;
  }
  
  public String getStatusDescription() {
    return (this.status == StoreOrderStatus.APPROVED) ? "Approved" : ((this.status == StoreOrderStatus.CLOSED) ? "Closed" : ((this.status == StoreOrderStatus.PENDING) ? "Pending" : ""));
  }
  
  public void setStatus(StoreOrderStatus paramStoreOrderStatus) {
    this.status = paramStoreOrderStatus;
  }
  
  public void addLineItem(StoreOrderLineItem paramStoreOrderLineItem) throws BusinessException {
    executeRule("addLineItem", new Object[] { paramStoreOrderLineItem });
    this.lineItems.add(paramStoreOrderLineItem);
  }
  
  public List<StoreOrderLineItem> getLineItems() {
    return this.lineItems;
  }
  
  public List<StoreOrderLineItem> getDeletedLineItems() {
    return this.deletedLineItems;
  }
  
  public void setStoreOrderLineItems(List<StoreOrderLineItem> paramList) {
    if (paramList != null)
      this.lineItems = paramList; 
  }
  
  public void removeAllStoreOrderLineItem() {
    this.lineItems.clear();
  }
  
  public void removeStoreOrderLineItem(StoreOrderLineItem paramStoreOrderLineItem) {
    if (paramStoreOrderLineItem == null)
      return; 
    if (paramStoreOrderLineItem.getOrderItem() != null)
      this.deletedLineItems.add(paramStoreOrderLineItem); 
    this.lineItems.remove(paramStoreOrderLineItem);
  }
  
  public String getStoreOrderNumber() {
    return this.storeOrderNumber;
  }
  
  public void setType(StoreOrderType paramStoreOrderType) {
    this.type = paramStoreOrderType;
  }
  
  public Source getFromLocation() {
    return this.fromLocation;
  }
  
  public Date getNotAfterDate() {
    return this.notAfterDate;
  }
  
  public Date getNotBeforeDate() {
    return this.notBeforeDate;
  }
  
  public Store getToLocation() {
    return this.toLocation;
  }
  
  public StoreOrderType getType() {
    return this.type;
  }
  
  public void setOrderState(StoreOrderState paramStoreOrderState) {
    this.orderState = paramStoreOrderState;
  }
  
  public void setStoreOrderNumber(String paramString) {
    this.storeOrderNumber = paramString;
  }
  
  public void setFromLocation(Source paramSource) throws BusinessException {
    doSetFromLocation(paramSource);
  }
  
  public void doSetFromLocation(Source paramSource) {
    this.fromLocation = paramSource;
  }
  
  public void setNotAfterDate(Date paramDate) throws BusinessException {
    doSetNotAfterDate(paramDate);
  }
  
  public void doSetNotAfterDate(Date paramDate) {
    this.notAfterDate = paramDate;
  }
  
  public void setToLocation(Store paramStore) throws BusinessException {
    doSetToLocation(paramStore);
  }
  
  public void doSetToLocation(Store paramStore) {
    this.toLocation = paramStore;
  }
  
  public void doSetType(StoreOrderType paramStoreOrderType) {
    this.type = paramStoreOrderType;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public void setNotBeforeDate(Date paramDate) throws BusinessException {
    doSetNotBeforeDate(paramDate);
  }
  
  public void doSetNotBeforeDate(Date paramDate) {
    this.notBeforeDate = paramDate;
  }
  
  public Quantity getQuantity() {
    return this.quantity;
  }
  
  public void setQuantity(Quantity paramQuantity) {
    this.quantity = paramQuantity;
  }
  
  public boolean isCoherent(TimeZone paramTimeZone) throws BusinessException {
    StoreOrderIsCoherentRule.execute(this, paramTimeZone);
    executeRule("isCoherent", new Object[] { paramTimeZone });
    return true;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storeorder\StoreOrder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */