package oracle.retail.sim.common.fulfillmentorderpick;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;

public class FulfillmentOrderPickQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = 4807999582686737102L;
  
  private Date fromDate = null;
  
  private Date toDate = null;
  
  private Long pickId = null;
  
  private Long simCustomerOrderId = null;
  
  private String fulfillmentOrderId = null;
  
  private String customerOrderId = null;
  
  private String binId = null;
  
  private String itemId = null;
  
  private FulfillmentOrderPickStatus status;
  
  private Long storeId = null;
  
  private FulfillmentOrderPickType type;
  
  private String userId = null;
  
  public Date getFromDate() {
    return this.fromDate;
  }
  
  public Date getToDate() {
    return this.toDate;
  }
  
  public void setDateRange(Date paramDate1, Date paramDate2) throws BusinessException {
    DateRangeValidRule.execute(paramDate1, paramDate2);
    Object[] arrayOfObject = { paramDate1, paramDate2 };
    executeRule("setDateRange", arrayOfObject);
    doSetDateRange(paramDate1, paramDate2);
  }
  
  public void doSetDateRange(Date paramDate1, Date paramDate2) throws BusinessException {
    this.fromDate = paramDate1;
    this.toDate = paramDate2;
  }
  
  public Long getPickId() {
    return this.pickId;
  }
  
  public void setPickId(Long paramLong) throws BusinessException {
    executeRule("setPickId", new Object[] { paramLong });
    doSetPickId(paramLong);
  }
  
  public void doSetPickId(Long paramLong) {
    this.pickId = paramLong;
  }
  
  public Long getSimCustomerOrderId() {
    return this.simCustomerOrderId;
  }
  
  public void setSimCustomerOrderId(Long paramLong) throws BusinessException {
    executeRule("setSimCustomerOrderId", new Object[] { paramLong });
    doSetSimCustomerOrderId(paramLong);
  }
  
  public void doSetSimCustomerOrderId(Long paramLong) throws BusinessException {
    this.simCustomerOrderId = paramLong;
  }
  
  public String getFulfillmentOrderId() {
    return this.fulfillmentOrderId;
  }
  
  public void setFulfillmentOrderId(String paramString) throws BusinessException {
    executeRule("setFulfillmentOrderId", new Object[] { paramString });
    doSetFulfillmentOrderId(paramString);
  }
  
  public void doSetFulfillmentOrderId(String paramString) throws BusinessException {
    this.fulfillmentOrderId = StringHelper.trimToNull(paramString);
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void setCustomerOrderId(String paramString) throws BusinessException {
    executeRule("setCustomerOrderId", new Object[] { paramString });
    doSetCustomerOrderId(paramString);
  }
  
  public void doSetCustomerOrderId(String paramString) throws BusinessException {
    this.customerOrderId = StringHelper.trimToNull(paramString);
  }
  
  public String getBinId() {
    return this.binId;
  }
  
  public void setBinId(String paramString) throws BusinessException {
    executeRule("setBinId", new Object[] { paramString });
    doSetBinId(paramString);
  }
  
  public void doSetBinId(String paramString) throws BusinessException {
    this.binId = StringHelper.trimToNull(paramString);
  }
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void setItemId(String paramString) throws BusinessException {
    executeRule("setItemId", new Object[] { paramString });
    doSetItemId(paramString);
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = StringHelper.trimToNull(paramString);
  }
  
  public FulfillmentOrderPickStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(FulfillmentOrderPickStatus paramFulfillmentOrderPickStatus) throws BusinessException {
    executeRule("setStatus", new Object[] { paramFulfillmentOrderPickStatus });
    doSetStatus(paramFulfillmentOrderPickStatus);
  }
  
  public void doSetStatus(FulfillmentOrderPickStatus paramFulfillmentOrderPickStatus) {
    this.status = paramFulfillmentOrderPickStatus;
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
  
  public FulfillmentOrderPickType getType() {
    return this.type;
  }
  
  public void setType(FulfillmentOrderPickType paramFulfillmentOrderPickType) throws BusinessException {
    executeRule("setType", new Object[] { paramFulfillmentOrderPickType });
    doSetType(paramFulfillmentOrderPickType);
  }
  
  public void doSetType(FulfillmentOrderPickType paramFulfillmentOrderPickType) {
    this.type = paramFulfillmentOrderPickType;
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void setUserId(String paramString) throws BusinessException {
    executeRule("setUserId", new Object[] { paramString });
    doSetUserId(paramString);
  }
  
  public void doSetUserId(String paramString) {
    this.userId = StringHelper.trimToNull(paramString);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorderpick\FulfillmentOrderPickQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */