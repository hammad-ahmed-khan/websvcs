package extra.retail.sim.webservice.fulfillmentorderdelivery.model;

import java.util.List;

import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;

public class ExtraIMEIFulfillmentOrderDelivery {
	private Long fulOrdId;
	private Long fulOrdDlvId;
	private String custOrdId;
	private FulfillmentOrderStatus OrderStatus;
	private Long storeId;
	private List<ExtraFulfillmentOrderLineItem> lineItems;
	private String status;
	
	
	
	public Long getFulOrdDlvId() {
		return fulOrdDlvId;
	}

	public void setFulOrdDlvId(Long fulOrdDlvId) {
		this.fulOrdDlvId = fulOrdDlvId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getFulOrdId() {
		return fulOrdId;
	}

	public void setFulOrdId(Long fulOrdId) {
		this.fulOrdId = fulOrdId;
	}

	public String getCustOrdId() {
		return custOrdId;
	}

	public void setCustOrdId(String custOrdId) {
		this.custOrdId = custOrdId;
	}

	public FulfillmentOrderStatus getOrderStatus() {
		return OrderStatus;
	}

	public void setOrderStatus(FulfillmentOrderStatus orderStatus) {
		OrderStatus = orderStatus;
	}

	public Long getStoreId() {
		return storeId;
	}

	public void setStoreId(Long storeId) {
		this.storeId = storeId;
	}

	public List<ExtraFulfillmentOrderLineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<ExtraFulfillmentOrderLineItem> lineItems) {
		this.lineItems = lineItems;
	}

}
