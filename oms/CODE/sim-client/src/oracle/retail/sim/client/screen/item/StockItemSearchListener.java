package oracle.retail.sim.client.screen.item;

import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.common.item.StockItem;

/********************************************************************************************************
 * Generic Stock Item Listener For Item Search Field Editor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public abstract class StockItemSearchListener implements SearchListener {

    public void search() {
        ItemLookupDialog dialog = new ItemLookupDialog();
        dialog.setSearchListener(this);
        dialog.setItemLookupType(ItemLookupType.STOCK_ITEM);
        dialog.loadDialog();
        dialog.setVisible(true);
    }

    public void assign(Object value) {
        assignStockItem((StockItem) value);
    }

    public abstract void assignStockItem(StockItem stockItem);
}
