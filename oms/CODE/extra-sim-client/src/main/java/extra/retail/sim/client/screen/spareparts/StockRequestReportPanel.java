package extra.retail.sim.client.screen.spareparts;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;

import extra.retail.sim.client.swing.tableeditor.StringTableEditor;
import extra.retail.sim.client.util.ExtraSimClientStateKey;
import extra.retail.sim.common.spareparts.StockRequestReportQueryFilter;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.LayoutUtility;
import oracle.retail.sim.common.configutil.SimConfigManager;

public class StockRequestReportPanel extends ScreenPanel implements REventListener {

	private static final long serialVersionUID = 1L;

	private static final String REPORT_FILTER_SELECTED = "StockRequestReportPanel.filterSelected";

	private static final String REPORT_SEARCH_LIMIT = "StockRequestReportPanel.searchLimit";

	private StockRequestReportQueryFilter filter = new StockRequestReportQueryFilter();

	private StockRequestReportModel model = new StockRequestReportModel();

	private SimTable stockRequestReportDetails = new SimTable(new StockRequestReportTableDefinition());

	private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

	private SimTablePane stockRequestReportrPane = new SimTablePane(this.stockRequestReportDetails);

	private StockRequestReportFilterDialog filterDialog = new StockRequestReportFilterDialog();

	private SimFilterFieldEditor filterfieldEditor = new SimFilterFieldEditor();

	public StockRequestReportPanel() {
		initializeScreen();
		layoutScreen();
	}

	private void initializeScreen() {

		filterfieldEditor.registerAction(this, REPORT_FILTER_SELECTED);
		filterDialog.addREventListener(this);
		searchLimitEditor.setIdentifier(SimName.ITEM_SEARCH_LIMIT);
		searchLimitEditor.setSizeType(EditorConstants.SMALL);
		searchLimitEditor.setRequired(true);
		searchLimitEditor.setMinimumValue(1);
		searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
		searchLimitEditor.setInteger(model.getDefaultSearchLimit());
		searchLimitEditor.registerAction(this, REPORT_SEARCH_LIMIT, 10);
	}

	private void layoutScreen() {

		REditorPanel rEditorPanel = new REditorPanel(1);
		rEditorPanel.add((Component) this.searchLimitEditor);
		RPanel rPanel1 = new RPanel(new GridBagLayout());
		rPanel1.add((Component) rEditorPanel, GridTool.constraints(0, 3, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
		RPanel rPanel2 = new RPanel(new GridBagLayout());
		rPanel2.add((Component) this.filterfieldEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
		rPanel2.add((Component) rPanel1, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 5, 0, 0));
		RPanel rPanel3 = new RPanel(new GridBagLayout());
		rPanel3.add((Component) rPanel2, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
		rPanel3.add((Component) this.stockRequestReportrPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
		List<REditorPanel> arrayList = new ArrayList<REditorPanel>();
		arrayList.add(rEditorPanel);
		LayoutUtility.alignPanels(arrayList);
		setContentPane(rPanel3);
	}

	public SimScreenModel getScreenModel() {
		return model;
	}

	public SimTable getScreenTable() {
		return this.stockRequestReportDetails;
	}

	@Override
	public void start() throws Exception {
		this.filter = model.getFilter();
		this.filterfieldEditor.setText(this.model.getDescriptionMap());
		this.searchLimitEditor.setInteger(this.filter.getSearchLimit());
		populateScreen();

	}

	private void populateScreen() throws Exception {
		filterfieldEditor.setText(model.getDescriptionMap());
		searchLimitEditor.setInteger(model.getFilter().getSearchLimit());
		stockRequestReportDetails.setRows(model.getRequestReportDetails());
	}

	private void doStockReportFilterSelected() throws Exception {
		filterDialog.setFilter(model.getFilter());
		filterDialog.setVisible(true);
	}

	private void doSearchLimitModified() throws Exception {
		filter = model.getFilter();
		filter.setSearchLimit(searchLimitEditor.getIntegerValue());
		populateScreen();
	}

	public void performActionEvent(RActionEvent event) {
		String command = event.getEventCommand();
		try {
			if (command.equals(REPORT_FILTER_SELECTED)) {
				doStockReportFilterSelected();
			} else if (command.equals(REPORT_SEARCH_LIMIT)) {
				doSearchLimitModified();
			} else if (command.equals(ExtraSimClientStateKey.STOCK_REQUEST_REPORT_FILTER)) {
				populateScreen();
			}
		} catch (Throwable exception) {
			displayException(exception);

		}
	}

	private class StockRequestReportTableDefinition extends SimTableDefinition {
		@Override
		public Class<StockRequestReportWrapper> getDataClass() {
			return StockRequestReportWrapper.class;
		}

		@Override
		public List<SimTableAttribute> getAttributes() {
			List<SimTableAttribute> attributes = new ArrayList<>(6);
			attributes.add(new SimTableAttribute("SR Number", StockRequestReturnProperty.SR_NO));
			attributes.add(new SimTableAttribute("Technician ID", StockRequestReturnProperty.TECHNICIAN_ID));
			attributes.add(new SimTableAttribute("Item", StockRequestReturnProperty.ITEM));
			attributes.add(new SimTableAttribute("Item Description", StockRequestReturnProperty.ITEM_DESC));
			attributes.add(new SimTableAttribute("Brand Name", StockRequestReturnProperty.BRAND_NAME));
			attributes.add(new SimTableAttribute("Qty", StockRequestReturnProperty.QTY));
			attributes.add(new SimTableAttribute("Request Type", StockRequestReturnProperty.REQUEST_TYPE));
			attributes.add(new SimTableAttribute("Status", StockRequestReturnProperty.STATUS));
			attributes.add(new SimTableAttribute("Request Location", StockRequestReturnProperty.REQUEST_LOC));
			attributes.add(new SimTableAttribute("Requested Date", StockRequestReturnProperty.REQUEST_DATE));
			attributes.add(new SimTableAttribute("Apprv/Rej Date", StockRequestReturnProperty.APP_REJ_TIME));
			attributes.add(new SimTableAttribute("Bin", StockRequestReturnProperty.BIN));
			attributes.add(new SimTableAttribute("SP Comments", StockRequestReturnProperty.SP_COMMENTS, new StringTableEditor()));
			return attributes;
		}
	}
}
