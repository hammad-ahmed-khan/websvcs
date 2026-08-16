package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderContactInfo;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderCreateVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtListVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;
import oracle.retail.sim.common.fulfillmentorder.ItemFulfillmentOrderVO;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPossiblePickVO;

@Remote
public interface FulfillmentOrderInterface {
  CompressedObject<List<FulfillmentOrderCreateVO>> createFulfillmentOrders(CompressedObject<List<FulfillmentOrderCreateVO>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FulfillmentOrderMgmtListVO>> findFulfillmentOrderMgmtListVOs(CompressedObject<FulfillmentOrderMgmtQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FulfillmentOrderVO>> findFulfillmentOrderVOs(CompressedObject<FulfillmentOrderQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FulfillmentOrderVO>> findFulfillmentOrderVOsForWarehouseDelivery(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FulfillmentOrder>> findFulfillmentOrdersForWarehouseDelivery(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ItemFulfillmentOrderVO>> findItemFulfillmentOrderVOs(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> markFulfillmentOrderInProgress(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FulfillmentOrder> readFulfillmentOrder(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FulfillmentOrder> readFulfillmentOrderByDeliveryId(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FulfillmentOrder> readFulfillmentOrderByReversePickId(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FulfillmentOrderContactInfo> readFulfillmentOrderContactInfo(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Date> readFulfillmentOrderTimestamp(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Map<Long, FulfillmentOrder>> readFulfillmentOrders(CompressedObject<List<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FulfillmentOrderPossiblePickVO>> readFulfillmentOrdersForPicking(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> updateFulfillmentOrder(CompressedObject<FulfillmentOrder> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\FulfillmentOrderInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */