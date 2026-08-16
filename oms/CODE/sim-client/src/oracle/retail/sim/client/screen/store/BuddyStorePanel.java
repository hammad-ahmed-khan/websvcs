package oracle.retail.sim.client.screen.store;

import java.awt.GridBagLayout;
import java.util.List;
import java.util.Set;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.AttributeComparator;
import oracle.retail.sim.client.swing.displayer.DualAttributeDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.RListTransferPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.store.BuddyStore;

/********************************************************************************************************
 * Buddy Store Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class BuddyStorePanel extends ScreenPanel {
    private static final long serialVersionUID = -118453562617580528L;

    private BuddyStoreModel model = new BuddyStoreModel();

    private RDisplayLabelEditor storeEditor = new RDisplayLabelEditor("Store");
    private RListTransferPanel transferPanel = new RListTransferPanel();

    public BuddyStorePanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        storeEditor.setDisplayer(new DualAttributeDisplayer("id", "name"));
        transferPanel.setTitle("Stores", "Selected Buddy Stores");
        transferPanel.setRowDisplayer(new DualAttributeDisplayer("id", "name"));
        transferPanel.setRowComparator(new AttributeComparator("name"));
        transferPanel.setIncludeAllOptions(true);
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

            List<BuddyStore> availableBuddyStores = model.getAllBuddyStores();
            Set<BuddyStore> selectedBuddyStores = model.getSelectedBuddyStores();

            transferPanel.setSelectableItems(availableBuddyStores);
            transferPanel.setSelectedItems(selectedBuddyStores);
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    public void handleSave() throws Exception {
        model.saveBuddyStores(transferPanel.getSelectedItems());
    }
}
