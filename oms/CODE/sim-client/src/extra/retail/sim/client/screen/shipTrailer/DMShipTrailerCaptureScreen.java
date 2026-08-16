/**
 * 
 */
package extra.retail.sim.client.screen.shipTrailer;

import oracle.retail.sim.client.application.NavigationEvent;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/**
 * @author aibrahim
 *
 */
public class DMShipTrailerCaptureScreen extends SimScreen {

	private static final long serialVersionUID = -6747897432825949659L;

	private DMShipTrailerCapturePanel panel = new DMShipTrailerCapturePanel();

	public DMShipTrailerCaptureScreen() {
		add(panel);
	}

	@Override
	public void performNavigationEvent(NavigationEvent event) {
		String command = event.getCommand();
		 try {
			if (SimNavigation.SHIP_TRAILER_DRAFT.equals(command)) {
				handleDraft(event);
			} else if (SimNavigation.SHIP_TRAILER_UPDATE.equals(command)) {
				handleUpdate(event);
			} else if (SimNavigation.SHIP_TRAILER_SAVE.equals(command)) {
				handleSave(event);
			}
		 } catch (Throwable t) {
			displayException(panel, event, t);
		}
	}

	private void handleSave(NavigationEvent event) throws Exception {
		if (panel.updateShipTrailer()) {
			return;
		}
		event.consume();
	}

	private void handleDraft(NavigationEvent event) throws Exception {
		if (panel.draftShipTrailer()) {
			return;
		}
		event.consume();
	}

	private void handleUpdate(NavigationEvent event) throws Exception {
		if (panel.updateShipTrailer()) {
			return;
		}
		event.consume();
	}

	@Override
	public ScreenPanel getScreenPanel() {
		return this.panel;
	}

	@Override
	public String getScreenName() {
		return "DM Ship Trailer";
	}

	@Override
	public void start() throws Throwable {
		super.start();
		panel.start();
		if (panel.isDraft()) {
			removeNavButton(SimNavigation.SHIP_TRAILER_UPDATE);
			removeNavButton(SimNavigation.SHIP_TRAILER_SAVE);
		} else if (panel.isNew()) {
			removeNavButton(SimNavigation.SHIP_TRAILER_DRAFT);
			removeNavButton(SimNavigation.SHIP_TRAILER_UPDATE);
		} else {
			removeNavButton(SimNavigation.SHIP_TRAILER_DRAFT);
			removeNavButton(SimNavigation.SHIP_TRAILER_SAVE);
		}
	}
}
