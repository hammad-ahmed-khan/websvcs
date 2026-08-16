package oracle.retail.sim.client.screen.uin;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.report.ReportMessageText;
import oracle.retail.sim.common.report.ReportResponse;
import oracle.retail.sim.common.uin.UINDetailVO;
import oracle.retail.sim.common.uin.UINMessageText;

/********************************************************************************************************
 * UIN Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINDetailPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 7822192350506339289L;

    private UINDetailModel model = new UINDetailModel();

    private RTextFieldEditor itemEditor = new RTextFieldEditor("Item");
    private RTextFieldEditor itemDescEditor = new RTextFieldEditor("Item Description");
    private RTextFieldEditor uinTypeEditor = new RTextFieldEditor("UIN Type");
    private RTextFieldEditor captureTimeEditor = new RTextFieldEditor("Capture Time");

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();
    private UINDetailFilterDialog filterDialog = new UINDetailFilterDialog();

    private SimTable serialNumberTable = new SimTable(new UINDetailLookupTableDefinition());
    private SimTablePane serialNumberPane = new SimTablePane(serialNumberTable);

    private static final String FILTER_SELECTED = "SerialNumber.filterSelected";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public UINDetailPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        itemEditor.setIdentifier(SimName.ITEM_ID);
        itemEditor.setSizeType(EditorConstants.MEDIUM);
        itemEditor.setEnabled(false);
        itemDescEditor.setIdentifier(SimName.ITEM_DESCRIPTION);
        itemDescEditor.setEnabled(false);

        uinTypeEditor.setEnabled(false);
        captureTimeEditor.setEnabled(false);

        filterEditor.registerAction(this, FILTER_SELECTED);
        filterDialog.addREventListener(this);

        serialNumberTable.setTableEditable(false);
        serialNumberTable.setMultipleRowSelectionMode();
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.setLineBorder(1);
        headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 5, 0, 0, 0));
        headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 5, 0, 0, 0));
        headerPanel.add(uinTypeEditor, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 3, 5, 0, 5, 0));
        headerPanel.add(captureTimeEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 5, 0, 5, 0));

        LayoutUtility.alignEditorsInGridBag(headerPanel);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(filterEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 5, 0, 0, 0));
        mainPanel.add(serialNumberPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return serialNumberTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public boolean isSerialNumberType() {
        return model.isSerialNumberType();
    }

    public void start() throws Exception {
        model.loadItem();

        StockItem stockItem = model.getStockItem();

        itemEditor.setText(stockItem.getId());
        itemDescEditor.setText(stockItem.getItemDescription());
        uinTypeEditor.setText(Translator.getText(stockItem.getUINType().toString()));
        captureTimeEditor.setText(Translator.getText(model.getCaptureTimeDescription()));

        populateScreen();
    }

    public void resume() throws Exception {
        populateScreen();
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        serialNumberTable.setRows(model.findUINDetailLookupVOs());
    }

    /****************************************************************************************************
     * Handle View History
     ***************************************************************************************************/

    public void handleViewHistory() throws Exception {
        List<UINDetailVO> detailContainerVOs = serialNumberTable.getAllSelectedRowData();
        if (detailContainerVOs.size() != 1) {
            throw new BusinessException(UINMessageText.UIN_VIEW_HISTORY_ROW_ERROR);
        }
        model.storeUINDetail(detailContainerVOs.get(0));
    }

    /****************************************************************************************************
     * Handle Print Ticket
     ***************************************************************************************************/

    public void handlePrintTicket() throws Exception {
        List<UINDetailVO> detailVOs = serialNumberTable.getAllSelectedRowData();
        if (detailVOs.isEmpty()) {
            throw new BusinessException(ReportMessageText.NO_ROW_SELECTED_FOR_PRINTING);
        }
        ReportResponse response = model.printTickets(detailVOs);
        if (response == null) {
            displayMessage(ReportMessageText.ITEM_TICKETS_PRINTED);
        } else {
            displayMessage(response.getMessage(), response.getMessageValue());
        }
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(FILTER_SELECTED)) {
                doFilterSelected();
            } else if (command.equals(SimClientStateKey.ITEM_UIN_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    /****************************************************************************************************
     * UIN STATUS TABLE DEFINITION
     ***************************************************************************************************/

    private static class UINDetailLookupTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return UINDetailVO.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(6);
            attributes.add(new SimTableAttribute("UIN", "uin"));
            attributes.add(new SimTableAttribute("Status", "status", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("Container", "cartonId"));
            attributes.add(new SimTableAttribute("Functional Area", "functionalArea",new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Identifier", "functionalAreaId"));
            attributes.add(new SimTableAttribute("Damaged", "damaged"));
            return attributes;
        }
    }
}
