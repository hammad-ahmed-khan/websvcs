package oracle.retail.sim.common.format;

import java.util.Currency;
import java.util.Locale;

public class MoneyMaskFactoryImpl implements MoneyMaskFactoryInterface {
  public MoneyMask createMoneyMask(Locale paramLocale) {
    return new MoneyMask(paramLocale, null, true);
  }
  
  public MoneyMask createMoneyMask(Locale paramLocale, Currency paramCurrency) {
    return new MoneyMask(paramLocale, paramCurrency, true);
  }
  
  public MoneyMask createMoneyMask(Locale paramLocale, boolean paramBoolean) {
    return new MoneyMask(paramLocale, null, paramBoolean);
  }
  
  public MoneyMask createMoneyMask(Locale paramLocale, Currency paramCurrency, boolean paramBoolean) {
    return new MoneyMask(paramLocale, paramCurrency, paramBoolean);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\MoneyMaskFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */