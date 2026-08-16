package extra.retail.sim.service.config;

import java.util.List;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;

import extra.retail.sim.service.ejb.ExtraConfigurationInterface;

/**
 * ExtraConfigurationEJBServices.java
 * aibrahim
 * 2024
 */
public class ExtraConfigurationEJBServices extends ExtraConfigurationServices {

	private ExtraConfigurationInterface lookup() throws Exception {
		try {
			return (ExtraConfigurationInterface) JndiServiceManager.cachedLookup("ExtraConfigurationBean", ExtraConfigurationInterface.class);
		} catch (Throwable throwable) {
			throw new DowntimeException("An error occurred accessing ExtraConfigurationServices. Please contact your system administrator.", throwable);
		}
	}

	private void removeCache() {
		JndiServiceManager.removeCache("ExtraConfigurationBean");
	}

	@Override
	public List<Long> getTsfRestrictStores() throws Exception {
		CompressedObject<SimSession> compressedObjectSession = new CompressedObject<SimSession>(UniversalContext.getSession());
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "ExtraConfigurationBean.getTsfRestrictStores(compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { compressedObjectSession }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			ExtraConfigurationInterface configurationInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<Long>> compressedObject = configurationInterface.getTsfRestrictStores(compressedObjectSession);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "ExtraConfigurationBean.getTsfRestrictStores(compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "ExtraConfigurationBean.getTsfRestrictStores(compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return (List<Long>) compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing ExtraConfigurationServices. Please contact your system administrator.", throwable);
	}
}
