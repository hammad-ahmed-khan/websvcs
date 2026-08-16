package extra.retail.sim.webservice.imeifulfillmentorderdelivery.model;

import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;

public class IMEIFulfillmentOrderDelivery {
	private Long id;
	private Long fulOrdId;
	private Long fulOrdDlvId;
	private Long fulOrdDlvLineItemId;
	private String custOrdId;
	private Long storeId;
	private String itemId;
	private String imeiNumber;
	private Double quantity;
	private String itemDescription;
	private FulfillmentOrderStatus OrderStatus;
	private String pickedQuantity;
	private int pickQuantity;
	private int orderedQuantity;
	
	
    
	public int getOrderedQuantity() {
		return orderedQuantity;
	}

	public void setOrderedQuantity(int orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}

	public int getPickQuantity() {
		return pickQuantity;
	}

	public void setPickQuantity(int pickQuantity) {
		this.pickQuantity = pickQuantity;
	}

	public Long getFulOrdDlvLineItemId() {
		return fulOrdDlvLineItemId;
	}

	public void setFulOrdDlvLineItemId(Long fulOrdDlvLineItemId) {
		this.fulOrdDlvLineItemId = fulOrdDlvLineItemId;
	}

	public String getPickedQuantity() {
		return pickedQuantity;
	}

	public void setPickedQuantity(String pickedQuantity) {
		this.pickedQuantity = pickedQuantity;
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

	public Double getQuantity() {
		return quantity;
	}

	public void setQuantity(Double quantity) {
		this.quantity = quantity;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public FulfillmentOrderStatus getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(FulfillmentOrderStatus orderStatus) {
		OrderStatus = orderStatus;
	}

}
