package oracle.retail.sim.client.screen.uin;

import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.ListSelectionModel;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.IntegerWithPrefixDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
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
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.common.uin.UINProblemDetail;

/********************************************************************************************************
 * UIN Resolution List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UINResolutionListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -4494830858385182388L;

    private UINResolutionListModel model = new UINResolutionListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable problemDetailTable = new SimTable(new UINAttributeDefinition());
    private SimTablePane problemDetailPane = new SimTablePane(problemDetailTable);

    private UINResolutionFilterDialog filterDialog = new UINResolutionFilterDialog();
    private RIntegerFieldEditor searchLimitEditor = new RIntegerFieldEditor("Search Limit");

    private static final String UIN_RESOLUTION_FILTER_SELECTED = "UINResolution.filterSelected";
    private static final String UIN_RESOLUTION_SELECTED = "UINResolution.selected";
    private static final String UIN_RESOLUTION_SEARCH_LIMIT_MODIFIED = "UinResolution.searchLimitModified";

    public UINResolutionListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, UIN_RESOLUTION_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        problemDetailTable.setTableEditable(false);
        problemDetailTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        problemDetailTable.registerDoubleClickAction(this, UIN_RESOLUTION_SELECTED);

        searchLimitEditor.registerAction(this, UIN_RESOLUTION_SEARCH_LIMIT_MODIFIED, KeyEvent.VK_ENTER);
        searchLimitEditor.setIdentifier(SimName.UIN_RESOLUTION_SEARCH_LIMIT);
        searchLimitEditor.setSizeType(EditorConstants.SMALL);
        searchLimitEditor.setMinimumValue(1);
        searchLimitEditor.setMaximumValue(SimConfigManager.SEARCH_LIMIT_MAX_VALUE);
    }

    private void layoutPanel() {
        REditorPanel headerPanel = new REditorPanel(1, 2);
        headerPanel.add(filterEditor);
        headerPanel.add(searchLimitEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(problemDetailPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return problemDetailTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Update Status
     ***************************************************************************************************/

    public boolean handleViewHistory() throws Exception {
        List<UINProblemDetail> problemDetails = problemDetailTable.getAllSelectedRowData();
        if (problemDetails.isEmpty()) {
            displayWarning(UINMessageText.UIN_ROW_NOT_SELECTED_ERROR);
            return false;
        }
        if (problemDetails.size() > 1) {
            displayWarning(CommonMessageText.ONE_ROW_MUST_BE_SELECTED);
            return false;
        }
        if (model.storeUINDetail(problemDetails.get(0))) {
            return true;
        }
        displayWarning(UINMessageText.UIN_NO_SELECTION_FOUND);
        return false;
    }

    /****************************************************************************************************
     * Handle Resolve
     ***************************************************************************************************/

    public boolean handleResolve() throws Exception {
        List<UINProblemDetail> problemDetails = problemDetailTable.getAllSelectedRowData();
        List<UINProblemDetail> problemDetailsToResolve = new ArrayList<>();
        if (problemDetails.isEmpty()) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED);
            return false;
        }
        for (UINProblemDetail uinProblemDetail : problemDetails) {
            if (!uinProblemDetail.isResolved()) {
                problemDetailsToResolve.add(uinProblemDetail);
            }
        }
        if (problemDetailsToResolve.isEmpty()) {
            return true;
        }
        if (RConfirmUtility.confirm("Confirmation", UINMessageText.UIN_RESOLUTION_CONFIRM)) {
            model.resolve(problemDetailsToResolve);
            populateScreen();
        }
        return true;
    }

    public void resolve() throws Exception {
        List<UINProblemDetail> problemDetails = problemDetailTable.getAllSelectedRowData();
        model.resolve(problemDetails);
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(UIN_RESOLUTION_FILTER_SELECTED)) {
                doResolutionsFilterSelected();
            } else if (command.equals(UIN_RESOLUTION_SEARCH_LIMIT_MODIFIED)) {
                doSearchLimitModified();
            } else if (command.equals(SimClientStateKey.UIN_ATTRIBUTE_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSearchLimitModified() throws Exception {
        model.getFilter().setSearchLimit(searchLimitEditor.getIntegerValue());
        populateScreen();
    }

    private void doResolutionsFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        problemDetailTable.setRows(model.findUINProblemDetails());
        searchLimitEditor.setInteger(model.getFilter().getSearchLimit());
    }

    /****************************************************************************************************
     * UIN Attribute Table Definition
     ***************************************************************************************************/

    private class UINAttributeDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return UINProblemDetail.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("updateDate"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(11);
            attributes.add(new SimTableAttribute("Item", "itemId"));
            attributes.add(new SimTableAttribute("UIN", "uin", new UINResolutionKeyDisplayer()));
            attributes.add(new SimTableAttribute("Create Date", "updateDate"));
            attributes.add(new SimTableAttribute("Current Status", "oldStatus", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("New Status", "newStatus", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("Action", "action", new TranslatedObjectDisplayer(), null, false));
            attributes.add(new SimTableAttribute("External Transaction ID", "functionalAreaIdentifier"));
            attributes.add(new SimTableAttribute("Quantity", "quantity", new IntegerWithPrefixDisplayer()));
            attributes.add(new SimTableAttribute("Resolved", "resolved", new BooleanDisplayer()));
            return attributes;
        }
    }
}
