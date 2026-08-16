package extra.retail.sim.service.ejb;

import java.util.Collection;
import java.util.List;

import javax.ejb.Remote;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;

import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.shipment.AWBRequest;

/**
 * ShipmentOrderInterface.java
 * aibrahim
 * 2023
 */
@Remote
public interface ShipmentOrderInterface {

	CompressedObject<FulfillmentOrderDelivery> getPendingDeliveryItems(CompressedObject<Long> fulfillmentOrderId, CompressedObject<SimSession> paramCompressedObject1) throws Exception;

	CompressedObject<AWBRequest> getAWBDetail(CompressedObject<Long> pickId, CompressedObject<SimSession> paramCompressedObject1) throws Exception;

	CompressedObject<List<UniqueSerialNumber>> getIMEIDetail(CompressedObject<Collection<Long>> dlvLineItemIds, CompressedObject<SimSession> paramCompressedObject1) throws Exception;

	CompressedObject<List<String>> lookupIMEIEnableItems(CompressedObject<Long> storeId, CompressedObject<Collection<String>> itemsCompressedObject, CompressedObject<SimSession> paramCompressedObject1) throws Exception;

	CompressedObject<String> requestAWB(CompressedObject<AWBRequest> awbRequestCompressedObject, CompressedObject<SimSession> simSessionCompressedObject) throws Exception;

	CompressedObject<Void> cancelAWB(CompressedObject<List<String>> awbCompressedObject, CompressedObject<SimSession> simSessionCompressedObject) throws Exception;

	CompressedObject<Void> printAWB(CompressedObject<Long> storeIdCompressedObject, CompressedObject<String> awbCompressedObject, CompressedObject<SimSession> simSessionCompressedObject) throws Exception;

	CompressedObject<Void> updateHTC(CompressedObject<String> awbCompressedObject, CompressedObject<SimSession> simSessionCompressedObject) throws Exception;
}
