package oracle.retail.sim.common.fulfillmentorder;

import java.io.Serializable;
import java.util.Date;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryType;
import oracle.retail.sim.common.shipment.ShipmentCarrier;
import oracle.retail.sim.common.shipment.ShipmentCarrierService;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class FulfillmentOrderVO implements Serializable {
  private static final long serialVersionUID = -1922351267929270673L;
  
  private Long id;
  
  private String externalId;
  
  private String customerOrderId;
  
  private Long storeId;
  
  private FulfillmentOrderType orderType;
  
  private FulfillmentOrderStatus status;
  
  private String comments;
  
  private Date createDate;
  
  private Date updateDate;
  
  private Date releaseDate;
  
  private Date deliveryDate;
  
  private FulfillmentOrderDeliveryType deliveryType;
  
  private ShipmentCarrier deliveryCarrier;
  
  private ShipmentCarrierService deliveryService;
  
  private boolean allowPartialDelivery;
  
  private int lineItemsCount;
  
  public Long getId() {
    return this.id;
  }
  
  public void doSetId(Long paramLong) {
    this.id = paramLong;
  }
  
  public String getCustomerOrderId() {
    return this.customerOrderId;
  }
  
  public void doSetCustomerOrderId(String paramString) {
    this.customerOrderId = paramString;
  }
  
  public String getExternalId() {
    return this.externalId;
  }
  
  public void doSetExternalId(String paramString) {
    this.externalId = paramString;
  }
  
  public Long getStoreId() {
    return this.storeId;
  }
  
  public void doSetStoreId(Long paramLong) {
    this.storeId = paramLong;
  }
  
  public FulfillmentOrderType getOrderType() {
    return this.orderType;
  }
  
  public void doSetOrderType(FulfillmentOrderType paramFulfillmentOrderType) {
    this.orderType = paramFulfillmentOrderType;
  }
  
  public FulfillmentOrderStatus getStatus() {
    return this.status;
  }
  
  public void doSetStatus(FulfillmentOrderStatus paramFulfillmentOrderStatus) {
    this.status = paramFulfillmentOrderStatus;
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
  
  public Date getUpdateDate() {
    return this.updateDate;
  }
  
  public void doSetUpdateDate(Date paramDate) {
    this.updateDate = paramDate;
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
  
  public void doSetLineItemsCount(int paramInt) {
    this.lineItemsCount = paramInt;
  }
  
  public int getLineItemsCount() {
    return this.lineItemsCount;
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
  
  public FulfillmentOrderDeliveryType getDeliveryType() {
    return this.deliveryType;
  }
  
  public void doSetDeliveryType(FulfillmentOrderDeliveryType paramFulfillmentOrderDeliveryType) {
    this.deliveryType = paramFulfillmentOrderDeliveryType;
  }
  
  public boolean isAllowPartialDelivery() {
    return this.allowPartialDelivery;
  }
  
  public void doSetAllowPartialDelivery(boolean paramBoolean) {
    this.allowPartialDelivery = paramBoolean;
  }
  
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("CustomerOrderVO: Id(");
    stringBuilder.append(getId());
    stringBuilder.append(") CustomerOrderId(");
    stringBuilder.append(getCustomerOrderId());
    stringBuilder.append(") StoreId(");
    stringBuilder.append(getStoreId()).append(")");
    return stringBuilder.toString();
  }
  
  public boolean equals(Object paramObject) {
    if (paramObject == this)
      return true; 
    if (paramObject == null || paramObject.getClass() != getClass())
      return false; 
    FulfillmentOrderVO fulfillmentOrderVO = (FulfillmentOrderVO)paramObject;
    EqualsBuilder equalsBuilder = new EqualsBuilder();
    equalsBuilder.append(this.id, fulfillmentOrderVO.id);
    return equalsBuilder.isEquals();
  }
  
  public int hashCode() {
    HashCodeBuilder hashCodeBuilder = new HashCodeBuilder();
    hashCodeBuilder.append(this.id);
    return hashCodeBuilder.toHashCode();
  }
}
