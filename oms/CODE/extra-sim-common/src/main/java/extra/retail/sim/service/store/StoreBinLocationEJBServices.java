package extra.retail.sim.service.store;

import java.util.List;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;

import extra.retail.sim.service.ejb.StoreBinLocationInterface;

/**
 * StoreBinLocationEJBServices.java
 * aibrahim
 * 2024
 */
public class StoreBinLocationEJBServices extends StoreBinLocationServices {

	private StoreBinLocationInterface lookup() throws Exception {
		try {
			return (StoreBinLocationInterface) JndiServiceManager.cachedLookup("StoreBinLocationBean", StoreBinLocationInterface.class);
		} catch (Throwable throwable) {
			throw new DowntimeException("An error occurred accessing StoreBinLocationBean. Please contact your system administrator.", throwable);
		}
	}

	private void removeCache() {
		JndiServiceManager.removeCache("StoreBinLocationBean");
	}

	@Override
	public List<String> getStoreBinLocations(Long storeId) throws Exception {
		CompressedObject<SimSession> compressedObjectSession = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<Long> compressedObjectStore = new CompressedObject<Long>(storeId);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "StoreBinLocationBean.getStoreBinLocations(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { compressedObjectStore, compressedObjectSession }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			StoreBinLocationInterface storeBinLocationInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<String>> compressedObject = storeBinLocationInterface.getStoreBinLocations(compressedObjectStore, compressedObjectSession);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "StoreBinLocationBean.getStoreBinLocations(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "StoreBinLocationBean.getStoreBinLocations(compressed0, compressedSimSession) (remote call) received "
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
		throw new DowntimeException("An error occurred accessing StoreBinLocationBean. Please contact your system administrator.", throwable);
	}
}
