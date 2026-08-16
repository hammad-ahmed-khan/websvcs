package extra.retail.sim.client.screen.item;

import java.util.Collections;
import java.util.List;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemDetailVO;

import extra.retail.sim.service.core.ExtraClientServiceFactory;

/**
 * BinLookupQuantityModel.java aibrahim 2024
 */
public class BinLookupLocationModel extends SimScreenModel {

	private ItemDetailVO itemDetailVO;

	private List<String> storeBinLocations;

	private List<String> itemBinLocations;

	public void setItem(ItemDetailVO item) {
		this.itemDetailVO = item;
	}

	public String getItemId() {
		return itemDetailVO.getId();
	}

	public String getItemDescription() {
		if (SimConfigManager.isItemShortDescription()) {
			return itemDetailVO.getShortDescription();
		}
		return itemDetailVO.getLongDescription();
	}

	public List<String> getStoreBinLocations() throws Exception {
		if (storeBinLocations == null) {
			storeBinLocations = ExtraClientServiceFactory.getStoreBinLocationServices().getStoreBinLocations(getStoreId());
			if (storeBinLocations == null) {
				storeBinLocations = Collections.emptyList();
			}
		}
		return storeBinLocations;
	}

	public List<String> getItemBinLocations() throws Exception {
		if (itemBinLocations == null) {
			itemBinLocations = ExtraClientServiceFactory.getItemBinLocationServices().getItemBinLocations(getStoreId(), getItemId());
			if (itemBinLocations == null) {
				itemBinLocations = Collections.emptyList();
			}
		}
		return itemBinLocations;
	}

	public void saveBinLocations(List<String> binLocations) throws Exception {
		ExtraClientServiceFactory.getItemBinLocationServices().saveItemBinLocation(getStoreId(), getItemId(), binLocations);
	}
}
