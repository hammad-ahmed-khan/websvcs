package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class FulfillmentOrderBinReportRequest extends ReportRequest {
  private static final long serialVersionUID = 2386050904719466415L;
  
  private static final String PARAM_BIN_ID = "BIN_ID";
  
  private static final String PARAM_ORDER_ID = "SIM_CUSTOMER_ORDER_ID";
  
  public FulfillmentOrderBinReportRequest(String paramString, Long paramLong) {
    addParameter("BIN_ID", paramString);
    addParameter("SIM_CUSTOMER_ORDER_ID", paramLong);
  }
  
  public String getBinId() {
    return getParameter("BIN_ID");
  }
  
  public Long getOrderId() {
    return getParameterLong("SIM_CUSTOMER_ORDER_ID");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\FulfillmentOrderBinReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */