package oracle.retail.sim.common.theme;

import java.io.Serializable;

public class CustomColor implements Serializable {
  private static final long serialVersionUID = -2359448921424056320L;
  
  private String key = null;
  
  private int red = 0;
  
  private int green = 0;
  
  private int blue = 0;
  
  public CustomColor(String paramString, int paramInt1, int paramInt2, int paramInt3) {
    this.key = paramString;
    this.red = paramInt1;
    this.green = paramInt2;
    this.blue = paramInt3;
  }
  
  public String getKey() {
    return this.key;
  }
  
  public int getRed() {
    return this.red;
  }
  
  public int getGreen() {
    return this.green;
  }
  
  public int getBlue() {
    return this.blue;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\theme\CustomColor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */