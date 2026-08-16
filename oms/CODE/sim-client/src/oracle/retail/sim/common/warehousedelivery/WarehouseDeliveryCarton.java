package oracle.retail.sim.common.warehousedelivery;

import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.SerialNumberLineItem;
import oracle.retail.sim.common.item.StockItem;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class WarehouseDeliveryCarton extends BusinessObject {
  private static final long serialVersionUID = 5948928157866489025L;
  
  private boolean dirty;
  
  private Long id;
  
  private String externalId;
  
  private WarehouseDeliveryStatus status = WarehouseDeliveryStatus.NEW;
  
  private WarehouseDelivery delivery;
  
  private List<WarehouseDeliveryLineItem> lineItems = new ArrayList<>();
  
  private List<WarehouseDeliveryLineItem> removedLineItems = new ArrayList<>();
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetDirty(boolean paramBoolean) {
    this.dirty = paramBoolean;
  }
  
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
    checkForNullParameter("Warehouse Delivery Carton Id", paramLong);
    executeRule("setId", new Object[] { paramLong });
    doSetId(paramLong);
    doSetDirty(true);
  }
  
  public boolean isInternal() {
    return (this.externalId == null);
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
    doSetDirty(true);
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
    if (this.status != paramWarehouseDeliveryStatus) {
      doSetStatus(paramWarehouseDeliveryStatus);
      doSetDirty(true);
    } 
  }
  
  public WarehouseDelivery getDelivery() {
    return this.delivery;
  }
  
  public void doSetDelivery(WarehouseDelivery paramWarehouseDelivery) {
    this.delivery = paramWarehouseDelivery;
  }
  
  public List<WarehouseDeliveryLineItem> getLineItems() {
    return Collections.unmodifiableList(this.lineItems);
  }
  
  public List<WarehouseDeliverySimpleLineItem> getSimpleLineItems() {
    ArrayList<WarehouseDeliverySimpleLineItem> arrayList = new ArrayList(this.lineItems.size());
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem instanceof WarehouseDeliverySimpleLineItem) {
        arrayList.add((WarehouseDeliverySimpleLineItem)warehouseDeliveryLineItem);
        continue;
      } 
      if (warehouseDeliveryLineItem instanceof WarehouseDeliveryCompositeLineItem)
        Collections.addAll(arrayList, ((WarehouseDeliveryCompositeLineItem)warehouseDeliveryLineItem).getLineItems()); 
    } 
    return arrayList;
  }
  
  public List<WarehouseDeliveryLineItem> getRemovedLineItems() {
    return Collections.unmodifiableList(this.removedLineItems);
  }
  
  public List<WarehouseDeliverySimpleLineItem> getRemovedSimpleLineItems() {
    ArrayList<WarehouseDeliverySimpleLineItem> arrayList = new ArrayList(this.removedLineItems.size());
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.removedLineItems) {
      if (warehouseDeliveryLineItem instanceof WarehouseDeliverySimpleLineItem) {
        arrayList.add((WarehouseDeliverySimpleLineItem)warehouseDeliveryLineItem);
        continue;
      } 
      if (warehouseDeliveryLineItem instanceof WarehouseDeliveryCompositeLineItem)
        Collections.addAll(arrayList, ((WarehouseDeliveryCompositeLineItem)warehouseDeliveryLineItem).getLineItems()); 
    } 
    return arrayList;
  }
  
  public void doAddLineItem(WarehouseDeliveryLineItem paramWarehouseDeliveryLineItem) {
    paramWarehouseDeliveryLineItem.doSetCarton(this);
    this.lineItems.add(paramWarehouseDeliveryLineItem);
  }
  
  public void addLineItem(WarehouseDeliveryLineItem paramWarehouseDeliveryLineItem) throws BusinessException {
    checkForNullParameter("Add Line Item", paramWarehouseDeliveryLineItem);
    executeRule("addLineItem", new Object[] { paramWarehouseDeliveryLineItem });
    if (findLineItemIndex(paramWarehouseDeliveryLineItem, this.lineItems) >= 0)
      throw new BusinessException(WarehouseDeliveryMessageText.DUPLICATE_ITEM); 
    doAddLineItem(paramWarehouseDeliveryLineItem);
  }
  
  public WarehouseDeliveryLineItem doRemoveLineItem(WarehouseDeliveryLineItem paramWarehouseDeliveryLineItem) {
    int i = findLineItemIndex(paramWarehouseDeliveryLineItem, this.lineItems);
    if (i < 0)
      return null; 
    WarehouseDeliveryLineItem warehouseDeliveryLineItem = this.lineItems.remove(i);
    if (!warehouseDeliveryLineItem.isNew())
      this.removedLineItems.add(warehouseDeliveryLineItem); 
    return warehouseDeliveryLineItem;
  }
  
  public WarehouseDeliveryLineItem removeLineItem(WarehouseDeliveryLineItem paramWarehouseDeliveryLineItem) throws BusinessException {
    checkForNullParameter("Remove Line Item", paramWarehouseDeliveryLineItem);
    executeRule("removeLineItem", new Object[] { paramWarehouseDeliveryLineItem });
    return doRemoveLineItem(paramWarehouseDeliveryLineItem);
  }
  
  private static int findLineItemIndex(WarehouseDeliveryLineItem paramWarehouseDeliveryLineItem, List<WarehouseDeliveryLineItem> paramList) {
    String str = paramWarehouseDeliveryLineItem.getStockItem().getId();
    for (byte b = 0; b < paramList.size(); b++) {
      if (str.equals(((WarehouseDeliveryLineItem)paramList.get(b)).getStockItem().getId()))
        return b; 
    } 
    return -1;
  }
  
  public void clearRemovedLineItems() {
    this.removedLineItems = new ArrayList<>();
  }
  
  public Map<String, StockItem> getAssociatedStockItems() {
    HashMap<Object, Object> hashMap = new HashMap<>(this.lineItems.size());
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems)
      hashMap.put(warehouseDeliveryLineItem.getStockItem().getId(), warehouseDeliveryLineItem.getStockItem()); 
    return (Map)hashMap;
  }
  
  public int getSerialNumberCountExpected() {
    int i = 0;
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getStockItem().isSerialNumberRequired()) {
        Quantity quantity = warehouseDeliveryLineItem.getQuantityExpected();
        if (quantity != null && quantity.isPositive())
          i += quantity.intValue(); 
      } 
    } 
    return i;
  }
  
  public int getSerialNumberCount() {
    int i = 0;
    for (SerialNumberLineItem serialNumberLineItem : this.lineItems)
      i += serialNumberLineItem.getSerialNumbers().size(); 
    return i;
  }
  
  public int getNumberOfLineItemsExpected() {
    byte b = 0;
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getQuantityExpectedOrZero().isPositive())
        b++; 
    } 
    return b;
  }
  
  public int getNumberOfLineItemsReceived() {
    byte b = 0;
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getQuantityReceivedOrZero().isPositive())
        b++; 
    } 
    return b;
  }
  
  public int getNumberOfLineItemsDamaged() {
    byte b = 0;
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getQuantityDamagedOrZero().isPositive())
        b++; 
    } 
    return b;
  }
  
  public Quantity getNumberOfCasesExpected() {
    Quantity quantity = Quantity.ZERO;
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems)
      quantity = addNumberOfCases(quantity, warehouseDeliveryLineItem.getQuantityExpected(), warehouseDeliveryLineItem.getCaseSize()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesReceived() {
    Quantity quantity = Quantity.ZERO;
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems)
      quantity = addNumberOfCases(quantity, warehouseDeliveryLineItem.getQuantityReceived(), warehouseDeliveryLineItem.getCaseSize()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesDamaged() {
    Quantity quantity = Quantity.ZERO;
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems)
      quantity = addNumberOfCases(quantity, warehouseDeliveryLineItem.getQuantityDamaged(), warehouseDeliveryLineItem.getCaseSize()); 
    return quantity;
  }
  
  private static Quantity addNumberOfCases(Quantity paramQuantity1, Quantity paramQuantity2, Quantity paramQuantity3) {
    return (paramQuantity2 == null || !paramQuantity2.isPositive()) ? paramQuantity1 : ((paramQuantity3 == null || paramQuantity3.compareTo(Quantity.ONE) == 0 || !paramQuantity3.isPositive()) ? paramQuantity1.add(paramQuantity2) : ((paramQuantity2.compareTo(paramQuantity3) <= 0) ? paramQuantity1.add(Quantity.ONE) : paramQuantity1.add(paramQuantity2.divide(paramQuantity3, 0, RoundingMode.UP))));
  }
  
  public boolean isEmpty() {
    return this.lineItems.isEmpty();
  }
  
  public boolean hasExpectedLineItems() {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.isExpected())
        return true; 
    } 
    return false;
  }
  
  public boolean hasUnexpectedLineItems() {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (!warehouseDeliveryLineItem.isExpected())
        return true; 
    } 
    return false;
  }
  
  public boolean hasReceivedLineItems() {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getQuantityReceivedOrZero().isPositive())
        return true; 
    } 
    return false;
  }
  
  public boolean hasDamagedLineItems() {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getQuantityDamagedOrZero().isPositive())
        return true; 
    } 
    return false;
  }
  
  public boolean hasReceivedOrDamagedLineItems() {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getQuantityReceivedOrZero().isPositive() || warehouseDeliveryLineItem.getQuantityDamagedOrZero().isPositive())
        return true; 
    } 
    return false;
  }
  
  public boolean isSerialNumberRequired() {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getStockItem().isSerialNumberRequired())
        return true; 
    } 
    return false;
  }
  
  public boolean isOnlyAGSNs() {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      StockItem stockItem = warehouseDeliveryLineItem.getStockItem();
      if (stockItem.isSerialNumberRequired() && !stockItem.isAgsnEnabled())
        return false; 
    } 
    return true;
  }
  
  public boolean isFulfillmentOrderRelated() {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.isFulfillmentOrderRelated())
        return true; 
    } 
    return false;
  }
  
  public boolean isOnlyFulfillmentOrder() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : getSimpleLineItems()) {
      if (!warehouseDeliverySimpleLineItem.isFulfillmentOrderRelated())
        return false; 
    } 
    return true;
  }
  
  public void receiveAll() throws BusinessException {
    executeRule("receiveAll", new Object[0]);
    if (this.status == WarehouseDeliveryStatus.MISSING)
      reset(); 
    WarehouseDeliveryStatus warehouseDeliveryStatus = WarehouseDeliveryStatus.MISSING;
    boolean bool = (this.delivery != null && this.delivery.isFinisherDelivery()) ? true : false;
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getQuantityDamaged() == null) {
        warehouseDeliveryLineItem.setQuantityDamaged(Quantity.ZERO);
      } else if (warehouseDeliveryStatus != WarehouseDeliveryStatus.DAMAGED && warehouseDeliveryLineItem.getQuantityDamaged().isPositive()) {
        warehouseDeliveryStatus = WarehouseDeliveryStatus.DAMAGED;
      } 
      StockItem stockItem = warehouseDeliveryLineItem.getStockItem();
      if (warehouseDeliveryLineItem.getQuantityReceived() == null) {
        Quantity quantity = Quantity.ZERO;
        if (warehouseDeliveryLineItem.getQuantityDamagedOrZero().isZero() && (!stockItem.isSerialNumberRequired() || (stockItem.isAgsnEnabled() && !bool)))
          quantity = warehouseDeliveryLineItem.getQuantityExpected(); 
        warehouseDeliveryLineItem.setQuantityReceived(quantity);
      } 
      if ((warehouseDeliveryStatus == WarehouseDeliveryStatus.MISSING && warehouseDeliveryLineItem.getQuantityReceived().isPositive()) || (stockItem.isSerialNumberRequired() && warehouseDeliveryLineItem.getQuantityReceivedOrZero().isZero()))
        warehouseDeliveryStatus = WarehouseDeliveryStatus.RECEIVED; 
    } 
    setStatus(warehouseDeliveryStatus);
  }
  
  public void receive() throws BusinessException {
    executeRule("receive", new Object[0]);
    WarehouseDeliveryStatus warehouseDeliveryStatus = WarehouseDeliveryStatus.MISSING;
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems) {
      if (warehouseDeliveryLineItem.getQuantityDamaged() == null) {
        warehouseDeliveryLineItem.setQuantityDamaged(Quantity.ZERO);
      } else if (warehouseDeliveryStatus != WarehouseDeliveryStatus.DAMAGED && warehouseDeliveryLineItem.getQuantityDamaged().isPositive()) {
        warehouseDeliveryStatus = WarehouseDeliveryStatus.DAMAGED;
      } 
      if (warehouseDeliveryLineItem.getQuantityReceived() == null) {
        warehouseDeliveryLineItem.setQuantityReceived(Quantity.ZERO);
        continue;
      } 
      if (warehouseDeliveryStatus == WarehouseDeliveryStatus.MISSING && warehouseDeliveryLineItem.getQuantityReceived().isPositive())
        warehouseDeliveryStatus = WarehouseDeliveryStatus.RECEIVED; 
    } 
    setStatus(warehouseDeliveryStatus);
  }
  
  public void reset() throws BusinessException {
    executeRule("reset", new Object[0]);
    setStatus(WarehouseDeliveryStatus.IN_PROGRESS);
    clearReceivedQuantities();
    removeSerialNumbers();
  }
  
  public void clearReceivedQuantities() throws BusinessException {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems)
      warehouseDeliveryLineItem.clearReceivedQuantities(); 
  }
  
  private void removeSerialNumbers() throws BusinessException {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems)
      warehouseDeliveryLineItem.removeSerialNumbers(); 
  }
  
  public void clearRemovedSerialNumbers() {
    for (WarehouseDeliveryLineItem warehouseDeliveryLineItem : this.lineItems)
      warehouseDeliveryLineItem.clearRemovedSerialNumbers(); 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    WarehouseDeliveryCarton warehouseDeliveryCarton = (WarehouseDeliveryCarton)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.externalId, warehouseDeliveryCarton.externalId);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.externalId);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryCarton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */