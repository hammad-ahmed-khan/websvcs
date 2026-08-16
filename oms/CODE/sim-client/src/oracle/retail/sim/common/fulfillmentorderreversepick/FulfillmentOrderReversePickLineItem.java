package oracle.retail.sim.common.fulfillmentorderreversepick;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.rules.core.CaseSizeMustBePositiveRule;
import oracle.retail.sim.common.rules.core.CaseSizeValidForUomRule;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;

public class FulfillmentOrderReversePickLineItem extends BusinessObject {
  private static final long serialVersionUID = 7287036459906590359L;
  
  private Long id;
  
  private Long fulfillmentOrderLineItemId;
  
  private Quantity suggestedQty;
  
  private Quantity quantity;
  
  private boolean dirty;
  
  private Quantity caseSize;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public Long getFulfillmentOrderLineItemId() {
    return this.fulfillmentOrderLineItemId;
  }
  
  public void doSetFulfillmentOrderLineItemId(Long paramLong) {
    this.fulfillmentOrderLineItemId = paramLong;
  }
  
  public Quantity getQuantity() {
    return this.quantity;
  }
  
  public Quantity getQuantityOrZero() {
    return (this.quantity != null) ? this.quantity : Quantity.ZERO;
  }
  
  public void doSetQuantity(Quantity paramQuantity) {
    this.quantity = paramQuantity;
  }
  
  public void setQuantity(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Quantity", paramQuantity);
    QuantityCannotBeNegativeRule.execute(paramQuantity);
    executeRule("setQuantity", new Object[] { paramQuantity });
    if (isAttributesNotEqual(paramQuantity, this.quantity)) {
      doSetQuantity(paramQuantity);
      doSetDirty();
    } 
  }
  
  public Quantity getCaseSize() {
    return this.caseSize;
  }
  
  public void setCaseSize(Quantity paramQuantity) throws BusinessException {
    checkForNullParameter("Case size", paramQuantity);
    CaseSizeMustBePositiveRule.execute(paramQuantity);
    CaseSizeValidForUomRule.execute(this, paramQuantity);
    executeRule("setCaseSize", new Object[] { paramQuantity });
    doSetCaseSize(paramQuantity);
    doSetDirty();
  }
  
  public void doSetCaseSize(Quantity paramQuantity) {
    this.caseSize = paramQuantity;
  }
  
  public Quantity getSuggestedQty() {
    return this.suggestedQty;
  }
  
  public void doSetSuggestedQty(Quantity paramQuantity) {
    this.suggestedQty = paramQuantity;
  }
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetDirty() {
    this.dirty = true;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (this.fulfillmentOrderLineItemId == null)
      throw new BusinessException(CommonMessageText.LINE_ITEM_NO_ITEM); 
    if (this.quantity == null || this.quantity.isNegative())
      throw new BusinessException(CommonMessageText.QUANTITY_INVALID_NEGATIVE); 
    return super.isCoherent();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderreversepick\FulfillmentOrderReversePickLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */