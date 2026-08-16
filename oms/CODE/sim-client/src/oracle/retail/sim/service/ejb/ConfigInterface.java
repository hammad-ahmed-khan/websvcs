package oracle.retail.sim.service.ejb;

import java.util.List;
import java.util.Map;
import javax.ejb.Remote;
import oracle.retail.sim.common.config.CodeInfo;
import oracle.retail.sim.common.config.ConfigurationOption;
import oracle.retail.sim.common.config.NavigationData;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.SimSession;

@Remote
public interface ConfigInterface {
  CompressedObject<List<CodeInfo>> findCodeInfos(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<NavigationData> getClientNavigationData(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<String>> getDefaultStoreAdminTopics(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<String>> getStoreAdminTopics(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<String>> getSystemAdminTopics(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> ping(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<ConfigurationOption>> readConfigOptions(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Map<String, Object>> readConfigSettings(CompressedObject<DeviceType> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ConfigurationOption>> readDefaultStoreConfigOptions(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<ConfigurationOption>> readStoreConfigOptions(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Map<String, Object>> readStoreConfigSettings(CompressedObject<Long> paramCompressedObject, CompressedObject<DeviceType> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateConfigOptions(CompressedObject<List<ConfigurationOption>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateDefaultStoreConfigOptions(CompressedObject<List<ConfigurationOption>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateStoreConfigOptions(CompressedObject<Long> paramCompressedObject, CompressedObject<List<ConfigurationOption>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ConfigInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */