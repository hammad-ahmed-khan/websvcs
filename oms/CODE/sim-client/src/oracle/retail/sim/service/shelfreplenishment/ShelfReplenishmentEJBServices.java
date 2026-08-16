package oracle.retail.sim.service.shelfreplenishment;

import java.util.List;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishment;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentQueryFilter;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentVO;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;
import oracle.retail.sim.service.ejb.ShelfReplenishmentInterface;

public class ShelfReplenishmentEJBServices extends ShelfReplenishmentServices {
  private ShelfReplenishmentInterface lookup() throws Exception {
    try {
      return (ShelfReplenishmentInterface)JndiServiceManager.cachedLookup("ShelfReplenishmentBean", ShelfReplenishmentInterface.class);
    } catch (Throwable throwable) {
      throw new DowntimeException("An error occurred accessing ShelfReplenishmentServices. Please contact your system administrator.", throwable);
    } 
  }
  
  private void removeCache() {
    JndiServiceManager.removeCache("ShelfReplenishmentBean");
  }
  
  public void cancelShelfReplenishment(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.cancelShelfReplenishment(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ShelfReplenishmentInterface shelfReplenishmentInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = shelfReplenishmentInterface.cancelShelfReplenishment(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ShelfReplenishmentBean.cancelShelfReplenishment(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.cancelShelfReplenishment(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing ShelfReplenishmentServices. Please contact your system administrator.", throwable);
  }
  
  public ShelfReplenishment createShelfReplenishment(ShelfReplenishment paramShelfReplenishment) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramShelfReplenishment);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.createShelfReplenishment(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ShelfReplenishmentInterface shelfReplenishmentInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = shelfReplenishmentInterface.createShelfReplenishment(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ShelfReplenishmentBean.createShelfReplenishment(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.createShelfReplenishment(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (ShelfReplenishment)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ShelfReplenishmentServices. Please contact your system administrator.", throwable);
  }
  
  public List<String> findEmployeesIds(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.findEmployeesIds(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ShelfReplenishmentInterface shelfReplenishmentInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = shelfReplenishmentInterface.findEmployeesIds(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ShelfReplenishmentBean.findEmployeesIds(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.findEmployeesIds(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<String>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ShelfReplenishmentServices. Please contact your system administrator.", throwable);
  }
  
  public List<ShelfReplenishmentVO> findShelfReplenishmentVOs(ShelfReplenishmentQueryFilter paramShelfReplenishmentQueryFilter) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramShelfReplenishmentQueryFilter);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.findShelfReplenishmentVOs(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ShelfReplenishmentInterface shelfReplenishmentInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = shelfReplenishmentInterface.findShelfReplenishmentVOs(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ShelfReplenishmentBean.findShelfReplenishmentVOs(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.findShelfReplenishmentVOs(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<ShelfReplenishmentVO>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ShelfReplenishmentServices. Please contact your system administrator.", throwable);
  }
  
  public List<ShelfReplenishment> findShelfReplenishments(ShelfReplenishmentQueryFilter paramShelfReplenishmentQueryFilter) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramShelfReplenishmentQueryFilter);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.findShelfReplenishments(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ShelfReplenishmentInterface shelfReplenishmentInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = shelfReplenishmentInterface.findShelfReplenishments(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ShelfReplenishmentBean.findShelfReplenishments(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.findShelfReplenishments(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<ShelfReplenishment>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ShelfReplenishmentServices. Please contact your system administrator.", throwable);
  }
  
  public ShelfReplenishment readShelfReplenishment(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.readShelfReplenishment(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ShelfReplenishmentInterface shelfReplenishmentInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = shelfReplenishmentInterface.readShelfReplenishment(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ShelfReplenishmentBean.readShelfReplenishment(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.readShelfReplenishment(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (ShelfReplenishment)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ShelfReplenishmentServices. Please contact your system administrator.", throwable);
  }
  
  public ShelfReplenishment updateShelfReplenishment(ShelfReplenishment paramShelfReplenishment) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramShelfReplenishment);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.updateShelfReplenishment(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ShelfReplenishmentInterface shelfReplenishmentInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = shelfReplenishmentInterface.updateShelfReplenishment(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ShelfReplenishmentBean.updateShelfReplenishment(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ShelfReplenishmentBean.updateShelfReplenishment(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (ShelfReplenishment)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ShelfReplenishmentServices. Please contact your system administrator.", throwable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\shelfreplenishment\ShelfReplenishmentEJBServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */