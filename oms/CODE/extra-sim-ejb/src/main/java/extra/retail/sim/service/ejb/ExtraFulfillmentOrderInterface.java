package extra.retail.sim.service.ejb;

import java.util.List;

import javax.ejb.Remote;

import oracle.retail.sim.common.core.CompressedObject;
import oracle.retail.sim.common.core.SimSession;

import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;

@Remote
public interface ExtraFulfillmentOrderInterface {

	CompressedObject<List<ExtraFulfillmentOrderVO>> findFulfillmentOrderVOs(CompressedObject<ExtraFulfillmentOrderQueryFilter> paramCompressedObject,
			CompressedObject<SimSession> paramCompressedObject1) throws Exception;

	CompressedObject<ExtraFulfillmentOrder> readFulfillmentOrder(CompressedObject<Long> compressedObject2, CompressedObject<SimSession> compressedObjectSession) throws Exception;
}
