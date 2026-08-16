package extra.retail.sim.server.dataaccess.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;

import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.shipment.AWBRequest;

/**
 * aibrahim
 * 2023
 */
public interface ShipmentOrderDao {

	FulfillmentOrderDelivery getPendingDeliveryItems(Long fulfillmentOrderId) throws SimServerException;

	AWBRequest getAWBDetail(Long pickId) throws SimServerException;

	List<UniqueSerialNumber> getIMEIDetail(Collection<Long> dlvLineItemIds) throws SimServerException;

	List<String> lookupIMEIEnableItems(Long storeId, Collection<String> items) throws SimServerException;

	String requestAWB(AWBRequest awbRequest) throws SimServerException;

	void cancelAWB(List<String> trackingNumbers) throws SimServerException;

	Entry<String, List<String>> getQueueDetail(Long storeId, String trackingNumber) throws SimServerException;

	void updateHTC(String awbNumber) throws SimServerException;
}
