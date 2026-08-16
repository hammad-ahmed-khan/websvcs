package extra.retail.sim.service.item;

import java.util.List;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;

import extra.retail.sim.service.ejb.ItemBinLocationInterface;

/**
 * ItemBinLocationEJBServices.java
 * aibrahim
 * 2024
 */
public class ItemBinLocationEJBServices extends ItemBinLocationServices {

	private ItemBinLocationInterface lookup() throws Exception {
		try {
			return (ItemBinLocationInterface) JndiServiceManager.cachedLookup("ItemBinLocationBean", ItemBinLocationInterface.class);
		} catch (Throwable throwable) {
			throw new DowntimeException("An error occurred accessing ItemBinLocationBean. Please contact your system administrator.", throwable);
		}
	}

	private void removeCache() {
		JndiServiceManager.removeCache("ItemBinLocationBean");
	}

	@Override
	public List<String> getItemBinLocations(Long storeId, String item) throws Exception {
		CompressedObject<SimSession> compressedObjectSession = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> compressedObjectStore = new CompressedObject<Long>(storeId);
		CompressedObject<String> compressedObjectItem = new CompressedObject<String>(item);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ItemBinLocationBean.getItemBinLocations(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { compressedObjectItem, compressedObjectSession }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ItemBinLocationInterface itemBinLocationInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<String>> compressedObject = itemBinLocationInterface.getItemBinLocations(compressedObjectStore, compressedObjectItem, compressedObjectSession);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ItemBinLocationBean.getItemBinLocations(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ItemBinLocationBean.getItemBinLocations(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return (List<String>) compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ItemBinLocationBean. Please contact your system administrator.", throwable);
	}

	@Override
	public void saveItemBinLocation(Long storeId, String item, List<String> locationIds) throws Exception {
		CompressedObject<SimSession> compressedObjectSession = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> compressedObjectStore = new CompressedObject<>(storeId);
		CompressedObject<String> compressedObjectItem = new CompressedObject<>(item);
		CompressedObject<List<String>> compressedlocationIds = new CompressedObject<>(locationIds);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ItemBinLocationBean.saveItemBinLocation(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { compressedObjectItem, compressedObjectSession }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ItemBinLocationInterface itemBinLocationInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				itemBinLocationInterface.saveItemBinLocation(compressedObjectStore, compressedObjectItem, compressedlocationIds, compressedObjectSession);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ItemBinLocationBean.saveItemBinLocation(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ItemBinLocationBean.getItemBinLocations(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize() + " bytes in the returned serialized object.");
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
		throw new DowntimeException("An error occurred accessing ItemBinLocationBean. Please contact your system administrator.", throwable);
	}
}
