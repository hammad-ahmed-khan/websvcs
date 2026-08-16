package oracle.retail.sim.service.ejb;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.schedule.ProductGroupBatchVO;
import oracle.retail.sim.common.schedule.ProductGroupSchedule;
import oracle.retail.sim.common.schedule.ProductGroupScheduleQueryFilter;
import oracle.retail.sim.common.schedule.ProductGroupScheduleVO;

@Remote
public interface ProductGroupScheduleInterface {
  CompressedObject<Long> create(CompressedObject<ProductGroupSchedule> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> delete(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ProductGroupScheduleVO>> findProductGroupScheduleVOs(CompressedObject<ProductGroupScheduleQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ProductGroupBatchVO>> generateBatchRecords(CompressedObject<Long> paramCompressedObject, CompressedObject<Date> paramCompressedObject1, CompressedObject<ProductGroupType> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<ProductGroupSchedule> readProductGroupSchedule(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> update(CompressedObject<ProductGroupSchedule> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ProductGroupScheduleInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */