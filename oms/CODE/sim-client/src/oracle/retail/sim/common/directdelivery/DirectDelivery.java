package oracle.retail.sim.common.directdelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.rules.core.MaxCommentSizeRule;
import oracle.retail.sim.common.rules.core.MaxStringSize30Rule;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.store.Store;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class DirectDelivery extends BusinessObject {
  private static final long serialVersionUID = 5766111242492840968L;
  
  private boolean allowAdjustment;
  
  private Long id;
  
  private Store store;
  
  private Supplier supplier;
  
  private DirectDeliveryStatus status = DirectDeliveryStatus.NEW;
  
  private PurchaseOrderVO purchaseOrder;
  
  private String asnId;
  
  private Date expectedArrivalDate;
  
  private Date createDate = SimDateUtil.getCurrentDate();
  
  private Date updateDate;
  
  private Date completeDate;
  
  private String invoiceNumber;
  
  private Date invoiceDate;
  
  private String userId;
  
  private String comments;
  
  private List<DirectDeliveryCarton> cartons = new ArrayList<>();
  
  private List<DirectDeliveryCarton> removedCartons = new ArrayList<>();
  
  public boolean isAllowAdjustment() {
    return this.allowAdjustment;
  }
  
  public void setAllowAdjustment(boolean paramBoolean) {
    this.allowAdjustment = paramBoolean;
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public Long getId() {
    return this.id;
  }
  
  public String getIdAsString() {
    return (this.id != null) ? this.id.toString() : null;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public void setId(Long paramLong) throws BusinessException {
    checkForNullParameter("Direct Delivery Id", paramLong);
    executeRule("setId", new Object[] { paramLong });
    doSetId(paramLong);
  }
  
  public Store getStore() {
    return this.store;
  }
  
  public void doSetStore(Store paramStore) {
    this.store = paramStore;
  }
  
  public void setStore(Store paramStore) throws BusinessException {
    checkForNullParameter("Store", paramStore);
    executeRule("setStore", new Object[] { paramStore });
    doSetStore(paramStore);
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
  
  public DirectDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(DirectDeliveryStatus paramDirectDeliveryStatus) {
    this.status = paramDirectDeliveryStatus;
  }
  
  public void setStatus(DirectDeliveryStatus paramDirectDeliveryStatus) throws BusinessException {
    checkForNullParameter("Status", paramDirectDeliveryStatus);
    executeRule("setStatus", new Object[] { paramDirectDeliveryStatus });
    doSetStatus(paramDirectDeliveryStatus);
  }
  
  public PurchaseOrderVO getPurchaseOrder() {
    return this.purchaseOrder;
  }
  
  public void doSetPurchaseOrder(PurchaseOrderVO paramPurchaseOrderVO) {
    this.purchaseOrder = paramPurchaseOrderVO;
  }
  
  public void setPurchaseOrder(PurchaseOrderVO paramPurchaseOrderVO) throws BusinessException {
    checkForNullParameter("Set Purchase Order", paramPurchaseOrderVO);
    executeRule("setPurchaseOrder", new Object[] { paramPurchaseOrderVO });
    doSetPurchaseOrder(paramPurchaseOrderVO);
  }
  
  public boolean isFulfillmentOrderRelated() {
    return (this.purchaseOrder != null && this.purchaseOrder.isFulfillmentOrderRelated());
  }
  
  public String getAsnId() {
    return this.asnId;
  }
  
  public void doSetAsnId(String paramString) {
    this.asnId = paramString;
  }
  
  public Date getExpectedArrivalDate() {
    return this.expectedArrivalDate;
  }
  
  public void doSetExpectedArrivalDate(Date paramDate) {
    this.expectedArrivalDate = paramDate;
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
  
  public Date getCompleteDate() {
    return this.completeDate;
  }
  
  public void doSetCompleteDate(Date paramDate) {
    this.completeDate = paramDate;
  }
  
  public String getInvoiceNumber() {
    return this.invoiceNumber;
  }
  
  public void doSetInvoiceNumber(String paramString) {
    this.invoiceNumber = paramString;
  }
  
  public void setInvoiceNumber(String paramString) throws BusinessException {
    MaxStringSize30Rule.execute(paramString);
    executeRule("setInvoiceNumber", new Object[] { paramString });
    doSetInvoiceNumber(paramString);
  }
  
  public Date getInvoiceDate() {
    return this.invoiceDate;
  }
  
  public void doSetInvoiceDate(Date paramDate) {
    this.invoiceDate = paramDate;
  }
  
  public void setInvoiceDate(Date paramDate) throws BusinessException {
    executeRule("setInvoiceDate", new Object[] { paramDate });
    doSetInvoiceDate(paramDate);
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void doSetUserId(String paramString) {
    this.userId = paramString;
  }
  
  public void setUserId(String paramString) throws BusinessException {
    checkForNullParameter("setUserId", paramString);
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
  
  public DirectDeliveryCarton getInternalCarton() {
    if (this.cartons.size() == 1 && ((DirectDeliveryCarton)this.cartons.get(0)).isInternal())
      return this.cartons.get(0); 
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      if (directDeliveryCarton.isInternal())
        return directDeliveryCarton; 
    } 
    return null;
  }
  
  public List<DirectDeliveryCarton> getCartons() {
    return Collections.unmodifiableList(this.cartons);
  }
  
  public List<DirectDeliveryCarton> getRemovedCartons() {
    return Collections.unmodifiableList(this.removedCartons);
  }
  
  public void doAddCarton(DirectDeliveryCarton paramDirectDeliveryCarton) {
    paramDirectDeliveryCarton.doSetDelivery(this);
    this.cartons.add(paramDirectDeliveryCarton);
  }
  
  public void addCarton(DirectDeliveryCarton paramDirectDeliveryCarton) throws BusinessException {
    checkForNullParameter("Add Carton", paramDirectDeliveryCarton);
    executeRule("addCarton", new Object[] { paramDirectDeliveryCarton });
    if (findCartonIndex(paramDirectDeliveryCarton) >= 0)
      throw new BusinessException(DirectDeliveryMessageText.DUPLICATE_CARTON); 
    doAddCarton(paramDirectDeliveryCarton);
  }
  
  public DirectDeliveryCarton doRemoveCarton(DirectDeliveryCarton paramDirectDeliveryCarton) {
    paramDirectDeliveryCarton.doSetDelivery(null);
    int i = findCartonIndex(paramDirectDeliveryCarton);
    if (i < 0)
      return null; 
    DirectDeliveryCarton directDeliveryCarton = this.cartons.remove(i);
    if (!directDeliveryCarton.isNew())
      this.removedCartons.add(directDeliveryCarton); 
    return directDeliveryCarton;
  }
  
  public DirectDeliveryCarton removeCarton(DirectDeliveryCarton paramDirectDeliveryCarton) throws BusinessException {
    checkForNullParameter("Remove Carton", paramDirectDeliveryCarton);
    executeRule("removeCarton", new Object[] { paramDirectDeliveryCarton });
    return doRemoveCarton(paramDirectDeliveryCarton);
  }
  
  public void doReplaceCarton(DirectDeliveryCarton paramDirectDeliveryCarton1, DirectDeliveryCarton paramDirectDeliveryCarton2) {
    int i = findCartonIndex(paramDirectDeliveryCarton1);
    if (i < 0)
      return; 
    paramDirectDeliveryCarton2.doSetDelivery(this);
    this.cartons.set(i, paramDirectDeliveryCarton2);
  }
  
  private int findCartonIndex(DirectDeliveryCarton paramDirectDeliveryCarton) {
    Long long_ = paramDirectDeliveryCarton.getId();
    String str = paramDirectDeliveryCarton.getExternalId();
    for (byte b = 0; b < this.cartons.size(); b++) {
      DirectDeliveryCarton directDeliveryCarton = this.cartons.get(b);
      if (long_ != null && long_.equals(directDeliveryCarton.getId()))
        return b; 
      if (str == null && directDeliveryCarton.getExternalId() == null)
        return b; 
      if (str != null && str.equals(directDeliveryCarton.getExternalId()))
        return b; 
    } 
    return -1;
  }
  
  public void clearRemovedCartons() {
    this.removedCartons = new ArrayList<>();
  }
  
  public List<DirectDeliveryLineItem> getLineItems() {
    if (this.cartons.size() == 1)
      return ((DirectDeliveryCarton)this.cartons.get(0)).getLineItems(); 
    ArrayList<DirectDeliveryLineItem> arrayList = new ArrayList();
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      arrayList.addAll(directDeliveryCarton.getLineItems()); 
    return arrayList;
  }
  
  public List<DirectDeliverySimpleLineItem> getSimpleLineItems() {
    if (this.cartons.size() == 1)
      return ((DirectDeliveryCarton)this.cartons.get(0)).getSimpleLineItems(); 
    ArrayList<DirectDeliverySimpleLineItem> arrayList = new ArrayList();
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      arrayList.addAll(directDeliveryCarton.getSimpleLineItems()); 
    return arrayList;
  }
  
  public List<DirectDeliveryLineItem> getRemovedLineItems() {
    if (this.cartons.size() == 1 && this.removedCartons.isEmpty())
      return ((DirectDeliveryCarton)this.cartons.get(0)).getRemovedLineItems(); 
    ArrayList<DirectDeliveryLineItem> arrayList = new ArrayList();
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      arrayList.addAll(directDeliveryCarton.getRemovedLineItems()); 
    for (DirectDeliveryCarton directDeliveryCarton : this.removedCartons) {
      arrayList.addAll(directDeliveryCarton.getLineItems());
      arrayList.addAll(directDeliveryCarton.getRemovedLineItems());
    } 
    return arrayList;
  }
  
  public List<DirectDeliverySimpleLineItem> getRemovedSimpleLineItems() {
    if (this.cartons.size() == 1 && this.removedCartons.isEmpty())
      return ((DirectDeliveryCarton)this.cartons.get(0)).getRemovedSimpleLineItems(); 
    ArrayList<DirectDeliverySimpleLineItem> arrayList = new ArrayList();
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      arrayList.addAll(directDeliveryCarton.getRemovedSimpleLineItems()); 
    for (DirectDeliveryCarton directDeliveryCarton : this.removedCartons) {
      arrayList.addAll(directDeliveryCarton.getSimpleLineItems());
      arrayList.addAll(directDeliveryCarton.getRemovedSimpleLineItems());
    } 
    return arrayList;
  }
  
  public void clearRemovedLineItems() {
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      directDeliveryCarton.clearRemovedLineItems(); 
  }
  
  public void clearRemovedSerialNumbers() {
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      directDeliveryCarton.clearRemovedSerialNumbers(); 
  }
  
  public Map<String, StockItem> getAssociatedStockItems() {
    HashMap<Object, Object> hashMap = new HashMap<>();
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      for (DirectDeliveryLineItem directDeliveryLineItem : directDeliveryCarton.getLineItems()) {
        StockItem stockItem = directDeliveryLineItem.getStockItem();
        if (!hashMap.containsKey(stockItem.getId()))
          hashMap.put(stockItem.getId(), stockItem); 
      } 
    } 
    return (Map)hashMap;
  }
  
  public int getNumberOfCartonsExpected() {
    return this.cartons.size();
  }
  
  public int getNumberOfCartonsReceived() {
    byte b = 0;
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      if (directDeliveryCarton.getStatus() == DirectDeliveryStatus.RECEIVED)
        b++; 
    } 
    return b;
  }
  
  public int getNumberOfCartonsDamaged() {
    byte b = 0;
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      if (directDeliveryCarton.getStatus() == DirectDeliveryStatus.DAMAGED || directDeliveryCarton.hasDamagedLineItems())
        b++; 
    } 
    return b;
  }
  
  public int getNumberOfCartonsMissing() {
    byte b = 0;
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      switch (directDeliveryCarton.getStatus()) {
        case MISSING:
        case IN_PROGRESS:
          b++;
      } 
    } 
    return b;
  }
  
  public int getNumberOfLineItemsExpected() {
    int i = 0;
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      i += directDeliveryCarton.getNumberOfLineItemsExpected(); 
    return i;
  }
  
  public int getNumberOfLineItemsReceived() {
    int i = 0;
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      i += directDeliveryCarton.getNumberOfLineItemsReceived(); 
    return i;
  }
  
  public int getNumberOfLineItemsDamaged() {
    int i = 0;
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      i += directDeliveryCarton.getNumberOfLineItemsDamaged(); 
    return i;
  }
  
  public Quantity getNumberOfCasesExpected() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      quantity = quantity.add(directDeliveryCarton.getNumberOfCasesExpected()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesReceived() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      quantity = quantity.add(directDeliveryCarton.getNumberOfCasesReceived()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesDamaged() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      quantity = quantity.add(directDeliveryCarton.getNumberOfCasesDamaged()); 
    return quantity;
  }
  
  public boolean isEmpty() {
    if (this.cartons.isEmpty())
      return true; 
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      if (!directDeliveryCarton.isEmpty())
        return false; 
    } 
    return true;
  }
  
  public boolean hasUnexpectedLineItems() {
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      if (directDeliveryCarton.hasUnexpectedLineItems())
        return true; 
    } 
    return false;
  }
  
  public boolean hasReceivedOrDamagedLineItems() {
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      if (directDeliveryCarton.hasReceivedOrDamagedLineItems())
        return true; 
    } 
    return false;
  }
  
  public boolean hasDiscrepantReceivedLineItems() {
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      if (directDeliveryCarton.hasDiscrepantReceivedLineItems())
        return true; 
    } 
    return false;
  }
  
  public boolean hasDiscrepantDamagedLineItems() {
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons) {
      if (directDeliveryCarton.hasDiscrepantDamagedLineItems())
        return true; 
    } 
    return false;
  }
  
  public void markInProgress() throws BusinessException {
    executeRule("markInProgress", new Object[0]);
    if (this.status == DirectDeliveryStatus.RECEIVED && !this.allowAdjustment)
      throw new BusinessException(DirectDeliveryMessageText.RECEIVED_ERROR); 
    if (this.status == DirectDeliveryStatus.CANCELED)
      throw new BusinessException(DirectDeliveryMessageText.CANCELED_STATE_NOT_RECEIVABLE); 
    setStatus(DirectDeliveryStatus.IN_PROGRESS);
    doSetUpdateDate(SimDateUtil.getCurrentDate());
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      directDeliveryCarton.setStatus(DirectDeliveryStatus.IN_PROGRESS); 
  }
  
  public void receiveAll() throws BusinessException {
    executeRule("receiveAll", new Object[0]);
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      directDeliveryCarton.receiveAll(); 
  }
  
  public void receive() throws BusinessException {
    executeRule("receive", new Object[0]);
    if (this.status != DirectDeliveryStatus.IN_PROGRESS)
      throw new BusinessException(DirectDeliveryMessageText.IN_PROGRESS_TO_RECEIVE_ERROR); 
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      directDeliveryCarton.receive(); 
    setStatus(DirectDeliveryStatus.RECEIVED);
    doSetCompleteDate(SimDateUtil.getCurrentDate());
  }
  
  public void reject() throws BusinessException {
    executeRule("reject", new Object[0]);
    if (this.status != DirectDeliveryStatus.IN_PROGRESS)
      throw new BusinessException(DirectDeliveryMessageText.IN_PROGRESS_TO_REJECT_ERROR); 
    if (this.purchaseOrder == null || StringHelper.isNullOrEmpty(this.asnId))
      throw new BusinessException(DirectDeliveryMessageText.REJECT_ERROR); 
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      directDeliveryCarton.reject(); 
    setStatus(DirectDeliveryStatus.REJECTED);
    doSetCompleteDate(SimDateUtil.getCurrentDate());
  }
  
  public void reset() throws BusinessException {
    executeRule("reset", new Object[0]);
    if (this.status == DirectDeliveryStatus.RECEIVED && !this.allowAdjustment)
      throw new BusinessException(DirectDeliveryMessageText.RECEIVED_ERROR); 
    if (this.status == DirectDeliveryStatus.CANCELED)
      throw new BusinessException(DirectDeliveryMessageText.CANCELED_STATE_NOT_RECEIVABLE); 
    setStatus(DirectDeliveryStatus.IN_PROGRESS);
    doSetUpdateDate(SimDateUtil.getCurrentDate());
    for (DirectDeliveryCarton directDeliveryCarton : this.cartons)
      directDeliveryCarton.reset(); 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    DirectDelivery directDelivery = (DirectDelivery)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, directDelivery.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDelivery.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */