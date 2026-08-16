package oracle.retail.sim.common.report;

import oracle.retail.sim.common.business.BusinessObject;

public class RetailStoreFormatPrinter extends BusinessObject {
  private static final long serialVersionUID = 2484472981022493937L;
  
  private StorePrinter printer;
  
  private String format;
  
  private String reportType;
  
  private String reportURL;
  
  public String getFormat() {
    return this.format;
  }
  
  public void setFormat(String paramString) {
    this.format = paramString;
  }
  
  public String getReportType() {
    return this.reportType;
  }
  
  public void setReportType(String paramString) {
    this.reportType = paramString;
  }
  
  public StorePrinter getPrinter() {
    return this.printer;
  }
  
  public void setPrinter(StorePrinter paramStorePrinter) {
    this.printer = paramStorePrinter;
  }
  
  public String getReportURL() {
    return this.reportURL;
  }
  
  public void setReportURL(String paramString) {
    this.reportURL = paramString;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\report\RetailStoreFormatPrinter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */