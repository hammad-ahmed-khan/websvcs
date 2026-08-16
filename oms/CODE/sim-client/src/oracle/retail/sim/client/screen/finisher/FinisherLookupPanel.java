package oracle.retail.sim.client.screen.finisher;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.source.FinisherVO;
import oracle.retail.sim.common.source.SourceQueryFilter;

/********************************************************************************************************
 * Finisher Lookup Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FinisherLookupPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 7745777389673227364L;

    private FinisherLookupModel model = new FinisherLookupModel();

    private RTextFieldEditor finisherIdEditor = new RTextFieldEditor("Finisher ID");
    private RTextFieldEditor finisherNameEditor = new RTextFieldEditor("Finisher Name");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private SimTable finisherTable = new SimTable(new FinisherTableDefinition());
    private SimTablePane finisherPane = new SimTablePane(finisherTable);

    private static final String FINISHED_SELECTED = "Finished Selected";

    public FinisherLookupPanel() {
        initializeEditors();
        layoutScreen();
    }

    private void initializeEditors() {
        finisherIdEditor.setIdentifier(SimName.FINISHER_ID);
        finisherNameEditor.setIdentifier(SimName.FINISHER_NAME);
        searchLimitEditor.setIdentifier(SimName.FINISHER_SEARCH_LIMIT);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.setRequired(true);

        finisherTable.setTableEditable(false);
        finisherTable.setSingleRowSelectionMode();
        finisherTable.registerDoubleClickAction(this, FINISHED_SELECTED);
    }

    private void layoutScreen() {
        REditorPanel editorPanel = new REditorPanel(1, 3);
        editorPanel.add(finisherIdEditor);
        editorPanel.add(finisherNameEditor);
        editorPanel.add(searchLimitEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(editorPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(finisherPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return finisherTable;
    }

    /****************************************************************************************************
     * START
     ***************************************************************************************************/

    public void start() {

    }

    public void assignFocusInScreen() {
        assignFocusInScreen(finisherIdEditor);
    }

    /****************************************************************************************************
     * SEARCH
     ***************************************************************************************************/

    public void handleSearch() throws Exception {
        SourceQueryFilter filter = BOFactory.createSourceQueryFilter();
        filter.setStoreId(model.getStoreId());
        filter.setFinisherId(finisherIdEditor.getTextOrNull());
        filter.setFinisherName(finisherNameEditor.getTextOrNull());
        filter.setSearchLimit(searchLimitEditor.getIntegerValue());

        finisherTable.setRows(model.findFinishers(filter));

        if (finisherTable.isEmpty()) {
            displayWarning(CommonMessageText.NO_RECORDS_FOUND);

            if (finisherNameEditor.isEmpty()) {
                finisherIdEditor.requestFocusInWindow();
            } else {
                finisherNameEditor.requestFocusInWindow();
            }
        }

        if (finisherTable.getRowCount() == 1) {
            model.storeFinisher((FinisherVO) finisherTable.getRowData(0));
            navigate(SimScreenName.FINISHER_DETAIL_SCREEN);
        }
    }

    /****************************************************************************************************
     * SAVE
     ***************************************************************************************************/

    private void storeSelectedFinisher() throws Exception {
        model.storeFinisher((FinisherVO) finisherTable.getSelectedRowData());
    }

    /****************************************************************************************************
     * FINISHER SCREEN EVENTS - Table Double Click
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(FINISHED_SELECTED)) {
                storeSelectedFinisher();
                navigate(SimScreenName.FINISHER_DETAIL_SCREEN);
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * SUPPLIER TABLE DEFINITION
     ***************************************************************************************************/

    private class FinisherTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FinisherVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> columns = new ArrayList<>();
            columns.add(new SimTableAttribute("Finisher ID", "id"));
            columns.add(new SimTableAttribute("Finisher Name", "name"));
            return columns;
        }
    }
}
