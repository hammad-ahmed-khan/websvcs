package oracle.retail.sim.common.source;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.rules.source.SourceQuerySearchLimitRule;

public class SourceQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = 8924943909784430513L;
  
  private String supplierId;
  
  private String supplierName;
  
  private String finisherId;
  
  private String finisherName;
  
  private String warehouseId;
  
  private String warehouseName;
  
  private Long storeId;
  
  private String itemId;
  
  private boolean includeItemPacks;
  
  private int searchLimit = 999;
  
  public String getSupplierId() {
    return this.supplierId;
  }
  
  public void doSetSupplierId(String paramString) {
    this.supplierId = StringHelper.trimToNull(paramString);
  }
  
  public void setSupplierId(String paramString) throws BusinessException {
    executeRule("setSupplierId", new Object[] { paramString });
    doSetSupplierId(paramString);
  }
  
  public String getSupplierName() {
    return this.supplierName;
  }
  
  public void doSetSupplierName(String paramString) {
    this.supplierName = StringHelper.trimToNull(paramString);
  }
  
  public void setSupplierName(String paramString) throws BusinessException {
    executeRule("setSupplierName", new Object[] { paramString });
    doSetSupplierName(paramString);
  }
  
  public String getFinisherId() {
    return this.finisherId;
  }
  
  public void doSetFinisherId(String paramString) {
    this.finisherId = StringHelper.trimToNull(paramString);
  }
  
  public void setFinisherId(String paramString) throws BusinessException {
    executeRule("setFinisherId", new Object[] { paramString });
    doSetFinisherId(paramString);
  }
  
  public String getFinisherName() {
    return this.finisherName;
  }
  
  public void doSetFinisherName(String paramString) {
    this.finisherName = StringHelper.trimToNull(paramString);
  }
  
  public void setFinisherName(String paramString) throws BusinessException {
    executeRule("setFinisherName", new Object[] { paramString });
    doSetFinisherName(paramString);
  }
  
  public String getWarehouseId() {
    return this.warehouseId;
  }
  
  public void doSetWarehouseId(String paramString) {
    this.warehouseId = StringHelper.trimToNull(paramString);
  }
  
  public void setWarehouseId(String paramString) throws BusinessException {
    executeRule("setWarehouseId", new Object[] { paramString });
    doSetWarehouseId(paramString);
  }
  
  public String getWarehouseName() {
    return this.warehouseName;
  }
  
  public void doSetWarehouseName(String paramString) {
    this.warehouseName = StringHelper.trimToNull(paramString);
  }
  
  public void setWarehouseName(String paramString) throws BusinessException {
    executeRule("setWarehouseName", new Object[] { paramString });
    doSetWarehouseName(paramString);
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public void setItemId(String paramString) throws BusinessException {
    executeRule("setItemId", new Object[] { paramString });
    doSetItemId(paramString);
  }
  
  public boolean isIncludeItemPacks() {
    return this.includeItemPacks;
  }
  
  public void doSetIncludeItemPacks(boolean paramBoolean) {
    this.includeItemPacks = paramBoolean;
  }
  
  public void setIncludeItemPacks(boolean paramBoolean) throws BusinessException {
    executeRule("setIncludeItemPacks", new Object[] { Boolean.valueOf(paramBoolean) });
    doSetIncludeItemPacks(paramBoolean);
  }
  
  public int getSearchLimit() {
    return this.searchLimit;
  }
  
  public void doSetSearchLimit(int paramInt) {
    this.searchLimit = paramInt;
  }
  
  public void setSearchLimit(int paramInt) throws BusinessException {
    SourceQuerySearchLimitRule.execute(Integer.valueOf(paramInt));
    executeRule("setSearchLimit", new Object[] { Integer.valueOf(paramInt) });
    doSetSearchLimit(paramInt);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\source\SourceQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */