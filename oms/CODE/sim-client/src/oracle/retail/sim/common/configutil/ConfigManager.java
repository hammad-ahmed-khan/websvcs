package oracle.retail.sim.common.configutil;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.ClassUtility;

public class ConfigManager {
  private Properties properties = new Properties();
  
  public ConfigManager(String paramString) {
    Properties properties = ResourceManager.getProperties(paramString);
    if (properties != null)
      this.properties = properties; 
  }
  
  public Object getObject(String paramString) {
    return ClassUtility.constructObject(getValue(paramString), null);
  }
  
  public <T> T getObject(String paramString, Class<T> paramClass) {
    return (T)ClassUtility.constructObject(getValue(paramString), paramClass);
  }
  
  public String getString(String paramString) {
    return getValue(paramString);
  }
  
  public String getString(String paramString1, String paramString2) {
    String str = getValue(paramString1);
    return (str != null) ? str : paramString2;
  }
  
  public Integer getInteger(String paramString) {
    try {
      return Integer.valueOf(getValue(paramString));
    } catch (Exception exception) {
      LogService.error(this, "getInteger() failed for " + paramString, exception);
      return null;
    } 
  }
  
  public int getInteger(String paramString, int paramInt) {
    try {
      String str = getValue(paramString);
      return (str != null) ? Integer.valueOf(str).intValue() : paramInt;
    } catch (Exception exception) {
      LogService.error(this, "getInteger() failed for " + paramString, exception);
      return paramInt;
    } 
  }
  
  public Long getLong(String paramString) {
    try {
      return Long.valueOf(getValue(paramString));
    } catch (Exception exception) {
      LogService.error(this, "getLong() failed for " + paramString, exception);
      return null;
    } 
  }
  
  public long getLong(String paramString, long paramLong) {
    try {
      String str = getValue(paramString);
      return (str != null) ? Long.valueOf(str).longValue() : paramLong;
    } catch (Exception exception) {
      LogService.error(this, "getLong() failed for " + paramString, exception);
      return paramLong;
    } 
  }
  
  public Double getDouble(String paramString) {
    try {
      return Double.valueOf(getValue(paramString));
    } catch (Exception exception) {
      LogService.error(this, "getDouble() failed for " + paramString, exception);
      return null;
    } 
  }
  
  public double getDouble(String paramString, double paramDouble) {
    try {
      String str = getValue(paramString);
      return (str != null) ? Double.valueOf(str).doubleValue() : paramDouble;
    } catch (Exception exception) {
      LogService.error(this, "getDouble() failed for " + paramString, exception);
      return paramDouble;
    } 
  }
  
  public Boolean getBoolean(String paramString) {
    try {
      return Boolean.valueOf(getValue(paramString));
    } catch (Exception exception) {
      LogService.error(this, "getBoolean() failed for " + paramString, exception);
      return Boolean.FALSE;
    } 
  }
  
  public boolean getBoolean(String paramString, boolean paramBoolean) {
    try {
      String str = getValue(paramString);
      return (str != null) ? Boolean.valueOf(str).booleanValue() : paramBoolean;
    } catch (Exception exception) {
      LogService.error(this, "getBoolean() failed for " + paramString, exception);
      return paramBoolean;
    } 
  }
  
  public Set<String> getKeys() {
    HashSet<String> hashSet = new HashSet<>();
    for (Object str : this.properties.keySet())
      hashSet.add(str.toString()); 
    return hashSet;
  }
  
  private String getValue(String paramString) {
    String str = this.properties.getProperty(paramString);
    return (str == null) ? null : str.trim();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\configutil\ConfigManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */