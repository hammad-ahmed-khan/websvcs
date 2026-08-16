package extra.retail.sim.client.screen.spareparts;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

import extra.retail.sim.client.util.ExtraSimClientStateKey;

/**
 * StockRequestReturnScreen.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnScreen extends SimScreen {

	private static final long serialVersionUID = -4000320884434339753L;

	private static final String APPROVE = "Approve";
	private static final String REJECT = "Reject";

	private StockRequestReturnPanel panel = new StockRequestReturnPanel();

	public StockRequestReturnScreen() {
		add(panel);
	}

	@Override
	public ScreenPanel getScreenPanel() {
		return panel;
	}

	public void start() throws Exception {
        showMenu();
        panel.start();
    }

	@Override
	public void stop() {
		RepositoryManager.removeStateObject(ExtraSimClientStateKey.TRANSFER_RETURN_REQUEST_FILTER);
		RepositoryManager.removeStateObject(ExtraSimClientStateKey.TRANSFER_RETURN_REQUEST_FILTER_BRANDS);
	}

	@Override
	public void performNavigationEvent(NavigationEvent event) {
		String command = event.getCommand();
		if (command.equals(APPROVE)) {
			try {
				panel.handleApproveRequest();
			} catch (Exception e) {
				
			}
		} else if (command.equals(REJECT)) {
			try {
				panel.handleRejectRequest();
			} catch (Exception e) {
				
			}
		}
	}

	@Override
	public String getScreenName() {
		return "Spare Parts Transfer/Return Approval";
	}
}
