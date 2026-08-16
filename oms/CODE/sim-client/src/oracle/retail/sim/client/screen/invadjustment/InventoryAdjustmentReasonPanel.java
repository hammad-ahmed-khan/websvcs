package oracle.retail.sim.client.screen.invadjustment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.InventoryDispositionDisplayer;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.NumericIdDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.table.DisplayerTableCellEditor;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.BooleanCheckBoxTableEditor;
import oracle.retail.sim.client.swing.tableeditor.PositiveIntegerTableEditor;
import oracle.retail.sim.client.swing.tableeditor.StringTableEditor;
import oracle.retail.sim.client.tableeditor.InventoryDispositionTableEditor;
import oracle.retail.sim.client.tableeditor.NonSellableQtyTypeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentMessageText;
import oracle.retail.sim.common.invadjustment.InventoryAdjustmentReasonProperty;

/********************************************************************************************************
 * Inventory Adjustment Reason Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryAdjustmentReasonPanel extends ScreenPanel {
    private static final long serialVersionUID = -9201758641069924237L;

    private InventoryAdjustmentReasonModel model = new InventoryAdjustmentReasonModel();

    private StringTableEditor descriptionEditor = new StringTableEditor(SimName.INVENTORY_ADJUSTMENT_REASON_DESCRIPTION);
    private InventoryDispositionTableEditor dispositionEditor = new InventoryDispositionTableEditor();
    private NonSellableQtyTypeTableEditor toSubBucketTableEditor = new NonSellableQtyTypeTableEditor();
    private NonSellableQtyTypeTableEditor fromSubBucketTableEditor = new NonSellableQtyTypeTableEditor();

    private SimTable reasonTable = new SimTable(new InvAdjustmentReasonTableDefinition());
    private SimTablePane reasonPane = new SimTablePane(reasonTable);

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public InventoryAdjustmentReasonPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        reasonTable.setTableEditable(true);
        reasonTable.setMultipleRowSelectionMode();
        reasonTable.setColumnSize(InventoryAdjustmentReasonProperty.CODE, EditorConstants.COLUMN_LABEL_WIDTH);

        reasonTable.setColumnSize(InventoryAdjustmentReasonProperty.DISPLAYABLE, EditorConstants.COLUMN_LABEL_WIDTH);
        reasonTable.setColumnRenderer(InventoryAdjustmentReasonProperty.DISPLAYABLE, new SimTableCheckBoxRenderer(true));
        reasonTable.setColumnEditor(InventoryAdjustmentReasonProperty.DISPLAYABLE, new DisplayerTableCellEditor(new BooleanCheckBoxTableEditor()));

        reasonTable.setColumnSize(InventoryAdjustmentReasonProperty.PUBLISH, EditorConstants.COLUMN_LABEL_WIDTH);
        reasonTable.setColumnRenderer(InventoryAdjustmentReasonProperty.PUBLISH, new SimTableCheckBoxRenderer(true));
        reasonTable.setColumnEditor(InventoryAdjustmentReasonProperty.PUBLISH, new DisplayerTableCellEditor(new BooleanCheckBoxTableEditor()));

        reasonTable.setColumnSize(InventoryAdjustmentReasonProperty.SYSTEM_REQUIRED, EditorConstants.COLUMN_LABEL_WIDTH);
        reasonTable.setColumnRenderer(InventoryAdjustmentReasonProperty.SYSTEM_REQUIRED, new SimTableCheckBoxRenderer(true));
        reasonTable.setColumnEditor(InventoryAdjustmentReasonProperty.SYSTEM_REQUIRED, new DisplayerTableCellEditor(new BooleanCheckBoxTableEditor()));
    }

    private void layoutPanel() {
        setContentPane(reasonPane);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return reasonTable;
    }

    /****************************************************************************************************
     * Start Panel
     ***************************************************************************************************/

    public void start() throws Exception {
        if (model.obtainReasonAdminLock()) {
            dispositionEditor.setDispositions(model.getInventoryDispositions());
            toSubBucketTableEditor.setItems(model.getNonSellableQtyTypes());
            fromSubBucketTableEditor.setItems(model.getNonSellableQtyTypes());
            reasonTable.setRows(model.getInventoryAdjustmentReasonWrappers());
        }
    }

    protected boolean confirmReasonAdminLock() throws Exception {
        return model.confirmReasonAdminLock();
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        reasonTable.stopEditing();

        List<InventoryAdjustmentReasonWrapper> wrappers = reasonTable.getAllRowData();
        validateReasons(wrappers);

        model.updateInventoryAdjustmentReasons(wrappers);
        model.releaseReasonAdminLock();
        return true;
    }

    /****************************************************************************************************
     * Handle Add
     ***************************************************************************************************/

    protected void handleAdd() throws Exception {
        List<InventoryAdjustmentReasonWrapper> wrappers = reasonTable.getAllRowData();
        validateReasons(wrappers);
        reasonTable.addRow(model.createInventoryAdjustmentReasonWrapper());
        reasonTable.editCellInLastRow(InventoryAdjustmentReasonProperty.CODE);
    }

    /****************************************************************************************************
     * Helper Method To Validate Reasons
     ***************************************************************************************************/

    private void validateReasons(List<InventoryAdjustmentReasonWrapper> wrappers) throws BusinessException {
        Set<Integer> uniqueReasonCodes = new HashSet<Integer>();
        for (int i = 0; i < wrappers.size(); i++) {
            InventoryAdjustmentReasonWrapper wrapper = wrappers.get(i);
            if (wrapper.getCode() == null) {
                reasonTable.editCellInRow(InventoryAdjustmentReasonProperty.CODE, i);
                throw new BusinessException(CommonMessageText.MISSING_REASON_CODE);
            }
            if (StringUtility.isNullOrEmpty(wrapper.getDescription())) {
                reasonTable.editCellInRow(InventoryAdjustmentReasonProperty.DESCRIPTION, i);
                throw new BusinessException(CommonMessageText.MISSING_DESCRIPTION);
            }
            if (wrapper.getDisposition() == null) {
                reasonTable.editCellInRow(InventoryAdjustmentReasonProperty.DISPOSITION, i);
                throw new BusinessException(InventoryAdjustmentMessageText.MISSING_DISPOSITION);
            }
            if (uniqueReasonCodes.contains(wrapper.getCode())) {
                reasonTable.editCellInRow(InventoryAdjustmentReasonProperty.CODE, i);
                throw new BusinessException(InventoryAdjustmentMessageText.DUPLICATE_REASON_CODE);
            }
            uniqueReasonCodes.add(wrapper.getCode());
        }
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleDelete() throws Exception {
        if (reasonTable.getSelectedRowCount() == 0) {
            displayError(InventoryAdjustmentMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (!RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM)) {
            return;
        }
        reasonTable.stopEditing();

        List<InventoryAdjustmentReasonWrapper> selectedWrappers = reasonTable.getAllSelectedRowData();

        // Validate System Required Selections
        int row = findSystemRequiredSelectedLine(selectedWrappers);
        if (row > -1) {
            reasonTable.setRowSelectionInterval(row, row);
            displayError(InventoryAdjustmentMessageText.SYSTEM_REQUIRED_CODE);
            return;
        }

        // Validate Actively Used Reason Codes
        row = findActivelyUsedSelectedLine(selectedWrappers);
        if (row > -1) {
            reasonTable.setRowSelectionInterval(row, row);
            displayError(InventoryAdjustmentMessageText.CODE_IN_USE_ERROR);
            return;
        }

        // Remove The Reason Codes
        for (InventoryAdjustmentReasonWrapper selectedWrapper : selectedWrappers) {
            model.deleteReason(selectedWrapper);
            reasonTable.removeRow(selectedWrapper);
        }
    }

    private int findSystemRequiredSelectedLine(List<InventoryAdjustmentReasonWrapper> selectedWrappers) {
        Set<Integer> invalidReasonsCodes = new HashSet<>();
        for (InventoryAdjustmentReasonWrapper selectedWrapper : selectedWrappers) {
            if (selectedWrapper.isSystemRequired()) {
                invalidReasonsCodes.add(selectedWrapper.getCode());
            }
        }
        if (invalidReasonsCodes.isEmpty()) {
            return -1;
        }
        List<InventoryAdjustmentReasonWrapper> wrappers = reasonTable.getAllRowData();
        for (int i = 0; i < wrappers.size(); i++) {
            if (invalidReasonsCodes.contains(wrappers.get(i).getCode())) {
                return i;
            }
        }
        return -1;
    }

    private int findActivelyUsedSelectedLine(List<InventoryAdjustmentReasonWrapper> selectedWrappers) throws Exception {
        Set<Integer> invalidReasonsCodes = new HashSet<>();
        Set<Integer> activelyUsedReasonCodes = model.loadActivelyUsedReasonCodes();
        for (InventoryAdjustmentReasonWrapper selectedWrapper : selectedWrappers) {
            if (activelyUsedReasonCodes.contains(selectedWrapper.getCode())) {
                invalidReasonsCodes.add(selectedWrapper.getCode());
            }
        }
        if (invalidReasonsCodes.isEmpty()) {
            return -1;
        }
        List<InventoryAdjustmentReasonWrapper> reasons = reasonTable.getAllRowData();
        for (int i = 0; i < reasons.size(); i++) {
            if (invalidReasonsCodes.contains(reasons.get(i).getCode())) {
                return i;
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public void handleCancel() {
        try {
            model.releaseReasonAdminLock();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Inventory Adjustment Reason Definition
     ***************************************************************************************************/
    private class InvAdjustmentReasonTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return InventoryAdjustmentReasonWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(InventoryAdjustmentReasonProperty.CODE));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("Code", InventoryAdjustmentReasonProperty.CODE, new NumericIdDisplayer(), new PositiveIntegerTableEditor()));
            attributes.add(new SimTableAttribute("Description", InventoryAdjustmentReasonProperty.DESCRIPTION, new TranslatedObjectDisplayer(), descriptionEditor));
            attributes.add(new SimTableAttribute("Use in UI", InventoryAdjustmentReasonProperty.DISPLAYABLE));
            attributes.add(new SimTableAttribute("Disposition", InventoryAdjustmentReasonProperty.DISPOSITION, new InventoryDispositionDisplayer(), dispositionEditor));
            if (model.isNonSellableTypesActive()) {
                attributes.add(new SimTableAttribute("To Sub-bucket", InventoryAdjustmentReasonProperty.TO_NONSELLABLE_QTY_TYPE, new TranslatedObjectDisplayer(), toSubBucketTableEditor));
                attributes.add(new SimTableAttribute("From Sub-bucket", InventoryAdjustmentReasonProperty.FROM_NONSELLABLE_QTY_TYPE, new TranslatedObjectDisplayer(), fromSubBucketTableEditor));
            }
            attributes.add(new SimTableAttribute("Publish", InventoryAdjustmentReasonProperty.PUBLISH));
            attributes.add(new SimTableAttribute("System", InventoryAdjustmentReasonProperty.SYSTEM_REQUIRED));
            return attributes;
        }
    }
}
