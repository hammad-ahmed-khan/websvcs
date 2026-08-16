package extra.retail.sim.service.store;

import java.util.List;

import extra.retail.sim.server.dataaccess.ExtraDAOFactory;

/**
 * ItemBinLocationServerServices.java
 * aibrahim
 * 2024
 */
public class StoreBinLocationServerServices extends StoreBinLocationServices {

	@Override
	public List<String> getStoreBinLocations(Long storeId) throws Exception {
		return ExtraDAOFactory.getStoreBinLocationDao().getStoreBinLocations(storeId);
	}
}
