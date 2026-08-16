package oracle.retail.sim.common.core.locale;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberParser {
  private Locale locale = Locale.US;
  
  public NumberParser(Locale paramLocale) {
    if (paramLocale != null)
      this.locale = paramLocale; 
  }
  
  public static NumberParser getInstance(Locale paramLocale) {
    return new NumberParser(paramLocale);
  }
  
  public int getIntValue(String paramString) throws ParseException {
    return parseNumber(paramString).intValue();
  }
  
  public Integer getInteger(String paramString) throws ParseException {
    return Integer.valueOf(parseNumber(paramString).intValue());
  }
  
  public long getLongValue(String paramString) throws ParseException {
    return parseNumber(paramString).longValue();
  }
  
  public Long getLong(String paramString) throws ParseException {
    return Long.valueOf(parseNumber(paramString).longValue());
  }
  
  public Long getLongOrNull(String paramString) throws ParseException {
    return StringHelper.isNullOrEmpty(paramString) ? null : getLong(paramString);
  }
  
  public double getDoubleValue(String paramString) throws ParseException {
    return parseNumber(paramString).doubleValue();
  }
  
  public Double getDouble(String paramString) throws ParseException {
    return Double.valueOf(parseNumber(paramString).doubleValue());
  }
  
  public float getFloatValue(String paramString) throws ParseException {
    return parseNumber(paramString).floatValue();
  }
  
  public Float getFloat(String paramString) throws ParseException {
    return Float.valueOf(parseNumber(paramString).floatValue());
  }
  
  public BigInteger getBigInteger(String paramString) throws ParseException {
    return getBigDecimal(paramString).toBigInteger();
  }
  
  public BigDecimal getBigDecimal(String paramString) throws ParseException {
    return BigDecimal.valueOf(parseNumber(paramString).doubleValue());
  }
  
  public BigDecimal getCurrency(String paramString) throws ParseException {
    BigDecimal bigDecimal = BigDecimal.valueOf(parseCurrency(paramString).doubleValue());
    return bigDecimal.setScale(6, RoundingMode.HALF_UP);
  }
  
  private Number parseNumber(String paramString) throws ParseException {
    char[] arrayOfChar = new char[0];
    if (paramString != null)
      arrayOfChar = paramString.toCharArray(); 
    StringBuilder stringBuilder = new StringBuilder();
    for (char c : arrayOfChar) {
      if (!Character.isWhitespace(c))
        stringBuilder.append(c); 
    } 
    DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance(this.locale);
    decimalFormat.setGroupingUsed(false);
    if (stringBuilder.length() < 1)
      stringBuilder.append(decimalFormat.getDecimalFormatSymbols().getZeroDigit()); 
    return decimalFormat.parse(stringBuilder.toString());
  }
  
  private Number parseCurrency(String paramString) throws ParseException {
    DecimalFormat decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance(this.locale);
    decimalFormat.setGroupingUsed(false);
    DecimalFormatSymbols decimalFormatSymbols = decimalFormat.getDecimalFormatSymbols();
    String str1 = decimalFormatSymbols.getCurrencySymbol();
    StringBuilder stringBuilder = new StringBuilder();
    if (StringHelper.isNullOrEmpty(paramString)) {
      stringBuilder.append(str1);
      stringBuilder.append(decimalFormatSymbols.getZeroDigit());
      return decimalFormat.parse(stringBuilder.toString());
    } 
    char[] arrayOfChar = new char[1];
    arrayOfChar[0] = decimalFormatSymbols.getMinusSign();
    String str2 = new String(arrayOfChar);
    StringHelper stringHelper = StringHelper.getInstance(this.locale);
    boolean bool = (stringHelper.indexOf(paramString, str2) > -1) ? true : false;
    paramString = stringHelper.replace(paramString, str1, "");
    if (bool) {
      stringBuilder.append(decimalFormat.getNegativePrefix());
      stringBuilder.append(stringHelper.replace(paramString, str2, ""));
      stringBuilder.append(decimalFormat.getNegativeSuffix());
    } else {
      stringBuilder.append(decimalFormat.getPositivePrefix());
      stringBuilder.append(paramString);
      stringBuilder.append(decimalFormat.getPositiveSuffix());
    } 
    return decimalFormat.parse(stringBuilder.toString());
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\locale\NumberParser.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */