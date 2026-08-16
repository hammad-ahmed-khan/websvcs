package extra.retail.sim.client.screen.spareparts;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;

import extra.retail.sim.common.spareparts.ReturnApprovalDetailVO;

public class ReturnRequestPanel extends ScreenPanel implements REventListener {

	private static final long serialVersionUID = 4506862240485614993L;

	private static final String REQUEST_TABLE_SELECT = "ReturnRequestPanel.selectRow";

	private ReturnRequestModel model = new ReturnRequestModel();

	private RCheckBoxEditor filterItemData = new RCheckBoxEditor("Show Pending Requests With Available Quantity only ");
	private SimTable returnPendingTable = new SimTable(new ReturnApprovalTableDefinition());
	private SimTable itemDetailTable = new SimTable(new ReturnApprovalTableDetailDefinition());

	public ReturnRequestPanel() {
		initializeScreen();
		layoutScreen();
	}

	private void initializeScreen() {
		returnPendingTable.registerSingleClickAction(this, REQUEST_TABLE_SELECT);
		returnPendingTable.setMultipleRowSelectionMode();
	}

	@Override
	public void performActionEvent(RActionEvent event) {
		String command = event.getEventCommand();

		if (REQUEST_TABLE_SELECT.equals(command)) {
			List<ReturnApprovalDetailItemWrapper> list = new ArrayList<>();
			int index = returnPendingTable.getSelectedRow();
			if (index > -1) {
				for (ReturnApprovalDetailVO detailVO : ((ReturnApprovalReportWrapper) returnPendingTable.getAllRowData().get(index)).getApprovalVO().getReturnApprovalDetailVO()) {
					list.add(new ReturnApprovalDetailItemWrapper(detailVO));
				}
				itemDetailTable.setRows(list);
			} else {
				itemDetailTable.clearRows();
			}
		}

	}

	@Override
	public void start() throws Exception {
		populateScreen();
	}

	private void populateScreen() throws Exception {
		returnPendingTable.setRows(model.getPendingApprovalRequest());
	}

	@Override
	public SimScreenModel getScreenModel() {
		return model;

	}

	@Override
	public SimTable getScreenTable() {
		return this.returnPendingTable;
	}

	private void layoutScreen() {

		RPanel mainPanel = new RPanel(new GridBagLayout());
		mainPanel.add(new SimTablePane(returnPendingTable), GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
		mainPanel.add(new SimTablePane(itemDetailTable), GridTool.constraints(0, 3, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

		setContentPane(mainPanel);
	}

	public void clearScreen() {

		filterItemData.setSelected(false);

	}

	private class ReturnApprovalTableDefinition extends SimTableDefinition {

		@Override
		public Class<ReturnApprovalReportWrapper> getDataClass() {
			return ReturnApprovalReportWrapper.class;
		}

		@Override
		public List<SimTableAttribute> getAttributes() {
			List<SimTableAttribute> attributes = new ArrayList<>(6);
			attributes.add(new SimTableAttribute("Return ID", ReturnApprovalReportProperty.ID));
			attributes.add(new SimTableAttribute("Store ID", ReturnApprovalReportProperty.STORE_ID));
			attributes.add(new SimTableAttribute("Source ID", ReturnApprovalReportProperty.SOURCE_ID));
			attributes.add(new SimTableAttribute("Supplier Name", ReturnApprovalReportProperty.SUPPLIER_NAME));
			attributes.add(new SimTableAttribute("Create User", ReturnApprovalReportProperty.CREATE_USER));
			attributes.add(new SimTableAttribute("Create Date", ReturnApprovalReportProperty.CREATE_DATE));
			;
			return attributes;
		}
	}

	private class ReturnApprovalTableDetailDefinition extends SimTableDefinition {

		@Override
		public Class<ReturnApprovalDetailItemWrapper> getDataClass() {
			return ReturnApprovalDetailItemWrapper.class;
		}

		@Override
		public List<SimTableAttribute> getAttributes() {
			List<SimTableAttribute> attributes = new ArrayList<>(6);
			attributes.add(new SimTableAttribute("Return ID", ReturnApprovalReportProperty.ID));
			attributes.add(new SimTableAttribute("Item ID", ReturnApprovalReportProperty.ITEM_ID));
			attributes.add(new SimTableAttribute("Item Description", ReturnApprovalReportProperty.ITEM_DESC));
			attributes.add(new SimTableAttribute("Quantity", ReturnApprovalReportProperty.QUANTITY));
			attributes.add(new SimTableAttribute("Brand Name", ReturnApprovalReportProperty.BRAND_NAME));

			return attributes;
		}
	}

	public void handleApproveRequest() throws Exception {
		List<Long> returnId = validateRequestStatus(false);
		if (returnId != null) {
			model.approveRequest(returnId);
			clearSelectedRows();
			UIStatusUtility.displayMessage(this, StockRequestReturnMessage.APPROVE_SUCCESS);
		}
	}

	private List<Long> validateRequestStatus(boolean reject) {
		List<Long> selectedApprovalVOs = new ArrayList<>();
		for (ReturnApprovalReportWrapper wrapper : (List<ReturnApprovalReportWrapper>) returnPendingTable.getAllSelectedRowData()) {
			selectedApprovalVOs.add(wrapper.getReturnId());
		}

		if (selectedApprovalVOs.isEmpty()) {
			UIStatusUtility.displayException(this, StockRequestReturnMessage.SELECT_ITEMS);
			return null;
		}
		return selectedApprovalVOs;
	}

	private void clearSelectedRows() {
		for (Object obj : returnPendingTable.getAllSelectedRowData()) {
			returnPendingTable.removeRow(obj);
		}
		itemDetailTable.clearRows();
	}

	public void handleRejectRequest() throws Exception {
		List<Long> returnId = validateRequestStatus(false);
		if (returnId != null) {
			model.rejectRequest(returnId);
			clearSelectedRows();
			UIStatusUtility.displayMessage(this, StockRequestReturnMessage.REJECT_SUCCESS);
		}
	}
}
