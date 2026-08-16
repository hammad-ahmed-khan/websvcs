package oracle.retail.sim.client.screen.item;

import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.common.item.OrderItem;

/********************************************************************************************************
 * Generic Order Item Listener For Item Search Field Editor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public abstract class OrderItemSearchListener implements SearchListener {

    public void search() {
        ItemLookupDialog dialog = new ItemLookupDialog();
        dialog.setSearchListener(this);
        dialog.setItemLookupType(ItemLookupType.ORDER_ITEM);
        dialog.loadDialog();
        dialog.setVisible(true);
    }

    public void assign(Object value) {
        assignOrderItem((OrderItem) value);
    }

    public abstract void assignOrderItem(OrderItem orderItem);
}
