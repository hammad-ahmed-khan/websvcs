package oracle.retail.sim.common.util;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.ejb.Remote;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import oracle.retail.sim.common.configutil.JndiConfigManager;
import oracle.retail.sim.common.core.JvmLocation;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.security.JndiCredentialProvider;
import oracle.retail.sim.common.security.PasswordCredential;

public final class JndiServiceManager {
  private static Map<String, Object> cache = new HashMap<>();
  
  private static InitialContext staticInitialContext;
  
  private static boolean isSingleContext = !JvmLocation.isServer();
  
  private static Integer maxConnectAttempts;
  
  private static String initialContextFactory;
  
  private static String namingServerUrl;
  
  private static JndiCredentialProvider credentialProvider;
  
  public static void setSingleContext(boolean paramBoolean) {
    isSingleContext = paramBoolean;
  }
  
  public static int getMaxConnectAttempts() {
    if (maxConnectAttempts == null)
      maxConnectAttempts = Integer.valueOf(JndiConfigManager.getMaxConnectAttempts()); 
    return maxConnectAttempts.intValue();
  }
  
  public static String getInitialContextFactory() {
    if (initialContextFactory == null)
      initialContextFactory = JndiConfigManager.getInitialContextFactory(); 
    return initialContextFactory;
  }
  
  public static void setInitialContextFactory(String paramString) {
    initialContextFactory = StringHelper.trimToNull(paramString);
  }
  
  public static String getNamingServerUrl() {
    if (namingServerUrl == null) {
      namingServerUrl = StringHelper.trimToNull(System.getProperty("NAMING_SERVER_URL"));
      if (namingServerUrl == null)
        namingServerUrl = JndiConfigManager.getNamingServerUrl(); 
    } 
    return namingServerUrl;
  }
  
  public static void setNamingServerUrl(String paramString) {
    namingServerUrl = StringHelper.trimToNull(paramString);
  }
  
  public static JndiCredentialProvider getCredentialProvider() {
    if (credentialProvider == null)
      credentialProvider = JndiConfigManager.getSecurityCredentialProvider(); 
    return credentialProvider;
  }
  
  public static void setCredentialProvider(JndiCredentialProvider paramJndiCredentialProvider) {
    credentialProvider = paramJndiCredentialProvider;
  }
  
  public static synchronized <T> T cachedLookup(String paramString, Class<T> paramClass) throws NamingException {
    Object object = cache.get(paramString);
    if (object != null)
      return (T)object; 
    object = lookupNamedObject(paramString, paramClass);
    if (object != null)
      cache.put(paramString, object); 
    return (T)object;
  }
  
  public static synchronized void removeCache(String paramString) {
    cache.remove(paramString);
  }
  
  public static synchronized void clearCache() {
    cache.clear();
  }
  
  public static synchronized <T> T lookup(String paramString, Class<T> paramClass) throws NamingException {
    return lookupNamedObject(paramString, paramClass);
  }
  
  private static <T> T lookupNamedObject(String paramString, Class<T> paramClass) throws NamingException {
    long l = System.currentTimeMillis();
    InitialContext initialContext = null;
    try {
      initialContext = getInitialContext();
      return (T)initialContext.lookup(getRemoteName(paramString, paramClass));
    } catch (NamingException namingException) {
      if (isSingleContext) {
        staticInitialContext = null;
        closeInitialContext(initialContext);
      } 
      throw namingException;
    } finally {
      if (!isSingleContext)
        closeInitialContext(initialContext); 
      if (LogService.isDebugEnabled("service-timings")) {
        long l1 = System.currentTimeMillis();
        LogService.debug("service-timings", "Lookup for " + paramString + " took " + (l1 - l) + "ms to complete");
      } 
    } 
  }
  
  private static String getRemoteName(String paramString, Class<?> paramClass) {
    return (paramClass != null && paramClass.getAnnotation(Remote.class) != null) ? (paramString + "#" + paramClass.getName()) : paramString;
  }
  
  private static InitialContext getInitialContext() throws NamingException {
    if (!isSingleContext)
      return createInitialContext(); 
    if (staticInitialContext == null)
      staticInitialContext = createInitialContext(); 
    return staticInitialContext;
  }
  
  private static InitialContext createInitialContext() throws NamingException {
    Hashtable<String, String> hashtable = buildContextProperties();
    if (hashtable != null && !hashtable.isEmpty())
      return new InitialContext(hashtable); 
    LogService.info(JndiServiceManager.class, "Unable to load JNDI properties, using system default properties.");
    return new InitialContext();
  }
  
  private static void closeInitialContext(InitialContext paramInitialContext) {
    if (paramInitialContext == null)
      return; 
    try {
      paramInitialContext.close();
    } catch (Throwable throwable) {
      LogService.error(JndiServiceManager.class, "Exception closing the context!", throwable);
    } 
  }
  
  public static void closeSingleContext() {
    if (staticInitialContext == null)
      return; 
    closeInitialContext(staticInitialContext);
    staticInitialContext = null;
  }
  
  public static void clearState() {
    initialContextFactory = null;
    credentialProvider = null;
    clearCache();
    closeSingleContext();
  }
  
  private static Hashtable<String, String> buildContextProperties() {
    try {
      String str1 = getInitialContextFactory();
      if (str1 == null)
        throw new SimServerException("Invalid JNDI initial context factory."); 
      String str2 = getNamingServerUrl();
      if (str2 == null)
        throw new SimServerException("Invalid JNDI server URL."); 
      JndiCredentialProvider jndiCredentialProvider = getCredentialProvider();
      Hashtable<Object, Object> hashtable = new Hashtable<>();
      hashtable.put("java.naming.factory.initial", str1);
      hashtable.put("java.naming.provider.url", str2);
      if (jndiCredentialProvider != null) {
        PasswordCredential passwordCredential = jndiCredentialProvider.readJndiCredential();
        if (passwordCredential != null) {
          String str = passwordCredential.getName();
          if (!StringHelper.isNullOrEmpty(str))
            hashtable.put("java.naming.security.principal", str); 
          char[] arrayOfChar = passwordCredential.getPassword();
          if (!ArrayUtility.isNullOrEmpty(arrayOfChar))
            hashtable.put("java.naming.security.credentials", new String(arrayOfChar)); 
        } 
      } 
      return (Hashtable)hashtable;
    } catch (Throwable throwable) {
      LogService.fatal(JndiServiceManager.class, "Error loading JNDI properties.", throwable);
      return null;
    } 
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\commo\\util\JndiServiceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */