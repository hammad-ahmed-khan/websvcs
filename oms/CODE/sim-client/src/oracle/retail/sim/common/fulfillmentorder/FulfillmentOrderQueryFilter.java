package oracle.retail.sim.common.fulfillmentorder;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;

public class FulfillmentOrderQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = 796445764306773407L;
  
  private Date fromDate;
  
  private Date toDate;
  
  private Long fulfillmentOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private String customerOrderId;
  
  private String binId;
  
  private String customerName;
  
  private String trackingId;
  
  private String itemId;
  
  private Long storeId;
  
  private FulfillmentOrderStatus status;
  
  private FulfillmentOrderType orderType;
  
  private String carrierCode;
  
  private String serviceCode;
  
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
  
  public void doSetDateRange(Date paramDate1, Date paramDate2) {
    this.fromDate = paramDate1;
    this.toDate = paramDate2;
  }
  
  public Long getFulfillmentOrderId() {
    return this.fulfillmentOrderId;
  }
  
  public void setFulfillmentOrderId(Long paramLong) throws BusinessException {
    executeRule("setFulfillmentOrderId", new Object[] { paramLong });
    doSetFulfillmentOrderId(paramLong);
  }
  
  public void doSetFulfillmentOrderId(Long paramLong) {
    this.fulfillmentOrderId = paramLong;
  }
  
  public String getFulfillmentOrderExternalId() {
    return this.fulfillmentOrderExternalId;
  }
  
  public void setFulfillmentOrderExternalId(String paramString) throws BusinessException {
    executeRule("setFulfillmentOrderExternalId", new Object[] { paramString });
    doSetFulfillmentOrderExternalId(paramString);
  }
  
  public void doSetFulfillmentOrderExternalId(String paramString) {
    this.fulfillmentOrderExternalId = StringHelper.trimToNull(paramString);
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void setCustomerOrderId(String paramString) throws BusinessException {
    executeRule("setCustomerOrderId", new Object[] { paramString });
    doSetCustomerOrderId(paramString);
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = StringHelper.trimToNull(paramString);
  }
  
  public String getBinId() {
    return this.binId;
  }
  
  public void setBinId(String paramString) throws BusinessException {
    executeRule("setBinId", new Object[] { paramString });
    doSetBinId(paramString);
  }
  
  public void doSetBinId(String paramString) {
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
  
  public String getCustomerName() {
    return this.customerName;
  }
  
  public void setCustomerName(String paramString) throws BusinessException {
    executeRule("setCustomerName", new Object[] { paramString });
    doSetCustomerName(paramString);
  }
  
  public void doSetCustomerName(String paramString) {
    this.customerName = StringHelper.trimToNull(paramString);
  }
  
  public String getTrackingId() {
    return this.trackingId;
  }
  
  public void setTrackingId(String paramString) throws BusinessException {
    executeRule("setTrackingId", new Object[] { paramString });
    doSetTrackingId(paramString);
  }
  
  public void doSetTrackingId(String paramString) {
    this.trackingId = paramString;
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
  
  public FulfillmentOrderType getOrderType() {
    return this.orderType;
  }
  
  public void setOrderType(FulfillmentOrderType paramFulfillmentOrderType) throws BusinessException {
    executeRule("setOrderType", new Object[] { paramFulfillmentOrderType });
    doSetOrderType(paramFulfillmentOrderType);
  }
  
  public void doSetOrderType(FulfillmentOrderType paramFulfillmentOrderType) {
    this.orderType = paramFulfillmentOrderType;
  }
  
  public FulfillmentOrderStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(FulfillmentOrderStatus paramFulfillmentOrderStatus) throws BusinessException {
    executeRule("setStatus", new Object[] { paramFulfillmentOrderStatus });
    doSetStatus(paramFulfillmentOrderStatus);
  }
  
  public void doSetStatus(FulfillmentOrderStatus paramFulfillmentOrderStatus) {
    this.status = paramFulfillmentOrderStatus;
  }
  
  public String getCarrierCode() {
    return this.carrierCode;
  }
  
  public void setCarrierCode(String paramString) throws BusinessException {
    executeRule("setCarrierCode", new Object[] { paramString });
    doSetCarrierCode(paramString);
  }
  
  public void doSetCarrierCode(String paramString) {
    this.carrierCode = StringHelper.trimToNull(paramString);
  }
  
  public String getServiceCode() {
    return this.serviceCode;
  }
  
  public void setServiceCode(String paramString) throws BusinessException {
    executeRule("setServiceCode", new Object[] { paramString });
    doSetServiceCode(paramString);
  }
  
  public void doSetServiceCode(String paramString) {
    this.serviceCode = StringHelper.trimToNull(paramString);
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */