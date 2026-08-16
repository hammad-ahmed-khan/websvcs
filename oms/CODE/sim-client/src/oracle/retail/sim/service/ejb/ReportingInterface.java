package oracle.retail.sim.service.ejb;

import java.util.List;
import java.util.Locale;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.report.ReportRequest;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.report.RetailStoreFormatPrinter;
import oracle.retail.sim.common.report.StorePrinter;

@Remote
public interface ReportingInterface {
  CompressedObject<?> createPrinters(CompressedObject<Long> paramCompressedObject, CompressedObject<List<StorePrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> deletePrinters(CompressedObject<List<String>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<StorePrinter>> findPrinters(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ReportResponse> requestReport(CompressedObject<ReportRequest> paramCompressedObject, CompressedObject<Locale> paramCompressedObject1, CompressedObject<StorePrinter> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<ReportResponse>> requestReports(CompressedObject<List<ReportRequest>> paramCompressedObject, CompressedObject<Locale> paramCompressedObject1, CompressedObject<List<RetailStoreFormatPrinter>> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<ReportResponse>> requestReports2(CompressedObject<List<ReportRequest>> paramCompressedObject, CompressedObject<Locale> paramCompressedObject1, CompressedObject<StorePrinter> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<?> updatePrinters(CompressedObject<Long> paramCompressedObject, CompressedObject<List<StorePrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ReportingInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */