package extra.retail.sim.client.screen.spareparts;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

public class ReturnRequestScreen extends SimScreen {

	private static final long serialVersionUID = -6244513932787855775L;
	private static final String APPROVE = "Approve";
	private static final String REJECT = "Reject";

	private ReturnRequestPanel panel = new ReturnRequestPanel();

	public ReturnRequestScreen() {
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
		return "Return Approval";
	}
}
