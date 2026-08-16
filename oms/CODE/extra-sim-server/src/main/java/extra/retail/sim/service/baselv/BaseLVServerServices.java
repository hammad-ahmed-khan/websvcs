package extra.retail.sim.service.baselv;

import java.util.List;

import extra.retail.sim.common.baselv.BaseLV;
import extra.retail.sim.server.dataaccess.ExtraDAOFactory;

/**
 * 
 */
public class BaseLVServerServices extends BaseLVServices {

	@Override
	public List<BaseLV> findBaseListOfValues(String listCode) throws Exception {
		return ExtraDAOFactory.getBaseLVDao().findBaseListOfValues(listCode);
	}
}
