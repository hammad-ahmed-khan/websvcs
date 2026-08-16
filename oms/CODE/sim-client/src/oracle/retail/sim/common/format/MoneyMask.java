package oracle.retail.sim.common.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.locale.NumberParser;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.logging.LogService;

public class MoneyMask extends MaskAdaptor {
  protected boolean returnBlankValue;
  
  private boolean negativeValueAllowed = true;
  
  private double maxDoubleValue = 9.999999999999E10D;
  
  private double minDoubleValue = -9.99999999999E9D;
  
  private double zeroDoubleValue = 0.0D;
  
  private BigDecimal maximumValue = BigDecimal.valueOf(this.maxDoubleValue);
  
  private BigDecimal minimumValue = BigDecimal.valueOf(this.minDoubleValue);
  
  private BigDecimal zeroValue = BigDecimal.valueOf(this.zeroDoubleValue);
  
  private StringHelper stringFormatter = null;
  
  private DecimalFormat currencyFormatter = null;
  
  private NumberParser numberParser = null;
  
  private char[] minus = new char[1];
  
  public MoneyMask(Locale paramLocale, Currency paramCurrency, boolean paramBoolean) {
    if (paramLocale == null)
      paramLocale = Locale.US; 
    if (paramCurrency == null)
      try {
        paramCurrency = Currency.getInstance(paramLocale);
      } catch (Throwable throwable) {
        paramCurrency = Currency.getInstance(Locale.US);
      }  
    this.returnBlankValue = paramBoolean;
    this.stringFormatter = StringHelper.getInstance(paramLocale);
    this.currencyFormatter = (DecimalFormat)NumberFormat.getCurrencyInstance(paramLocale);
    this.currencyFormatter.setCurrency(paramCurrency);
    this.currencyFormatter.setGroupingUsed(false);
    this.numberParser = NumberParser.getInstance(paramLocale);
  }
  
  public Currency getCurrency() {
    return this.currencyFormatter.getCurrency();
  }
  
  public int getPostDecimalLength() {
    return this.currencyFormatter.getCurrency().getDefaultFractionDigits();
  }
  
  public char getDecimalSeparator() {
    return getDecimalFormatSymbols().getDecimalSeparator();
  }
  
  public char getGroupingSeparator() {
    return getDecimalFormatSymbols().getGroupingSeparator();
  }
  
  public String getZeroString() {
    return this.currencyFormatter.format(0L);
  }
  
  public String getMinusString() {
    this.minus[0] = getDecimalFormatSymbols().getMinusSign();
    return new String(this.minus);
  }
  
  public String getCurrencySymbol() {
    return this.currencyFormatter.getCurrency().getSymbol();
  }
  
  public String getLocaleCurrencySymbol() {
    return getDecimalFormatSymbols().getCurrencySymbol();
  }
  
  public BigDecimal getMaximumValue() {
    return this.maximumValue;
  }
  
  public void setMaximumValue(BigDecimal paramBigDecimal) {
    if (paramBigDecimal != null)
      this.maximumValue = paramBigDecimal; 
  }
  
  public BigDecimal getMinimumValue() {
    return this.minimumValue;
  }
  
  public void setMinimumValue(BigDecimal paramBigDecimal) {
    if (paramBigDecimal != null)
      this.minimumValue = paramBigDecimal; 
  }
  
  public boolean isNegativeValueAllowed() {
    return this.negativeValueAllowed;
  }
  
  public void setNegativeValueAllowed(boolean paramBoolean) {
    this.negativeValueAllowed = paramBoolean;
  }
  
  private DecimalFormatSymbols getDecimalFormatSymbols() {
    return this.currencyFormatter.getDecimalFormatSymbols();
  }
  
  public String formatData(Object paramObject) {
    if (paramObject == null)
      return this.returnBlankValue ? "" : getZeroString(); 
    try {
      return this.currencyFormatter.format(paramObject);
    } catch (Throwable throwable) {
      return paramObject.toString();
    } 
  }
  
  public String format(String paramString) {
    if (StringHelper.isNullOrEmpty(paramString)) {
      if (this.returnBlankValue)
        return ""; 
      paramString = getZeroString();
    } 
    if (this.stringFormatter.hasMultiplesOfChar(paramString, getDecimalSeparator()))
      return paramString; 
    try {
      return this.currencyFormatter.format(this.numberParser.getCurrency(paramString));
    } catch (Exception exception) {
      LogService.debug(this, exception.getLocalizedMessage());
      return paramString;
    } 
  }
  
  public String unformat(String paramString) {
    DecimalFormatSymbols decimalFormatSymbols = this.currencyFormatter.getDecimalFormatSymbols();
    char[] arrayOfChar1 = { decimalFormatSymbols.getZeroDigit() };
    char[] arrayOfChar2 = { decimalFormatSymbols.getGroupingSeparator() };
    if (StringHelper.isNullOrEmpty(paramString))
      return this.returnBlankValue ? "" : new String(arrayOfChar1); 
    if (this.stringFormatter.indexOf(paramString, this.currencyFormatter.getNegativePrefix()) > -1) {
      paramString = this.stringFormatter.replace(paramString, this.currencyFormatter.getNegativePrefix(), getMinusString());
      paramString = this.stringFormatter.replace(paramString, this.currencyFormatter.getNegativeSuffix(), "");
    } else {
      paramString = this.stringFormatter.replace(paramString, decimalFormatSymbols.getCurrencySymbol(), "");
    } 
    paramString = this.stringFormatter.replace(paramString, new String(arrayOfChar2), "").trim();
    return (paramString.length() > 0) ? paramString : (this.returnBlankValue ? "" : new String(arrayOfChar1));
  }
  
  public BusinessException validate(String paramString) {
    if (StringHelper.isNullOrEmpty(paramString))
      return null; 
    try {
      if (this.stringFormatter.hasMultiplesOfChar(paramString, getDecimalSeparator()))
        return new BusinessException((MessageText)CommonMessageText.CURRENCY_INVALID_FORMAT); 
      String str1 = this.stringFormatter.toUpperCase(paramString);
      String str2 = this.currencyFormatter.getCurrency().getCurrencyCode();
      if (this.stringFormatter.startsWith(str1, str2))
        paramString = this.stringFormatter.replace(str1, str2, ""); 
      BigDecimal bigDecimal = this.numberParser.getCurrency(paramString);
      if (bigDecimal.compareTo(this.maximumValue) > 0) {
        Object[] arrayOfObject = { this.currencyFormatter.format(this.maximumValue) };
        return new BusinessException((MessageText)CommonMessageText.CURRENCY_INVALID_MAX_SIZE, arrayOfObject);
      } 
      if (bigDecimal.compareTo(this.minimumValue) < 0) {
        Object[] arrayOfObject = { this.currencyFormatter.format(this.maximumValue) };
        return new BusinessException((MessageText)CommonMessageText.CURRENCY_INVALID_MIN_SIZE, arrayOfObject);
      } 
      if (bigDecimal.compareTo(this.zeroValue) < 0 && !this.negativeValueAllowed)
        return new BusinessException((MessageText)CommonMessageText.CURRENCY_INVALID_SIGN); 
    } catch (ParseException parseException) {
      return new BusinessException((MessageText)CommonMessageText.CURRENCY_INVALID_FORMAT);
    } 
    return null;
  }
  
  public boolean validCharacter(char paramChar) {
    if (Character.isDigit(paramChar))
      return true; 
    DecimalFormatSymbols decimalFormatSymbols = getDecimalFormatSymbols();
    char[] arrayOfChar1 = decimalFormatSymbols.getCurrencySymbol().toCharArray();
    for (char c : arrayOfChar1) {
      if (paramChar == c)
        return true; 
    } 
    char[] arrayOfChar2 = this.currencyFormatter.getCurrency().getCurrencyCode().toCharArray();
    for (char c : arrayOfChar2) {
      if (paramChar == c)
        return true; 
    } 
    return (paramChar == decimalFormatSymbols.getMinusSign()) ? true : ((paramChar == decimalFormatSymbols.getMonetaryDecimalSeparator()));
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\MoneyMask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */