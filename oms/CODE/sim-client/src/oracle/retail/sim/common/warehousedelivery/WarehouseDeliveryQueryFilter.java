package oracle.retail.sim.common.warehousedelivery;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.SourceType;

public class WarehouseDeliveryQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = 1045040518188045946L;
  
  private Long storeId;
  
  private Date fromDate;
  
  private Date toDate;
  
  private WarehouseDeliveryStatus status;
  
  private String sourceId;
  
  private SourceType sourceType;
  
  private String asnId;
  
  private String userId;
  
  private String itemId;
  
  private ContextType contextType;
  
  private String contextValue;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private boolean anyFulfillmentOrder;
  
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
  
  public WarehouseDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(WarehouseDeliveryStatus paramWarehouseDeliveryStatus) {
    this.status = paramWarehouseDeliveryStatus;
  }
  
  public void setStatus(WarehouseDeliveryStatus paramWarehouseDeliveryStatus) throws BusinessException {
    executeRule("setStatus", new Object[] { paramWarehouseDeliveryStatus });
    doSetStatus(paramWarehouseDeliveryStatus);
  }
  
  public String getSourceId() {
    return this.sourceId;
  }
  
  public void doSetSourceId(String paramString) {
    this.sourceId = StringHelper.trimToNull(paramString);
  }
  
  public void setSourceId(String paramString) throws BusinessException {
    executeRule("setSourceId", new Object[] { paramString });
    doSetSourceId(paramString);
  }
  
  public SourceType getSourceType() {
    return this.sourceType;
  }
  
  public void doSetSourceType(SourceType paramSourceType) {
    this.sourceType = paramSourceType;
  }
  
  public void setSourceType(SourceType paramSourceType) throws BusinessException {
    executeRule("setSourceType", new Object[] { paramSourceType });
    doSetSourceType(paramSourceType);
  }
  
  public String getAsnId() {
    return this.asnId;
  }
  
  public void doSetAsnId(String paramString) {
    this.asnId = StringHelper.trimToNull(paramString);
  }
  
  public void setAsnId(String paramString) throws BusinessException {
    executeRule("setAsnId", new Object[] { paramString });
    doSetAsnId(paramString);
  }
  
  public String getUserId() {
    return this.userId;
  }
  
  public void doSetUserId(String paramString) {
    this.userId = StringHelper.trimToNull(paramString);
  }
  
  public void setUserId(String paramString) throws BusinessException {
    executeRule("setUserId", new Object[] { paramString });
    doSetUserId(paramString);
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
  
  public ContextType getContextType() {
    return this.contextType;
  }
  
  public void doSetContextType(ContextType paramContextType) {
    this.contextType = paramContextType;
  }
  
  public void setContextType(ContextType paramContextType) throws BusinessException {
    executeRule("setContextType", new Object[] { paramContextType });
    doSetContextType(paramContextType);
  }
  
  public String getContextValue() {
    return this.contextValue;
  }
  
  public void doSetContextValue(String paramString) {
    this.contextValue = StringHelper.trimToNull(paramString);
  }
  
  public void setContextValue(String paramString) throws BusinessException {
    executeRule("setContextValue", new Object[] { paramString });
    doSetContextValue(paramString);
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


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\warehousedelivery\WarehouseDeliveryQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */