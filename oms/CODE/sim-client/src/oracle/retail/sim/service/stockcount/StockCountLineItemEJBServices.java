package oracle.retail.sim.service.stockcount;

import java.util.List;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.item.Item;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.stockcount.StockCountAuthorizeSerialNumber;
import oracle.retail.sim.common.stockcount.StockCountLineItem;
import oracle.retail.sim.common.stockcount.StockCountLineItemAreaBreakdownVO;
import oracle.retail.sim.common.stockcount.StockCountLineItemCompBreakdownVO;
import oracle.retail.sim.common.stockcount.StockCountPhase;
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;
import oracle.retail.sim.common.stockcount.StockCountSerialNumber;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;
import oracle.retail.sim.service.ejb.StockCountLineItemInterface;

public class StockCountLineItemEJBServices extends StockCountLineItemServices {
  private StockCountLineItemInterface lookup() throws Exception {
    try {
      return (StockCountLineItemInterface)JndiServiceManager.cachedLookup("StockCountLineItemBean", StockCountLineItemInterface.class);
    } catch (Throwable throwable) {
      throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
    } 
  }
  
  private void removeCache() {
    JndiServiceManager.removeCache("StockCountLineItemBean");
  }
  
  public StockCountLineItem createAdhocLineItem(Long paramLong1, Long paramLong2, Long paramLong3, String paramString) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    CompressedObject compressedObject4 = new CompressedObject(paramLong3);
    CompressedObject compressedObject5 = new CompressedObject(paramString);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.createAdhocLineItem(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.createAdhocLineItem(compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.createAdhocLineItem(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.createAdhocLineItem(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (StockCountLineItem)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public void createRejectedLineItem(Long paramLong, StockCountRejectedLineItem paramStockCountRejectedLineItem) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramStockCountRejectedLineItem);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.createRejectedLineItem(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.createRejectedLineItem(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.createRejectedLineItem(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.createRejectedLineItem(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public StockCountLineItem createUnitAndAmountLineItem(Long paramLong1, Long paramLong2, Long paramLong3, String paramString) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    CompressedObject compressedObject4 = new CompressedObject(paramLong3);
    CompressedObject compressedObject5 = new CompressedObject(paramString);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.createUnitAndAmountLineItem(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.createUnitAndAmountLineItem(compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.createUnitAndAmountLineItem(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.createUnitAndAmountLineItem(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (StockCountLineItem)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public List<StockCountAuthorizeSerialNumber> findAuthorizationLineItemSerialNumbers(Long paramLong1, Long paramLong2, String paramString) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    CompressedObject compressedObject4 = new CompressedObject(paramString);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findAuthorizationLineItemSerialNumbers(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.findAuthorizationLineItemSerialNumbers(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.findAuthorizationLineItemSerialNumbers(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findAuthorizationLineItemSerialNumbers(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<StockCountAuthorizeSerialNumber>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public List<StockCountLineItemCompBreakdownVO> findLineItemComponentCountBreakdownDetails(Long paramLong1, Long paramLong2, String paramString) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    CompressedObject compressedObject4 = new CompressedObject(paramString);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findLineItemComponentCountBreakdownDetails(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.findLineItemComponentCountBreakdownDetails(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.findLineItemComponentCountBreakdownDetails(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findLineItemComponentCountBreakdownDetails(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<StockCountLineItemCompBreakdownVO>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public List<StockCountLineItemAreaBreakdownVO> findLineItemSequencedAreaBreakdownDetails(Long paramLong1, Long paramLong2, String paramString) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    CompressedObject compressedObject4 = new CompressedObject(paramString);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findLineItemSequencedAreaBreakdownDetails(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.findLineItemSequencedAreaBreakdownDetails(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.findLineItemSequencedAreaBreakdownDetails(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findLineItemSequencedAreaBreakdownDetails(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<StockCountLineItemAreaBreakdownVO>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public List<StockCountRejectedLineItem> findRejectedLineItems(Long paramLong1, Long paramLong2) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findRejectedLineItems(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.findRejectedLineItems(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.findRejectedLineItems(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findRejectedLineItems(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<StockCountRejectedLineItem>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public StockCountSerialNumber findStockCountSerialNumber(Long paramLong, String paramString1, String paramString2) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramString1);
    CompressedObject compressedObject4 = new CompressedObject(paramString2);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findStockCountSerialNumber(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.findStockCountSerialNumber(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.findStockCountSerialNumber(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.findStockCountSerialNumber(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (StockCountSerialNumber)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public List<StockCountAuthorizeSerialNumber> generateAuthorizedSerialNumbers(Long paramLong, StockCountLineItem paramStockCountLineItem, Integer paramInteger) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramStockCountLineItem);
    CompressedObject compressedObject4 = new CompressedObject(paramInteger);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.generateAuthorizedSerialNumbers(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.generateAuthorizedSerialNumbers(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.generateAuthorizedSerialNumbers(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.generateAuthorizedSerialNumbers(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<StockCountAuthorizeSerialNumber>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public int getNumberOfUncountedLineItems(Long paramLong1, Long paramLong2, StockCountPhase paramStockCountPhase) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    CompressedObject compressedObject4 = new CompressedObject(paramStockCountPhase);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.getNumberOfUncountedLineItems(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.getNumberOfUncountedLineItems(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.getNumberOfUncountedLineItems(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.getNumberOfUncountedLineItems(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public boolean isAvailableToCount(Long paramLong1, Long paramLong2, Item paramItem) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    CompressedObject compressedObject4 = new CompressedObject(paramItem);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.isAvailableToCount(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.isAvailableToCount(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.isAvailableToCount(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.isAvailableToCount(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public boolean isSerialNumberAlreadyCounted(Long paramLong, StockCountPhase paramStockCountPhase, String paramString1, String paramString2) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramStockCountPhase);
    CompressedObject compressedObject4 = new CompressedObject(paramString1);
    CompressedObject compressedObject5 = new CompressedObject(paramString2);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.isSerialNumberAlreadyCounted(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.isSerialNumberAlreadyCounted(compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.isSerialNumberAlreadyCounted(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.isSerialNumberAlreadyCounted(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public void markStockCountLineItemAsDiscrepant(Long paramLong1, Long paramLong2, String paramString) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    CompressedObject compressedObject4 = new CompressedObject(paramString);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.markStockCountLineItemAsDiscrepant(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.markStockCountLineItemAsDiscrepant(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.markStockCountLineItemAsDiscrepant(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.markStockCountLineItemAsDiscrepant(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public List<StockCountLineItem> readPrimaryStockCountLineItems(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.readPrimaryStockCountLineItems(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.readPrimaryStockCountLineItems(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.readPrimaryStockCountLineItems(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.readPrimaryStockCountLineItems(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<StockCountLineItem>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public StockCountLineItem readStockCountLineItem(Long paramLong, String paramString) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramString);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.readStockCountLineItem(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.readStockCountLineItem(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.readStockCountLineItem(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.readStockCountLineItem(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (StockCountLineItem)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public List<StockCountLineItem> readStockCountLineItems(Long paramLong) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.readStockCountLineItems(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.readStockCountLineItems(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.readStockCountLineItems(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.readStockCountLineItems(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<StockCountLineItem>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public List<StockCountLineItem> readStockCountLineItems(Long paramLong, List<Long> paramList) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramList);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.readStockCountLineItems2(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.readStockCountLineItems2(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.readStockCountLineItems2(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.readStockCountLineItems2(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<StockCountLineItem>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public void updateAuthorizedSerialNumbers(StockCountLineItem paramStockCountLineItem, List<StockCountAuthorizeSerialNumber> paramList) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramStockCountLineItem);
    CompressedObject compressedObject3 = new CompressedObject(paramList);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.updateAuthorizedSerialNumbers(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.updateAuthorizedSerialNumbers(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.updateAuthorizedSerialNumbers(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.updateAuthorizedSerialNumbers(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public void updateLineItems(Long paramLong1, Long paramLong2, List<StockCountLineItem> paramList) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong1);
    CompressedObject compressedObject3 = new CompressedObject(paramLong2);
    CompressedObject compressedObject4 = new CompressedObject(paramList);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.updateLineItems(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.updateLineItems(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.updateLineItems(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.updateLineItems(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
  
  public List<String> updateRejectedLineItems(Long paramLong, List<StockCountRejectedLineItem> paramList) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLong);
    CompressedObject compressedObject3 = new CompressedObject(paramList);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "StockCountLineItemBean.updateRejectedLineItems(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      StockCountLineItemInterface stockCountLineItemInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = stockCountLineItemInterface.updateRejectedLineItems(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "StockCountLineItemBean.updateRejectedLineItems(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "StockCountLineItemBean.updateRejectedLineItems(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing StockCountLineItemServices. Please contact your system administrator.", throwable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\stockcount\StockCountLineItemEJBServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */