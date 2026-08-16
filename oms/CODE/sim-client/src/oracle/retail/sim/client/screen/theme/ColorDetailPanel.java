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
 * Color Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ColorDetailPanel extends ScreenPanel {
    private static final long serialVersionUID = -1352036580283314928L;

    private RDisplayLabelEditor themeEditor = new RDisplayLabelEditor("Theme");

    private ColorDetailModel model = new ColorDetailModel();

    private SimTable colorTable = new SimTable(new ColorTableDefinition());
    private SimTablePane colorPane = new SimTablePane(colorTable);

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/

    public ColorDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        colorTable.setTableEditable(false);
        colorTable.setColumnSize("custom", SimTable.LABEL_WIDTH);
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(themeEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(colorPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return colorTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadTheme();
        themeEditor.setData(model.getThemeDescription());
        colorTable.setRows(model.getColors());
    }

    /****************************************************************************************************
     * Reset, Edit, and Save Font
     ***************************************************************************************************/

    public void doResetColor() {
        List<ColorWrapper> wrappers = colorTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (RConfirmUtility.confirm("Color Confirmation", CommonMessageText.THEME_COLOR_RESET_CONFIRM)) {
            for (ColorWrapper wrapper : wrappers) {
                model.resetColor(wrapper);
            }
        }
    }

    public void doEditColor() {
        List<ColorWrapper> wrappers = colorTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        ColorDialog dialog = new ColorDialog(Application.getFrame());
        dialog.setColors(wrappers);
        dialog.setVisible(true);
    }

    public void doSaveColors() throws Exception {
        model.saveColors(colorTable.getAllRowData());
    }

    /****************************************************************************************************
     * Color Table Definition
     ***************************************************************************************************/

    private class ColorTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return ColorWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("Name", "name", new ThemeKeyDisplayer()));
            attributes.add(new SimTableAttribute("Red", "red"));
            attributes.add(new SimTableAttribute("Green", "green"));
            attributes.add(new SimTableAttribute("Blue", "blue"));
            attributes.add(new SimTableAttribute("Customized", "custom", new BooleanDisplayer()));
            return attributes;
        }
    }
}
