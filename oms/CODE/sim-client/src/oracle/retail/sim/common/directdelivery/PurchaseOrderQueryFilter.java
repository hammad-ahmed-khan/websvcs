package oracle.retail.sim.common.directdelivery;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;

public class PurchaseOrderQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = 1142318496698318186L;
  
  private PurchaseOrderStatus status;
  
  private Long storeId;
  
  private String supplierId;
  
  private String externalId;
  
  private String itemId;
  
  private Date fromDate;
  
  private Date toDate;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private boolean anyFulfillmentOrder;
  
  public PurchaseOrderStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(PurchaseOrderStatus paramPurchaseOrderStatus) {
    this.status = paramPurchaseOrderStatus;
  }
  
  public void setStatus(PurchaseOrderStatus paramPurchaseOrderStatus) throws BusinessException {
    executeRule("setStatus", new Object[] { paramPurchaseOrderStatus });
    doSetStatus(paramPurchaseOrderStatus);
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
  
  public String getItemId() {
    return this.itemId;
  }
  
  public void doSetItemId(String paramString) {
    this.itemId = StringHelper.trimToNull(paramString);
  }
  
  public void setItemId(String paramString) throws BusinessException {
    executeRule("setItemId", new Object[] { paramString });
    doSetItemId(paramString);
  }
  
  public Date getFromDate() {
    return this.fromDate;
  }
  
  public Date getToDate() {
    return this.toDate;
  }
  
  public void doSetDateRange(Date paramDate1, Date paramDate2) {
    this.fromDate = paramDate1;
    this.toDate = paramDate2;
  }
  
  public void setDateRange(Date paramDate1, Date paramDate2) throws BusinessException {
    DateRangeValidRule.execute(paramDate1, paramDate2);
    executeRule("setDateRange", new Object[] { paramDate1, paramDate2 });
    doSetDateRange(paramDate1, paramDate2);
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = StringHelper.trimToNull(paramString);
  }
  
  public void setCustomerOrderId(String paramString) throws BusinessException {
    executeRule("setCustomerOrderId", new Object[] { paramString });
    doSetCustomerOrderId(paramString);
  }
  
  public String getFulfillmentOrderExternalId() {
    return this.fulfillmentOrderExternalId;
  }
  
  public void doSetFulfillmentOrderExternalId(String paramString) {
    this.fulfillmentOrderExternalId = StringHelper.trimToNull(paramString);
  }
  
  public void setFulfillmentOrderExternalId(String paramString) throws BusinessException {
    executeRule("setFulfillmentOrderExternalId", new Object[] { paramString });
    doSetFulfillmentOrderExternalId(paramString);
  }
  
  public boolean isAnyFulfillmentOrder() {
    return this.anyFulfillmentOrder;
  }
  
  public void doSetAnyFulfillmentOrder(boolean paramBoolean) {
    this.anyFulfillmentOrder = paramBoolean;
  }
  
  public void setAnyFulfillmentOrder(boolean paramBoolean) throws BusinessException {
    executeRule("setAnyFulfillmentOrder", new Object[] { Boolean.valueOf(paramBoolean) });
    doSetAnyFulfillmentOrder(paramBoolean);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\PurchaseOrderQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */