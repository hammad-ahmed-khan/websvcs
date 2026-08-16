package oracle.retail.sim.common.warehousedelivery;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;

public class WarehouseDeliveryCartonQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = -122127277148797845L;
  
  private String externalId;
  
  private boolean loadCountData;
  
  private Long storeId;
  
  private String uinValue;
  
  private int searchLimit = 30;
  
  public String getExternalId() {
    return this.externalId;
  }
  
  public void doSetExternalId(String paramString) {
    this.externalId = StringHelper.trimToNull(paramString);
  }
  
  public void setExternalId(String paramString) throws BusinessException {
    executeRule("setExternalId", new Object[] { paramString });
    doSetExternalId(paramString);
  }
  
  public boolean isLoadCountData() {
    return this.loadCountData;
  }
  
  public void doSetLoadCountData(boolean paramBoolean) {
    this.loadCountData = paramBoolean;
  }
  
  public void setLoadCountData(boolean paramBoolean) throws BusinessException {
    executeRule("setLoadCountData", new Object[] { Boolean.valueOf(paramBoolean) });
    doSetLoadCountData(paramBoolean);
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
  
  public String getUinValue() {
    return this.uinValue;
  }
  
  public void doSetUinValue(String paramString) {
    this.uinValue = StringHelper.trimToNull(paramString);
  }
  
  public void setUinValue(String paramString) throws BusinessException {
    executeRule("setUinValue", new Object[] { paramString });
    doSetUinValue(paramString);
  }
  
  public int getSearchLimit() {
    return this.searchLimit;
  }
  
  public void doSetSearchLimit(int paramInt) {
    this.searchLimit = paramInt;
  }
  
  public void setSearchLimit(int paramInt1, int paramInt2) throws BusinessException {
    if (paramInt1 <= 0)
      throw new BusinessException(CommonMessageText.ITEM_UNDER_SEARCH_LIMIT, new Object[] { Long.valueOf(0L) }); 
    if (paramInt1 > paramInt2)
      throw new BusinessException(CommonMessageText.ITEM_OVER_SEARCH_LIMIT, new Object[] { Long.valueOf(paramInt2) }); 
    executeRule("setSearchLimit", new Object[] { Integer.valueOf(paramInt1), Integer.valueOf(paramInt2) });
    doSetSearchLimit(paramInt1);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryCartonQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */