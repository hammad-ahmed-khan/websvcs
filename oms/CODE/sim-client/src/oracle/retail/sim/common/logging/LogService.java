package oracle.retail.sim.common.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.locale.StringConstants;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class LogService {
  private static final String NO_CLASS = "NoClass";
  
  public static boolean isTraceEnabled(Object paramObject) {
    return getLog(paramObject).isTraceEnabled();
  }
  
  public static boolean isDebugEnabled(Object paramObject) {
    return getLog(paramObject).isDebugEnabled();
  }
  
  public static boolean isInfoEnabled(Object paramObject) {
    return getLog(paramObject).isInfoEnabled();
  }
  
  public static boolean isWarnEnabled(Object paramObject) {
    return getLog(paramObject).isEnabledFor((Priority)Level.WARN);
  }
  
  public static boolean isErrorEnabled(Object paramObject) {
    return getLog(paramObject).isEnabledFor((Priority)Level.ERROR);
  }
  
  public static boolean isFatalEnabled(Object paramObject) {
    return getLog(paramObject).isEnabledFor((Priority)Level.FATAL);
  }
  
  public static void trace(Object paramObject, String paramString) {
    trace(paramObject, null, paramString, null);
  }
  
  public static void trace(Object paramObject, String paramString, Throwable paramThrowable) {
    trace(paramObject, null, paramString, paramThrowable);
  }
  
  public static void trace(Object paramObject, String paramString1, String paramString2) {
    trace(paramObject, paramString1, paramString2, null);
  }
  
  public static void trace(Object paramObject, String paramString1, String paramString2, Throwable paramThrowable) {
    Logger logger = getLog(paramObject);
    String str = buildMessage(paramString1, paramString2, null, paramThrowable);
    if (paramThrowable == null) {
      logger.trace(str);
    } else {
      logger.trace(str, paramThrowable);
    } 
  }
  
  public static void debug(Object paramObject, String paramString) {
    debug(paramObject, null, paramString, null);
  }
  
  public static void debug(Object paramObject, String paramString, Throwable paramThrowable) {
    debug(paramObject, null, paramString, paramThrowable);
  }
  
  public static void debug(Object paramObject, String paramString1, String paramString2) {
    debug(paramObject, paramString1, paramString2, null);
  }
  
  public static void debug(Object paramObject, String paramString1, String paramString2, Throwable paramThrowable) {
    Logger logger = getLog(paramObject);
    String str = buildMessage(paramString1, paramString2, null, paramThrowable);
    if (paramThrowable == null) {
      logger.debug(str);
    } else {
      logger.debug(str, paramThrowable);
    } 
  }
  
  public static void info(Object paramObject, String paramString) {
    info(paramObject, null, paramString, null);
  }
  
  public static void info(Object paramObject, String paramString, Throwable paramThrowable) {
    info(paramObject, null, paramString, paramThrowable);
  }
  
  public static void info(Object paramObject, String paramString1, String paramString2) {
    info(paramObject, paramString1, paramString2, null);
  }
  
  public static void info(Object paramObject, String paramString1, String paramString2, Throwable paramThrowable) {
    Logger logger = getLog(paramObject);
    String str = buildMessage(paramString1, paramString2, null, paramThrowable);
    if (paramThrowable == null) {
      logger.info(str);
    } else {
      logger.info(str, paramThrowable);
    } 
  }
  
  public static void warn(Object paramObject, String paramString) {
    warn(paramObject, null, paramString, null, null);
  }
  
  public static void warn(Object paramObject, String paramString, Throwable paramThrowable) {
    warn(paramObject, null, paramString, null, paramThrowable);
  }
  
  public static void warn(Object paramObject, String paramString1, String paramString2, String paramString3) {
    warn(paramObject, paramString1, paramString2, paramString3, null);
  }
  
  public static void warn(Object paramObject, String paramString1, String paramString2, String paramString3, Throwable paramThrowable) {
    Logger logger = getLog(paramObject);
    String str = buildMessage(paramString1, paramString2, paramString3, paramThrowable);
    if (paramThrowable == null) {
      logger.warn(str);
    } else {
      logger.warn(str, paramThrowable);
    } 
  }
  
  public static void error(Object paramObject, String paramString) {
    error(paramObject, null, paramString, null, null);
  }
  
  public static void error(Object paramObject, String paramString, Throwable paramThrowable) {
    error(paramObject, null, paramString, null, paramThrowable);
  }
  
  public static void error(Object paramObject, String paramString1, String paramString2) {
    error(paramObject, paramString1, paramString2, null, null);
  }
  
  public static void error(Object paramObject, String paramString1, String paramString2, String paramString3) {
    error(paramObject, paramString1, paramString2, paramString3, null);
  }
  
  public static void error(Object paramObject, String paramString1, String paramString2, Throwable paramThrowable) {
    error(paramObject, paramString1, paramString2, null, paramThrowable);
  }
  
  public static void error(Object paramObject, String paramString1, String paramString2, String paramString3, Throwable paramThrowable) {
    Logger logger = getLog(paramObject);
    if (paramThrowable instanceof SimServerException) {
      logger.error(buildErrorMessage(paramString1, paramString2, paramString3, paramThrowable), paramThrowable.getCause());
    } else {
      logger.error(buildErrorMessage(paramString1, paramString2, paramString3, null), paramThrowable);
    } 
  }
  
  public static void fatal(Object paramObject, String paramString) {
    fatal(paramObject, null, paramString, null, null);
  }
  
  public static void fatal(Object paramObject, String paramString, Throwable paramThrowable) {
    fatal(paramObject, null, paramString, null, paramThrowable);
  }
  
  public static void fatal(Object paramObject, String paramString1, String paramString2, String paramString3) {
    fatal(paramObject, paramString1, paramString2, paramString3, null);
  }
  
  public static void fatal(Object paramObject, String paramString1, String paramString2, String paramString3, Throwable paramThrowable) {
    Logger logger = getLog(paramObject);
    if (paramThrowable instanceof SimServerException) {
      logger.fatal(buildErrorMessage(paramString1, paramString2, paramString3, paramThrowable), paramThrowable.getCause());
    } else {
      logger.fatal(buildErrorMessage(paramString1, paramString2, paramString3, null), paramThrowable);
    } 
  }
  
  private static Logger getLog(Object paramObject) {
    return (paramObject instanceof String) ? Logger.getLogger((String)paramObject) : ((paramObject instanceof Class) ? Logger.getLogger((Class)paramObject) : ((paramObject != null) ? Logger.getLogger(paramObject.getClass().getName()) : Logger.getLogger("NoClass")));
  }
  
  private static String buildMessage(String paramString1, String paramString2, String paramString3, Throwable paramThrowable) {
    StringBuilder stringBuilder = new StringBuilder();
    if (paramString1 != null)
      stringBuilder.append(paramString1).append(": "); 
    if (paramString2 != null)
      stringBuilder.append(paramString2); 
    if (paramString3 != null)
      stringBuilder.append(", ").append(paramString3); 
    if (paramThrowable != null) {
      stringBuilder.append(StringConstants.EOL);
      if (paramThrowable instanceof SimServerException) {
        stringBuilder.append(((SimServerException)paramThrowable).getLogMessage());
      } else {
        stringBuilder.append(paramThrowable.getMessage());
      } 
      stringBuilder.append(" ");
    } 
    return stringBuilder.toString();
  }
  
  private static String buildErrorMessage(String paramString1, String paramString2, String paramString3, Throwable paramThrowable) {
    StringBuilder stringBuilder = new StringBuilder();
    if (paramString1 != null)
      stringBuilder.append(paramString1).append(": "); 
    if (paramString2 != null)
      stringBuilder.append(paramString2); 
    if (paramThrowable != null) {
      stringBuilder.append(StringConstants.EOL);
      if (paramThrowable instanceof SimServerException) {
        stringBuilder.append(((SimServerException)paramThrowable).getLogMessage());
      } else {
        stringBuilder.append(paramThrowable.getMessage());
      } 
      stringBuilder.append(" ");
    } 
    if (paramString3 != null)
      stringBuilder.append(", ").append(paramString3); 
    return stringBuilder.toString();
  }
  
  public static String getStackTraceAsString(Throwable paramThrowable) {
    StringWriter stringWriter = new StringWriter();
    paramThrowable.printStackTrace(new PrintWriter(stringWriter, true));
    return stringWriter.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\logging\LogService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */