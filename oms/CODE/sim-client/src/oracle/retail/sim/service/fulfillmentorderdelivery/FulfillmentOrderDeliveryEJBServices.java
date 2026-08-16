package oracle.retail.sim.service.fulfillmentorderdelivery;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryQueryFilter;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryVO;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;
import oracle.retail.sim.service.ejb.FulfillmentOrderDeliveryInterface;

public class FulfillmentOrderDeliveryEJBServices extends FulfillmentOrderDeliveryServices {
  private FulfillmentOrderDeliveryInterface lookup() throws Exception {
    try {
      return (FulfillmentOrderDeliveryInterface)JndiServiceManager.cachedLookup("FulfillmentOrderDeliveryBean", FulfillmentOrderDeliveryInterface.class);
    } catch (Throwable throwable) {
      throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
    } 
  }
  
  private void removeCache() {
    JndiServiceManager.removeCache("FulfillmentOrderDeliveryBean");
  }
  
  public void cancelFulfillmentOrderDelivery(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.cancelFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.cancelFulfillmentOrderDelivery(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.cancelFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.cancelFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public void cancelSubmitFulfillmentOrderDelivery(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.cancelSubmitFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.cancelSubmitFulfillmentOrderDelivery(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.cancelSubmitFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.cancelSubmitFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public void dispatchFulfillmentOrderDeliveries(List<String> paramList, String paramString1, String paramString2, Date paramDate) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramList);
    CompressedObject compressedObject3 = new CompressedObject(paramString1);
    CompressedObject compressedObject4 = new CompressedObject(paramString2);
    CompressedObject compressedObject5 = new CompressedObject(paramDate);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.dispatchFulfillmentOrderDeliveries(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.dispatchFulfillmentOrderDeliveries(compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.dispatchFulfillmentOrderDeliveries(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.dispatchFulfillmentOrderDeliveries(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public Long dispatchFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramFulfillmentOrderDelivery);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.dispatchFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.dispatchFulfillmentOrderDelivery(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.dispatchFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.dispatchFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public Long dispatchFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery, List<SessionPrinter> paramList) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramFulfillmentOrderDelivery);
    CompressedObject compressedObject3 = new CompressedObject(paramList);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.dispatchFulfillmentOrderDelivery2(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.dispatchFulfillmentOrderDelivery2(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.dispatchFulfillmentOrderDelivery2(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.dispatchFulfillmentOrderDelivery2(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public List<FulfillmentOrderDeliveryVO> findFulfillmentOrderDeliveryVOs(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.findFulfillmentOrderDeliveryVOs(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.findFulfillmentOrderDeliveryVOs(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.findFulfillmentOrderDeliveryVOs(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.findFulfillmentOrderDeliveryVOs(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<FulfillmentOrderDeliveryVO>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public List<FulfillmentOrderDeliveryVO> findFulfillmentOrderDeliveryVOs(FulfillmentOrderDeliveryQueryFilter paramFulfillmentOrderDeliveryQueryFilter) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramFulfillmentOrderDeliveryQueryFilter);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.findFulfillmentOrderDeliveryVOs2(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.findFulfillmentOrderDeliveryVOs2(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.findFulfillmentOrderDeliveryVOs2(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.findFulfillmentOrderDeliveryVOs2(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<FulfillmentOrderDeliveryVO>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public FulfillmentOrderDelivery readFulfillmentOrderDelivery(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.readFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.readFulfillmentOrderDelivery(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.readFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.readFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (FulfillmentOrderDelivery)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public Long submitFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramFulfillmentOrderDelivery);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.submitFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.submitFulfillmentOrderDelivery(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.submitFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.submitFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public Long submitFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery, List<SessionPrinter> paramList) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramFulfillmentOrderDelivery);
    CompressedObject compressedObject3 = new CompressedObject(paramList);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.submitFulfillmentOrderDelivery2(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.submitFulfillmentOrderDelivery2(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.submitFulfillmentOrderDelivery2(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.submitFulfillmentOrderDelivery2(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public Long updateFulfillmentOrderDelivery(FulfillmentOrderDelivery paramFulfillmentOrderDelivery) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramFulfillmentOrderDelivery);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.updateFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      FulfillmentOrderDeliveryInterface fulfillmentOrderDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = fulfillmentOrderDeliveryInterface.updateFulfillmentOrderDelivery(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "FulfillmentOrderDeliveryBean.updateFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "FulfillmentOrderDeliveryBean.updateFulfillmentOrderDelivery(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing FulfillmentOrderDeliveryServices. Please contact your system administrator.", throwable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\fulfillmentorderdelivery\FulfillmentOrderDeliveryEJBServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */