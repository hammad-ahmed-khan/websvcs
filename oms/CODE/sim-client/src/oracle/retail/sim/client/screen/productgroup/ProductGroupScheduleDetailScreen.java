package oracle.retail.sim.client.screen.productgroup;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Product Group Schedule Detail Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleDetailScreen extends SimScreen {
    private static final long serialVersionUID = 310747828968638334L;

    private ProductGroupScheduleDetailPanel panel = new ProductGroupScheduleDetailPanel();

    public ProductGroupScheduleDetailScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Product Group Schedule Detail";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() throws Exception {
        showMenu();
        panel.start();
        validateButtons();
    }

    public void stop() {
        panel.stop();
    }

    private void validateButtons() throws Exception {
        if (panel.isNewProductGroupSchedule() || panel.isProductGroupScheduleEditable()) {
            removeNavButton(SimNavigation.BACK);
        } else {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.CANCEL);
        }
    }

    /****************************************************************************************************
     * Handle Navigation Methods
     ***************************************************************************************************/

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        if (command.equals(SimNavigation.SAVE)) {
            handleSave(event);
        }
    }

    private void handleSave(NavigationEvent event) {
        if (panel.handleSave()) {
            return;
        }
        event.consume();
    }
}
