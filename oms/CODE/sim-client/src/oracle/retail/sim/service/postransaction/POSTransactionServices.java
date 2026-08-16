package oracle.retail.sim.service.postransaction;

import java.util.List;
import oracle.retail.sim.common.postransaction.POSTransaction;
import oracle.retail.sim.common.postransaction.POSTransactionSourceType;

public abstract class POSTransactionServices {
  public abstract void stagePOSTransactions(Long paramLong, POSTransactionSourceType paramPOSTransactionSourceType, List<POSTransaction> paramList) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\postransaction\POSTransactionServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */