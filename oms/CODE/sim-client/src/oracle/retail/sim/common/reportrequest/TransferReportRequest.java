package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class TransferReportRequest extends ReportRequest {
  private static final long serialVersionUID = -8793214571038884371L;
  
  private static final String PARAM_ID = "Transfer_ID";
  
  public TransferReportRequest(Long paramLong) {
    addParameter("Transfer_ID", paramLong);
  }
  
  public Long getTransferId() {
    return getParameterLong("Transfer_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\TransferReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */