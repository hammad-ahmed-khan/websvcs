package oracle.retail.sim.common.config;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.type.HelpDisplayer;
import oracle.retail.sim.common.rules.util.IsValidConfigurationValueRule;

public class ConfigurationOption extends BusinessObject implements HelpDisplayer {
  private static final long serialVersionUID = -8373897294315835558L;
  
  public static final String CONFIG_KEY_HELP_SUFFIX = "_DESC";
  
  private String configTopic = "";
  
  private String configKey = "";
  
  private String configType = "";
  
  private Object configValue;
  
  private boolean isEditable = true;
  
  private DeviceType deviceType;
  
  public ConfigurationOption(String paramString) {
    this.configKey = paramString;
  }
  
  public String getConfigTopic() {
    return this.configTopic;
  }
  
  public void setConfigTopic(String paramString) throws BusinessException {
    checkForNullParameter("Topic", paramString);
    doSetConfigTopic(paramString);
  }
  
  public void doSetConfigTopic(String paramString) {
    this.configTopic = paramString;
  }
  
  public String getConfigKey() {
    return this.configKey;
  }
  
  public void setConfigKey(String paramString) throws BusinessException {
    checkForNullParameter("Key", paramString);
    doSetConfigKey(paramString);
  }
  
  public void doSetConfigKey(String paramString) {
    this.configKey = paramString;
  }
  
  public String getConfigType() {
    return this.configType;
  }
  
  public void setConfigType(String paramString) throws BusinessException {
    checkForNullParameter("Type", paramString);
    doSetConfigType(paramString);
  }
  
  public void doSetConfigType(String paramString) {
    this.configType = paramString;
  }
  
  public Object getConfigValue() {
    return this.configValue;
  }
  
  public void setConfigValue(Object paramObject) throws BusinessException {
    checkForNullParameter("Value", paramObject);
    IsValidConfigurationValueRule.execute(this, paramObject);
    executeRule("setConfigValue", new Object[] { paramObject });
    doSetConfigValue(paramObject);
  }
  
  public void doSetConfigValue(Object paramObject) {
    this.configValue = paramObject;
  }
  
  public ConfigurationOption getValue() {
    return this;
  }
  
  public void setValue(Object paramObject) throws BusinessException {
    setConfigValue(paramObject);
  }
  
  public boolean isEditable() {
    return this.isEditable;
  }
  
  public void setEditable(boolean paramBoolean) {
    this.isEditable = paramBoolean;
  }
  
  public DeviceType getDeviceType() {
    return this.deviceType;
  }
  
  public void setDeviceType(DeviceType paramDeviceType) throws BusinessException {
    executeRule("setDeviceType", new Object[] { paramDeviceType });
    doSetDeviceType(paramDeviceType);
  }
  
  public void doSetDeviceType(DeviceType paramDeviceType) {
    this.deviceType = paramDeviceType;
  }
  
  public boolean isPropertyModifiable(String paramString) {
    return this.isEditable;
  }
  
  public String toHelpString() {
    return this.configKey + "_DESC";
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("ConfigurationOption: key(");
    stringBuilder.append(getConfigKey());
    stringBuilder.append(") ConfigType(");
    stringBuilder.append(getConfigType());
    stringBuilder.append(") ConfigTopic(");
    stringBuilder.append(getConfigTopic());
    stringBuilder.append(") ConfigValue(");
    stringBuilder.append(getConfigValue());
    stringBuilder.append(") DeviceType(");
    stringBuilder.append(getDeviceType()).append(")");
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\config\ConfigurationOption.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */