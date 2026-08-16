package oracle.retail.sim.common.directdelivery;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.item.SupplierItem;
import oracle.retail.sim.common.rules.RulesInfo;
import oracle.retail.sim.common.rules.core.CurrencyMustBePositiveRule;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class PurchaseOrderLineItem extends BusinessObject {
  private static final long serialVersionUID = 8575317520160803317L;
  
  private boolean dirty;
  
  private Long id;
  
  private SupplierItem supplierItem;
  
  private Quantity caseSize;
  
  private SimMoney unitCost;
  
  private String preferredUom;
  
  private Quantity quantityExpected = Quantity.ZERO;
  
  private Quantity quantityReceived = Quantity.ZERO;
  
  public PurchaseOrderLineItem(SupplierItem paramSupplierItem) {
    this.supplierItem = paramSupplierItem;
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
  
  public SupplierItem getSupplierItem() {
    return this.supplierItem;
  }
  
  public Quantity getCaseSize() {
    return this.caseSize;
  }
  
  public void doSetCaseSize(Quantity paramQuantity) {
    this.caseSize = paramQuantity;
  }
  
  public void setCaseSize(Quantity paramQuantity) throws BusinessException {
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
    checkForNullParameter("Unit Cost", paramSimMoney);
    RulesInfo rulesInfo = CurrencyMustBePositiveRule.execute(paramSimMoney);
    if (!rulesInfo.isError()) {
      executeRule("setUnitCost", new Object[] { paramSimMoney });
      doSetUnitCost(paramSimMoney);
      doSetDirty(true);
    } else {
      throw new BusinessException(rulesInfo.getMessage());
    } 
  }
  
  public String getPreferredUom() {
    return this.preferredUom;
  }
  
  public void doSetPreferredUom(String paramString) {
    this.preferredUom = paramString;
  }
  
  public void setPreferredUom(String paramString) throws BusinessException {
    executeRule("setPreferredUom", new Object[] { paramString });
    doSetPreferredUom(paramString);
    doSetDirty(true);
  }
  
  public boolean isExpected() {
    return this.quantityExpected.isPositive();
  }
  
  public Quantity getQuantityExpected() {
    return this.quantityExpected;
  }
  
  public void doSetQuantityExpected(Quantity paramQuantity) {
    this.quantityExpected = paramQuantity;
  }
  
  public void setQuantityExpected(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Quantity Expected", paramQuantity);
    executeRule("setQuantityExpected", new Object[] { paramQuantity });
    doSetQuantityExpected(paramQuantity);
    doSetDirty(true);
  }
  
  public Quantity getQuantityReceived() {
    return this.quantityReceived;
  }
  
  public void doSetQuantityReceived(Quantity paramQuantity) {
    this.quantityReceived = paramQuantity;
  }
  
  public void setQuantityReceived(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Quantity Received", paramQuantity);
    executeRule("setQuantityReceived", new Object[] { paramQuantity });
    doSetQuantityReceived(paramQuantity);
    doSetDirty(true);
  }
  
  public Quantity getQuantityOrdered() {
    return this.quantityExpected.subtract(this.quantityReceived).max(Quantity.ZERO);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    PurchaseOrderLineItem purchaseOrderLineItem = (PurchaseOrderLineItem)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.supplierItem.getItemId(), purchaseOrderLineItem.supplierItem.getItemId());
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.supplierItem.getItemId());
    return hashCodeBuilder.hashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\PurchaseOrderLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */