package extra.retail.sim.server.dataaccess.dao;

import java.util.List;

import oracle.retail.sim.common.core.SimServerException;

/**
 * ExtraConfigurationDao.java
 * aibrahim
 * 2024
 */
public interface ExtraConfigurationDao {

	List<Long> getTsfRestrictStores () throws SimServerException;
}
