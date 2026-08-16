package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.report.ReportRequest;

public class StockCountReportRequest extends ReportRequest {
  private static final long serialVersionUID = -1439903967029654748L;
  
  private static final String PARAM_ID = "STOCK_COUNT_ID";
  
  private static final String PARAM_CHILD_ID = "LOCATION_ID";
  
  private static final String PARAM_PHASE = "PHASE";
  
  public void setStockCountId(Long paramLong) {
    addParameter("STOCK_COUNT_ID", paramLong);
  }
  
  public Long getStockCountId() {
    return getParameterLong("STOCK_COUNT_ID");
  }
  
  public void setStockCountChildId(Long paramLong) {
    addParameter("LOCATION_ID", paramLong);
  }
  
  public Long getStockCountChildId() {
    return getParameterLong("LOCATION_ID");
  }
  
  public void setStockCountPhaseCode(Integer paramInteger) {
    addParameter("PHASE", paramInteger);
  }
  
  public Integer getStockCountPhase() {
    return getParameterInteger("PHASE");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\StockCountReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */