package oracle.retail.sim.service.uda;

import java.util.List;
import oracle.retail.sim.common.uda.ItemUDAVO;
import oracle.retail.sim.common.uda.UDADetail;
import oracle.retail.sim.common.uda.UDAValue;

public abstract class UDAServices {
  public abstract List<UDADetail> findAllUDADetails() throws Exception;
  
  public abstract List<ItemUDAVO> findItemUDAVOs(String paramString) throws Exception;
  
  public abstract List<UDAValue> findUDAValues(Long paramLong) throws Exception;
  
  public abstract void updateUDADetails(List<UDADetail> paramList) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\servic\\uda\UDAServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */