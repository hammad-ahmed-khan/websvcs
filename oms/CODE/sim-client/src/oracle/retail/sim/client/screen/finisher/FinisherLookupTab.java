package oracle.retail.sim.client.screen.finisher;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.widget.SimTab;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.source.Finisher;
import oracle.retail.sim.common.source.FinisherVO;
import oracle.retail.sim.common.source.SourceQueryFilter;

/********************************************************************************************************
 * Finisher Lookup Tab
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FinisherLookupTab extends SimTab {
    private static final long serialVersionUID = -7374652129677305380L;

    private FinisherLookupTabModel model = new FinisherLookupTabModel();

    private RTextFieldEditor finisherIdEditor = new RTextFieldEditor("Finisher ID");
    private RTextFieldEditor finisherNameEditor = new RTextFieldEditor("Finisher Name");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private SimTable finisherTable = new SimTable(new FinisherTableDefinition());
    private SimTablePane finisherPane = new SimTablePane(finisherTable);

    /****************************************************************************************************
     * Build Tab
     ***************************************************************************************************/
    public FinisherLookupTab() {
        setTitle("Finisher Lookup");
        setSize(800, 600);
        initializeTab();
        layoutTab();
    }

    private void initializeTab() {
        finisherIdEditor.setIdentifier(SimName.FINISHER_ID);
        finisherNameEditor.setIdentifier(SimName.FINISHER_NAME);
        searchLimitEditor.setIdentifier(SimName.FINISHER_SEARCH_LIMIT);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.setRequired(true);

        finisherTable.setTableEditable(false);
        finisherTable.setSingleRowSelectionMode();
    }

    private void layoutTab() {
        REditorPanel editorPanel = new REditorPanel(1, 3);
        editorPanel.add(finisherIdEditor);
        editorPanel.add(finisherNameEditor);
        editorPanel.add(searchLimitEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        setLayout(new GridBagLayout());
        add(editorPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        add(finisherPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
    }

    /****************************************************************************************************
     * Get and Set Dialog Properties
     ***************************************************************************************************/

    protected void registerAction(REventListener listener, String actionCommand) {
        finisherTable.registerSingleClickAction(listener, actionCommand);
    }

    protected boolean isFinisherSelected() {
        return finisherTable.getSelectedRowCount() > 0;
    }

    /****************************************************************************************************
     * RESET
     ***************************************************************************************************/

    protected void doReset() {
        finisherTable.clearRows();
        finisherIdEditor.clear();
        finisherNameEditor.clear();
        searchLimitEditor.setInteger(99);
        finisherIdEditor.requestFocusInWindow();
    }

    /****************************************************************************************************
     * SEARCH
     ***************************************************************************************************/

    protected void doSearch() throws Exception {
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
    }

    /****************************************************************************************************
     * Retrieve Finisher
     ***************************************************************************************************/

    public Finisher getSelectedFinisher() throws Exception {
        FinisherVO finisherVO = (FinisherVO) finisherTable.getSelectedRowData();
        if (finisherVO != null) {
            return model.getFinisher(finisherVO);
        }
        return null;
    }

    /****************************************************************************************************
     * FINISHER TABLE DEFINITION
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
