package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnQueryFilter;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.common.stockreturn.ReturnVO;

@Remote
public interface ReturnInterface {
  CompressedObject<?> cancelReturn(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> cancelSubmittedStockReturn(CompressedObject<Return> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> dispatchReturn(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> dispatchReturn2(CompressedObject<Long> paramCompressedObject, CompressedObject<List<SessionPrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> dispatchReturns(CompressedObject<List<String>> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<Date> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
  
  CompressedObject<List<ReturnReason>> findAllReturnReasons(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<String>> findEmployeeIds(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Set<ReturnReason>> findInUseReturnReasons(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Map<Long, Set<Long>>> findReturnReasons(CompressedObject<List<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ReturnReason>> findReturnReasons2(CompressedObject<SourceType> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ReturnVO>> findReturnVOs(CompressedObject<ReturnQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> insertReturn(CompressedObject<Return> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Boolean> isSerialNumberOnReturn(CompressedObject<String> paramCompressedObject, CompressedObject<SourceType> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<Return> readReturn(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> submitReturn(CompressedObject<Return> paramCompressedObject, CompressedObject<List<SessionPrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> submitStockReturn(CompressedObject<Return> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> submitStockReturn2(CompressedObject<Return> paramCompressedObject, CompressedObject<List<SessionPrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateAndDispatchReturn(CompressedObject<Return> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateAndDispatchReturn2(CompressedObject<Return> paramCompressedObject, CompressedObject<List<SessionPrinter>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateReturn(CompressedObject<Return> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateReturnReasons(CompressedObject<List<ReturnReason>> paramCompressedObject, CompressedObject<List<Long>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ReturnInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */