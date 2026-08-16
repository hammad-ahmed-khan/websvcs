package oracle.retail.sim.client.screen.theme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableCheckBoxRenderer;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Theme List Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ThemeListPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = -1352036580283314928L;

    private ThemeListModel model = new ThemeListModel();

    private SimTable themeTable = new SimTable(new ThemeTableDefinition());
    private SimTablePane themePane = new SimTablePane(themeTable);

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/

    public ThemeListPanel() {
        themeTable.setSingleRowSelectionMode();
        themeTable.setTableEditable(false);
        themeTable.registerDoubleClickAction(this, SimClientStateKey.THEME_SELECTED);

        setContentPane(themePane);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return themeTable;
    }

    public void start() throws Exception {
        refreshTable();
    }

    /****************************************************************************************************
     * Create, Edit & Store Theme
     ***************************************************************************************************/

    public void doCreateTheme() throws Exception {
        CustomThemeDialog dialog = new CustomThemeDialog(Application.getFrame());
        dialog.setThemes(themeTable.getAllRowData());
        dialog.addREventListener(this);
        dialog.setVisible(true);
        refreshTable();
    }

    public void doStoreTheme() throws Exception {
        CustomThemeWrapper theme = (CustomThemeWrapper) themeTable.getSelectedRowData();
        if (theme == null) {
            throw new BusinessException(CommonMessageText.THEME_NONE_SELECTED);
        }
        model.storeTheme(theme);
    }

    /****************************************************************************************************
     * Screen Actions
     ***************************************************************************************************/

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimClientStateKey.THEME_SELECTED)) {
                doEditTheme();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doEditTheme() throws Exception {
        CustomThemeWrapper theme = (CustomThemeWrapper) themeTable.getSelectedRowData();
        if (theme != null) {
            CustomThemeDialog dialog = new CustomThemeDialog(Application.getFrame());
            dialog.setThemes(themeTable.getAllRowData());
            dialog.setCustomTheme(theme);
            dialog.addREventListener(this);
            dialog.setVisible(true);
            refreshTable();
        }
    }

    private void refreshTable() throws Exception {
        themeTable.setRows(model.findThemes());
    }

    /****************************************************************************************************
     * Table Definition
     ***************************************************************************************************/

    private class ThemeTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return CustomThemeWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("description"));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Description", "description"));
            attributes.add(new SimTableAttribute("Look & Feel", "lookAndFeelName"));
            attributes.add(new SimTableAttribute("Active", "active", new SimTableCheckBoxRenderer()));
            return attributes;
        }
    }
}
