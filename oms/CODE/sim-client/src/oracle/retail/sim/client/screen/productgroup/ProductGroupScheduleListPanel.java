package oracle.retail.sim.client.screen.productgroup;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.core.SimScreenName;
import oracle.retail.sim.client.editor.SimFilterFieldEditor;
import oracle.retail.sim.client.swing.dialog.RConfirmDialog;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.schedule.ProductGroupScheduleMessageText;

/********************************************************************************************************
 * Product Group Schedule List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ProductGroupScheduleListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -1251492823389132419L;

    private ProductGroupScheduleListModel model = new ProductGroupScheduleListModel();

    private SimFilterFieldEditor filterEditor = new SimFilterFieldEditor();

    private SimTable scheduleTable = new SimTable(new ProductGroupScheduleDefinition());
    private SimTablePane schedulePane = new SimTablePane(scheduleTable);

    private ProductGroupScheduleFilterDialog filterDialog = new ProductGroupScheduleFilterDialog();

    private static final String SCHEDULE_FILTER_SELECTED = "Schedule.filterSelected";
    private static final String SCHEDULE_SELECTED = "Schedule.selected";

    /****************************************************************************************************
     * Create Panel
     ***************************************************************************************************/

    public ProductGroupScheduleListPanel() {
        initializePanel();
        layoutPanel();
    }

    private void initializePanel() {
        filterEditor.registerAction(this, SCHEDULE_FILTER_SELECTED);

        filterDialog.addREventListener(this);

        scheduleTable.setTableEditable(false);
        scheduleTable.registerDoubleClickAction(this, SCHEDULE_SELECTED);
    }

    private void layoutPanel() {
        RPanel headerPanel = new RPanel(new GridBagLayout());
        headerPanel.add(filterEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(schedulePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return scheduleTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        populateScreen();
    }

    /****************************************************************************************************
     * Handle Delete
     ***************************************************************************************************/

    public void handleDelete() throws Exception {
        if (scheduleTable.getSelectedRowCount() == 0) {
            displayWarning(CommonMessageText.NO_ROWS_SELECTED_DELETE);
            return;
        }

        String title = "Stock Count Schedule Group Delete Confirmation";
        RConfirmDialog dialog = new RConfirmDialog(Application.getFrame(), title);
        dialog.setMessage(ProductGroupScheduleMessageText.DELETE_CONFIRM);
        dialog.setYesNoType();

        if (dialog.getConfirmation()) {
            model.deleteProductGroupSchedules(scheduleTable.getAllSelectedRowData());
        }
        scheduleTable.setRows(model.getScheduleWrappers());
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SCHEDULE_SELECTED)) {
                doScheduleSelected();
            } else if (command.equals(SCHEDULE_FILTER_SELECTED)) {
                doScheduleFilterSelected();
            } else if (command.equals(SimClientStateKey.PRODUCT_GROUP_SCHEDULE_FILTER_MODIFIED)) {
                populateScreen();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doScheduleSelected() throws Exception {
        model.storeProductGroupSchedule((ProductGroupScheduleWrapper) scheduleTable.getSelectedRowData());
        navigate(SimScreenName.PRODUCT_GROUP_SCHEDULE_DETAIL_SCREEN);
    }

    private void doScheduleFilterSelected() throws Exception {
        filterDialog.setFilter(model.getFilter());
        filterDialog.setVisible(true);
    }

    private void populateScreen() throws Exception {
        filterEditor.setText(model.getDescriptionMap());
        scheduleTable.setRows(model.getScheduleWrappers());
    }

    /****************************************************************************************************
     * Product Group Schedule Table Definition
     ***************************************************************************************************/

    private class ProductGroupScheduleDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ProductGroupScheduleWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(7);
            attributes.add(new SimTableAttribute("ID", "scheduleVO.id"));
            attributes.add(new SimTableAttribute("Description", "scheduleVO.description"));
            attributes.add(new SimTableAttribute("Group", "scheduleVO.fullGroupDescription"));
            attributes.add(new SimTableAttribute("Next Date", "nextDate"));
            attributes.add(new SimTableAttribute("Final Date", "lastDate"));
            attributes.add(new SimTableAttribute("Status", "scheduleVO.status", new TranslatedObjectDisplayer(), null));
            attributes.add(new SimTableAttribute("Store", "scheduleVO.storeId", new TranslatedObjectDisplayer()));
            return attributes;
        }
    }
}
