package oracle.retail.sim.common.warehousedelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.rules.core.MaxCommentSizeRule;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.Source;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.store.Store;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class WarehouseDelivery extends BusinessObject {
  private static final long serialVersionUID = -7524644921043835346L;
  
  private boolean allowAdjustment;
  
  private Long id;
  
  private Store store;
  
  private Source source;
  
  private WarehouseDeliveryStatus status = WarehouseDeliveryStatus.NEW;
  
  private String asnId;
  
  private Date expectedArrivalDate;
  
  private Date createDate = SimDateUtil.getCurrentDate();
  
  private Date updateDate;
  
  private Date completeDate;
  
  private ContextType contextType;
  
  private String contextValue;
  
  private String userId;
  
  private String comments;
  
  private List<WarehouseDeliveryCarton> cartons = new ArrayList<>();
  
  private List<WarehouseDeliveryCarton> removedCartons = new ArrayList<>();
  
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
    checkForNullParameter("Warehouse Delivery Id", paramLong);
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
  
  public Source getSource() {
    return this.source;
  }
  
  public void doSetSource(Source paramSource) {
    this.source = paramSource;
  }
  
  public void setSource(Source paramSource) throws BusinessException {
    checkForNullParameter("Source", paramSource);
    executeRule("setSource", new Object[] { paramSource });
    doSetSource(paramSource);
  }
  
  public boolean isFinisherDelivery() {
    return (this.source != null && this.source.getSourceType() == SourceType.FINISHER);
  }
  
  public WarehouseDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(WarehouseDeliveryStatus paramWarehouseDeliveryStatus) {
    this.status = paramWarehouseDeliveryStatus;
  }
  
  public void setStatus(WarehouseDeliveryStatus paramWarehouseDeliveryStatus) throws BusinessException {
    checkForNullParameter("Status", paramWarehouseDeliveryStatus);
    executeRule("setStatus", new Object[] { paramWarehouseDeliveryStatus });
    doSetStatus(paramWarehouseDeliveryStatus);
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
  
  public ContextType getContextType() {
    return this.contextType;
  }
  
  public void doSetContextType(ContextType paramContextType) {
    this.contextType = paramContextType;
  }
  
  public void setContextType(ContextType paramContextType) throws BusinessException {
    executeRule("setContextType", new Object[] { paramContextType });
    doSetContextType(paramContextType);
  }
  
  public String getContextValue() {
    return this.contextValue;
  }
  
  public void doSetContextValue(String paramString) {
    this.contextValue = paramString;
  }
  
  public void setContextValue(String paramString) throws BusinessException {
    executeRule("setContextValue", new Object[] { paramString });
    doSetContextValue(paramString);
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
  
  public WarehouseDeliveryCarton getInternalCarton() {
    if (this.cartons.size() == 1 && ((WarehouseDeliveryCarton)this.cartons.get(0)).isInternal())
      return this.cartons.get(0); 
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      if (warehouseDeliveryCarton.isInternal())
        return warehouseDeliveryCarton; 
    } 
    return null;
  }
  
  public List<WarehouseDeliveryCarton> getCartons() {
    return Collections.unmodifiableList(this.cartons);
  }
  
  public List<WarehouseDeliveryCarton> getRemovedCartons() {
    return Collections.unmodifiableList(this.removedCartons);
  }
  
  public void doAddCarton(WarehouseDeliveryCarton paramWarehouseDeliveryCarton) {
    paramWarehouseDeliveryCarton.doSetDelivery(this);
    this.cartons.add(paramWarehouseDeliveryCarton);
  }
  
  public void addCarton(WarehouseDeliveryCarton paramWarehouseDeliveryCarton) throws BusinessException {
    checkForNullParameter("Add Carton", paramWarehouseDeliveryCarton);
    executeRule("addCarton", new Object[] { paramWarehouseDeliveryCarton });
    if (findCartonIndex(paramWarehouseDeliveryCarton) >= 0)
      throw new BusinessException(WarehouseDeliveryMessageText.DUPLICATE_CARTON); 
    doAddCarton(paramWarehouseDeliveryCarton);
  }
  
  public WarehouseDeliveryCarton doRemoveCarton(WarehouseDeliveryCarton paramWarehouseDeliveryCarton) {
    paramWarehouseDeliveryCarton.doSetDelivery(null);
    int i = findCartonIndex(paramWarehouseDeliveryCarton);
    if (i < 0)
      return null; 
    WarehouseDeliveryCarton warehouseDeliveryCarton = this.cartons.remove(i);
    if (!warehouseDeliveryCarton.isNew())
      this.removedCartons.add(warehouseDeliveryCarton); 
    return warehouseDeliveryCarton;
  }
  
  public WarehouseDeliveryCarton removeCarton(WarehouseDeliveryCarton paramWarehouseDeliveryCarton) throws BusinessException {
    checkForNullParameter("Remove Carton", paramWarehouseDeliveryCarton);
    executeRule("removeCarton", new Object[] { paramWarehouseDeliveryCarton });
    return doRemoveCarton(paramWarehouseDeliveryCarton);
  }
  
  public void doReplaceCarton(WarehouseDeliveryCarton paramWarehouseDeliveryCarton1, WarehouseDeliveryCarton paramWarehouseDeliveryCarton2) {
    int i = findCartonIndex(paramWarehouseDeliveryCarton1);
    if (i < 0)
      return; 
    paramWarehouseDeliveryCarton2.doSetDelivery(this);
    this.cartons.set(i, paramWarehouseDeliveryCarton2);
  }
  
  private int findCartonIndex(WarehouseDeliveryCarton paramWarehouseDeliveryCarton) {
    Long long_ = paramWarehouseDeliveryCarton.getId();
    String str = paramWarehouseDeliveryCarton.getExternalId();
    for (byte b = 0; b < this.cartons.size(); b++) {
      WarehouseDeliveryCarton warehouseDeliveryCarton = this.cartons.get(b);
      if (long_ != null && long_.equals(warehouseDeliveryCarton.getId()))
        return b; 
      if (str == null && warehouseDeliveryCarton.getExternalId() == null)
        return b; 
      if (str != null && str.equals(warehouseDeliveryCarton.getExternalId()))
        return b; 
    } 
    return -1;
  }
  
  public void clearRemovedCartons() {
    this.removedCartons = new ArrayList<>();
  }
  
  public List<WarehouseDeliveryLineItem> getLineItems() {
    if (this.cartons.size() == 1)
      return ((WarehouseDeliveryCarton)this.cartons.get(0)).getLineItems(); 
    ArrayList<WarehouseDeliveryLineItem> arrayList = new ArrayList();
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      arrayList.addAll(warehouseDeliveryCarton.getLineItems()); 
    return arrayList;
  }
  
  public List<WarehouseDeliverySimpleLineItem> getSimpleLineItems() {
    if (this.cartons.size() == 1)
      return ((WarehouseDeliveryCarton)this.cartons.get(0)).getSimpleLineItems(); 
    ArrayList<WarehouseDeliverySimpleLineItem> arrayList = new ArrayList();
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      arrayList.addAll(warehouseDeliveryCarton.getSimpleLineItems()); 
    return arrayList;
  }
  
  public List<WarehouseDeliveryLineItem> getRemovedLineItems() {
    if (this.cartons.size() == 1 && this.removedCartons.isEmpty())
      return ((WarehouseDeliveryCarton)this.cartons.get(0)).getRemovedLineItems(); 
    ArrayList<WarehouseDeliveryLineItem> arrayList = new ArrayList();
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      arrayList.addAll(warehouseDeliveryCarton.getRemovedLineItems()); 
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.removedCartons) {
      arrayList.addAll(warehouseDeliveryCarton.getLineItems());
      arrayList.addAll(warehouseDeliveryCarton.getRemovedLineItems());
    } 
    return arrayList;
  }
  
  public List<WarehouseDeliverySimpleLineItem> getRemovedSimpleLineItems() {
    if (this.cartons.size() == 1 && this.removedCartons.isEmpty())
      return ((WarehouseDeliveryCarton)this.cartons.get(0)).getRemovedSimpleLineItems(); 
    ArrayList<WarehouseDeliverySimpleLineItem> arrayList = new ArrayList();
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      arrayList.addAll(warehouseDeliveryCarton.getRemovedSimpleLineItems()); 
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.removedCartons) {
      arrayList.addAll(warehouseDeliveryCarton.getSimpleLineItems());
      arrayList.addAll(warehouseDeliveryCarton.getRemovedSimpleLineItems());
    } 
    return arrayList;
  }
  
  public void clearRemovedLineItems() {
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      warehouseDeliveryCarton.clearRemovedLineItems(); 
  }
  
  public void clearRemovedSerialNumbers() {
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      warehouseDeliveryCarton.clearRemovedSerialNumbers(); 
  }
  
  public Map<String, StockItem> getAssociatedStockItems() {
    HashMap<Object, Object> hashMap = new HashMap<>();
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : warehouseDeliveryCarton.getLineItems()) {
        StockItem stockItem = warehouseDeliveryLineItem.getStockItem();
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
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      if (warehouseDeliveryCarton.getStatus() == WarehouseDeliveryStatus.RECEIVED)
        b++; 
    } 
    return b;
  }
  
  public int getNumberOfCartonsDamaged() {
    byte b = 0;
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      if (warehouseDeliveryCarton.getStatus() == WarehouseDeliveryStatus.DAMAGED || warehouseDeliveryCarton.hasDamagedLineItems())
        b++; 
    } 
    return b;
  }
  
  public int getNumberOfCartonsMissing() {
    byte b = 0;
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      switch (warehouseDeliveryCarton.getStatus()) {
        case MISSING:
        case IN_PROGRESS:
          b++;
      } 
    } 
    return b;
  }
  
  public int getNumberOfLineItemsExpected() {
    int i = 0;
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      i += warehouseDeliveryCarton.getNumberOfLineItemsExpected(); 
    return i;
  }
  
  public int getNumberOfLineItemsReceived() {
    int i = 0;
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      i += warehouseDeliveryCarton.getNumberOfLineItemsReceived(); 
    return i;
  }
  
  public int getNumberOfLineItemsDamaged() {
    int i = 0;
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      i += warehouseDeliveryCarton.getNumberOfLineItemsDamaged(); 
    return i;
  }
  
  public Quantity getNumberOfCasesExpected() {
    Quantity quantity = Quantity.ZERO;
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      quantity = quantity.add(warehouseDeliveryCarton.getNumberOfCasesExpected()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesReceived() {
    Quantity quantity = Quantity.ZERO;
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      quantity = quantity.add(warehouseDeliveryCarton.getNumberOfCasesReceived()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesDamaged() {
    Quantity quantity = Quantity.ZERO;
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      quantity = quantity.add(warehouseDeliveryCarton.getNumberOfCasesDamaged()); 
    return quantity;
  }
  
  public boolean isEmpty() {
    if (this.cartons.isEmpty())
      return true; 
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      if (!warehouseDeliveryCarton.isEmpty())
        return false; 
    } 
    return true;
  }
  
  public boolean hasUnexpectedLineItems() {
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      if (warehouseDeliveryCarton.hasUnexpectedLineItems())
        return true; 
    } 
    return false;
  }
  
  public boolean hasReceivedOrDamagedLineItems() {
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      if (warehouseDeliveryCarton.hasReceivedOrDamagedLineItems())
        return true; 
    } 
    return false;
  }
  
  public boolean isSerialNumberRequired() {
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      if (warehouseDeliveryCarton.isSerialNumberRequired())
        return true; 
    } 
    return false;
  }
  
  public boolean isFulfillmentOrderRelated() {
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons) {
      if (warehouseDeliveryCarton.isFulfillmentOrderRelated())
        return true; 
    } 
    return false;
  }
  
  public void markInProgress() throws BusinessException {
    executeRule("markInProgress", new Object[0]);
    if (this.status == WarehouseDeliveryStatus.RECEIVED && !this.allowAdjustment)
      throw new BusinessException(WarehouseDeliveryMessageText.RECEIVED_ERROR); 
    if (this.status == WarehouseDeliveryStatus.CANCELED)
      throw new BusinessException(WarehouseDeliveryMessageText.CANCELED_STATE_NOT_RECEIVABLE); 
    setStatus(WarehouseDeliveryStatus.IN_PROGRESS);
    doSetUpdateDate(SimDateUtil.getCurrentDate());
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      warehouseDeliveryCarton.setStatus(WarehouseDeliveryStatus.IN_PROGRESS); 
  }
  
  public void receiveAll() throws BusinessException {
    executeRule("receiveAll", new Object[0]);
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      warehouseDeliveryCarton.receiveAll(); 
  }
  
  public void receive() throws BusinessException {
    executeRule("receive", new Object[0]);
    if (this.status != WarehouseDeliveryStatus.IN_PROGRESS)
      throw new BusinessException(WarehouseDeliveryMessageText.IN_PROGRESS_TO_RECEIVE_ERROR); 
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      warehouseDeliveryCarton.receive(); 
    setStatus(WarehouseDeliveryStatus.RECEIVED);
    doSetCompleteDate(SimDateUtil.getCurrentDate());
  }
  
  public void reset() throws BusinessException {
    executeRule("reset", new Object[0]);
    if (this.status == WarehouseDeliveryStatus.RECEIVED && !this.allowAdjustment)
      throw new BusinessException(WarehouseDeliveryMessageText.RECEIVED_ERROR); 
    if (this.status == WarehouseDeliveryStatus.CANCELED)
      throw new BusinessException(WarehouseDeliveryMessageText.CANCELED_STATE_NOT_RECEIVABLE); 
    setStatus(WarehouseDeliveryStatus.IN_PROGRESS);
    doSetUpdateDate(SimDateUtil.getCurrentDate());
    for (WarehouseDeliveryCarton warehouseDeliveryCarton : this.cartons)
      warehouseDeliveryCarton.reset(); 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    WarehouseDelivery warehouseDelivery = (WarehouseDelivery)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, warehouseDelivery.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDelivery.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */