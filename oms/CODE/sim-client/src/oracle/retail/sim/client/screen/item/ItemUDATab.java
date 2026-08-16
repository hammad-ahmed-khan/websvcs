package oracle.retail.sim.client.screen.item;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemMessageText;

/********************************************************************************************************
 * Item User Defined Attributes Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemUDATab extends SimTab {
    private static final long serialVersionUID = 5181964002070847857L;

    private ItemUDATabModel model = new ItemUDATabModel();

    private SimTable itemUDATable = new SimTable(new UDATableDefinition());
    private SimTablePane itemUDAPane = new SimTablePane(itemUDATable);

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/

    public ItemUDATab() {
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        itemUDATable.setSingleRowSelectionMode();
        itemUDATable.setTableEditable(false);
    }

    private void layoutTab() {
        setLayout(new BorderLayout(0, 10));
        add(itemUDAPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Load Tab
     ***************************************************************************************************/

    protected void loadTab(ItemDetailVO itemVO) throws Exception {
        if (itemVO == null) {
            displayWarning(ItemMessageText.NO_UDA_DETAIL);
            return;
        }
        List<ItemUDAWrapper> wrappers = model.findUserDefinedAttributes(itemVO);
        if (wrappers.isEmpty()) {
            displayWarning(ItemMessageText.NO_UDA_DETAIL);
            return;
        }
        itemUDATable.setRows(wrappers);
    }

    /****************************************************************************************************
     * Item UDA Table Definition
     ***************************************************************************************************/

    private class UDATableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ItemUDAWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("description", true));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(2);
            attributes.add(new SimTableAttribute("UDA", "description"));
            attributes.add(new SimTableAttribute("Value", "value"));
            return attributes;
        }
    }
}
