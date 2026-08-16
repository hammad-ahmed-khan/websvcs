package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.mdsehierarchy.MdseHierarchyNode;

@Remote
public interface MdseHierarchyInterface {
  CompressedObject<List<MdseHierarchyNode>> findClasses(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<MdseHierarchyNode>> findDepartments(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<MdseHierarchyNode>> findSubclasses(CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\MdseHierarchyInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */