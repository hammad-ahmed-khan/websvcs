package oracle.retail.sim.common.format;

import java.util.Locale;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.telephone.Telephone;

public class GermanPhoneMask extends BasicPhoneMask {
  public GermanPhoneMask() {
    super(Locale.GERMANY);
  }
  
  public String formatData(Object paramObject) {
    if (paramObject instanceof Telephone) {
      Telephone telephone = (Telephone)paramObject;
      String str = telephone.getTelephoneNumber();
      if (StringHelper.isNullOrEmpty(str))
        return ""; 
      int i = str.length();
      if (i < 5)
        return str; 
      String[] arrayOfString = new String[3];
      if (i < 8) {
        arrayOfString[0] = str.substring(i - 4);
        arrayOfString[1] = str.substring(0, i - 4);
      } else {
        arrayOfString[0] = str.substring(i - 4);
        arrayOfString[1] = str.substring(i - 8, i - 4);
        arrayOfString[2] = str.substring(0, i - 8);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      if (!StringHelper.isNullOrEmpty(arrayOfString[2])) {
        stringBuilder.append(arrayOfString[2]);
        stringBuilder.append(" ");
      } 
      stringBuilder.append(arrayOfString[1]);
      stringBuilder.append(" ");
      stringBuilder.append(arrayOfString[0]);
      return stringBuilder.toString();
    } 
    return "";
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\GermanPhoneMask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */