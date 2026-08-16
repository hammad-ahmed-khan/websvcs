package oracle.retail.sim.service.itembasket;

import java.util.List;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.itembasket.ItemBasket;
import oracle.retail.sim.common.itembasket.ItemBasketType;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;
import oracle.retail.sim.service.ejb.ItemBasketInterface;

public class ItemBasketEJBServices extends ItemBasketServices {
  private ItemBasketInterface lookup() throws Exception {
    try {
      return (ItemBasketInterface)JndiServiceManager.cachedLookup("ItemBasketBean", ItemBasketInterface.class);
    } catch (Throwable throwable) {
      throw new DowntimeException("An error occurred accessing ItemBasketServices. Please contact your system administrator.", throwable);
    } 
  }
  
  private void removeCache() {
    JndiServiceManager.removeCache("ItemBasketBean");
  }
  
  public void deleteItemBasket(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ItemBasketBean.deleteItemBasket(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ItemBasketInterface itemBasketInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = itemBasketInterface.deleteItemBasket(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ItemBasketBean.deleteItemBasket(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ItemBasketBean.deleteItemBasket(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing ItemBasketServices. Please contact your system administrator.", throwable);
  }
  
  public List<ItemBasketType> findItemBasketTypes() throws Exception {
    CompressedObject compressedObject = new CompressedObject(UniversalContext.getSession());
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ItemBasketBean.findItemBasketTypes(compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ItemBasketInterface itemBasketInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject1 = itemBasketInterface.findItemBasketTypes(compressedObject);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ItemBasketBean.findItemBasketTypes(compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ItemBasketBean.findItemBasketTypes(compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject1) + " bytes in the returned serialized object."); 
        return (List<ItemBasketType>)compressedObject1.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ItemBasketServices. Please contact your system administrator.", throwable);
  }
  
  public int findNumberOfItemBaskets(String paramString, Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString);
    CompressedObject compressedObject3 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ItemBasketBean.findNumberOfItemBaskets(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ItemBasketInterface itemBasketInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = itemBasketInterface.findNumberOfItemBaskets(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ItemBasketBean.findNumberOfItemBaskets(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ItemBasketBean.findNumberOfItemBaskets(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return ((Integer)compressedObject.recoverObject()).intValue();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ItemBasketServices. Please contact your system administrator.", throwable);
  }
  
  public Long insertItemBasket(ItemBasket paramItemBasket) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramItemBasket);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ItemBasketBean.insertItemBasket(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ItemBasketInterface itemBasketInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = itemBasketInterface.insertItemBasket(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ItemBasketBean.insertItemBasket(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ItemBasketBean.insertItemBasket(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing ItemBasketServices. Please contact your system administrator.", throwable);
  }
  
  public ItemBasket readItemBasket(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ItemBasketBean.readItemBasket(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ItemBasketInterface itemBasketInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = itemBasketInterface.readItemBasket(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ItemBasketBean.readItemBasket(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ItemBasketBean.readItemBasket(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (ItemBasket)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ItemBasketServices. Please contact your system administrator.", throwable);
  }
  
  public ItemBasket readItemBasket(String paramString, Long paramLong, Integer paramInteger) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString);
    CompressedObject compressedObject3 = new CompressedObject(paramLong);
    CompressedObject compressedObject4 = new CompressedObject(paramInteger);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ItemBasketBean.readItemBasket2(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ItemBasketInterface itemBasketInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = itemBasketInterface.readItemBasket2(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ItemBasketBean.readItemBasket2(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ItemBasketBean.readItemBasket2(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (ItemBasket)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing ItemBasketServices. Please contact your system administrator.", throwable);
  }
  
  public void updateItemBasket(ItemBasket paramItemBasket) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramItemBasket);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "ItemBasketBean.updateItemBasket(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      ItemBasketInterface itemBasketInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = itemBasketInterface.updateItemBasket(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "ItemBasketBean.updateItemBasket(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "ItemBasketBean.updateItemBasket(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing ItemBasketServices. Please contact your system administrator.", throwable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\itembasket\ItemBasketEJBServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */