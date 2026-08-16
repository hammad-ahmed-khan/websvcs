package oracle.retail.sim.common.report;

import java.util.HashMap;
import oracle.retail.sim.common.business.BusinessObject;

public class ReportRequest extends BusinessObject {
  private static final long serialVersionUID = 4419756255417526471L;
  
  public static final String STORE_TIMEZONE = "Store_Timezone";
  
  private String url;
  
  private HashMap<String, String> parameters = new HashMap<>();
  
  private int copies = 1;
  
  public String getUrl() {
    return this.url;
  }
  
  public void setUrl(String paramString) {
    this.url = paramString;
  }
  
  public HashMap<String, String> getParameters() {
    return this.parameters;
  }
  
  public void setParameters(HashMap<String, String> paramHashMap) {
    this.parameters = paramHashMap;
  }
  
  public int getCopies() {
    return this.copies;
  }
  
  public void setCopies(int paramInt) {
    this.copies = paramInt;
  }
  
  protected String getParameter(String paramString) {
    return this.parameters.get(paramString);
  }
  
  protected void addParameter(String paramString1, String paramString2) {
    this.parameters.put(paramString1, paramString2);
  }
  
  protected void removeParameter(String paramString) {
    this.parameters.remove(paramString);
  }
  
  public void setStoreTimezoneId(String paramString) {
    addParameter("Store_Timezone", paramString);
  }
  
  protected void addParameter(String paramString, Integer paramInteger) {
    if (paramInteger != null) {
      addParameter(paramString, paramInteger.toString());
    } else {
      removeParameter(paramString);
    } 
  }
  
  protected void addParameter(String paramString, Long paramLong) {
    if (paramLong != null) {
      addParameter(paramString, paramLong.toString());
    } else {
      removeParameter(paramString);
    } 
  }
  
  protected Integer getParameterInteger(String paramString) {
    String str = getParameter(paramString);
    return (str != null) ? Integer.valueOf(str) : null;
  }
  
  protected Long getParameterLong(String paramString) {
    String str = getParameter(paramString);
    return (str != null) ? Long.valueOf(str) : null;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\report\ReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */