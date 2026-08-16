package oracle.retail.sim.common.format;

import java.util.Locale;
import oracle.retail.sim.common.configutil.CommonConfigManager;

public class PhoneMaskFactory {
  private static PhoneMaskFactoryInterface factory = getDefaultFactory();
  
  private static PhoneMaskFactoryInterface getDefaultFactory() {
    return CommonConfigManager.getPhoneMaskFactory();
  }
  
  public static Mask createPhoneMask(Locale paramLocale) {
    return factory.createPhoneMask(paramLocale);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\PhoneMaskFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */