package oracle.retail.sim.common.lineitem;

import java.util.Collections;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.uin.SerialNumberValue;

public abstract class SerialNumberLineItemWrapper extends StockLineItemWrapper {
  public boolean isSerialNumberRequired() {
    StockItem stockItem = getStockItem();
    return (stockItem != null && stockItem.isSerialNumberRequired());
  }
  
  public void setQuantitiesBasedOnSerialNumbers() throws BusinessException {}
  
  public void addSerialNumber(SerialNumberValue paramSerialNumberValue) throws BusinessException {}
  
  public void removeSerialNumber(SerialNumberValue paramSerialNumberValue) throws BusinessException {}
  
  public List<SerialNumberValue> getSerialNumbers() {
    return Collections.emptyList();
  }
  
  public List<SerialNumberValue> getRemovedSerialNumbers() {
    return Collections.emptyList();
  }
  
  public Integer getSerialNumberCount() {
    return Integer.valueOf(0);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\lineitem\SerialNumberLineItemWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */