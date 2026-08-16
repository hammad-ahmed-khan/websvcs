package oracle.retail.sim.common.source;

import oracle.retail.sim.common.core.SimEnum;

public enum PartnerType implements SimEnum<String> {
  FINISHER("FI");
  
  private final String code;
  
  PartnerType(String paramString1) {
    this.code = paramString1;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public String toString() {
    return this.code;
  }
  
  public static PartnerType toValue(String paramString) {
    if (paramString != null)
      for (PartnerType partnerType : values()) {
        if (partnerType.code.equals(paramString))
          return partnerType; 
      }  
    throw new IllegalArgumentException("invalid code: " + paramString);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\PartnerType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */