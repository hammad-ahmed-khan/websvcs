package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class ItemReportRequest extends ReportRequest {
  private static final long serialVersionUID = 3725101859075760267L;
  
  private static final String PARAM_STORE_ID = "STOREID";
  
  private static final String PARAM_ITEM_ID = "ITEMID";
  
  public ItemReportRequest(String paramString) {
    addParameter("ITEMID", paramString);
  }
  
  public String getItemId() {
    return getParameter("ITEMID");
  }
  
  public void setStoreId(Long paramLong) {
    addParameter("STOREID", paramLong);
  }
  
  public Long getStoreId() {
    return getParameterLong("STOREID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\ItemReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */