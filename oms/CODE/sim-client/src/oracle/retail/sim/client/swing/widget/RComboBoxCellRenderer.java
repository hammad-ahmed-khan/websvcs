package oracle.retail.sim.client.swing.widget;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.util.ColorUtility;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.core.type.Displayable;

/********************************************************************************************************
 * This is a generic list cell renderer that can be used inside of an RList or an RComboBox.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RComboBoxCellRenderer extends DefaultListCellRenderer {
    private static final long serialVersionUID = 52102946048177351L;

    private RComboBoxEmptyType emptyType = RComboBoxEmptyType.SELECT;
    private static final Object EMPTY_SELECTION = new Object();

    private BasicDisplayer displayer;

    /****************************************************************************************************
     * Creates a new RListCellRenderer.
     ***************************************************************************************************/
    public RComboBoxCellRenderer() {
        setOpaque(true);
    }

    /****************************************************************************************************
     * Creates a new RListCellRenderer with a basic displayer.
     * <p>
     * @param basicDisplayer A basic displayer with which to display the cell contents.
     ***************************************************************************************************/
    public RComboBoxCellRenderer(BasicDisplayer basicDisplayer) {
        displayer = basicDisplayer;
        setOpaque(true);
    }

    /****************************************************************************************************
     * Assigns the description to display for the NULL selection in the combo box.
     ***************************************************************************************************/
    public void setEmptyType(RComboBoxEmptyType type) {
        if (type == null) {
            emptyType = RComboBoxEmptyType.SELECT;
        } else {
            emptyType = type;
        }
    }

    /****************************************************************************************************
     * Retrieves the empty selection.
     ***************************************************************************************************/
    public Object getEmptySelection() {
        return EMPTY_SELECTION;
    }

    /****************************************************************************************************
     * Returns a component that should render a list selection. This sets the preferred size of the label
     * to a very small width. This is because a label preferredsize is equal to all the text and the
     * editors should NOT use the text length of the data to determine the width to assign to the editor.
     * <p>
     * @param list The JList to be painted.
     * @param value The value to be painted.
     * @param index The index of the cell in the list.
     * @param isSelected True if the specified cell is currently selected.
     * @param cellHasFocus True if the specified cell has the focus.
     *            <p>
     * @return A component representing the cell.
     ***************************************************************************************************/
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (!list.isEnabled()) {
            label.setForeground(ColorUtility.disabledTint(label.getForeground()));
            label.setOpaque(false);
        } else {
            label.setOpaque(true);
        }

        String displayText = StringConstants.SPACE;
        if (value == null || value == EMPTY_SELECTION) {
            displayText = Translator.getText(emptyType.toString());
        } else if (displayer != null) {
            displayText = displayer.getDisplayText(value);
        } else if (value instanceof Displayable) {
            displayText = ((Displayable) value).toDisplayString();
        } else {
            displayText = value.toString();
        }
        label.setText(displayText);
        label.setPreferredSize(new Dimension(5, label.getPreferredSize().height));

        return label;
    }
}
