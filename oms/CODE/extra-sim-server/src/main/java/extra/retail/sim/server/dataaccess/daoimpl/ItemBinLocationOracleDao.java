package extra.retail.sim.server.dataaccess.daoimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.server.dataaccess.BaseOracleDao;
import oracle.retail.sim.server.dataaccess.BatchParametricStatement;
import oracle.retail.sim.server.dataaccess.ParametricStatement;

import extra.retail.sim.server.dataaccess.dao.ItemBinLocationDao;
import extra.retail.sim.server.dataaccess.databean.generated.ItemBinLocationDataBean;

/**
 * ItemBinLocationOracleDao.java aibrahim 2024
 */
public class ItemBinLocationOracleDao extends BaseOracleDao implements ItemBinLocationDao {

	@Override
	public List<String> getItemBinLocations(Long storeId, String item) throws SimServerException {
		List<Object> paramList = new ArrayList<>();
		StringBuilder queryBuilder = new StringBuilder(ItemBinLocationDataBean.SELECT_SQL);
		addWhere(queryBuilder, "STORE_ID", storeId, paramList);
		addAnd(queryBuilder, "ITEM", item, paramList);
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

	@Override
	public void mergeItemBinLocation(Long storeId, String item, List<String> locationIds) throws SimServerException {
		BatchParametricStatement batchParametricStatement = new BatchParametricStatement(ItemBinLocationDataBean.INSERT_SQL);
		for (String location : locationIds) {
			batchParametricStatement.addParams(fromObjectToBean(storeId, item, location).toList(false));
		}
		removeItemBinLocation(storeId, item);
		executeBatch(batchParametricStatement);
	}

	private void removeItemBinLocation(Long storeId, String item) throws SimServerException {
		StringBuilder queryBuilder = new StringBuilder(ItemBinLocationDataBean.DELETE_SQL);
		List<Object> paramList = new ArrayList<>();
		addWhere(queryBuilder, "STORE_ID", storeId, paramList);
		addAnd(queryBuilder, "ITEM", item, paramList);
		execute(new ParametricStatement(queryBuilder.toString(), paramList));
	}

	private ItemBinLocationDataBean fromObjectToBean(Long storeId, String item, String location) {
		ItemBinLocationDataBean bean = new ItemBinLocationDataBean();
		bean.setStoreId(storeId);
		bean.setItem(item);
		bean.setBin(location);
		return bean;
	}
}
