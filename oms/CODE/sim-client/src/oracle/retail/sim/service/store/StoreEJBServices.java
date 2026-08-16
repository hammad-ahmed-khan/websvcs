package oracle.retail.sim.service.store;

import java.util.Collection;
import java.util.List;
import java.util.TimeZone;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.store.BuddyStore;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.store.StoreAddressVO;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;
import oracle.retail.sim.service.ejb.StoreInterface;

public class StoreEJBServices extends StoreServices {
  private StoreInterface lookup() throws Exception {
    try {
      return (StoreInterface)JndiServiceManager.cachedLookup("StoreBean", StoreInterface.class);
    } catch (Throwable throwable) {
      throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
    } 
  }
  
  private void removeCache() {
    JndiServiceManager.removeCache("StoreBean");
  }
  
  public List<Store> findAllStores() throws Exception {
    CompressedObject compressedObject = new CompressedObject(UniversalContext.getSession());
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.findAllStores(compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject1 = storeInterface.findAllStores(compressedObject);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.findAllStores(compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.findAllStores(compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject1) + " bytes in the returned serialized object."); 
        return (List<Store>)compressedObject1.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public List<BuddyStore> findAutoReceiveStores(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.findAutoReceiveStores(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.findAutoReceiveStores(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.findAutoReceiveStores(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.findAutoReceiveStores(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<BuddyStore>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public List<BuddyStore> findBuddyStores(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.findBuddyStores(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.findBuddyStores(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.findBuddyStores(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.findBuddyStores(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<BuddyStore>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public List<Store> findStoreIdInTransferZone(Long paramLong, String paramString) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramString);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.findStoreIdInTransferZone(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.findStoreIdInTransferZone(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.findStoreIdInTransferZone(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.findStoreIdInTransferZone(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<Store>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public List<Store> findStoreNamesInTransferZone(String paramString1, Long paramLong, String paramString2) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong);
    CompressedObject compressedObject4 = new CompressedObject(paramString2);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.findStoreNamesInTransferZone(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.findStoreNamesInTransferZone(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.findStoreNamesInTransferZone(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.findStoreNamesInTransferZone(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<Store>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public List<Store> findStores(String paramString) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.findStores(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.findStores(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.findStores(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.findStores(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<Store>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public List<Store> findStoresInTransferZone(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.findStoresInTransferZone(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.findStoresInTransferZone(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.findStoresInTransferZone(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.findStoresInTransferZone(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<Store>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public boolean isValidStoreId(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.isValidStoreId(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.isValidStoreId(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.isValidStoreId(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.isValidStoreId(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return ((Boolean)compressedObject.recoverObject()).booleanValue();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public Store readStore(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.readStore(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.readStore(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.readStore(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.readStore(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (Store)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public List<Store> readStores(Collection<Long> paramCollection) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramCollection);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.readStores(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.readStores(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.readStores(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.readStores(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<Store>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public TimeZone readTimeZone(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.readTimeZone(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.readTimeZone(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.readTimeZone(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.readTimeZone(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (TimeZone)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public StoreAddressVO selectStoreAddress(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.selectStoreAddress(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.selectStoreAddress(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.selectStoreAddress(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.selectStoreAddress(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (StoreAddressVO)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public void updateAutoReceiveStores(Long paramLong, Collection<BuddyStore> paramCollection) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramCollection);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.updateAutoReceiveStores(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.updateAutoReceiveStores(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.updateAutoReceiveStores(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.updateAutoReceiveStores(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public void updateBuddyStores(Long paramLong, Collection<BuddyStore> paramCollection) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramCollection);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.updateBuddyStores(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.updateBuddyStores(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.updateBuddyStores(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.updateBuddyStores(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
  
  public void updateSimStoreFlags(Collection<Store> paramCollection) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramCollection);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StoreBean.updateSimStoreFlags(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StoreInterface storeInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = storeInterface.updateSimStoreFlags(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StoreBean.updateSimStoreFlags(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StoreBean.updateSimStoreFlags(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StoreServices. Please contact your system administrator.", throwable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\store\StoreEJBServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */