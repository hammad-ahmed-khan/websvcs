package extra.retail.sim.server.dataaccess.daoimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import extra.retail.sim.common.baselv.BaseLV;
import extra.retail.sim.common.business.ExtraBOFactory;
import extra.retail.sim.server.dataaccess.dao.BaseLVDao;
import extra.retail.sim.server.dataaccess.databean.generated.BaseLVDataBean;
import oracle.retail.sim.common.core.SimServerException;
import oracle.retail.sim.server.dataaccess.BaseOracleDao;

/**
 * 
 */
public class BaseLVOracleDao extends BaseOracleDao implements BaseLVDao {

	@Override
	public List<BaseLV> findBaseListOfValues(String listCode) throws SimServerException {
		StringBuilder stringBuilder = new StringBuilder(BaseLVDataBean.SELECT_SQL);
	    stringBuilder.append(where("LIST_CODE"));
	    List<BaseLVDataBean> list = query(new BaseLVDataBean(), stringBuilder.toString(), Collections.singletonList(listCode));
	    List<BaseLV> baseLVs = new ArrayList<BaseLV>();
		for (BaseLVDataBean baseLVDataBean : list) {
			baseLVs.add(fromBeanToValueObject(baseLVDataBean));
		}
		return baseLVs;
	}

	private BaseLV fromBeanToValueObject(BaseLVDataBean baseLVDataBean) {
		BaseLV baseLV = ExtraBOFactory.createBaseLV();
		baseLV.setId(baseLVDataBean.getId());
		baseLV.setKey(baseLVDataBean.getKey());
		baseLV.setValue(baseLVDataBean.getValue());
		baseLV.setListCode(baseLVDataBean.getListCode());
		return baseLV;
	}
}
