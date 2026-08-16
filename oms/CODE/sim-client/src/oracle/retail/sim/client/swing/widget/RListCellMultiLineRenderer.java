package oracle.retail.sim.client.swing.widget;

import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import oracle.retail.sim.client.swing.util.ColorUtility;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayable;

/**************************************************************************************************************
 * This is a generic list cell renderer that can be used inside of an RList or an RComboBox.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 **************************************************************************************************************/

public class RListCellMultiLineRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = -3052438799270020142L;

    /**************************************************************************************************************
     * Creates a new RListCellRenderer.
     **************************************************************************************************************/
    public RListCellMultiLineRenderer() {
        setOpaque(true);
    }

    /**************************************************************************************************************
    * Returns a component that should render a list selection.
    * <p>
    * @param list The JList to be painted.
    * @param value The value to be painted.
    * @param index The index of the cell in the list.
    * @param isSelected True if the specified cell is currently selected.
    * @param cellHasFocus True if the specified cell has the focus.
    * <p>
    * @return A component representing the cell.
    **************************************************************************************************************/
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        RDisplayTextArea textArea = new RDisplayTextArea();

        textArea.setBackground(list.getBackground());
        textArea.setForeground(list.getForeground());

        if (!list.isEnabled()) {
            textArea.setForeground(ColorUtility.disabledTint(textArea.getForeground()));
        } else if (isSelected) {
            textArea.setBackground(list.getSelectionBackground());
            textArea.setForeground(list.getSelectionForeground());
        }

        String displayText = StringConstants.EMPTY;

        if (value != null) {
            if (list instanceof RList) {
                displayText = ((RList) list).getRowDisplayer().getDisplayText(value);
            } else if (value instanceof Displayable) {
                displayText = ((Displayable) value).toDisplayString();
            } else {
                displayText = value.toString();
            }
        }
        textArea.setText(displayText);

        return textArea;
    }
}
