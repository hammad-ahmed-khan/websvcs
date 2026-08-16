package extra.retail.sim.service.item;

import java.util.List;

import extra.retail.sim.server.dataaccess.ExtraDAOFactory;

/**
 * ItemBinLocationServerServices.java
 * aibrahim
 * 2024
 */
public class ItemBinLocationServerServices extends ItemBinLocationServices {

	@Override
	public List<String> getItemBinLocations(Long storeId, String item) throws Exception {
		return ExtraDAOFactory.getItemBinLocationDao().getItemBinLocations(storeId, item);
	}

	@Override
	public void saveItemBinLocation(Long storeId, String item, List<String> locationIds) throws Exception {
		ExtraDAOFactory.getItemBinLocationDao().mergeItemBinLocation(storeId, item, locationIds);
	}
}
