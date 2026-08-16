package oracle.retail.sim.client.screen.invadjustment;

import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
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
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentProperty;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentQueryFilter;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentStatus;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentVO;
import oracle.retail.sim.common.report.ReportMessageText;


/********************************************************************************************************
 * Inventory Adjustment List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -6254829072515220134L;

    private InventoryAdjustmentListModel model = new InventoryAdjustmentListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private SimTable adjustmentTable = new SimTable(new InventoryAdjustmentListTableDefinition());
    private SimTablePane adjustmentPane = new SimTablePane(adjustmentTable);

    private InventoryAdjustmentFilterDialog filterDialog = new InventoryAdjustmentFilterDialog();

    private static final String ADJUSTMENT_FILTER_SELECTED = "InventoryAdjustment.filterSelected";
    private static final String ADJUSTMENT_SELECTED = "InventoryAdjustment.selected";
    private static final String ADJUSTMENT_LIMIT_MODIFIED = "InventoryAdjustment.searchLimitModified";

    public InventoryAdjustmentListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, ADJUSTMENT_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        adjustmentTable.setTableEditable(false);
        adjustmentTable.registerDoubleClickAction(this, ADJUSTMENT_SELECTED);

        searchLimitEditor.registerAction(this, ADJUSTMENT_LIMIT_MODIFIED, KeyEvent.VK_ENTER);
        searchLimitEditor.setIdentifier(SimName.INVENTORY_ADJUSTMENT_SEARCH_LIMIT);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());

        adjustmentTable.setColumnSize(InventoryAdjustmentProperty.ID, EditorConstants.COLUMN_LABEL_WIDTH);
        adjustmentTable.setColumnSize(InventoryAdjustmentProperty.TOTAL_ITEMS, EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutScreen() {
        RPanel topPanel = new RPanel(new GridBagLayout());
        topPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        topPanel.add(searchLimitEditor, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 3, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(adjustmentPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return adjustmentTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }
    
    public boolean isDeleteFunctionAvailable() {
        return model.isDeleteFunctionAvailable();
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        List<InventoryAdjustmentVO> adjustmentVOs = adjustmentTable.getAllSelectedRowData();
        if (adjustmentVOs.isEmpty()) {
            throw new BusinessException(ReportMessageText.NO_ROWS_SELECTED_PRINT);
        }
        model.printInventoryAdjustments(adjustmentVOs);
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleDelete() throws Exception {
        if (adjustmentTable.getSelectedRowCount() == 0) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        List<InventoryAdjustmentVO> adjustmentVOs = adjustmentTable.getAllSelectedRowData();
        for (InventoryAdjustmentVO adjustmentVO : adjustmentVOs) {
            if (adjustmentVO.getStatus() != InventoryAdjustmentStatus.IN_PROGRESS) {
                displayError(InventoryAdjustmentMessageText.DELETE_ADJUSTMENT_STATUS_INVALID);
                return;
            }
        }
        if (!RConfirmUtility.confirm("Delete Confirmation", InventoryAdjustmentMessageText.DELETE_ADJUSTMENT_CONFIRM)) {
            return;
        }
        try {
            model.cancelInventoryAdjustments(adjustmentVOs);
        } catch (Throwable exception) {
            displayException(exception);
        } finally {
            populateScreen();
        }
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(ADJUSTMENT_SELECTED)) {
                doAdjustmentSelected();
            } else if (command.equals(ADJUSTMENT_FILTER_SELECTED)) {
                doAdjustmentFilterSelected();
            } else if (command.equals(SimClientStateKey.INVENTORY_ADJUSTMENT_FILTER_MODIFIED)) {
                populateScreen();
            } else if (command.equals(ADJUSTMENT_LIMIT_MODIFIED)) {
                doSearchLimitModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doAdjustmentSelected() throws Exception {
        model.storeAdjustment((InventoryAdjustmentVO) adjustmentTable.getSelectedRowData());
        navigate(SimScreenName.INVENTORY_ADJUSTMENT_DETAIL_SCREEN);
    }

    private void doSearchLimitModified() throws Exception {
        InventoryAdjustmentQueryFilter filter = model.getFilter();
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());
        populateScreen();
    }

    private void doAdjustmentFilterSelected() throws Exception {
        InventoryAdjustmentQueryFilter filter = model.getFilter();
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());
        filterDialog.setFilter(filter);
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        searchLimitEditor.setInteger(model.getFilter().getSearchLimit());
        filterEditor.setText(model.getDescriptionMap());
        adjustmentTable.setRows(model.findInventoryAdjustmentVOs());
    }

    /****************************************************************************************************
     * Adjustment Table Definition
     ***************************************************************************************************/

    private static class InventoryAdjustmentListTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return InventoryAdjustmentVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(6);
            attributes.add(new SimTableAttribute("Adjustment ID", InventoryAdjustmentProperty.ID));
            attributes.add(new SimTableAttribute("Template", InventoryAdjustmentProperty.TEMPLATE_DESC));
            attributes.add(new SimTableAttribute("Date", InventoryAdjustmentProperty.DATE));
            attributes.add(new SimTableAttribute("Status", InventoryAdjustmentProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Total SKUs", InventoryAdjustmentProperty.TOTAL_ITEMS));
            attributes.add(new SimTableAttribute("Create User", InventoryAdjustmentProperty.CREATE_USER));
            return attributes;
        }
    }
}
