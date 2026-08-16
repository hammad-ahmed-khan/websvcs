package oracle.retail.sim.client.screen.shelfreplenishment;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.DateTimeDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentMessageText;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentProperty;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentVO;

/********************************************************************************************************
 * Shelf Replenishment List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -6022739710890546358L;

    private ShelfReplenishmentModel model = new ShelfReplenishmentModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable shelfReplenishmentTable = new SimTable(new ShelfReplenishmentDefinition());
    private SimTablePane shelfReplenishmentPane = new SimTablePane(shelfReplenishmentTable);

    private ShelfReplenishmentFilterDialog filterDialog = new ShelfReplenishmentFilterDialog();

    private static final String SHELF_REPLENISHMENT_FILTER_SELECTED = "ShelfReplenishment.filterSelected";
    private static final String SHELF_REPLENISHMENT_SELECTED = "ShelfReplenishment.selected";

    public ShelfReplenishmentListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, SHELF_REPLENISHMENT_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        shelfReplenishmentTable.setTableEditable(false);
        shelfReplenishmentTable.registerDoubleClickAction(this, SHELF_REPLENISHMENT_SELECTED);
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(shelfReplenishmentPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return shelfReplenishmentTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Refresh
     ***************************************************************************************************/

    public void handleRefresh() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Cancel Replenishment
     ***************************************************************************************************/

    public void handleCancelReplenishment() {
        if (shelfReplenishmentTable.getSelectedRowCount() == 0) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (!RConfirmUtility.confirm("Delete Confirmation", ShelfReplenishmentMessageText.DELETE_CONFIRM)) {
            return;
        }
        try {
            model.cancelShelfReplenishments(shelfReplenishmentTable.getAllSelectedRowData());
        } catch (Throwable exception) {
            displayException(exception);
        }
        try {
            populateScreen();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Handle Panel Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SHELF_REPLENISHMENT_SELECTED)) {
                doShelfReplenishmentSelected();
            } else if (command.equals(SHELF_REPLENISHMENT_FILTER_SELECTED)) {
                doShelfReplenishmentFilterSelected();
            } else if (command.equals(SimClientStateKey.SHELF_REPLENISHMENT_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doShelfReplenishmentSelected() throws Exception {
        ShelfReplenishmentVO shelfReplenishment = (ShelfReplenishmentVO) shelfReplenishmentTable.getSelectedRowData();
        model.storeShelfReplenishment(shelfReplenishment);
        navigate(SimScreenName.SHELF_REPLENISHMENT_DETAIL_SCREEN);
    }

    private void doShelfReplenishmentFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        shelfReplenishmentTable.setRows(model.findShelfReplenishmentVOs());
    }

    /****************************************************************************************************
     * Shelf Replenishment List Definition
     ***************************************************************************************************/

    private class ShelfReplenishmentDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ShelfReplenishmentVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(8);
            attributes.add(new SimTableAttribute("ID", ShelfReplenishmentProperty.ID));
            attributes.add(new SimTableAttribute("Product Group", ShelfReplenishmentProperty.GROUP_DESC));
            attributes.add(new SimTableAttribute("User", ShelfReplenishmentProperty.USER_ID));
            attributes.add(new SimTableAttribute("Create Date/Time", ShelfReplenishmentProperty.CREATE_DATE, new DateTimeDisplayer()));
            attributes.add(new SimTableAttribute("Type", ShelfReplenishmentProperty.TYPE, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Qty", ShelfReplenishmentProperty.QUANTITY_TO_REPLENISH));
            attributes.add(new SimTableAttribute("UOM", ShelfReplenishmentProperty.UOM_MODE, new UomModeDisplayer()));
            attributes.add(new SimTableAttribute("Status", ShelfReplenishmentProperty.STATUS, new TranslatedObjectDisplayer()));
            return attributes;
        }
    }
}
