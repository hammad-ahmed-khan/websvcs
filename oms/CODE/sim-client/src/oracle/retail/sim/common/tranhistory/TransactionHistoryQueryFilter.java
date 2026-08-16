package oracle.retail.sim.common.tranhistory;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;

public class TransactionHistoryQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = -1963731853732666809L;
  
  private Long storeId = null;
  
  private Date fromDate = null;
  
  private Date toDate = null;
  
  private TransactionType type;
  
  private String itemId = null;
  
  private String username = null;
  
  private String reasonDescription = null;
  
  private Integer searchLimit = new Integer(500);
  
  public void setStoreId(Long paramLong) throws BusinessException {
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public Date getFromDate() {
    return this.fromDate;
  }
  
  public Date getToDate() {
    return this.toDate;
  }
  
  public void setDateRange(Date paramDate1, Date paramDate2) throws BusinessException {
    Object[] arrayOfObject = { paramDate1, paramDate2 };
    executeRule("setDateRange", arrayOfObject);
    doSetDateRange(paramDate1, paramDate2);
  }
  
  public void doSetDateRange(Date paramDate1, Date paramDate2) {
    this.fromDate = paramDate1;
    this.toDate = paramDate2;
  }
  
  public void setItemId(String paramString) throws BusinessException {
    executeRule("setItemId", new Object[] { paramString });
    doSetItemId(paramString);
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = StringHelper.trimToNull(paramString);
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void setType(TransactionType paramTransactionType) throws BusinessException {
    executeRule("setType", new Object[] { paramTransactionType });
    doSetType(paramTransactionType);
  }
  
  public void doSetType(TransactionType paramTransactionType) {
    this.type = paramTransactionType;
  }
  
  public TransactionType getType() {
    return this.type;
  }
  
  public void setReasonDescription(String paramString) throws BusinessException {
    executeRule("setReasonDescription", new Object[] { paramString });
    doSetReasonDescription(paramString);
  }
  
  public void doSetReasonDescription(String paramString) {
    this.reasonDescription = paramString;
  }
  
  public String getReasonDescription() {
    return this.reasonDescription;
  }
  
  public void setUsername(String paramString) throws BusinessException {
    executeRule("setUsername", new Object[] { paramString });
    doSetUsername(paramString);
  }
  
  public void doSetUsername(String paramString) {
    this.username = StringHelper.trimToNull(paramString);
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public int getSearchLimit() {
    return this.searchLimit.intValue();
  }
  
  public void setSearchLimit(int paramInt) throws BusinessException {
    executeRule("setSearchLimit", new Object[] { Integer.valueOf(paramInt) });
    doSetSearchLimit(paramInt);
  }
  
  public void doSetSearchLimit(int paramInt) {
    this.searchLimit = Integer.valueOf(paramInt);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\tranhistory\TransactionHistoryQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */