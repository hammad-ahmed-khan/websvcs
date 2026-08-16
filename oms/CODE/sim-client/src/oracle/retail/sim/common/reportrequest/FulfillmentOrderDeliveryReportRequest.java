package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class FulfillmentOrderDeliveryReportRequest extends ReportRequest {
  private static final long serialVersionUID = 5734590186308902005L;
  
  private static final String PARAM_ID = "Delivery_Id";
  
  public FulfillmentOrderDeliveryReportRequest(Long paramLong) {
    addParameter("Delivery_Id", paramLong);
  }
  
  public Long getDeliveryId() {
    return getParameterLong("Delivery_Id");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\FulfillmentOrderDeliveryReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */