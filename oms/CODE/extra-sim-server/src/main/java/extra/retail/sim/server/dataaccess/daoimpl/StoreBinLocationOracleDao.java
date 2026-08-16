package extra.retail.sim.server.dataaccess.daoimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.server.dataaccess.BaseOracleDao;

import extra.retail.sim.server.dataaccess.dao.StoreBinLocationDao;
import extra.retail.sim.server.dataaccess.databean.generated.StoreBinLocationDataBean;

/**
 * StoreBinLocationOracleDao.java
 * aibrahim
 * 2024
 */
public class StoreBinLocationOracleDao extends BaseOracleDao implements StoreBinLocationDao {

	@Override
	public List<String> getStoreBinLocations(Long storeId) throws SimServerException {
		List<Object> paramList = new ArrayList<>(1);
		StringBuilder queryBuilder = new StringBuilder(StoreBinLocationDataBean.SELECT_SQL);
		addWhere(queryBuilder, "STORE_ID", storeId, paramList);
		List<String> list = queryForStrings(queryBuilder.toString(), paramList);
		if (list.isEmpty()) {
			return Collections.emptyList();
		}
		List<String> binList = new ArrayList<>();
		for (String bean : list) {
			binList.add(bean.trim());
		}
		return Collections.unmodifiableList(binList);
	}
}
