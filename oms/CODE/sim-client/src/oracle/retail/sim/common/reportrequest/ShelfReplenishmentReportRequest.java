package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class ShelfReplenishmentReportRequest extends ReportRequest {
  private static final long serialVersionUID = -5949194837610368399L;
  
  private static final String PARAM_ID = "SHELF_REPLENISH_ID";
  
  public ShelfReplenishmentReportRequest(Long paramLong) {
    addParameter("SHELF_REPLENISH_ID", paramLong);
  }
  
  public Long getShelfReplenishmentId() {
    return getParameterLong("SHELF_REPLENISH_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\ShelfReplenishmentReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */