package oracle.retail.sim.common.format;

import oracle.retail.sim.common.business.BusinessException;

public class MaskAdaptor implements Mask {
  public String formatData(Object paramObject) {
    return (paramObject == null) ? "" : format(paramObject.toString());
  }
  
  public String format(String paramString) {
    return paramString.trim();
  }
  
  public String format(String[] paramArrayOfString) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String str : paramArrayOfString) {
      if (str != null)
        stringBuilder.append(str.trim()); 
    } 
    return format(stringBuilder.toString());
  }
  
  public String unformat(String paramString) {
    return paramString.trim();
  }
  
  public BusinessException validate(String paramString) {
    return null;
  }
  
  public boolean validCharacter(char paramChar) {
    return true;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\MaskAdaptor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */