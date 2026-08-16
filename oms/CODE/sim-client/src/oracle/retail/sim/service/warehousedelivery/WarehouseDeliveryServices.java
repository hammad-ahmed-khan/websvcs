package oracle.retail.sim.service.warehousedelivery;

import java.util.List;
import oracle.retail.sim.common.warehousedelivery.WarehouseDelivery;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCarton;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryCartonVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQueryFilter;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryQuickVO;
import oracle.retail.sim.common.warehousedelivery.WarehouseDeliveryVO;

public abstract class WarehouseDeliveryServices {
  public abstract WarehouseDelivery readWarehouseDelivery(Long paramLong) throws Exception;
  
  public abstract Long updateWarehouseDelivery(WarehouseDelivery paramWarehouseDelivery) throws Exception;
  
  public abstract WarehouseDelivery receiveWarehouseDelivery(WarehouseDelivery paramWarehouseDelivery) throws Exception;
  
  public abstract List<WarehouseDeliveryVO> findWarehouseDeliveryVOs(WarehouseDeliveryQueryFilter paramWarehouseDeliveryQueryFilter) throws Exception;
  
  public abstract WarehouseDeliveryVO readWarehouseDeliveryVOForCarton(String paramString, boolean paramBoolean) throws Exception;
  
  public abstract WarehouseDeliveryCarton readWarehouseDeliveryCarton(Long paramLong) throws Exception;
  
  public abstract WarehouseDeliveryCarton updateWarehouseDeliveryCarton(WarehouseDeliveryCarton paramWarehouseDeliveryCarton, Long paramLong) throws Exception;
  
  public abstract WarehouseDeliveryCartonVO readWarehouseDeliveryCartonVO(String paramString, boolean paramBoolean) throws Exception;
  
  public abstract WarehouseDeliveryCartonVO readWarehouseDeliveryCartonVO(String paramString, Long paramLong, boolean paramBoolean) throws Exception;
  
  public abstract List<WarehouseDeliveryCartonVO> findWarehouseDeliveryCartonVOs(WarehouseDeliveryCartonQueryFilter paramWarehouseDeliveryCartonQueryFilter) throws Exception;
  
  public abstract WarehouseDeliveryQuickVO receiveWarehouseDeliveryCarton(WarehouseDeliveryCartonVO paramWarehouseDeliveryCartonVO) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\warehousedelivery\WarehouseDeliveryServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */