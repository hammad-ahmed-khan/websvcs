package oracle.retail.sim.service.directdelivery;

import java.util.List;
import oracle.retail.sim.common.directdelivery.DirectDelivery;
import oracle.retail.sim.common.directdelivery.DirectDeliveryCartonVO;
import oracle.retail.sim.common.directdelivery.DirectDeliveryQueryFilter;
import oracle.retail.sim.common.directdelivery.DirectDeliveryVO;
import oracle.retail.sim.common.directdelivery.PurchaseOrder;
import oracle.retail.sim.common.directdelivery.PurchaseOrderQueryFilter;
import oracle.retail.sim.common.directdelivery.PurchaseOrderVO;

public abstract class DirectDeliveryServices {
  public abstract DirectDelivery readDirectDelivery(Long paramLong) throws Exception;
  
  public abstract Long updateDirectDelivery(DirectDelivery paramDirectDelivery) throws Exception;
  
  public abstract Long receiveDirectDelivery(DirectDelivery paramDirectDelivery) throws Exception;
  
  public abstract void cancelDirectDelivery(DirectDelivery paramDirectDelivery) throws Exception;
  
  public abstract List<DirectDeliveryVO> findDirectDeliveryVOs(DirectDeliveryQueryFilter paramDirectDeliveryQueryFilter, boolean paramBoolean) throws Exception;
  
  public abstract List<DirectDeliveryVO> findOpenAsnDirectDeliveryVOs(Long paramLong, boolean paramBoolean) throws Exception;
  
  public abstract DirectDeliveryCartonVO readDirectDeliveryCartonVO(String paramString) throws Exception;
  
  public abstract PurchaseOrder readPurchaseOrder(Long paramLong) throws Exception;
  
  public abstract PurchaseOrderVO readPurchaseOrderVO(String paramString, Long paramLong) throws Exception;
  
  public abstract List<PurchaseOrderVO> findPurchaseOrderVOs(PurchaseOrderQueryFilter paramPurchaseOrderQueryFilter) throws Exception;
  
  public abstract DirectDelivery createDirectDeliveryForPurchaseOrder(Long paramLong) throws Exception;
  
  public abstract DirectDelivery prepareDirectDeliveryAsnForPurchaseOrder(Long paramLong) throws Exception;
  
  public abstract List<String> findDirectDeliveryUserIds(Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\directdelivery\DirectDeliveryServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */