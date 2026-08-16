package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class StockCountRejectedItemReportRequest extends ReportRequest {
  private static final long serialVersionUID = 2088351734760691556L;
  
  private static final String PARAM_STORE_ID = "STORE_ID";
  
  public StockCountRejectedItemReportRequest(Long paramLong) {
    addParameter("STORE_ID", paramLong);
  }
  
  public Long getStoreId() {
    return getParameterLong("STORE_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\StockCountRejectedItemReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */