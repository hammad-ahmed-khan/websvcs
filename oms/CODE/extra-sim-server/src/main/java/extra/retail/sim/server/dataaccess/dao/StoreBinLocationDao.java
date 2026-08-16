package extra.retail.sim.server.dataaccess.dao;

import java.util.List;

import oracle.retail.sim.common.core.SimServerException;

/**
 * StoreBinLocationDao.java
 * aibrahim
 * 2024
 */
public interface StoreBinLocationDao {

	List<String> getStoreBinLocations(Long storeId) throws SimServerException;
}
