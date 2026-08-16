package extra.retail.sim.client.screen.spareparts;

import java.awt.GridBagLayout;

import javax.swing.JLabel;

import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
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
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.item.ItemVO;

import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;

/********************************************************************************************************
 * This dialog handles entering the filter information for Fulfillment Order
 * List Screen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockRequestReportFilterDialog extends RDialog implements REventListener {
	private static final long serialVersionUID = -4032604723271357715L;

	private StockRequestReportFilterDialogModel model = new StockRequestReportFilterDialogModel();

	private RDateFieldEditor fromDateEditor = new RDateFieldEditor("From Date");

	private RDateFieldEditor toDateEditor = new RDateFieldEditor("To Date");

	private RTextFieldEditor serialNumberEditor = new RTextFieldEditor("SR Number");

	private RComboBoxEditor requestTypeEditor = new RComboBoxEditor("Request Type");

	private RTextFieldEditor technicianIdEditor = new RTextFieldEditor("Technician ID");

	private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(false);

	private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");

	private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

	private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
	private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
	private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

	/****************************************************************************************************
	 * Build Dialog
	 ***************************************************************************************************/
	public StockRequestReportFilterDialog() {
		super(Application.getFrame());
		setStatusBarVisible(false);
		setTitle("Stock Report Filter");
		setSize(450, 460);
		initContent();
		layoutContent();
		centerOnOwner();
	}

	private void initContent() {
		serialNumberEditor.setIdentifier("StockReport.serialNO");
		requestTypeEditor.setIdentifier("StockReport.requestType");
		technicianIdEditor.setIdentifier("StockReport.technicianId");
		statusEditor.setIdentifier("StockReport.status");
		searchLimitEditor.setIdentifier(SimName.ITEM_SEARCH_LIMIT);
		fromDateEditor.setSizeType(EditorConstants.LARGE);
		toDateEditor.setSizeType(EditorConstants.LARGE);
		serialNumberEditor.setSizeType(EditorConstants.LARGE);
		requestTypeEditor.setSizeType(EditorConstants.LARGE);
		requestTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
		technicianIdEditor.setSizeType(EditorConstants.LARGE);
		itemEditor.setSearchListener(buildItemSearchListener());
		statusEditor.setSizeType(EditorConstants.LARGE);
		statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
		serialNumberEditor.setLength(128);
		technicianIdEditor.setLength(128);
		itemEditor.setLength(128);
		searchLimitEditor.setSizeType(EditorConstants.SMALL);
		searchLimitEditor.setRequired(true);
		searchLimitEditor.setMinimumValue(1);
		searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
		searchLimitEditor.setInteger(model.getDefaultSearchLimit());
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
		miscFilterPanel.add(requestTypeEditor);
		miscFilterPanel.add(technicianIdEditor);
		miscFilterPanel.add(itemEditor);
		miscFilterPanel.add(statusEditor);
		miscFilterPanel.add(searchLimitEditor);

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(dateFilterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(miscFilterPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(new JLabel(), GridTool.constraints(0, 2, 2, 1, 0, 1, 0, 3, 0, 0, 0, 0));

		LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

		setContentPane(mainPanel);
	}


	public void setFilter(StockRequestReportQueryFilter filter) throws Exception {
		model.setFilter(filter);
		fromDateEditor.setDate(filter.getFromDate());
		toDateEditor.setDate(filter.getToDate());
		serialNumberEditor.setText(filter.getSerialNo());
		requestTypeEditor.setItems(model.getrequestTypeData().toArray(new String[0]));
		requestTypeEditor.setSelectedItem(filter.getRequestType());
		technicianIdEditor.setText(filter.getTechnicianId());
		if (filter.getItemId() != null) {
			itemEditor.setData(model.loadItem());
		}
		statusEditor.setItems(model.getStatusData().toArray(new String[0]));
		statusEditor.setSelectedItem(filter.getStatus());
		searchLimitEditor.setInteger(filter.getSearchLimit());

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
		requestTypeEditor.clear();
		technicianIdEditor.clear();
		itemEditor.clear();
		statusEditor.clear();
		setFilter(model.resetFilter());
	}

	private void doApply() throws Exception {
		StockRequestReportQueryFilter filter = model.getFilter();
		filter.setSerialNo(serialNumberEditor.getTextOrNull());
		filter.setRequestType((String) requestTypeEditor.getSelectedItem());
		filter.setTechnicianId(technicianIdEditor.getTextOrNull());
		ItemVO item = (ItemVO) itemEditor.getData();
		if (item != null) {
			filter.setItemId(item.getId());
		} else {
			filter.setItemId(null);
		}

		if (item != null) {
			RepositoryManager.addStateObject(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER_ITEM_VO, item);
		} else {
			RepositoryManager.removeStateObject(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER_ITEM_VO);
		}
		filter.setStatus((String) statusEditor.getSelectedItem());
		filter.setSearchLimit(searchLimitEditor.getIntegerValue());
		filter.setDateRange(fromDateEditor.getDateAtStartOfDay(), toDateEditor.getDateAtEndOfDay());
		RepositoryManager.addStateObject(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER, filter);
		notifyREventListeners(new RActionEvent(this, ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER, filter));
		closeWindow();
	}

	private void doCancel() {
		closeWindow();
	}
}
