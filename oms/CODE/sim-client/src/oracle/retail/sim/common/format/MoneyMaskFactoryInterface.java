package oracle.retail.sim.common.format;

import java.util.Currency;
import java.util.Locale;

public interface MoneyMaskFactoryInterface {
  MoneyMask createMoneyMask(Locale paramLocale);
  
  MoneyMask createMoneyMask(Locale paramLocale, Currency paramCurrency);
  
  MoneyMask createMoneyMask(Locale paramLocale, boolean paramBoolean);
  
  MoneyMask createMoneyMask(Locale paramLocale, Currency paramCurrency, boolean paramBoolean);
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\MoneyMaskFactoryInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */