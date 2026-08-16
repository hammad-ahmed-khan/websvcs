package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.deals.Deal;

@Remote
public interface DealInterface {
  CompressedObject<List<Deal>> findDeals(CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject1, CompressedObject<String> paramCompressedObject2, CompressedObject<Date> paramCompressedObject3, CompressedObject<SimSession> paramCompressedObject4) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\DealInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */