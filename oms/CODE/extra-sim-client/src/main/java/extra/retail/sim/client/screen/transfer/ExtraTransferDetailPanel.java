package extra.retail.sim.client.screen.transfer;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.displayer.GenericIdDisplayer;
import oracle.retail.sim.client.displayer.TransferStatusDisplayer;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.item.StockItemSearchListener;
import oracle.retail.sim.client.screen.itemprice.PromotionSearchListener;
import oracle.retail.sim.client.screen.scanner.ItemScannerDialog;
import oracle.retail.sim.client.screen.scanner.ItemScannerListener;
import oracle.retail.sim.client.screen.scanner.StockItemScannerDialog;
import oracle.retail.sim.client.screen.store.StoreSearchListener;
import oracle.retail.sim.client.screen.transfer.TransferLineItemWrapper;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RSearchComboEditor;
import oracle.retail.sim.client.swing.editor.RSearchFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.client.uom.StockItemTableEditor;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.item.StockItem;
import oracle.retail.sim.common.itemprice.PromotionVO;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.source.ContextType;
import oracle.retail.sim.common.source.ContextTypeComparator;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.transfer.Transfer;
import oracle.retail.sim.common.transfer.TransferProperty;

/********************************************************************************************************
 * Transfer Detail Panel - This is the superclass of all transfer panels in the
 * PC GUI.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class ExtraTransferDetailPanel extends ScreenPanel implements REventListener, ItemScannerListener {
	private static final long serialVersionUID = -8843524802273274408L;

	RDisplayLabelEditor transferEditor = new RDisplayLabelEditor("Transfer ID");
	RDisplayLabelEditor statusEditor = new RDisplayLabelEditor("Status");
	RSearchComboEditor sendingStoreEditor = SimEditorFactory.createStoreSearchComboEditor("Transfer From");
	RSearchComboEditor receivingStoreEditor = SimEditorFactory.createStoreSearchComboEditor("Transfer To");
	RLongTextFieldEditor requestCommentsEditor = new RLongTextFieldEditor("Request Comments");
	RLongTextFieldEditor transferCommentsEditor = new RLongTextFieldEditor("Transfer Comments");
	RTextFieldEditor awbFieldEditor = new RTextFieldEditor("AWB No#");

	RDisplayLabelEditor requestDateEditor = new RDisplayLabelEditor("Request Date");
	RDisplayLabelEditor approvalDateEditor = new RDisplayLabelEditor("Request Approval");
	RDisplayLabelEditor dispatchDateEditor = new RDisplayLabelEditor("Dispatch Date");
	RDisplayLabelEditor receiptDateEditor = new RDisplayLabelEditor("Receipt Date");

	RDisplayLabelEditor requestUserEditor = new RDisplayLabelEditor("Request User");
	RDisplayLabelEditor approvalUserEditor = new RDisplayLabelEditor("Request Approver");
	RDisplayLabelEditor dispatchUserEditor = new RDisplayLabelEditor("Dispatch User");
	RDisplayLabelEditor receiptUserEditor = new RDisplayLabelEditor("Receiver");

	RDisplayLabelEditor expectedLinesEditor = new RDisplayLabelEditor("Expected Lines");
	RDisplayLabelEditor receivedLinesEditor = new RDisplayLabelEditor("Received Lines");
	RDisplayLabelEditor damagedLinesEditor = new RDisplayLabelEditor("Damaged Lines");
	RDisplayLabelEditor discrepantLinesEditor = new RDisplayLabelEditor("Discrepancies");
	RCheckBoxEditor allowPartialDeliveryEditor = new RCheckBoxEditor("Allow Partial Delivery");
	RCheckBoxEditor customerOrderRelatedEditor = new RCheckBoxEditor("Customer Order Related");

	ItemScannerDialog scannerDialog = null;

	StockItemTableEditor stockItemEditor = new StockItemTableEditor();

	SimTable lineItemTable = new SimTable(new TransferDetailDefinition());
	SimTablePane lineItemPane = new SimTablePane(lineItemTable);

	REditorPanel headerPanel = new REditorPanel(6, 1);
	RComboBoxEditor contextTypeEditor = new RComboBoxEditor("Context Type");
	RSearchFieldEditor contextValueEditor = SimEditorFactory.createPromotionSearchFieldEditor("Context Value");

	static final String REQUEST_COMMENTS_MODIFIED = "RequestComments.modified";
	static final String TRANSFER_COMMENTS_MODIFIED = "TransferComments.modified";
	static final String RECEIVING_STORE_MODIFIED = "ReceivingStore.modified";
	static final String SENDING_STORE_MODIFIED = "SendingStore.modified";
	static final String CONTEXT_TYPE_MODIFIED = "ContextType.modified";
	static final String CONTEXT_VALUE_MODIFIED = "ContextValue.modified";
	static final String TRANSFER_AWB_MODIFIED = "TransferComments.modified";

	/****************************************************************************************************
	 * BUILD PANEL
	 ***************************************************************************************************/

	protected ExtraTransferDetailPanel() {
		initializeScreen();
		layoutScreen();
	}

	private void initializeScreen() {
		awbFieldEditor.setVisible(false);
		transferEditor.setDisplayer(new GenericIdDisplayer());
		sendingStoreEditor.registerAction(this, SENDING_STORE_MODIFIED);
		sendingStoreEditor.setSearchListener(buildSendingStoreListener());
		receivingStoreEditor.registerAction(this, RECEIVING_STORE_MODIFIED);
		receivingStoreEditor.setSearchListener(buildReceivingStoreListener());
		requestCommentsEditor.registerAction(this, REQUEST_COMMENTS_MODIFIED);
		requestCommentsEditor.setIdentifier(SimName.TRANSFER_COMMENT);
		transferCommentsEditor.registerAction(this, TRANSFER_COMMENTS_MODIFIED);
		transferCommentsEditor.setIdentifier(SimName.TRANSFER_COMMENT);
		awbFieldEditor.registerAction(this, TRANSFER_AWB_MODIFIED);
		awbFieldEditor.setIdentifier(SimName.TRANSFER_COMMENT);
		requestDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
		approvalDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
		dispatchDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
		receiptDateEditor.setDataType(DataTypeConstants.DATE_SHORT);
		allowPartialDeliveryEditor.setSizeType(EditorConstants.SMALL);
		customerOrderRelatedEditor.setSizeType(EditorConstants.SMALL);
		allowPartialDeliveryEditor.setEnabled(false, false);
		customerOrderRelatedEditor.setEnabled(false, false);

		stockItemEditor.setSearchListener(buildStockItemSearchListener());
		stockItemEditor.addTableEditorListener(buildStockItemListener());

		if (PermissionManager.hasPermission(PermissionKey.PC_ACCESS_TRANSFER_CONTEXT)) {
			contextTypeEditor.setDisplayer(new TranslatedObjectDisplayer());
			contextTypeEditor.setComparator(ContextTypeComparator.getInstance());
			contextTypeEditor.setEmptyType(RComboBoxEmptyType.ALL);
			contextTypeEditor.registerAction(this, CONTEXT_TYPE_MODIFIED);

			contextValueEditor.registerAction(this, CONTEXT_VALUE_MODIFIED);
			contextValueEditor.setSearchListener(buildPromotionSearchListener());

			setContextInfoAvailable(true);
		} else {
			setContextInfoAvailable(false);
		}
	}

	private void layoutScreen() {
		headerPanel.add(transferEditor);
		headerPanel.add(statusEditor);
		headerPanel.add(sendingStoreEditor);
		headerPanel.add(receivingStoreEditor);

		RPanel commentPanel = new RPanel(new GridBagLayout());
		commentPanel.add(requestCommentsEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
		commentPanel.add(transferCommentsEditor, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
		commentPanel.add(contextTypeEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
		commentPanel.add(contextValueEditor, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

		RPanel checkboxCommentPanel = new RPanel(new GridBagLayout());

		commentPanel.add(allowPartialDeliveryEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
		commentPanel.add(checkboxCommentPanel, GridTool.constraints(1, 2, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

		checkboxCommentPanel.add(customerOrderRelatedEditor, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
		checkboxCommentPanel.add(awbFieldEditor, GridTool.constraints(1, 2, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

		REditorPanel datePanel = new REditorPanel(4, 1);
		datePanel.setTitleBorder("Date");
		datePanel.add(requestDateEditor);
		datePanel.add(approvalDateEditor);
		datePanel.add(dispatchDateEditor);
		datePanel.add(receiptDateEditor);

		REditorPanel userPanel = new REditorPanel(4, 1);
		userPanel.setTitleBorder("User");
		userPanel.add(requestUserEditor);
		userPanel.add(approvalUserEditor);
		userPanel.add(dispatchUserEditor);
		userPanel.add(receiptUserEditor);

		REditorPanel summaryPanel = new REditorPanel(4, 1);
		summaryPanel.setTitleBorder("Summary");
		summaryPanel.add(expectedLinesEditor);
		summaryPanel.add(receivedLinesEditor);
		summaryPanel.add(damagedLinesEditor);
		summaryPanel.add(discrepantLinesEditor);

		RDivider divider = new RDivider(RDivider.HORIZONTAL);

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(datePanel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(userPanel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(summaryPanel, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(commentPanel, GridTool.constraints(0, 1, 4, 1, 0, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(divider, GridTool.constraints(0, 4, 4, 1, 0, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(lineItemPane, GridTool.constraints(0, 5, 4, 1, 1, 1, 0, 3, 0, 0, 0, 0));

		setContentPane(mainPanel);

		LayoutUtility.alignEditorsInGridBag(commentPanel);
	}

	public SimTable getScreenTable() {
		return lineItemTable;
	}

	/****************************************************************************************************
	 * Method to load basic date, user and comment information.
	 ***************************************************************************************************/
	void populateBasicInformation(Transfer transfer) throws Exception {
		transferEditor.setData(transfer.getId());

		statusEditor.setDisplayer(new TransferStatusDisplayer(transfer));
		statusEditor.setData(transfer.getStatus());

		requestDateEditor.setData(transfer.getCreateDate());
		approvalDateEditor.setData(transfer.getApprovalDate());
		dispatchDateEditor.setData(transfer.getShippedDate());
		receiptDateEditor.setData(transfer.getReceiveDate());

		requestUserEditor.setData(transfer.getCreateUser());
		approvalUserEditor.setData(transfer.getApproveUser());
		dispatchUserEditor.setData(transfer.getShippedUser());
		receiptUserEditor.setData(transfer.getReceiveUser());

		requestCommentsEditor.setText(transfer.getRequestComments());
		if (!StringUtility.isNullOrEmpty(transfer.getTransferComments())) {
			String[] tokens = transfer.getTransferComments().split("~");
			if (tokens.length > 0) {
				transferCommentsEditor.setText(tokens[0]);
			}
			if (tokens.length > 1) {
				awbFieldEditor.setText(tokens[1]);
			}
		}
		allowPartialDeliveryEditor.setSelected(transfer.isAllowPartialDelivery());
		customerOrderRelatedEditor.setSelected(transfer.isFulfillmentOrderRelated());
	}

	/****************************************************************************************************
	 * Method to load basic date, user and comment information.
	 ***************************************************************************************************/
	void populateContextType(Transfer transfer, List<ContextType> contextTypes) throws Exception {
		contextTypeEditor.setActionsEnabled(false);
		contextTypeEditor.setItems(contextTypes);
		contextTypeEditor.setSelectedItem(transfer.getContextType());
		contextTypeEditor.setActionsEnabled(true);

		contextValueEditor.setActionsEnabled(false);
		contextValueEditor.setText(transfer.getContextValue());
		contextValueEditor.setActionsEnabled(true);
	}

	/****************************************************************************************************
	 * Sets context fields available or not
	 ***************************************************************************************************/
	private void setContextInfoAvailable(boolean available) {
		contextTypeEditor.setVisible(available);
		contextTypeEditor.setEnabled(available);
		contextValueEditor.setVisible(available);
		contextValueEditor.setEnabled(available);
	}

	/****************************************************************************************************
	 * Scanner Methods
	 ***************************************************************************************************/

	abstract boolean isScannerAvailable();

	abstract boolean isScannerAutoDisplay();

	void launchScanner() {
		if (isScannerAvailable()) {
			if (isScannerAutoDisplay()) {
				displayScanner();
			}
		}
	}

	void displayScanner() {
		if (isScannerAvailable()) {
			if (scannerDialog == null) {
				scannerDialog = new StockItemScannerDialog();
				scannerDialog.setItemProcessor(this);
			}
			scannerDialog.setVisible(true);
		}
	}

	void shutdownScanner() {
		if (scannerDialog != null) {
			scannerDialog.setVisible(false);
			scannerDialog = null;
		}
	}

	TransferLineItemWrapper findExistingWrapper(String itemId) {
		List<TransferLineItemWrapper> wrappers = getAllTableWrappers();
		for (TransferLineItemWrapper wrapper : wrappers) {
			StockItem stockItem = wrapper.getStockItem();
			if (stockItem != null && itemId.equals(stockItem.getId())) {
				return wrapper;
			}
		}
		return null;
	}

	TransferLineItemWrapper findEmptyWrapper() {
		List<TransferLineItemWrapper> wrappers = getAllTableWrappers();
		for (TransferLineItemWrapper wrapper : wrappers) {
			if (wrapper.getStockItem() == null) {
				return wrapper;
			}
		}
		return null;
	}

	private List<TransferLineItemWrapper> getAllTableWrappers() {
		return lineItemTable.getAllRowData();
	}

	/****************************************************************************************************
	 * Method to validate the state of all the editors on the screen.
	 ***************************************************************************************************/
	abstract void validateEditorState();

	/****************************************************************************************************
	 * Search Listeners
	 ***************************************************************************************************/

	private StoreSearchListener buildSendingStoreListener() {
		return new StoreSearchListener() {
			public void assignStore(Store store) {
				if (store != null) {
					sendingStoreEditor.setData(BOFactory.createBuddyStore(store));
				}
			}
		};
	}

	protected abstract StoreSearchListener buildReceivingStoreListener();

	private SimTableEditorListener buildStockItemListener() {
		return new SimTableEditorListener() {
			public void performTableEditorEvent(SimTableEditorEvent event) {
				validateEditorState();
			}
		};
	}

	private StockItemSearchListener buildStockItemSearchListener() {
		return new StockItemSearchListener() {
			public void assignStockItem(StockItem stockItem) {
				if (stockItem != null) {
					stockItemEditor.setData(stockItem);
				}
			}
		};
	}

	private PromotionSearchListener buildPromotionSearchListener() {
		return new PromotionSearchListener() {
			public void assignPromotion(PromotionVO promotionVO) {
				if (promotionVO != null) {
					contextValueEditor.setData(promotionVO);
				}
			}
		};
	}

	/****************************************************************************************************
	 * Transfer Detail Definition - Empty definition as a place-holder for widget
	 * declaration.
	 ***************************************************************************************************/

	private class TransferDetailDefinition extends SimTableDefinition {
		public Class<TransferLineItemWrapper> getDataClass() {
			return TransferLineItemWrapper.class;
		}

		public List<SimTableAttribute> getAttributes() {
			List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>();
			attributes.add(new SimTableAttribute("Item Description", TransferProperty.DESCRIPTION));
			return attributes;
		}
	}
}
