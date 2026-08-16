package oracle.retail.sim.common.storesequence;

import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;

public class StoreSequenceItemQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = -4234767474712731577L;
  
  private String itemId;
  
  private String itemDescription;
  
  private int searchLimit = -1;
  
  public void setItemId(String paramString) throws BusinessException {
    executeRule("setItemId", new Object[] { paramString });
    doSetItemId(paramString);
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = paramString;
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void setItemDescription(String paramString) throws BusinessException {
    executeRule("setItemDescription", new Object[] { paramString });
    doSetItemDescription(paramString);
  }
  
  public void doSetItemDescription(String paramString) {
    this.itemDescription = paramString;
  }
  
  public String getItemDescription() {
    return this.itemDescription;
  }
  
  public void setSearchLimit(int paramInt) throws BusinessException {
    executeRule("setSearchLimit", new Object[] { Integer.valueOf(paramInt) });
    doSetSearchLimit(paramInt);
  }
  
  public void doSetSearchLimit(int paramInt) {
    this.searchLimit = paramInt;
  }
  
  public int getSearchLimit() {
    return this.searchLimit;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\storesequence\StoreSequenceItemQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */