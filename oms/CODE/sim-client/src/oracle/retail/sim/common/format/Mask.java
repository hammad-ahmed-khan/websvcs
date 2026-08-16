package oracle.retail.sim.common.format;

import oracle.retail.sim.common.business.BusinessException;

public interface Mask {
  String formatData(Object paramObject);
  
  String format(String paramString);
  
  String format(String[] paramArrayOfString);
  
  String unformat(String paramString);
  
  boolean validCharacter(char paramChar);
  
  BusinessException validate(String paramString);
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\Mask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */