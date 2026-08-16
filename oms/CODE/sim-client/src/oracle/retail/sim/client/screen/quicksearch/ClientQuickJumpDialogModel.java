package oracle.retail.sim.client.screen.quicksearch;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.QuickJumpMode;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.security.PermissionKey;

/********************************************************************************************************
 * CLIENT QUICK JUMP DIALOG MODEL
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ClientQuickJumpDialogModel extends SimScreenModel {

    public List<QuickJumpMode> getAvailableSearches() {
        List<QuickJumpMode> searchModes = new ArrayList<>();
        if (hasPermission(PermissionKey.PC_ACCESS_LOOKUP)) {
            if (hasPermission(PermissionKey.PC_ACCESS_ITEM_LOOKUP)) {
                searchModes.add(QuickJumpMode.ITEM_LOOKUP);
            }
            if (hasPermission(PermissionKey.PC_ACCESS_TRANSACTION_HISTORY)) {
                searchModes.add(QuickJumpMode.TRANSACTION_HISTORY);
            }
        }
        if (hasPermission(PermissionKey.PC_ACCESS_INVENTORY_MANAGEMENT)) {
            if (hasPermission(PermissionKey.PC_ACCESS_STOCK_COUNT)) {
                searchModes.add(QuickJumpMode.STOCK_COUNT);
            }
            if (hasPermission(PermissionKey.PC_ACCESS_INVENTORY_ADJUSTMENT)) {
                searchModes.add(QuickJumpMode.INVENTORY_ADJUSTMENT);
            }
        }
        if (hasPermission(PermissionKey.PC_ACCESS_SHIPPING_RECEIVING)) {
            if (hasPermission(PermissionKey.PC_ACCESS_WAREHOUSE_DELIVERY)) {
                searchModes.add(QuickJumpMode.WAREHOUSE_DELIVERY);
            }
            if (hasPermission(PermissionKey.PC_ACCESS_DIRECT_DELIVERY)) {
                searchModes.add(QuickJumpMode.DIRECT_DELIVERY);
            }
            if (hasPermission(PermissionKey.PC_ACCESS_RETURN)) {
                searchModes.add(QuickJumpMode.RETURN);
            }
            if (hasPermission(PermissionKey.PC_ACCESS_TRANSFER)) {
                searchModes.add(QuickJumpMode.TRANSFER);
            }
        }
        if (hasPermission(PermissionKey.PC_ACCESS_ADMIN)) {
            if (hasPermission(PermissionKey.PC_ACCESS_PRODUCT_GROUP)) {
                searchModes.add(QuickJumpMode.PRODUCT_GROUP);
            }
        }
        if (hasPermission(PermissionKey.PC_ACCESS_CUSTOMER_ORDER_MGMT)) {
            if (hasPermission(PermissionKey.PC_ACCESS_CUSTOMER_ORDER)) {
                searchModes.add(QuickJumpMode.FULFILLMENT_ORDER);
            }
            if (hasPermission(PermissionKey.PC_ACCESS_CUSTOMER_ORDER_PICK)) {
                searchModes.add(QuickJumpMode.FULFILLMENT_ORDER_PICK);
            }
        }
        return searchModes;
    }
}
