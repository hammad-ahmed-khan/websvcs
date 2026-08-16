package oracle.retail.sim.common.translation;

import java.io.Serializable;
import java.util.Arrays;

public class TranslationExportRecord implements Serializable {
  private static final long serialVersionUID = 289416754578178698L;
  
  private String key;
  
  private String english;
  
  private String comment;
  
  private String translation;
  
  public String getKey() {
    return this.key;
  }
  
  public String getEnglish() {
    return (this.english == null) ? this.key : this.english;
  }
  
  public String getComment() {
    return this.comment;
  }
  
  public String getTranslation() {
    return this.translation;
  }
  
  public void setComment(String paramString) {
    this.comment = paramString;
  }
  
  public void setEnglish(String paramString) {
    this.english = paramString;
  }
  
  public void setKey(String paramString) {
    this.key = paramString;
  }
  
  public void setTranslation(String paramString) {
    this.translation = paramString;
  }
  
  public String toString() {
    String[] arrayOfString = { this.key, this.comment, this.translation };
    return Arrays.<String>asList(arrayOfString).toString();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\translation\TranslationExportRecord.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */