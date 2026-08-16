package extra.retail.sim.client.screen.spareparts;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingConstants;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RDivider;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RComboBoxEmptyType;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.store.Store;

import extra.retail.sim.client.swing.displayer.TitlecaseDisplayer;
import extra.retail.sim.client.swing.tableeditor.StringTableEditor;
import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.spareparts.StockRequestReturnVO;
import extra.retail.sim.common.spareparts.TransferReturnRequestQueryFilter;

/**
 * StockRequestReturnPanel.java
 * aibrahim
 * 2024
 */
public class StockRequestReturnPanel extends ScreenPanel implements REventListener {

	private static final long serialVersionUID = 4506862240485614993L;

	private static final String REQUEST_TYPE_SELECT = "StockRequestReturnPanel.selectType";

	private static final String ITEM_CHECK_SELECT = "StockRequestReturnPanel.selected";

	private static final String FILTER_SELECTED = "StockRequestReturnPanel.filterSelected";

	private SimFilterFieldEditor filterfieldEditor = new SimFilterFieldEditor();

	TransferReturnRequestQueryFilter queryFilter = new TransferReturnRequestQueryFilter();

	TransferReturnRequestFilterDialog filterDialog = new TransferReturnRequestFilterDialog();

	private StockRequestReturnModel model = new StockRequestReturnModel();

	private RComboBoxEditor requestTypeEditor = new RComboBoxEditor("Request Type", true);
	private RComboBoxEditor storeEditor = new RComboBoxEditor("Location", true);
	private RCheckBoxEditor filterItemData = new RCheckBoxEditor("Show Pending Requests With Available Quantity only ");
	private SimTable requestReturnPendingTable = new SimTable(new StockRequestReturnTableDefinition());

	public StockRequestReturnPanel() {
		initializeScreen();
		layoutScreen();
	}

	private void initializeScreen() {
		filterfieldEditor.registerAction(this, FILTER_SELECTED);
		filterDialog.addREventListener(this);
		filterItemData.setSelected(true);
		requestTypeEditor.setDisplayer(new TitlecaseDisplayer());
		requestTypeEditor.setEmptyType(RComboBoxEmptyType.SELECT);
		List<String> typeItems = new ArrayList<>(2);
		typeItems.add("Stock Request");
		typeItems.add("Return Request");
		requestTypeEditor.setItems(typeItems);

		storeEditor.setDisplayer(new StoreDisplayer());
		storeEditor.setEmptyType(RComboBoxEmptyType.SELECT);

		storeEditor.registerAction(this, REQUEST_TYPE_SELECT);
		requestTypeEditor.registerAction(this, REQUEST_TYPE_SELECT);
		filterItemData.registerAction(this, ITEM_CHECK_SELECT);

		requestReturnPendingTable.setMultipleRowSelectionMode();
	}

	@Override
	public void performActionEvent(RActionEvent event) {
		String command = event.getEventCommand();
		if (REQUEST_TYPE_SELECT.equals(command)) {
			String requestType = (String) requestTypeEditor.getSelectedItem();
			Store selectedStore = (Store) storeEditor.getSelectedItem();
			if (!StringUtility.isNullOrEmpty(requestType) && selectedStore != null) {
				try {
					requestReturnPendingTable.setRows(getFilteredItems(model.getPendingApprovalRequest(requestType, selectedStore.getId())));
				} catch (Exception e) {
					LogService.error(this, "Error while fetching the request pending list ", e);
				}
			} else {
				requestReturnPendingTable.clearRows();
			}
		} else if (ITEM_CHECK_SELECT.equals(command)) {
			requestReturnPendingTable.setRows(getFilteredItems(model.getPendingApprovals()));
		} else if (FILTER_SELECTED.equals(command)) {
			try {
				doTransferRequestFilterSelected();
			} catch (Exception e) {
				LogService.error(this, "Error while opening the filter dialog ", e);
			}
		} else if (ExtraSimClientStateKey.TRANSFER_RETURN_REQUEST_FILTER.equals(command)) {
			requestReturnPendingTable.setRows(getFilteredItems(model.getPendingApprovals()));
		}
	}

	@Override
	public void start() throws Exception {
		populateScreen();
	}

	private void populateScreen() throws Exception {
		storeEditor.setItems(model.getUserApprovalLocations());
	}

	@Override
	public SimScreenModel getScreenModel() {
		return this.model;
	}

	@Override
	public SimTable getScreenTable() {
		return this.requestReturnPendingTable;
	}

	private void doTransferRequestFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

	private void layoutScreen() {
		REditorPanel detailPanel = new REditorPanel(1, 4);
		detailPanel.add(requestTypeEditor);
		detailPanel.add(storeEditor);
		detailPanel.add(filterItemData);
		filterItemData.setTitleAlignment(SwingConstants.RIGHT);
		RDivider divider = new RDivider(RDivider.HORIZONTAL);

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(detailPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		mainPanel.add(filterfieldEditor, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 3, 0, 3, 2, 0));
		mainPanel.add(divider, GridTool.constraints(0, 2, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		mainPanel.add(new SimTablePane(requestReturnPendingTable), GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

		setContentPane(mainPanel);
	}

	public void clearScreen() {

		filterItemData.setSelected(false);

	}

	private class StockRequestReturnTableDefinition extends SimTableDefinition {

		@Override
		public Class<StockRequestReturnItemWrapper> getDataClass() {
			return StockRequestReturnItemWrapper.class;
		}

		@Override
		public List<SimTableAttribute> getAttributes() {
			List<SimTableAttribute> attributes = new ArrayList<>(6);
			attributes.add(new SimTableAttribute("Transfer Sequence Number", StockRequestReturnProperty.TSF_SEQUENCE_NO));
			attributes.add(new SimTableAttribute("SR Number", StockRequestReturnProperty.SR_NO));
			attributes.add(new SimTableAttribute("Technician ID", StockRequestReturnProperty.TECHNICIAN_ID));
			attributes.add(new SimTableAttribute("Request Location", StockRequestReturnProperty.REQUEST_LOC));
			attributes.add(new SimTableAttribute("Item", StockRequestReturnProperty.ITEM));
			attributes.add(new SimTableAttribute("Item Description", StockRequestReturnProperty.ITEM_DESC));
			attributes.add(new SimTableAttribute("Brand Name", StockRequestReturnProperty.BRAND_NAME));
			attributes.add(new SimTableAttribute("Qty", StockRequestReturnProperty.QTY));
			attributes.add(new SimTableAttribute("Available Qty", StockRequestReturnProperty.AVAILABLE_QTY));
			attributes.add(new SimTableAttribute("Tech Sub Qty", StockRequestReturnProperty.TECH_SUB_QTY));
			attributes.add(new SimTableAttribute("Bin", StockRequestReturnProperty.BIN));
			attributes.add(new SimTableAttribute("Requested Date", StockRequestReturnProperty.REQUEST_DATE));
			attributes.add(new SimTableAttribute("Comments", StockRequestReturnProperty.SP_COMMENTS, new StringTableEditor()));
			return attributes;
		}
	}

	public void handleApproveRequest() throws Exception {
		List<StockRequestReturnVO> selectedVOs = validateRequestStatus(false);
		if (selectedVOs != null) {
			model.approveRequest(selectedVOs, requestTypeEditor.getSelectedItem().toString());
			refresh();
			UIStatusUtility.displayMessage(this, StockRequestReturnMessage.APPROVE_SUCCESS);
		}
	}

	private void refresh() throws Exception {
		String requestType = (String) requestTypeEditor.getSelectedItem();
		Store selectedStore = (Store) storeEditor.getSelectedItem();
		requestReturnPendingTable.setRows(getFilteredItems(model.getPendingApprovalRequest(requestType, selectedStore.getId())));
	}

	private List<StockRequestReturnVO> validateRequestStatus(boolean reject) {
		if (requestTypeEditor.getSelectedItem() == null) {
			UIStatusUtility.displayException(this, StockRequestReturnMessage.SELECT_APPROVAL_TYPE);
			return null;
		}
		if (storeEditor.getSelectedItem() == null) {
			UIStatusUtility.displayException(this, StockRequestReturnMessage.SELECT_STORE);
			return null;
		}
		if (reject && requestTypeEditor.getSelectedItem().equals("Stock Request")) {
			UIStatusUtility.displayException(this, StockRequestReturnMessage.REQUEST_REJECT_NA);
			return null;
		}
		List<StockRequestReturnVO> selectedApprovalVOs = new ArrayList<>();
		for (StockRequestReturnItemWrapper wrapper : (List<StockRequestReturnItemWrapper>) requestReturnPendingTable.getAllSelectedRowData()) {
			if (selectedApprovalVOs.contains(wrapper.getApprovalVO())) {
				continue;
			}

			boolean hasApproval = false;
			boolean isFilterEnabled = filterItemData.isSelected(); // Checkbox state
			if (isFilterEnabled) {
				if (wrapper.getQty() != null && wrapper.getAvailableQty() != null && wrapper.getQty().intValue() <= wrapper.getAvailableQty().intValue()) {
					hasApproval = true;
				}
			} else {
				hasApproval = true;
			}
			if (hasApproval) {
				selectedApprovalVOs.add(wrapper.getApprovalVO());
			}
		}

		if (selectedApprovalVOs.isEmpty()) {
			UIStatusUtility.displayException(this, StockRequestReturnMessage.SELECT_ITEMS);
			return null;
		}
		return selectedApprovalVOs;
	}

	public void handleRejectRequest() throws Exception {
		List<StockRequestReturnVO> selectedVOs = validateRequestStatus(true);
		if (selectedVOs != null) {
			model.rejectRequest(selectedVOs);
			refresh();
			UIStatusUtility.displayMessage(this, StockRequestReturnMessage.REJECT_SUCCESS);
		}
	}

	private List<StockRequestReturnItemWrapper> getFilteredItems(List<StockRequestReturnItemWrapper> itemWrappers) {
		List<StockRequestReturnItemWrapper> list = new ArrayList<>();
		TransferReturnRequestQueryFilter queryFilter;
		try {
			queryFilter = model.getFilter();
			if (itemWrappers != null) {
				for (StockRequestReturnItemWrapper detail : itemWrappers) {
					if (!(!filterItemData.isSelected() || (detail.getQty() != null && detail.getAvailableQty() != null && detail.getQty().intValue() <= detail.getAvailableQty().intValue()))) {
						continue;
					}
					if (queryFilter.getFromDate() != null && detail.getRequestDate().before(queryFilter.getFromDate())) {
						continue;
					}
					if (queryFilter.getToDate() != null && detail.getRequestDate().after(queryFilter.getToDate())) {
						continue;
					}
					if (queryFilter.getItemId() != null && !queryFilter.getItemId().equals(detail.getItem())) {
						continue;
					}
					if (queryFilter.getSrNumber() != null && !queryFilter.getSrNumber().equals(detail.getSrNo())) {
						continue;
					}
					if (queryFilter.getBrand() != null && !queryFilter.getBrand().equals(detail.getBrandName())) {
						continue;
					}
					list.add(detail);
				}
			}
			filterfieldEditor.setText(model.getDescriptionMap());
			return list;
		} catch (Exception e) {
			LogService.error(this, "Error while applying filter ", e);
			UIStatusUtility.displayException(this, e);
			return itemWrappers;
		}
	}
}
