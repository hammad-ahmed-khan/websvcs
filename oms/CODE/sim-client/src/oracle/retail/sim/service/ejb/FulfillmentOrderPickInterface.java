package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderBin;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickQueryFilter;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickVO;

@Remote
public interface FulfillmentOrderPickInterface {
  CompressedObject<?> cancelFulfillmentOrderPick(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> confirmFulfillmentOrderPick(CompressedObject<FulfillmentOrderPick> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FulfillmentOrderPick> createFulfillmentOrderPickByBins(CompressedObject<Integer> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<FulfillmentOrderPick> createFulfillmentOrderPickForFulfillmentOrder(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<String>> findFulfillmentOrderPickUsernames(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FulfillmentOrderPickVO>> findFulfillmentOrderPickVOs(CompressedObject<FulfillmentOrderPickQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FulfillmentOrderBin> readFulfillmentOrderBin(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<FulfillmentOrderPick> readFulfillmentOrderPick(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FulfillmentOrderBin>> updateFulfillmentOrderBins(CompressedObject<List<FulfillmentOrderBin>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> updateFulfillmentOrderPick(CompressedObject<FulfillmentOrderPick> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\FulfillmentOrderPickInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */