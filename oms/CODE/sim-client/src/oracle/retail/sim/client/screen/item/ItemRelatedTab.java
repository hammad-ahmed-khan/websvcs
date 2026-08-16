package oracle.retail.sim.client.screen.item;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.RelatedItem;

/********************************************************************************************************
 * Related Item Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemRelatedTab extends SimTab {
    private static final long serialVersionUID = 3915644651111819844L;

    private ItemRelatedTabModel model = new ItemRelatedTabModel();

    private SimTable relatedItemTable = new SimTable(new RelatedItemTableDefinition());
    private SimTablePane relatedItemPane = new SimTablePane(relatedItemTable);

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/

    public ItemRelatedTab() {
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        relatedItemTable.setSingleRowSelectionMode();
        relatedItemTable.setTableEditable(false);
    }

    private void layoutTab() {
        setLayout(new BorderLayout(0, 10));
        add(relatedItemPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Load Tab
     ***************************************************************************************************/

    protected void loadTab(ItemDetailVO itemVO) throws Exception {
        model.setItemDetail(itemVO);

        if (StringUtility.isNullOrEmpty(itemVO.getParentItemId())) {
            displayWarning(ItemMessageText.NO_RELATED_ITEMS);
            return;
        }

        relatedItemTable.setRows(model.findRelatedItems());
    }

    /****************************************************************************************************
     * Related Item Table Definition
     ***************************************************************************************************/

    private class RelatedItemTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return RelatedItem.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("itemId"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(8);
            attributes.add(new SimTableAttribute("Item", "itemId"));
            attributes.add(new SimTableAttribute("Description", "description"));
            attributes.add(new SimTableAttribute("Diff1", "diff1"));
            attributes.add(new SimTableAttribute("Diff2", "diff2"));
            attributes.add(new SimTableAttribute("Diff3", "diff3"));
            attributes.add(new SimTableAttribute("Diff4", "diff4"));
            attributes.add(new SimTableAttribute("UOM", "unitOfMeasure"));
            attributes.add(new SimTableAttribute("SOH", "availableStockOnHand"));
            return attributes;
        }
    }
}
