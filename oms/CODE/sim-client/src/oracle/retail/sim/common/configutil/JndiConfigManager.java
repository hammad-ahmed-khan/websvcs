package oracle.retail.sim.common.configutil;

import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.security.JndiCredentialProvider;

public final class JndiConfigManager {
  public static final String INITIAL_CONTEXT_FACTORY = "INITIAL_CONTEXT_FACTORY";
  
  public static final String NAMING_SERVER_URL = "NAMING_SERVER_URL";
  
  public static final String SECURITY_CREDENTIAL_PROVIDER = "SECURITY_CREDENTIAL_PROVIDER";
  
  public static final String SECURITY_USER_ALIAS = "SECURITY_USER_ALIAS";
  
  public static final String MAX_CONNECT_ATTEMPTS = "MAX_CONNECT_ATTEMPTS";
  
  public static final int MAX_CONNECT_ATTEMPTS_DEFAULT = 2;
  
  private static ConfigManager configManager;
  
  public static String getInitialContextFactory() {
    return configManager.getString("INITIAL_CONTEXT_FACTORY");
  }
  
  public static String getNamingServerUrl() {
    return configManager.getString("NAMING_SERVER_URL");
  }
  
  public static JndiCredentialProvider getSecurityCredentialProvider() {
    return configManager.<JndiCredentialProvider>getObject("SECURITY_CREDENTIAL_PROVIDER", JndiCredentialProvider.class);
  }
  
  public static String getSecurityUserAlias() {
    return configManager.getString("SECURITY_USER_ALIAS");
  }
  
  public static int getMaxConnectAttempts() {
    return configManager.getInteger("MAX_CONNECT_ATTEMPTS", 2);
  }
  
  static {
    try {
      configManager = new ConfigManager("jndi.cfg");
    } catch (Throwable throwable) {
      LogService.error(JndiConfigManager.class, "Failed loading: jndi.cfg", throwable);
    } 
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\configutil\JndiConfigManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */