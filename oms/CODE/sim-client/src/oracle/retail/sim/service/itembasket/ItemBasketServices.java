package oracle.retail.sim.service.itembasket;

import java.util.List;
import oracle.retail.sim.common.itembasket.ItemBasket;
import oracle.retail.sim.common.itembasket.ItemBasketType;

public abstract class ItemBasketServices {
  public abstract ItemBasket readItemBasket(Long paramLong) throws Exception;
  
  public abstract ItemBasket readItemBasket(String paramString, Long paramLong, Integer paramInteger) throws Exception;
  
  public abstract Long insertItemBasket(ItemBasket paramItemBasket) throws Exception;
  
  public abstract void updateItemBasket(ItemBasket paramItemBasket) throws Exception;
  
  public abstract void deleteItemBasket(Long paramLong) throws Exception;
  
  public abstract int findNumberOfItemBaskets(String paramString, Long paramLong) throws Exception;
  
  public abstract List<ItemBasketType> findItemBasketTypes() throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\itembasket\ItemBasketServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */