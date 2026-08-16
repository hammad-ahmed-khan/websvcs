package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.productgroup.ProductGroup;
import oracle.retail.sim.common.productgroup.ProductGroupQueryFilter;
import oracle.retail.sim.common.productgroup.ProductGroupType;
import oracle.retail.sim.common.productgroup.ProductGroupVO;
import oracle.retail.sim.common.productgroup.StockCountGroupVO;

@Remote
public interface ProductGroupInterface {
  CompressedObject<Integer> calculateNumberOfItems(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Integer> calculateNumberOfItems2(CompressedObject<ProductGroupType> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<Long> paramCompressedObject2, CompressedObject<Long> paramCompressedObject3, CompressedObject<Long> paramCompressedObject4, CompressedObject<SimSession> paramCompressedObject5) throws Exception;
  
  CompressedObject<Long> create(CompressedObject<ProductGroup> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<ProductGroupVO>> findProductGroupVOs(CompressedObject<ProductGroupQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<StockCountGroupVO>> findStockCountProductGroupVOs(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ProductGroup> readProductGroup(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ProductGroupVO> readProductGroupVO(CompressedObject<Long> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<?> update(CompressedObject<ProductGroup> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\ProductGroupInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */