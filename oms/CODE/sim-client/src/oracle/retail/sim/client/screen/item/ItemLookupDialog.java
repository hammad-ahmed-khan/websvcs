package oracle.retail.sim.client.screen.item;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.SearchListener;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RTab;
import oracle.retail.sim.client.swing.widget.RTabbedPane;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.security.PermissionKey;

/********************************************************************************************************
 * Item Lookup Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemLookupDialog extends RDialog implements REventListener, ChangeListener {
    private static final long serialVersionUID = 2670568113760385511L;

    private RTabbedPane tabbedPane = new RTabbedPane();
    private ItemLookupTab lookupTab = new ItemLookupTab();
    private ItemDetailTab detailTab = new ItemDetailTab();
    private ItemStockTab stockTab = new ItemStockTab();
    private ItemPriceTab priceTab = new ItemPriceTab();
    private ItemNonSellableTab nonSellableTab = new ItemNonSellableTab();
    private ItemRelatedTab relatedTab = new ItemRelatedTab();
    private ItemUDATab udaTab = new ItemUDATab();
    private ItemCustomerOrderTab orderTab = new ItemCustomerOrderTab();
    private ItemImageTab imageTab = new ItemImageTab();

    private RTab lastSelectedTab = lookupTab;

    private RButton searchButton = new RButton(SimNavigation.DIALOG_SEARCH);
    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private static final String ITEM_SELECTED = "Item.selected";

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ItemLookupDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("Item Lookup");
        setSize(850, 600);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        searchButton.registerAction(this, SimNavigation.DIALOG_SEARCH);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        lookupTab.registerAction(this, ITEM_SELECTED);
    }

    private void layoutContent() {
        addButton(searchButton);
        addButton(applyButton);
        addButton(resetButton);
        addButton(cancelButton);

        tabbedPane.addTab("Lookup", lookupTab);
        tabbedPane.addTab("Details", detailTab);
        if (PermissionManager.hasPermission(PermissionKey.PC_DISPLAY_STOCK_LOCATOR)) {
            tabbedPane.addTab("Stock Locator", stockTab);
        }
        tabbedPane.addTab("Price Information", priceTab);
        if (SimConfigManager.getBoolean(SimConfigManager.ENABLE_SUB_BUCKETS)) {
            tabbedPane.addTab("Nonsellable", nonSellableTab);
        }
        tabbedPane.addTab("Related Items", relatedTab);
        if (PermissionManager.hasPermission(PermissionKey.PC_ACCESS_ITEM_UDA)) {
            tabbedPane.addTab("UDA Detail", udaTab);
        }
        tabbedPane.addTab("Customer Orders", orderTab);
        if (SimConfigManager.getBoolean(SimConfigManager.DISPLAY_ITEM_IMAGE_BUTTON)) {
            tabbedPane.addTab("Image", imageTab);
        }
        tabbedPane.setDoubleBuffered(true);
        tabbedPane.setSelectedIndex(0);
        tabbedPane.addChangeListener(this);

        RPanel mainPanel = new RPanel(new BorderLayout());
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        setDefaultButton(searchButton);
        setContentPane(mainPanel);

        validateEnabledState();
    }

    /****************************************************************************************************
     * Helper method to validate enabled state
     ***************************************************************************************************/

    private void validateEnabledState() {
        boolean isItemSelected = lookupTab.isItemSelected();

        tabbedPane.setEnabledAt("Details", isItemSelected);
        tabbedPane.setEnabledAt("Stock Locator", isItemSelected);
        tabbedPane.setEnabledAt("Price Information", isItemSelected);
        tabbedPane.setEnabledAt("Nonsellable", isItemSelected);
        tabbedPane.setEnabledAt("Related Items", isItemSelected);
        tabbedPane.setEnabledAt("UDA Detail", isItemSelected);
        tabbedPane.setEnabledAt("Customer Orders", isItemSelected);
        tabbedPane.setEnabledAt("Image", isItemSelected);
    }

    /****************************************************************************************************
     * Load Dialog Information
     ***************************************************************************************************/

    public void setItemLookupType(ItemLookupType type) {
        lookupTab.setItemLookupType(type);
    }

    public void setSearchListener(SearchListener listener) {
        lookupTab.setSearchListener(listener);
    }

    public void loadDialog() {
        try {
            lookupTab.loadDialog();
        } catch (Exception exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Handle Tab Change
     ***************************************************************************************************/

    public void stateChanged(ChangeEvent event) {
        RTab selectedTab = tabbedPane.getSelectedTab();
        if (selectedTab != lastSelectedTab) {
            try {
                if (selectedTab == detailTab) {
                    detailTab.loadTab(lookupTab.getSelectedDetailItem());
                } else if (selectedTab == stockTab) {
                    stockTab.loadTab(lookupTab.getSelectedDetailItem());
                } else if (selectedTab == priceTab) {
                    priceTab.loadTab(lookupTab.getSelectedDetailItem());
                } else if (selectedTab == nonSellableTab) {
                    nonSellableTab.loadTab(lookupTab.getSelectedDetailItem());
                } else if (selectedTab == relatedTab) {
                    relatedTab.loadTab(lookupTab.getSelectedDetailItem());
                } else if (selectedTab == udaTab) {
                    udaTab.loadTab(lookupTab.getSelectedDetailItem());
                } else if (selectedTab == orderTab) {
                    orderTab.loadTab(lookupTab.getSelectedDetailItem());
                } else if (selectedTab == imageTab) {
                    imageTab.loadTab(lookupTab.getSelectedDetailItem());
                }
                lastSelectedTab = selectedTab;
            } catch (Throwable exception) {
                displayException(exception);
                tabbedPane.setSelectedTab(lastSelectedTab);
            }
        }
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_RESET)) {
                lookupTab.doReset();
            } else if (command.equals(SimNavigation.DIALOG_SEARCH)) {
                lookupTab.doSearch();
            } else if (command.equals(SimNavigation.DIALOG_APPLY)) {
                lookupTab.doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                closeWindow();
            } else if (command.equals(ITEM_SELECTED)) {
                validateEnabledState();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Closes the Item Lookup Dialog Box
     ***************************************************************************************************/
    public void windowClosing(WindowEvent event) {
        closeWindow();
    }
}
