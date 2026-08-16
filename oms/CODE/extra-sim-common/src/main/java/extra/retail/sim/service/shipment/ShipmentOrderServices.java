package extra.retail.sim.service.shipment;

import java.util.Collection;
import java.util.List;

import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;

import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.shipment.AWBRequest;

/**
 * aibrahim
 * 2023
 */
public abstract class ShipmentOrderServices {

	public abstract FulfillmentOrderDelivery getPendingDeliveryItems(Long fulfillmentOrderId) throws Exception;

	public abstract AWBRequest getAWBDetail(Long pickId) throws Exception;

	public abstract  List<UniqueSerialNumber> getIMEIDetail(Collection<Long> dlvLineItemIds) throws Exception;

	public abstract List<String> lookupIMEIEnableItems(Long storeId, Collection<String> items) throws Exception;

	public abstract String requestAWB(AWBRequest awbRequest) throws Exception;

	public abstract void cancelAWB(List<String> trackingNumber) throws Exception;

	public abstract void printAWB(Long storeId, String trackingNumber) throws Exception;

	public abstract void updateHTC(String recoverObject) throws Exception;
}
