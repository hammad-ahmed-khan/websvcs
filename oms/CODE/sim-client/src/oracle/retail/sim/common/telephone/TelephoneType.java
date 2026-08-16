package oracle.retail.sim.common.telephone;

import oracle.retail.sim.common.core.SimEnum;

public enum TelephoneType implements SimEnum<String> {
  WORK("WORK"),
  FAX("FAX"),
  VOICE("VOICE"),
  STORE("STORE"),
  STORE_ALT("STORE_ALT"),
  STORE_FAX("STORE_FAX");
  
  private final String code;
  
  TelephoneType(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public static TelephoneType toValue(String paramString) {
    if (paramString != null)
      for (TelephoneType telephoneType : values()) {
        if (telephoneType.code.equals(paramString))
          return telephoneType; 
      }  
    return VOICE;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\telephone\TelephoneType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */