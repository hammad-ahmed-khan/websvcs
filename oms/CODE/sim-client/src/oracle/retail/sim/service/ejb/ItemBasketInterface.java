package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.itembasket.ItemBasket;
import oracle.retail.sim.common.itembasket.ItemBasketType;

@Remote
public interface ItemBasketInterface {
  CompressedObject<?> deleteItemBasket(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ItemBasketType>> findItemBasketTypes(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Integer> findNumberOfItemBaskets(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Long> insertItemBasket(CompressedObject<ItemBasket> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ItemBasket> readItemBasket(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ItemBasket> readItemBasket2(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<Integer> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<?> updateItemBasket(CompressedObject<ItemBasket> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ItemBasketInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */