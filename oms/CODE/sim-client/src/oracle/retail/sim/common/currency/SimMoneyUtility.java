package oracle.retail.sim.common.currency;

import java.math.BigDecimal;
import java.util.Currency;
import oracle.retail.sim.common.configutil.CommonConfigManager;
import oracle.retail.sim.common.logging.LogService;

public class SimMoneyUtility {
  private static String defaultCode;
  
  public static SimMoney getZeroMoney() {
    return getZeroMoney(getDefaultCurrencyCode());
  }
  
  public static SimMoney getZeroMoney(String paramString) {
    return new SimMoney(BigDecimal.ZERO, Currency.getInstance(paramString));
  }
  
  public static String getDefaultCurrencyCode() {
    if (defaultCode == null) {
      defaultCode = CommonConfigManager.getDefaultCurrencyCode();
      if (defaultCode == null || defaultCode.length() < 3)
        defaultCode = "USD"; 
    } 
    return defaultCode;
  }
  
  public static SimMoney convertToSimMoney(Double paramDouble) {
    return convertToSimMoney(getDefaultCurrencyCode(), paramDouble);
  }
  
  public static SimMoney convertToSimMoney(String paramString, Double paramDouble) {
    if (paramDouble == null)
      return null; 
    Currency currency = null;
    if (paramString != null)
      try {
        currency = Currency.getInstance(paramString);
      } catch (IllegalArgumentException illegalArgumentException) {
        if (LogService.isErrorEnabled(SimMoneyUtility.class))
          LogService.error(SimMoneyUtility.class, "Unsupported currency - defaulting to base currency", illegalArgumentException); 
      }  
    if (currency == null)
      currency = Currency.getInstance(getDefaultCurrencyCode()); 
    return new SimMoney(BigDecimal.valueOf(paramDouble.doubleValue()), currency);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\currency\SimMoneyUtility.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */