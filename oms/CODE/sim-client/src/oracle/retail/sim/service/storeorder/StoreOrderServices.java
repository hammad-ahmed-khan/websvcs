package oracle.retail.sim.service.storeorder;

import java.util.List;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import oracle.retail.sim.common.storeorder.ItemSale;
import oracle.retail.sim.common.storeorder.StoreOrder;
import oracle.retail.sim.common.storeorder.StoreOrderQueryFilter;

public abstract class StoreOrderServices {
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract List<StoreOrder> findStoreOrders(StoreOrderQueryFilter paramStoreOrderQueryFilter) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract StoreOrder updateStoreOrderLineItems(StoreOrder paramStoreOrder) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract void create(StoreOrder paramStoreOrder) throws Exception;
  
  public abstract void update(StoreOrder paramStoreOrder) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract void delete(StoreOrder paramStoreOrder) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract List<ItemSale> findItemSales(Long paramLong, String paramString) throws Exception;
  
  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public abstract String pingExternalService(String paramString) throws Exception;
  
  public abstract void createTempRecordsForPrint(StoreOrder paramStoreOrder, Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\storeorder\StoreOrderServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */