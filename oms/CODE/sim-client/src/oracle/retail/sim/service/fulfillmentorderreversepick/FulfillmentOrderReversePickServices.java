package oracle.retail.sim.service.fulfillmentorderreversepick;

import java.util.List;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickVO;

public abstract class FulfillmentOrderReversePickServices {
  public abstract List<FulfillmentOrderReversePickVO> findFulfillmentOrderReversePickVOs(Long paramLong) throws Exception;
  
  public abstract FulfillmentOrderReversePick readFulfillmentOrderReversePick(Long paramLong) throws Exception;
  
  public abstract void cancelFulfillmentOrderReversePick(Long paramLong) throws Exception;
  
  public abstract void confirmFulfillmentOrderReversePick(FulfillmentOrderReversePick paramFulfillmentOrderReversePick) throws Exception;
  
  public abstract Long updateFulfillmentOrderReversePick(FulfillmentOrderReversePick paramFulfillmentOrderReversePick) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\fulfillmentorderreversepick\FulfillmentOrderReversePickServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */