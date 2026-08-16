package oracle.retail.sim.client.screen.returns;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.BooleanAvailableDisplayer;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.DisplayerTableCellEditor;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.tableeditor.BooleanCheckBoxTableEditor;
import oracle.retail.sim.client.swing.tableeditor.StringTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.tableeditor.BooleanAvailableTableEditor;
import oracle.retail.sim.client.tableeditor.NonSellableQtyTypeTableEditor;
import oracle.retail.sim.client.tableeditor.SourceTypeTableEditor;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.source.SourceType;
import oracle.retail.sim.common.stockreturn.ReturnMessageText;
import oracle.retail.sim.common.stockreturn.ReturnReason;
import oracle.retail.sim.common.stockreturn.ReturnReasonProperty;

/********************************************************************************************************
 * Return Reason Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReturnReasonPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -3402934927169693135L;

    private RComboBoxEditor typeFilterEditor = new RComboBoxEditor("Type");

    private ReturnReasonModel model = new ReturnReasonModel();

    private StringTableEditor reasonCodeEditor = new StringTableEditor(SimName.RETURN_REASON_CODE);
    private StringTableEditor descriptionEditor = new StringTableEditor(SimName.RETURN_REASON_DESCRIPTION);
    private SourceTypeTableEditor sourceTypeEditor = new SourceTypeTableEditor();
    private NonSellableQtyTypeTableEditor nonSellableTypeTableEditor = new NonSellableQtyTypeTableEditor();

    private SimTable reasonTable = new SimTable(new ReturnReasonTableDefinition());
    private SimTablePane reasonPane = new SimTablePane(reasonTable);

    private static final String FILTER_MODIFIED = "Filter.modified";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public ReturnReasonPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {

        typeFilterEditor.setDisplayer(new TranslatedObjectDisplayer());
        typeFilterEditor.setItems(SourceType.values());
        typeFilterEditor.setSizeType(EditorConstants.MEDIUM);
        typeFilterEditor.setEmptyType(RComboBoxEmptyType.ALL);
        typeFilterEditor.registerAction(this, FILTER_MODIFIED);

        reasonTable.setTableEditable(true);
        reasonTable.setMultipleRowSelectionMode();

        reasonTable.setColumnSize(ReturnReasonProperty.CODE, EditorConstants.COLUMN_LABEL_WIDTH);
        reasonTable.setColumnSize(ReturnReasonProperty.USE_AVAILABLE, EditorConstants.COLUMN_LABEL_WIDTH);

        reasonTable.setColumnSize(ReturnReasonProperty.SYSTEM_REQUIRED, EditorConstants.COLUMN_LABEL_WIDTH);
        reasonTable.setColumnRenderer(ReturnReasonProperty.SYSTEM_REQUIRED, new SimTableCheckBoxRenderer(true));
        reasonTable.setColumnEditor(ReturnReasonProperty.SYSTEM_REQUIRED, new DisplayerTableCellEditor(new BooleanCheckBoxTableEditor()));
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(typeFilterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(reasonPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
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
            model.loadReturnReasons();
            loadNonSellableTypes();
            populateScreen();
        }
    }

    private void loadNonSellableTypes() throws Exception {
        nonSellableTypeTableEditor.setItems(model.getNonSellableQtyTypes());
    }

    public boolean confirmReasonAdminLock() throws Exception {
        return model.confirmReasonAdminLock();
    }

    /****************************************************************************************************
     * Handle Add
     ***************************************************************************************************/

    public void handleAdd() throws Exception {
        List<ReturnReasonWrapper> wrappers = reasonTable.getAllRowData();
        for (int i = 0; i < wrappers.size(); i++) {
            ReturnReason reason = wrappers.get(i).getReason();
            if (StringUtility.isNullOrEmpty(reason.getCode())) {
                reasonTable.editCellInRow(ReturnReasonProperty.CODE, i);
                return;
            }
        }
        reasonTable.addRow(model.buildReturnReasonWrapper());
        reasonTable.editCellInLastRow(ReturnReasonProperty.TYPE);
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleDelete() throws Exception {
        if (reasonTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (!RConfirmUtility.confirm("Item Delete Confirmation", CommonMessageText.LINE_ITEM_DELETE_CONFIRM)) {
            return;
        }
        reasonTable.stopEditing();

        List<ReturnReasonWrapper> selectedWrappers = reasonTable.getAllSelectedRowData();

        // Validate System Required Selections
        int row = findSystemRequiredSelectedLine(selectedWrappers);
        if (row > -1) {
            reasonTable.setRowSelectionInterval(row, row);
            displayError(ReturnMessageText.SYSTEM_REQUIRED_CODE);
            return;
        }

        // Validate Actively Used Reason Codes
        row = findActivelyUsedSelectedLine(selectedWrappers);
        if (row > -1) {
            reasonTable.setRowSelectionInterval(row, row);
            displayError(ReturnMessageText.REASON_IN_USE_ERROR);
            return;
        }

        // Remove The Reason Codes
        for (ReturnReasonWrapper wrapper : selectedWrappers) {
            model.deleteReason(wrapper.getReason());
            reasonTable.removeRow(wrapper);
        }
    }

    private int findSystemRequiredSelectedLine(List<ReturnReasonWrapper> wrappers) {
        for (ReturnReasonWrapper wrapper : wrappers) {
            ReturnReason reason = wrapper.getReason();
            if (reason.isSystemRequired()) {
                return getRowForReturnReason(reason);
            }
        }
        return -1;
    }

    private int findActivelyUsedSelectedLine(List<ReturnReasonWrapper> wrappers) throws Exception {
        Set<ReturnReason> activelyUsedReasons = model.loadActivelyUsedReasons();
        for (ReturnReasonWrapper wrapper : wrappers) {
            ReturnReason reason = wrapper.getReason();
            if (activelyUsedReasons.contains(reason)) {
                return getRowForReturnReason(reason);
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        reasonTable.stopEditing();

        List<ReturnReasonWrapper> wrappers = model.getFilteredReasons(null);
        if (wrappers.isEmpty()) {
            model.releaseReasonAdminLock();
            return true;
        }
        // Unique Type & Code
        Set<ReturnReason> uniqueReasons = new HashSet<>();

        // Validate
        List<ReturnReason> modifiedReasons = new ArrayList<>();
        for (ReturnReasonWrapper wrapper : wrappers) {
            ReturnReason reason = wrapper.getReason();

            if (StringUtility.isNullOrEmpty(reason.getCode())) {
                int row = getRowForReturnReason(reason);
                if (row > -1) {
                    reasonTable.editCellInRow(ReturnReasonProperty.CODE, row);
                }
                throw new BusinessException(CommonMessageText.MISSING_REASON_CODE);
            }
            if (StringUtility.isNullOrEmpty(reason.getDescription())) {
                int row = getRowForReturnReason(reason);
                if (row > -1) {
                    reasonTable.editCellInRow(ReturnReasonProperty.DESCRIPTION, row);
                }
                throw new BusinessException(CommonMessageText.MISSING_DESCRIPTION);
            }
            if (reason.getNonSellableQtyTypeId() == null && reason.isUseUnavailable() && model.isNonSellableTypesActive()) {
                int row = getRowForReturnReason(reason);
                if (row > -1) {
                    reasonTable.editCellInRow(ReturnReasonProperty.NONSELLABLE_QTY_TYPE, row);
                }
                throw new BusinessException(ReturnMessageText.MISSING_REASON_SUBBUCKET);
            }
            if (uniqueReasons.contains(reason)) {
                int row = getRowForReturnReason(reason);
                if (row > -1) {
                    reasonTable.editCellInRow(ReturnReasonProperty.CODE, row);
                }
                throw new BusinessException(ReturnMessageText.DUPLICATE_REASON_CODE);
            } else {
                uniqueReasons.add(reason);
            }
            if (reason.isDirty()) {
                modifiedReasons.add(reason);
            }
        }

        // Update
        model.updateReturnReasons(modifiedReasons);
        model.releaseReasonAdminLock();

        return true;
    }

    /****************************************************************************************************
     * Helper Method To Return Row For Reason
     ***************************************************************************************************/

    private int getRowForReturnReason(ReturnReason reason) {
        List<ReturnReasonWrapper> wrappers = reasonTable.getAllRowData();
        for (int i = 0; i < wrappers.size(); i++) {
            if (reason.equals(wrappers.get(i).getReason())) {
                return i;
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    protected void handleCancel() {
        try {
            model.releaseReasonAdminLock();
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Screen Events
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(FILTER_MODIFIED)) {
            populateScreen();
        }
    }

    private void populateScreen() {
        SourceType sourceType = (SourceType) typeFilterEditor.getSelectedItem();
        reasonTable.setRows(model.getFilteredReasons(sourceType));
    }

    /****************************************************************************************************
     * Return Reason Definition
     ***************************************************************************************************/
    private class ReturnReasonTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ReturnReasonWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(ReturnReasonProperty.CODE));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("Type", ReturnReasonProperty.TYPE, new TranslatedObjectDisplayer(), sourceTypeEditor));
            attributes.add(new SimTableAttribute("Code", ReturnReasonProperty.CODE, new TranslatedObjectDisplayer(), reasonCodeEditor));
            attributes.add(new SimTableAttribute("Description", ReturnReasonProperty.DESCRIPTION, new TranslatedObjectDisplayer(), descriptionEditor));
            attributes.add(new SimTableAttribute("Inventory Status", ReturnReasonProperty.USE_AVAILABLE, new BooleanAvailableDisplayer(), new BooleanAvailableTableEditor()));
            if (model.isNonSellableTypesActive()) {
                attributes.add(new SimTableAttribute("Sub-bucket", ReturnReasonProperty.NONSELLABLE_QTY_TYPE, new TranslatedObjectDisplayer(), nonSellableTypeTableEditor));
            }
            attributes.add(new SimTableAttribute("System", ReturnReasonProperty.SYSTEM_REQUIRED));
            return attributes;
        }
    }
}
