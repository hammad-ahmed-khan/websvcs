package oracle.retail.sim.common.fulfillmentorderpick;

import java.util.Comparator;

public class FulfillmentOrderPickItemOrderComparator implements Comparator<FulfillmentOrderPickLineItem> {
  public int compare(FulfillmentOrderPickLineItem paramFulfillmentOrderPickLineItem1, FulfillmentOrderPickLineItem paramFulfillmentOrderPickLineItem2) {
    return (paramFulfillmentOrderPickLineItem1.getPickOrder() != null && paramFulfillmentOrderPickLineItem2.getPickOrder() != null) ? paramFulfillmentOrderPickLineItem1.getPickOrder().compareTo(paramFulfillmentOrderPickLineItem2.getPickOrder()) : ((paramFulfillmentOrderPickLineItem1.getPickOrder() != null && paramFulfillmentOrderPickLineItem2.getPickOrder() == null) ? -1 : ((paramFulfillmentOrderPickLineItem1.getPickOrder() == null && paramFulfillmentOrderPickLineItem2.getPickOrder() != null) ? 1 : paramFulfillmentOrderPickLineItem1.getStockItem().getId().compareTo(paramFulfillmentOrderPickLineItem2.getStockItem().getId())));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPickItemOrderComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */