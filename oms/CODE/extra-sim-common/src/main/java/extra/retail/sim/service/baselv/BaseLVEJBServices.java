package extra.retail.sim.service.baselv;

import java.util.List;

import extra.retail.sim.common.baselv.BaseLV;
import extra.retail.sim.service.ejb.BaseLVInterface;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.DowntimeException;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.common.util.SimObjectUtils;

/**
 * 
 */
public class BaseLVEJBServices extends BaseLVServices {

	private BaseLVInterface lookup() throws Exception {
		try {
			return (BaseLVInterface) JndiServiceManager.cachedLookup("BaseLVBean", BaseLVInterface.class);
		} catch (Throwable throwable) {
			throw new DowntimeException("An error occurred accessing BaseLVServices. Please contact your system administrator.", throwable);
		}
	}

	private void removeCache() {
		JndiServiceManager.removeCache("BaseLVBean");
	}

	@Override
	public List<BaseLV> findBaseListOfValues(String listCode) throws Exception {
		CompressedObject<SimSession> compressedObject1 = new CompressedObject<SimSession>(UniversalContext.getSession());
		CompressedObject<String> compressedObject2 = new CompressedObject<String>(listCode);
		if (LogService.isDebugEnabled("serialized-object-sizes"))
			LogService.debug("serialized-object-sizes", "BaseLVBean.findBaseListOfValues(compressed0, compressedSimSession) (remote call) is sending "
					+ SimObjectUtils.calculateByteSize(new Object[] { compressedObject2, compressedObject1 }) + " bytes in serialized object(s) for the remote call.");
		Throwable throwable = null;
		int i = JndiServiceManager.getMaxConnectAttempts();
		byte b = 0;
		while (b < i) {
			BaseLVInterface baseLVInterface = lookup();
			try {
				long l = System.currentTimeMillis();
				CompressedObject<List<BaseLV>> compressedObject = baseLVInterface.findBaseListOfValues(compressedObject2, compressedObject1);
				if (LogService.isDebugEnabled("service-timings")) {
					long l1 = System.currentTimeMillis();
					LogService.debug("service-timings", "BaseLVBean.findBaseListOfValues(compressed0, compressedSimSession) (remote call) took " + (l1 - l) + "ms to complete");
				}
				if (LogService.isDebugEnabled("serialized-object-sizes"))
					LogService.debug("serialized-object-sizes", "BaseLVBean.findBaseListOfValues(compressed0, compressedSimSession) (remote call) received "
							+ SimObjectUtils.calculateByteSize(compressedObject) + " bytes in the returned serialized object.");
				return (List<BaseLV>) compressedObject.recoverObject();
			} catch (SimServerException | oracle.retail.sim.common.business.BusinessException | SecurityException simServerException) {
				throw simServerException;
			} catch (Throwable throwable1) {
				throwable = throwable1;
				LogService.error(this, "Unexpected error in EJB connection attempt: " + (b + 1), throwable1);
				removeCache();
				b++;
			}
		}
		throw new DowntimeException("An error occurred accessing BaseLVServices. Please contact your system administrator.", throwable);
	}
}
