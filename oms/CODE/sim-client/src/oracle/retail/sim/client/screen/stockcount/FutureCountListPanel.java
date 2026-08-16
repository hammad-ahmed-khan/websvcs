package oracle.retail.sim.client.screen.stockcount;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ListSelectionModel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.DateDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.stockcount.FutureCountVO;
import oracle.retail.sim.common.stockcount.StockCountMessageText;

/********************************************************************************************************
 * Future Count List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FutureCountListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -5452501890870712494L;

    private FutureCountListModel model = new FutureCountListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();
    private RDisplayLabelEditor totalItemEditor = new RDisplayLabelEditor("Total Items");
    private SimTable futureCountTable = new SimTable(new FutureCountDefinition());
    private SimTablePane futureCountPane = new SimTablePane(futureCountTable);

    private FutureCountFilterDialog filterDialog = new FutureCountFilterDialog();

    private static final String FUTURE_COUNT_FILTER_SELECTED = "FutureCount.filterSelected";
    private static final String FUTURE_COUNT_SELECTED = "FutureCount.selected";

    /****************************************************************************************************
     * Build Panel
     ***************************************************************************************************/

    public FutureCountListPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        filterEditor.registerAction(this, FUTURE_COUNT_FILTER_SELECTED);

        totalItemEditor.setDataType(DataTypeConstants.INTEGER_LEFT);
        totalItemEditor.setSizeType(EditorConstants.SMALL);

        filterDialog.addREventListener(this);

        futureCountTable.setTableEditable(false);
        futureCountTable.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        futureCountTable.registerDoubleClickAction(this, FUTURE_COUNT_SELECTED);
    }

    private void layoutScreen() {
        RPanel filterPanel = new RPanel(new GridBagLayout());
        filterPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        filterPanel.add(totalItemEditor, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(filterPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(futureCountPane, GridTool.constraints(0, 2, 1, 1, 1, 1, 0, 3, 0, 0, 5, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return futureCountTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public boolean isStartable() {
        try {
            doFutureCountFilterSelected();
        } catch (Exception exception) {
            displayException(exception);
        }
        return model.isFutureCountFilterAvailable();
    }

    public void start() throws Exception {
        doFutureCountFilterModified();
    }

    /****************************************************************************************************
     * Handle Refresh
     ***************************************************************************************************/

    public void handleRefresh() throws Exception {
        doFutureCountFilterModified();
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(FUTURE_COUNT_SELECTED)) {
                doStockCountSelected(event);
            } else if (command.equals(FUTURE_COUNT_FILTER_SELECTED)) {
                doFutureCountFilterSelected();
            } else if (command.equals(SimClientStateKey.FUTURE_COUNT_FILTER_MODIFIED)) {
                doFutureCountFilterModified();
            }
        } catch (Throwable exception) {
            displayException(exception);
        } finally {
            showScreenBusy(false);
        }
    }

    private void doStockCountSelected(RActionEvent event) throws Exception {
        if (RConfirmUtility.confirm("Future Stock Counts", StockCountMessageText.FUTURE_GENERATE_CONFIRM)) {
            showScreenBusy(true);
            try {
                model.generateFutureStockCount((FutureCountVO) futureCountTable.getSelectedRowData());
                navigate(SimScreenName.STOCK_COUNT_LOCATION_SCREEN);
            } finally {
                showScreenBusy(false);
            }
        }
    }

    private void doFutureCountFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void doFutureCountFilterModified() throws Exception {
        List<FutureCountVO> wrappers = model.findFutureStockCounts();
        filterEditor.setText(model.getDescriptionMap());
        totalItemEditor.setData(model.calculateTotalItems(wrappers));
        futureCountTable.setRows(wrappers);
    }

    /****************************************************************************************************
     * Future Count Table Definition
     ***************************************************************************************************/

    private class FutureCountDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FutureCountVO.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("scheduledDate"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(5);
            attributes.add(new SimTableAttribute("Count Description", "countDescription"));
            attributes.add(new SimTableAttribute("Count Group", "groupDescription"));
            attributes.add(new SimTableAttribute("Date", "scheduledDate", new DateDisplayer()));
            attributes.add(new SimTableAttribute("Product Group Type", "type", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Items To Count", "totalItems"));
            return attributes;
        }
    }
}
