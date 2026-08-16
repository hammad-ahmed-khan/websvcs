package oracle.retail.sim.common.currency;

import java.math.BigDecimal;
import java.util.Currency;

public class SimMoneyCalculator {
  public static SimMoney add(SimMoney paramSimMoney1, SimMoney paramSimMoney2) {
    checkCurrencyCode(paramSimMoney1, paramSimMoney2);
    BigDecimal bigDecimal1 = paramSimMoney1.getAmount();
    BigDecimal bigDecimal2 = paramSimMoney2.getAmount();
    return new SimMoney(bigDecimal1.add(bigDecimal2), paramSimMoney1.getCurrency());
  }
  
  public static SimMoney subtract(SimMoney paramSimMoney1, SimMoney paramSimMoney2) {
    checkCurrencyCode(paramSimMoney1, paramSimMoney2);
    BigDecimal bigDecimal1 = paramSimMoney1.getAmount();
    BigDecimal bigDecimal2 = paramSimMoney2.getAmount();
    return new SimMoney(bigDecimal1.subtract(bigDecimal2), paramSimMoney1.getCurrency());
  }
  
  public static SimMoney multiply(SimMoney paramSimMoney, int paramInt) {
    return multiply(paramSimMoney, BigDecimal.valueOf(paramInt));
  }
  
  public static SimMoney multiply(SimMoney paramSimMoney, double paramDouble) {
    return multiply(paramSimMoney, BigDecimal.valueOf(paramDouble));
  }
  
  private static SimMoney multiply(SimMoney paramSimMoney, BigDecimal paramBigDecimal) {
    return new SimMoney(paramSimMoney.getAmount().multiply(paramBigDecimal), paramSimMoney.getCurrency());
  }
  
  public static boolean lessThan(SimMoney paramSimMoney1, SimMoney paramSimMoney2) {
    BigDecimal bigDecimal1 = paramSimMoney1.getAmount();
    BigDecimal bigDecimal2 = paramSimMoney2.getAmount();
    checkCurrencyCode(paramSimMoney1, paramSimMoney2);
    return (bigDecimal1.compareTo(bigDecimal2) < 0);
  }
  
  public static SimMoney round(SimMoney paramSimMoney) {
    Currency currency = paramSimMoney.getCurrency();
    int i = currency.getDefaultFractionDigits();
    BigDecimal bigDecimal = paramSimMoney.getAmount().setScale(i, 4);
    return new SimMoney(bigDecimal, currency);
  }
  
  private static void checkCurrencyCode(SimMoney paramSimMoney1, SimMoney paramSimMoney2) {
    String str1 = paramSimMoney1.getCurrencyCode();
    String str2 = paramSimMoney2.getCurrencyCode();
    if (!str1.equals(str2))
      throw new IllegalArgumentException("Currencies not equal: " + str1 + ", " + str2); 
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\currency\SimMoneyCalculator.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */