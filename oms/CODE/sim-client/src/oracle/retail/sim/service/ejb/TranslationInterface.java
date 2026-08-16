package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.translation.TranslationExportRecord;
import oracle.retail.sim.common.translation.TranslationMap;

@Remote
public interface TranslationInterface {
  CompressedObject<?> addTranslationKey(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<TranslationMap> findAllTranslations(CompressedObject<Locale> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<Locale>> findLocales(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<TranslationExportRecord>> findTranslationDeltaRecords(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<String> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<TranslationExportRecord>> findTranslationExportRecords(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<String> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<TranslationExportRecord>> findTranslationExportRecords2(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<String> paramCompressedObject3, CompressedObject<Date> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<Map<String, String>> findTranslationKeys(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<TranslationMap> findTranslations(CompressedObject<Locale> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<TranslationMap> findUpdatedTranslations(CompressedObject<Locale> paramCompressedObject, CompressedObject<Date> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> importTranslationRecords(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<String> paramCompressedObject3, CompressedObject<List<TranslationExportRecord>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<?> updateTranslation(CompressedObject<Locale> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<?> updateTranslationKey(CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\TranslationInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */