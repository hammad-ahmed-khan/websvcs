package oracle.retail.sim.common.fulfillmentorderpick;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.item.StockLineItem;
import oracle.retail.sim.common.rules.core.CaseSizeMustBePositiveRule;
import oracle.retail.sim.common.rules.core.CaseSizeValidForUomRule;
import oracle.retail.sim.common.rules.core.QuantityCannotBeNegativeRule;

public class FulfillmentOrderPickLineItem extends BusinessObject implements StockLineItem {
  private static final long serialVersionUID = 9163427760562266069L;
  
  private Long id;
  
  private StockItem stockItem;
  
  private Long pickId;
  
  private Long fulfillmentOrderId;
  
  private Long fulfillmentOrderLineItemId;
  
  private Long substituteLineItemId;
  
  private String preferredUom;
  
  private Quantity suggestedQuantity;
  
  private Quantity quantity;
  
  private Quantity caseSize;
  
  private FulfillmentOrderBin bin;
  
  private Integer pickOrder;
  
  private boolean dirty;
  
  public FulfillmentOrderPickLineItem(StockItem paramStockItem) {
    this.stockItem = paramStockItem;
    this.caseSize = paramStockItem.getDefaultCaseSize();
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public StockItem getStockItem() {
    return this.stockItem;
  }
  
  public Long getPickId() {
    return this.pickId;
  }
  
  public void doSetPickId(Long paramLong) {
    this.pickId = paramLong;
  }
  
  public Long getFulfillmentOrderId() {
    return this.fulfillmentOrderId;
  }
  
  public void doSetFulfillmentOrderId(Long paramLong) {
    this.fulfillmentOrderId = paramLong;
  }
  
  public Long getFulfillmentOrderLineItemId() {
    return this.fulfillmentOrderLineItemId;
  }
  
  public void doSetFulfillmentOrderLineItemId(Long paramLong) {
    this.fulfillmentOrderLineItemId = paramLong;
  }
  
  public Long getSubstituteLineItemId() {
    return this.substituteLineItemId;
  }
  
  public void doSetSubstituteLineItemId(Long paramLong) {
    this.substituteLineItemId = paramLong;
  }
  
  public boolean isSubstitute() {
    return (this.substituteLineItemId != null);
  }
  
  public String getPreferredUom() {
    return this.preferredUom;
  }
  
  public void doSetPreferredUom(String paramString) {
    this.preferredUom = paramString;
  }
  
  public FulfillmentOrderBin getBin() {
    return this.bin;
  }
  
  public void setBin(FulfillmentOrderBin paramFulfillmentOrderBin) throws BusinessException {
    checkForNullParameter("Bin", paramFulfillmentOrderBin);
    executeRule("setBin", new Object[0]);
    doSetBin(paramFulfillmentOrderBin);
  }
  
  public void doSetBin(FulfillmentOrderBin paramFulfillmentOrderBin) {
    this.bin = paramFulfillmentOrderBin;
  }
  
  public Integer getPickOrder() {
    return this.pickOrder;
  }
  
  public void doSetPickOrder(Integer paramInteger) {
    this.pickOrder = paramInteger;
  }
  
  public Quantity getSuggestedQuantity() {
    return this.suggestedQuantity;
  }
  
  public void doSetSuggestedQuantity(Quantity paramQuantity) {
    this.suggestedQuantity = paramQuantity;
  }
  
  public Quantity getQuantity() {
    return this.quantity;
  }
  
  public Quantity getQuantityOrZero() {
    return (this.quantity == null) ? Quantity.ZERO : this.quantity;
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
  
  public void doSetQuantity(Quantity paramQuantity) {
    this.quantity = paramQuantity;
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
  
  public boolean isDirty() {
    return this.dirty;
  }
  
  public void doSetClean() {
    this.dirty = false;
  }
  
  public void doSetDirty() {
    this.dirty = true;
  }
  
  public boolean isCoherent() throws BusinessException {
    if (this.quantity != null && this.quantity.isNegative())
      throw new BusinessException(CommonMessageText.QUANTITY_INVALID_NEGATIVE); 
    return super.isCoherent();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPickLineItem.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */