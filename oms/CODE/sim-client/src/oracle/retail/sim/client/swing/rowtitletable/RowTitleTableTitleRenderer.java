package oracle.retail.sim.client.swing.rowtitletable;

import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * The renderer that displays the row title of the table.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RowTitleTableTitleRenderer extends JLabel implements ListCellRenderer {
    private static final long serialVersionUID = 2233496850885136051L;

    /****************************************************************************************************
     * Constructs a new row title renderer.
     ***************************************************************************************************/
    public RowTitleTableTitleRenderer(RowTitleTable parent) {
        setForeground(parent.getTableHeaderForeground());
        setBackground(parent.getTableHeaderBackground());
        setFont(parent.getTableHeaderFont());
        setBorder(parent.getTableHeaderBorder());
        setHorizontalAlignment(CENTER);
        setOpaque(true);
    }

    /****************************************************************************************************
     * Retrieves the component that will renderer the value of the cell.
     ***************************************************************************************************/
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        if (value != null) {
            setText(value.toString());
        } else {
            setText(StringConstants.EMPTY);
        }
        return this;
    }
}
