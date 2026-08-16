package oracle.retail.sim.service.fulfillmentorder;

import java.util.Date;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderContactInfo;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderCreateVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtListVO;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderMgmtQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderQueryFilter;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderVO;
import oracle.retail.sim.common.fulfillmentorder.ItemFulfillmentOrderVO;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPossiblePickVO;

public abstract class FulfillmentOrderServices {
  public abstract List<FulfillmentOrderCreateVO> createFulfillmentOrders(List<FulfillmentOrderCreateVO> paramList) throws Exception;
  
  public abstract FulfillmentOrder readFulfillmentOrder(Long paramLong) throws Exception;
  
  public abstract FulfillmentOrder readFulfillmentOrderByDeliveryId(Long paramLong) throws Exception;
  
  public abstract FulfillmentOrder readFulfillmentOrderByReversePickId(Long paramLong) throws Exception;
  
  public abstract Map<Long, FulfillmentOrder> readFulfillmentOrders(List<Long> paramList) throws Exception;
  
  public abstract List<FulfillmentOrderPossiblePickVO> readFulfillmentOrdersForPicking(Long paramLong) throws Exception;
  
  public abstract Date readFulfillmentOrderTimestamp(Long paramLong) throws Exception;
  
  public abstract Long updateFulfillmentOrder(FulfillmentOrder paramFulfillmentOrder) throws Exception;
  
  public abstract void markFulfillmentOrderInProgress(Long paramLong) throws Exception;
  
  public abstract List<FulfillmentOrderVO> findFulfillmentOrderVOs(FulfillmentOrderQueryFilter paramFulfillmentOrderQueryFilter) throws Exception;
  
  public abstract List<FulfillmentOrderMgmtListVO> findFulfillmentOrderMgmtListVOs(FulfillmentOrderMgmtQueryFilter paramFulfillmentOrderMgmtQueryFilter) throws Exception;
  
  public abstract List<ItemFulfillmentOrderVO> findItemFulfillmentOrderVOs(String paramString, Long paramLong) throws Exception;
  
  public abstract FulfillmentOrderContactInfo readFulfillmentOrderContactInfo(Long paramLong) throws Exception;
  
  public abstract List<FulfillmentOrder> findFulfillmentOrdersForWarehouseDelivery(Long paramLong) throws Exception;
  
  public abstract List<FulfillmentOrderVO> findFulfillmentOrderVOsForWarehouseDelivery(Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\fulfillmentorder\FulfillmentOrderServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */