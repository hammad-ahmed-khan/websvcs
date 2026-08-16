package oracle.retail.sim.common.directdelivery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.uin.SerialNumberValue;

public class DirectDeliveryCompositeLineItem extends BusinessObject implements DirectDeliveryLineItem {
  private static final long serialVersionUID = -6609079929527139323L;
  
  private DirectDeliverySimpleLineItem[] lineItems;
  
  public DirectDeliveryCompositeLineItem(DirectDeliverySimpleLineItem[] paramArrayOfDirectDeliverySimpleLineItem) {
    if (paramArrayOfDirectDeliverySimpleLineItem == null || paramArrayOfDirectDeliverySimpleLineItem.length < 2)
      throw new IllegalArgumentException("DirectDeliveryLineItems cannot be null or contain less than 2 line items!"); 
    this.lineItems = paramArrayOfDirectDeliverySimpleLineItem;
    sortLineItems();
  }
  
  public DirectDeliverySimpleLineItem[] getLineItems() {
    return this.lineItems;
  }
  
  private void sortLineItems() {
    Arrays.sort(this.lineItems, new DirectDeliverySimpleLineItemComparator());
  }
  
  public boolean isNew() {
    return false;
  }
  
  public boolean isDirty() {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.isDirty())
        return true; 
    } 
    return false;
  }
  
  public StockItem getStockItem() {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getStockItem() != null)
        return directDeliverySimpleLineItem.getStockItem(); 
    } 
    return null;
  }
  
  public Quantity getCaseSize() {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getCaseSize() != null)
        return directDeliverySimpleLineItem.getCaseSize(); 
    } 
    return null;
  }
  
  public void setCaseSize(Quantity paramQuantity) throws BusinessException {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems)
      directDeliverySimpleLineItem.setCaseSize(paramQuantity); 
  }
  
  public SimMoney getUnitCost() {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getUnitCost() != null)
        return directDeliverySimpleLineItem.getUnitCost(); 
    } 
    return null;
  }
  
  public void setUnitCost(SimMoney paramSimMoney) throws BusinessException {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems)
      directDeliverySimpleLineItem.setUnitCost(paramSimMoney); 
  }
  
  public boolean isReceivable() {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.isReceivable())
        return true; 
    } 
    return false;
  }
  
  public boolean isExpected() {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.isExpected())
        return true; 
    } 
    return false;
  }
  
  public Quantity getQuantityExpected() {
    Quantity quantity = null;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityExpected() != null)
        quantity = (quantity != null) ? quantity.add(directDeliverySimpleLineItem.getQuantityExpected()) : directDeliverySimpleLineItem.getQuantityExpected(); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityExpectedOrZero() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityExpected() != null)
        quantity = quantity.add(directDeliverySimpleLineItem.getQuantityExpected()); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityReceived() {
    Quantity quantity = null;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityReceived() != null)
        quantity = (quantity != null) ? quantity.add(directDeliverySimpleLineItem.getQuantityReceived()) : directDeliverySimpleLineItem.getQuantityReceived(); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityReceivedOrZero() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityReceived() != null)
        quantity = quantity.add(directDeliverySimpleLineItem.getQuantityReceived()); 
    } 
    return quantity;
  }
  
  private void doSetQuantityReceived(Quantity paramQuantity) throws BusinessException {
    Quantity quantity = paramQuantity;
    for (byte b = 0; b < this.lineItems.length; b++) {
      DirectDeliverySimpleLineItem directDeliverySimpleLineItem = this.lineItems[b];
      Quantity quantity1 = directDeliverySimpleLineItem.getQuantityExpected();
      if (quantity.compareTo(quantity1) <= 0 || b >= this.lineItems.length - 1) {
        directDeliverySimpleLineItem.setQuantityReceived(quantity);
        quantity = Quantity.ZERO;
      } else {
        directDeliverySimpleLineItem.setQuantityReceived(quantity1);
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
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityDamaged() != null)
        quantity = (quantity != null) ? quantity.add(directDeliverySimpleLineItem.getQuantityDamaged()) : directDeliverySimpleLineItem.getQuantityDamaged(); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityDamagedOrZero() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityDamaged() != null)
        quantity = quantity.add(directDeliverySimpleLineItem.getQuantityDamaged()); 
    } 
    return quantity;
  }
  
  private void doSetQuantityDamaged(Quantity paramQuantity) throws BusinessException {
    Quantity quantity = paramQuantity;
    for (int i = this.lineItems.length - 1; i >= 0; i--) {
      DirectDeliverySimpleLineItem directDeliverySimpleLineItem = this.lineItems[i];
      Quantity quantity1 = directDeliverySimpleLineItem.getQuantityExpected();
      if (quantity.compareTo(quantity1) <= 0 || i <= 0) {
        directDeliverySimpleLineItem.setQuantityDamaged(quantity);
        quantity = Quantity.ZERO;
      } else {
        directDeliverySimpleLineItem.setQuantityDamaged(quantity1);
        quantity = quantity.subtract(quantity1);
      } 
    } 
  }
  
  public void setQuantityDamaged(Quantity paramQuantity) throws BusinessException {
    this.lineItems[0].checkForNullParameter("Quantity Damaged", paramQuantity);
    this.lineItems[0].executeRule("setQuantityDamaged", new Object[] { paramQuantity });
    doSetQuantityDamaged(paramQuantity);
  }
  
  public Quantity getQuantityReceivedDiscrepant() {
    Quantity quantity = null;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityReceivedDiscrepant() != null)
        quantity = (quantity != null) ? quantity.add(directDeliverySimpleLineItem.getQuantityReceivedDiscrepant()) : directDeliverySimpleLineItem.getQuantityReceivedDiscrepant(); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityReceivedDiscrepantOrZero() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityReceivedDiscrepant() != null)
        quantity = quantity.add(directDeliverySimpleLineItem.getQuantityReceivedDiscrepant()); 
    } 
    return quantity;
  }
  
  public void setQuantityReceivedDiscrepant(Quantity paramQuantity) throws BusinessException {
    this.lineItems[this.lineItems.length - 1].setQuantityReceivedDiscrepant(paramQuantity);
  }
  
  public Quantity getQuantityDamagedDiscrepant() {
    Quantity quantity = null;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityDamagedDiscrepant() != null)
        quantity = (quantity != null) ? quantity.add(directDeliverySimpleLineItem.getQuantityDamagedDiscrepant()) : directDeliverySimpleLineItem.getQuantityDamagedDiscrepant(); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityDamagedDiscrepantOrZero() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityDamagedDiscrepant() != null)
        quantity = quantity.add(directDeliverySimpleLineItem.getQuantityDamagedDiscrepant()); 
    } 
    return quantity;
  }
  
  public void setQuantityDamagedDiscrepant(Quantity paramQuantity) throws BusinessException {
    this.lineItems[this.lineItems.length - 1].setQuantityDamagedDiscrepant(paramQuantity);
  }
  
  public Quantity getQuantityShipped() {
    Quantity quantity = null;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityShipped() != null)
        quantity = (quantity != null) ? quantity.add(directDeliverySimpleLineItem.getQuantityShipped()) : directDeliverySimpleLineItem.getQuantityShipped(); 
    } 
    return quantity;
  }
  
  public Quantity getQuantityShippedOrZero() {
    Quantity quantity = Quantity.ZERO;
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getQuantityShipped() != null)
        quantity = quantity.add(directDeliverySimpleLineItem.getQuantityShipped()); 
    } 
    return quantity;
  }
  
  public void clearReceivedQuantities() throws BusinessException {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems)
      directDeliverySimpleLineItem.clearReceivedQuantities(); 
  }
  
  public DirectDeliveryCarton getCarton() {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      if (directDeliverySimpleLineItem.getCarton() != null)
        return directDeliverySimpleLineItem.getCarton(); 
    } 
    return null;
  }
  
  public void doSetCarton(DirectDeliveryCarton paramDirectDeliveryCarton) {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems)
      directDeliverySimpleLineItem.doSetCarton(paramDirectDeliveryCarton); 
  }
  
  public List<SerialNumberValue> getSerialNumbers() {
    ArrayList<SerialNumberValue> arrayList = new ArrayList();
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      List<SerialNumberValue> list = directDeliverySimpleLineItem.getSerialNumbers();
      if (!list.isEmpty())
        arrayList.addAll(list); 
    } 
    return arrayList;
  }
  
  public List<SerialNumberValue> getRemovedSerialNumbers() {
    ArrayList<SerialNumberValue> arrayList = new ArrayList();
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      List<SerialNumberValue> list = directDeliverySimpleLineItem.getRemovedSerialNumbers();
      if (!list.isEmpty())
        arrayList.addAll(list); 
    } 
    return arrayList;
  }
  
  public void clearRemovedSerialNumbers() {
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems)
      directDeliverySimpleLineItem.clearRemovedSerialNumbers(); 
  }
  
  private void doAddSerialNumber(SerialNumberValue paramSerialNumberValue) {
    if (getSerialNumbers().contains(paramSerialNumberValue))
      return; 
    for (byte b = 0; b < this.lineItems.length; b++) {
      DirectDeliverySimpleLineItem directDeliverySimpleLineItem = this.lineItems[b];
      if (directDeliverySimpleLineItem.getSerialNumbers().size() < directDeliverySimpleLineItem.getQuantityExpectedOrZero().intValue() || b >= this.lineItems.length - 1) {
        directDeliverySimpleLineItem.doAddSerialNumber(paramSerialNumberValue);
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
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems) {
      SerialNumberValue serialNumberValue = directDeliverySimpleLineItem.doRemoveSerialNumber(paramString);
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
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems)
      directDeliverySimpleLineItem.removeSerialNumbers(); 
  }
  
  public void resetSerialNumberQuantities() throws BusinessException {
    if (!getStockItem().isSerialNumberRequired())
      return; 
    for (DirectDeliverySimpleLineItem directDeliverySimpleLineItem : this.lineItems)
      directDeliverySimpleLineItem.resetSerialNumberQuantities(); 
  }
  
  public boolean equals(Object paramObject) {
    return (paramObject == this) ? true : ((paramObject == null || paramObject.getClass() != getClass()) ? false : Arrays.equals((Object[])this.lineItems, (Object[])((DirectDeliveryCompositeLineItem)paramObject).lineItems));
  }
  
  public int hashCode() {
    return 17 + Arrays.hashCode((Object[])this.lineItems);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryCompositeLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */