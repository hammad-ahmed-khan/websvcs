package oracle.retail.sim.common.integration;

public enum CoreSimMessageFamily implements SimMessageFamily {
  NOTIFICATION("Notification");
  
  private final String code;
  
  CoreSimMessageFamily(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.code;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\integration\CoreSimMessageFamily.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */