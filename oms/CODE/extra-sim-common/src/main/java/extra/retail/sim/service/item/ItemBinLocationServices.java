package extra.retail.sim.service.item;

import java.util.List;

/**
 * BinLookupLocationServices.java
 * aibrahim
 * 2024
 */
public abstract class ItemBinLocationServices {

	public abstract List<String> getItemBinLocations(Long storeId, String item) throws Exception;

	public abstract void saveItemBinLocation(Long storeId, String item, List<String> locationIds) throws Exception;
}
