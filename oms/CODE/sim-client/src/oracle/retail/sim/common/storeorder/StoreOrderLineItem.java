package oracle.retail.sim.common.storeorder;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.rules.RulesInfo;
import oracle.retail.sim.common.rules.core.CaseSizeMustBePositiveRule;
import oracle.retail.sim.common.rules.core.CaseSizeValidForUomRule;
import oracle.retail.sim.common.rules.core.CurrencyMustBePositiveRule;
import oracle.retail.sim.common.rules.core.QuantityMustBePositiveRule;
import oracle.retail.sim.common.rules.core.QuantityMustConformToUOMRule;

public class StoreOrderLineItem extends BusinessObject {
  private static final long serialVersionUID = 8964098912085024595L;
  
  private OrderItem orderItem;
  
  private Quantity quantity;
  
  private Quantity caseSize;
  
  private String orderNumber;
  
  private String originCountryId;
  
  private Quantity originalQuantity;
  
  private SimMoney unitCost;
  
  private boolean isNew = true;
  
  private boolean dirty;
  
  public StoreOrderLineItem() {}
  
  public StoreOrderLineItem(String paramString) {
    setOrderNumber(paramString);
    doSetClean();
  }
  
  public StoreOrderLineItem(OrderItem paramOrderItem) {
    this.orderItem = paramOrderItem;
    this.caseSize = paramOrderItem.getDefaultCaseSize();
  }
  
  public StoreOrderLineItem(String paramString, OrderItem paramOrderItem) {
    this(paramOrderItem);
    setOrderNumber(paramString);
    doSetClean();
  }
  
  public OrderItem getOrderItem() {
    return this.orderItem;
  }
  
  public Quantity getCaseSize() {
    return this.caseSize;
  }
  
  public Quantity getQuantity() {
    return this.quantity;
  }
  
  public String getOrderNumber() {
    return this.orderNumber;
  }
  
  public String getOriginCountryId() {
    return this.originCountryId;
  }
  
  public Quantity getOrigQuantity() {
    return this.originalQuantity;
  }
  
  public SimMoney getUnitCost() {
    return this.unitCost;
  }
  
  private void setOrderNumber(String paramString) {
    this.orderNumber = paramString;
  }
  
  public void setCaseSize(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Case Size", paramQuantity);
    CaseSizeMustBePositiveRule.execute(paramQuantity);
    CaseSizeValidForUomRule.execute(this, paramQuantity);
    executeRule("setCaseSize", new Object[] { paramQuantity });
    doSetCaseSize(paramQuantity);
    doSetDirty();
  }
  
  public void setQuantity(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("setQuantity", paramQuantity);
    QuantityMustBePositiveRule.execute(paramQuantity);
    QuantityMustConformToUOMRule.execute(this, this.quantity);
    executeRule("setQuantity", new Object[] { paramQuantity });
    if (!paramQuantity.equals(this.quantity)) {
      doSetQuantity(paramQuantity);
      doSetDirty();
    } 
  }
  
  public void setOriginCountryId(String paramString) {
    this.originCountryId = paramString;
    doSetDirty();
  }
  
  public void setOrigQuantity(Quantity paramQuantity) {
    this.originalQuantity = paramQuantity;
  }
  
  public void setUnitCost(SimMoney paramSimMoney) throws BusinessException {
    RulesInfo rulesInfo = CurrencyMustBePositiveRule.execute(paramSimMoney);
    if (!rulesInfo.isError()) {
      executeRule("setUnitCost", new Object[] { paramSimMoney });
      this.unitCost = paramSimMoney;
      doSetDirty();
    } else {
      throw new BusinessException(rulesInfo.getMessage());
    } 
  }
  
  public void doSetQuantity(Quantity paramQuantity) {
    this.quantity = paramQuantity;
  }
  
  public void doSetCaseSize(Quantity paramQuantity) {
    this.caseSize = paramQuantity;
  }
  
  public void doSetDirty() {
    this.dirty = true;
  }
  
  public void doSetClean() {
    this.dirty = false;
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetPersisted() {
    this.isNew = false;
  }
  
  public boolean isNew() {
    return this.isNew;
  }
  
  public boolean isPropertyModifiable(String paramString) {
    if (this.orderItem == null)
      return false; 
    if ("unitOfMeasureMode".equals(paramString)) {
      Quantity quantity = this.orderItem.getDefaultCaseSize();
      if (quantity == null || quantity.doubleValue() <= 0.0D)
        return false; 
    } 
    if ("caseSize".equals(paramString) && !this.orderItem.isSellable())
      return false; 
    try {
      executeRule("isPropertyModifiable", new Object[] { paramString, this.orderItem });
    } catch (BusinessException businessException) {
      return false;
    } 
    return true;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storeorder\StoreOrderLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */