package oracle.retail.sim.common.lineitem;

import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.StockItem;

public abstract class StockLineItemWrapper extends UnitOfMeasureWrapper {
  public abstract StockItem getStockItem();
  
  public String getStockItemDescription() {
    return getStockItemDescription(SimConfigManager.isItemShortDescription());
  }
  
  public String getStockItemDescription(boolean paramBoolean) {
    StockItem stockItem = getStockItem();
    return (stockItem == null) ? null : (paramBoolean ? stockItem.getShortDescription() : stockItem.getLongDescription());
  }
  
  public String getStandardUnitOfMeasure() {
    StockItem stockItem = getStockItem();
    return (stockItem == null) ? null : stockItem.getUnitOfMeasure();
  }
  
  public boolean isEstimatedStockOnHandQuantities() {
    StockItem stockItem = getStockItem();
    return (stockItem != null && stockItem.isInventoryAtComponentLevel());
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\lineitem\StockLineItemWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */