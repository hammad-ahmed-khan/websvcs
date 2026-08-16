package extra.retail.sim.client.screen.fulfillmentorder;

import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.JLabel;

import extra.retail.sim.client.core.ExtraSimName;
import extra.retail.sim.common.baselv.BaseLV;
import extra.retail.sim.common.fulfillmentorder.ExtraFulfillmentOrderQueryFilter;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.screen.item.ItemSearchListener;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RNumericIdEditor;
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
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderStatus;
import oracle.retail.sim.common.fulfillmentorder.FulfillmentOrderType;
import oracle.retail.sim.common.item.ItemVO;

/********************************************************************************************************
 * This dialog handles entering the filter information for Fulfillment Order
 * List Screen.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ExtraFulfillmentOrderFilterDialog extends RDialog implements REventListener {
	private static final long serialVersionUID = -4032604723271357715L;

	private ExtraFulfillmentOrderFilterDialogModel model = new ExtraFulfillmentOrderFilterDialogModel();

	private RDateFieldEditor fromReleaseDateEditor = new RDateFieldEditor("Release From Date");
	private RDateFieldEditor toReleaseDateEditor = new RDateFieldEditor("Release To Date");

	private RNumericIdEditor simCustomerOrderEditor = new RNumericIdEditor("SIM Customer Order ID", "SIM Customer Order ID");
	private RTextFieldEditor fulfillmentOrderEditor = new RTextFieldEditor("Fulfillment Order ID");
	private RTextFieldEditor customerOrderEditor = new RTextFieldEditor("Customer Order ID");
	private RTextFieldEditor binEditor = new RTextFieldEditor("BIN ID");

	private RSearchFieldEditor itemEditor = SimEditorFactory.createItemVOSearchFieldEditor(true);
	private RComboBoxEditor statusEditor = new RComboBoxEditor("Status");
	private RComboBoxEditor resvTypeEditor = new RComboBoxEditor("Reservation Type");
	private RTextFieldEditor customerNameEditor = new RTextFieldEditor("Customer Name");
	private RTextFieldEditor trackingEditor = new RTextFieldEditor("Tracking ID");

	private RComboBoxEditor deliveryModeEditor = new RComboBoxEditor("Delivery Mode");
	private RTextFieldEditor deliverySlotEditor = new RTextFieldEditor("Delivery Slot");

	private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
	private RButton resetButton = new RButton(SimNavigation.DIALOG_RESET);
	private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

	/****************************************************************************************************
	 * Build Dialog
	 ***************************************************************************************************/
	public ExtraFulfillmentOrderFilterDialog() {
		super(Application.getFrame());
		setStatusBarVisible(false);
		setTitle("Customer Order Filter");
		setSize(450, 460);
		initContent();
		layoutContent();
		centerOnOwner();
	}

	private void initContent() {
		simCustomerOrderEditor.setIdentifier(SimName.CUSTOMER_ORDER_ID);
		customerOrderEditor.setIdentifier(SimName.CUSTOMER_ORDER_NUMBER);
		fulfillmentOrderEditor.setIdentifier(SimName.CUSTOMER_ORDER_FULFILLMENT_ID);
		binEditor.setIdentifier(SimName.CUSTOMER_ORDER_BIN_ID);
		customerNameEditor.setIdentifier(SimName.CUSTOMER_ORDER_CUSTOMER_NAME);
		trackingEditor.setIdentifier(SimName.CUSTOMER_ORDER_TRACKING_ID);

		statusEditor.setDisplayer(new TranslatedObjectDisplayer());
		resvTypeEditor.setDisplayer(new TranslatedObjectDisplayer());

		fromReleaseDateEditor.setSizeType(EditorConstants.LARGE);
		toReleaseDateEditor.setSizeType(EditorConstants.LARGE);
		simCustomerOrderEditor.setSizeType(EditorConstants.LARGE);
		fulfillmentOrderEditor.setSizeType(EditorConstants.MEDIUM);
		customerOrderEditor.setSizeType(EditorConstants.LARGE);
		customerOrderEditor.setLength(128);
		binEditor.setSizeType(EditorConstants.MEDIUM);
		statusEditor.setSizeType(EditorConstants.LARGE);
		resvTypeEditor.setSizeType(EditorConstants.LARGE);
		customerNameEditor.setSizeType(EditorConstants.LARGE);
		trackingEditor.setSizeType(EditorConstants.LARGE);

		statusEditor.setEmptyType(RComboBoxEmptyType.ALL);
		resvTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
		
		deliveryModeEditor.setDisplayer(new TranslatedObjectDisplayer());
		deliverySlotEditor.setIdentifier(ExtraSimName.CUSTOMER_ORDER_DELIVERY_SLOT);
		
		deliveryModeEditor.setEmptyType(RComboBoxEmptyType.ALL);

		itemEditor.setSearchListener(buildItemSearchListener());

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
		dateFilterPanel.add(fromReleaseDateEditor);
		dateFilterPanel.add(toReleaseDateEditor);

		REditorPanel miscFilterPanel = new REditorPanel(12);
		miscFilterPanel.setTitleBorder("Additional Filters");
		miscFilterPanel.add(simCustomerOrderEditor);
		miscFilterPanel.add(fulfillmentOrderEditor);
		miscFilterPanel.add(customerOrderEditor);
		miscFilterPanel.add(binEditor);
		miscFilterPanel.add(itemEditor);
		miscFilterPanel.add(statusEditor);
		miscFilterPanel.add(resvTypeEditor);
		miscFilterPanel.add(customerNameEditor);
		miscFilterPanel.add(trackingEditor);
		miscFilterPanel.add(deliveryModeEditor);
		miscFilterPanel.add(deliverySlotEditor);

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(dateFilterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(miscFilterPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(new JLabel(), GridTool.constraints(0, 2, 2, 1, 0, 1, 0, 3, 0, 0, 0, 0));

		LayoutUtility.alignPanels(dateFilterPanel, miscFilterPanel);

		setContentPane(mainPanel);
	}

	/****************************************************************************************************
	 * Assign Filter To Dialog
	 ***************************************************************************************************/

	public void setFilter(ExtraFulfillmentOrderQueryFilter filter) throws Exception {
		model.setFilter(filter);

		fromReleaseDateEditor.setDate(filter.getFromDate());
		toReleaseDateEditor.setDate(filter.getToDate());
		statusEditor.setItems(model.findCustomerOrderStatus());
		resvTypeEditor.setItems(model.findOrderTypes());
		if (filter.getFulfillmentOrderId() != null) {
			simCustomerOrderEditor.setLong(filter.getFulfillmentOrderId());
		} else {
			simCustomerOrderEditor.clear();
		}
		customerOrderEditor.setText(filter.getCustomerOrderId());
		fulfillmentOrderEditor.setText(filter.getFulfillmentOrderExternalId());
		binEditor.setText(filter.getBinId());
		customerNameEditor.setText(filter.getCustomerName());
		trackingEditor.setText(filter.getTrackingId());

		if (filter.getItemId() != null) {
			itemEditor.setData(model.loadItem());
		}
		statusEditor.setSelectedItem(filter.getStatus());
		resvTypeEditor.setSelectedItem(filter.getOrderType());

		List<BaseLV> baseLVs = model.getDeliveryModes();
		deliveryModeEditor.setItems(baseLVs);
		deliverySlotEditor.setText(filter.getDeliverySlot());
		deliveryModeEditor.setSelectedItem(BaseLV.findItem(baseLVs, filter.getDeliveryMode()));

		setDefaultButton(applyButton);
	}

	/****************************************************************************************************
	 * Item Search Listener - pops open the item lookup dialog
	 ***************************************************************************************************/

	private ItemSearchListener buildItemSearchListener() {
		return new ItemSearchListener() {
			public void assignItem(ItemVO itemVO) {
				if (itemVO != null) {
					itemEditor.setData(itemVO);
				}
			}
		};
	}

	/****************************************************************************************************
	 * Handle Actions
	 ***************************************************************************************************/
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
		itemEditor.clear();
		setFilter(model.resetFilter());
	}

	/****************************************************************************************************
	 * Apply Action
	 ***************************************************************************************************/
	private void doApply() throws Exception {
		ExtraFulfillmentOrderQueryFilter filter = model.getFilter();

		filter.setBinId(binEditor.getTextOrNull());
		filter.setCustomerName(customerNameEditor.getTextOrNull());
		filter.setCustomerOrderId(customerOrderEditor.getTextOrNull());
		filter.setStatus((FulfillmentOrderStatus) statusEditor.getSelectedItem());
		filter.setOrderType((FulfillmentOrderType) resvTypeEditor.getSelectedItem());
		filter.setDateRange(fromReleaseDateEditor.getDateAtStartOfDay(), toReleaseDateEditor.getDateAtEndOfDay());
		filter.setFulfillmentOrderExternalId(fulfillmentOrderEditor.getTextOrNull());
		filter.setFulfillmentOrderId(simCustomerOrderEditor.getLongOrNull());
		filter.setTrackingId(trackingEditor.getTextOrNull());
		Object baseLV = deliveryModeEditor.getSelectedItem();
		filter.setDeliveryMode(baseLV != null ? ((BaseLV) baseLV).getKey() : null);
		filter.setDeliverySlot( deliverySlotEditor.getTextOrNull());

		ItemVO item = (ItemVO) itemEditor.getData();
		if (item != null) {
			filter.setItemId(item.getId());
		} else {
			filter.setItemId(null);
		}

		if (item != null) {
			RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_FILTER_ITEM_VO, item);
		} else {
			RepositoryManager.removeStateObject(SimClientStateKey.FULFILLMENT_ORDER_FILTER_ITEM_VO);
		}
		RepositoryManager.addStateObject(SimClientStateKey.FULFILLMENT_ORDER_FILTER, filter);
		notifyREventListeners(new RActionEvent(this, SimClientStateKey.FULFILLMENT_ORDER_FILTER_MODIFIED, filter));
		closeWindow();
	}

	/****************************************************************************************************
	 * Cancel Action
	 ***************************************************************************************************/
	private void doCancel() {
		closeWindow();
	}
}
