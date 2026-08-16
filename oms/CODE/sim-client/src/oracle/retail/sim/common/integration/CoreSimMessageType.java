package oracle.retail.sim.common.integration;

public enum CoreSimMessageType implements SimMessageType {
  EMAIL_NOTIFICATION("EmailNotification", CoreSimMessageFamily.NOTIFICATION);
  
  private final String code;
  
  private final SimMessageFamily family;
  
  CoreSimMessageType(String paramString1, SimMessageFamily paramSimMessageFamily) {
    this.code = paramString1;
    this.family = paramSimMessageFamily;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public SimMessageFamily getFamily() {
    return this.family;
  }
  
  public String toString() {
    return this.code;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\integration\CoreSimMessageType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */