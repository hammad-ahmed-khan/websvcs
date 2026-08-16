package oracle.retail.sim.common.format;

import java.util.Locale;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.telephone.Telephone;

public class ChineseSimpPhoneMask extends BasicPhoneMask {
  public ChineseSimpPhoneMask() {
    super(Locale.SIMPLIFIED_CHINESE);
  }
  
  public String formatData(Object paramObject) {
    if (paramObject instanceof Telephone) {
      Telephone telephone = (Telephone)paramObject;
      String str = telephone.getTelephoneNumber();
      if (StringHelper.isNullOrEmpty(str))
        return ""; 
      int i = str.length();
      if (i < 9)
        return str; 
      String[] arrayOfString = new String[4];
      if (i < 11) {
        arrayOfString[0] = str.substring(i - 8);
        arrayOfString[1] = str.substring(0, i - 8);
      } else if (i < 13) {
        arrayOfString[0] = str.substring(i - 8);
        arrayOfString[1] = str.substring(i - 10, i - 8);
        arrayOfString[2] = str.substring(0, i - 10);
      } else {
        arrayOfString[0] = str.substring(i - 8);
        arrayOfString[1] = str.substring(i - 10, i - 8);
        arrayOfString[2] = str.substring(i - 12, i - 10);
        arrayOfString[3] = str.substring(0, i - 12);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      if (!StringHelper.isNullOrEmpty(arrayOfString[3]))
        stringBuilder.append(arrayOfString[3]).append(" "); 
      if (!StringHelper.isNullOrEmpty(arrayOfString[2]))
        stringBuilder.append(arrayOfString[2]).append(" "); 
      if (!StringHelper.isNullOrEmpty(arrayOfString[1]))
        stringBuilder.append(arrayOfString[1]).append(" "); 
      stringBuilder.append(arrayOfString[0]);
      return stringBuilder.toString();
    } 
    return "";
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\ChineseSimpPhoneMask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */