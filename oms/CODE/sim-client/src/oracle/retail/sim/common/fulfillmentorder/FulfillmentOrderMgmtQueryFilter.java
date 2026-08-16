package oracle.retail.sim.common.fulfillmentorder;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;
import oracle.retail.sim.common.rules.fulfillmentorder.FulfillmentOrderMgmtSearchLimitRule;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;

public class FulfillmentOrderMgmtQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = 796445764306773407L;
  
  private Date fromDate;
  
  private Date toDate;
  
  private FulfillmentOrderTranType tranType;
  
  private FulfillmentOrderMgmtQueryStatus status;
  
  private String tranId;
  
  private Integer tranStatusCode;
  
  private String customerOrderId;
  
  private Long storeId;
  
  private Integer searchLimit = Integer.valueOf(500);
  
  private ShipmentCarrier shipmentCarrier;
  
  private ShipmentCarrierService shipmentCarrierService;
  
  private String userId;
  
  private String itemId;
  
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
  
  public void doSetDateRange(Date paramDate1, Date paramDate2) {
    this.fromDate = paramDate1;
    this.toDate = paramDate2;
  }
  
  public Integer getTranStatusCode() {
    return this.tranStatusCode;
  }
  
  public void setTranStatusCode(Integer paramInteger) throws BusinessException {
    executeRule("setTranStatusCode", new Object[] { paramInteger });
    doSetTranStatusCode(paramInteger);
  }
  
  public void doSetTranStatusCode(Integer paramInteger) {
    this.tranStatusCode = paramInteger;
  }
  
  public FulfillmentOrderTranType getTranType() {
    return this.tranType;
  }
  
  public void setTranType(FulfillmentOrderTranType paramFulfillmentOrderTranType) throws BusinessException {
    executeRule("setTranType", new Object[] { paramFulfillmentOrderTranType });
    doSetTranType(paramFulfillmentOrderTranType);
  }
  
  public void doSetTranType(FulfillmentOrderTranType paramFulfillmentOrderTranType) {
    this.tranType = paramFulfillmentOrderTranType;
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
  
  public String getTranId() {
    return this.tranId;
  }
  
  public void setTranId(String paramString) throws BusinessException {
    executeRule("setTranId", new Object[] { paramString });
    doSetTranId(paramString);
  }
  
  public void doSetTranId(String paramString) {
    this.tranId = StringHelper.trimToNull(paramString);
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public int getSearchLimit() {
    return this.searchLimit.intValue();
  }
  
  public void setSearchLimit(int paramInt) throws BusinessException {
    FulfillmentOrderMgmtSearchLimitRule.execute(Integer.valueOf(paramInt));
    executeRule("setSearchLimit", new Object[] { Integer.valueOf(paramInt) });
    doSetSearchLimit(paramInt);
  }
  
  public void doSetSearchLimit(int paramInt) {
    this.searchLimit = Integer.valueOf(paramInt);
  }
  
  public FulfillmentOrderMgmtQueryStatus getStatus() {
    return this.status;
  }
  
  public void setStatus(FulfillmentOrderMgmtQueryStatus paramFulfillmentOrderMgmtQueryStatus) throws BusinessException {
    executeRule("setStatus", new Object[] { paramFulfillmentOrderMgmtQueryStatus });
    doSetStatus(paramFulfillmentOrderMgmtQueryStatus);
  }
  
  public void doSetStatus(FulfillmentOrderMgmtQueryStatus paramFulfillmentOrderMgmtQueryStatus) {
    this.status = paramFulfillmentOrderMgmtQueryStatus;
  }
  
  public ShipmentCarrier getShipmentCarrier() {
    return this.shipmentCarrier;
  }
  
  public void setShipmentCarrier(ShipmentCarrier paramShipmentCarrier) throws BusinessException {
    executeRule("setShipmentCarrier", new Object[] { paramShipmentCarrier });
    doSetShipmentCarrier(paramShipmentCarrier);
  }
  
  public void doSetShipmentCarrier(ShipmentCarrier paramShipmentCarrier) {
    this.shipmentCarrier = paramShipmentCarrier;
  }
  
  public ShipmentCarrierService getShipmentCarrierService() {
    return this.shipmentCarrierService;
  }
  
  public void setShipmentCarrierService(ShipmentCarrierService paramShipmentCarrierService) throws BusinessException {
    executeRule("setShipmentCarrierService", new Object[] { paramShipmentCarrierService });
    doSetShipmentCarrierService(paramShipmentCarrierService);
  }
  
  public void doSetShipmentCarrierService(ShipmentCarrierService paramShipmentCarrierService) {
    this.shipmentCarrierService = paramShipmentCarrierService;
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
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrderMgmtQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */