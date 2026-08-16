package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryQueryFilter;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryVO;
import oracle.retail.sim.common.report.SessionPrinter;

@Remote
public interface FulfillmentOrderDeliveryInterface {
  CompressedObject<?> cancelFulfillmentOrderDelivery(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> cancelSubmitFulfillmentOrderDelivery(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> dispatchFulfillmentOrderDeliveries(CompressedObject<List<String>> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<Date> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<Long> dispatchFulfillmentOrderDelivery(CompressedObject<FulfillmentOrderDelivery> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> dispatchFulfillmentOrderDelivery2(CompressedObject<FulfillmentOrderDelivery> paramCompressedObject, CompressedObject<List<SessionPrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<FulfillmentOrderDeliveryVO>> findFulfillmentOrderDeliveryVOs(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FulfillmentOrderDeliveryVO>> findFulfillmentOrderDeliveryVOs2(CompressedObject<FulfillmentOrderDeliveryQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FulfillmentOrderDelivery> readFulfillmentOrderDelivery(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> submitFulfillmentOrderDelivery(CompressedObject<FulfillmentOrderDelivery> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> submitFulfillmentOrderDelivery2(CompressedObject<FulfillmentOrderDelivery> paramCompressedObject, CompressedObject<List<SessionPrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Long> updateFulfillmentOrderDelivery(CompressedObject<FulfillmentOrderDelivery> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\FulfillmentOrderDeliveryInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */