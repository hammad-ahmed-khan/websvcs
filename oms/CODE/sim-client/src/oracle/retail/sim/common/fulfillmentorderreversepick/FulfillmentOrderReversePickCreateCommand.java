package oracle.retail.sim.common.fulfillmentorderreversepick;

import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.Command;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMessageText;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;

public class FulfillmentOrderReversePickCreateCommand extends Command {
  private FulfillmentOrder fulfillmentOrder = null;
  
  private FulfillmentOrderReversePick reversePick = null;
  
  public void setFulfillmentOrder(FulfillmentOrder paramFulfillmentOrder) {
    this.fulfillmentOrder = paramFulfillmentOrder;
  }
  
  public FulfillmentOrderReversePick getReversePick() {
    return this.reversePick;
  }
  
  protected void doExecute() throws Exception {
    if (this.fulfillmentOrder == null)
      throw new IllegalArgumentException("Fulfillment Order cannot be null."); 
    if (this.fulfillmentOrder.getStatus() == FulfillmentOrderStatus.CANCELED || this.fulfillmentOrder.getStatus() == FulfillmentOrderStatus.COMPLETED)
      throw new BusinessException(FulfillmentOrderMessageText.INVALID_CUSTOMER_ORDER_STATUS_DELIVERY_CREATE); 
    this.reversePick = BOFactory.createFulfillmentOrderReversePick();
    this.reversePick.doSetFulfillmentOrderId(this.fulfillmentOrder.getId());
    this.reversePick.doSetStoreId(this.fulfillmentOrder.getStoreId());
    this.reversePick.doSetCreateDate(SimDateUtil.getCurrentDate());
    this.reversePick.doSetCreateUser(UniversalContext.getUserName());
    FulfillmentOrderReversePickLineItem fulfillmentOrderReversePickLineItem = null;
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem : this.fulfillmentOrder.getLineItems()) {
      fulfillmentOrderReversePickLineItem = BOFactory.createFulfillmentOrderReversePickLineItem();
      fulfillmentOrderReversePickLineItem.doSetFulfillmentOrderLineItemId(fulfillmentOrderLineItem.getId());
      fulfillmentOrderReversePickLineItem.doSetCaseSize(fulfillmentOrderLineItem.getStockItem().getDefaultCaseSize());
      fulfillmentOrderReversePickLineItem.doSetSuggestedQty(new Quantity(0L));
      this.reversePick.doAddLineItem(fulfillmentOrderReversePickLineItem);
    } 
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderreversepick\FulfillmentOrderReversePickCreateCommand.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */