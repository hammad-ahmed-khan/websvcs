package extra.retail.sim.service.fulfillmentorder;

import java.util.List;

import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;
import extra.retail.sim.server.dataaccess.ExtraDAOFactory;

public class ExtraFulfillmentOrderServerServices extends ExtraFulfillmentOrderServices {

	public List<ExtraFulfillmentOrderVO> findFulfillmentOrderVOs(ExtraFulfillmentOrderQueryFilter paramFulfillmentOrderQueryFilter) throws Exception {
		return ExtraDAOFactory.getFulfillmentOrderDao().selectFulfillmentOrders(paramFulfillmentOrderQueryFilter);
	}

	@Override
	public ExtraFulfillmentOrder readFulfillmentOrder(Long paramLong) throws Exception {
		return ExtraDAOFactory.getFulfillmentOrderDao().selectFulfillmentOrder(paramLong);
	}
}
