package oracle.retail.sim.common.business;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Quantity implements Serializable, Comparable {
  private static final long serialVersionUID = -1909186929131207962L;
  
  public static final Quantity ZERO = new Quantity(BigDecimal.ZERO);
  
  public static final Quantity ONE = new Quantity(BigDecimal.ONE);
  
  private static final int SCALE = 4;
  
  private BigDecimal quantity;
  
  public Quantity(String paramString) {
    if (paramString == null)
      throw new IllegalArgumentException("Input cannot be null!"); 
    this.quantity = new BigDecimal(paramString);
    this.quantity = this.quantity.setScale(4, RoundingMode.HALF_UP);
  }
  
  public Quantity(BigDecimal paramBigDecimal) {
    if (paramBigDecimal == null)
      throw new IllegalArgumentException("Input cannot be null!"); 
    this.quantity = paramBigDecimal.setScale(4, RoundingMode.HALF_UP);
  }
  
  public Quantity(double paramDouble) {
    BigDecimal bigDecimal = BigDecimal.valueOf(paramDouble);
    this.quantity = bigDecimal.setScale(4, RoundingMode.HALF_UP);
  }
  
  public Quantity(long paramLong) {
    BigDecimal bigDecimal = BigDecimal.valueOf(paramLong);
    this.quantity = bigDecimal.setScale(4, RoundingMode.HALF_UP);
  }
  
  public BigDecimal getBigDecimal() {
    return this.quantity;
  }
  
  public double doubleValue() {
    return this.quantity.doubleValue();
  }
  
  public long longValue() {
    return this.quantity.longValue();
  }
  
  public int intValue() {
    return this.quantity.intValue();
  }
  
  public Quantity add(BigDecimal paramBigDecimal) {
    return new Quantity(this.quantity.add(paramBigDecimal));
  }
  
  public Quantity add(Quantity paramQuantity) {
    return new Quantity(this.quantity.add(paramQuantity.getBigDecimal()));
  }
  
  public Quantity subtract(BigDecimal paramBigDecimal) {
    return new Quantity(this.quantity.subtract(paramBigDecimal));
  }
  
  public Quantity subtract(Quantity paramQuantity) {
    return new Quantity(this.quantity.subtract(paramQuantity.getBigDecimal()));
  }
  
  public Quantity multiply(Quantity paramQuantity) {
    return new Quantity(this.quantity.multiply(paramQuantity.getBigDecimal()));
  }
  
  public Quantity multiply(BigDecimal paramBigDecimal) {
    return new Quantity(this.quantity.multiply(paramBigDecimal));
  }
  
  public Quantity multiply(Integer paramInteger) {
    return new Quantity(this.quantity.doubleValue() * paramInteger.intValue());
  }
  
  public Quantity divide(BigDecimal paramBigDecimal) {
    return new Quantity(this.quantity.divide(paramBigDecimal, 4, RoundingMode.HALF_UP));
  }
  
  public Quantity divide(Quantity paramQuantity) {
    return new Quantity(this.quantity.divide(paramQuantity.getBigDecimal(), 4, RoundingMode.HALF_UP));
  }
  
  public Quantity divide(BigDecimal paramBigDecimal, int paramInt, RoundingMode paramRoundingMode) {
    return new Quantity(this.quantity.divide(paramBigDecimal, paramInt, paramRoundingMode));
  }
  
  public Quantity divide(Quantity paramQuantity, int paramInt, RoundingMode paramRoundingMode) {
    return new Quantity(this.quantity.divide(paramQuantity.getBigDecimal(), paramInt, paramRoundingMode));
  }
  
  public Quantity divide(Integer paramInteger, int paramInt, RoundingMode paramRoundingMode) {
    return divide(BigDecimal.valueOf(paramInteger.intValue()), paramInt, paramRoundingMode);
  }
  
  public Quantity abs() {
    return new Quantity(this.quantity.abs());
  }
  
  public Quantity max(Quantity paramQuantity) {
    return (compareTo(paramQuantity) >= 0) ? this : paramQuantity;
  }
  
  public Quantity min(Quantity paramQuantity) {
    return (compareTo(paramQuantity) <= 0) ? this : paramQuantity;
  }
  
  public boolean isFractional() {
    BigDecimal bigDecimal1 = this.quantity.setScale(0, RoundingMode.DOWN);
    BigDecimal bigDecimal2 = this.quantity.setScale(0, RoundingMode.UP);
    return (bigDecimal1.compareTo(bigDecimal2) != 0);
  }
  
  public boolean isPositive() {
    return (compareTo(ZERO) > 0);
  }
  
  public boolean isNegative() {
    return (compareTo(ZERO) < 0);
  }
  
  public boolean isZero() {
    return (compareTo(ZERO) == 0);
  }
  
  public boolean evenlyDivisibleBy(Quantity paramQuantity) {
    if (paramQuantity == null || paramQuantity.equals(ZERO))
      return false; 
    Quantity quantity1 = divide(paramQuantity, 0, RoundingMode.DOWN);
    Quantity quantity2 = divide(paramQuantity, 0, RoundingMode.UP);
    return (quantity1.compareTo(quantity2) == 0);
  }
  
  public int compareTo(Object paramObject) {
    return (paramObject instanceof Quantity) ? this.quantity.compareTo(((Quantity)paramObject).getBigDecimal()) : ((paramObject instanceof BigDecimal) ? this.quantity.compareTo((BigDecimal)paramObject) : 0);
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    Quantity quantity = (Quantity)paramObject;
    return Objects.equals(this.quantity, quantity.quantity);
  }
  
  public int hashCode() {
    return Objects.hashCode(this.quantity);
  }
  
  public String toString() {
    return this.quantity.toString();
  }
  
  public static Quantity valueOf(BigDecimal paramBigDecimal) {
    return (paramBigDecimal != null) ? new Quantity(paramBigDecimal) : ZERO;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\business\Quantity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */