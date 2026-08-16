package oracle.retail.sim.common.shelfreplenishment;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;

public class ShelfReplenishmentQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = 7474560479915295017L;
  
  private String itemId;
  
  private Long shelfReplenishmentId;
  
  private Long productGroupId;
  
  private Long storeId;
  
  private ShelfReplenishmentStatus status = null;
  
  private ShelfReplenishmentType type = null;
  
  private String userId = null;
  
  private Date fromDate;
  
  private Date toDate;
  
  private int searchLimit;
  
  private boolean querySuccessful;
  
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
  
  public void setShelfReplenishmentId(Long paramLong) throws BusinessException {
    executeRule("setShelfReplenishmentId", new Object[] { paramLong });
    doSetShelfReplenishmentId(paramLong);
  }
  
  public void doSetShelfReplenishmentId(Long paramLong) {
    this.shelfReplenishmentId = paramLong;
  }
  
  public Long getShelfReplenishmentId() {
    return this.shelfReplenishmentId;
  }
  
  public void setProductGroupId(Long paramLong) throws BusinessException {
    executeRule("setProductGroupID", new Object[] { paramLong });
    doSetProductGroupId(paramLong);
  }
  
  public void doSetProductGroupId(Long paramLong) {
    this.productGroupId = paramLong;
  }
  
  public Long getProductGroupID() {
    return this.productGroupId;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public ShelfReplenishmentStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(ShelfReplenishmentStatus paramShelfReplenishmentStatus) throws BusinessException {
    executeRule("setStatus", new Object[] { paramShelfReplenishmentStatus });
    doSetStatus(paramShelfReplenishmentStatus);
  }
  
  public void doSetStatus(ShelfReplenishmentStatus paramShelfReplenishmentStatus) {
    this.status = paramShelfReplenishmentStatus;
  }
  
  public void setType(ShelfReplenishmentType paramShelfReplenishmentType) throws BusinessException {
    executeRule("setType", new Object[] { paramShelfReplenishmentType });
    doSetType(paramShelfReplenishmentType);
  }
  
  public void doSetType(ShelfReplenishmentType paramShelfReplenishmentType) {
    this.type = paramShelfReplenishmentType;
  }
  
  public ShelfReplenishmentType getType() {
    return this.type;
  }
  
  public void setUserId(String paramString) throws BusinessException {
    executeRule("setUserId", new Object[] { paramString });
    doSetUserId(paramString);
  }
  
  public void doSetUserId(String paramString) {
    this.userId = StringHelper.trimToNull(paramString);
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void setSearchLimit(int paramInt) throws BusinessException {
    executeRule("setSearchLimit", new Object[] { Integer.valueOf(paramInt) });
    doSetSearchLimit(paramInt);
  }
  
  public int getSearchLimit() {
    return this.searchLimit;
  }
  
  public void doSetSearchLimit(int paramInt) {
    this.searchLimit = paramInt;
  }
  
  public Date getFromDate() {
    return this.fromDate;
  }
  
  public Date getToDate() {
    return this.toDate;
  }
  
  public void setDateRange(Date paramDate1, Date paramDate2) throws BusinessException {
    DateRangeValidRule.execute(paramDate1, paramDate2);
    executeRule("setDateRange", new Object[] { paramDate1, paramDate2 });
    doSetDateRange(paramDate1, paramDate2);
  }
  
  public void doSetDateRange(Date paramDate1, Date paramDate2) throws BusinessException {
    this.fromDate = paramDate1;
    this.toDate = paramDate2;
  }
  
  public void doSetQuerySuccessful(boolean paramBoolean) {
    this.querySuccessful = paramBoolean;
  }
  
  public boolean isQuerySuccessful() {
    return this.querySuccessful;
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\shelfreplenishment\ShelfReplenishmentQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */