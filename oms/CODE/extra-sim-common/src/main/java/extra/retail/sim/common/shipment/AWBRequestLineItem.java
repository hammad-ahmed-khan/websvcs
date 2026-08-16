package extra.retail.sim.common.shipment;

import java.io.Serializable;

/**
 * AWBRequestLineItem.java
 * aibrahim
 * 2023
 */
public class AWBRequestLineItem implements Serializable {

	private static final long serialVersionUID = 3116245968842780079L;

	private Long deliveryId;

	private Long pickLineItemId;

	private String awbNumber;

	private String item;

	private Integer qty;

	private String cartonNumber;

	public Long getDeliveryId() {
		return deliveryId;
	}

	public Long getPickLineItemId() {
		return pickLineItemId;
	}

	public String getItem() {
		return item;
	}

	public Integer getQty() {
		return qty;
	}

	public String getCartonNumber() {
		return cartonNumber;
	}

	public void setDeliveryId(Long deliveryId) {
		this.deliveryId = deliveryId;
	}

	public void setPickLineItemId(Long pickLineItemId) {
		this.pickLineItemId = pickLineItemId;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public void setCartonNumber(String cartonNumber) {
		this.cartonNumber = cartonNumber;
	}

	public String getAwbNumber() {
		return awbNumber;
	}

	public void setAwbNumber(String awbNumber) {
		this.awbNumber = awbNumber;
	}
}
