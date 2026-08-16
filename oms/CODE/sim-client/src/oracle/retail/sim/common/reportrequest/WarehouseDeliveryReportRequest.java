package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class WarehouseDeliveryReportRequest extends ReportRequest {
  private static final long serialVersionUID = -6984619173071797187L;
  
  private static final String PARAM_ID = "Document_ID";
  
  public WarehouseDeliveryReportRequest(Long paramLong) {
    addParameter("Document_ID", paramLong);
  }
  
  public Long getDocumentId() {
    return getParameterLong("Document_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\WarehouseDeliveryReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */