package extra.retail.sim.client.screen.imeifulfillmentorderdelivery;

import extra.retail.sim.webservice.imeifulfillmentorderdelivery.model.IMEIFulfillmentOrderDelivery;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;

/********************************************************************************************************
 * Fulfillment Order Delivery Line Item Wrapper
 * <p>
 * Wraps a FulfillmentOrderDeliveryLineItem, the FulfillmentOrderDelivery it is
 * on, the FulfillmentOrder the delivery was created for, and the
 * FulfillmentOrderLineItem associated to the delivery line item into a class
 * for presentation on the PC client.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class IMEIFulfillmentOrderDeliveryLineItemWrapper {

	private IMEIFulfillmentOrderDelivery bean;

	public IMEIFulfillmentOrderDeliveryLineItemWrapper(IMEIFulfillmentOrderDelivery bean) {
		this.bean = bean;
	}

	public Long getId() {
		return bean.getId();
	}

	public Long getFulOrdId() {
		return bean.getFulOrdId();
	}

	public Long getFulOrdDlvId() {
		return bean.getFulOrdDlvId();
	}

	public String getCustOrdId() {
		return bean.getCustOrdId();
	}

	public Long getStoreId() {
		return bean.getStoreId();
	}

	public String getItemId() {
		return bean.getItemId();
	}

	public String getImeiNumber() {
		return bean.getImeiNumber();
	}

	public Double getQuantity() {
		return bean.getQuantity();
	}

	public String getItemDescription() {
		return bean.getItemDescription();
	}

	public FulfillmentOrderStatus getOrderStatus() {
		return bean.getOrderStatus();
	}

	public void setImeiNumber(String imeiNumber){
	       bean.setImeiNumber(imeiNumber);
	}
	
/*	public void setPickedQuantity(String pickedQuantity){
	       bean.setPickedQuantity(pickedQuantity);
	}
	*/
	public String getPickedQuantity() {
		return "1";
	}
	


}
