package extra.retail.sim.service.shipment;

import java.util.Collection;
import java.util.List;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.fulfillmentorderdelivery.FulfillmentOrderDelivery;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;

import extra.retail.sim.common.imei.UniqueSerialNumber;
import extra.retail.sim.common.shipment.AWBRequest;
import extra.retail.sim.service.ejb.ShipmentOrderInterface;

/**
 * aibrahim
 * 2023
 */
public class ShipmentOrderEJBServices extends ShipmentOrderServices {

	private ShipmentOrderInterface lookup() throws Exception {
		try {
			return (ShipmentOrderInterface) JndiServiceManager.cachedLookup("ShipmentOrderBean", ShipmentOrderInterface.class);
		} catch (Throwable throwable) {
			throw new DowntimeException("An error occurred accessing FulfillmentOrderServices. Please contact your system administrator.", throwable);
		}
	}

	private void removeCache() {
		JndiServiceManager.removeCache("ShipmentOrderBean");
	}

	@Override
	public FulfillmentOrderDelivery getPendingDeliveryItems(Long fulfillmentOrderId) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> fulfillmentOrderIdObject = new CompressedObject<Long>(fulfillmentOrderId);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ShipmentOrderBean.getPendingDeliveryItems(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { fulfillmentOrderIdObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ShipmentOrderInterface shipmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<FulfillmentOrderDelivery> compressedObject = shipmentOrderInterface.getPendingDeliveryItems(fulfillmentOrderIdObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ShipmentOrderBean.getPendingDeliveryItems(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ShipmentOrderBean.getPendingDeliveryItems(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return (FulfillmentOrderDelivery) compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ShipmentOrderOrderServices. Please contact your system administrator.", throwable);
	}

	@Override
	public AWBRequest getAWBDetail(Long pickId) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> pickIdObject = new CompressedObject<Long>(pickId);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ShipmentOrderBean.getAWBDetail(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { pickIdObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ShipmentOrderInterface shipmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<AWBRequest> compressedObject = shipmentOrderInterface.getAWBDetail(pickIdObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ShipmentOrderBean.getAWBDetail(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ShipmentOrderBean.getPendingDeliveryItems(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return (AWBRequest) compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ShipmentOrderOrderServices. Please contact your system administrator.", throwable);
	}

	@Override
	public String requestAWB(AWBRequest awbRequest) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<AWBRequest> awbCompressedObject = new CompressedObject<AWBRequest>(awbRequest);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ShipmentOrderBean.requestAWB(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { awbCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ShipmentOrderInterface shipmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<String> compressedObject = shipmentOrderInterface.requestAWB(awbCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ShipmentOrderBean.requestAWB(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ShipmentOrderBean.requestAWB(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return (String) compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ShipmentOrderOrderServices. Please contact your system administrator.", throwable);
	}

	@Override
	public void cancelAWB(List<String> trackingNumbers) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<List<String>> awbCompressedObject = new CompressedObject<List<String>>(trackingNumbers);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ShipmentOrderBean.cancelAWB(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { awbCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ShipmentOrderInterface shipmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<Void> compressedObject = shipmentOrderInterface.cancelAWB(awbCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ShipmentOrderBean.cancelAWB(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ShipmentOrderBean.cancelAWB(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return;
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ShipmentOrderOrderServices. Please contact your system administrator.", throwable);
	}

	@Override
	public void printAWB(Long storeId, String trackingNumber) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> storeIdCompressedObject = new CompressedObject<Long>(storeId);
		CompressedObject<String> awbCompressedObject = new CompressedObject<String>(trackingNumber);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ShipmentOrderBean.printAWB(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { awbCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ShipmentOrderInterface shipmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<Void> compressedObject = shipmentOrderInterface.printAWB(storeIdCompressedObject, awbCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ShipmentOrderBean.printAWB(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ShipmentOrderBean.printAWB(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return;
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ShipmentOrderOrderServices. Please contact your system administrator.", throwable);
	}

	@Override
	public List<String> lookupIMEIEnableItems(Long storeId, Collection<String> items) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> storeIdCompressedObject = new CompressedObject<Long>(storeId);
		CompressedObject<Collection<String>> itemsCompressedObject = new CompressedObject<Collection<String>>(items);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ShipmentOrderBean.lookupIMEIEnableItems(compressed0, compressed1, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { storeIdCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ShipmentOrderInterface shipmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<String>> compressedObject = shipmentOrderInterface.lookupIMEIEnableItems(storeIdCompressedObject, itemsCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ShipmentOrderBean.lookupIMEIEnableItems(compressed0, compressed1, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ShipmentOrderBean.lookupIMEIEnableItems(compressed0, compressed1, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ShipmentOrderOrderServices. Please contact your system administrator.", throwable);
	}

	@Override
	public List<UniqueSerialNumber> getIMEIDetail(Collection<Long> dlvLineItemIds) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Collection<Long>> imeiCompressedObject = new CompressedObject<Collection<Long>>(dlvLineItemIds);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ShipmentOrderBean.getIMEIDetail(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { imeiCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ShipmentOrderInterface shipmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<UniqueSerialNumber>> compressedObject = shipmentOrderInterface.getIMEIDetail(imeiCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ShipmentOrderBean.getIMEIDetail(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ShipmentOrderBean.getIMEIDetail(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ShipmentOrderOrderServices. Please contact your system administrator.", throwable);
	}

	@Override
	public void updateHTC(String trackingNumber) throws Exception {
		CompressedObject<SimSession> sessionObject = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<String> awbCompressedObject = new CompressedObject<String>(trackingNumber);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ShipmentOrderBean.updateHTC(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { awbCompressedObject, sessionObject }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ShipmentOrderInterface shipmentOrderInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<Void> compressedObject = shipmentOrderInterface.updateHTC(awbCompressedObject, sessionObject);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ShipmentOrderBean.updateHTC(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ShipmentOrderBean.updateHTC(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return;
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ShipmentOrderOrderServices. Please contact your system administrator.", throwable);
	}
}
