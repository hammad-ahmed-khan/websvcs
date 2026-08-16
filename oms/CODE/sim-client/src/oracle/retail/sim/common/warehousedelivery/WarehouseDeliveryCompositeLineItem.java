package oracle.retail.sim.common.warehousedelivery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.uin.SerialNumberValue;

public class WarehouseDeliveryCompositeLineItem extends BusinessObject implements WarehouseDeliveryLineItem {
  private static final long serialVersionUID = 7027261288335468024L;
  
  private WarehouseDeliverySimpleLineItem[] lineItems;
  
  public WarehouseDeliveryCompositeLineItem(WarehouseDeliverySimpleLineItem[] paramArrayOfWarehouseDeliverySimpleLineItem) {
    if (paramArrayOfWarehouseDeliverySimpleLineItem == null || paramArrayOfWarehouseDeliverySimpleLineItem.length < 2)
      throw new IllegalArgumentException("WarehouseDeliveryLineItems cannot be null or contain less than 2 line items!"); 
    this.lineItems = paramArrayOfWarehouseDeliverySimpleLineItem;
    sortLineItems();
  }
  
  public WarehouseDeliverySimpleLineItem[] getLineItems() {
    return this.lineItems;
  }
  
  private void sortLineItems() {
    Arrays.sort(this.lineItems, new WarehouseDeliverySimpleLineItemComparator());
  }
  
  public boolean isNew() {
    return false;
  }
  
  public boolean isDirty() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.isDirty())
        return true; 
    } 
    return false;
  }
  
  public StockItem getStockItem() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getStockItem() != null)
        return warehouseDeliverySimpleLineItem.getStockItem(); 
    } 
    return null;
  }
  
  public Quantity getCaseSize() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getCaseSize() != null)
        return warehouseDeliverySimpleLineItem.getCaseSize(); 
    } 
    return null;
  }
  
  public void setCaseSize(Quantity paramQuantity) throws BusinessException {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems)
      warehouseDeliverySimpleLineItem.setCaseSize(paramQuantity); 
  }
  
  public SimMoney getUnitCost() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getUnitCost() != null)
        return warehouseDeliverySimpleLineItem.getUnitCost(); 
    } 
    return null;
  }
  
  public void setUnitCost(SimMoney paramSimMoney) throws BusinessException {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems)
      warehouseDeliverySimpleLineItem.setUnitCost(paramSimMoney); 
  }
  
  public String getReceiptParentDocumentId() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (!StringHelper.isNullOrEmpty(warehouseDeliverySimpleLineItem.getReceiptParentDocumentId()))
        return warehouseDeliverySimpleLineItem.getReceiptParentDocumentId(); 
    } 
    return null;
  }
  
  public boolean isFulfillmentOrderRelated() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.isFulfillmentOrderRelated())
        return true; 
    } 
    return false;
  }
  
  public boolean isExpected() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.isExpected())
        return true; 
    } 
    return false;
  }
  
  public Quantity getQuantityExpected() {
    Quantity quantity = null;
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getQuantityExpected() != null)
        quantity = (quantity != null) ? quantity.add(warehouseDeliverySimpleLineItem.getQuantityExpected()) : warehouseDeliverySimpleLineItem.getQuantityExpected(); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityExpectedOrZero() {
    Quantity quantity = Quantity.ZERO;
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getQuantityExpected() != null)
        quantity = quantity.add(warehouseDeliverySimpleLineItem.getQuantityExpected()); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityReceived() {
    Quantity quantity = null;
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getQuantityReceived() != null)
        quantity = (quantity != null) ? quantity.add(warehouseDeliverySimpleLineItem.getQuantityReceived()) : warehouseDeliverySimpleLineItem.getQuantityReceived(); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityReceivedOrZero() {
    Quantity quantity = Quantity.ZERO;
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getQuantityReceived() != null)
        quantity = quantity.add(warehouseDeliverySimpleLineItem.getQuantityReceived()); 
    } 
    return quantity;
  }
  
  private void doSetQuantityReceived(Quantity paramQuantity) throws BusinessException {
    Quantity quantity = paramQuantity;
    for (byte b = 0; b < this.lineItems.length; b++) {
      WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem = this.lineItems[b];
      Quantity quantity1 = warehouseDeliverySimpleLineItem.getQuantityExpected();
      if (quantity.compareTo(quantity1) <= 0 || b >= this.lineItems.length - 1) {
        warehouseDeliverySimpleLineItem.setQuantityReceived(quantity);
        quantity = Quantity.ZERO;
      } else {
        warehouseDeliverySimpleLineItem.setQuantityReceived(quantity1);
        quantity = quantity.subtract(quantity1);
      } 
    } 
  }
  
  public void setQuantityReceived(Quantity paramQuantity) throws BusinessException {
    this.lineItems[0].checkForNullParameter("Quantity Received", paramQuantity);
    this.lineItems[0].executeRule("setQuantityReceived", new Object[] { paramQuantity });
    doSetQuantityReceived(paramQuantity);
  }
  
  public Quantity getQuantityDamaged() {
    Quantity quantity = null;
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getQuantityDamaged() != null)
        quantity = (quantity != null) ? quantity.add(warehouseDeliverySimpleLineItem.getQuantityDamaged()) : warehouseDeliverySimpleLineItem.getQuantityDamaged(); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityDamagedOrZero() {
    Quantity quantity = Quantity.ZERO;
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getQuantityDamaged() != null)
        quantity = quantity.add(warehouseDeliverySimpleLineItem.getQuantityDamaged()); 
    } 
    return quantity;
  }
  
  private void doSetQuantityDamaged(Quantity paramQuantity) throws BusinessException {
    Quantity quantity = paramQuantity;
    for (int i = this.lineItems.length - 1; i >= 0; i--) {
      WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem = this.lineItems[i];
      Quantity quantity1 = warehouseDeliverySimpleLineItem.getQuantityExpected();
      if (quantity.compareTo(quantity1) <= 0 || i <= 0) {
        warehouseDeliverySimpleLineItem.setQuantityDamaged(quantity);
        quantity = Quantity.ZERO;
      } else {
        warehouseDeliverySimpleLineItem.setQuantityDamaged(quantity1);
        quantity = quantity.subtract(quantity1);
      } 
    } 
  }
  
  public void setQuantityDamaged(Quantity paramQuantity) throws BusinessException {
    this.lineItems[0].checkForNullParameter("Quantity Damaged", paramQuantity);
    this.lineItems[0].executeRule("setQuantityDamaged", new Object[] { paramQuantity });
    doSetQuantityDamaged(paramQuantity);
  }
  
  public void clearReceivedQuantities() throws BusinessException {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems)
      warehouseDeliverySimpleLineItem.clearReceivedQuantities(); 
  }
  
  public WarehouseDeliveryCarton getCarton() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      if (warehouseDeliverySimpleLineItem.getCarton() != null)
        return warehouseDeliverySimpleLineItem.getCarton(); 
    } 
    return null;
  }
  
  public void doSetCarton(WarehouseDeliveryCarton paramWarehouseDeliveryCarton) {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems)
      warehouseDeliverySimpleLineItem.doSetCarton(paramWarehouseDeliveryCarton); 
  }
  
  public List<SerialNumberValue> getSerialNumbers() {
    ArrayList<SerialNumberValue> arrayList = new ArrayList();
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      List<SerialNumberValue> list = warehouseDeliverySimpleLineItem.getSerialNumbers();
      if (!list.isEmpty())
        arrayList.addAll(list); 
    } 
    return arrayList;
  }
  
  public List<SerialNumberValue> getRemovedSerialNumbers() {
    ArrayList<SerialNumberValue> arrayList = new ArrayList();
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      List<SerialNumberValue> list = warehouseDeliverySimpleLineItem.getRemovedSerialNumbers();
      if (!list.isEmpty())
        arrayList.addAll(list); 
    } 
    return arrayList;
  }
  
  public void clearRemovedSerialNumbers() {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems)
      warehouseDeliverySimpleLineItem.clearRemovedSerialNumbers(); 
  }
  
  private void doAddSerialNumber(SerialNumberValue paramSerialNumberValue) {
    if (getSerialNumbers().contains(paramSerialNumberValue))
      return; 
    for (byte b = 0; b < this.lineItems.length; b++) {
      WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem = this.lineItems[b];
      if (warehouseDeliverySimpleLineItem.getSerialNumbers().size() < warehouseDeliverySimpleLineItem.getQuantityExpectedOrZero().intValue() || b >= this.lineItems.length - 1) {
        warehouseDeliverySimpleLineItem.doAddSerialNumber(paramSerialNumberValue);
        break;
      } 
    } 
  }
  
  public void addSerialNumber(SerialNumberValue paramSerialNumberValue) throws BusinessException {
    this.lineItems[0].checkForNullParameter("UIN", paramSerialNumberValue);
    this.lineItems[0].checkForNullParameter("UIN", paramSerialNumberValue.getUin());
    this.lineItems[0].executeRule("addSerialNumber", new Object[] { paramSerialNumberValue });
    doAddSerialNumber(paramSerialNumberValue);
  }
  
  private SerialNumberValue doRemoveSerialNumber(String paramString) {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems) {
      SerialNumberValue serialNumberValue = warehouseDeliverySimpleLineItem.doRemoveSerialNumber(paramString);
      if (serialNumberValue != null)
        return serialNumberValue; 
    } 
    return null;
  }
  
  public SerialNumberValue removeSerialNumber(String paramString) throws BusinessException {
    this.lineItems[0].checkForNullParameter("UIN", paramString);
    this.lineItems[0].executeRule("removeSerialNumber", new Object[] { paramString });
    return doRemoveSerialNumber(paramString);
  }
  
  public void removeSerialNumbers() throws BusinessException {
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems)
      warehouseDeliverySimpleLineItem.removeSerialNumbers(); 
  }
  
  public void resetSerialNumberQuantities() throws BusinessException {
    if (!getStockItem().isSerialNumberRequired())
      return; 
    for (WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem : this.lineItems)
      warehouseDeliverySimpleLineItem.resetSerialNumberQuantities(); 
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject == this) ? true : ((paramObject == null || paramObject.getClass() != getClass()) ? false : Arrays.equals((Object[])this.lineItems, (Object[])((WarehouseDeliveryCompositeLineItem)paramObject).lineItems));
  }
  
  public int hashCode() {
    return 17 + Arrays.hashCode((Object[])this.lineItems);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryCompositeLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */