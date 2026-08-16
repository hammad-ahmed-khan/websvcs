package oracle.retail.sim.client.screen.productgroup;

import java.awt.BorderLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.widget.RTab;
import oracle.retail.sim.client.swing.widget.RTabbedPane;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.productgroup.ProductGroupMessageText;

/********************************************************************************************************
 * Product Group Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupDetailPanel extends ScreenPanel implements ChangeListener, REventListener {
    private static final long serialVersionUID = 743390530366151169L;

    private ProductGroupDetailModel model = new ProductGroupDetailModel();

    private RTabbedPane tabbedPane = new RTabbedPane();
    private ProductGroupAttributeTab attributeTab = new ProductGroupAttributeTab();
    private ProductGroupComponentTab componentTab = new ProductGroupComponentTab();

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/
    public ProductGroupDetailPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        attributeTab.setModel(model);
        attributeTab.addREventListener(this);
        componentTab.setModel(model);
    }

    private void layoutPanel() {
        tabbedPane.addTab("Product Group Attributes", attributeTab);
        tabbedPane.addTab("Product Group Components", componentTab);
        tabbedPane.setEnabledAt("Product Group Components", false);
        tabbedPane.setDoubleBuffered(true);
        tabbedPane.setSelectedIndex(0);
        tabbedPane.addChangeListener(this);

        RPanel mainPanel = new RPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return null;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        if (model.loadProductGroup()) {
            attributeTab.loadTabInEditMode();
            tabbedPane.setEnabledAt(1, true);
        } else {
            attributeTab.loadTabInCreateMode();
            tabbedPane.setEnabledAt(1, false);
        }
        setContentModified(false);
    }

    public void stop() {
        try {
            model.releaseProductGroupLock();
        } catch (Throwable e) {
            displayException(e);
        }
    }

    /****************************************************************************************************
     * State Methods
     ***************************************************************************************************/

    public boolean isProductGroupEditable() throws Exception {
        return model.isProductGroupEditable();
    }

    /****************************************************************************************************
     * Handle Action Events
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimClientStateKey.PRODUCT_GROUP_SELECTED)) {
                doProductGroupSelected();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doProductGroupSelected() {
        tabbedPane.setEnabledAt(1, model.isTypeSelected());
    }

    /****************************************************************************************************
     * State Changed Event Handler - When tabs are switched.
     ***************************************************************************************************/

    public void stateChanged(ChangeEvent event) {
        RTab selectedTab = tabbedPane.getSelectedTab();
        try {
            if (selectedTab == componentTab) {
                componentTab.loadTab();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Handle Cancel Action
     ***************************************************************************************************/

    public boolean handleCancel() throws Exception {
        if (model.isProductGroupModified() || isContentModified()) {
            if (!RConfirmUtility.confirm("Confirmation", ProductGroupMessageText.UNSAVED_CHANGES_MESSAGE)) {
                return false;
            }
        }
        return true;
    }

    /****************************************************************************************************
     * Handle Save Action
     ***************************************************************************************************/

    public boolean handleSave() throws Exception {
        return attributeTab.handleSave();
    }
}
