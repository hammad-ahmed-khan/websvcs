package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Remote;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;

/**
 * StoreBinLocationInterface.java
 * aibrahim
 * 2024
 */
@Remote
public interface StoreBinLocationInterface {

	CompressedObject<List<String>> getStoreBinLocations(CompressedObject<Long> storeIdCompressedObject, CompressedObject<SimSession> sessionCompressedObject) throws Exception;
}
