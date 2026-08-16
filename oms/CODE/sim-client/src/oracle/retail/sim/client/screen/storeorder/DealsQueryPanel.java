package oracle.retail.sim.client.screen.storeorder;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import oracle.retail.sim.client.core.RHeaderPanel;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.displayer.StoreDisplayer;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.common.deals.Deal;

/********************************************************************************************************
 * Deals Query Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DealsQueryPanel extends ScreenPanel {
    private static final long serialVersionUID = 436223740931588721L;

    private DealsQueryModel model = new DealsQueryModel();

    private RDisplayLabelEditor storeEditor = new RDisplayLabelEditor("Store");
    private SimTable dealQueryTable = new SimTable(new DealQueryDefinition());
    private SimTablePane dealQueryPane = new SimTablePane(dealQueryTable);

    public DealsQueryPanel() {
        initializeScreen();
        layoutScreen();
    }

    private void initializeScreen() {
        storeEditor.setDisplayer(new StoreDisplayer());

        dealQueryTable.setTableEditable(false);
        dealQueryTable.setSingleRowSelectionMode();
    }

    private void layoutScreen() {
        RHeaderPanel headerPanel = new RHeaderPanel(1);
        headerPanel.add(storeEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(headerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
        mainPanel.add(dealQueryPane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return model;
    }

    public SimTable getScreenTable() {
        return dealQueryTable;
    }

    public void start() {
        try {
            model.loadStateInformation();
            storeEditor.setData(model.getStore());
            dealQueryTable.setRows(model.findDealsQuery());
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    /****************************************************************************************************
     * Deal Query Table Definition
     ***************************************************************************************************/

    private class DealQueryDefinition extends SimTableDefinition {

        public Class getDataClass() {
            return Deal.class;
        }

        public List getSortAttributes() {
            return Collections.singletonList(new SimTableSortAttribute("id"));
        }

        public List getAttributes() {
            List attributes = new ArrayList<>();
            attributes.add(new SimTableAttribute("Deal ID", "id"));
            attributes.add(new SimTableAttribute("Deal Type", "type"));
            attributes.add(new SimTableAttribute("Active Date", "activeDate"));
            attributes.add(new SimTableAttribute("Close Date", "closeDate"));
            attributes.add(new SimTableAttribute("Deal Class", "dealClass"));
            attributes.add(new SimTableAttribute("Limit Type", "limitType"));
            attributes.add(new SimTableAttribute("Value Type", "valueType"));
            attributes.add(new SimTableAttribute("Lower Limit", "lowerLimit"));
            attributes.add(new SimTableAttribute("Upper Limit", "upperLimit"));
            attributes.add(new SimTableAttribute("Value", "value"));
            return attributes;
        }
    }
}
