package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class FulfillmentOrderReversePickReportRequest extends ReportRequest {
  private static final long serialVersionUID = 7054376970211865791L;
  
  private static final String PARAM_ID = "Reverse_Pick_Id";
  
  public FulfillmentOrderReversePickReportRequest(Long paramLong) {
    addParameter("Reverse_Pick_Id", paramLong);
  }
  
  public Long getReversePickId() {
    return getParameterLong("Reverse_Pick_Id");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\FulfillmentOrderReversePickReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */