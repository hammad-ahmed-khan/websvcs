package oracle.retail.sim.common.fulfillmentorderpick;

import java.util.Comparator;
import oracle.retail.sim.common.item.StockItem;

public class FulfillmentOrderPickItemSortComparator implements Comparator<StockItem> {
  public int compare(StockItem paramStockItem1, StockItem paramStockItem2) {
    return (paramStockItem1.getStockInBackRoom().isPositive() && !paramStockItem2.getStockInBackRoom().isPositive()) ? -1 : ((!paramStockItem1.getStockInBackRoom().isPositive() && paramStockItem2.getStockInBackRoom().isPositive()) ? 1 : ((paramStockItem1.getStockInDeliveryBay().isPositive() && !paramStockItem2.getStockInDeliveryBay().isPositive()) ? -1 : ((!paramStockItem1.getStockInDeliveryBay().isPositive() && paramStockItem2.getStockInDeliveryBay().isPositive()) ? 1 : ((paramStockItem1.getItem().getDepartmentId().longValue() < paramStockItem2.getItem().getDepartmentId().longValue()) ? -1 : ((paramStockItem1.getItem().getDepartmentId().longValue() > paramStockItem2.getItem().getDepartmentId().longValue()) ? 1 : ((paramStockItem1.getItem().getClassId().longValue() < paramStockItem2.getItem().getClassId().longValue()) ? -1 : ((paramStockItem1.getItem().getClassId().longValue() > paramStockItem2.getItem().getClassId().longValue()) ? -1 : ((paramStockItem1.getItem().getSubclassId().longValue() < paramStockItem2.getItem().getSubclassId().longValue()) ? -1 : ((paramStockItem1.getItem().getSubclassId().longValue() > paramStockItem2.getItem().getSubclassId().longValue()) ? -1 : paramStockItem1.getItem().getId().compareTo(paramStockItem2.getItem().getId()))))))))));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPickItemSortComparator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */