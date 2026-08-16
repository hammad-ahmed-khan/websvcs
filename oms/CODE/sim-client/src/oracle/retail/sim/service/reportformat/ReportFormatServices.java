package oracle.retail.sim.service.reportformat;

import java.util.List;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;

public abstract class ReportFormatServices {
  public abstract List<ReportTypeFormat> findAllReportTypeFormats(Long paramLong) throws Exception;
  
  public abstract List<ReportTypeFormat> findReportTypeFormats(Long paramLong, String paramString) throws Exception;
  
  public abstract List<ReportTypeFormat> findReportTypeFormatsForPrinter(Long paramLong1, Long paramLong2) throws Exception;
  
  public abstract void createReportTypeFormats(Long paramLong, List<ReportTypeFormat> paramList) throws Exception;
  
  public abstract void updateReportTypeFormats(Long paramLong, List<ReportTypeFormat> paramList) throws Exception;
  
  public abstract void deleteReportTypeFormats(List<String> paramList) throws Exception;
  
  public abstract ReportTypeFormat findDefaultReportTypeFormat(String paramString, Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\reportformat\ReportFormatServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */