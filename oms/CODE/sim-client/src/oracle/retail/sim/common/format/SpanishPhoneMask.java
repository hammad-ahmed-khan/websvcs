package oracle.retail.sim.common.format;

import java.util.Locale;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.telephone.Telephone;

public class SpanishPhoneMask extends BasicPhoneMask {
  public SpanishPhoneMask() {
    super(new Locale("es"));
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
      String[] arrayOfString = new String[4];
      if (i < 7) {
        arrayOfString[0] = str.substring(i - 4);
        arrayOfString[1] = str.substring(0, i - 4);
      } else if (i < 9) {
        arrayOfString[0] = str.substring(i - 4);
        arrayOfString[1] = str.substring(i - 7, i - 4);
        arrayOfString[2] = str.substring(0, i - 7);
      } else {
        arrayOfString[0] = str.substring(i - 4);
        arrayOfString[1] = str.substring(i - 7, i - 4);
        arrayOfString[2] = str.substring(i - 9, i - 7);
        arrayOfString[3] = str.substring(0, i - 9);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      if (!StringHelper.isNullOrEmpty(arrayOfString[3])) {
        stringBuilder.append(arrayOfString[3]);
        stringBuilder.append(" ");
      } 
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


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\SpanishPhoneMask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */