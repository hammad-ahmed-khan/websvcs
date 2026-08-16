package extra.retail.sim.service.config;

import java.util.List;

import extra.retail.sim.server.dataaccess.ExtraDAOFactory;

/**
 * ExtraConfigurationServices.java
 * aibrahim
 * 2024
 */
public class ExtraConfigurationServerServices extends ExtraConfigurationServices {

	public List<Long> getTsfRestrictStores() throws Exception {
		return ExtraDAOFactory.getExtraConfigurationDao().getTsfRestrictStores();
	}
}
