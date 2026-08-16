package extra.retail.sim.client.screen.item;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.util.SimClientStateKey;

import extra.retail.sim.client.core.ExtraSimNavigation;

/********************************************************************************************************
 * Item Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraItemDetailScreen extends SimScreen {
    private static final long serialVersionUID = 3594523092814484365L;

    private ExtraItemDetailPanel panel = new ExtraItemDetailPanel();

    public ExtraItemDetailScreen() {
        add(panel);
    }

    /****************************************************************************************************
     * Screen Methods
     ***************************************************************************************************/

    public String getScreenName() {
        return "Item Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        panel.loadItem();
        displayMenu();
        panel.start();
    }

    public void resume() throws Exception {
        displayMenu();
    }

    // If the user does not have permission to STOCK LOCATOR, then hide the stock locator button
    private void displayMenu() throws Exception {
        showMenu(SimNavigation.BACK);
        if (!panel.showComponentInfo()) {
            removeNavButton(SimNavigation.COMPONENT_INFO);
        }
        if (!panel.showPackInfo()) {
            removeNavButton(SimNavigation.PACK_INFO);
        }
        if (!panel.showStockLocatorButton()) {
            removeNavButton(SimNavigation.STOCK_LOCATOR);
        }
        if (!panel.showUINDetailButton()) {
            removeNavButton(SimNavigation.UIN_DETAIL);
        }
        if (!panel.showNonSellableDetailButton()) {
            removeNavButton(SimNavigation.NON_SELLABLE);
        }
    }

    public void stop() {
        RepositoryManager.removeStateObject(SimClientStateKey.QUICK_JUMP_ITEM);
        RepositoryManager.removeStateObject(SimClientStateKey.SELECTED_ITEM);
    }

    /****************************************************************************************************
     * Handle Navigation Events
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.PACK_INFO)) {
                handlePackDetails(event);
            } else if (command.equals(SimNavigation.COMPONENT_INFO)) {
                handleComponentDetails(event);
            } else if (command.equals(SimNavigation.STOCK_LOCATOR)) {
                panel.handleStockLocator();
            } else if (command.equals(SimNavigation.PRICE_INFORMATION)) {
                panel.handlePriceInformation();
            } else if (command.equals(SimNavigation.ADDITIONAL_SUPPLIERS)) {
                handleAdditionalSupplier(event);
            } else if (command.equals(SimNavigation.SUPPLIER_DETAIL)) {
                handleSupplierDetail(event);
            } else if (command.equals(SimNavigation.RELATED_ITEMS)) {
                handleRelatedItem(event);
            } else if (command.equals(SimNavigation.UIN_DETAIL)) {
                panel.handleUINDetail();
            } else if (command.equals(SimNavigation.UDA_DETAIL)) {
                handleUDADetail(event);
            } else if (command.equals(SimNavigation.CUSTOMER_ORDER)) {
                handleCustomerOrder(event);
            } else if (command.equals(SimNavigation.PRINT)) {
                panel.handlePrint();
            } else if (command.equals(SimNavigation.NON_SELLABLE)) {
                panel.handleNonSellable();
            } else if (command.equals(ExtraSimNavigation.BIN_LOOKUP)) {
            	panel.handleBinLookup();
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handlePackDetails(NavigationEvent event) throws Exception {
        if (panel.displayPacksForComponent()) {
            return;
        }
        event.consume();
    }

    private void handleComponentDetails(NavigationEvent event) throws Exception {
        if (panel.displayComponentsForPack()) {
            return;
        }
        event.consume();
    }

    private void handleAdditionalSupplier(NavigationEvent event) throws Exception {
        if (panel.handleAdditionalSupplier()) {
            return;
        }
        event.consume();
    }

    private void handleSupplierDetail(NavigationEvent event) {
        try {
            panel.doDisplaySupplier();
        } catch (Exception exception) {
            displayException(exception);
        }
        event.consume();
    }

    private void handleRelatedItem(NavigationEvent event) {
        if (panel.handleRelatedItem()) {
            return;
        }
        event.consume();
    }

    private void handleUDADetail(NavigationEvent event) throws Exception {
        if (panel.handleUDADetail()) {
            return;
        }
        event.consume();
    }

    private void handleCustomerOrder(NavigationEvent event) throws Exception {
        if (panel.handleCustomerOrder()) {
            return;
        }
        event.consume();
    }
}
