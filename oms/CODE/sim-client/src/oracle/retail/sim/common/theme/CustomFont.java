package oracle.retail.sim.common.theme;

import java.io.Serializable;

public class CustomFont implements Serializable {
  private static final long serialVersionUID = 8364822428754395468L;
  
  private String key = null;
  
  private String family = null;
  
  private int style = 0;
  
  private int size = 0;
  
  public CustomFont(String paramString) {
    this.key = paramString;
  }
  
  public String getKey() {
    return this.key;
  }
  
  public String getFamily() {
    return this.family;
  }
  
  public void setFamily(String paramString) {
    this.family = paramString;
  }
  
  public int getStyle() {
    return this.style;
  }
  
  public void setStyle(int paramInt) {
    this.style = paramInt;
  }
  
  public int getSize() {
    return this.size;
  }
  
  public void setSize(int paramInt) {
    this.size = paramInt;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\theme\CustomFont.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */