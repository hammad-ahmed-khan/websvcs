package extra.retail.sim.server.dataaccess.daoimpl;

import java.util.Collections;
import java.util.List;

import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.server.dataaccess.BaseOracleDao;

import extra.retail.sim.server.dataaccess.dao.ExtraConfigurationDao;

/**
 * ExtraConfigurationOracleDao.java
 * aibrahim
 * 2024
 */
public class ExtraConfigurationOracleDao extends BaseOracleDao implements ExtraConfigurationDao {

	private static final String TSF_RESTRICT_STORE_SQL = "SELECT * FROM XTRAINT.XX_TSF_RESTRICT_LOC";

	@Override
	public List<Long> getTsfRestrictStores() throws SimServerException {
		return queryForLongs(TSF_RESTRICT_STORE_SQL, Collections.emptyList());
	}
}
