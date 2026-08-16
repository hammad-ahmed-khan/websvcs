package oracle.retail.sim.client.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableModel;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.business.BusinessException;

/********************************************************************************************************
 * This class is the table cell editor used in the Sim Table. It nows how to find the correct editor
 * through the TableEditor interface.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class DisplayerTableCellEditor extends AbstractCellEditor implements TableCellEditor, SimTableEditorListener {
    private static final long serialVersionUID = -8595711324305393487L;

    private SimTableEditor editor;
    private JTable lastTable;
    private SimTableErrorTask errorTask = new TableEditorErrorFocus();
    private int lastRow = -1;
    private int lastCol = -1;
    private int lastUpdateRow = -1;
    private int lastUpdateCol = -1;

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param editor The table editor to use as the table cell editor.
     ***************************************************************************************************/
    public DisplayerTableCellEditor(SimTableEditor editor) {
        this.editor = editor;
    }

    /****************************************************************************************************
     * Returns the height of the editor. The default height is 16 pixels if no preferred height can be
     * determined.
     ***************************************************************************************************/
    public int getEditorHeight() {
        if (editor instanceof JComponent) {
            return (int) ((JComponent) editor).getPreferredSize().getHeight();
        }
        return 16;
    }

    /****************************************************************************************************
     * Assigns a recover task to execute if an error occurs while handling a table editor event. If a
     * null value is passed in, the default recovery task is executed which returns the focus to the task
     * at hand.
     ***************************************************************************************************/
    public void setErrorTask(SimTableErrorTask task) {
        if (task == null) {
            errorTask = new TableEditorErrorFocus();
        } else {
            errorTask = task;
        }
    }

    /****************************************************************************************************
     * Retreives the table editor
     ***************************************************************************************************/
    public SimTableEditor getTableEditor() {
        return editor;
    }

    /****************************************************************************************************
     * Retrieves the table cell editor component. Assigns the last accesses table, row and column.
     * Assigns the correct model and returns the component indicated by the table editor.
     ***************************************************************************************************/
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        lastCol = column;
        lastRow = row;
        lastTable = table;

        TableModel tableModel = table.getModel();

        if (tableModel instanceof SimTableModel) {
            editor.setModel(((SimTableModel) tableModel).getRow(row));
        }

        editor.setValueClass(tableModel.getColumnClass(table.convertColumnIndexToModel(column)));
        editor.removeTableEditorListener(this);
        editor.setCoordinates(row, column);
        editor.setValue(value);

        JComponent component = editor.getComponent();
        if (component != null) {
            component.requestFocusInWindow();
            component.setFont(table.getFont());

            if (component.getBorder() == null) {
                Color color = UIManager.getColor(UIThemeName.TABLE_EDITOR_BORDER_COLOR);
                component.setBorder(BorderFactory.createLineBorder(color));
            }
            if (!component.isEnabled()) {
                component.setBackground(table.getSelectionBackground());
            }
        }
        editor.addTableEditorListener(this);

        return component;
    }

    /****************************************************************************************************
     * Retrieve the cell editor value. Returns an empty object by default.
     ***************************************************************************************************/
    public Object getCellEditorValue() {
        return editor.getValue();
    }

    /****************************************************************************************************
     * Determines if this cell is editable. This will check the click count.
     ***************************************************************************************************/
    public boolean isCellEditable(EventObject event) {
        if (event instanceof MouseEvent) {
            return ((MouseEvent) event).getClickCount() >= 2;
        }
        return true;
    }

    /****************************************************************************************************
     * Returns true if the cell should be selected, false if not.
     ***************************************************************************************************/
    public boolean shouldSelectCell(EventObject event) {
        editor.getComponent().requestFocusInWindow();
        return true;
    }

    /****************************************************************************************************
     * This method gets called whenever something appropriate changes on a type editor. Request focus so
     * that the table and row gets focus, then set the last cell so the exception can use it in case
     * something goes wrong. Attempt to set the value at the cell, catch the exception and handle the
     * exception.
     ***************************************************************************************************/
    public void performTableEditorEvent(SimTableEditorEvent event) {
        try {
            lastTable.requestFocusInWindow();

            lastUpdateCol = lastCol;
            lastUpdateRow = lastRow;

            lastTable.getModel().setValueAt(editor.getValue(), lastRow, lastTable.convertColumnIndexToModel(lastCol));
        } catch (SimTableException exception) {
            if (exception.getCause() instanceof SimTableResetFocusException) {
                lastTable.requestFocusInWindow();
                lastTable.setRowSelectionInterval(lastUpdateRow, lastUpdateRow);
                lastTable.editCellAt(lastUpdateRow, lastUpdateCol);
            } else {
                errorTask.setException(exception);
                SwingUtilities.invokeLater(errorTask);
            }
        }
    }

    /****************************************************************************************************
     * Inner Class - Error task that handles what happens when an invalid editor value is set on a cell.
     ***************************************************************************************************/
    private class TableEditorErrorFocus extends SimTableErrorTask {

        protected void handleException(SimTableException exception) {
            if (exception.getCause() instanceof BusinessException) {
                UIStatusUtility.displayException(this, exception.getCause());
            } else if (exception.getCause() instanceof UIException) {
                UIStatusUtility.displayException(this, exception.getCause());
            } else {
                UIStatusUtility.displayException(this, exception);
            }
            lastTable.requestFocusInWindow();
            lastTable.setRowSelectionInterval(lastUpdateRow, lastUpdateRow);
            lastTable.editCellAt(lastUpdateRow, lastUpdateCol);
        }
    }
}
