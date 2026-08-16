package oracle.retail.sim.client.swing.table;

import java.awt.BorderLayout;
import java.awt.Color;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RScrollPane;

/********************************************************************************************************
 * This class is a title pane that contains a SIM table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTablePane extends RPanel {
    private static final long serialVersionUID = -4165186702647872268L;

    private RScrollPane scrollPane = new RScrollPane();

    /****************************************************************************************************
     * Creates a new SIM table pane.
     ***************************************************************************************************/
    public SimTablePane() {
        initialize();
    }

    /****************************************************************************************************
     * Creates a new SIM table pane around a SIM table.
     * <p>
     * @param table The table to assign to the pane.
     ***************************************************************************************************/
    public SimTablePane(SimTable table) {
        initialize();
        setTable(table);
    }

    /****************************************************************************************************
     * Initializes the display table pane.
     ***************************************************************************************************/
    private void initialize() {
        // These can be used to set something in the corner of the pane, but are not needed now.
        //scrollPane.setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, new JLabel("A"));
        //scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        scrollPane.setLineBorder();

        setBackground(getBackground());
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Assign the SIM table to the SIM table pane.
     * <p>
     * @param table The SimTable to assign to the pane.
     ***************************************************************************************************/
    public void setTable(SimTable table) {
        scrollPane.setViewportView(table);
    }

    /****************************************************************************************************
     * Retrieves the SIM table assigned to the pane.
     * <p>
     * @return The SIM assigned to the pane, or null if none has been assigned.
     ***************************************************************************************************/
    public SimTable getTable() {
        return (SimTable) scrollPane.getViewport().getView();
    }

    /****************************************************************************************************
     * Assigns the background color of the pane. This color is propagated through the scrollpane.
     * <p>
     * @param color The background color.
     ***************************************************************************************************/
    public void setBackground(Color color) {
        if (color != null) {
            super.setBackground(color);
            if (scrollPane != null) {
                scrollPane.setExtendedBackground(color);
            }
        }
    }
}
