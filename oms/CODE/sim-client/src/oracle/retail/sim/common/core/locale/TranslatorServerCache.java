package oracle.retail.sim.common.core.locale;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.CacheRefreshStrategy;
import oracle.retail.sim.common.util.SystemTimestampProvider;
import oracle.retail.sim.common.util.TimedCacheRefreshStrategy;
import oracle.retail.sim.common.util.TimestampProvider;
import oracle.retail.sim.service.core.NativeServiceFactory;

public class TranslatorServerCache {
  private static Map<Locale, Map<String, String>> localeMap = new HashMap<>();
  
  private static TimestampProvider timestampProvider = (TimestampProvider)new SystemTimestampProvider();
  
  private static CacheRefreshStrategy translationCacheTimer = (CacheRefreshStrategy)new TimedCacheRefreshStrategy(timestampProvider, "REFRESH_RATE_TRANSLATION", 36000000L);
  
  public static String getText(Locale paramLocale, String paramString) {
    Object object = getObject(paramLocale, paramString);
    return (object instanceof String) ? (String)object : processUntranslatedKey(paramString);
  }
  
  public static String getMessage(Locale paramLocale, String paramString) {
    Object object = getObject(paramLocale, paramString);
    return (object instanceof String) ? (String)object : processUntranslatedKey(paramString);
  }
  
  public static String getMessage(Locale paramLocale, String paramString1, String paramString2) {
    if (paramString2 != null) {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = getText(paramLocale, paramString2);
      return getMessage(paramLocale, paramString1, (Object[])arrayOfString);
    } 
    return getMessage(paramLocale, paramString1);
  }
  
  public static String getMessage(Locale paramLocale, String paramString1, String paramString2, String paramString3) {
    String[] arrayOfString = new String[2];
    arrayOfString[0] = paramString2;
    arrayOfString[1] = paramString3;
    return getMessage(paramLocale, paramString1, (Object[])arrayOfString);
  }
  
  public static String getMessage(Locale paramLocale, String paramString1, String paramString2, String paramString3, String paramString4) {
    String[] arrayOfString = new String[3];
    arrayOfString[0] = paramString2;
    arrayOfString[1] = paramString3;
    arrayOfString[2] = paramString4;
    return getMessage(paramLocale, paramString1, (Object[])arrayOfString);
  }
  
  public static String getMessage(Locale paramLocale, String paramString, Object[] paramArrayOfObject) {
    String str1 = getMessage(paramLocale, paramString);
    if (paramArrayOfObject == null || paramArrayOfObject.length <= 0)
      return str1; 
    for (byte b = 0; b < paramArrayOfObject.length; b++) {
      if (paramArrayOfObject[b] == null)
        paramArrayOfObject[b] = ""; 
      if (paramArrayOfObject[b] instanceof String)
        paramArrayOfObject[b] = getText(paramLocale, (String)paramArrayOfObject[b]); 
    } 
    StringHelper stringHelper = new StringHelper(paramLocale);
    String str2 = stringHelper.replace(str1, "'", "''");
    MessageFormat messageFormat = new MessageFormat(str2, paramLocale);
    return messageFormat.format(paramArrayOfObject);
  }
  
  private static Object getObject(Locale paramLocale, String paramString) {
    if (paramLocale == null)
      paramLocale = Locale.ENGLISH; 
    Map<String, String> map = localeMap.get(paramLocale);
    if (map == null || translationCacheTimer.isCacheStale())
      try {
        map = NativeServiceFactory.getTranslationServices().findTranslations(paramLocale).getTranslations();
        localeMap.put(paramLocale, map);
      } catch (Throwable throwable) {
        LogService.error(TranslatorServerCache.class, "getObject()", "Unable to retrieve translations.", "Validate locale exist in database.", throwable);
        map = Collections.emptyMap();
      }  
    return map.get(paramString);
  }
  
  private static String processUntranslatedKey(String paramString) {
    if (LogService.isDebugEnabled(TranslatorServerCache.class))
      LogService.debug(TranslatorServerCache.class, "The following key has no translation: [" + paramString + "]"); 
    return paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\locale\TranslatorServerCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */