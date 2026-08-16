package oracle.retail.sim.common.format;

import java.util.Locale;

public class PhoneMaskFactoryImpl implements PhoneMaskFactoryInterface {
  public Mask createPhoneMask(Locale paramLocale) {
    String str = paramLocale.getLanguage();
    if (str != null) {
      if (str.equals(Locale.ENGLISH.getLanguage()))
        return new USPhoneMask(); 
      if (str.equals(Locale.FRENCH.getLanguage()))
        return new FrenchPhoneMask(); 
      if (str.equals(Locale.GERMAN.getLanguage()))
        return new GermanPhoneMask(); 
      if (str.equals(Locale.ITALIAN.getLanguage()))
        return new ItalianPhoneMask(); 
      if ("es".equals(str))
        return new SpanishPhoneMask(); 
      if ("ru".equals(str))
        return new RussianPhoneMask(); 
      if (str.equals(Locale.SIMPLIFIED_CHINESE.getLanguage()))
        return new ChineseSimpPhoneMask(); 
      if (str.equals(Locale.TRADITIONAL_CHINESE.getLanguage()))
        return new ChineseSimpPhoneMask(); 
      if (str.equals(Locale.JAPANESE.getLanguage()))
        return new JapanesePhoneMask(); 
      if (str.equals(Locale.KOREAN.getLanguage()))
        return new KoreanPhoneMask(); 
      if ("pt".equals(str) && "BR".equalsIgnoreCase(paramLocale.getCountry()))
        return new BrazilPhoneMask(); 
    } 
    return new BasicPhoneMask(Locale.US);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\format\PhoneMaskFactoryImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */