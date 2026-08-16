package extra.retail.sim.client.screen.imeifulfillmentorderdelivery;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.common.business.CommonMessageText;

public class IMEIFulfillmentOrderDeliveryDetailScreen extends SimScreen {

    private static final long serialVersionUID = 4106385246600686228L;

    private IMEIFulfillmentOrderDeliveryDetailPanel panel = new IMEIFulfillmentOrderDeliveryDetailPanel();

    public IMEIFulfillmentOrderDeliveryDetailScreen() {
        add(panel);
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public String getScreenName() {
        return "IMEI Customer Order Delivery Detail";
    }

    public void start() throws Exception {
        panel.start();
        displayMenu();
    }

    public void resume() throws Exception {
        panel.start();
        displayMenu();
    }

    private void displayMenu() {
        showMenu();

       /* if (panel.isDeliveryClosed() || panel.isViewOnlyMode() || panel.isDeliverySubmitted()) {
            removeNavButton(SimNavigation.SAVE);
            removeNavButton(SimNavigation.DEFAULT_QUANTITIES);
            removeNavButton(SimNavigation.CANCEL);
            removeNavButton(SimNavigation.SCANNER);
            removeNavButton(SimNavigation.REFRESH);
        } else {
            removeNavButton(SimNavigation.BACK);
        }
        if (!panel.isCancelSubmitAllowed()) {
            removeNavButton(SimNavigation.CANCEL_SUBMIT);
        }
        if (!panel.isDispatchAllowed()) {
            removeNavButton(SimNavigation.DISPATCH);
        }
        if (!panel.isSubmitAllowed()) {
            removeNavButton(SimNavigation.SUBMIT);
        }
        if (!panel.isScannerAvailable()) {
            removeNavButton(SimNavigation.SCANNER);
        }
        if (!panel.isWebOrder()) {
            removeNavButton(SimNavigation.BILL_OF_LADING);
        }*/
    }

    public void pause() {
        panel.stop();
    }
    
    public void stop() {
        panel.stop();
    }

    /****************************************************************************************************
     * Navigation Methods
     ***************************************************************************************************/
    public void performNavigationEvent(NavigationEvent event) {
        String command = event.getCommand();
        try {
           /* if (command.equals(SimNavigation.DEFAULT_QUANTITIES)) {
                handleDefaultQuantities(event);
            } else*/ 
        	if (command.equals(SimNavigation.BACK)) {
                handleBack();
            } else if (command.equals(SimNavigation.SAVE)) {
                handleSave(event);
            } else if (command.equals(SimNavigation.CANCEL)) {
               // handleCancel();
            }/* else if (command.equals(SimNavigation.SUBMIT)) {
                handleSubmit(event);
            } else if (command.equals(SimNavigation.CANCEL_SUBMIT)) {
                handleCancelSubmit(event);
            } else if (command.equals(SimNavigation.DISPATCH)) {
                handleDispatch(event);
            } else if (command.equals(SimNavigation.BILL_OF_LADING)) {
                handleBillOfLading(event);
            } else if (command.equals(SimNavigation.REFRESH)) {
                handleRefresh();
            } else if (command.equals(SimNavigation.SCANNER)) {
                handleScanner();
            } else if (command.equals(SimNavigation.NOTES)) {
                handleNotes();
            }*/
        } catch (Throwable exception) {
            displayException(panel, event, exception);
        }
    }

   
    private void handleBack() {
        panel.clearViewOnly();
    }

    private void handleSave(NavigationEvent event) throws Exception {

    	if(panel.handleSave()){
    		return;
    	}
    	event.consume();
    	
    }

    private void handleRefresh() throws Exception {
        panel.handleRefresh();
    }

    private void handleCancel() throws Exception {
        panel.handleCancel();
    }

   

    private void handleCancelSubmit(NavigationEvent event) throws Exception {
        if (panel.handleCancelSubmit()) {
            return;
        }
        event.consume();
    }



    private void handleBillOfLading(NavigationEvent event) throws Exception {
        panel.storeDeliveryForBillOfLading();
        return;
    }


    public void handleNotes() {
        panel.handleNotes();
    }

    /****************************************************************************************************
     * Helper Methods
     ***************************************************************************************************/

    private boolean confirmActivityLock() throws Exception {
        if (panel.confirmActivityLock()) {
            return true;
        }
        displayException(CommonMessageText.LOCK_TAKEN_OVER);
        navigate(SimNavigation.PREVIOUS_SCREEN);
        return false;
    }
}
