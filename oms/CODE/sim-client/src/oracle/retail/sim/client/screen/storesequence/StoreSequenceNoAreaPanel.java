package oracle.retail.sim.client.screen.storesequence;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.StoreSequenceAreaDisplayer;
import oracle.retail.sim.client.displayer.TicketTypeFormatDisplayer;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.displayer.QuantityDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.tableeditor.StoreSequenceAreaTableEditor;
import oracle.retail.sim.client.tableeditor.TicketTypeFormatTableEditor;
import oracle.retail.sim.client.uom.LineItemQuantityTableEditor;
import oracle.retail.sim.client.uom.UomModeDisplayer;
import oracle.retail.sim.client.uom.UomModeTableEditor;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.storesequence.StoreSequenceArea;
import oracle.retail.sim.common.storesequence.StoreSequenceMessageText;
import oracle.retail.sim.common.storesequence.StoreSequenceProperty;

/********************************************************************************************************
 * Store Sequence No Area Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreSequenceNoAreaPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 440208981278731459L;

    private StoreSequenceNoAreaModel model = new StoreSequenceNoAreaModel();

    private RDisplayLabelEditor sequenceEditor = new RDisplayLabelEditor("Location");
    private RDisplayLabelEditor totalItemsEditor = new RDisplayLabelEditor("Total Items");

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private StoreSequenceAreaTableEditor sequenceAreaTableEditor = new StoreSequenceAreaTableEditor();
    private TicketTypeFormatTableEditor shelfLabelTableEditor = new TicketTypeFormatTableEditor();

    private SimTable sequenceItemTable = new SimTable(new SequenceItemTableDefinition());
    private SimTablePane sequenceItemPane = new SimTablePane(sequenceItemTable);

    private StoreSequenceNoAreaFilterDialog filterDialog = new StoreSequenceNoAreaFilterDialog();

    private static final String FILTER_SELECTED = "Filter.selected";

    public StoreSequenceNoAreaPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, FILTER_SELECTED);
        sequenceEditor.setDisplayer(new StoreSequenceAreaDisplayer());

        sequenceAreaTableEditor.setSelectionRequired(true);

        sequenceItemTable.setColumnSize(StoreSequenceProperty.CAPACITY, SimTable.LABEL_WIDTH);
        sequenceItemTable.setColumnSize(StoreSequenceProperty.WIDTH, SimTable.LABEL_WIDTH);
        sequenceItemTable.setColumnSize(StoreSequenceProperty.QUANTITY, SimTable.LABEL_WIDTH);
        sequenceItemTable.setSingleRowSelectionMode();

        filterDialog.addREventListener(this);
    }

    private void layoutScreen() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(filterEditor, GridTool.constraints(0, 0, 2, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        headerPanel.add(sequenceEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        headerPanel.add(totalItemsEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(sequenceItemPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return sequenceItemTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadSequenceAreas();

        sequenceAreaTableEditor.setSequenceAreas(model.getSequenceAreas());
        shelfLabelTableEditor.setItems(model.getShelfLabelFormats());
        sequenceItemTable.setTableEditable(!model.isActiveAllLocationCount());

        StoreSequenceArea noAreaSequence = model.getNoAreaStoreSequence();
        if (noAreaSequence != null) {
            sequenceEditor.setData(noAreaSequence);
            totalItemsEditor.setData(noAreaSequence.getNumberOfItems());
        }
        refreshTable();
    }

    public void resume() throws Exception {
        refreshTable();
    }

    public void stop() {
        model.releaseAllSequenceLock();
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        sequenceItemTable.stopEditing();
        if (RConfirmUtility.confirm("Exit Confirmation", StoreSequenceMessageText.NO_LOCATION_DONE_CONFIRM)) {
            List<StoreSequenceItemWrapper> wrappers = sequenceItemTable.getAllRowData();
            model.updateSequenceItems(wrappers);
            sequenceItemTable.clearRows();
            return true;
        }
        return false;
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(FILTER_SELECTED)) {
                doFilterSelected();
            } else if (command.equals(SimClientStateKey.STORE_SEQUENCE_ITEM_FILTER_MODIFIED)) {
                refreshTable();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void refreshTable() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        sequenceItemTable.setRows(model.findSequenceItems());
    }

    /****************************************************************************************************
     * Store Sequence No Area Table Definition
     ***************************************************************************************************/

    private class SequenceItemTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return StoreSequenceItemWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(8);
            attributes.add(new SimTableAttribute("Item", StoreSequenceProperty.STOCK_ITEM, new AttributeDisplayer("id")));
            attributes.add(new SimTableAttribute("Item Description", StoreSequenceProperty.ITEM_DESCRIPTION));
            attributes.add(new SimTableAttribute("Location", StoreSequenceProperty.SEQUENCE_AREA, new StoreSequenceAreaDisplayer(), sequenceAreaTableEditor));
            attributes.add(new SimTableAttribute("Capacity", StoreSequenceProperty.CAPACITY, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("Width", StoreSequenceProperty.WIDTH, new QuantityDisplayer(), new LineItemQuantityTableEditor()));
            attributes.add(new SimTableAttribute("UOM", StoreSequenceProperty.UOM_MODE, new UomModeDisplayer(), new UomModeTableEditor()));
            attributes.add(new SimTableAttribute("Label Format", StoreSequenceProperty.LABEL_FORMAT, new TicketTypeFormatDisplayer(), shelfLabelTableEditor));
            attributes.add(new SimTableAttribute("Label Qty", StoreSequenceProperty.QUANTITY));
            return attributes;
        }
    }
}
