package oracle.retail.sim.service.fulfillmentorderpick;

import java.util.List;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderBin;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickQueryFilter;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickVO;

public abstract class FulfillmentOrderPickServices {
  public abstract List<FulfillmentOrderPickVO> findFulfillmentOrderPickVOs(FulfillmentOrderPickQueryFilter paramFulfillmentOrderPickQueryFilter) throws Exception;
  
  public abstract FulfillmentOrderPick readFulfillmentOrderPick(Long paramLong) throws Exception;
  
  public abstract List<String> findFulfillmentOrderPickUsernames(Long paramLong) throws Exception;
  
  public abstract FulfillmentOrderPick createFulfillmentOrderPickForFulfillmentOrder(Long paramLong) throws Exception;
  
  public abstract FulfillmentOrderPick createFulfillmentOrderPickByBins(Integer paramInteger, Long paramLong) throws Exception;
  
  public abstract void cancelFulfillmentOrderPick(Long paramLong) throws Exception;
  
  public abstract void confirmFulfillmentOrderPick(FulfillmentOrderPick paramFulfillmentOrderPick) throws Exception;
  
  public abstract Long updateFulfillmentOrderPick(FulfillmentOrderPick paramFulfillmentOrderPick) throws Exception;
  
  public abstract FulfillmentOrderBin readFulfillmentOrderBin(String paramString) throws Exception;
  
  public abstract List<FulfillmentOrderBin> updateFulfillmentOrderBins(List<FulfillmentOrderBin> paramList) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\fulfillmentorderpick\FulfillmentOrderPickServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */