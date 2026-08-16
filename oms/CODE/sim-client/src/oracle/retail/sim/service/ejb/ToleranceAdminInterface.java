package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.tolerance.ToleranceAdmin;
import oracle.retail.sim.common.tolerance.ToleranceVO;

@Remote
public interface ToleranceAdminInterface {
  CompressedObject<List<ToleranceAdmin>> findAllToleranceAdmins(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ToleranceAdmin>> findToleranceAdmins(CompressedObject<List<ToleranceVO>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> update(CompressedObject<List<ToleranceAdmin>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ToleranceAdminInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */