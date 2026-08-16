package oracle.retail.sim.client.swing.entrytable;

import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displaytable.RDisplayTable;
import oracle.retail.sim.client.swing.displaytable.RDisplayTablePane;
import oracle.retail.sim.client.swing.displaytable.SimpleTableRowDisplayer;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RRadioButtonEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * REntryTable Configuration Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class REntryTableDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -6039281630974015345L;

    private REditorPanel viewPanel = new REditorPanel(2);
    private RIntegerFieldEditor pageSizeEditor = new RIntegerFieldEditor("Rows Per Page");

    private REditorPanel columnPanel = new REditorPanel(3);
    private RDisplayLabelEditor columnLabelEditor = new RDisplayLabelEditor("Column Title");
    private RCheckBoxEditor columnHideEditor = new RCheckBoxEditor("Hide Column");
    private RCheckBoxEditor columnSortEditor = new RCheckBoxEditor("Default Sort Column");
    private REditorPanel widthPanel = new REditorPanel(1, 2);
    private RRadioButtonEditor columnWidthEditor = new RRadioButtonEditor();
    private RIntegerFieldEditor columnPixelEditor = new RIntegerFieldEditor();

    private static final String STRETCH = "Stretchable";
    private static final String FIXED_MIN = "Fixed Minimum";
    private static final String FIXED_PIX = "Fixed Pixel";
    private static final String[] BUTTONS = { STRETCH, FIXED_PIX, FIXED_MIN, };

    private static final String SEQUENCE = "#";
    private static final String VISIBLE = "Columns";
    private static final String WIDTH = "Width";
    private static final String HIDDEN = "Hidden";
    private static final String SORT = "Sort";
    private static final String REQUIRED = "Required";

    private String[] headers = { SEQUENCE, VISIBLE, WIDTH, HIDDEN, SORT, REQUIRED };
    private String[] sortHeaders = { SEQUENCE };

    private RDisplayTable columnTable = new RDisplayTable("REntryConfigDialog.columnTable");
    private RDisplayTablePane columnPane = new RDisplayTablePane(columnTable);

    private static final String MOVE_UP = "Move Up";
    private static final String MOVE_DOWN = "Move Down";
    private static final String RESET = "Reset";
    private static final String SAVE = "Save";
    private static final String CANCEL = "Cancel";

    private RButton moveUpButton = new RButton(MOVE_UP);
    private RButton moveDownButton = new RButton(MOVE_DOWN);
    private RButton resetButton = new RButton(RESET);
    private RButton saveButton = new RButton(SAVE);
    private RButton cancelButton = new RButton(CANCEL);

    private List originalColumns = new ArrayList<>();
    private List currentColumns = new ArrayList<>();

    private static final String COLUMN_SELECTION = "Column.selection";
    private static final String COLUMN_MODIFIED = "Column.modified";
    private static final String SORT_MODIFIED = "Column.sortModified";

    private REntryColumn column;

    /****************************************************************************************************
     * Constructor
     * <p>
     * @pram dialog The parent dialog.
     ***************************************************************************************************/
    public REntryTableDialog(JDialog dialog) {
        super(dialog);
        initDialog();
        layoutDialog();
    }

    /****************************************************************************************************
     * Constructor
     * <p>
     * @pram frame The parent frame.
     ***************************************************************************************************/
    public REntryTableDialog(JFrame frame) {
        super(frame);
        initDialog();
        layoutDialog();
    }

    /****************************************************************************************************
     * Initializes the dialog and editor settings.
     ***************************************************************************************************/
    private void initDialog() {
        setTitle("Table Configuration");
        setSize(750, 550);
        setStatusBarVisible(false);
        centerWindow();

        pageSizeEditor.setLength(3);
        pageSizeEditor.setSizeType(EditorConstants.SMALL);

        columnWidthEditor.setRadioTextPosition(EditorConstants.RIGHT);
        columnWidthEditor.setRadioButtons(BUTTONS, 3, 1);

        columnPixelEditor.setLength(3);
        columnPixelEditor.setSizeType(EditorConstants.SMALL);

        columnTable.setTableRowDisplayer(new ColumnTableDisplayer(headers));
        columnTable.setColumnSortOrder(sortHeaders);
        columnTable.registerSingleClickAction(this, COLUMN_SELECTION);
        columnTable.setSelectionMode(RDisplayTable.MULTIPLE_ROWS);

        columnHideEditor.registerAction(this, COLUMN_MODIFIED);
        columnSortEditor.registerAction(this, SORT_MODIFIED);
        columnWidthEditor.registerAction(this, COLUMN_MODIFIED);
        columnPixelEditor.registerAction(this, COLUMN_MODIFIED);

        moveUpButton.registerAction(this, MOVE_UP);
        moveDownButton.registerAction(this, MOVE_DOWN);
        resetButton.registerAction(this, RESET);
        saveButton.registerAction(this, SAVE);
        cancelButton.registerAction(this, CANCEL);

        columnHideEditor.setEnabled(false);
        columnSortEditor.setEnabled(false);
        columnWidthEditor.setEnabled(false);
        columnPixelEditor.setEnabled(false);
        moveUpButton.setEnabled(false);
        moveDownButton.setEnabled(false);
    }

    /****************************************************************************************************
     * Lays out the components within the dialog.
     ***************************************************************************************************/
    private void layoutDialog() {
        addButton(moveUpButton);
        addButton(moveDownButton);
        addButton(resetButton);
        addButton(saveButton);
        addButton(cancelButton);

        viewPanel.setTitleBorder("Table Settings");
        viewPanel.add(pageSizeEditor);

        columnPanel.setTitleBorder("Column Settings");
        columnPanel.add(columnLabelEditor);
        columnPanel.add(columnHideEditor);
        columnPanel.add(columnSortEditor);

        widthPanel.setTitleBorder("Column Width");
        widthPanel.add(columnWidthEditor);
        widthPanel.add(columnPixelEditor);

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(viewPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(columnPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(widthPanel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        mainPanel.add(columnPane, GridTool.constraints(0, 1, 3, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Sets the columns to configure within the dialog. This makes a copy of the columns before editing
     * begins.
     * <p>
     * @param columns An array of columns.
     ***************************************************************************************************/
    protected void setColumns(REntryColumn[] columns) {
        originalColumns = new ArrayList<>();
        currentColumns = new ArrayList<>();

        for (REntryColumn column2 : columns) {
            originalColumns.add(copyObject(column2));
            currentColumns.add(column2);
        }
        currentColumns = resequence(currentColumns);

        columnTable.setRows(currentColumns);
    }

    /****************************************************************************************************
     * Copies an entire column.
     ***************************************************************************************************/
    private REntryColumn copyObject(REntryColumn inColumn) {
        REntryColumn copyColumn = new REntryColumn();
        copyColumn.setTitle(inColumn.getTitle());
        copyColumn.setAttribute(inColumn.getAttribute());
        copyColumn.setRequired(inColumn.isRequired());
        copyColumn.setVisible(inColumn.isVisible());
        copyColumn.setSequence(inColumn.getSequence());
        copyColumn.setPrimarySort(inColumn.isPrimarySort());
        copyColumn.setWidth(inColumn.getWidth());
        return copyColumn;
    }

    /****************************************************************************************************
     * Implements the action listener for the buttons, delegating to the appropriate method.
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(COLUMN_SELECTION)) {
            doColumnSelected();
        } else if (command.equals(COLUMN_MODIFIED)) {
            doApplyColumn();
        } else if (command.equals(SORT_MODIFIED)) {
            doPrimarySortModified();
        } else if (command.equals(MOVE_UP)) {
            doMoveUp();
        } else if (command.equals(MOVE_DOWN)) {
            doMoveDown();
        } else if (command.equals(RESET)) {
            doReset();
        } else if (command.equals(SAVE)) {
            doSave();
        } else if (command.equals(CANCEL)) {
            doCancel();
        }
    }

    /****************************************************************************************************
     * Apply Column Action
     ***************************************************************************************************/
    private void doApplyColumn() {
        if (column != null) {
            column.setVisible(!columnHideEditor.isSelected());

            if (columnWidthEditor.isSelected(STRETCH)) {
                column.setWidth(EditorConstants.COLUMN_STRETCHABLE);
                columnPixelEditor.setEnabled(false);
                columnPixelEditor.clear();
            } else if (columnWidthEditor.isSelected(FIXED_MIN)) {
                column.setWidth(EditorConstants.COLUMN_LABEL_WIDTH);
                columnPixelEditor.setEnabled(false);
                columnPixelEditor.clear();
            } else {
                processFixedWidth();
            }
            columnTable.updateRow(column);
        }
    }

    /****************************************************************************************************
     * Helper method to processing fixed width settings when an column is applied.
     ***************************************************************************************************/
    private void processFixedWidth() {
        try {
            if (columnPixelEditor.getIntegerValue() < 1) {
                columnPixelEditor.setInteger(1);
            }
            column.setWidth(columnPixelEditor.getIntegerValue());
        } catch (UIException exception) {
            columnPixelEditor.setInteger(1);
            column.setWidth(1);
        }
        columnPixelEditor.setEnabled(true);
    }

    /****************************************************************************************************
     * Helper method to update display table properly when primary sort information is modified.
     ***************************************************************************************************/
    private void doPrimarySortModified() {
        if (column != null) {
            if (columnSortEditor.isSelected()) {
                REntryColumn tmpColumn = null;
                for (Iterator iterator = columnTable.getAllData().iterator(); iterator.hasNext();) {
                    tmpColumn = (REntryColumn) iterator.next();
                    tmpColumn.setPrimarySort(false);
                    columnTable.updateRow(tmpColumn);
                }
            }
            column.setPrimarySort(columnSortEditor.isSelected());

            columnTable.updateRow(column);
        }
    }

    /****************************************************************************************************
     * Move Up Action - Moves column up in sequence one place
     ***************************************************************************************************/
    private void doMoveUp() {
        REntryColumn tmpColumn = (REntryColumn) columnTable.getSelectedData();
        if (tmpColumn == null) {
            return;
        }
        int sequence = tmpColumn.getSequence();
        if (sequence == 1) {
            return;
        }
        swapSequence(tmpColumn, columnTable.getAllData(), sequence, sequence - 1);
    }

    /****************************************************************************************************
     * Move Down Action - Moves column down in sequence one place
     ***************************************************************************************************/
    private void doMoveDown() {
        REntryColumn tmpColumn = (REntryColumn) columnTable.getSelectedData();
        if (tmpColumn == null) {
            return;
        }
        List rows = columnTable.getAllData();

        int sequence = tmpColumn.getSequence();
        if (sequence == rows.size()) {
            return;
        }
        swapSequence(tmpColumn, rows, sequence, sequence + 1);
    }

    /****************************************************************************************************
     * Helper method that swaps the sequence of two rows within the column table.
     ***************************************************************************************************/
    private void swapSequence(REntryColumn swapColumn, List rows, int sequence, int findSequence) {
        for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
            REntryColumn tmpColumn = (REntryColumn) iterator.next();

            if (tmpColumn.getSequence() == findSequence) {
                tmpColumn.setSequence(sequence);
            }
        }
        swapColumn.setSequence(findSequence);

        columnTable.setRows(rows);
        columnTable.setRowSelection(swapColumn);
        columnTable.resort();
    }

    /****************************************************************************************************
     * Row Select Action - Updates the editors when a new row is selected in the table.
     ***************************************************************************************************/
    private void doColumnSelected() {
        List columnList = columnTable.getAllSelectedData();

        if (columnList.size() != 1) {
            disableColumnEditors();
            return;
        }
        column = (REntryColumn) columnList.get(0);

        setActionsEnabled(false);

        columnLabelEditor.setData(column.toDisplayString());
        columnHideEditor.setSelected(!column.isVisible());
        columnSortEditor.setSelected(column.isPrimarySort());

        if (column.getWidth() == -1) {
            columnPixelEditor.clear();
            columnWidthEditor.setSelected(STRETCH, true);
        } else if (column.getWidth() == 0) {
            columnPixelEditor.clear();
            columnWidthEditor.setSelected(FIXED_MIN, true);
        } else {
            columnPixelEditor.setInteger(column.getWidth());
            columnWidthEditor.setSelected(FIXED_PIX, true);
        }

        columnHideEditor.setEnabled(!column.isRequired());
        columnSortEditor.setEnabled(true);
        columnWidthEditor.setEnabled(true);
        columnPixelEditor.setEnabled(columnWidthEditor.isSelected(FIXED_PIX));
        moveUpButton.setEnabled(true);
        moveDownButton.setEnabled(true);

        setActionsEnabled(true);
    }

    /****************************************************************************************************
     * Disables the column editors
     ***************************************************************************************************/
    private void disableColumnEditors() {
        column = null;

        setActionsEnabled(false);
        columnLabelEditor.clear();
        columnHideEditor.setSelected(false);
        columnSortEditor.setSelected(false);
        columnWidthEditor.setSelected(STRETCH, true);
        columnPixelEditor.clear();
        columnHideEditor.setEnabled(false);
        columnSortEditor.setEnabled(false);
        columnWidthEditor.setEnabled(false);
        columnPixelEditor.setEnabled(false);
        moveUpButton.setEnabled(false);
        moveDownButton.setEnabled(false);
        setActionsEnabled(true);
    }

    /****************************************************************************************************
     * Resequences all the columns
     ***************************************************************************************************/
    private List resequence(List columns) {
        int i = 1;
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
            ((REntryColumn) iterator.next()).setSequence(i++);
        }
        return columns;
    }

    /****************************************************************************************************
     * Reset Action - Resets the columns back to their original state when the dialog was opened.
     ***************************************************************************************************/
    private void doReset() {
        int i = 0;
        REntryColumn[] array = new REntryColumn[originalColumns.size()];
        for (Iterator iterator = originalColumns.iterator(); iterator.hasNext();) {
            array[i++] = (REntryColumn) iterator.next();
        }
        setColumns(array);
    }

    /****************************************************************************************************
     * Save Action - Save the new configuration settings.
     ***************************************************************************************************/
    private void doSave() {
        REntryColumn[] columns = (REntryColumn[]) columnTable.getAllData().toArray(new REntryColumn[0]);
        notifyREventListeners(new RActionEvent(this, UIPropertyName.ENTRY_TABLE_CONFIG_UPDATE, columns));
        closeWindow();
    }

    /****************************************************************************************************
     * Cancel Action - Closes the window without taking any action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }

    /****************************************************************************************************
     * COLUMN DISPLAYER - Defines the table settings for the internal column table.
     ***************************************************************************************************/
    private class ColumnTableDisplayer extends SimpleTableRowDisplayer {

        public ColumnTableDisplayer(String[] headers) {
            super(headers);
            setDataType(SEQUENCE, DataTypeConstants.INTEGER);
            setColumnSize(SEQUENCE, EditorConstants.COLUMN_LABEL_WIDTH);
            setDataType(HIDDEN, DataTypeConstants.BOOLEAN);
            setColumnSize(HIDDEN, EditorConstants.COLUMN_LABEL_WIDTH);
            setDataType(SORT, DataTypeConstants.BOOLEAN);
            setColumnSize(SORT, EditorConstants.COLUMN_LABEL_WIDTH);
            setDataType(REQUIRED, DataTypeConstants.BOOLEAN);
            setColumnSize(REQUIRED, EditorConstants.COLUMN_LABEL_WIDTH);
        }

        public String[] buildRow(Object object) throws UIException {
            REntryColumn attribute = (REntryColumn) object;
            String[] row = new String[6];
            row[0] = LocaleManager.getIntegerFormatter().format(attribute.getSequence());
            row[1] = attribute.toDisplayString();
            row[2] = LocaleManager.getIntegerFormatter().format(attribute.getWidth());
            row[3] = String.valueOf(!attribute.isVisible());
            row[4] = String.valueOf(attribute.isPrimarySort());
            row[5] = String.valueOf(attribute.isRequired());

            if (attribute.getWidth() == -1) {
                row[2] = Translator.getText("Stretch");
            } else if (attribute.getWidth() == 0) {
                row[2] = Translator.getText("Fixed Minimum");
            }
            return row;
        }
    }
}
