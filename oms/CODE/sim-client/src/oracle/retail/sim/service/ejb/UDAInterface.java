package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.uda.ItemUDAVO;
import oracle.retail.sim.common.uda.UDADetail;
import oracle.retail.sim.common.uda.UDAValue;

@Remote
public interface UDAInterface {
  CompressedObject<List<UDADetail>> findAllUDADetails(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<ItemUDAVO>> findItemUDAVOs(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<UDAValue>> findUDAValues(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateUDADetails(CompressedObject<List<UDADetail>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\UDAInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */