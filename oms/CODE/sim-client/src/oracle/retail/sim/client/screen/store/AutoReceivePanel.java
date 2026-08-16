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
 * Auto-Receive Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class AutoReceivePanel extends ScreenPanel {
    private static final long serialVersionUID = -1777839935212596995L;

    private AutoReceiveModel model = new AutoReceiveModel();

    private RDisplayLabelEditor storeEditor = new RDisplayLabelEditor("Store");
    private RListTransferPanel transferPanel = new RListTransferPanel();

    public AutoReceivePanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        storeEditor.setDisplayer(new DualAttributeDisplayer("id", "name"));
        transferPanel.setTitle("Stores", "Selected Auto-Receive Stores");
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
            storeEditor.setData(model.getMiniStore());

            List<BuddyStore> availableAutoStores = model.getAllAutoStores();
            Set<BuddyStore> selectedAutoStores = model.getSelectedAutoStores();

            transferPanel.setSelectableItems(availableAutoStores);
            transferPanel.setSelectedItems(selectedAutoStores);
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    public void handleSave() throws Exception {
        model.saveBuddyStores(transferPanel.getSelectedItems());
    }
}
