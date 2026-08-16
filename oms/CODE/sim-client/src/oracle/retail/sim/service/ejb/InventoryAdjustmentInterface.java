package oracle.retail.sim.service.ejb;

import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.invadjustment.InventoryAdjustment;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplate;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateVO;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentVO;
import oracle.retail.sim.common.item.NonSellableQtyType;

@Remote
public interface InventoryAdjustmentInterface {
  CompressedObject<?> cancelInventoryAdjustment(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> cancelTemplate(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<InventoryAdjustment> confirmInventoryAdjustment(CompressedObject<InventoryAdjustment> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> confirmTemplate(CompressedObject<InventoryAdjustmentTemplate> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<InventoryAdjustmentReason>> findAllInventoryAdjustmentReasons(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Set<Integer>> findInUseInventoryAdjustmentReasonCodes(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Map<Long, Set<Long>>> findInventoryAdjustmentReasons(CompressedObject<Set<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<String>> findInventoryAdjustmentUsernames(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<InventoryAdjustmentVO>> findInventoryAdjustmentVOs(CompressedObject<InventoryAdjustmentQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<NonSellableQtyType>> findNonSellableQtyTypes(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<Map<Long, Set<Long>>> findTemplateReasons(CompressedObject<Set<Long>> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<String>> findTemplateUsernames(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<InventoryAdjustmentTemplateVO>> findTemplateVOs(CompressedObject<InventoryAdjustmentTemplateQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Boolean> isValidInventoryAdjustmentId(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Boolean> isValidTemplateId(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<InventoryAdjustment> readInventoryAdjustment(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<InventoryAdjustmentTemplate> readTemplate(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Long> updateInventoryAdjustment(CompressedObject<InventoryAdjustment> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> updateInventoryAdjustmentReasons(CompressedObject<List<InventoryAdjustmentReason>> paramCompressedObject, CompressedObject<List<Long>> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<?> updateTemplate(CompressedObject<InventoryAdjustmentTemplate> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\InventoryAdjustmentInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */