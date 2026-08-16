package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class FulfillmentOrderPickReportRequest extends ReportRequest {
  private static final long serialVersionUID = 2803100093188883114L;
  
  private static final String PARAM_ID = "Pick_Id";
  
  public FulfillmentOrderPickReportRequest(Long paramLong) {
    addParameter("Pick_Id", paramLong);
  }
  
  public Long getPickId() {
    return getParameterLong("Pick_Id");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\FulfillmentOrderPickReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */