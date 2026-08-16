package extra.retail.sim.client.screen.shipment;

import java.math.BigDecimal;
import java.util.Objects;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.business.Wrapper;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrder;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItem;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDeliveryStatus;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPick;
import oracle.retail.sim.common.fulfillmentorderpick.FulfillmentOrderPickLineItem;

import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.common.shipment.AWBRequestLineItem;

/**
 * aibrahim 
 * 2023
 */
public class FulfillmentOrderShipmentItemWrapper extends Wrapper {

	private FulfillmentOrder fulfillmentOrder;

	private FulfillmentOrderLineItem orderLineItem;

	private FulfillmentOrderPick pick;

	private FulfillmentOrderPickLineItem pickLineItem;

	private FulfillmentOrderDelivery delivery;

	private FulfillmentOrderDeliveryLineItem deliveryLineItem;

	private AWBRequestLineItem awbRequestLineItem;

	private Integer cartonNumber;

	private String uniqueSerialNumber;

	private Integer seqNo;

	public Long getFulfillmentOrderId() {
		return this.fulfillmentOrder.getId();
	}

	public Long getFulfillmentPickId() {
		return this.pick.getId();
	}

	public String getItemId() {
		return this.orderLineItem.getItemId();
	}

	public Long getFulfilemntLineItemId() {
		return this.orderLineItem.getId();
	}

	public Long getDeliveryLineItemId() {
		return this.deliveryLineItem != null ? this.deliveryLineItem.getId() : null;
	}

	public BigDecimal getQtyPicked() {
		return BigDecimal.ONE;
	}

	public String getUniqueSerialNumber() {
		return uniqueSerialNumber;
	}

	public String getTrackingNumber() {
		return this.awbRequestLineItem != null ? this.awbRequestLineItem.getAwbNumber() : null;
	}

	public void setFulfillmentOrder(FulfillmentOrder fulfillmentOrder) {
		this.fulfillmentOrder = fulfillmentOrder;
	}

	public void setOrderLineItem(FulfillmentOrderLineItem orderLineItem) {
		this.orderLineItem = orderLineItem;
	}

	public void setPick(FulfillmentOrderPick pick) {
		this.pick = pick;
	}

	public void setPickLineItem(FulfillmentOrderPickLineItem pickLineItem) {
		this.pickLineItem = pickLineItem;
	}

	public void setDelivery(FulfillmentOrderDelivery delivery) {
		this.delivery = delivery;
	}

	public void setDeliveryLineItem(FulfillmentOrderDeliveryLineItem deliveryLineItem) {
		this.deliveryLineItem = deliveryLineItem;
	}

	public void setUniqueSerialNumber(String uniqueSerialNumber) {
		this.uniqueSerialNumber = uniqueSerialNumber;
	}

	public Integer getCartonNumber() {
		return cartonNumber;
	}

	public void setCartonNumber(Integer cartonNumber) {
		this.cartonNumber = cartonNumber;
	}

	public Long getDeliveryId() {
		return this.delivery != null ? this.delivery.getId() : null;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getCustomerOrderID() {
		return this.fulfillmentOrder.getCustomerOrderId();
	}

	public boolean isDispatched() {
		return this.delivery != null && this.delivery.getStatus().equals(FulfillmentOrderDeliveryStatus.COMPLETED);
	}

	@Override
	public int hashCode() {
		return Objects.hash(seqNo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof FulfillmentOrderShipmentItemWrapper))
			return false;
		FulfillmentOrderShipmentItemWrapper other = (FulfillmentOrderShipmentItemWrapper) obj;
		return Objects.equals(seqNo, other.seqNo);
	}

	public void setTrackingNumber(String trackingNumber) {
		if (StringUtility.isNullOrEmpty(trackingNumber)) {
			this.awbRequestLineItem = null;
		} else {
			this.awbRequestLineItem = ExtraBOFactory.createAWBRequestLineItem();
			this.awbRequestLineItem.setAwbNumber(trackingNumber);
		}
	}

	public Long getPickLineItemId() {
		return this.pickLineItem.getId();
	}

	public void setAwbRequestLineItem(AWBRequestLineItem awbRequestLineItem) {
		this.awbRequestLineItem = awbRequestLineItem;
	}
}
