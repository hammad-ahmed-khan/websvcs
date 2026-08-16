package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;

@Remote
public interface ReportFormatInterface {
  CompressedObject<?> createReportTypeFormats(CompressedObject<Long> paramCompressedObject, CompressedObject<List<ReportTypeFormat>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> deleteReportTypeFormats(CompressedObject<List<String>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ReportTypeFormat>> findAllReportTypeFormats(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ReportTypeFormat> findDefaultReportTypeFormat(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ReportTypeFormat>> findReportTypeFormats(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<ReportTypeFormat>> findReportTypeFormatsForPrinter(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<?> updateReportTypeFormats(CompressedObject<Long> paramCompressedObject, CompressedObject<List<ReportTypeFormat>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ReportFormatInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */