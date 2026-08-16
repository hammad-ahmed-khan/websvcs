package oracle.retail.sim.client.screen.invadjustment;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.InventoryDispositionDisplayer;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.screen.item.StockItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.tableeditor.InventoryAdjustmentReasonTableEditor;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.StockItemTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReason;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplate;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentTemplateProperty;
import oracle.retail.sim.common.item.StockItem;

/********************************************************************************************************
 * Inventory Template Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryTemplateDetailPanel extends ScreenPanel {
    private static final long serialVersionUID = -1908635260448775092L;

    private InventoryTemplateDetailModel model = new InventoryTemplateDetailModel();

    private RDisplayLabelEditor templateEditor = new RDisplayLabelEditor("Template ID");
    private RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
    private RTextFieldEditor descriptionEditor = new RTextFieldEditor("Description");
    private RDisplayLabelEditor createDateEditor = new RDisplayLabelEditor("Create Date");
    private RDisplayLabelEditor approveDateEditor = new RDisplayLabelEditor("Approval Date");
    private RDisplayLabelEditor createUserEditor = new RDisplayLabelEditor("Create User");
    private RDisplayLabelEditor approveUserEditor = new RDisplayLabelEditor("Approval User");
    private RLongTextFieldEditor commentsEditor = new RLongTextFieldEditor("Comments");
    private RComboBoxEditor defaultReasonEditor = new RComboBoxEditor("Reason");

    private StockItemTableEditor stockItemTableEditor = new StockItemTableEditor();
    private InventoryAdjustmentReasonTableEditor reasonTableEditor = new InventoryAdjustmentReasonTableEditor();

    private SimTable lineItemTable = new SimTable(new TemplateCreateTableDefinition());
    private SimTablePane lineItemPane = new SimTablePane(lineItemTable);

    /****************************************************************************************************
     * Initialization & Layout
     ***************************************************************************************************/

    public InventoryTemplateDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        statusEditor.setDisplayer(new TranslatedObjectDisplayer());

        descriptionEditor.setIdentifier(SimName.INVENTORY_ADJUSTMENT_TEMPLATE_DESCRIPTION);

        createDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
        approveDateEditor.setDataType(DataTypeConstants.DATE_SHORT);

        commentsEditor.setIdentifier(SimName.INVENTORY_ADJUSTMENT_TEMPLATE_COMMENT);

        defaultReasonEditor.setDisplayer(new TranslatedObjectDisplayer());
        defaultReasonEditor.setSizeType(EditorConstants.LARGE);

        stockItemTableEditor.setSearchListener(buildItemSearchListener());

        lineItemTable.setColumnSize(InventoryAdjustmentTemplateProperty.CASE_SIZE, EditorConstants.COLUMN_LABEL_WIDTH);
        lineItemTable.setColumnSize(InventoryAdjustmentTemplateProperty.QUANTITY_BASED_ON_UOM, EditorConstants.COLUMN_LABEL_WIDTH);
    }

    private void layoutScreen() {
        REditorPanel datePanel = new REditorPanel(2, 1);
        datePanel.setTitleBorder("Date");
        datePanel.add(createDateEditor);
        datePanel.add(approveDateEditor);

        REditorPanel userPanel = new REditorPanel(2, 1);
        userPanel.setTitleBorder("User");
        userPanel.add(createUserEditor);
        userPanel.add(approveUserEditor);

        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(templateEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 2, 3, 5, 0, 0, 0));
        headerPanel.add(statusEditor, GridTool.constraints(0, 1, 1, 1, 0, 0, 2, 3, 3, 0, 0, 0));
        headerPanel.add(descriptionEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 5, 10, 0, 0));
        headerPanel.add(commentsEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 3, 10, 0, 0));
        headerPanel.add(defaultReasonEditor, GridTool.constraints(1, 2, 1, 1, 1, 0, 0, 3, 3, 10, 0, 0));
        headerPanel.add(datePanel, GridTool.constraints(2, 0, 1, 3, 0, 0, 0, 3, 0, 0, 0, 0));
        headerPanel.add(userPanel, GridTool.constraints(3, 0, 1, 3, 0, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(lineItemPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);

        LayoutUtility.alignEditorsInGridBag(headerPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return lineItemTable;
    }

    /****************************************************************************************************
     * Navigation
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadTemplate();
        loadAdjustmentReasons();
        populateScreen();
        validateScreenState();
    }

    private void loadAdjustmentReasons() throws Exception {
        List<InventoryAdjustmentReason> reasons = model.getInventoryAdjustmentReasons();
        defaultReasonEditor.setItems(reasons);
        reasonTableEditor.setItems(reasons);
    }

    private void populateScreen() {
        InventoryAdjustmentTemplate template = model.getTemplate();
        if (template.isNew()) {
            templateEditor.setData(Translator.getText("New"));
        } else {
            templateEditor.setData(template.getId());
        }
        statusEditor.setData(template.getStatus());
        descriptionEditor.setText(template.getDescription());
        createDateEditor.setData(template.getCreateDate());
        approveDateEditor.setData(template.getApproveDate());
        createUserEditor.setData(template.getCreateUser());
        approveUserEditor.setData(template.getApproveUser());
        commentsEditor.setText(template.getComments());

        lineItemTable.setRows(model.getLineItemWrappers());
    }

    public boolean isTemplateEditable() {
        return model.isTemplateEditable();
    }

    private void validateScreenState() {
        boolean isEditable = model.isTemplateEditable();
        descriptionEditor.setEnabled(isEditable);
        commentsEditor.setEnabled(isEditable);
        defaultReasonEditor.setEnabled(isEditable);
        lineItemTable.setTableEditable(isEditable);
    }

    public boolean confirmTemplateLock() throws Exception {
        return model.confirmTemplateLock();
    }

    /****************************************************************************************************
     * Handle Add Item
     ***************************************************************************************************/

    public void handleAddItem() {
        lineItemTable.stopEditing();
        if (hasEmptyLineItems()) {
            return;
        }
        InventoryAdjustmentReason reason = (InventoryAdjustmentReason) defaultReasonEditor.getSelectedItem();
        InventoryTemplateLineItemWrapper wrapper = model.createNewLineItemWrapper(reason);
        lineItemTable.addRow(wrapper);
        lineItemTable.editCellInLastRow(InventoryAdjustmentTemplateProperty.STOCK_ITEM);
    }

    private boolean hasEmptyLineItems() {
        List<InventoryTemplateLineItemWrapper> wrappers = lineItemTable.getAllRowData();
        for (InventoryTemplateLineItemWrapper wrapper : wrappers) {
            if (wrapper.getStockItem() == null || wrapper.getReason() == null) {
                return true;
            }
            if (wrapper.getQuantityBasedOnUOM() == null || wrapper.getQuantityBasedOnUOM().isZero()) {
                return true;
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Delete Item
     ***************************************************************************************************/

    public void handleRemoveItem() throws BusinessException {
        lineItemTable.stopEditing();

        if (lineItemTable.getSelectedRowCount() < 1) {
            displayMessage(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (!RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM)) {
            return;
        }
        List<InventoryTemplateLineItemWrapper> lineItemWrappers = lineItemTable.getAllSelectedRowData();
        List<InventoryTemplateLineItemWrapper> removedLineItemWrappers = new ArrayList<>();
        for (InventoryTemplateLineItemWrapper wrapper : lineItemWrappers) {
            model.deleteLineItem(wrapper);
            removedLineItemWrappers.add(wrapper);
        }
        for (InventoryTemplateLineItemWrapper wrapper : removedLineItemWrappers) {
            lineItemTable.removeRow(wrapper);
        }
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void handleCancel() throws Exception {
        model.releaseTemplateLock();
    }

    /****************************************************************************************************
     * Handle Confirm & Done
     ***************************************************************************************************/

    public boolean handleConfirm() {
        try {
            InventoryAdjustmentTemplate template = model.getTemplate();
            template.setDescription(descriptionEditor.getTextOrNull());
            template.setComments(commentsEditor.getTextOrNull());
            model.confirmTemplate();
            return true;
        } catch (Exception exception) {
            displayException(exception);
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() {
        lineItemTable.stopEditing();
        try {
            InventoryAdjustmentTemplate template = model.getTemplate();
            template.setDescription(descriptionEditor.getTextOrNull());
            template.setComments(commentsEditor.getTextOrNull());
            model.saveTemplate();
            return true;
        } catch (Exception exception) {
            displayException(exception);
        }
        return false;
    }

    /****************************************************************************************************
     * Search Listeners
     ***************************************************************************************************/

    private StockItemSearchListener buildItemSearchListener() {
        return new StockItemSearchListener() {
            public void search() {
                super.search();
            }

            public void assignStockItem(StockItem stockItem) {
                stockItemTableEditor.setData(stockItem);
            }
        };
    }

    /****************************************************************************************************
     * Inventory Create Table Definition
     ***************************************************************************************************/

    private class TemplateCreateTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return InventoryTemplateLineItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(7);
            attributes.add(new SimTableAttribute("Item", InventoryAdjustmentTemplateProperty.STOCK_ITEM, new AttributeDisplayer("id"), stockItemTableEditor));
            attributes.add(new SimTableAttribute("Item Description", InventoryAdjustmentTemplateProperty.ITEM_DESCRIPTION));
            attributes.add(new SimTableAttribute("Reason", InventoryAdjustmentTemplateProperty.REASON, new TranslatedObjectDisplayer(), reasonTableEditor));
            attributes.add(new SimTableAttribute("Disposition", InventoryAdjustmentTemplateProperty.DISPOSITION, new InventoryDispositionDisplayer()));
            attributes.add(new SimTableAttribute("UOM", InventoryAdjustmentTemplateProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Pack Size", InventoryAdjustmentTemplateProperty.CASE_SIZE, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Quantity", InventoryAdjustmentTemplateProperty.QUANTITY_BASED_ON_UOM, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            return attributes;
        }
    }
}
