package extra.retail.sim.service.fulfillmentorder;

import java.util.List;

import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;

public abstract class ExtraFulfillmentOrderServices {
	
	public abstract List<ExtraFulfillmentOrderVO> findFulfillmentOrderVOs(ExtraFulfillmentOrderQueryFilter paramFulfillmentOrderQueryFilter) throws Exception;

	public abstract ExtraFulfillmentOrder readFulfillmentOrder(Long paramLong) throws Exception;
}
