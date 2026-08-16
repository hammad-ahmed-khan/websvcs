package oracle.retail.sim.service.translation;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import oracle.retail.sim.common.translation.TranslationExportRecord;
import oracle.retail.sim.common.translation.TranslationMap;

public abstract class TranslationServices {
  public abstract List<Locale> findLocales() throws Exception;
  
  public abstract Map<String, String> findTranslationKeys() throws Exception;
  
  public abstract void addTranslationKey(String paramString1, String paramString2) throws Exception;
  
  public abstract void updateTranslationKey(String paramString1, String paramString2) throws Exception;
  
  public abstract TranslationMap findTranslations(Locale paramLocale) throws Exception;
  
  public abstract TranslationMap findAllTranslations(Locale paramLocale) throws Exception;
  
  public abstract TranslationMap findUpdatedTranslations(Locale paramLocale, Date paramDate) throws Exception;
  
  public abstract void updateTranslation(Locale paramLocale, String paramString1, String paramString2) throws Exception;
  
  public abstract List<TranslationExportRecord> findTranslationExportRecords(String paramString1, String paramString2, String paramString3) throws Exception;
  
  public abstract List<TranslationExportRecord> findTranslationExportRecords(String paramString1, String paramString2, String paramString3, Date paramDate) throws Exception;
  
  public abstract List<TranslationExportRecord> findTranslationDeltaRecords(String paramString1, String paramString2, String paramString3) throws Exception;
  
  public abstract void importTranslationRecords(String paramString1, String paramString2, String paramString3, List<TranslationExportRecord> paramList) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\translation\TranslationServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */