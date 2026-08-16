package oracle.retail.sim.service.activityhistory;

import java.util.Collection;
import oracle.retail.sim.common.activityhistory.ActivityHistoryVO;

public abstract class ActivityHistoryServices {
  public abstract void writeActivityRecord(ActivityHistoryVO paramActivityHistoryVO) throws Exception;
  
  public abstract void writeActivityRecord(Collection<ActivityHistoryVO> paramCollection) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\activityhistory\ActivityHistoryServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */