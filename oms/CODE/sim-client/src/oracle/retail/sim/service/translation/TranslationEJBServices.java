package oracle.retail.sim.service.translation;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.translation.TranslationExportRecord;
import oracle.retail.sim.common.translation.TranslationMap;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;
import oracle.retail.sim.service.ejb.TranslationInterface;

public class TranslationEJBServices extends TranslationServices {
  private TranslationInterface lookup() throws Exception {
    try {
      return (TranslationInterface)JndiServiceManager.cachedLookup("TranslationBean", TranslationInterface.class);
    } catch (Throwable throwable) {
      throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
    } 
  }
  
  private void removeCache() {
    JndiServiceManager.removeCache("TranslationBean");
  }
  
  public void addTranslationKey(String paramString1, String paramString2) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString1);
    CompressedObject compressedObject3 = new CompressedObject(paramString2);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.addTranslationKey(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.addTranslationKey(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.addTranslationKey(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.addTranslationKey(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public TranslationMap findAllTranslations(Locale paramLocale) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLocale);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.findAllTranslations(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.findAllTranslations(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.findAllTranslations(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.findAllTranslations(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (TranslationMap)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public List<Locale> findLocales() throws Exception {
    CompressedObject compressedObject = new CompressedObject(UniversalContext.getSession());
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.findLocales(compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject1 = translationInterface.findLocales(compressedObject);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.findLocales(compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.findLocales(compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject1) + " bytes in the returned serialized object."); 
        return (List<Locale>)compressedObject1.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public List<TranslationExportRecord> findTranslationDeltaRecords(String paramString1, String paramString2, String paramString3) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString1);
    CompressedObject compressedObject3 = new CompressedObject(paramString2);
    CompressedObject compressedObject4 = new CompressedObject(paramString3);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.findTranslationDeltaRecords(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.findTranslationDeltaRecords(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.findTranslationDeltaRecords(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.findTranslationDeltaRecords(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<TranslationExportRecord>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public List<TranslationExportRecord> findTranslationExportRecords(String paramString1, String paramString2, String paramString3) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString1);
    CompressedObject compressedObject3 = new CompressedObject(paramString2);
    CompressedObject compressedObject4 = new CompressedObject(paramString3);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.findTranslationExportRecords(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.findTranslationExportRecords(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.findTranslationExportRecords(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.findTranslationExportRecords(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<TranslationExportRecord>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public List<TranslationExportRecord> findTranslationExportRecords(String paramString1, String paramString2, String paramString3, Date paramDate) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString1);
    CompressedObject compressedObject3 = new CompressedObject(paramString2);
    CompressedObject compressedObject4 = new CompressedObject(paramString3);
    CompressedObject compressedObject5 = new CompressedObject(paramDate);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.findTranslationExportRecords2(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.findTranslationExportRecords2(compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.findTranslationExportRecords2(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.findTranslationExportRecords2(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (List<TranslationExportRecord>)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public Map<String, String> findTranslationKeys() throws Exception {
    CompressedObject compressedObject = new CompressedObject(UniversalContext.getSession());
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.findTranslationKeys(compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject1 = translationInterface.findTranslationKeys(compressedObject);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.findTranslationKeys(compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.findTranslationKeys(compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject1) + " bytes in the returned serialized object."); 
        return (Map<String, String>)compressedObject1.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public TranslationMap findTranslations(Locale paramLocale) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLocale);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.findTranslations(compressed0, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.findTranslations(compressedObject2, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.findTranslations(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.findTranslations(compressed0, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (TranslationMap)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public TranslationMap findUpdatedTranslations(Locale paramLocale, Date paramDate) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLocale);
    CompressedObject compressedObject3 = new CompressedObject(paramDate);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.findUpdatedTranslations(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.findUpdatedTranslations(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.findUpdatedTranslations(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.findUpdatedTranslations(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
        return (TranslationMap)compressedObject.recoverObject();
      } catch (SimServerException|oracle.retail.sim.common.business.BusinessException|SecurityException simServerException) {
        throw simServerException;
      } catch (Throwable throwable1) {
        throwable = throwable1;
        LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
        removeCache();
        b++;
      } 
    } 
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public void importTranslationRecords(String paramString1, String paramString2, String paramString3, List<TranslationExportRecord> paramList) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString1);
    CompressedObject compressedObject3 = new CompressedObject(paramString2);
    CompressedObject compressedObject4 = new CompressedObject(paramString3);
    CompressedObject compressedObject5 = new CompressedObject(paramList);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.importTranslationRecords(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.importTranslationRecords(compressedObject2, compressedObject3, compressedObject4, compressedObject5, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.importTranslationRecords(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.importTranslationRecords(compressed0, compressed1, compressed2, compressed3, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public void updateTranslation(Locale paramLocale, String paramString1, String paramString2) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramLocale);
    CompressedObject compressedObject3 = new CompressedObject(paramString1);
    CompressedObject compressedObject4 = new CompressedObject(paramString2);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.updateTranslation(compressed0, compressed1, compressed2, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject4, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.updateTranslation(compressedObject2, compressedObject3, compressedObject4, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.updateTranslation(compressed0, compressed1, compressed2, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.updateTranslation(compressed0, compressed1, compressed2, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
  
  public void updateTranslationKey(String paramString1, String paramString2) throws Exception {
    CompressedObject compressedObject1 = new CompressedObject(UniversalContext.getSession());
    CompressedObject compressedObject2 = new CompressedObject(paramString1);
    CompressedObject compressedObject3 = new CompressedObject(paramString2);
    if (LogService.isDebugEnabled("serialized-object-sizes"))
      LogService.debug("serialized-object-sizes", "TranslationBean.updateTranslationKey(compressed0, compressed1, compressedSimSession) (remote call) is sending " + SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject3, compressedObject1 }) + " bytes in serialized object(s) for the remote call."); 
    Throwable throwable = null;
    int i = JndiServiceManager.getMaxConnectAttempts();
    byte b = 0;
    while (b < i) {
      TranslationInterface translationInterface = lookup();
      try {
        long l = System.currentTimeMillis();
        CompressedObject compressedObject = translationInterface.updateTranslationKey(compressedObject2, compressedObject3, compressedObject1);
        if (LogService.isDebugEnabled("service-timings")) {
          long l1 = System.currentTimeMillis();
          LogService.debug("service-timings", "TranslationBean.updateTranslationKey(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
        } 
        if (LogService.isDebugEnabled("serialized-object-sizes"))
          LogService.debug("serialized-object-sizes", "TranslationBean.updateTranslationKey(compressed0, compressed1, compressedSimSession) (remote call) received " + SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object."); 
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
    throw new DowntimeException("An error occurred accessing TranslationServices. Please contact your system administrator.", throwable);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\translation\TranslationEJBServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */