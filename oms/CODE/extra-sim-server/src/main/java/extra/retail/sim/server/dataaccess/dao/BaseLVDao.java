package extra.retail.sim.server.dataaccess.dao;

import java.util.List;

import extra.retail.sim.common.baselv.BaseLV;
import oracle.retail.sim.common.core.SimServerException;

/**
 * 
 */
public interface BaseLVDao {

	List<BaseLV> findBaseListOfValues(String listCode) throws SimServerException;
}
