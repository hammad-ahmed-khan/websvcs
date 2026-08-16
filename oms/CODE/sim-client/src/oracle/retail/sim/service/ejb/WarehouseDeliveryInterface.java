package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQuickVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryVO;

@Remote
public interface WarehouseDeliveryInterface {
  CompressedObject<List<WarehouseDeliveryCartonVO>> findWarehouseDeliveryCartonVOs(CompressedObject<WarehouseDeliveryCartonQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<WarehouseDeliveryVO>> findWarehouseDeliveryVOs(CompressedObject<WarehouseDeliveryQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<WarehouseDelivery> readWarehouseDelivery(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<WarehouseDeliveryCarton> readWarehouseDeliveryCarton(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<WarehouseDeliveryCartonVO> readWarehouseDeliveryCartonVO(CompressedObject<String> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<WarehouseDeliveryCartonVO> readWarehouseDeliveryCartonVO2(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<WarehouseDeliveryVO> readWarehouseDeliveryVOForCarton(CompressedObject<String> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<WarehouseDelivery> receiveWarehouseDelivery(CompressedObject<WarehouseDelivery> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<WarehouseDeliveryQuickVO> receiveWarehouseDeliveryCarton(CompressedObject<WarehouseDeliveryCartonVO> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> updateWarehouseDelivery(CompressedObject<WarehouseDelivery> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<WarehouseDeliveryCarton> updateWarehouseDeliveryCarton(CompressedObject<WarehouseDeliveryCarton> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\WarehouseDeliveryInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */