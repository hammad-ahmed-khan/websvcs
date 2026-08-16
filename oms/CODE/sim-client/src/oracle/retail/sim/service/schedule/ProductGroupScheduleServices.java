package oracle.retail.sim.service.schedule;

import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.schedule.ProductGroupBatchVO;
import oracle.retail.sim.common.schedule.ProductGroupSchedule;
import oracle.retail.sim.common.schedule.ProductGroupScheduleQueryFilter;
import oracle.retail.sim.common.schedule.ProductGroupScheduleVO;

public abstract class ProductGroupScheduleServices {
  public abstract Long create(ProductGroupSchedule paramProductGroupSchedule) throws Exception;
  
  public abstract void update(ProductGroupSchedule paramProductGroupSchedule) throws Exception;
  
  public abstract void delete(Long paramLong) throws Exception;
  
  public abstract ProductGroupSchedule readProductGroupSchedule(Long paramLong) throws Exception;
  
  public abstract List<ProductGroupScheduleVO> findProductGroupScheduleVOs(ProductGroupScheduleQueryFilter paramProductGroupScheduleQueryFilter) throws Exception;
  
  public abstract List<ProductGroupBatchVO> generateBatchRecords(Long paramLong, Date paramDate, ProductGroupType paramProductGroupType) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\schedule\ProductGroupScheduleServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */