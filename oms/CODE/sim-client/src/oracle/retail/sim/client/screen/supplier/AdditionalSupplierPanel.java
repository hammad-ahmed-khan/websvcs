package oracle.retail.sim.client.screen.supplier;

import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.source.Supplier;

/********************************************************************************************************
 * Additional Supplier Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AdditionalSupplierPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -7235425458899468492L;

    private AdditionalSupplierModel model = new AdditionalSupplierModel();

    private SimTable supplierTable = new SimTable(new AdditionalSupplierDefinition());
    private SimTablePane supplierPane = new SimTablePane(supplierTable);

    public AdditionalSupplierPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        supplierTable.setTableEditable(false);
        supplierTable.setSingleRowSelectionMode();
        supplierTable.registerDoubleClickAction(this, SimNavigation.SUPPLIER_DETAIL);
    }

    private void layoutScreen() {
        setContentPane(supplierPane);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return supplierTable;
    }

    public void start() throws Exception {
        model.loadItem();
        supplierTable.setRows(model.getSuppliers());
    }

    public void performActionEvent(RActionEvent event) {
        if (event.getEventCommand().equals(SimNavigation.SUPPLIER_DETAIL)) {
            doSupplierSelected();
        }
    }

    private void doSupplierSelected() {
        Supplier supplier = (Supplier) supplierTable.getSelectedRowData();
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_SUPPLIER, supplier);
        navigate(SimScreenName.SUPPLIER_DETAIL_SCREEN);
    }

    /****************************************************************************************************
     * Additional Supplier Table Definition
     ***************************************************************************************************/

    private class AdditionalSupplierDefinition extends SimTableDefinition {
        public Class getDataClass() {
            return Supplier.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Supplier ID", "id"));
            attributes.add(new SimTableAttribute("Supplier Name", "name"));
            return attributes;
        }
    }
}
