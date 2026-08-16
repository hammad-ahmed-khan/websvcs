package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Remote;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;

/**
 * ExtraConfigurationInterface.java
 * aibrahim
 * 2024
 */
@Remote
public interface ExtraConfigurationInterface {

	CompressedObject<List<Long>> getTsfRestrictStores(CompressedObject<SimSession> session) throws Exception;
}
