package oracle.retail.sim.client.screen.item;

import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.common.item.ItemVO;

/********************************************************************************************************
 * Generic Item Listener For Item Search Field Editor
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public abstract class ItemSearchListener implements SearchListener {

    public void search() {
        ItemLookupDialog dialog = new ItemLookupDialog();
        dialog.setSearchListener(this);
        dialog.setItemLookupType(ItemLookupType.ITEM);
        dialog.loadDialog();
        dialog.setVisible(true);
    }

    public void assign(Object value) {
        assignItem((ItemVO) value);
    }

    public abstract void assignItem(ItemVO itemVO);
}
