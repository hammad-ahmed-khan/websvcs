package oracle.retail.sim.service.mdsehierarchy;

import java.util.List;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;
import oracle.retail.sim.service.ejb.MdseHierarchyInterface;

public class MdseHierarchyEJBServices extends MdseHierarchyServices {
  private MdseHierarchyInterface lookup() throws Exception {
    try {
      return (MdseHierarchyInterface)JndiServiceManager.cachedLookup("MdseHierarchyBean", MdseHierarchyInterface.class);
    } catch (Throwable throwable) {
      throw new DowntimeException("An error occurred accessing MdseHierarchyServices. Please contact your system administrator.", throwable);
    } 
  }
  
  private void removeCache() {
    JndiServiceManager.removeCache("MdseHierarchyBean");
  }
  
  public List<MdseHierarchyNode> findClasses(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "MdseHierarchyBean.findClasses(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      MdseHierarchyInterface mdseHierarchyInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = mdseHierarchyInterface.findClasses(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "MdseHierarchyBean.findClasses(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "MdseHierarchyBean.findClasses(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<MdseHierarchyNode>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing MdseHierarchyServices. Please contact your system administrator.", throwable);
  }
  
  public List<MdseHierarchyNode> findDepartments() throws Exception {
    CompressedObject compressedObject = new CompressedObject(UniversalContext.getSession());
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "MdseHierarchyBean.findDepartments(compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      MdseHierarchyInterface mdseHierarchyInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject1 = mdseHierarchyInterface.findDepartments(compressedObject);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "MdseHierarchyBean.findDepartments(compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "MdseHierarchyBean.findDepartments(compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject1) + " bytes in the returned serialized object."); 
        return (List<MdseHierarchyNode>)compressedObject1.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing MdseHierarchyServices. Please contact your system administrator.", throwable);
  }
  
  public List<MdseHierarchyNode> findSubclasses(Long paramLong1, Long paramLong2) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "MdseHierarchyBean.findSubclasses(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      MdseHierarchyInterface mdseHierarchyInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = mdseHierarchyInterface.findSubclasses(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "MdseHierarchyBean.findSubclasses(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "MdseHierarchyBean.findSubclasses(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<MdseHierarchyNode>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing MdseHierarchyServices. Please contact your system administrator.", throwable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\mdsehierarchy\MdseHierarchyEJBServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */