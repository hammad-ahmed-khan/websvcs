package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class ItemRequestReportRequest extends ReportRequest {
  private static final long serialVersionUID = 3721674443592389080L;
  
  private static final String PARAM_ID = "Item_Request_ID";
  
  public ItemRequestReportRequest(Long paramLong) {
    addParameter("Item_Request_ID", paramLong);
  }
  
  public Long getItemRequestId() {
    return getParameterLong("Item_Request_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\ItemRequestReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */