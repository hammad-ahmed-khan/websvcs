package oracle.retail.sim.common.source;

import oracle.retail.sim.common.core.SimEnum;

public enum SourceType implements SimEnum<Integer> {
  SUPPLIER(1, "S", "Supplier"),
  WAREHOUSE(2, "W", "Warehouse"),
  FINISHER(3, "F", "Finisher");
  
  private final int code;
  
  private final String externalCode;
  
  private final String description;
  
  SourceType(int paramInt1, String paramString1, String paramString2) {
    this.code = paramInt1;
    this.description = paramString2;
    this.externalCode = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String getExternalCode() {
    return this.externalCode;
  }
  
  public String toString() {
    return this.description;
  }
  
  public static SourceType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (SourceType sourceType : values()) {
        if (sourceType.code == paramInteger.intValue())
          return sourceType; 
      }  
    return null;
  }
  
  public static SourceType toValue(String paramString) {
    if (paramString != null)
      for (SourceType sourceType : values()) {
        if (sourceType.externalCode.equals(paramString))
          return sourceType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\SourceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */