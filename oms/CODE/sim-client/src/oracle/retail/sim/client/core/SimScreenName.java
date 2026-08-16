package oracle.retail.sim.client.core;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.screen.carton.CartonDetailScreen;
import oracle.retail.sim.client.screen.directdelivery.DirectDeliveryAsnListScreen;
import oracle.retail.sim.client.screen.directdelivery.DirectDeliveryDetailScreen;
import oracle.retail.sim.client.screen.directdelivery.DirectDeliveryListScreen;
import oracle.retail.sim.client.screen.directdelivery.PurchaseOrderDetailScreen;
import oracle.retail.sim.client.screen.finisher.FinisherDetailScreen;
import oracle.retail.sim.client.screen.fulfillmentorder.FulfillmentOrderDetailScreen;
import oracle.retail.sim.client.screen.fulfillmentorder.FulfillmentOrderListScreen;
import oracle.retail.sim.client.screen.fulfillmentorderdelivery.FulfillmentOrderDeliveryDetailScreen;
import oracle.retail.sim.client.screen.fulfillmentorderdelivery.FulfillmentOrderDeliveryListScreen;
import oracle.retail.sim.client.screen.fulfillmentorderpick.FulfillmentOrderPickDetailScreen;
import oracle.retail.sim.client.screen.fulfillmentorderpick.FulfillmentOrderPickListScreen;
import oracle.retail.sim.client.screen.fulfillmentorderreversepick.FulfillmentOrderReversePickDetailScreen;
import oracle.retail.sim.client.screen.fulfillmentorderreversepick.FulfillmentOrderReversePickListScreen;
import oracle.retail.sim.client.screen.invadjustment.InventoryAdjustmentDetailScreen;
import oracle.retail.sim.client.screen.invadjustment.InventoryAdjustmentListScreen;
import oracle.retail.sim.client.screen.invadjustment.InventoryTemplateDetailScreen;
import oracle.retail.sim.client.screen.item.ItemCustomerOrderScreen;
import oracle.retail.sim.client.screen.item.ItemDetailScreen;
import oracle.retail.sim.client.screen.item.ItemLookupScreen;
import oracle.retail.sim.client.screen.itemprice.ItemPriceDetailScreen;
import oracle.retail.sim.client.screen.itemrequest.ItemRequestDetailScreen;
import oracle.retail.sim.client.screen.itemticket.ItemTicketDetailScreen;
import oracle.retail.sim.client.screen.login.LogoutScreen;
import oracle.retail.sim.client.screen.productgroup.ProductGroupDetailScreen;
import oracle.retail.sim.client.screen.productgroup.ProductGroupListScreen;
import oracle.retail.sim.client.screen.productgroup.ProductGroupScheduleDetailScreen;
import oracle.retail.sim.client.screen.returns.ReturnDetailScreen;
import oracle.retail.sim.client.screen.returns.ReturnListScreen;
import oracle.retail.sim.client.screen.security.RoleDetailScreen;
import oracle.retail.sim.client.screen.security.UserDetailScreen;
import oracle.retail.sim.client.screen.shelfreplenishment.ShelfReplenishmentDetailScreen;
import oracle.retail.sim.client.screen.stockcount.StockCountAuthorizeScreen;
import oracle.retail.sim.client.screen.stockcount.StockCountChildScreen;
import oracle.retail.sim.client.screen.stockcount.StockCountDetailScreen;
import oracle.retail.sim.client.screen.stockcount.StockCountListScreen;
import oracle.retail.sim.client.screen.storeorder.StoreOrderDetailScreen;
import oracle.retail.sim.client.screen.storesequence.ItemStoreSequenceScreen;
import oracle.retail.sim.client.screen.storesequence.StoreSequenceItemScreen;
import oracle.retail.sim.client.screen.storesequence.StoreSequenceListScreen;
import oracle.retail.sim.client.screen.storesequence.StoreSequenceNoAreaScreen;
import oracle.retail.sim.client.screen.supplier.SupplierDetailScreen;
import oracle.retail.sim.client.screen.supplier.SupplierLookupScreen;
import oracle.retail.sim.client.screen.tranhistory.TransactionHistoryListScreen;
import oracle.retail.sim.client.screen.transfer.TransferApproveScreen;
import oracle.retail.sim.client.screen.transfer.TransferDispatchScreen;
import oracle.retail.sim.client.screen.transfer.TransferListScreen;
import oracle.retail.sim.client.screen.transfer.TransferReceiveScreen;
import oracle.retail.sim.client.screen.transfer.TransferRequestScreen;
import oracle.retail.sim.client.screen.transfer.TransferViewScreen;
import oracle.retail.sim.client.screen.warehousedelivery.WarehouseDeliveryCartonDetailScreen;
import oracle.retail.sim.client.screen.warehousedelivery.WarehouseDeliveryDetailScreen;
import oracle.retail.sim.client.screen.warehousedelivery.WarehouseDeliveryListScreen;
import oracle.retail.sim.common.configutil.ConfigManager;

/********************************************************************************************************
 * Contains global variables to the screens that are navigated to within the actual code base. Although
 * the value of each screen is defaulted to the correct class. During initialization of the client, each
 * screen may be over-loaded from the configuration file
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimScreenName {
    public static String CARTON_DETAIL_SCREEN = CartonDetailScreen.class.getName();
    public static String DIRECT_DELIVERY_ASN_LIST_SCREEN = DirectDeliveryAsnListScreen.class.getName();
    public static String DIRECT_DELIVERY_DETAIL_SCREEN = DirectDeliveryDetailScreen.class.getName();
    public static String DIRECT_DELIVERY_LIST_SCREEN = DirectDeliveryListScreen.class.getName();
    public static String FINISHER_DETAIL_SCREEN = FinisherDetailScreen.class.getName();
    public static String FULFILLMENT_ORDER_DELIVERY_LIST_SCREEN = FulfillmentOrderDeliveryListScreen.class.getName();
    public static String FULFILLMENT_ORDER_DELIVERY_DETAIL_SCREEN = FulfillmentOrderDeliveryDetailScreen.class.getName();
    public static String FULFILLMENT_ORDER_DETAIL_SCREEN = FulfillmentOrderDetailScreen.class.getName();
    public static String FULFILLMENT_ORDER_LIST_SCREEN = FulfillmentOrderListScreen.class.getName();
    public static String FULFILLMENT_ORDER_PICK_LIST_SCREEN = FulfillmentOrderPickListScreen.class.getName();
    public static String FULFILLMENT_ORDER_PICK_DETAIL_SCREEN = FulfillmentOrderPickDetailScreen.class.getName();
    public static String INV_ADJ_TEMPLATE_DETAIL_SCREEN = InventoryTemplateDetailScreen.class.getName();
    public static String INVENTORY_ADJUSTMENT_LIST_SCREEN = InventoryAdjustmentListScreen.class.getName();
    public static String INVENTORY_ADJUSTMENT_DETAIL_SCREEN = InventoryAdjustmentDetailScreen.class.getName();
    public static String ITEM_CUSTOMER_ORDER_SCREEN = ItemCustomerOrderScreen.class.getName();
    public static String ITEM_DETAIL_SCREEN = ItemDetailScreen.class.getName();
    public static String ITEM_LOOKUP_SCREEN = ItemLookupScreen.class.getName();
    public static String ITEM_REQUEST_DETAIL_SCREEN = ItemRequestDetailScreen.class.getName();
    public static String ITEM_STORE_SEQUENCE_SCREEN = ItemStoreSequenceScreen.class.getName();
    public static String ITEM_TICKET_DETAIL_SCREEN = ItemTicketDetailScreen.class.getName();
    public static String LOGOUT_SCREEN = LogoutScreen.class.getName();
    public static String SHELF_REPLENISHMENT_DETAIL_SCREEN = ShelfReplenishmentDetailScreen.class.getName();
    public static String PURCHASE_ORDER_DETAIL_SCREEN = PurchaseOrderDetailScreen.class.getName();
    public static String PRICE_CHANGE_DETAIL_SCREEN = ItemPriceDetailScreen.class.getName();
    public static String PRODUCT_GROUP_DETAIL_SCREEN = ProductGroupDetailScreen.class.getName();
    public static String PRODUCT_GROUP_LIST_SCREEN = ProductGroupListScreen.class.getName();
    public static String PRODUCT_GROUP_SCHEDULE_DETAIL_SCREEN = ProductGroupScheduleDetailScreen.class.getName();
    public static String WAREHOUSE_DELIVERY_CARTON_DETAIL_SCREEN = WarehouseDeliveryCartonDetailScreen.class.getName();
    public static String WAREHOUSE_DELIVERY_DETAIL_SCREEN = WarehouseDeliveryDetailScreen.class.getName();
    public static String WAREHOUSE_DELIVERY_LIST_SCREEN = WarehouseDeliveryListScreen.class.getName();
    public static String RETURN_DETAIL_SCREEN = ReturnDetailScreen.class.getName();
    public static String RETURN_LIST_SCREEN = ReturnListScreen.class.getName();
    public static String ROLE_DETAIL_SCREEN = RoleDetailScreen.class.getName();
    public static String SUPPLIER_DETAIL_SCREEN = SupplierDetailScreen.class.getName();
    public static String SUPPLIER_LOOKUP_SCREEN = SupplierLookupScreen.class.getName();
    public static String STOCK_COUNT_AUTHORIZE_SCREEN = StockCountAuthorizeScreen.class.getName();
    public static String STOCK_COUNT_DETAIL_SCREEN = StockCountDetailScreen.class.getName();
    public static String STOCK_COUNT_LIST_SCREEN = StockCountListScreen.class.getName();
    public static String STOCK_COUNT_LOCATION_SCREEN = StockCountChildScreen.class.getName();
    public static String STORE_ORDER_DETAIL_SCREEN = StoreOrderDetailScreen.class.getName();
    public static String STORE_SEQUENCE_LIST_SCREEN = StoreSequenceListScreen.class.getName();
    public static String STORE_SEQUENCE_ITEM_SCREEN = StoreSequenceItemScreen.class.getName();
    public static String STORE_SEQUENCE_NO_AREA_SCREEN = StoreSequenceNoAreaScreen.class.getName();
    public static String TRANSACTION_HISTORY_SCREEN = TransactionHistoryListScreen.class.getName();
    public static String TRANSFER_APPROVE_SCREEN = TransferApproveScreen.class.getName();
    public static String TRANSFER_DISPATCH_SCREEN = TransferDispatchScreen.class.getName();
    public static String TRANSFER_LIST_SCREEN = TransferListScreen.class.getName();
    public static String TRANSFER_RECEIVE_SCREEN = TransferReceiveScreen.class.getName();
    public static String TRANSFER_REQUEST_SCREEN = TransferRequestScreen.class.getName();
    public static String TRANSFER_VIEW_SCREEN = TransferViewScreen.class.getName();
    public static String USER_DETAIL_SCREEN = UserDetailScreen.class.getName();
    public static String FULFILLMENT_ORDER_REVERSE_PICK_LIST_SCREEN = FulfillmentOrderReversePickListScreen.class.getName();
    public static String FULFILLMENT__ORDER_REVERSE_PICK_DETAIL_SCREEN = FulfillmentOrderReversePickDetailScreen.class.getName();

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    private SimScreenName() {
    }

    /****************************************************************************************************
     * Applies fully qualified screen names from the basic configuration file.
     ***************************************************************************************************/
    public static void applyConfigSettings() {
        ConfigManager manager = Application.getConfigManager();
        CARTON_DETAIL_SCREEN = getScreen(manager, "CARTON_DETAIL_SCREEN", CartonDetailScreen.class);
        DIRECT_DELIVERY_DETAIL_SCREEN = getScreen(manager, "DELIVERY_DETAIL_SCREEN", DirectDeliveryDetailScreen.class);
        INV_ADJ_TEMPLATE_DETAIL_SCREEN = getScreen(manager, "INV_ADJ_TEMPLATE_DETAIL_SCREEN", InventoryTemplateDetailScreen.class);
        INVENTORY_ADJUSTMENT_LIST_SCREEN = getScreen(manager, "INVENTORY_ADJUSTMENT_LIST_SCREEN", InventoryAdjustmentListScreen.class);
        INVENTORY_ADJUSTMENT_DETAIL_SCREEN = getScreen(manager, "INVENTORY_ADJUSTMENT_DETAIL_SCREEN", InventoryAdjustmentDetailScreen.class);
        ITEM_DETAIL_SCREEN = getScreen(manager, "ITEM_DETAIL_SCREEN", ItemDetailScreen.class);
        ITEM_LOOKUP_SCREEN = getScreen(manager, "ITEM_LOOKUP_SCREEN", ItemLookupScreen.class);
        ITEM_REQUEST_DETAIL_SCREEN = getScreen(manager, "ITEM_REQUEST_DETAIL_SCREEN", ItemRequestDetailScreen.class);
        ITEM_TICKET_DETAIL_SCREEN = getScreen(manager, "ITEM_TICKET_DETAIL_SCREEN", ItemTicketDetailScreen.class);
        ITEM_STORE_SEQUENCE_SCREEN = getScreen(manager, "ITEM_STORE_SEQUENCE_SCREEN", ItemStoreSequenceScreen.class);
        LOGOUT_SCREEN = getScreen(manager, "LOGOUT_SCREEN", LogoutScreen.class);
        SHELF_REPLENISHMENT_DETAIL_SCREEN = getScreen(manager, "SHELF_REPLENISHMENT_DETAIL_SCREEN", ShelfReplenishmentDetailScreen.class);
        PRICE_CHANGE_DETAIL_SCREEN = getScreen(manager, "PRICE_CHANGE_DETAIL_SCREEN", ItemPriceDetailScreen.class);
        PRODUCT_GROUP_DETAIL_SCREEN = getScreen(manager, "PRODUCT_GROUP_DETAIL_SCREEN", ProductGroupDetailScreen.class);
        RETURN_DETAIL_SCREEN = getScreen(manager, "RETURN_DETAIL_SCREEN", ReturnDetailScreen.class);
        ROLE_DETAIL_SCREEN = getScreen(manager, "ROLE_DETAIL_SCREEN", RoleDetailScreen.class);
        PRODUCT_GROUP_SCHEDULE_DETAIL_SCREEN = getScreen(manager, "STOCK_COUNT_SCHEDULE_DETAIL_SCREEN", ProductGroupScheduleDetailScreen.class);
        STOCK_COUNT_AUTHORIZE_SCREEN = getScreen(manager, "STOCK_COUNT_AUTHORIZE_SCREEN", StockCountAuthorizeScreen.class);
        STOCK_COUNT_DETAIL_SCREEN = getScreen(manager, "STOCK_COUNT_DETAIL_SCREEN", StockCountDetailScreen.class);
        STORE_ORDER_DETAIL_SCREEN = getScreen(manager, "STORE_ORDER_DETAIL_SCREEN", StoreOrderDetailScreen.class);
        STORE_SEQUENCE_LIST_SCREEN = getScreen(manager, "STORE_SEQUENCE_LIST_SCREEN", StoreSequenceListScreen.class);
        STORE_SEQUENCE_ITEM_SCREEN = getScreen(manager, "STORE_SEQUENCE_ITEM_SCREEN", StoreSequenceItemScreen.class);
        STORE_SEQUENCE_NO_AREA_SCREEN = getScreen(manager, "STORE_SEQUENCE_NO_AREA_SCREEN", StoreSequenceNoAreaScreen.class);
        SUPPLIER_DETAIL_SCREEN = getScreen(manager, "SUPPLIER_DETAIL_SCREEN", SupplierDetailScreen.class);
        SUPPLIER_LOOKUP_SCREEN = getScreen(manager, "SUPPLIER_LOOKUP_SCREEN", SupplierLookupScreen.class);
        TRANSACTION_HISTORY_SCREEN = getScreen(manager, "TRANSACTION_HISTORY_SCREEN", TransactionHistoryListScreen.class);
        USER_DETAIL_SCREEN = getScreen(manager, "USER_DETAIL_SCREEN", UserDetailScreen.class);
        WAREHOUSE_DELIVERY_CARTON_DETAIL_SCREEN = getScreen(manager, "WAREHOUSE_DELIVERY_CARTON_DETAIL_SCREEN", WarehouseDeliveryCartonDetailScreen.class);
        WAREHOUSE_DELIVERY_DETAIL_SCREEN = getScreen(manager, "WAREHOUSE_DELIVERY_DETAIL_SCREEN", WarehouseDeliveryDetailScreen.class);
        FULFILLMENT__ORDER_REVERSE_PICK_DETAIL_SCREEN = getScreen(manager, "FULFILLMENT__ORDER_REVERSE_PICK_DETAIL_SCREEN", FulfillmentOrderReversePickDetailScreen.class);
    }

    /**
     * Helper method to retrieve a class name from the configuration file
     */
    private static String getScreen(ConfigManager manager, String key, Class<? extends SimScreen> screen) {
        return manager.getString(key, screen.getName());
    }
}
