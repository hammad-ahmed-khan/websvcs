package oracle.retail.sim.common.uda;

import oracle.retail.sim.common.core.SimEnum;

public enum UDAType implements SimEnum<String> {
  DATE("DT", "Date"),
  VALUE("LV", "Value"),
  TEXT("FF", "Text");
  
  private final String code;
  
  private final String description;
  
  UDAType(String paramString1, String paramString2) {
    this.code = paramString1;
    this.description = paramString2;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.description;
  }
  
  public static UDAType toValue(String paramString) {
    if (paramString != null)
      for (UDAType uDAType : values()) {
        if (uDAType.code.equals(paramString))
          return uDAType; 
      }  
    return TEXT;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\commo\\uda\UDAType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */