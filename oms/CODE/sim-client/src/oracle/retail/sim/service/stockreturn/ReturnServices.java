package oracle.retail.sim.service.stockreturn;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockreturn.Return;
import oracle.retail.sim.common.stockreturn.ReturnQueryFilter;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.common.stockreturn.ReturnVO;

public abstract class ReturnServices {
  public abstract List<ReturnVO> findReturnVOs(ReturnQueryFilter paramReturnQueryFilter) throws Exception;
  
  public abstract Return readReturn(Long paramLong) throws Exception;
  
  public abstract Long insertReturn(Return paramReturn) throws Exception;
  
  public abstract void updateReturn(Return paramReturn) throws Exception;
  
  public abstract void cancelReturn(Long paramLong) throws Exception;
  
  public abstract void dispatchReturn(Long paramLong) throws Exception;
  
  public abstract void dispatchReturn(Long paramLong, List<SessionPrinter> paramList) throws Exception;
  
  public abstract void dispatchReturns(List<String> paramList, String paramString1, String paramString2, Date paramDate) throws Exception;
  
  public abstract void updateAndDispatchReturn(Return paramReturn, List<SessionPrinter> paramList) throws Exception;
  
  public abstract void updateAndDispatchReturn(Return paramReturn) throws Exception;
  
  public abstract void submitStockReturn(Return paramReturn) throws Exception;
  
  public abstract void submitStockReturn(Return paramReturn, List<SessionPrinter> paramList) throws Exception;
  
  public abstract void submitReturn(Return paramReturn, List<SessionPrinter> paramList) throws Exception;
  
  public abstract void cancelSubmittedStockReturn(Return paramReturn) throws Exception;
  
  public abstract List<ReturnReason> findAllReturnReasons() throws Exception;
  
  public abstract List<ReturnReason> findReturnReasons(SourceType paramSourceType) throws Exception;
  
  public abstract Map<Long, Set<Long>> findReturnReasons(List<Long> paramList) throws Exception;
  
  public abstract Set<ReturnReason> findInUseReturnReasons() throws Exception;
  
  public abstract void updateReturnReasons(List<ReturnReason> paramList, List<Long> paramList1) throws Exception;
  
  public abstract List<String> findEmployeeIds(Long paramLong) throws Exception;
  
  public abstract boolean isSerialNumberOnReturn(String paramString, SourceType paramSourceType, Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\stockreturn\ReturnServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */