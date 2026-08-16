package oracle.retail.sim.common.format;

import java.util.Locale;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.telephone.Telephone;
import oracle.retail.sim.common.telephone.TelephoneType;

public class BasicPhoneMask extends MaskAdaptor {
  private Locale locale;
  
  public BasicPhoneMask() {
    this(Locale.US);
  }
  
  public BasicPhoneMask(Locale paramLocale) {
    this.locale = paramLocale;
  }
  
  public String formatData(Object paramObject) {
    if (paramObject instanceof Telephone) {
      Telephone telephone = (Telephone)paramObject;
      return telephone.getTelephoneNumber();
    } 
    return "";
  }
  
  public String format(String paramString) {
    return StringHelper.isNullOrEmpty(paramString) ? "" : formatData(new Telephone(TelephoneType.VOICE, unformat(paramString)));
  }
  
  public String unformat(String paramString) {
    return StringHelper.isNullOrEmpty(paramString) ? "" : NumberHelper.getInstance(this.locale).collapseToInteger(paramString);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\BasicPhoneMask.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */