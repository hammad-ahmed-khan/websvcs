package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class FulfillmentOrderReportRequest extends ReportRequest {
  private static final long serialVersionUID = -1793206280520674502L;
  
  private static final String PARAM_ID = "Order_Id";
  
  public FulfillmentOrderReportRequest(Long paramLong) {
    addParameter("Order_Id", paramLong);
  }
  
  public Long getOrderId() {
    return getParameterLong("Order_Id");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\FulfillmentOrderReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */