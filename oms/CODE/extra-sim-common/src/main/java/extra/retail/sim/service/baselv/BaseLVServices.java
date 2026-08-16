package extra.retail.sim.service.baselv;

import java.util.List;

import extra.retail.sim.common.baselv.BaseLV;

/**
 * 
 */
public abstract class BaseLVServices {

	public abstract List<BaseLV> findBaseListOfValues(String listCode) throws Exception;
}
