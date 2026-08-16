package extra.retail.sim.service.store;

import java.util.List;

/**
 * StoreBinLocationServices.java
 * aibrahim
 * 2024
 */
public abstract class StoreBinLocationServices {

	public abstract List<String> getStoreBinLocations(Long storeId) throws Exception;
}
