package oracle.retail.sim.client.screen.theme;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.dialog.RConfirmUtility;
import oracle.retail.sim.client.swing.displayer.BooleanDisplayer;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;

/********************************************************************************************************
 * Font Detail Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FontDetailPanel extends ScreenPanel {
    private static final long serialVersionUID = -1352036580283314928L;

    private RDisplayLabelEditor themeEditor = new RDisplayLabelEditor("Theme");

    private FontDetailModel model = new FontDetailModel();

    private SimTable fontTable = new SimTable(new FontTableDefinition());
    private SimTablePane fontPane = new SimTablePane(fontTable);

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/

    public FontDetailPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        fontTable.setTableEditable(false);
        fontTable.setColumnSize("custom", SimTable.LABEL_WIDTH);
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(themeEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(fontPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return fontTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/

    public void start() throws Exception {
        model.loadTheme();
        themeEditor.setData(model.getThemeDescription());
        fontTable.setRows(model.getFonts());
    }

    /****************************************************************************************************
     * Reset, Edit, and Save Font
     ***************************************************************************************************/

    public void doResetFont() {
        List<FontWrapper> wrappers = fontTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        if (RConfirmUtility.confirm("Font Confirmation", CommonMessageText.THEME_FONT_RESET_CONFIRM)) {
            for (FontWrapper wrapper : wrappers) {
                model.resetFont(wrapper);
            }
        }
    }

    public void doEditFont() throws BusinessException {
        List<FontWrapper> wrappers = fontTable.getAllSelectedRowData();
        if (wrappers.isEmpty()) {
            displayError(CommonMessageText.NO_ROWS_SELECTED);
            return;
        }
        FontDialog dialog = new FontDialog(Application.getFrame());
        dialog.setFonts(wrappers);
        dialog.setVisible(true);
    }

    public void doApplyToAll() {
        List<FontWrapper> wrappers = fontTable.getAllSelectedRowData();
        if (wrappers.isEmpty() || wrappers.size() > 1) {
            return;
        }
        if (RConfirmUtility.confirm("Font Confirmation", CommonMessageText.THEME_FONT_APPLY_CONFIRM)) {
            model.applyToAll(wrappers.get(0), fontTable.getAllRowData());
        }
    }

    public void doSaveFonts() throws Exception {
        model.saveFonts(fontTable.getAllRowData());
    }

    /****************************************************************************************************
     * Font Table Definition
     ***************************************************************************************************/

    private class FontTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return FontWrapper.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(5);
            attributes.add(new SimTableAttribute("Name", "name", new ThemeKeyDisplayer()));
            attributes.add(new SimTableAttribute("Font", "family"));
            attributes.add(new SimTableAttribute("Style", "style", new TranslatedObjectDisplayer()));
            attributes.add(new SimTableAttribute("Size", "size"));
            attributes.add(new SimTableAttribute("Customized", "custom", new BooleanDisplayer()));
            return attributes;
        }
    }
}
