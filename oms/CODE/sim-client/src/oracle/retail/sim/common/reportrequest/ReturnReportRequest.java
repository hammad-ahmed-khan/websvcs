package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class ReturnReportRequest extends ReportRequest {
  private static final long serialVersionUID = -6642531243221048384L;
  
  private static final String PARAM_ID = "Return_ID";
  
  public ReturnReportRequest(Long paramLong) {
    addParameter("Return_ID", paramLong);
  }
  
  public Long getReturnId() {
    return getParameterLong("Return_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\ReturnReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */