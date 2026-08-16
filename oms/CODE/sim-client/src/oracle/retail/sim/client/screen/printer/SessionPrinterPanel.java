package oracle.retail.sim.client.screen.printer;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.editor.REditorLabel;
import oracle.retail.sim.client.swing.editor.RPlainEditorLabel;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.tableeditor.SessionPrinterTableEditor;
import oracle.retail.sim.common.report.SessionPrinterProperty;

/********************************************************************************************************
 * Session Printer Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SessionPrinterPanel extends ScreenPanel {
    private static final long serialVersionUID = 1036041336736306982L;

    private SessionPrinterModel model = new SessionPrinterModel();

    private RPlainEditorLabel headerEditor = new RPlainEditorLabel("Please select a printer.");
    private SessionPrinterTableEditor printerTableEditor = new SessionPrinterTableEditor();

    //The model table definition needs to be instantiate after the referenced editors are instantiated.
    private SimTable printerTable = new SimTable(new SessionPrinterTableDefinition());
    private SimTablePane printerPane = new SimTablePane(printerTable);

    public SessionPrinterPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        headerEditor.setHorizontalAlignment(REditorLabel.LEFT);
        printerTable.setTableEditable(true);
    }

    private void layoutScreen() {
        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerEditor, GridTool.constraints(0, 0, 1, 1, 1, 0, 1, 1, 0, 0, 5, 0));
        mainPanel.add(printerPane,  GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 5, 0, 0, 0));
        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return printerTable;
    }

    /****************************************************************************************************
     * Start
     ***************************************************************************************************/
    public void start() throws Exception {
        model.loadSessionPrinters();
        printerTableEditor.setPrinters(model.getPrinters());
        printerTable.setRows(model.getSessionPrinterWrappers());
    }

    /****************************************************************************************************
     * Handle Save
     ***************************************************************************************************/
    public void handleSave() throws Exception {
        model.saveInSessionPrinters();
    }

    /****************************************************************************************************
     * Session Printer Table Definition
     ***************************************************************************************************/
    private class SessionPrinterTableDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return SessionPrinterWrapper.class;
        }

        public List<SimTableSortAttribute> getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute(SessionPrinterProperty.FORMAT_NAME));
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<SimTableAttribute>(2);
            attributes.add(new SimTableAttribute("Format Name", SessionPrinterProperty.FORMAT_NAME, new TranslatedObjectDisplayer(), printerTableEditor));
            attributes.add(new SimTableAttribute("Printer", SessionPrinterProperty.PRINTER, new TranslatedObjectDisplayer(), printerTableEditor));
            return attributes;
        }
    }
}