package oracle.retail.sim.client.screen.invadjustment;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateProperty;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateStatus;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateVO;


/********************************************************************************************************
 * Inventory Template List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryTemplateListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 7398232540390037823L;

    private InventoryTemplateListModel model = new InventoryTemplateListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable templateTable = new SimTable(new InventoryTemplateListTableDefinition());
    private SimTablePane templatePane = new SimTablePane(templateTable);

    private InventoryTemplateFilterDialog filterDialog = new InventoryTemplateFilterDialog();

    private static final String TEMPLATE_FILTER_SELECTED = "InventoryTemplate.filterSelected";
    private static final String TEMPLATE_SELECTED = "InventoryTemplate.selected";

    public InventoryTemplateListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, TEMPLATE_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        templateTable.setTableEditable(false);
        templateTable.setColumnSize(InventoryAdjustmentTemplateProperty.ID, EditorConstants.COLUMN_LABEL_WIDTH);
        templateTable.setColumnSize(InventoryAdjustmentTemplateProperty.TOTAL_ITEMS, EditorConstants.COLUMN_LABEL_WIDTH);
        templateTable.registerDoubleClickAction(this, TEMPLATE_SELECTED);
    }

    private void layoutScreen() {
        REditorPanel topPanel = new REditorPanel(1, 2);
        topPanel.add(filterEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(topPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(templatePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return templateTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void handleCancelTemplate() throws Exception {
        if (templateTable.getSelectedRowCount() == 0) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        List<InventoryAdjustmentTemplateVO> templateVOs = templateTable.getAllSelectedRowData();
        for (InventoryAdjustmentTemplateVO templateVO : templateVOs) {
            if (templateVO.getStatus() == InventoryAdjustmentTemplateStatus.CANCELED) {
                displayError(InventoryAdjustmentMessageText.DELETE_TEMPLATE_STATUS_INVALID);
                return;
            }
        }
        if (!RConfirmUtility.confirm("Delete Confirmation", InventoryAdjustmentMessageText.DELETE_TEMPLATE_CONFIRM)) {
            return;
        }
        try {
            model.cancelTemplates(templateVOs);
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
            if (command.equals(TEMPLATE_SELECTED)) {
                doTemplateSelected();
            } else if (command.equals(TEMPLATE_FILTER_SELECTED)) {
                doTemplateFilterSelected();
            } else if (command.equals(SimClientStateKey.INVENTORY_ADJUSTMENT_TEMPLATE_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doTemplateSelected() throws Exception {
        model.storeTemplate((InventoryAdjustmentTemplateVO) templateTable.getSelectedRowData());
        navigate(SimScreenName.INV_ADJ_TEMPLATE_DETAIL_SCREEN);
    }

    private void doTemplateFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        templateTable.setRows(model.findTemplates());
    }

    /****************************************************************************************************
     * Adjustment Table Definition
     ***************************************************************************************************/

    private static class InventoryTemplateListTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return InventoryAdjustmentTemplateVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(6);
            attributes.add(new SimTableAttribute("Template", InventoryAdjustmentTemplateProperty.ID));
            attributes.add(new SimTableAttribute("Description", InventoryAdjustmentTemplateProperty.DESCRIPTION));
            attributes.add(new SimTableAttribute("Date", InventoryAdjustmentTemplateProperty.DATE));
            attributes.add(new SimTableAttribute("Status", InventoryAdjustmentTemplateProperty.STATUS, new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Total SKUs", InventoryAdjustmentTemplateProperty.TOTAL_ITEMS));
            attributes.add(new SimTableAttribute("Create User", InventoryAdjustmentTemplateProperty.CREATE_USER));
            return attributes;
        }
    }
}
