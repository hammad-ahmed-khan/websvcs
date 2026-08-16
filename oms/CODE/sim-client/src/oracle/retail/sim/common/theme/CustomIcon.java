package oracle.retail.sim.common.theme;

import java.io.Serializable;

public class CustomIcon implements Serializable {
  private static final long serialVersionUID = 5353571418253104346L;
  
  private String key = null;
  
  private String iconPath = null;
  
  public CustomIcon(String paramString1, String paramString2) {
    this.key = paramString1;
    this.iconPath = paramString2;
  }
  
  public String getKey() {
    return this.key;
  }
  
  public String getIconPath() {
    return this.iconPath;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\theme\CustomIcon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */