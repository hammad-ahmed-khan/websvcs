package oracle.retail.sim.service.mdsehierarchy;

import java.util.List;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;

public abstract class MdseHierarchyServices {
  public abstract List<MdseHierarchyNode> findDepartments() throws Exception;
  
  public abstract List<MdseHierarchyNode> findClasses(Long paramLong) throws Exception;
  
  public abstract List<MdseHierarchyNode> findSubclasses(Long paramLong1, Long paramLong2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\mdsehierarchy\MdseHierarchyServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */