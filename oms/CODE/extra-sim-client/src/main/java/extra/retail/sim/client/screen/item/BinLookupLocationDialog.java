package extra.retail.sim.client.screen.item;

import java.awt.GridBagLayout;
import java.util.List;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RListTransferPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.common.item.ItemDetailVO;
import oracle.retail.sim.common.logging.LogService;

/**
 * @author aibrahim 2024
 */
public class BinLookupLocationDialog extends RDialog implements REventListener {

	private static final long serialVersionUID = -8265030765799781000L;

	private BinLookupLocationModel model = new BinLookupLocationModel();

	private RDisplayLabelEditor itemEditor = new RDisplayLabelEditor("Item");
	private RDisplayLabelEditor itemDescEditor = new RDisplayLabelEditor("Item Description");

	private RListTransferPanel locationTransferPanel = new RListTransferPanel();

	private RButton saveButton = new RButton(SimNavigation.SAVE);
	private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

	public BinLookupLocationDialog() {
		super(Application.getFrame());
		setStatusBarVisible(false);
		setTitle("Bin Lookup");
		setSize(550, 420);
		initializeWidgets();
		layoutContent();
		centerWindow();
	}

	private void initializeWidgets() {
		closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);
		saveButton.registerAction(this, SimNavigation.SAVE);

		locationTransferPanel.setTitle("Bin", "Selected Bin");
		locationTransferPanel.setIncludeAllOptions(true);
	}

	private void layoutContent() {
		addButton(saveButton);
		addButton(closeButton);

		RPanel headerPanel = new RPanel(new GridBagLayout());
		headerPanel.add(itemEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 10));
		headerPanel.add(itemDescEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 3, 0, 0, 0));

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(locationTransferPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

		setContentPane(mainPanel);

		LayoutUtility.alignEditorsInGridBag(headerPanel);
	}

	/****************************************************************************************************
	 * Start
	 ***************************************************************************************************/

	public void setItem(ItemDetailVO item) throws Exception {
		model.setItem(item);

		itemEditor.setData(model.getItemId());
		itemDescEditor.setData(model.getItemDescription());
	
		locationTransferPanel.setSelectableItems(model.getStoreBinLocations());
		locationTransferPanel.setSelectedItems(model.getItemBinLocations());
	}

	@Override
	public void performActionEvent(RActionEvent event) {
		String command = event.getEventCommand();
		if (command.equals(SimNavigation.DIALOG_CLOSE)) {
			closeWindow();
		} else if (command.equals(SimNavigation.SAVE)) {
			List<String> binLocations = locationTransferPanel.getSelectedItems();
			if (binLocations.isEmpty()) {
				displayException(ItemLookupMessage.NO_ROW);
				return;
			}
			try {
				model.saveBinLocations(binLocations);
				dispose();
			} catch (Exception e) {
				LogService.error(this, "Error while saving bin locations", e);
				displayException(this, e);
			}
		}
	}
}
