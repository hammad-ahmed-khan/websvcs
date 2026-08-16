package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Remote;

import extra.retail.sim.common.baselv.BaseLV;
import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;

/**
 * 
 */
@Remote
public interface BaseLVInterface {

	CompressedObject<List<BaseLV>> findBaseListOfValues(CompressedObject<String> listCode, CompressedObject<SimSession> session) throws Exception;
}
