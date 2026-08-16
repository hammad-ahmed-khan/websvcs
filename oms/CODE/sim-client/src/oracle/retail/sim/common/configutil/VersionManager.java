package oracle.retail.sim.common.configutil;

public class VersionManager {
  public static final String SIM_VERSION_MAJOR = "sim.version.major";
  
  public static final String SIM_VERSION_MINOR = "sim.version.minor";
  
  public static final String SIM_VERSION_POINT = "sim.version.point";
  
  public static final String SIM_VERSION_HOTFIX = "sim.version.hotfix";
  
  public static final String SIM_VERSION_BUILD = "sim.version.build";
  
  public static final String SIM_VERSION_DATE = "sim.version.date";
  
  private static String MAJOR;
  
  private static String MINOR;
  
  private static String POINT;
  
  private static String HOTFIX;
  
  private static String BUILD;
  
  private static String DATE;
  
  public static String getVersion(boolean paramBoolean1, boolean paramBoolean2) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(MAJOR).append(".").append(MINOR).append(".").append(POINT).append(".").append(HOTFIX);
    if (paramBoolean2 && BUILD != null && BUILD.length() > 0)
      stringBuilder.append(" build ").append(BUILD); 
    if (paramBoolean1 && DATE != null && DATE.length() > 0)
      stringBuilder.append(" (").append(DATE).append(")"); 
    return stringBuilder.toString();
  }
  
  static {
    ConfigManager configManager = new ConfigManager("version.properties");
    MAJOR = configManager.getString("sim.version.major");
    MINOR = configManager.getString("sim.version.minor");
    POINT = configManager.getString("sim.version.point");
    HOTFIX = configManager.getString("sim.version.hotfix");
    BUILD = configManager.getString("sim.version.build");
    DATE = configManager.getString("sim.version.date");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\configutil\VersionManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */