package oracle.retail.sim.common.format;

import java.util.Currency;
import java.util.Locale;
import oracle.retail.sim.common.configutil.CommonConfigManager;

public class MoneyMaskFactory {
  private static MoneyMaskFactoryInterface factory = getDefaultFactory();
  
  private static MoneyMaskFactoryInterface getDefaultFactory() {
    return CommonConfigManager.getMoneyMaskFactory();
  }
  
  public static MoneyMask createMoneyMask(Locale paramLocale) {
    try {
      return factory.createMoneyMask(paramLocale);
    } catch (Throwable throwable) {
      return new MoneyMask(paramLocale, null, true);
    } 
  }
  
  public static MoneyMask createMoneyMask(Locale paramLocale, boolean paramBoolean) {
    try {
      return factory.createMoneyMask(paramLocale, paramBoolean);
    } catch (Throwable throwable) {
      return new MoneyMask(paramLocale, null, paramBoolean);
    } 
  }
  
  public static MoneyMask createMoneyMask(Locale paramLocale, Currency paramCurrency) {
    try {
      return factory.createMoneyMask(paramLocale, paramCurrency);
    } catch (Throwable throwable) {
      return new MoneyMask(paramLocale, paramCurrency, true);
    } 
  }
  
  public static MoneyMask createMoneyMask(Locale paramLocale, Currency paramCurrency, boolean paramBoolean) {
    try {
      return factory.createMoneyMask(paramLocale, paramCurrency, paramBoolean);
    } catch (Throwable throwable) {
      return new MoneyMask(paramLocale, paramCurrency, paramBoolean);
    } 
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\MoneyMaskFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */