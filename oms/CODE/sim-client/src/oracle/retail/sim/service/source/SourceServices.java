package oracle.retail.sim.service.source;

import java.util.List;
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

public abstract class SourceServices {
  public abstract Supplier readSupplier(String paramString, Long paramLong) throws Exception;
  
  public abstract Supplier readSupplier(String paramString, Long paramLong, boolean paramBoolean) throws Exception;
  
  public abstract List<Supplier> readSuppliers(List<String> paramList, Long paramLong) throws Exception;
  
  public abstract List<Supplier> readSuppliers(List<String> paramList, Long paramLong, boolean paramBoolean) throws Exception;
  
  public abstract List<SupplierVO> findSupplierVOs(SourceQueryFilter paramSourceQueryFilter) throws Exception;
  
  public abstract List<SupplierVO> findSupplierVOs(SourceQueryFilter paramSourceQueryFilter, boolean paramBoolean) throws Exception;
  
  public abstract List<SupplierVO> findSupplierVOs(String paramString, Long paramLong) throws Exception;
  
  public abstract List<Supplier> findSuppliers(String paramString, Long paramLong) throws Exception;
  
  public abstract List<Supplier> findSuppliers(String paramString, Long paramLong, boolean paramBoolean) throws Exception;
  
  public abstract List<String> findAdditionalSupplierIds(String paramString1, Long paramLong, String paramString2) throws Exception;
  
  public abstract List<SupplierContactInfo> readSupplierContactInfo(String paramString) throws Exception;
  
  public abstract WarehouseDetailVO readWarehouseDetail(String paramString) throws Exception;
  
  public abstract List<Warehouse> findAllWarehouses() throws Exception;
  
  public abstract List<WarehouseVO> findWarehouses(SourceQueryFilter paramSourceQueryFilter) throws Exception;
  
  public abstract ContextType readContextType(String paramString) throws Exception;
  
  public abstract List<ContextType> findAllContextTypes() throws Exception;
  
  public abstract List<FinisherVO> findFinisherVOs(SourceQueryFilter paramSourceQueryFilter) throws Exception;
  
  public abstract List<FinisherVO> findFinisherVOs(SourceQueryFilter paramSourceQueryFilter, boolean paramBoolean) throws Exception;
  
  public abstract List<Finisher> findFinishersByItemId(String paramString, Long paramLong) throws Exception;
  
  public abstract Finisher readFinisher(String paramString, Long paramLong) throws Exception;
  
  public abstract List<FinisherContactInfo> readFinisherContactInfo(String paramString) throws Exception;
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\service\source\SourceServices.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */