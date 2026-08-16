package oracle.retail.sim.common.directdelivery;

import java.util.Date;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.QueryFilter;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.rules.core.DateRangeValidRule;

public class DirectDeliveryQueryFilter extends BusinessObject implements QueryFilter {
  private static final long serialVersionUID = -4766029950911309798L;
  
  private Long storeId;
  
  private Date fromDate;
  
  private Date toDate;
  
  private DirectDeliveryStatus status;
  
  private String supplierId;
  
  private String invoiceNumber;
  
  private String purchaseOrderExternalId;
  
  private String asnId;
  
  private String userId;
  
  private String itemId;
  
  private String customerOrderId;
  
  private String fulfillmentOrderExternalId;
  
  private boolean anyFulfillmentOrder;
  
  private Long purchaseOrderId;
  
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
  
  public DirectDeliveryStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(DirectDeliveryStatus paramDirectDeliveryStatus) {
    this.status = paramDirectDeliveryStatus;
  }
  
  public void setStatus(DirectDeliveryStatus paramDirectDeliveryStatus) throws BusinessException {
    executeRule("setStatus", new Object[] { paramDirectDeliveryStatus });
    doSetStatus(paramDirectDeliveryStatus);
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
  
  public String getInvoiceNumber() {
    return this.invoiceNumber;
  }
  
  public void doSetInvoiceNumber(String paramString) {
    this.invoiceNumber = StringHelper.trimToNull(paramString);
  }
  
  public void setInvoiceNumber(String paramString) throws BusinessException {
    executeRule("setInvoiceNumber", new Object[] { paramString });
    doSetInvoiceNumber(paramString);
  }
  
  public String getPurchaseOrderExternalId() {
    return this.purchaseOrderExternalId;
  }
  
  public void doSetPurchaseOrderExternalId(String paramString) {
    this.purchaseOrderExternalId = StringHelper.trimToNull(paramString);
  }
  
  public void setPurchaseOrderExternalId(String paramString) throws BusinessException {
    executeRule("setPurchaseOrderExternalId", new Object[] { paramString });
    doSetPurchaseOrderExternalId(paramString);
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
  
  public Long getPurchaseOrderId() {
    return this.purchaseOrderId;
  }
  
  public void doSetPurchaseOrderId(Long paramLong) {
    this.purchaseOrderId = paramLong;
  }
  
  public void setPurchaseOrderId(Long paramLong) throws BusinessException {
    executeRule("setPurchaseOrderId", new Object[] { paramLong });
    doSetPurchaseOrderId(paramLong);
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


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\directdelivery\DirectDeliveryQueryFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */