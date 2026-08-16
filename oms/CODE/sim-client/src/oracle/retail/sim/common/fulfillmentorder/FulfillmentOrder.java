package oracle.retail.sim.common.fulfillmentorder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.BusinessObject;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryType;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FulfillmentOrder extends BusinessObject {
  private static final long serialVersionUID = -3161990344853467023L;
  
  private Long id;
  
  private String externalId;
  
  private String customerOrderId;
  
  private Long storeId;
  
  private String customerId;
  
  private FulfillmentOrderType orderType;
  
  private FulfillmentOrderStatus status = FulfillmentOrderStatus.NEW;
  
  private String comments;
  
  private Date transactionTimestamp;
  
  private Date createDate;
  
  private Date updateDate;
  
  private Date releaseDate;
  
  private Date deliveryDate;
  
  private SimMoney deliveryCharge;
  
  private FulfillmentOrderDeliveryType deliveryType;
  
  private ShipmentCarrier deliveryCarrier;
  
  private ShipmentCarrierService deliveryService;
  
  private boolean allowPartialDelivery;
  
  private List<FulfillmentOrderLineItem> lineItems = new ArrayList<>();
  
  public boolean isNew() {
    return (this.id == null);
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getIdAsString() {
    return (this.id == null) ? null : this.id.toString();
  }
  
  public String getExternalId() {
    return this.externalId;
  }
  
  public void setExternalId(String paramString) throws BusinessException {
    checkForNullParameter("External Id", paramString);
    executeRule("setExternalId", new Object[] { paramString });
    doSetExternalId(paramString);
  }
  
  public void doSetExternalId(String paramString) {
    this.externalId = paramString;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void setStoreId(Long paramLong) throws BusinessException {
    checkForNullParameter("Store", paramLong);
    executeRule("setStoreId", new Object[] { paramLong });
    doSetStoreId(paramLong);
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void setCustomerOrderId(String paramString) throws BusinessException {
    executeRule("setCustomerOrderId", new Object[] { paramString });
    doSetCustomerOrderId(paramString);
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = paramString;
  }
  
  public String getCustomerId() {
    return this.customerId;
  }
  
  public void setCustomerId(String paramString) throws BusinessException {
    checkForNullParameter("CustomerId", paramString);
    executeRule("setCustomerId", new Object[] { paramString });
    doSetCustomerId(paramString);
  }
  
  public void doSetCustomerId(String paramString) {
    this.customerId = paramString;
  }
  
  public FulfillmentOrderType getOrderType() {
    return this.orderType;
  }
  
  public void setOrderType(FulfillmentOrderType paramFulfillmentOrderType) throws BusinessException {
    checkForNullParameter("Order Type", paramFulfillmentOrderType);
    executeRule("setOrderType", new Object[] { paramFulfillmentOrderType });
    doSetOrderType(paramFulfillmentOrderType);
  }
  
  public void doSetOrderType(FulfillmentOrderType paramFulfillmentOrderType) {
    this.orderType = paramFulfillmentOrderType;
  }
  
  public boolean isWebOrder() {
    return (this.orderType == FulfillmentOrderType.WEB_ORDER);
  }
  
  public Date getTransactionTimestamp() {
    return this.transactionTimestamp;
  }
  
  public void setTransactionTimestamp(Date paramDate) throws BusinessException {
    checkForNullParameter("Transaction Timestamp", paramDate);
    executeRule("setTransactionTimestamp", new Object[] { paramDate });
    doSetTransactionTimestamp(paramDate);
  }
  
  public void doSetTransactionTimestamp(Date paramDate) {
    this.transactionTimestamp = paramDate;
  }
  
  public String getComments() {
    return this.comments;
  }
  
  public void doSetComments(String paramString) {
    this.comments = paramString;
  }
  
  public Date getCreateDate() {
    return this.createDate;
  }
  
  public void doSetCreateDate(Date paramDate) {
    this.createDate = paramDate;
  }
  
  public Date getReleaseDate() {
    return this.releaseDate;
  }
  
  public void doSetReleaseDate(Date paramDate) {
    this.releaseDate = paramDate;
  }
  
  public Date getDeliveryDate() {
    return this.deliveryDate;
  }
  
  public void doSetDeliveryDate(Date paramDate) {
    this.deliveryDate = paramDate;
  }
  
  public FulfillmentOrderStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(FulfillmentOrderStatus paramFulfillmentOrderStatus) {
    this.status = paramFulfillmentOrderStatus;
  }
  
  public Date getUpdateDate() {
    return this.updateDate;
  }
  
  public void doSetUpdateDate(Date paramDate) {
    this.updateDate = paramDate;
  }
  
  public SimMoney getDeliveryCharge() {
    return this.deliveryCharge;
  }
  
  public void doSetDeliveryCharge(SimMoney paramSimMoney) {
    this.deliveryCharge = paramSimMoney;
  }
  
  public FulfillmentOrderDeliveryType getDeliveryType() {
    return this.deliveryType;
  }
  
  public void doSetDeliveryType(FulfillmentOrderDeliveryType paramFulfillmentOrderDeliveryType) {
    this.deliveryType = paramFulfillmentOrderDeliveryType;
  }
  
  public ShipmentCarrier getDeliveryCarrier() {
    return this.deliveryCarrier;
  }
  
  public void doSetDeliveryCarrier(ShipmentCarrier paramShipmentCarrier) {
    this.deliveryCarrier = paramShipmentCarrier;
  }
  
  public ShipmentCarrierService getDeliveryService() {
    return this.deliveryService;
  }
  
  public void doSetDeliveryService(ShipmentCarrierService paramShipmentCarrierService) {
    this.deliveryService = paramShipmentCarrierService;
  }
  
  public boolean isAllowPartialDelivery() {
    return this.allowPartialDelivery;
  }
  
  public void doSetAllowPartialDelivery(boolean paramBoolean) {
    this.allowPartialDelivery = paramBoolean;
  }
  
  public Quantity getRemainingQty(Long paramLong) {
    FulfillmentOrderLineItem fulfillmentOrderLineItem = null;
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem1 : this.lineItems) {
      if (fulfillmentOrderLineItem1.getId().equals(paramLong)) {
        fulfillmentOrderLineItem = fulfillmentOrderLineItem1;
        break;
      } 
    } 
    if (fulfillmentOrderLineItem == null)
      return Quantity.ZERO; 
    if (fulfillmentOrderLineItem.getSubstituteLineItemId() != null) {
      Quantity quantity = fulfillmentOrderLineItem.getPickedQuantity().subtract(fulfillmentOrderLineItem.getDeliveredQuantity());
      return quantity.isNegative() ? Quantity.ZERO : quantity;
    } 
    ArrayList<FulfillmentOrderLineItem> arrayList = new ArrayList();
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem1 : this.lineItems) {
      if (fulfillmentOrderLineItem.getId().equals(fulfillmentOrderLineItem1.getId()) || fulfillmentOrderLineItem.getId().equals(fulfillmentOrderLineItem1.getSubstituteLineItemId()))
        arrayList.add(fulfillmentOrderLineItem1); 
    } 
    if (arrayList.size() == 1) {
      Quantity quantity = Quantity.ZERO;
      if (((FulfillmentOrderLineItem)arrayList.get(0)).getPickedQuantity().subtract(((FulfillmentOrderLineItem)arrayList.get(0)).getOrderedQuantity()).isPositive()) {
        quantity = ((FulfillmentOrderLineItem)arrayList.get(0)).getPickedQuantity().subtract(((FulfillmentOrderLineItem)arrayList.get(0)).getDeliveredQuantity());
      } else {
        quantity = ((FulfillmentOrderLineItem)arrayList.get(0)).getOrderedQuantity().subtract(((FulfillmentOrderLineItem)arrayList.get(0)).getDeliveredQuantity()).subtract(((FulfillmentOrderLineItem)arrayList.get(0)).getCanceledQuantity());
      } 
      return quantity.isNegative() ? Quantity.ZERO : quantity;
    } 
    Quantity quantity1 = Quantity.ZERO;
    Quantity quantity2 = Quantity.ZERO;
    Quantity quantity3 = Quantity.ZERO;
    Quantity quantity4 = Quantity.ZERO;
    Quantity quantity5 = Quantity.ZERO;
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem1 : arrayList) {
      quantity1 = quantity1.add(fulfillmentOrderLineItem1.getOrderedQuantity());
      quantity2 = quantity2.add(fulfillmentOrderLineItem1.getPickedQuantity());
      quantity3 = quantity3.add(fulfillmentOrderLineItem1.getDeliveredQuantity());
      quantity4 = quantity4.add(fulfillmentOrderLineItem1.getCanceledQuantity());
      if (!fulfillmentOrderLineItem.getId().equals(fulfillmentOrderLineItem1.getId()))
        quantity5 = quantity5.add(getRemainingQty(fulfillmentOrderLineItem1.getId())); 
    } 
    Quantity quantity6 = Quantity.ZERO;
    if (quantity2.subtract(quantity1).isPositive()) {
      for (FulfillmentOrderLineItem fulfillmentOrderLineItem1 : arrayList) {
        if (fulfillmentOrderLineItem.getId().equals(fulfillmentOrderLineItem1.getId())) {
          quantity6 = fulfillmentOrderLineItem1.getPickedQuantity().subtract(fulfillmentOrderLineItem1.getDeliveredQuantity());
          break;
        } 
      } 
    } else {
      quantity6 = quantity1.subtract(quantity4).subtract(quantity3).subtract(quantity5);
    } 
    return quantity6.isNegative() ? Quantity.ZERO : quantity6;
  }
  
  public Quantity getRemainingPickQty(Long paramLong) {
    FulfillmentOrderLineItem fulfillmentOrderLineItem = null;
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem1 : this.lineItems) {
      if (fulfillmentOrderLineItem1.getId().equals(paramLong)) {
        fulfillmentOrderLineItem = fulfillmentOrderLineItem1;
        break;
      } 
    } 
    if (fulfillmentOrderLineItem == null)
      return Quantity.ZERO; 
    if (fulfillmentOrderLineItem.isSubstitute())
      return Quantity.ZERO; 
    ArrayList<FulfillmentOrderLineItem> arrayList = new ArrayList();
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem1 : this.lineItems) {
      if (fulfillmentOrderLineItem.getId().equals(fulfillmentOrderLineItem1.getId()) || fulfillmentOrderLineItem.getId().equals(fulfillmentOrderLineItem1.getSubstituteLineItemId()))
        arrayList.add(fulfillmentOrderLineItem1); 
    } 
    Quantity quantity1 = Quantity.ZERO;
    Quantity quantity2 = Quantity.ZERO;
    Quantity quantity3 = Quantity.ZERO;
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem1 : arrayList) {
      quantity1 = quantity1.add(fulfillmentOrderLineItem1.getOrderedQuantity());
      quantity2 = quantity2.add(fulfillmentOrderLineItem1.getPickedQuantity());
      quantity3 = quantity3.add(fulfillmentOrderLineItem1.getCanceledQuantity());
    } 
    Quantity quantity4 = quantity1.subtract(quantity2).subtract(quantity3);
    return quantity4.isNegative() ? Quantity.ZERO : quantity4;
  }
  
  public List<FulfillmentOrderLineItem> getLineItems() {
    return Collections.unmodifiableList(this.lineItems);
  }
  
  public void addLineItem(FulfillmentOrderLineItem paramFulfillmentOrderLineItem) throws BusinessException {
    checkForNullParameter("fulfillment order line item", paramFulfillmentOrderLineItem);
    executeRule("addLineItem", new Object[] { paramFulfillmentOrderLineItem });
    doAddLineItem(paramFulfillmentOrderLineItem);
  }
  
  public void doAddLineItem(FulfillmentOrderLineItem paramFulfillmentOrderLineItem) {
    this.lineItems.add(paramFulfillmentOrderLineItem);
  }
  
  public void doSetLineItems(List<FulfillmentOrderLineItem> paramList) {
    if (paramList != null)
      this.lineItems = paramList; 
  }
  
  public boolean isCoherent() throws BusinessException {
    if (this.lineItems.isEmpty())
      throw new BusinessException(CommonMessageText.LINE_ITEM_NO_ITEM); 
    for (FulfillmentOrderLineItem fulfillmentOrderLineItem : this.lineItems)
      fulfillmentOrderLineItem.isCoherent(); 
    return super.isCoherent();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    FulfillmentOrder fulfillmentOrder = (FulfillmentOrder)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, fulfillmentOrder.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    hashCodeBuilder.append(this.customerOrderId);
    hashCodeBuilder.append(this.externalId);
    return hashCodeBuilder.toHashCode();
  }
}


/* Location:              C:\Users\aibrahim\eclipse-workspace\sim-client\lib\sim-common.jar!\oracle\retail\sim\common\fulfillmentorder\FulfillmentOrder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */