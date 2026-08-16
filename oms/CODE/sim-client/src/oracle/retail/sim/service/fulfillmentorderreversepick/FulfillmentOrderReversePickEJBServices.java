package oracle.retail.sim.service.fulfillmentorderreversepick;

import java.util.List;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePick;
import oracle.retail.sim.common.fulfillmentorderreversepick.FulfillmentOrderReversePickVO;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;
import oracle.retail.sim.service.ejb.FulfillmentOrderReversePickInterface;

public class FulfillmentOrderReversePickEJBServices extends FulfillmentOrderReversePickServices {
  private FulfillmentOrderReversePickInterface lookup() throws Exception {
    try {
      return (FulfillmentOrderReversePickInterface)JndiServiceManager.cachedLookup("FulfillmentOrderReversePickBean", FulfillmentOrderReversePickInterface.class);
    } catch (Throwable throwable) {
      throw new DowntimeException("An error occurred accessing FulfillmentOrderReversePickServices. Please contact your system administrator.", throwable);
    } 
  }
  
  private void removeCache() {
    JndiServiceManager.removeCache("FulfillmentOrderReversePickBean");
  }
  
  public void cancelFulfillmentOrderReversePick(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.cancelFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderReversePickInterface fulfillmentOrderReversePickInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderReversePickInterface.cancelFulfillmentOrderReversePick(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderReversePickBean.cancelFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.cancelFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return;
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing FulfillmentOrderReversePickServices. Please contact your system administrator.", throwable);
  }
  
  public void confirmFulfillmentOrderReversePick(FulfillmentOrderReversePick paramFulfillmentOrderReversePick) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramFulfillmentOrderReversePick);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.confirmFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderReversePickInterface fulfillmentOrderReversePickInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderReversePickInterface.confirmFulfillmentOrderReversePick(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderReversePickBean.confirmFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.confirmFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return;
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing FulfillmentOrderReversePickServices. Please contact your system administrator.", throwable);
  }
  
  public List<FulfillmentOrderReversePickVO> findFulfillmentOrderReversePickVOs(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.findFulfillmentOrderReversePickVOs(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderReversePickInterface fulfillmentOrderReversePickInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderReversePickInterface.findFulfillmentOrderReversePickVOs(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderReversePickBean.findFulfillmentOrderReversePickVOs(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.findFulfillmentOrderReversePickVOs(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<FulfillmentOrderReversePickVO>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing FulfillmentOrderReversePickServices. Please contact your system administrator.", throwable);
  }
  
  public FulfillmentOrderReversePick readFulfillmentOrderReversePick(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.readFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderReversePickInterface fulfillmentOrderReversePickInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderReversePickInterface.readFulfillmentOrderReversePick(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderReversePickBean.readFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.readFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (FulfillmentOrderReversePick)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing FulfillmentOrderReversePickServices. Please contact your system administrator.", throwable);
  }
  
  public Long updateFulfillmentOrderReversePick(FulfillmentOrderReversePick paramFulfillmentOrderReversePick) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramFulfillmentOrderReversePick);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.updateFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderReversePickInterface fulfillmentOrderReversePickInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderReversePickInterface.updateFulfillmentOrderReversePick(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderReversePickBean.updateFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderReversePickBean.updateFulfillmentOrderReversePick(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (Long)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing FulfillmentOrderReversePickServices. Please contact your system administrator.", throwable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\fulfillmentorderreversepick\FulfillmentOrderReversePickEJBServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */