package oracle.retail.sim.service.ejb;

import java.util.List;
import javax.ejb.Remote;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.person.FinisherContactInfo;
import oracle.retail.sim.common.person.SupplierContactInfo;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.FinisherVO;
import oracle.retail.sim.common.source.SourceQueryFilter;
import oracle.retail.sim.common.source.Supplier;
import oracle.retail.sim.common.source.SupplierVO;
import oracle.retail.sim.common.source.Warehouse;
import oracle.retail.sim.common.source.WarehouseDetailVO;
import oracle.retail.sim.common.source.WarehouseVO;

@Remote
public interface SourceInterface {
  CompressedObject<List<String>> findAdditionalSupplierIds(CompressedObject<String> paramCompressedObject1, CompressedObject<Long> paramCompressedObject, CompressedObject<String> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<ContextType>> findAllContextTypes(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<Warehouse>> findAllWarehouses(CompressedObject<SimSession> paramCompressedObject) throws Exception;
  
  CompressedObject<List<FinisherVO>> findFinisherVOs(CompressedObject<SourceQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<FinisherVO>> findFinisherVOs2(CompressedObject<SourceQueryFilter> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<Finisher>> findFinishersByItemId(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<SupplierVO>> findSupplierVOs(CompressedObject<SourceQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<SupplierVO>> findSupplierVOs2(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<SupplierVO>> findSupplierVOs3(CompressedObject<SourceQueryFilter> paramCompressedObject, CompressedObject<Boolean> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<Supplier>> findSuppliers(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<Supplier>> findSuppliers2(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<WarehouseVO>> findWarehouses(CompressedObject<SourceQueryFilter> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<ContextType> readContextType(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Finisher> readFinisher(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<FinisherContactInfo>> readFinisherContactInfo(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<Supplier> readSupplier(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<Supplier> readSupplier2(CompressedObject<String> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<List<SupplierContactInfo>> readSupplierContactInfo(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
  
  CompressedObject<List<Supplier>> readSuppliers(CompressedObject<List<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<SimSession> paramCompressedObject2) throws Exception;
  
  CompressedObject<List<Supplier>> readSuppliers2(CompressedObject<List<String>> paramCompressedObject, CompressedObject<Long> paramCompressedObject1, CompressedObject<Boolean> paramCompressedObject2, CompressedObject<SimSession> paramCompressedObject3) throws Exception;
  
  CompressedObject<WarehouseDetailVO> readWarehouseDetail(CompressedObject<String> paramCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\ejb\SourceInterface.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */