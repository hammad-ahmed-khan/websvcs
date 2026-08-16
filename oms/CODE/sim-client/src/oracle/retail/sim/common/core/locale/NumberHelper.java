package oracle.retail.sim.common.core.locale;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import oracle.retail.sim.common.core.UniversalContext;

public class NumberHelper {
  private DecimalFormatSymbols decimalSymbols;
  
  public NumberHelper(Locale paramLocale) {
    this.decimalSymbols = ((DecimalFormat)NumberFormat.getNumberInstance(paramLocale)).getDecimalFormatSymbols();
  }
  
  public static NumberHelper getInstance() {
    return new NumberHelper(UniversalContext.getLocale());
  }
  
  public static NumberHelper getInstance(Locale paramLocale) {
    return new NumberHelper(paramLocale);
  }
  
  public static boolean isIdentifierNumeric(String paramString) {
    if (StringHelper.isNullOrEmpty(paramString))
      return false; 
    char[] arrayOfChar = paramString.toCharArray();
    for (char c : arrayOfChar) {
      if (c < '0' || c > '9')
        return false; 
    } 
    return true;
  }
  
  public boolean hasDecimalSeparator(String paramString) {
    if (StringHelper.isNullOrEmpty(paramString))
      return false; 
    char c = this.decimalSymbols.getDecimalSeparator();
    for (char c1 : paramString.toCharArray()) {
      if (c1 == c)
        return true; 
    } 
    return false;
  }
  
  public boolean isValidIntegerInput(String paramString) {
    if (StringHelper.isNullOrEmpty(paramString))
      return false; 
    char[] arrayOfChar = paramString.toCharArray();
    if (arrayOfChar.length == 1)
      return Character.isDigit(arrayOfChar[0]); 
    byte b1 = 0;
    if (arrayOfChar[0] == this.decimalSymbols.getMinusSign())
      b1 = 1; 
    for (byte b2 = b1; b2 < arrayOfChar.length; b2++) {
      if (!Character.isDigit(arrayOfChar[b2]))
        return false; 
    } 
    return true;
  }
  
  public boolean isNumericOrDecimal(String paramString) {
    if (StringHelper.isNullOrEmpty(paramString))
      return false; 
    char[] arrayOfChar = paramString.toCharArray();
    if (arrayOfChar.length == 1)
      return Character.isDigit(arrayOfChar[0]); 
    byte b1 = 0;
    if (arrayOfChar[0] == this.decimalSymbols.getMinusSign())
      b1 = 1; 
    char c1 = this.decimalSymbols.getDecimalSeparator();
    char c2 = this.decimalSymbols.getMonetaryDecimalSeparator();
    char c3 = this.decimalSymbols.getGroupingSeparator();
    char c4 = this.decimalSymbols.getPercent();
    boolean bool1 = false;
    boolean bool2 = false;
    for (byte b2 = b1; b2 < arrayOfChar.length; b2++) {
      if (arrayOfChar[b2] == c1) {
        if (bool1)
          return false; 
        bool1 = true;
      } else if (arrayOfChar[b2] == c4) {
        if (bool2)
          return false; 
        bool2 = true;
      } else if (arrayOfChar[b2] != c3 && arrayOfChar[b2] != c2 && !Character.isDigit(arrayOfChar[b2])) {
        return false;
      } 
    } 
    return true;
  }
  
  public boolean isValidDecimalInput(String paramString) {
    if (StringHelper.isNullOrEmpty(paramString))
      return false; 
    char[] arrayOfChar = paramString.toCharArray();
    if (arrayOfChar.length == 1)
      return Character.isDigit(arrayOfChar[0]); 
    byte b1 = 0;
    if (arrayOfChar[0] == this.decimalSymbols.getMinusSign())
      b1 = 1; 
    char c = this.decimalSymbols.getDecimalSeparator();
    boolean bool = false;
    for (byte b2 = b1; b2 < arrayOfChar.length; b2++) {
      if (arrayOfChar[b2] == c) {
        if (bool)
          return false; 
        bool = true;
      } else if (!Character.isDigit(arrayOfChar[b2])) {
        return false;
      } 
    } 
    return true;
  }
  
  public boolean isValidPercentInput(String paramString) {
    if (StringHelper.isNullOrEmpty(paramString))
      return false; 
    char[] arrayOfChar = paramString.toCharArray();
    if (arrayOfChar.length == 1)
      return Character.isDigit(arrayOfChar[0]); 
    byte b1 = 0;
    if (arrayOfChar[0] == this.decimalSymbols.getMinusSign())
      b1 = 1; 
    char c1 = this.decimalSymbols.getDecimalSeparator();
    char c2 = this.decimalSymbols.getPercent();
    boolean bool1 = false;
    boolean bool2 = false;
    for (byte b2 = b1; b2 < arrayOfChar.length; b2++) {
      if (arrayOfChar[b2] == c1) {
        if (bool1)
          return false; 
        bool1 = true;
      } else if (arrayOfChar[b2] == c2) {
        if (bool2)
          return false; 
        bool2 = true;
      } else if (!Character.isWhitespace(arrayOfChar[b2]) && !Character.isDigit(arrayOfChar[b2])) {
        return false;
      } 
    } 
    return true;
  }
  
  public String collapseToInteger(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    char[] arrayOfChar = paramString.toCharArray();
    char c1 = this.decimalSymbols.getMinusSign();
    char c2 = this.decimalSymbols.getZeroDigit();
    boolean bool = true;
    for (char c : arrayOfChar) {
      if (c == c1 && bool && stringBuilder.length() == 0) {
        stringBuilder.append(c);
        bool = false;
      } else if (Character.isDigit(c)) {
        stringBuilder.append(c);
      } 
    } 
    if (stringBuilder.length() < 1)
      stringBuilder.append(c2); 
    return stringBuilder.toString();
  }
  
  public String collapseToDecimal(String paramString) {
    StringBuilder stringBuilder = new StringBuilder();
    boolean bool1 = true;
    boolean bool2 = true;
    char c1 = this.decimalSymbols.getMinusSign();
    char c2 = this.decimalSymbols.getDecimalSeparator();
    char c3 = this.decimalSymbols.getZeroDigit();
    for (byte b = 0; b < paramString.length(); b++) {
      char c = paramString.charAt(b);
      if (c == c1 && bool1 && stringBuilder.length() == 0) {
        stringBuilder.append(c);
        bool1 = false;
      } else if (c == c2 && bool2) {
        stringBuilder.append(c);
        bool2 = false;
      } else if (Character.isDigit(c)) {
        stringBuilder.append(c);
      } 
    } 
    if (stringBuilder.length() < 1)
      stringBuilder.append(c3); 
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\locale\NumberHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */