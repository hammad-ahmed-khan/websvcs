package oracle.retail.sim.client.screen.supplier;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
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
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.source.SourceQueryFilter;
import oracle.retail.sim.common.source.SupplierVO;

/********************************************************************************************************
 * Supplier Lookup Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SupplierLookupPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 7745777389673227364L;

    private SupplierLookupModel model = new SupplierLookupModel();

    private RTextFieldEditor supplierIdEditor = new RTextFieldEditor("Supplier ID");
    private RTextFieldEditor supplierNameEditor = new RTextFieldEditor("Supplier Name");
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private SimTable supplierTable = new SimTable(new SupplierTableDefinition());
    private SimTablePane supplierPane = new SimTablePane(supplierTable);

    public SupplierLookupPanel() {
        initializeEditors();
        layoutScreen();
    }

    private void initializeEditors() {
        supplierIdEditor.setIdentifier(SimName.SUPPLIER_ID);
        supplierNameEditor.setIdentifier(SimName.SUPPLIER_NAME);
        searchLimitEditor.setIdentifier(SimName.SUPPLIER_SEARCH_LIMIT);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
        searchLimitEditor.setInteger(model.getDefaultSearchLimit());
        searchLimitEditor.setRequired(true);

        supplierTable.setTableEditable(false);
        supplierTable.setSingleRowSelectionMode();
        supplierTable.registerDoubleClickAction(this, SimNavigation.SUPPLIER_DETAIL);
    }

    private void layoutScreen() {
        REditorPanel editorPanel = new REditorPanel(1, 3);
        editorPanel.add(supplierIdEditor);
        editorPanel.add(supplierNameEditor);
        editorPanel.add(searchLimitEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(editorPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
        mainPanel.add(supplierPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return supplierTable;
    }

    /****************************************************************************************************
     * START
     ***************************************************************************************************/

    public void start() {
        supplierIdEditor.clear();
        supplierNameEditor.clear();
        supplierTable.clearRows();
    }

    public void assignFocusInScreen() {
        assignFocusInScreen(supplierIdEditor);
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_SUPPLIER);
    }

    /****************************************************************************************************
     * HANDLE SEARCH
     ***************************************************************************************************/

    public void handleSearch() throws Exception {
        SourceQueryFilter supplierFilter = BOFactory.createSourceQueryFilter();
        supplierFilter.setStoreId(model.getStoreId());
        supplierFilter.setSupplierId(supplierIdEditor.getTextOrNull());
        supplierFilter.setSupplierName(supplierNameEditor.getTextOrNull());
        supplierFilter.setSearchLimit(searchLimitEditor.getIntegerValue());

        supplierTable.setRows(model.findSuppliers(supplierFilter));

        if (supplierTable.isEmpty()) {
            displayWarning(CommonMessageText.NO_RECORDS_FOUND);

            if (supplierNameEditor.isEmpty()) {
                supplierIdEditor.requestFocusInWindow();
            } else {
                supplierNameEditor.requestFocusInWindow();
            }
        }

        if (supplierTable.getRowCount() == 1) {
            SupplierVO supplier = (SupplierVO) supplierTable.getRowData(0);
            model.storeSupplier(supplier);
            navigate(SimScreenName.SUPPLIER_DETAIL_SCREEN);
        }
    }

    /****************************************************************************************************
     * HANDLE SUPPLIER DETAIL
     ***************************************************************************************************/

    public void handleSupplierDetail() throws Exception {
        model.storeSupplier((SupplierVO) supplierTable.getSelectedRowData());
    }

    /****************************************************************************************************
     * SUPPLIER SCREEN EVENTS - Table Double Click
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.SUPPLIER_DETAIL)) {
                doSupplierSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSupplierSelected() throws Exception {
        model.storeSupplier((SupplierVO) supplierTable.getSelectedRowData());
        navigate(SimScreenName.SUPPLIER_DETAIL_SCREEN);
    }

    /****************************************************************************************************
     * SUPPLIER TABLE DEFINITION
     ***************************************************************************************************/

    private class SupplierTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return SupplierVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> columns = new ArrayList<>(2);
            columns.add(new SimTableAttribute("Supplier ID", "id"));
            columns.add(new SimTableAttribute("Supplier Name", "name"));
            return columns;
        }
    }
}
