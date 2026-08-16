package oracle.retail.sim.common.lineitem;

import java.math.BigDecimal;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringHelper;

public abstract class UnitOfMeasureWrapper extends Wrapper {
  private UOMMode uomMode;
  
  public abstract Quantity getCaseSize();
  
  public abstract String getStandardUnitOfMeasure();
  
  public boolean isEachesStandardUnitOfMeasure() {
    return "EA".equalsIgnoreCase(getStandardUnitOfMeasure());
  }
  
  public UOMMode getUnitOfMeasureMode() {
    if (this.uomMode == null)
      this.uomMode = getDefaultUomMode(); 
    return this.uomMode;
  }
  
  public UOMMode getDefaultUomMode() {
    if (isPreferredUomAvailable())
      return isPreferredUomConversionAvailable() ? UOMMode.PREFERRED : UOMMode.STANDARD; 
    Integer integer = SimConfigManager.getDefaultUom();
    return (integer != null) ? UOMMode.toValue(integer) : UOMMode.STANDARD;
  }
  
  public boolean isCasesMode() {
    return (getUnitOfMeasureMode() == UOMMode.CASES);
  }
  
  public boolean isStandardMode() {
    return (getUnitOfMeasureMode() == UOMMode.STANDARD);
  }
  
  public boolean isPreferredMode() {
    return (getUnitOfMeasureMode() == UOMMode.PREFERRED);
  }
  
  protected void setCasesMode() {
    this.uomMode = UOMMode.CASES;
  }
  
  protected void setStandardMode() {
    this.uomMode = UOMMode.STANDARD;
  }
  
  protected void setPreferredMode() {
    this.uomMode = UOMMode.PREFERRED;
  }
  
  public void setUnitOfMeasureMode(UOMMode paramUOMMode) throws BusinessException {
    checkForNullParameter("Unit Of Measure", paramUOMMode);
    this.uomMode = paramUOMMode;
  }
  
  public boolean isPreferredUomAvailable() {
    return !StringHelper.isNullOrEmpty(getPreferredUnitOfMeasure());
  }
  
  public boolean isPreferredUomConversionAvailable() {
    String str = getPreferredUnitOfMeasure();
    if (StringHelper.isNullOrEmpty(str))
      return false; 
    if (str.equals(getStandardUnitOfMeasure()))
      return false; 
    BigDecimal bigDecimal = getPreferredUomConversionFactor();
    return (bigDecimal == null) ? false : ((BigDecimal.ONE.compareTo(bigDecimal) != 0));
  }
  
  public abstract String getPreferredUnitOfMeasure();
  
  public abstract BigDecimal getPreferredUomConversionFactor();
  
  protected Quantity rationalizeQuantityBasedOnUom(Quantity paramQuantity) {
    if (paramQuantity == null)
      return null; 
    if (isStandardMode())
      return paramQuantity; 
    if (isCasesMode()) {
      Quantity quantity1 = getCaseSize();
      if (quantity1 == null)
        return paramQuantity; 
      if (quantity1.equals(Quantity.ZERO))
        return Quantity.ZERO; 
      Quantity quantity2 = paramQuantity.divide(quantity1);
      return (paramQuantity.doubleValue() > 0.0D && quantity2.doubleValue() < 0.001D) ? new Quantity(0.001D) : quantity2;
    } 
    if (isPreferredMode()) {
      BigDecimal bigDecimal = getPreferredUomConversionFactor();
      if (bigDecimal != null)
        return paramQuantity.multiply(bigDecimal); 
    } 
    return null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\lineitem\UnitOfMeasureWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */