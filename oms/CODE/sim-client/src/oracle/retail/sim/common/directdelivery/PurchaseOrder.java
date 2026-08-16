package oracle.retail.sim.common.directdelivery;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.rules.core.MaxCommentSizeRule;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.store.Store;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PurchaseOrder extends BusinessObject {
  private static final long serialVersionUID = 6588662687862297152L;
  
  private Long id;
  
  private Store store;
  
  private Supplier supplier;
  
  private PurchaseOrderStatus status = PurchaseOrderStatus.UNKNOWN;
  
  private String externalId;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private Date notBeforeDate;
  
  private Date notAfterDate;
  
  private Date createDate = SimDateUtil.getCurrentDate();
  
  private Date updateDate;
  
  private Date completeDate;
  
  private String userId;
  
  private String comments;
  
  private List<PurchaseOrderLineItem> lineItems = new ArrayList<>();
  
  private List<PurchaseOrderLineItem> removedLineItems = new ArrayList<>();
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void setId(Long paramLong) throws BusinessException {
    checkForNullParameter("Purchase Order Id", paramLong);
    executeRule("setId", new Object[] { paramLong });
    doSetId(paramLong);
  }
  
  public Store getStore() {
    return this.store;
  }
  
  public void doSetStore(Store paramStore) {
    this.store = paramStore;
  }
  
  public Supplier getSupplier() {
    return this.supplier;
  }
  
  public void doSetSupplier(Supplier paramSupplier) {
    this.supplier = paramSupplier;
  }
  
  public void setSupplier(Supplier paramSupplier) throws BusinessException {
    checkForNullParameter("Supplier", paramSupplier);
    executeRule("setSupplier", new Object[] { paramSupplier });
    doSetSupplier(paramSupplier);
  }
  
  public PurchaseOrderStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(PurchaseOrderStatus paramPurchaseOrderStatus) {
    this.status = paramPurchaseOrderStatus;
  }
  
  public String getExternalId() {
    return this.externalId;
  }
  
  public void doSetExternalId(String paramString) {
    this.externalId = paramString;
  }
  
  public void setExternalId(String paramString) throws BusinessException {
    executeRule("setExternalId", new Object[] { paramString });
    doSetExternalId(paramString);
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = paramString;
  }
  
  public void setCustomerOrderId(String paramString) throws BusinessException {
    executeRule("setCustomerOrderId", new Object[] { paramString });
    doSetCustomerOrderId(paramString);
  }
  
  public String getFulfillmentOrderExternalId() {
    return this.fulfillmentOrderExternalId;
  }
  
  public void doSetFulfillmentOrderExternalId(String paramString) {
    this.fulfillmentOrderExternalId = paramString;
  }
  
  public void setFulfillmentOrderExternalId(String paramString) throws BusinessException {
    executeRule("setFulfillmentOrderExternalId", new Object[] { paramString });
    doSetFulfillmentOrderExternalId(paramString);
  }
  
  public boolean isFulfillmentOrderRelated() {
    return !StringHelper.isNullOrEmpty(this.customerOrderId);
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void doSetUserId(String paramString) {
    this.userId = paramString;
  }
  
  public void setUserId(String paramString) throws BusinessException {
    checkForNullParameter("User Id", paramString);
    executeRule("setUserId", new Object[] { paramString });
    doSetUserId(paramString);
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public void setComments(String paramString) throws BusinessException {
    MaxCommentSizeRule.execute(paramString);
    executeRule("setComments", new Object[] { paramString });
    doSetComments(paramString);
  }
  
  public Date getNotBeforeDate() {
    return this.notBeforeDate;
  }
  
  public void doSetNotBeforeDate(Date paramDate) {
    this.notBeforeDate = paramDate;
  }
  
  public void setNotBeforeDate(Date paramDate) throws BusinessException {
    executeRule("setNotBeforeDate", new Object[] { paramDate });
    doSetNotBeforeDate(paramDate);
  }
  
  public Date getNotAfterDate() {
    return this.notAfterDate;
  }
  
  public void doSetNotAfterDate(Date paramDate) {
    this.notAfterDate = paramDate;
  }
  
  public void setNotAfterDate(Date paramDate) throws BusinessException {
    executeRule("setNotAfterDate", new Object[] { paramDate });
    doSetNotAfterDate(paramDate);
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public Date getUpdateDate() {
    return this.updateDate;
  }
  
  public void doSetUpdateDate(Date paramDate) {
    this.updateDate = paramDate;
  }
  
  public void setUpdateDate(Date paramDate) throws BusinessException {
    checkForNullParameter("Update Date", paramDate);
    executeRule("setUpdateDate", new Object[] { paramDate });
    doSetUpdateDate(paramDate);
  }
  
  public Date getCompleteDate() {
    return this.completeDate;
  }
  
  public void doSetCompleteDate(Date paramDate) {
    this.completeDate = paramDate;
  }
  
  public void setCompleteDate(Date paramDate) throws BusinessException {
    checkForNullParameter("Complete Date", paramDate);
    executeRule("setCompleteDate", new Object[] { paramDate });
    doSetCompleteDate(paramDate);
  }
  
  public List<PurchaseOrderLineItem> getLineItems() {
    return Collections.unmodifiableList(this.lineItems);
  }
  
  public List<PurchaseOrderLineItem> getRemovedLineItems() {
    return Collections.unmodifiableList(this.removedLineItems);
  }
  
  public void doAddLineItem(PurchaseOrderLineItem paramPurchaseOrderLineItem) {
    this.lineItems.add(paramPurchaseOrderLineItem);
  }
  
  public void addLineItem(PurchaseOrderLineItem paramPurchaseOrderLineItem) throws BusinessException {
    checkForNullParameter("Add Line Item", paramPurchaseOrderLineItem);
    executeRule("addLineItem", new Object[] { paramPurchaseOrderLineItem });
    if (findLineItemIndex(paramPurchaseOrderLineItem, this.lineItems) >= 0)
      throw new BusinessException(DirectDeliveryMessageText.DUPLICATE_ITEM); 
    doAddLineItem(paramPurchaseOrderLineItem);
  }
  
  public void doRemoveLineItem(PurchaseOrderLineItem paramPurchaseOrderLineItem) {
    int i = findLineItemIndex(paramPurchaseOrderLineItem, this.lineItems);
    if (i < 0)
      return; 
    PurchaseOrderLineItem purchaseOrderLineItem = this.lineItems.remove(i);
    if (!purchaseOrderLineItem.isNew())
      this.removedLineItems.add(purchaseOrderLineItem); 
  }
  
  public void removeLineItem(PurchaseOrderLineItem paramPurchaseOrderLineItem) throws BusinessException {
    checkForNullParameter("Remove Line Item", paramPurchaseOrderLineItem);
    executeRule("removeLineItem", new Object[] { paramPurchaseOrderLineItem });
    doRemoveLineItem(paramPurchaseOrderLineItem);
  }
  
  private static int findLineItemIndex(PurchaseOrderLineItem paramPurchaseOrderLineItem, List<PurchaseOrderLineItem> paramList) {
    String str = paramPurchaseOrderLineItem.getSupplierItem().getItemId();
    for (byte b = 0; b < paramList.size(); b++) {
      if (str.equals(((PurchaseOrderLineItem)paramList.get(b)).getSupplierItem().getItemId()))
        return b; 
    } 
    return -1;
  }
  
  public void clearRemovedLineItems() {
    this.removedLineItems = new ArrayList<>();
  }
  
  public Quantity getNumberOfCasesExpected() {
    Quantity quantity = Quantity.ZERO;
    for (PurchaseOrderLineItem purchaseOrderLineItem : this.lineItems)
      quantity = addNumberOfCases(quantity, purchaseOrderLineItem.getQuantityExpected(), purchaseOrderLineItem.getCaseSize()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesReceived() {
    Quantity quantity = Quantity.ZERO;
    for (PurchaseOrderLineItem purchaseOrderLineItem : this.lineItems)
      quantity = addNumberOfCases(quantity, purchaseOrderLineItem.getQuantityReceived(), purchaseOrderLineItem.getCaseSize()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesOrdered() {
    Quantity quantity = Quantity.ZERO;
    for (PurchaseOrderLineItem purchaseOrderLineItem : this.lineItems)
      quantity = addNumberOfCases(quantity, purchaseOrderLineItem.getQuantityOrdered(), purchaseOrderLineItem.getCaseSize()); 
    return quantity;
  }
  
  private static Quantity addNumberOfCases(Quantity paramQuantity1, Quantity paramQuantity2, Quantity paramQuantity3) {
    return (paramQuantity2 == null || !paramQuantity2.isPositive()) ? paramQuantity1 : ((paramQuantity3 == null || paramQuantity3.compareTo(Quantity.ONE) == 0 || !paramQuantity3.isPositive()) ? paramQuantity1.add(paramQuantity2) : ((paramQuantity2.compareTo(paramQuantity3) <= 0) ? paramQuantity1.add(Quantity.ONE) : paramQuantity1.add(paramQuantity2.divide(paramQuantity3, 0, RoundingMode.UP))));
  }
  
  public boolean isEmpty() {
    return this.lineItems.isEmpty();
  }
  
  public void close() throws BusinessException {
    executeRule("close", new Object[0]);
    if (this.externalId == null) {
      doSetStatus(PurchaseOrderStatus.CLOSED);
      doSetCompleteDate(SimDateUtil.getCurrentDate());
    } 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    PurchaseOrder purchaseOrder = (PurchaseOrder)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, purchaseOrder.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\PurchaseOrder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */