package oracle.retail.sim.common.reportrequest;

import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.report.ReportRequest;

public class ItemTicketReportRequest extends ReportRequest {
  private static final long serialVersionUID = -3520859167321976475L;
  
  private static final String PARAM_STORE_ID = "STORE_ID";
  
  private static final String PARAM_ITEM_ID = "ITEM_ID";
  
  private static final String PARAM_DESCRIPTION = "DESCRIPTION";
  
  private static final String PARAM_USER_NAME = "USER_ID";
  
  private static final String PARAM_COUNTRY_MFR_NAME = "CTRY_OF_MFR";
  
  private static final String PARAM_PRICE_PER_UOM = "PRICE_PER_UOM";
  
  private static final String PARAM_PRICE = "PRICE";
  
  private static final String PARAM_COPIES = "COPIES";
  
  private SimMoney price;
  
  private SimMoney pricePerUom;
  
  public void setStoreId(Long paramLong) {
    addParameter("STORE_ID", paramLong);
  }
  
  public Long getStoreId() {
    return getParameterLong("STORE_ID");
  }
  
  public void setItemId(String paramString) {
    addParameter("ITEM_ID", paramString);
  }
  
  public String getItemId() {
    return getParameter("ITEM_ID");
  }
  
  public void setDescription(String paramString) {
    addParameter("DESCRIPTION", paramString);
  }
  
  public String getDescription() {
    return getParameter("DESCRIPTION");
  }
  
  public void setUsername(String paramString) {
    addParameter("USER_ID", paramString);
  }
  
  public String getUsername() {
    return getParameter("USER_ID");
  }
  
  public void setCountryOfManufactureName(String paramString) {
    addParameter("CTRY_OF_MFR", paramString);
  }
  
  public String getCountryOfManufactureName() {
    return getParameter("CTRY_OF_MFR");
  }
  
  public void setPricePerUom(SimMoney paramSimMoney) {
    this.pricePerUom = paramSimMoney;
  }
  
  public SimMoney getPricePerUom() {
    return this.pricePerUom;
  }
  
  public void setFormattedPricePerUom(String paramString) {
    addParameter("PRICE_PER_UOM", paramString);
  }
  
  public String getFormattedPricePerUom() {
    return getParameter("PRICE_PER_UOM");
  }
  
  public void setPrice(SimMoney paramSimMoney) {
    this.price = paramSimMoney;
  }
  
  public SimMoney getPrice() {
    return this.price;
  }
  
  public void setFormattedPrice(String paramString) {
    addParameter("PRICE", paramString);
  }
  
  public String getFormattedPrice() {
    return getParameter("PRICE");
  }
  
  public void setTicketQuantity(Integer paramInteger) {
    addParameter("COPIES", paramInteger);
  }
  
  public Integer getTicketQuantity() {
    return getParameterInteger("COPIES");
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\reportrequest\ItemTicketReportRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */