package oracle.retail.sim.service.tolerance;

import java.util.List;
import oracle.retail.sim.common.tolerance.ToleranceAdmin;
import oracle.retail.sim.common.tolerance.ToleranceVO;

public abstract class ToleranceAdminServices {
  public abstract List<ToleranceAdmin> findAllToleranceAdmins(Long paramLong) throws Exception;
  
  public abstract List<ToleranceAdmin> findToleranceAdmins(List<ToleranceVO> paramList) throws Exception;
  
  public abstract void update(List<ToleranceAdmin> paramList) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\tolerance\ToleranceAdminServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */