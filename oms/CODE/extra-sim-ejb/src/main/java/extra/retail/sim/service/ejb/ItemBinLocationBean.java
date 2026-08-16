package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Stateless;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.service.core.BaseServiceSessionBean;

import extra.retail.sim.service.core.ExtraServerServiceFactory;

@Stateless(mappedName = "ItemBinLocationBean")
public class ItemBinLocationBean extends BaseServiceSessionBean implements ItemBinLocationInterface {

	@Override
	public CompressedObject<List<String>> getItemBinLocations(CompressedObject<Long> storeIdCompressedObject, CompressedObject<String> itemCompressedObject, CompressedObject<SimSession> sessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ItemBinLocationBean.getItemBinLocations()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionCompressedObject.recoverObject());
			return new CompressedObject<List<String>>(
					ExtraServerServiceFactory.getItemBinLocationServices().getItemBinLocations(storeIdCompressedObject.recoverObject(), itemCompressedObject.recoverObject()));
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ItemBinLocationBean.getItemBinLocations()");
			}
		}
	}

	@Override
	public void saveItemBinLocation(CompressedObject<Long> storeIdCompressedObject, CompressedObject<String> itemCompressedObject, CompressedObject<List<String>> locationIdsCompressedObject, CompressedObject<SimSession> sessionCompressedObject) throws Exception {
		long l = System.currentTimeMillis();
		if (LogService.isDebugEnabled("service-timings")) {
			LogService.debug("service-timings", "Starting service method: ItemBinLocationBean.saveItemBinLocation()");
		}
		try {
			UniversalContext.setSession((SimSession) sessionCompressedObject.recoverObject());
			ExtraServerServiceFactory.getItemBinLocationServices().saveItemBinLocation(storeIdCompressedObject.recoverObject(), itemCompressedObject.recoverObject(), locationIdsCompressedObject.recoverObject());
		} catch (Throwable throwable) {
			rollbackSession();
			throw buildException(throwable);
		} finally {
			completeServiceContext();
			long l1 = System.currentTimeMillis();
			if (LogService.isDebugEnabled("service-timings")) {
				LogService.debug("service-timings", "Took " + (l1 - l) + "ms. for invocation of: ItemBinLocationBean.saveItemBinLocation()");
			}
		}
	}
}
