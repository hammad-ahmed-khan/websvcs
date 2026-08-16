package oracle.retail.sim.common.core.type;

import java.util.Locale;
import oracle.retail.sim.common.core.locale.StringHelper;

public class LocaleDisplayer extends AbstractDisplayer {
  private Locale userLocale = Locale.US;
  
  public LocaleDisplayer(Locale paramLocale) {
    if (paramLocale != null)
      this.userLocale = paramLocale; 
  }
  
  public String getDisplayText(Object paramObject) {
    if (paramObject instanceof Locale) {
      Locale locale = (Locale)paramObject;
      StringBuilder stringBuilder = new StringBuilder(locale.getDisplayLanguage(this.userLocale));
      if (!StringHelper.isNullOrEmpty(locale.getDisplayCountry(this.userLocale))) {
        stringBuilder.append(" - ");
        stringBuilder.append(locale.getDisplayCountry(this.userLocale));
      } 
      if (!StringHelper.isNullOrEmpty(locale.getDisplayVariant(this.userLocale))) {
        stringBuilder.append(" - ");
        stringBuilder.append(locale.getDisplayVariant(this.userLocale));
      } 
      return stringBuilder.toString();
    } 
    return " ";
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\core\type\LocaleDisplayer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */