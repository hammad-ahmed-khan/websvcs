package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.postransaction.POSTransaction;
import oracle.retail.sim.common.postransaction.POSTransactionSourceType;

@Remote
public interface POSTransactionInterface {
  CompressedObject<?> stagePOSTransactions(CompressedObject<Long> paramCompressedObject, CompressedObject<POSTransactionSourceType> paramCompressedObject1, CompressedObject<List<POSTransaction>> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\POSTransactionInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */