package oracle.retail.sim.common.theme;

import oracle.retail.sim.common.core.SimEnum;

public enum CustomThemeType implements SimEnum<Integer> {
  FONT(0, "Font"),
  COLOR(1, "Color"),
  ICON(2, "Icon");
  
  private final int code;
  
  private final String description;
  
  CustomThemeType(int paramInt1, String paramString1) {
    this.code = paramInt1;
    this.description = paramString1;
  }
  
  public Integer getCode() {
    return Integer.valueOf(this.code);
  }
  
  public String toString() {
    return this.description;
  }
  
  public static CustomThemeType toValue(Integer paramInteger) {
    if (paramInteger != null)
      for (CustomThemeType customThemeType : values()) {
        if (customThemeType.code == paramInteger.intValue())
          return customThemeType; 
      }  
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\theme\CustomThemeType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */