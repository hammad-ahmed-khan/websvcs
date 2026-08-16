package oracle.retail.sim.service.config;

import java.util.List;
import java.util.Map;
import oracle.retail.sim.common.config.CodeInfo;
import oracle.retail.sim.common.config.ConfigurationOption;
import oracle.retail.sim.common.config.NavigationData;
import oracle.retail.sim.common.core.DeviceType;

public abstract class ConfigServices {
  public abstract void ping() throws Exception;
  
  public abstract Map<String, Object> readConfigSettings(DeviceType paramDeviceType) throws Exception;
  
  public abstract List<ConfigurationOption> readConfigOptions() throws Exception;
  
  public abstract void updateConfigOptions(List<ConfigurationOption> paramList) throws Exception;
  
  public abstract Map<String, Object> readStoreConfigSettings(Long paramLong, DeviceType paramDeviceType) throws Exception;
  
  public abstract List<ConfigurationOption> readStoreConfigOptions(Long paramLong) throws Exception;
  
  public abstract void updateStoreConfigOptions(Long paramLong, List<ConfigurationOption> paramList) throws Exception;
  
  public abstract List<ConfigurationOption> readDefaultStoreConfigOptions() throws Exception;
  
  public abstract void updateDefaultStoreConfigOptions(List<ConfigurationOption> paramList) throws Exception;
  
  public abstract List<String> getSystemAdminTopics() throws Exception;
  
  public abstract List<String> getStoreAdminTopics(Long paramLong) throws Exception;
  
  public abstract List<String> getDefaultStoreAdminTopics() throws Exception;
  
  public abstract NavigationData getClientNavigationData() throws Exception;
  
  public abstract List<CodeInfo> findCodeInfos(String paramString) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\config\ConfigServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */