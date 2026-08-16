package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickVO;

@Remote
public interface FulfillmentOrderReversePickInterface {
  CompressedObject<?> cancelFulfillmentOrderReversePick(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> confirmFulfillmentOrderReversePick(CompressedObject<FulfillmentOrderReversePick> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FulfillmentOrderReversePickVO>> findFulfillmentOrderReversePickVOs(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FulfillmentOrderReversePick> readFulfillmentOrderReversePick(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> updateFulfillmentOrderReversePick(CompressedObject<FulfillmentOrderReversePick> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\FulfillmentOrderReversePickInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */