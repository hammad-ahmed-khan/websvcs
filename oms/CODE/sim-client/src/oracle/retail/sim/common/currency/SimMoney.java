package oracle.retail.sim.common.currency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

public final class SimMoney implements Serializable, Comparable<SimMoney> {
  private static final long serialVersionUID = 1L;
  
  private static final int MONEY_SCALE = 6;
  
  private final BigDecimal amount;
  
  private final String currencyCode;
  
  private transient Currency cachedCurrency;
  
  public SimMoney(BigDecimal paramBigDecimal, Currency paramCurrency) {
    if (paramBigDecimal == null)
      throw new IllegalArgumentException("Amount cannot be null!"); 
    if (paramCurrency == null)
      throw new IllegalArgumentException("Currency cannot be null!"); 
    this.amount = paramBigDecimal.setScale(6, 1);
    this.currencyCode = paramCurrency.getCurrencyCode();
    this.cachedCurrency = paramCurrency;
  }
  
  public BigDecimal getAmount() {
    return this.amount;
  }
  
  public String getCurrencyCode() {
    return this.currencyCode;
  }
  
  public Currency getCurrency() {
    if (this.cachedCurrency == null)
      this.cachedCurrency = Currency.getInstance(this.currencyCode); 
    return this.cachedCurrency;
  }
  
  public int compareTo(SimMoney paramSimMoney) {
    return Objects.equals(this.currencyCode, paramSimMoney.currencyCode) ? this.amount.compareTo(paramSimMoney.amount) : this.currencyCode.compareTo(paramSimMoney.currencyCode);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    SimMoney simMoney = (SimMoney)paramObject;
    return (Objects.equals(this.currencyCode, simMoney.currencyCode) && Objects.equals(this.amount, simMoney.amount));
  }
  
  public int hashCode() {
    return Objects.hash(new Object[] { this.currencyCode, this.amount });
  }
  
  public String toString() {
    return this.currencyCode + " " + this.amount;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\currency\SimMoney.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */