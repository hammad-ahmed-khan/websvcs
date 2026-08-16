package oracle.retail.sim.client.screen.store;

import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.swing.displayer.AttributeComparator;
import oracle.retail.sim.client.swing.displayer.DualAttributeDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.RListTransferPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Store Admin Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimManagedStoresPanel extends ScreenPanel implements PropertyChangeListener {
    private static final long serialVersionUID = 3604762316175947653L;

    private SimManagedStoresModel model = new SimManagedStoresModel();

    private RDisplayLabelEditor storeEditor = new RDisplayLabelEditor("Store");
    private RListTransferPanel transferPanel = new RListTransferPanel();

    private boolean storeSelectionModified;

    public SimManagedStoresPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        storeEditor.setDisplayer(new StoreDisplayer());
        transferPanel.setTitle("Stores", "SIM Managed Stores");
        transferPanel.setRowDisplayer(new DualAttributeDisplayer("id", "name"));
        transferPanel.setRowComparator(new AttributeComparator("name"));
        transferPanel.setIncludeAllOptions(true);
        transferPanel.addPropertyChangeListener(this);
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(storeEditor);

        RDivider divider = new RDivider(RDivider.HORIZONTAL);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(divider, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(transferPanel, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    public void start() {
        try {
            storeEditor.setData(model.getStore());

            List<Store> availableStores = model.getAllStores();
            List<Store> selectedStores = model.getSelectedStores(availableStores);

            transferPanel.setSelectableItems(availableStores);
            transferPanel.setSelectedItems(selectedStores);

            storeSelectionModified = false;
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    public void handleSave() throws Exception {
        if (storeSelectionModified) {
            model.updateStores(transferPanel.getRemainingSelectableItems(), transferPanel.getSelectedItems());
        }
    }

    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(UIPropertyName.LIST_TRANSFER_OCCURRED)) {
            storeSelectionModified = true;
        }
    }
}
