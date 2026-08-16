package extra.retail.sim.common.imei;

import java.io.Serializable;

public class UniqueSerialNumber implements Serializable {

	private static final long serialVersionUID = 3278010309973846579L;

	private Long id;
	private Long fulOrdId;
	private Long fulOrdDlvId;
	private Long fulOrdDlvLineItemId;
	private String custOrdId;
	private Long storeId;
	private String itemId;
	private String imeiNumber;
	private int quantity;

	public Long getFulOrdDlvLineItemId() {
		return fulOrdDlvLineItemId;
	}

	public void setFulOrdDlvLineItemId(Long fulOrdDlvLineItemId) {
		this.fulOrdDlvLineItemId = fulOrdDlvLineItemId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFulOrdId() {
		return fulOrdId;
	}

	public void setFulOrdId(Long fulOrdId) {
		this.fulOrdId = fulOrdId;
	}

	public Long getFulOrdDlvId() {
		return fulOrdDlvId;
	}

	public void setFulOrdDlvId(Long fulOrdDlvId) {
		this.fulOrdDlvId = fulOrdDlvId;
	}

	public String getCustOrdId() {
		return custOrdId;
	}

	public void setCustOrdId(String custOrdId) {
		this.custOrdId = custOrdId;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getImeiNumber() {
		return imeiNumber;
	}

	public void setImeiNumber(String imeiNumber) {
		this.imeiNumber = imeiNumber;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
