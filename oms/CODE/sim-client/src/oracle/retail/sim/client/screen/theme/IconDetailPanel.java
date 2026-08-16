package oracle.retail.sim.client.screen.theme;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Icon Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class IconDetailPanel extends ScreenPanel {
    private static final long serialVersionUID = -1352036580283314928L;

    private RDisplayLabelEditor themeEditor = new RDisplayLabelEditor("Theme");

    private IconDetailModel model = new IconDetailModel();

    private SimTable iconTable = new SimTable(new IconTableDefinition());
    private SimTablePane iconPane = new SimTablePane(iconTable);

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/

    public IconDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        iconTable.setTableEditable(false);
        iconTable.setColumnSize("custom", SimTable.LABEL_WIDTH);
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(themeEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(iconPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return iconTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadTheme();
        themeEditor.setData(model.getThemeDescription());
        iconTable.setRows(model.getIcons());
    }

    /****************************************************************************************************
     * Reset, Edit, and Save Font
     ***************************************************************************************************/

    public void doResetIcon() {
        List<IconWrapper> wrappers = iconTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (RConfirmUtility.confirm("Icon Confirmation", CommonMessageText.THEME_ICON_RESET_CONFIRM)) {
            for (IconWrapper wrapper : wrappers) {
                model.resetIcon(wrapper);
            }
        }
    }

    public void doEditIcon() {
        IconWrapper wrapper = (IconWrapper) iconTable.getSelectedRowData();
        if (iconTable.getSelectedRowCount() != 1) {
            displayError(CommonMessageText.NO_SINGLE_ROW_SELECTED);
            return;
        }
        IconDialog dialog = new IconDialog(Application.getFrame());
        dialog.setIcon(wrapper);
        dialog.setVisible(true);
    }

    public void doSaveIcons() throws Exception {
        model.saveIcons(iconTable.getAllRowData());
    }

    /****************************************************************************************************
     * Icon Table Definition
     ***************************************************************************************************/

    private class IconTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return IconWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(3);
            attributes.add(new SimTableAttribute("Name", "key", new ThemeKeyDisplayer()));
            attributes.add(new SimTableAttribute("Icon Path", "iconPath"));
            attributes.add(new SimTableAttribute("Customized", "custom", new BooleanDisplayer()));
            return attributes;
        }
    }
}
