package oracle.retail.sim.service.tranhistory;

import java.util.List;
import java.util.Set;
import oracle.retail.sim.common.tranhistory.TransactionHistoryQueryFilter;
import oracle.retail.sim.common.tranhistory.TransactionHistoryVO;

public abstract class TransactionHistoryServices {
  public abstract Set<String> findHistoryReasonDescriptions(Long paramLong) throws Exception;
  
  public abstract List<TransactionHistoryVO> findTransactionHistoryVOs(TransactionHistoryQueryFilter paramTransactionHistoryQueryFilter) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\tranhistory\TransactionHistoryServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */