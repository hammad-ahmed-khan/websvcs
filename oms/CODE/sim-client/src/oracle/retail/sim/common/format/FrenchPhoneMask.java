package oracle.retail.sim.common.format;

import java.util.Locale;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.telephone.Telephone;

public class FrenchPhoneMask extends BasicPhoneMask {
  public FrenchPhoneMask() {
    super(Locale.FRANCE);
  }
  
  public String formatData(Object paramObject) {
    if (paramObject instanceof Telephone) {
      Telephone telephone = (Telephone)paramObject;
      String str = telephone.getTelephoneNumber();
      if (StringHelper.isNullOrEmpty(str))
        return ""; 
      int i = str.length();
      if (i < 3)
        return str; 
      String[] arrayOfString = new String[7];
      if (i < 5) {
        arrayOfString[1] = str.substring(i - 2);
        arrayOfString[0] = str.substring(0, i - 2);
      } else if (i < 7) {
        arrayOfString[2] = str.substring(i - 2);
        arrayOfString[1] = str.substring(i - 4, i - 2);
        arrayOfString[0] = str.substring(0, i - 4);
      } else if (i < 9) {
        arrayOfString[3] = str.substring(i - 2);
        arrayOfString[2] = str.substring(i - 4, i - 2);
        arrayOfString[1] = str.substring(i - 6, i - 4);
        arrayOfString[0] = str.substring(0, i - 6);
      } else if (i < 11) {
        arrayOfString[4] = str.substring(i - 2);
        arrayOfString[3] = str.substring(i - 4, i - 2);
        arrayOfString[2] = str.substring(i - 6, i - 4);
        arrayOfString[1] = str.substring(i - 8, i - 6);
        arrayOfString[0] = str.substring(0, i - 8);
        arrayOfString[0] = processText(arrayOfString[0]);
      } else if (i < 13) {
        arrayOfString[5] = str.substring(i - 2);
        arrayOfString[4] = str.substring(i - 4, i - 2);
        arrayOfString[3] = str.substring(i - 6, i - 4);
        arrayOfString[2] = str.substring(i - 8, i - 6);
        arrayOfString[1] = str.substring(i - 10, i - 8);
        arrayOfString[0] = str.substring(0, i - 10);
        arrayOfString[1] = processText(arrayOfString[1]);
      } else if (i < 16) {
        arrayOfString[6] = str.substring(i - 2);
        arrayOfString[5] = str.substring(i - 4, i - 2);
        arrayOfString[4] = str.substring(i - 6, i - 4);
        arrayOfString[3] = str.substring(i - 8, i - 6);
        arrayOfString[2] = str.substring(i - 10, i - 8);
        arrayOfString[1] = str.substring(i - 12, i - 10);
        arrayOfString[0] = str.substring(0, i - 12);
        arrayOfString[2] = processText(arrayOfString[2]);
      } 
      StringBuilder stringBuilder = new StringBuilder();
      boolean bool = false;
      for (String str1 : arrayOfString) {
        if (str1 != null) {
          if (bool)
            stringBuilder.append(" "); 
          stringBuilder.append(str1);
          bool = true;
        } 
      } 
      return stringBuilder.toString();
    } 
    return "";
  }
  
  private String processText(String paramString) {
    return (paramString.length() == 1) ? paramString : ("(" + paramString.substring(0, 1) + ")" + paramString.substring(1));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\FrenchPhoneMask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */