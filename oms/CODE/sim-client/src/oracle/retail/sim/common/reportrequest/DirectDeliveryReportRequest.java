package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class DirectDeliveryReportRequest extends ReportRequest {
  private static final long serialVersionUID = -4574403633790189628L;
  
  private static final String PARAM_ID = "Receipt_ID";
  
  public DirectDeliveryReportRequest(Long paramLong) {
    addParameter("Receipt_ID", paramLong);
  }
  
  public Long getDeliveryId() {
    return getParameterLong("Receipt_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\DirectDeliveryReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */