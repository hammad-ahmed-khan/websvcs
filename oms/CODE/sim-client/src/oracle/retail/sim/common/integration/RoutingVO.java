package oracle.retail.sim.common.integration;

import java.io.Serializable;

public class RoutingVO implements Serializable {
  private static final long serialVersionUID = 1160969255357882295L;
  
  private String name;
  
  private String value;
  
  private String detail1Name;
  
  private String detail1Value;
  
  private String detail2Name;
  
  private String detail2Value;
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String paramString) {
    this.name = paramString;
  }
  
  public String getValue() {
    return this.value;
  }
  
  public void setValue(String paramString) {
    this.value = paramString;
  }
  
  public String getDetail1Name() {
    return this.detail1Name;
  }
  
  public void setDetail1Name(String paramString) {
    this.detail1Name = paramString;
  }
  
  public String getDetail1Value() {
    return this.detail1Value;
  }
  
  public void setDetail1Value(String paramString) {
    this.detail1Value = paramString;
  }
  
  public String getDetail2Name() {
    return this.detail2Name;
  }
  
  public void setDetail2Name(String paramString) {
    this.detail2Name = paramString;
  }
  
  public String getDetail2Value() {
    return this.detail2Value;
  }
  
  public void setDetail2Value(String paramString) {
    this.detail2Value = paramString;
  }
  
  public String getMessageDesc() {
    StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(this.name);
    stringBuffer.append(":").append(this.value);
    return stringBuffer.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\integration\RoutingVO.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */