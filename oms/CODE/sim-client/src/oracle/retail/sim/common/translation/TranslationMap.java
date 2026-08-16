package oracle.retail.sim.common.translation;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TranslationMap implements Serializable {
  private static final long serialVersionUID = -7829569415560053516L;
  
  private Date timestamp;
  
  private Map<String, String> translations;
  
  public TranslationMap(Date paramDate, Map<String, String> paramMap) {
    this.timestamp = paramDate;
    if (paramMap == null) {
      this.translations = new HashMap<>();
    } else {
      this.translations = new HashMap<>(paramMap);
    } 
  }
  
  public Date getTimestamp() {
    return this.timestamp;
  }
  
  public Map<String, String> getTranslations() {
    return this.translations;
  }
  
  public void addAll(TranslationMap paramTranslationMap) {
    this.translations.putAll(paramTranslationMap.getTranslations());
    this.timestamp = paramTranslationMap.getTimestamp();
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.timestamp);
    stringBuilder.append(": ");
    stringBuilder.append(this.translations.toString());
    return stringBuilder.toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\translation\TranslationMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */