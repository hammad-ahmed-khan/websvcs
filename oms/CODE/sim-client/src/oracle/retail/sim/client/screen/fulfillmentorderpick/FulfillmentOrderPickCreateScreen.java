package oracle.retail.sim.client.screen.fulfillmentorderpick;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Fulfillment Order Pick Create Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class FulfillmentOrderPickCreateScreen extends SimScreen {

    private static final long serialVersionUID = -2193697728082078926L;

    private FulfillmentOrderPickCreatePanel panel = new FulfillmentOrderPickCreatePanel();

    public FulfillmentOrderPickCreateScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public String getScreenName() {
        return "Customer Order Pick Create";
    }

    public void start() throws Exception {
        panel.start();
        showMenu();
    }

    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
            if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            }
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

    private void handleSave(NavigationEvent event) throws Exception {
        if (panel.handleSave()) {
            removeFromScreenHistory();
            stop();
            return;
        }
        event.consume();
    }

}
