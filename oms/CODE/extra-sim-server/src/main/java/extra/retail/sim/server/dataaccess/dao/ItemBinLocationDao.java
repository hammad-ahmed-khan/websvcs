package extra.retail.sim.server.dataaccess.dao;

import java.util.List;

import oracle.retail.sim.common.core.SimServerException;

/**
 * ItemBinLocationDao.java
 * aibrahim
 * 2024
 */
public interface ItemBinLocationDao {

	List<String> getItemBinLocations(Long storeId, String item) throws SimServerException;

	void mergeItemBinLocation(Long storeId, String item, List<String> locationIds) throws SimServerException;
}
