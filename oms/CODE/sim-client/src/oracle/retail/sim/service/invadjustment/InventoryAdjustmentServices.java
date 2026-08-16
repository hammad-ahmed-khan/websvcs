package oracle.retail.sim.service.invadjustment;

import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplate;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateVO;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentVO;
import oracle.retail.sim.common.item.NonSellableQtyType;

public abstract class InventoryAdjustmentServices {
  public abstract List<InventoryAdjustmentReason> findAllInventoryAdjustmentReasons() throws Exception;
  
  public abstract Set<Integer> findInUseInventoryAdjustmentReasonCodes() throws Exception;
  
  public abstract void updateInventoryAdjustmentReasons(List<InventoryAdjustmentReason> paramList, List<Long> paramList1) throws Exception;
  
  public abstract List<InventoryAdjustmentTemplateVO> findTemplateVOs(InventoryAdjustmentTemplateQueryFilter paramInventoryAdjustmentTemplateQueryFilter) throws Exception;
  
  public abstract InventoryAdjustmentTemplate readTemplate(Long paramLong) throws Exception;
  
  public abstract void updateTemplate(InventoryAdjustmentTemplate paramInventoryAdjustmentTemplate) throws Exception;
  
  public abstract void confirmTemplate(InventoryAdjustmentTemplate paramInventoryAdjustmentTemplate) throws Exception;
  
  public abstract void cancelTemplate(Long paramLong) throws Exception;
  
  public abstract List<String> findTemplateUsernames(Long paramLong) throws Exception;
  
  public abstract Map<Long, Set<Long>> findTemplateReasons(Set<Long> paramSet) throws Exception;
  
  public abstract List<InventoryAdjustmentVO> findInventoryAdjustmentVOs(InventoryAdjustmentQueryFilter paramInventoryAdjustmentQueryFilter) throws Exception;
  
  public abstract InventoryAdjustment readInventoryAdjustment(Long paramLong) throws Exception;
  
  public abstract Long updateInventoryAdjustment(InventoryAdjustment paramInventoryAdjustment) throws Exception;
  
  public abstract InventoryAdjustment confirmInventoryAdjustment(InventoryAdjustment paramInventoryAdjustment, boolean paramBoolean) throws Exception;
  
  public abstract void cancelInventoryAdjustment(Long paramLong) throws Exception;
  
  public abstract List<String> findInventoryAdjustmentUsernames(Long paramLong) throws Exception;
  
  public abstract Map<Long, Set<Long>> findInventoryAdjustmentReasons(Set<Long> paramSet) throws Exception;
  
  public abstract List<NonSellableQtyType> findNonSellableQtyTypes() throws Exception;
  
  public abstract boolean isValidTemplateId(Long paramLong) throws Exception;
  
  public abstract boolean isValidInventoryAdjustmentId(Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\invadjustment\InventoryAdjustmentServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */