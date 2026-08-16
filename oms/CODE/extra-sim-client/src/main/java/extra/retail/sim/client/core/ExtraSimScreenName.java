package extra.retail.sim.client.core;

import extra.retail.sim.client.screen.fulfillmentorder.ExtraFulfillmentOrderDetailScreen;
import extra.retail.sim.client.screen.item.ExtraItemDetailScreen;
import extra.retail.sim.client.screen.returns.ExtraReturnDetailScreen;
import extra.retail.sim.client.screen.transfer.ExtraTransferDispatchScreen;
import extra.retail.sim.client.screen.transfer.ExtraTransferListScreen;
import extra.retail.sim.client.screen.transfer.ExtraTransferRequestScreen;
import extra.retail.sim.client.screen.transfer.ExtraTransferViewScreen;

public class ExtraSimScreenName {

	public static String RETURN_DETAIL_SCREEN = ExtraReturnDetailScreen.class.getName();

	public static String FULFILLMENT_ORDER_DETAIL_SCREEN = ExtraFulfillmentOrderDetailScreen.class.getName();

	public static String ITEM_DETAIL_SCREEN = ExtraItemDetailScreen.class.getName();

	public static String TRANSFER_DISPATCH_SCREEN = ExtraTransferDispatchScreen.class.getName();

	public static String TRANSFER_LIST_SCREEN = ExtraTransferListScreen.class.getName();

	public static String TRANSFER_REQUEST_SCREEN = ExtraTransferRequestScreen.class.getName();

	public static String TRANSFER_VIEW_SCREEN = ExtraTransferViewScreen.class.getName();

	private ExtraSimScreenName() {
	}
}
