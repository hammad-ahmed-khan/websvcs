package extra.retail.sim.server.dataaccess.dao;

import java.util.List;

import oracle.retail.sim.common.core.SimServerException;

import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrder;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderVO;

public interface ExtraFulfillmentOrderDao {

	List<ExtraFulfillmentOrderVO> selectFulfillmentOrders(ExtraFulfillmentOrderQueryFilter paramFulfillmentOrderQueryFilter) throws SimServerException;

	ExtraFulfillmentOrder selectFulfillmentOrder(Long paramLong) throws SimServerException;
}
