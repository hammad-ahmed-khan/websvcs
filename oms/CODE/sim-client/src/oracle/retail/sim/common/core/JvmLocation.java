package oracle.retail.sim.common.core;

public enum JvmLocation implements SimEnum<String> {
  CLIENT, SERVER;
  
  private static JvmLocation location;
  
  public String getCode() {
    return name();
  }
  
  public static boolean isClient() {
    return (location == CLIENT);
  }
  
  public static boolean isServer() {
    return (location == SERVER);
  }
  
  public static void setClient() {
    location = CLIENT;
  }
  
  public static void setServer() {
    location = SERVER;
  }
  
  static {
    location = SERVER;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\JvmLocation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */