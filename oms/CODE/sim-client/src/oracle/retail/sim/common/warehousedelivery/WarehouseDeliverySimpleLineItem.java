package oracle.retail.sim.common.warehousedelivery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.rules.RulesInfo;
import oracle.retail.sim.common.rules.core.CurrencyMustBePositiveRule;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;
import oracle.retail.sim.common.rules.core.QuantityMustBePositiveRule;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class WarehouseDeliverySimpleLineItem extends BusinessObject implements WarehouseDeliveryLineItem {
  private static final long serialVersionUID = -8023582211439083056L;
  
  private boolean dirty;
  
  private Long id;
  
  private StockItem stockItem;
  
  private Quantity caseSize;
  
  private SimMoney unitCost;
  
  private String receiptDocumentId;
  
  private String receiptDocumentType;
  
  private String receiptParentDocumentId;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private String comments;
  
  private Quantity quantityExpected;
  
  private Quantity quantityReceived;
  
  private Quantity quantityDamaged;
  
  private WarehouseDeliveryCarton carton;
  
  private List<SerialNumberValue> serialNumbers = new ArrayList<>();
  
  private List<SerialNumberValue> removedSerialNumbers = new ArrayList<>();
  
  public WarehouseDeliverySimpleLineItem(StockItem paramStockItem) {
    this.stockItem = paramStockItem;
    this.caseSize = paramStockItem.getDefaultCaseSize();
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetDirty(boolean paramBoolean) {
    this.dirty = paramBoolean;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public StockItem getStockItem() {
    return this.stockItem;
  }
  
  public Quantity getCaseSize() {
    return this.caseSize;
  }
  
  public void doSetCaseSize(Quantity paramQuantity) {
    this.caseSize = paramQuantity;
  }
  
  public void setCaseSize(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Case Size", paramQuantity);
    QuantityMustBePositiveRule.execute(paramQuantity);
    executeRule("setCaseSize", new Object[] { paramQuantity });
    doSetCaseSize(paramQuantity);
    doSetDirty(true);
  }
  
  public SimMoney getUnitCost() {
    return this.unitCost;
  }
  
  public void doSetUnitCost(SimMoney paramSimMoney) {
    this.unitCost = paramSimMoney;
  }
  
  public void setUnitCost(SimMoney paramSimMoney) throws BusinessException {
    RulesInfo rulesInfo = CurrencyMustBePositiveRule.execute(paramSimMoney);
    if (!rulesInfo.isError()) {
      executeRule("setUnitCost", new Object[] { paramSimMoney });
      if (isAttributesNotEqual(paramSimMoney, this.unitCost)) {
        doSetUnitCost(paramSimMoney);
        doSetDirty(true);
      } 
    } else {
      throw new BusinessException(rulesInfo.getMessage());
    } 
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public String getReceiptDocumentId() {
    return this.receiptDocumentId;
  }
  
  public void doSetReceiptDocumentId(String paramString) {
    this.receiptDocumentId = paramString;
  }
  
  public String getReceiptDocumentType() {
    return this.receiptDocumentType;
  }
  
  public void doSetReceiptDocumentType(String paramString) {
    this.receiptDocumentType = paramString;
  }
  
  public String getReceiptParentDocumentId() {
    return this.receiptParentDocumentId;
  }
  
  public void doSetReceiptParentDocumentId(String paramString) {
    this.receiptParentDocumentId = paramString;
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
  
  public boolean isExpected() {
    return (this.quantityExpected != null && this.quantityExpected.isPositive());
  }
  
  public Quantity getQuantityExpected() {
    return this.quantityExpected;
  }
  
  public Quantity getQuantityExpectedOrZero() {
    return (this.quantityExpected != null) ? this.quantityExpected : Quantity.ZERO;
  }
  
  public void doSetQuantityExpected(Quantity paramQuantity) {
    this.quantityExpected = paramQuantity;
  }
  
  public void setQuantityExpected(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Quantity Expected", paramQuantity);
    QuantityCannotBeNegativeRule.execute(paramQuantity);
    executeRule("setQuantityExpected", new Object[] { paramQuantity });
    doSetQuantityExpected(paramQuantity);
    doSetDirty(true);
  }
  
  public Quantity getQuantityReceived() {
    return this.quantityReceived;
  }
  
  public Quantity getQuantityReceivedOrZero() {
    return (this.quantityReceived != null) ? this.quantityReceived : Quantity.ZERO;
  }
  
  public void doSetQuantityReceived(Quantity paramQuantity) {
    this.quantityReceived = paramQuantity;
  }
  
  public void setQuantityReceived(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Quantity Received", paramQuantity);
    QuantityCannotBeNegativeRule.execute(paramQuantity);
    executeRule("setQuantityReceived", new Object[] { paramQuantity });
    doSetQuantityReceived(paramQuantity);
    doSetDirty(true);
  }
  
  public Quantity getQuantityDamaged() {
    return this.quantityDamaged;
  }
  
  public Quantity getQuantityDamagedOrZero() {
    return (this.quantityDamaged != null) ? this.quantityDamaged : Quantity.ZERO;
  }
  
  public void doSetQuantityDamaged(Quantity paramQuantity) {
    this.quantityDamaged = paramQuantity;
  }
  
  public void setQuantityDamaged(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Quantity Damaged", paramQuantity);
    QuantityCannotBeNegativeRule.execute(paramQuantity);
    executeRule("setQuantityDamaged", new Object[] { paramQuantity });
    doSetQuantityDamaged(paramQuantity);
    doSetDirty(true);
  }
  
  public void clearReceivedQuantities() throws BusinessException {
    executeRule("clearReceivedQuantities", new Object[0]);
    this.quantityReceived = null;
    this.quantityDamaged = null;
    doSetDirty(true);
  }
  
  public WarehouseDeliveryCarton getCarton() {
    return this.carton;
  }
  
  public void doSetCarton(WarehouseDeliveryCarton paramWarehouseDeliveryCarton) {
    this.carton = paramWarehouseDeliveryCarton;
  }
  
  public List<SerialNumberValue> getSerialNumbers() {
    return Collections.unmodifiableList(this.serialNumbers);
  }
  
  public void doSetSerialNumbers(List<SerialNumberValue> paramList) {
    this.serialNumbers = paramList;
  }
  
  public List<SerialNumberValue> getRemovedSerialNumbers() {
    return Collections.unmodifiableList(this.removedSerialNumbers);
  }
  
  public void clearRemovedSerialNumbers() {
    this.removedSerialNumbers = new ArrayList<>();
  }
  
  public void doAddSerialNumber(SerialNumberValue paramSerialNumberValue) {
    if (findSerialNumberIndex(paramSerialNumberValue.getUin(), this.serialNumbers) >= 0)
      return; 
    int i = findSerialNumberIndex(paramSerialNumberValue.getUin(), this.removedSerialNumbers);
    if (i >= 0)
      this.removedSerialNumbers.remove(i); 
    this.serialNumbers.add(paramSerialNumberValue);
    paramSerialNumberValue.doSetDirty(true);
    doSetDirty(true);
  }
  
  public void addSerialNumber(SerialNumberValue paramSerialNumberValue) throws BusinessException {
    if (!this.stockItem.isSerialNumberRequired())
      throw new BusinessException(UINMessageText.ITEM_NOT_UIN_ENABLED); 
    checkForNullParameter("UIN", paramSerialNumberValue);
    checkForNullParameter("UIN", paramSerialNumberValue.getUin());
    executeRule("addSerialNumber", new Object[] { paramSerialNumberValue });
    doAddSerialNumber(paramSerialNumberValue);
  }
  
  public SerialNumberValue doRemoveSerialNumber(String paramString) {
    if (this.serialNumbers.isEmpty())
      return null; 
    int i = findSerialNumberIndex(paramString, this.serialNumbers);
    if (i < 0)
      return null; 
    SerialNumberValue serialNumberValue = this.serialNumbers.remove(i);
    if (serialNumberValue.getId() != null)
      this.removedSerialNumbers.add(serialNumberValue); 
    doSetDirty(true);
    return serialNumberValue;
  }
  
  public SerialNumberValue removeSerialNumber(String paramString) throws BusinessException {
    checkForNullParameter("UIN", paramString);
    executeRule("removeSerialNumber", new Object[] { paramString });
    return doRemoveSerialNumber(paramString);
  }
  
  public void removeSerialNumbers() throws BusinessException {
    executeRule("removeSerialNumbers", new Object[0]);
    if (this.serialNumbers.isEmpty())
      return; 
    for (SerialNumberValue serialNumberValue : this.serialNumbers) {
      if (serialNumberValue.getId() != null)
        this.removedSerialNumbers.add(serialNumberValue); 
    } 
    this.serialNumbers = new ArrayList<>();
  }
  
  private static int findSerialNumberIndex(String paramString, List<SerialNumberValue> paramList) {
    for (byte b = 0; b < paramList.size(); b++) {
      if (paramString.equals(((SerialNumberValue)paramList.get(b)).getUin()))
        return b; 
    } 
    return -1;
  }
  
  public void resetSerialNumberQuantities() throws BusinessException {
    if (!this.stockItem.isSerialNumberRequired())
      return; 
    byte b1 = 0;
    byte b2 = 0;
    for (SerialNumberValue serialNumberValue : this.serialNumbers) {
      if (serialNumberValue.isDamaged()) {
        b1++;
        continue;
      } 
      b2++;
    } 
    setQuantityDamaged(new Quantity(b1));
    setQuantityReceived(new Quantity(b2));
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    WarehouseDeliverySimpleLineItem warehouseDeliverySimpleLineItem = (WarehouseDeliverySimpleLineItem)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.stockItem.getId(), warehouseDeliverySimpleLineItem.stockItem.getId());
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.stockItem.getId());
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliverySimpleLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */