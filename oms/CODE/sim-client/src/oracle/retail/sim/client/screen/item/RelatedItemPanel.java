package oracle.retail.sim.client.screen.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.RelatedItemTypeDisplayer;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.uom.OrderItemEstimatedQuantityDisplayer;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.item.RelatedItem;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Related Item Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RelatedItemPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 1796538784877970764L;

    private RelatedItemModel model = new RelatedItemModel();

    private SimTable relatedItemTable = new SimTable(new RelatedItemDefinition());
    private SimTablePane relatedItemPane = new SimTablePane(relatedItemTable);

    private static final String ITEM_SELECTED = "Item.selected";

    public RelatedItemPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        relatedItemTable.setSingleRowSelectionMode();
        relatedItemTable.setTableEditable(false);
        relatedItemTable.registerDoubleClickAction(this, ITEM_SELECTED);
    }

    private void layoutScreen() {
        setContentPane(relatedItemPane);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return relatedItemTable;
    }

    public void start() {
        model.loadItem();
        try {
            showScreenBusy(true);
            List<RelatedItemWrapper> wrappers = model.findRelatedItems();
            if (wrappers.size() < 1) {
                displayMessage(ItemMessageText.NO_RELATED_ITEMS);
                return;
            }
            relatedItemTable.setRows(wrappers);
            showScreenBusy(false);
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ITEM_SELECTED)) {
                handleItemDetail();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    // They selected a row, and hit done, so set it in state:
    private void handleItemDetail() throws Exception {
        RelatedItemWrapper relatedItemWrapper = (RelatedItemWrapper) relatedItemTable.getSelectedRowData();
        RelatedItem relatedItem = relatedItemWrapper.getRelatedItem();
        if (relatedItem != null) {
            ItemDetailVO itemDetailVO = ClientServiceFactory.getItemServices().readItemDetailVO(relatedItem.getId(), model.getStoreId());
            RepositoryManager.addStateObject(SimClientStateKey.SELECTED_ITEM, itemDetailVO);
        }
        navigate(SimScreenName.ITEM_DETAIL_SCREEN);
    }

    /****************************************************************************************************
     * Related Item Table Definition
     ***************************************************************************************************/

    private class RelatedItemDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return RelatedItem.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("itemId"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(10);
            attributes.add(new SimTableAttribute("Item", "itemId"));
            attributes.add(new SimTableAttribute("Description", "description"));
            attributes.add(new SimTableAttribute("Diff1", "diff1"));
            attributes.add(new SimTableAttribute("Diff2", "diff2"));
            attributes.add(new SimTableAttribute("Diff3", "diff3"));
            attributes.add(new SimTableAttribute("Diff4", "diff4"));
            attributes.add(new SimTableAttribute("Type", "type", new RelatedItemTypeDisplayer()));
            attributes.add(new SimTableAttribute("Required", "required", new BooleanDisplayer()));
            attributes.add(new SimTableAttribute("UOM", "unitOfMeasure"));
            attributes.add(new SimTableAttribute("SOH", "availableStockOnHand", new OrderItemEstimatedQuantityDisplayer()));
            return attributes;
        }
    }
}
