package oracle.retail.sim.client.screen.storesequence;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ListSelectionModel;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.displayer.StoreSequenceAreaDescDisplayer;
import oracle.retail.sim.client.editor.ItemVOSearchProcessor;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.tableeditor.StringTableEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.tableeditor.SequenceAreaTypeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.item.ItemVO;
import oracle.retail.sim.common.itemticket.ItemTicket;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.storesequence.StoreSequenceAreaType;
import oracle.retail.sim.common.storesequence.StoreSequenceMessageText;
import oracle.retail.sim.common.storesequence.StoreSequenceProperty;

/********************************************************************************************************
 * Store Sequence List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 4505807530169824224L;

    private StoreSequenceListModel model = new StoreSequenceListModel();

    private REditorPanel itemPanel = new REditorPanel(1);
    private RSearchFieldEditor itemEditor = new RSearchFieldEditor("Find Item");
    private StoreSequenceAreaDescDisplayer descriptionDisplayer = new StoreSequenceAreaDescDisplayer();
    private StringTableEditor descriptionEditor = new StringTableEditor(SimName.SEQUENCE_DESCRIPTION);
    private SimTable sequenceAreaTable = new SimTable(new StoreSequenceTableDefinition());
    private SimTablePane sequenceAreaPane = new SimTablePane(sequenceAreaTable);

    private static final String STORE_SEQUENCE_SELECTED = "StoreSequence.selected";

    public StoreSequenceListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        itemEditor.setIdentifier(SimName.ITEM_ID);
        itemEditor.setSearchProcessor(new ItemVOSearchProcessor(true));
        itemEditor.setSearchListener(buildItemSearchListener());

        sequenceAreaTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        sequenceAreaTable.setSortingEnabled(false);
    }

    private void layoutScreen() {
        itemPanel.add(itemEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(itemPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(sequenceAreaPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return sequenceAreaTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() {
        populateScreen();
    }

    public void resume() {
        populateScreen();
    }

    private void populateScreen() {
        if (isEditMode()) {
            sequenceAreaTable.removeDoubleClickAction();
        } else {
            sequenceAreaTable.registerDoubleClickAction(this, STORE_SEQUENCE_SELECTED);
        }
        try {
            itemPanel.setVisible(!isEditMode());
            itemEditor.clear();

            sequenceAreaTable.setTableEditable(isEditMode());

            String itemId = model.getSelectedItemId();
            if (itemId != null) {
                itemEditor.setText(itemId);
            }

            model.loadSequenceAreas();

            sequenceAreaTable.setRows(model.getSequenceAreaWrappers());
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * State Methods
     ***************************************************************************************************/

    public boolean isEditMode() {
        return model.isEditMode();
    }

    /****************************************************************************************************
     * Handle Cancel
     ***************************************************************************************************/

    public boolean handleCancel() throws Exception {
        if (isEditMode()) {
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_EDIT_MODE);
            model.releaseSequenceLock();
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        sequenceAreaTable.stopEditing();
        if (isEditMode() && isAllSequencesCoherent()) {
            model.updateStoreSequences();
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_EDIT_MODE);
            return true;
        }
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_STORE_SEQUENCE);
        RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_ITEM_FILTER);
        return false;
    }

    /****************************************************************************************************
     * Handle Move Up
     ***************************************************************************************************/

    public void handleMoveUp() throws BusinessException {
        sequenceAreaTable.stopEditing();

        if (isAllSequencesCoherent()) {
            int firstRow = sequenceAreaTable.getSelectedRow();
            if (firstRow < 0) {
                return;
            }
            int[] rows = sequenceAreaTable.getSelectedRows();
            int beforeIndex = firstRow - 1;
            if (beforeIndex < 0) {
                return;
            }

            StoreSequenceAreaWrapper beforeWrapper = (StoreSequenceAreaWrapper) sequenceAreaTable.getRowData(beforeIndex);
            StoreSequenceAreaWrapper moveWrapper = null;

            for (int row : rows) {
                moveWrapper = (StoreSequenceAreaWrapper) sequenceAreaTable.getRowData(row);
                moveWrapper.swapSequenceOrder(beforeWrapper);
            }

            sequenceAreaTable.setRows(model.getSequenceAreaWrappers());
            sequenceAreaTable.setRowSelectionInterval(beforeIndex, beforeIndex + rows.length - 1);
        }
    }

    /****************************************************************************************************
     * Handle Move Down
     ***************************************************************************************************/

    public void handleMoveDown() throws BusinessException {
        sequenceAreaTable.stopEditing();
        if (isAllSequencesCoherent()) {
            if (sequenceAreaTable.getSelectedRow() < 0) {
                return;
            }

            int[] rows = sequenceAreaTable.getSelectedRows();
            int lastSelectedRow = rows[rows.length - 1];
            int afterIndex = lastSelectedRow + 1;
            if (afterIndex > sequenceAreaTable.getRowCount() - 1) {
                return;
            }

            StoreSequenceAreaWrapper afterWrapper = (StoreSequenceAreaWrapper) sequenceAreaTable.getRowData(afterIndex);
            StoreSequenceAreaWrapper moveWrapper = null;

            for (int i = rows.length - 1; i >= 0; i--) {
                moveWrapper = (StoreSequenceAreaWrapper) sequenceAreaTable.getRowData(rows[i]);
                moveWrapper.swapSequenceOrder(afterWrapper);
            }

            sequenceAreaTable.setRows(model.getSequenceAreaWrappers());
            sequenceAreaTable.setRowSelectionInterval(afterIndex - rows.length + 1, afterIndex);
        }
    }

    /****************************************************************************************************
     * Handle Print
     ***************************************************************************************************/

    public void handlePrint() throws Exception {
        int totalSize = 0;
        if (sequenceAreaTable.getSelectedRowCount() == 0) {
            displayWarning(ReportMessageText.NO_ROWS_SELECTED_PRINT);
            return;
        }
        if (!RConfirmUtility.confirm("Print Confirmation", StoreSequenceMessageText.PRINT_LABELS_CONFIRM)) {
            return;
        }

        List<StoreSequenceAreaWrapper> wrappers = sequenceAreaTable.getAllSelectedRowData();
        for (StoreSequenceAreaWrapper wrapper : wrappers) {
            if (wrapper.getId() == null) {
                displayError(StoreSequenceMessageText.UNSAVED_LOCATION_ERROR);
                return;
            }
            if (wrapper.isNotSequenced()) {
                displayError(StoreSequenceMessageText.CANNOT_PRINT_NO_LOCATION);
                return;
            }
            totalSize = totalSize + wrapper.getNumberOfItems();
        }

        List<ItemTicket> itemTickets = model.buildTickets(wrappers);
        int failedCount = model.printTickets(itemTickets);

        if (failedCount == -1) { // Check for authentication failure
            displayMessage(ReportMessageText.BIP_AUTHENTICATION_FAILURE_MESSAGE);
        } else if (itemTickets == null || totalSize > itemTickets.size() || itemTickets.size() == 0 && failedCount == 0 || failedCount != 0) {
            displayMessage(StoreSequenceMessageText.TICKET_PRINT_FAILED);
        } else if (totalSize == itemTickets.size() && failedCount == 0) {
            displayMessage(StoreSequenceMessageText.SHELF_LABELS_PRINTED);
        }
    }

    /****************************************************************************************************
     * Handle Item Search
     ***************************************************************************************************/

    public boolean storeItemForLookup() {
        try {
            model.storeItemForLookup(itemEditor.getText());
            return model.obtainSequenceLock();
        } catch (Throwable exception) {
            displayException(exception);
            return false;
        }
    }

    /****************************************************************************************************
     * Handle Remove Sequence
     ***************************************************************************************************/

    public void handleRemoveSequence() throws BusinessException {
        if (sequenceAreaTable.getSelectedRowCount() == 0) {
            displayError(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }
        if (!RConfirmUtility.confirm("Delete Confirmation", StoreSequenceMessageText.DELETE_LOCATION_CONFIRM)) {
            return;
        }

        sequenceAreaTable.stopEditing();

        List<StoreSequenceAreaWrapper> wrappers = sequenceAreaTable.getAllSelectedRowData();
        for (StoreSequenceAreaWrapper wrapper : wrappers) {
            model.removeSequenceArea(wrapper);
            sequenceAreaTable.removeRow(wrapper);
        }
    }

    /****************************************************************************************************
     * Handle Apply Class List
     ***************************************************************************************************/

    public void handleApplyClassList() throws Exception {
        if (isAllSequencesCoherent()) {
            if (RConfirmUtility.confirm("Confirmation", StoreSequenceMessageText.GENERATE_SEQUENCE_CONFIRM)) {
                StoreSequenceAreaType sequenceArea = StoreSequenceAreaType.BACKROOM;
                if (RConfirmUtility.confirm("Select Area", StoreSequenceMessageText.SHOPFLOOR_OR_BACKROOM, "Shopfloor", "Backroom")) {
                    sequenceArea = StoreSequenceAreaType.SHOPFLOOR;
                }
                try {
                    model.applyClassList(sequenceArea);
                } catch (BusinessException exception) {
                    displayException(exception);
                }
                sequenceAreaTable.setRows(model.getSequenceAreaWrappers());
            }
        }
    }

    /****************************************************************************************************
     * Handle Add Location
     ***************************************************************************************************/

    public void handleAddSequence() throws BusinessException {
        if (isAllSequencesCoherent()) {
            sequenceAreaTable.stopEditing();
            sequenceAreaTable.addRow(model.createSequenceAreaWrapper());
            sequenceAreaTable.editCellInLastRow("description");
        }
    }

    /****************************************************************************************************
     * Handle Edit Locations
     ***************************************************************************************************/

    public void handleEditSequences() throws Exception {
        if (model.obtainSequenceLock()) {
            RepositoryManager.addStateObject(SimClientStateKey.STORE_SEQUENCE_EDIT_MODE, Boolean.TRUE);
        } else {
            RepositoryManager.removeStateObject(SimClientStateKey.STORE_SEQUENCE_EDIT_MODE);
        }
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(STORE_SEQUENCE_SELECTED) && model.obtainSequenceLock()) {
                doStoreSequenceSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doStoreSequenceSelected() throws Exception {
        StoreSequenceAreaWrapper wrapper = (StoreSequenceAreaWrapper) sequenceAreaTable.getSelectedRowData();

        model.storeSelectedSequenceArea(wrapper);

        if (wrapper.isNotSequenced()) {
            navigate(SimScreenName.STORE_SEQUENCE_NO_AREA_SCREEN);
        } else {
            navigate(SimScreenName.STORE_SEQUENCE_ITEM_SCREEN);
        }
    }

    private boolean isAllSequencesCoherent() throws BusinessException {
        return model.isSequenceAreasCoherent(sequenceAreaTable.getAllRowData());
    }

    /****************************************************************************************************
     * Item Search Listener - pops open the item lookup dialog
     ***************************************************************************************************/

    private ItemSearchListener buildItemSearchListener() {
        return new ItemSearchListener() {
            public void assignItem(ItemVO itemVO) {
                if (itemVO != null) {
                    itemEditor.setData(itemVO);
                }
            }
        };
    }

    /****************************************************************************************************
     * Store Sequence Table Definition
     ***************************************************************************************************/

    public class StoreSequenceTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StoreSequenceAreaWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(3);
            attributes.add(new SimTableAttribute("Location", StoreSequenceProperty.AREA_DESCRIPTION, descriptionDisplayer, descriptionEditor));
            attributes.add(new SimTableAttribute("Area", StoreSequenceProperty.AREA_TYPE, new TranslatedObjectDisplayer(), new SequenceAreaTypeTableEditor(), true));
            attributes.add(new SimTableAttribute("Total Items", StoreSequenceProperty.AREA_ITEM_COUNT, false));
            return attributes;
        }
    }
}
