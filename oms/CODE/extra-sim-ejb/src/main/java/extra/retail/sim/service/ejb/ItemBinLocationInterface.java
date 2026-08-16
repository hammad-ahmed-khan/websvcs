package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Remote;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;

/**
 * ItemBinLocationInterface.java
 * aibrahim
 * 2024
 */
@Remote
public interface ItemBinLocationInterface {

	CompressedObject<List<String>> getItemBinLocations(CompressedObject<Long> storeIdCompressedObject, CompressedObject<String> itemCompressedObject, CompressedObject<SimSession> sessionCompressedObject) throws Exception;

	void saveItemBinLocation(CompressedObject<Long> storeId, CompressedObject<String> item, CompressedObject<List<String>> locationIds, CompressedObject<SimSession> sessionCompressedObject) throws Exception;
}
