package oracle.retail.sim.service.report;

import java.util.List;
import java.util.Locale;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.report.StorePrinter;

public abstract class ReportingServices {
  public abstract ReportResponse requestReport(ReportRequest paramReportRequest, Locale paramLocale, StorePrinter paramStorePrinter) throws Exception;
  
  public abstract List<ReportResponse> requestReports(List<ReportRequest> paramList, Locale paramLocale, StorePrinter paramStorePrinter) throws Exception;
  
  public abstract List<ReportResponse> requestReports(List<ReportRequest> paramList, Locale paramLocale, List<RetailStoreFormatPrinter> paramList1) throws Exception;
  
  public abstract List<StorePrinter> findPrinters(Long paramLong) throws Exception;
  
  public abstract void createPrinters(Long paramLong, List<StorePrinter> paramList) throws Exception;
  
  public abstract void updatePrinters(Long paramLong, List<StorePrinter> paramList) throws Exception;
  
  public abstract void deletePrinters(List<String> paramList) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\report\ReportingServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */