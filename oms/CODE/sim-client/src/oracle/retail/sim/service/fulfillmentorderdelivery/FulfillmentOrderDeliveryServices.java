package oracle.retail.sim.service.fulfillmentorderdelivery;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryQueryFilter;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryVO;
import oracle.retail.sim.common.report.SessionPrinter;

public abstract class FulfillmentOrderDeliveryServices {
  public abstract List<FulfillmentOrderDeliveryVO> findFulfillmentOrderDeliveryVOs(Long paramLong) throws Exception;
  
  public abstract List<FulfillmentOrderDeliveryVO> findFulfillmentOrderDeliveryVOs(FulfillmentOrderDeliveryQueryFilter paramFulfillmentOrderDeliveryQueryFilter) throws Exception;
  
  public abstract FulfillmentOrderDelivery readFulfillmentOrderDelivery(Long paramLong) throws Exception;
  
  public abstract Long dispatchFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery) throws Exception;
  
  public abstract Long dispatchFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery, List<SessionPrinter> paramList) throws Exception;
  
  public abstract void dispatchFulfillmentOrderDeliveries(List<String> paramList, String paramString1, String paramString2, Date paramDate) throws Exception;
  
  public abstract Long updateFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery) throws Exception;
  
  public abstract void cancelFulfillmentOrderDelivery(Long paramLong) throws Exception;
  
  public abstract Long submitFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery) throws Exception;
  
  public abstract Long submitFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery, List<SessionPrinter> paramList) throws Exception;
  
  public abstract void cancelSubmitFulfillmentOrderDelivery(Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\fulfillmentorderdelivery\FulfillmentOrderDeliveryServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */