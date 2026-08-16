package extra.retail.sim.client.screen.spareparts;

import java.awt.GridBagLayout;
import java.util.Collection;

import javax.swing.JLabel;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.item.ItemVO;

import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.spareparts.TransferReturnRequestQueryFilter;

/********************************************************************************************************
 * This dialog handles entering the filter information for Transfer Approval
 * List Screen.
 * @author aibrahim
 *******************************************************************************************************/

public class TransferReturnRequestFilterDialog extends RDialog implements REventListener {
	private static final long serialVersionUID = -4032604723271357715L;

	private TranferReturnRequestFilterDialogModel model = new TranferReturnRequestFilterDialogModel();

	private RDateFieldEditor fromDateEditor = new RDateFieldEditor("From Date");

	private RDateFieldEditor toDateEditor = new RDateFieldEditor("To Date");

	private RTextFieldEditor serialNumberEditor = new RTextFieldEditor("SR Number");

	private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);

	private RComboBoxEditor brandNameEditor = new RComboBoxEditor("Brand Name");

	private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
	private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
	private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

	/****************************************************************************************************
	 * Build Dialog
	 ***************************************************************************************************/
	public TransferReturnRequestFilterDialog() {
		super(Application.getFrame());
		setStatusBarVisible(false);
		setTitle("Transfer / Return Request Filter");
		setSize(450, 300);
		initContent();
		layoutContent();
		centerOnOwner();
	}

	private void initContent() {
		serialNumberEditor.setIdentifier("StockReport.serialNO");
		fromDateEditor.setSizeType(EditorConstants.LARGE);
		toDateEditor.setSizeType(EditorConstants.LARGE);
		serialNumberEditor.setSizeType(EditorConstants.LARGE);
		brandNameEditor.setSizeType(EditorConstants.LARGE);
		itemEditor.setSearchListener(buildItemSearchListener());
		brandNameEditor.setEmptyType(RComboBoxEmptyType.ALL);
		serialNumberEditor.setLength(128);
		itemEditor.setLength(128);
		applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
		resetButton.registerAction(this, SimNavigation.DIALOG_RESET);
		cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
	}

	private void layoutContent() {
		addButton(applyButton);
		addButton(resetButton);
		addButton(cancelButton);

		REditorPanel dateFilterPanel = new REditorPanel(3);
		dateFilterPanel.setTitleBorder("Date Filters");
		dateFilterPanel.add(fromDateEditor);
		dateFilterPanel.add(toDateEditor);

		REditorPanel miscFilterPanel = new REditorPanel(12);
		miscFilterPanel.setTitleBorder("Additional Filters");
		miscFilterPanel.add(serialNumberEditor);
		miscFilterPanel.add(itemEditor);
		miscFilterPanel.add(brandNameEditor);

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(dateFilterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(miscFilterPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(new JLabel(), GridTool.constraints(0, 2, 2, 1, 0, 1, 0, 3, 0, 0, 0, 0));

		LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

		setContentPane(mainPanel);
	}


	@SuppressWarnings("unchecked")
	public void setFilter(TransferReturnRequestQueryFilter filter) throws Exception {
		model.setFilter(filter);
		fromDateEditor.setDate(filter.getFromDate());
		toDateEditor.setDate(filter.getToDate());
		serialNumberEditor.setText(filter.getSrNumber());
		if (filter.getItemId() != null) {
			itemEditor.setData(model.loadItem());
		}
		brandNameEditor.setItems((Collection<String>)RepositoryManager.getStateObject(ExtraSimClientStateKey.TRANSFER_RETURN_REQUEST_FILTER_BRANDS));
		brandNameEditor.setSelectedItem(filter.getBrand());
		setDefaultButton(applyButton);
	}

	
	private ItemSearchListener buildItemSearchListener() {
		return new ItemSearchListener() {
			public void assignItem(ItemVO itemVO) {
				if (itemVO != null) {
					itemEditor.setData(itemVO);
				}
			}
		};
	}
	public void performActionEvent(RActionEvent event) {
		String command = event.getEventCommand();
		try {
			if (command.equals(SimNavigation.DIALOG_RESET)) {
				doReset();
			} else if (command.equals(SimNavigation.DIALOG_APPLY)) {
				doApply();
			} else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
				doCancel();
			}
		} catch (Throwable exception) {
			displayException(exception);
		}
	}

	/****************************************************************************************************
	 * Reset Action
	 ***************************************************************************************************/
	private void doReset() throws Exception {
		fromDateEditor.clear();
		toDateEditor.clear();
		serialNumberEditor.clear();
		itemEditor.clear();
		setFilter(model.resetFilter());
	}

	private void doApply() throws Exception {
		TransferReturnRequestQueryFilter filter = model.getFilter();
		filter.setSrNumber(serialNumberEditor.getTextOrNull());
		ItemVO item = (ItemVO) itemEditor.getData();
		if (item != null) {
			filter.setItemId(item.getId());
		} else {
			filter.setItemId(null);
		}

		if (item != null) {
			RepositoryManager.addStateObject(ExtraSimClientStateKey.TRANSFER_REQUEST_RETURN_FILTER_ITEM_VO, item);
		} else {
			RepositoryManager.removeStateObject(ExtraSimClientStateKey.TRANSFER_REQUEST_RETURN_FILTER_ITEM_VO);
		}
		filter.setDateRange(fromDateEditor.getDateAtStartOfDay(), toDateEditor.getDateAtEndOfDay());
		filter.setBrand((String)brandNameEditor.getSelectedItem());
		RepositoryManager.addStateObject(ExtraSimClientStateKey.TRANSFER_RETURN_REQUEST_FILTER, filter);
		notifyREventListeners(new RActionEvent(this, ExtraSimClientStateKey.TRANSFER_RETURN_REQUEST_FILTER, filter));
		closeWindow();
	}

	private void doCancel() {
		closeWindow();
	}
}
