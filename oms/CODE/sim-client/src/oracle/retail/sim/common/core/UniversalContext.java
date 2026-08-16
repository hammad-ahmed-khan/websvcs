package oracle.retail.sim.common.core;

import java.util.Locale;
import oracle.retail.sim.common.core.locale.StringHelper;

public final class UniversalContext {
  private static ThreadLocal<SimSession> threadedSession = new ThreadLocal<>();
  
  private static SimSession staticSession;
  
  private static boolean isSingleSession;
  
  public static void setSingleSession(boolean paramBoolean) {
    isSingleSession = paramBoolean;
  }
  
  public static String getSessionId() {
    SimSession simSession = getSession();
    return (simSession != null) ? simSession.getSessionId() : null;
  }
  
  public static String getUserName() {
    SimSession simSession = getSession();
    return (simSession != null) ? simSession.getUserName() : null;
  }
  
  public static DeviceType getDeviceType() {
    SimSession simSession = getSession();
    return (simSession != null) ? simSession.getDeviceType() : null;
  }
  
  public static Locale getLocale() {
    SimSession simSession = getSession();
    return (simSession != null) ? simSession.getLocale() : null;
  }
  
  public static SimSession getSession() {
    return isSingleSession ? staticSession : threadedSession.get();
  }
  
  public static void setSession(SimSession paramSimSession) {
    if (isSingleSession) {
      staticSession = paramSimSession;
    } else {
      threadedSession.set(paramSimSession);
    } 
  }
  
  public static void startSession(String paramString, DeviceType paramDeviceType) {
    startSession(paramString, paramDeviceType, null);
  }
  
  public static void startSession(String paramString, DeviceType paramDeviceType, Locale paramLocale) {
    if (StringHelper.isNullOrEmpty(paramString))
      throw new IllegalArgumentException("Invalid UserName"); 
    if (paramDeviceType == null)
      throw new IllegalArgumentException("Invalid DeviceType"); 
    setSession(new SimSession(StringHelper.trimToNull(paramString), paramDeviceType, (paramLocale != null) ? paramLocale : Locale.getDefault()));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\UniversalContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */