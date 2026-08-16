package extra.retail.sim.webservice.fulfillmentorderdelivery.model;

public class IMEICancelDelivery {
	private Long fulfillmentOrderId;
	private Long deliveryId;

	public Long getFulfillmentOrderId() {
		return fulfillmentOrderId;
	}

	public void setFulfillmentOrderId(Long fulfillmentOrderId) {
		this.fulfillmentOrderId = fulfillmentOrderId;
	}

	public Long getDeliveryId() {
		return deliveryId;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

}
