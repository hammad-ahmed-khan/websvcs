package oracle.retail.sim.common.core.locale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateFormatHelper {
  public static boolean isValidShortDate(String paramString, DateFormat paramDateFormat) {
    if (StringHelper.isNullOrEmpty(paramString))
      return true; 
    if (paramDateFormat instanceof SimpleDateFormat) {
      SimpleDateFormat simpleDateFormat = (SimpleDateFormat)paramDateFormat;
      String str1 = simpleDateFormat.getDateFormatSymbols().getLocalPatternChars();
      String str2 = simpleDateFormat.toLocalizedPattern();
      char[] arrayOfChar1 = str1.toCharArray();
      char[] arrayOfChar2 = str2.toCharArray();
      char[] arrayOfChar3 = paramString.toCharArray();
      boolean bool = true;
      for (char c : arrayOfChar3) {
        if (!Character.isDigit(c)) {
          for (char c1 : arrayOfChar1) {
            if (c == c1)
              return false; 
          } 
          bool = true;
          for (char c1 : arrayOfChar2) {
            if (c == c1) {
              bool = false;
              break;
            } 
          } 
          if (bool)
            return false; 
        } 
      } 
    } 
    return true;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\locale\DateFormatHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */