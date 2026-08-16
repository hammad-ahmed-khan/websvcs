package oracle.retail.sim.service.ejb;

import java.util.Collection;
import javax.ejb.Remote;
import oracle.retail.sim.common.activityhistory.ActivityHistoryVO;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;

@Remote
public interface ActivityHistoryInterface {
  CompressedObject<?> writeActivityRecord(CompressedObject<Collection<ActivityHistoryVO>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> writeActivityRecord2(CompressedObject<ActivityHistoryVO> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ActivityHistoryInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */