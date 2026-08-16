package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class UinReportRequest extends ReportRequest {
  private static final long serialVersionUID = 8655068900244522931L;
  
  private static final String PARAM_STORE_ID = "STORE_ID";
  
  private static final String PARAM_ITEM_ID = "ITEM_ID";
  
  private static final String PARAM_UIN_ID = "UIN_ID";
  
  public void setStoreId(Long paramLong) {
    addParameter("STORE_ID", paramLong);
  }
  
  public Long getStoreId() {
    return getParameterLong("STORE_ID");
  }
  
  public void setItemId(String paramString) {
    addParameter("ITEM_ID", paramString);
  }
  
  public String getItemId() {
    return getParameter("ITEM_ID");
  }
  
  public void setUin(String paramString) {
    addParameter("UIN_ID", paramString);
  }
  
  public String getUin() {
    return getParameter("UIN_ID");
  }
  
  public void setUinId(Long paramLong) {
    addParameter("UIN_ID", paramLong);
  }
  
  public Long getUinId() {
    return getParameterLong("UIN_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\UinReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */