package oracle.retail.sim.service.warehousedelivery;

import java.util.List;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQuickVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryVO;
import oracle.retail.sim.service.ejb.WarehouseDeliveryInterface;

public class WarehouseDeliveryEJBServices extends WarehouseDeliveryServices {
  private WarehouseDeliveryInterface lookup() throws Exception {
    try {
      return (WarehouseDeliveryInterface)JndiServiceManager.cachedLookup("WarehouseDeliveryBean", WarehouseDeliveryInterface.class);
    } catch (Throwable throwable) {
      throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
    } 
  }
  
  private void removeCache() {
    JndiServiceManager.removeCache("WarehouseDeliveryBean");
  }
  
  public List<WarehouseDeliveryCartonVO> findWarehouseDeliveryCartonVOs(WarehouseDeliveryCartonQueryFilter paramWarehouseDeliveryCartonQueryFilter) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramWarehouseDeliveryCartonQueryFilter);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.findWarehouseDeliveryCartonVOs(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.findWarehouseDeliveryCartonVOs(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.findWarehouseDeliveryCartonVOs(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.findWarehouseDeliveryCartonVOs(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<WarehouseDeliveryCartonVO>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public List<WarehouseDeliveryVO> findWarehouseDeliveryVOs(WarehouseDeliveryQueryFilter paramWarehouseDeliveryQueryFilter) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramWarehouseDeliveryQueryFilter);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.findWarehouseDeliveryVOs(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.findWarehouseDeliveryVOs(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.findWarehouseDeliveryVOs(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.findWarehouseDeliveryVOs(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<WarehouseDeliveryVO>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public WarehouseDelivery readWarehouseDelivery(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDelivery(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.readWarehouseDelivery(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.readWarehouseDelivery(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDelivery(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (WarehouseDelivery)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public WarehouseDeliveryCarton readWarehouseDeliveryCarton(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDeliveryCarton(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.readWarehouseDeliveryCarton(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.readWarehouseDeliveryCarton(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDeliveryCarton(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (WarehouseDeliveryCarton)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public WarehouseDeliveryCartonVO readWarehouseDeliveryCartonVO(String paramString, boolean paramBoolean) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString);
    CompressedObject compressedObject3 = new CompressedObject(Boolean.valueOf(paramBoolean));
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDeliveryCartonVO(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.readWarehouseDeliveryCartonVO(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.readWarehouseDeliveryCartonVO(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDeliveryCartonVO(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (WarehouseDeliveryCartonVO)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public WarehouseDeliveryCartonVO readWarehouseDeliveryCartonVO(String paramString, Long paramLong, boolean paramBoolean) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString);
    CompressedObject compressedObject3 = new CompressedObject(paramLong);
    CompressedObject compressedObject4 = new CompressedObject(Boolean.valueOf(paramBoolean));
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDeliveryCartonVO2(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.readWarehouseDeliveryCartonVO2(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.readWarehouseDeliveryCartonVO2(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDeliveryCartonVO2(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (WarehouseDeliveryCartonVO)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public WarehouseDeliveryVO readWarehouseDeliveryVOForCarton(String paramString, boolean paramBoolean) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString);
    CompressedObject compressedObject3 = new CompressedObject(Boolean.valueOf(paramBoolean));
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDeliveryVOForCarton(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.readWarehouseDeliveryVOForCarton(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.readWarehouseDeliveryVOForCarton(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.readWarehouseDeliveryVOForCarton(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (WarehouseDeliveryVO)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public WarehouseDelivery receiveWarehouseDelivery(WarehouseDelivery paramWarehouseDelivery) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramWarehouseDelivery);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.receiveWarehouseDelivery(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.receiveWarehouseDelivery(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.receiveWarehouseDelivery(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.receiveWarehouseDelivery(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (WarehouseDelivery)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public WarehouseDeliveryQuickVO receiveWarehouseDeliveryCarton(WarehouseDeliveryCartonVO paramWarehouseDeliveryCartonVO) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramWarehouseDeliveryCartonVO);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.receiveWarehouseDeliveryCarton(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.receiveWarehouseDeliveryCarton(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.receiveWarehouseDeliveryCarton(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.receiveWarehouseDeliveryCarton(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (WarehouseDeliveryQuickVO)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public Long updateWarehouseDelivery(WarehouseDelivery paramWarehouseDelivery) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramWarehouseDelivery);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.updateWarehouseDelivery(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.updateWarehouseDelivery(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.updateWarehouseDelivery(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.updateWarehouseDelivery(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
  
  public WarehouseDeliveryCarton updateWarehouseDeliveryCarton(WarehouseDeliveryCarton paramWarehouseDeliveryCarton, Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramWarehouseDeliveryCarton);
    CompressedObject compressedObject3 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.updateWarehouseDeliveryCarton(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      WarehouseDeliveryInterface warehouseDeliveryInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = warehouseDeliveryInterface.updateWarehouseDeliveryCarton(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "WarehouseDeliveryBean.updateWarehouseDeliveryCarton(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "WarehouseDeliveryBean.updateWarehouseDeliveryCarton(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (WarehouseDeliveryCarton)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing WarehouseDeliveryServices. Please contact your system administrator.", throwable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\warehousedelivery\WarehouseDeliveryEJBServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */