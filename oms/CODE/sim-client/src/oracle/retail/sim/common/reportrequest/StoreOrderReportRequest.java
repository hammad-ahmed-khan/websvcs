package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class StoreOrderReportRequest extends ReportRequest {
  private static final long serialVersionUID = 3927853226201031927L;
  
  private static final String PARAM_ID = "STORE_ORDER_ID";
  
  public StoreOrderReportRequest(String paramString) {
    addParameter("STORE_ORDER_ID", paramString);
  }
  
  public String getStoreOrderId() {
    return getParameter("STORE_ORDER_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\StoreOrderReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */