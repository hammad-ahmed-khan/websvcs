package extra.retail.sim.common.shipment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * AWBRequest.java
 * aibrahim
 * 2023
 */
public class AWBRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5396687461544384050L;

	private Long storeId;

	private Long fulOrdId;

	private Long pickId;

	private List<AWBRequestLineItem> lineItems = new ArrayList<>();

	public Long getStoreId() {
		return storeId;
	}

	public Long getFulOrdId() {
		return fulOrdId;
	}

	public Long getPickId() {
		return pickId;
	}

	public List<AWBRequestLineItem> getLineItems() {
		return lineItems;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public void setFulOrdId(Long fulOrdId) {
		this.fulOrdId = fulOrdId;
	}

	public void setPickId(Long pickId) {
		this.pickId = pickId;
	}

	public void setLineItems(List<AWBRequestLineItem> lineItems) {
		this.lineItems = lineItems;
	}
}
