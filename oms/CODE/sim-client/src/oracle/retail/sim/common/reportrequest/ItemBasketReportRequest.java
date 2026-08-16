package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class ItemBasketReportRequest extends ReportRequest {
  private static final long serialVersionUID = 2209705160536634327L;
  
  private static final String PARAM_ID = "ITEM_BASKET_ID";
  
  private static final String PARAM_EXT_REF = "CUST_REF";
  
  public ItemBasketReportRequest(Long paramLong) {
    addParameter("ITEM_BASKET_ID", paramLong);
  }
  
  public Long getBasketId() {
    return getParameterLong("ITEM_BASKET_ID");
  }
  
  public void setExternalId(String paramString) {
    addParameter("CUST_REF", paramString);
  }
  
  public String getExternalId() {
    return getParameter("CUST_REF");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\ItemBasketReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */