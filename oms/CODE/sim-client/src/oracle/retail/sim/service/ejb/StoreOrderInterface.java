package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.storeorder.ItemSale;
import oracle.retail.sim.common.storeorder.StoreOrder;
import oracle.retail.sim.common.storeorder.StoreOrderQueryFilter;

@Remote
public interface StoreOrderInterface {
  CompressedObject<?> create(CompressedObject<StoreOrder> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> createTempRecordsForPrint(CompressedObject<StoreOrder> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> delete(CompressedObject<StoreOrder> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ItemSale>> findItemSales(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<StoreOrder>> findStoreOrders(CompressedObject<StoreOrderQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<String> pingExternalService(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> update(CompressedObject<StoreOrder> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<StoreOrder> updateStoreOrderLineItems(CompressedObject<StoreOrder> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\StoreOrderInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */