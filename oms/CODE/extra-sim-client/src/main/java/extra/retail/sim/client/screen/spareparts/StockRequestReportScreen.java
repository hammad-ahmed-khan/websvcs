package extra.retail.sim.client.screen.spareparts;

import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

import extra.retail.sim.client.util.ExtraSimClientStateKey;

public class StockRequestReportScreen extends SimScreen {

	private static final long serialVersionUID = 1L;

	private StockRequestReportPanel panel = new StockRequestReportPanel();

	public StockRequestReportScreen() {
		add(panel);
	}

	@Override
	public ScreenPanel getScreenPanel() {
		return panel;

	}

	@Override
	public String getScreenName() {
		return "Report Details";
	}

	public void start() throws Exception {
		showMenu();
		panel.start();
	}

	public void resume() throws Exception {
		showMenu();
		if (RepositoryManager.getStateObject(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER) != null) {
			RepositoryManager.removeStateObject(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER);
			panel.start();
		}
	}

	public void stop() {
		RepositoryManager.removeStateObject(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER);
	}
}
