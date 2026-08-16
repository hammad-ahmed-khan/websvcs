package oracle.retail.sim.service.productgroup;

import java.util.List;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupQueryFilter;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.productgroup.StockCountGroupVO;

public abstract class ProductGroupServices {
  public abstract Long create(ProductGroup paramProductGroup) throws Exception;
  
  public abstract void update(ProductGroup paramProductGroup) throws Exception;
  
  public abstract ProductGroup readProductGroup(Long paramLong) throws Exception;
  
  public abstract ProductGroupVO readProductGroupVO(Long paramLong) throws Exception;
  
  public abstract List<ProductGroupVO> findProductGroupVOs(ProductGroupQueryFilter paramProductGroupQueryFilter) throws Exception;
  
  public abstract List<StockCountGroupVO> findStockCountProductGroupVOs(Long paramLong) throws Exception;
  
  public abstract Integer calculateNumberOfItems(ProductGroupType paramProductGroupType, Long paramLong1, Long paramLong2, Long paramLong3, Long paramLong4) throws Exception;
  
  public abstract Integer calculateNumberOfItems(Long paramLong) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\productgroup\ProductGroupServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */