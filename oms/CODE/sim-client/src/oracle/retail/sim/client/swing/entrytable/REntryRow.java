package oracle.retail.sim.client.swing.entrytable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RLabel;

/********************************************************************************************************
 * REntryRow
 * <p>
 * This class is a single row within the REntryTable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class REntryRow extends RPanel implements KeyListener {
    private static final long serialVersionUID = -4707810028648646544L;

    private REntryTableTraits traits;
    private JComponent[] editorArray = new JComponent[0];
    private Object data;
    private Object[] values;
    private int row = -1;

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param traits A data object containing the table settings.
     ***************************************************************************************************/
    public REntryRow(REntryTableTraits traits) {
        super(new GridBagLayout());
        this.traits = traits;
        addKeyListener(this);
    }

    /****************************************************************************************************
     * Assigns a row number to the row. This will trigger background color validation.
     * <p>
     * @param row The row number.
     ***************************************************************************************************/
    protected void setRowNumber(int row) {
        this.row = row;
        validateColors();
    }

    /****************************************************************************************************
     * Retrieves the row number of the row.
     ***************************************************************************************************/
    protected int getRowNumber() {
        return row;
    }

    /****************************************************************************************************
     * Assigns a data object to the row.
     * <p>
     * @param data The data object.
     ***************************************************************************************************/
    protected void setRowData(Object data) {
        this.data = data;
        loadColumnValues();
    }

    /****************************************************************************************************
     * Updates the data object in the row. This will assign the data, but also redraw the row.
     * <p>
     * @param data The data object.
     ***************************************************************************************************/
    protected void updateRowData(Object data) {
        this.data = data;
        loadColumnValues();
    }

    /****************************************************************************************************
     * Retrieves the data object represented by this row.
     ***************************************************************************************************/
    protected Object getRowData() {
        return data;
    }

    /****************************************************************************************************
     * Assigns all the values to the row. This will display one value in each column building an editor
     * for each cell.
     * <p>
     * @param values An array of values to display within the row.
     ***************************************************************************************************/
    private void loadColumnValues() {
        values = traits.getDataWrapper().getRowValues(data, traits.getVisibleAttributes());
        editorArray = new JComponent[values.length];
        for (int i = 0; i < editorArray.length; i++) {
            editorArray[i] = buildEditor(traits.getColumn(i), values[i]);
        }
        resizeColumns();
    }

    /****************************************************************************************************
     * Builds an editor for a column and value and then returns the editor comoponent.
     ***************************************************************************************************/
    public JComponent buildEditor(REntryColumn column, Object value) {
        if (column.getEditorCreator() == null) {
            RLabel label = new RLabel();
            label.setFont(column.getFont());
            label.setText(value.toString());
            return label;
        }

        REntryTableEditor editor = column.getEditorCreator().createEditor();
        if (column.getIdentifier() != null) {
            editor.setIdentifier(column.getIdentifier());
        }
        editor.setFont(column.getFont());
        editor.setDataWrapper(traits.getDataWrapper());
        editor.setModel(data);
        editor.setAttribute(column.getAttribute());
        editor.setData(value);

        JComponent component = editor.getComponent();
        component.addKeyListener(this);
        return component;
    }

    /****************************************************************************************************
     * Resizes the entire row (which redraws each cell within the row).
     ***************************************************************************************************/
    protected void resizeColumns() {
        for (int i = 0; i < editorArray.length; i++) {
            assignDimension(editorArray[i], traits.getHeaderColumnWidth(i));
        }
        layoutRow();
        validate();
        repaint();
    }

    /****************************************************************************************************
     * Lays out the entire row of editors.
     ***************************************************************************************************/
    private void layoutRow() {
        int gap = traits.getColumnGap();

        removeAll();

        for (int i = 0; i < editorArray.length; i++) {
            if (traits.getHeaderColumnWidth(i) > -1) {
                add(editorArray[i], GridTool.constraints(i, 0, 1, 1, 0, 0, 1, 1, 3, 0, 0, gap));
            } else {
                add(editorArray[i], GridTool.constraints(i, 0, 1, 1, 1, 0, 1, 1, 3, 0, 0, gap));
            }
        }
    }

    /****************************************************************************************************
     * Validates that the colors assign to each editor and the row are correct.
     ***************************************************************************************************/
    private void validateColors() {
        setForeground(traits.getForegroundColor());
        setBackground(traits.getRowBackground(row));

        REntryColumn[] columns = traits.getColumns();

        for (int i = 0; i < editorArray.length; i++) {
            assignColors(editorArray[i], columns[i].getForeground(), columns[i].getBackground());
        }
    }

    /****************************************************************************************************
     * Assigns the correct colors to each individual component in the row.
     ***************************************************************************************************/
    private void assignColors(JComponent component, Color foreground, Color background) {
        if (foreground == null) {
            foreground = traits.getForegroundColor();
        }
        if (background == null) {
            background = traits.getRowBackground(row);
        }
        component.setForeground(foreground);
        component.setBackground(background);
    }

    /****************************************************************************************************
     * Assigns dimension to a component, locking its size into the given height and width.
     * <p>
     * @param component The component to assign dimension to.
     * @param width The width to assign to the component.
     ***************************************************************************************************/
    private void assignDimension(JComponent component, int width) {
        Dimension dimension = new Dimension(width, traits.getRowHeight());

        component.setMinimumSize(dimension);
        component.setPreferredSize(dimension);
        component.setMaximumSize(dimension);
    }

    /****************************************************************************************************
     * Implements the KeyListener method for the row and all the editors in it. If the up, down or tab
     * key is pressed the appropriate action is sent as a propery change event.
     ***************************************************************************************************/
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.isAltDown()) {
            if (keyEvent.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
                String mnemonic = String.valueOf(keyEvent.getKeyChar());
                firePropertyChange(UIPropertyName.ENTRY_TABLE_MNEMONIC_SORT, null, mnemonic);
            }
            return;
        }
        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_UP:
                if (keyEvent.getSource() instanceof JComboBox) {
                    if (((JComboBox) keyEvent.getSource()).isPopupVisible()) {
                        break;
                    }
                }
                firePropertyChange(UIPropertyName.ENTRY_TABLE_UP_PRESSED, false, true);
                keyEvent.consume();
                break;
            case KeyEvent.VK_DOWN:
                if (keyEvent.getSource() instanceof JComboBox) {
                    if (((JComboBox) keyEvent.getSource()).isPopupVisible()) {
                        break;
                    }
                }
                firePropertyChange(UIPropertyName.ENTRY_TABLE_DOWN_PRESSED, false, true);
                keyEvent.consume();
                break;
            case KeyEvent.VK_TAB:
                Boolean shiftDown = keyEvent.isShiftDown();
                firePropertyChange(UIPropertyName.ENTRY_TABLE_TAB_PRESSED, null, shiftDown);
                break;
            default:
                break;
        }
    }

    /****************************************************************************************************
     * The remaining key listener methods. These do not need to be defined.
     ***************************************************************************************************/
    public void keyReleased(KeyEvent event) {
    }

    public void keyTyped(KeyEvent event) {
    }

    /****************************************************************************************************
     * Retrieves the location of the focus within the row. The int[] consists of two values: row index
     * and column index.
     * <p>
     * @return The location of the focus within the row or null if the row does not have focus.
     ***************************************************************************************************/
    protected int[] getFocusedLocation() {
        for (int i = 0; i < editorArray.length; i++) {
            if (editorArray[i].isFocusOwner()) {
                return new int[] { row, i };
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Retrieves whether or not a given column is traversable.
     * <p>
     * @param column The column index.
     * @return True if the component in the column is traversable, false if not.
     ***************************************************************************************************/
    protected boolean isColumnTraversable(int column) {
        validateColumn(column);
        JComponent component = editorArray[column];
        if (!(component.isShowing() && component.isVisible() && component.isDisplayable() && component.isFocusable() && component.isEnabled())) {
            return false;
        }
        return true;
    }

    /****************************************************************************************************
     * The component located at the given column will request the focus.
     * <p>
     * @param column The column index.
     ***************************************************************************************************/
    protected void requestColumnFocus(int column) {
        validateColumn(column);
        editorArray[column].requestFocusInWindow();
    }

    /****************************************************************************************************
     * Sets the row to enabled or disabled state.
     * <p>
     * @param enabled True if the row should be enabled, false if not.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for (JComponent element : editorArray) {
            element.setEnabled(enabled);
        }
    }

    /****************************************************************************************************
     * Sets the component within the row at the specified column to enabled or disabled state.
     * <p>
     * @param column A column index.
     * @param enabled True if the component should be enabled, false if not.
     ***************************************************************************************************/
    protected void setEnabledAt(int column, boolean enabled) {
        validateColumn(column);
        editorArray[column].setEnabled(enabled);
    }

    /****************************************************************************************************
     * Retrieves the enabled state of the component at the specified column.
     * <p>
     * @param row The row number.
     * @param column A column number.
     * @param return True if the component at the location is enabled, false otherwise.
     ***************************************************************************************************/
    protected boolean isEnabledAt(int column) {
        validateColumn(column);
        return editorArray[column].isEnabled();
    }

    /****************************************************************************************************
     * Validate the column index.
     ***************************************************************************************************/
    private void validateColumn(int column) {
        if (column < 0 || column >= editorArray.length) {
            throw new IllegalArgumentException("Invalid column.");
        }
    }
}
