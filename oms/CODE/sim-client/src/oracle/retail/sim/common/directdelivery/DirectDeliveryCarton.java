package oracle.retail.sim.common.directdelivery;

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

public class DirectDeliveryCarton extends BusinessObject {
  private static final long serialVersionUID = 998739577602955398L;
  
  private boolean dirty;
  
  private Long id;
  
  private String externalId;
  
  private DirectDeliveryStatus status = DirectDeliveryStatus.NEW;
  
  private DirectDelivery delivery;
  
  private List<DirectDeliveryLineItem> lineItems = new ArrayList<>();
  
  private List<DirectDeliveryLineItem> removedLineItems = new ArrayList<>();
  
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
    checkForNullParameter("Direct Delivery Carton Id", paramLong);
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
  
  public DirectDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(DirectDeliveryStatus paramDirectDeliveryStatus) {
    this.status = paramDirectDeliveryStatus;
  }
  
  public void setStatus(DirectDeliveryStatus paramDirectDeliveryStatus) throws BusinessException {
    checkForNullParameter("Status", paramDirectDeliveryStatus);
    executeRule("setStatus", new Object[] { paramDirectDeliveryStatus });
    if (this.status != paramDirectDeliveryStatus) {
      doSetStatus(paramDirectDeliveryStatus);
      doSetDirty(true);
    } 
  }
  
  public DirectDelivery getDelivery() {
    return this.delivery;
  }
  
  public void doSetDelivery(DirectDelivery paramDirectDelivery) {
    this.delivery = paramDirectDelivery;
  }
  
  public List<DirectDeliveryLineItem> getLineItems() {
    return Collections.unmodifiableList(this.lineItems);
  }
  
  public List<DirectDeliverySimpleLineItem> getSimpleLineItems() {
    ArrayList<DirectDeliverySimpleLineItem> arrayList = new ArrayList(this.lineItems.size());
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem instanceof DirectDeliverySimpleLineItem) {
        arrayList.add((DirectDeliverySimpleLineItem)directDeliveryLineItem);
        continue;
      } 
      if (directDeliveryLineItem instanceof DirectDeliveryCompositeLineItem)
        Collections.addAll(arrayList, ((DirectDeliveryCompositeLineItem)directDeliveryLineItem).getLineItems()); 
    } 
    return arrayList;
  }
  
  public List<DirectDeliveryLineItem> getRemovedLineItems() {
    return Collections.unmodifiableList(this.removedLineItems);
  }
  
  public List<DirectDeliverySimpleLineItem> getRemovedSimpleLineItems() {
    ArrayList<DirectDeliverySimpleLineItem> arrayList = new ArrayList(this.removedLineItems.size());
    for (DirectDeliveryLineItem directDeliveryLineItem : this.removedLineItems) {
      if (directDeliveryLineItem instanceof DirectDeliverySimpleLineItem) {
        arrayList.add((DirectDeliverySimpleLineItem)directDeliveryLineItem);
        continue;
      } 
      if (directDeliveryLineItem instanceof DirectDeliveryCompositeLineItem)
        Collections.addAll(arrayList, ((DirectDeliveryCompositeLineItem)directDeliveryLineItem).getLineItems()); 
    } 
    return arrayList;
  }
  
  public void doAddLineItem(DirectDeliveryLineItem paramDirectDeliveryLineItem) {
    paramDirectDeliveryLineItem.doSetCarton(this);
    this.lineItems.add(paramDirectDeliveryLineItem);
  }
  
  public void addLineItem(DirectDeliveryLineItem paramDirectDeliveryLineItem) throws BusinessException {
    checkForNullParameter("Add Line Item", paramDirectDeliveryLineItem);
    executeRule("addLineItem", new Object[] { paramDirectDeliveryLineItem });
    if (findLineItemIndex(paramDirectDeliveryLineItem, this.lineItems) >= 0)
      throw new BusinessException(DirectDeliveryMessageText.DUPLICATE_ITEM); 
    doAddLineItem(paramDirectDeliveryLineItem);
  }
  
  public DirectDeliveryLineItem doRemoveLineItem(DirectDeliveryLineItem paramDirectDeliveryLineItem) {
    int i = findLineItemIndex(paramDirectDeliveryLineItem, this.lineItems);
    if (i < 0)
      return null; 
    DirectDeliveryLineItem directDeliveryLineItem = this.lineItems.remove(i);
    if (!directDeliveryLineItem.isNew())
      this.removedLineItems.add(directDeliveryLineItem); 
    return directDeliveryLineItem;
  }
  
  public DirectDeliveryLineItem removeLineItem(DirectDeliveryLineItem paramDirectDeliveryLineItem) throws BusinessException {
    checkForNullParameter("Remove Line Item", paramDirectDeliveryLineItem);
    executeRule("removeLineItem", new Object[] { paramDirectDeliveryLineItem });
    return doRemoveLineItem(paramDirectDeliveryLineItem);
  }
  
  private static int findLineItemIndex(DirectDeliveryLineItem paramDirectDeliveryLineItem, List<DirectDeliveryLineItem> paramList) {
    String str = paramDirectDeliveryLineItem.getStockItem().getId();
    for (byte b = 0; b < paramList.size(); b++) {
      if (str.equals(((DirectDeliveryLineItem)paramList.get(b)).getStockItem().getId()))
        return b; 
    } 
    return -1;
  }
  
  public void clearRemovedLineItems() {
    this.removedLineItems = new ArrayList<>();
  }
  
  public Map<String, StockItem> getAssociatedStockItems() {
    HashMap<Object, Object> hashMap = new HashMap<>(this.lineItems.size());
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems)
      hashMap.put(directDeliveryLineItem.getStockItem().getId(), directDeliveryLineItem.getStockItem()); 
    return (Map)hashMap;
  }
  
  public int getSerialNumberCountExpected() {
    int i = 0;
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.getStockItem().isSerialNumberRequired()) {
        Quantity quantity = directDeliveryLineItem.getQuantityExpected();
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
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.getQuantityExpectedOrZero().isPositive())
        b++; 
    } 
    return b;
  }
  
  public int getNumberOfLineItemsReceived() {
    byte b = 0;
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.getQuantityReceivedOrZero().isPositive())
        b++; 
    } 
    return b;
  }
  
  public int getNumberOfLineItemsDamaged() {
    byte b = 0;
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.getQuantityDamagedOrZero().isPositive())
        b++; 
    } 
    return b;
  }
  
  public Quantity getNumberOfCasesExpected() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems)
      quantity = addNumberOfCases(quantity, directDeliveryLineItem.getQuantityExpected(), directDeliveryLineItem.getCaseSize()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesReceived() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems)
      quantity = addNumberOfCases(quantity, directDeliveryLineItem.getQuantityReceived(), directDeliveryLineItem.getCaseSize()); 
    return quantity;
  }
  
  public Quantity getNumberOfCasesDamaged() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems)
      quantity = addNumberOfCases(quantity, directDeliveryLineItem.getQuantityDamaged(), directDeliveryLineItem.getCaseSize()); 
    return quantity;
  }
  
  private static Quantity addNumberOfCases(Quantity paramQuantity1, Quantity paramQuantity2, Quantity paramQuantity3) {
    return (paramQuantity2 == null || !paramQuantity2.isPositive()) ? paramQuantity1 : ((paramQuantity3 == null || paramQuantity3.compareTo(Quantity.ONE) == 0 || !paramQuantity3.isPositive()) ? paramQuantity1.add(paramQuantity2) : ((paramQuantity2.compareTo(paramQuantity3) <= 0) ? paramQuantity1.add(Quantity.ONE) : paramQuantity1.add(paramQuantity2.divide(paramQuantity3, 0, RoundingMode.UP))));
  }
  
  public boolean isEmpty() {
    return this.lineItems.isEmpty();
  }
  
  public boolean hasExpectedLineItems() {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.isExpected())
        return true; 
    } 
    return false;
  }
  
  public boolean hasUnexpectedLineItems() {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (!directDeliveryLineItem.isExpected())
        return true; 
    } 
    return false;
  }
  
  public boolean hasReceivedLineItems() {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.getQuantityReceivedOrZero().isPositive())
        return true; 
    } 
    return false;
  }
  
  public boolean hasDamagedLineItems() {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.getQuantityDamagedOrZero().isPositive())
        return true; 
    } 
    return false;
  }
  
  public boolean hasReceivedOrDamagedLineItems() {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.getQuantityReceivedOrZero().isPositive() || directDeliveryLineItem.getQuantityDamagedOrZero().isPositive())
        return true; 
    } 
    return false;
  }
  
  public boolean hasDiscrepantReceivedLineItems() {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.getQuantityReceivedDiscrepantOrZero().isPositive())
        return true; 
    } 
    return false;
  }
  
  public boolean hasDiscrepantDamagedLineItems() {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (directDeliveryLineItem.getQuantityDamagedDiscrepantOrZero().isPositive())
        return true; 
    } 
    return false;
  }
  
  public void receiveAll() throws BusinessException {
    executeRule("receiveAll", new Object[0]);
    if (this.status == DirectDeliveryStatus.MISSING)
      reset(); 
    DirectDeliveryStatus directDeliveryStatus = DirectDeliveryStatus.MISSING;
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (!directDeliveryLineItem.isReceivable())
        continue; 
      if (directDeliveryLineItem.getQuantityDamaged() == null) {
        directDeliveryLineItem.setQuantityDamaged(Quantity.ZERO);
      } else if (directDeliveryStatus != DirectDeliveryStatus.DAMAGED && directDeliveryLineItem.getQuantityDamaged().isPositive()) {
        directDeliveryStatus = DirectDeliveryStatus.DAMAGED;
      } 
      if (directDeliveryLineItem.getQuantityReceived() == null) {
        Quantity quantity = Quantity.ZERO;
        if (!directDeliveryLineItem.getQuantityDamagedOrZero().isPositive()) {
          StockItem stockItem = directDeliveryLineItem.getStockItem();
          if (!stockItem.isSerialNumberRequired() || stockItem.isAgsnEnabled())
            quantity = directDeliveryLineItem.getQuantityExpected(); 
        } 
        directDeliveryLineItem.setQuantityReceived(quantity);
      } 
      if (directDeliveryStatus == DirectDeliveryStatus.MISSING && directDeliveryLineItem.getQuantityReceived().isPositive())
        directDeliveryStatus = DirectDeliveryStatus.RECEIVED; 
    } 
    setStatus(directDeliveryStatus);
  }
  
  public void receive() throws BusinessException {
    executeRule("receive", new Object[0]);
    DirectDeliveryStatus directDeliveryStatus = DirectDeliveryStatus.MISSING;
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (!directDeliveryLineItem.isReceivable()) {
        if (directDeliveryLineItem.getQuantityReceived() != null || directDeliveryLineItem.getQuantityDamaged() != null)
          directDeliveryLineItem.clearReceivedQuantities(); 
        continue;
      } 
      if (directDeliveryLineItem.getQuantityDamaged() == null) {
        directDeliveryLineItem.setQuantityDamaged(Quantity.ZERO);
      } else if (directDeliveryStatus != DirectDeliveryStatus.DAMAGED && directDeliveryLineItem.getQuantityDamaged().isPositive()) {
        directDeliveryStatus = DirectDeliveryStatus.DAMAGED;
      } 
      if (directDeliveryLineItem.getQuantityReceived() == null) {
        directDeliveryLineItem.setQuantityReceived(Quantity.ZERO);
        continue;
      } 
      if (directDeliveryStatus == DirectDeliveryStatus.MISSING && directDeliveryLineItem.getQuantityReceived().isPositive())
        directDeliveryStatus = DirectDeliveryStatus.RECEIVED; 
    } 
    setStatus(directDeliveryStatus);
  }
  
  public void reject() throws BusinessException {
    executeRule("reject", new Object[0]);
    removeSerialNumbers();
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems) {
      if (!directDeliveryLineItem.isReceivable()) {
        if (directDeliveryLineItem.getQuantityReceived() != null || directDeliveryLineItem.getQuantityDamaged() != null)
          directDeliveryLineItem.clearReceivedQuantities(); 
        continue;
      } 
      directDeliveryLineItem.setQuantityReceived(Quantity.ZERO);
      directDeliveryLineItem.setQuantityDamaged(Quantity.ZERO);
    } 
    setStatus(DirectDeliveryStatus.MISSING);
  }
  
  public void reset() throws BusinessException {
    executeRule("reset", new Object[0]);
    setStatus(DirectDeliveryStatus.IN_PROGRESS);
    clearReceivedQuantities();
    removeSerialNumbers();
  }
  
  public void clearReceivedQuantities() throws BusinessException {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems)
      directDeliveryLineItem.clearReceivedQuantities(); 
  }
  
  private void removeSerialNumbers() throws BusinessException {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems)
      directDeliveryLineItem.removeSerialNumbers(); 
  }
  
  public void clearRemovedSerialNumbers() {
    for (DirectDeliveryLineItem directDeliveryLineItem : this.lineItems)
      directDeliveryLineItem.clearRemovedSerialNumbers(); 
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    DirectDeliveryCarton directDeliveryCarton = (DirectDeliveryCarton)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.externalId, directDeliveryCarton.externalId);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.externalId);
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryCarton.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */