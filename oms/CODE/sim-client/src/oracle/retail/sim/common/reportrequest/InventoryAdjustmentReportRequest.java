package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class InventoryAdjustmentReportRequest extends ReportRequest {
  private static final long serialVersionUID = -1989963043485309043L;
  
  private static final String PARAM_ID = "Inv_Adj_ID";
  
  public InventoryAdjustmentReportRequest(Long paramLong) {
    addParameter("Inv_Adj_ID", paramLong);
  }
  
  public Long getAdjustmentId() {
    return getParameterLong("Inv_Adj_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\InventoryAdjustmentReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */