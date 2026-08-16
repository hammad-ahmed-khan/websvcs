package extra.retail.sim.common.spareparts;

import java.io.Serializable;

import oracle.retail.sim.common.business.Quantity;

/**
 * ReturnRequest.java
 * aibrahim
 * 2024
 */
public class ReturnRequestItem implements Serializable {

	private static final long serialVersionUID = 3562117620381817331L;

	private Long returnId;

	private Long storeId;

	private String sourceId;

	private String itemId;

	private Quantity qty;

	private String status = "NEW";

	public Long getReturnId() {
		return returnId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public String getSourceId() {
		return sourceId;
	}

	public String getItemId() {
		return itemId;
	}

	public Quantity getQty() {
		return qty;
	}

	public String getStatus() {
		return status;
	}

	public void setReturnId(Long returnId) {
		this.returnId = returnId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public void setQty(Quantity qty) {
		this.qty = qty;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
