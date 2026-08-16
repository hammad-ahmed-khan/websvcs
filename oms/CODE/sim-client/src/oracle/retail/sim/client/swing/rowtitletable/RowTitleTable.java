package oracle.retail.sim.client.swing.rowtitletable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import oracle.retail.sim.client.swing.displaytable.RDisplayTableModel;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.CustomSwanLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.common.core.locale.StringConstants;

/****************************************************************************************************
 * This class represents a table with both row and column headers.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ****************************************************************************************************/

public class RowTitleTable extends RPanel implements MouseListener, FocusListener {
    private static final long serialVersionUID = -3603570084061063422L;

    public static final String CELL_ACTION = "RowTitleTable.cellAction";
    public static final String CELL_ACTIVATED = "RowTitleTable.cellActivated";
    public static final String CELL_DEACTIVATED = "RowTitleTable.cellDeactivated";

    private JTable table = new JTable();
    private RScrollPane scrollView = new RScrollPane(table);

    private JList rowTitleList;
    private RowTitleTableModel headerModel = new RowTitleTableModel();

    private Font tableHeaderFont = CustomSwanLookAndFeel.getControlTextFont();

    private Color tableHeaderBackground = Color.WHITE;
    private Color tableHeaderForeground = Color.BLACK;
    private Color cellFocusBackground = Color.BLUE;
    private Color cellDefaultBackground = Color.WHITE;
    private Color cellFocusForeground = Color.WHITE;
    private Color cellDefaultForeground = Color.BLACK;

    private Border tableHeaderBorder = BorderFactory.createBevelBorder(BevelBorder.RAISED);
    private Border cellBorder = BorderFactory.createBevelBorder(1, Color.BLACK, Color.GRAY);

    private int cellHeight = 40;
    private int headerPad = 20;
    private int selectedRow = -1;
    private int selectedCol = -1;

    /****************************************************************************************************
     * Constructs a new RowTitleTable.
     ****************************************************************************************************/
    public RowTitleTable() {
        scrollView.setExtendedBackground(CustomSwanLookAndFeel.getSwanBaseBackgroundColor(), true);

        initializeColors();

        table.setShowGrid(true);
        table.setRowHeight(cellHeight);
        table.addMouseListener(this);
        table.addFocusListener(this);

        setLayout(new BorderLayout());
        add(scrollView, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Retrieves colors from the UIManager and assigns them to the table.
     ****************************************************************************************************/
    private void initializeColors() {
        setTableHeaderBackground(UIManager.getColor(UIThemeName.ROWTITLETABLE_HEADER_BACKGROUND));
        setTableHeaderForeground(UIManager.getColor(UIThemeName.ROWTITLETABLE_HEADER_FOREGROUND));
        setTableHeaderFont(UIManager.getFont(UIThemeName.ROWTITLETABLEE_HEADER_FONT));
        setCellDefaultBackground(UIManager.getColor(UIThemeName.ROWTITLETABLE_CELL_BACKGROUND));
        setCellDefaultForeground(UIManager.getColor(UIThemeName.ROWTITLETABLE_CELL_FOREGROUND));
        setCellFocusBackground(UIManager.getColor(UIThemeName.ROWTITLETABLE_CELL_FOCUS_BACKGROUND));
        setCellFocusForeground(UIManager.getColor(UIThemeName.ROWTITLETABLE_CELL_FOCUS_FOREGROUND));
    }

    /******************************************************************************************
     * Assigns the header area background color.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setTableHeaderBackground(Color color) {
        if (color != null) {
            tableHeaderBackground = color;
        }
    }

    /******************************************************************************************
     * Retrieves the header area background color.
     * <p>
     * @return The header area background color.
     *****************************************************************************************/
    public Color getTableHeaderBackground() {
        return tableHeaderBackground;
    }

    /******************************************************************************************
     * Assigns the header area foreground color.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setTableHeaderForeground(Color color) {
        if (color != null) {
            tableHeaderForeground = color;
        }
    }

    /******************************************************************************************
     * Retrieves the header area foreground color.
     * <p>
     * @return The header area foreground color.
     *****************************************************************************************/
    public Color getTableHeaderForeground() {
        return tableHeaderForeground;
    }

    /******************************************************************************************
     * Assigns the header area font.
     * <p>
     * @param font The font to assign
     *****************************************************************************************/
    public void setTableHeaderFont(Font font) {
        if (font != null) {
            tableHeaderFont = font;
        }
    }

    /******************************************************************************************
     * Retrieves the header area font.
     * <p>
     * @return The header area font.
     *****************************************************************************************/
    public Font getTableHeaderFont() {
        return tableHeaderFont;
    }

    /******************************************************************************************
     * Assigns the table header border to use to render the table headers.
     * <p>
     * @param border The border to assign
     *****************************************************************************************/
    public void setTableHeaderBorder(Border border) {
        tableHeaderBorder = border;
    }

    /******************************************************************************************
     * Retrieves the table header border to use to render the table headers.
     * <p>
     * @return The border to use to render table headers.
     *****************************************************************************************/
    public Border getTableHeaderBorder() {
        return tableHeaderBorder;
    }

    /****************************************************************************************************
     * Assign a border to display for a cell that contains focus.
     * <p>
     * @param border The border to assign.
     ****************************************************************************************************/
    public void setCellFocusBorder(Border border) {
        cellBorder = border;
    }

    /****************************************************************************************************
     * Retrieves a border to display for a cell that contains focus.
     * <p>
     * @return A border to display for a cell that contains focus.
     ****************************************************************************************************/
    public Border getCellFocusBorder() {
        return cellBorder;
    }

    /******************************************************************************************
     * Assigns the cell focus background color.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setCellFocusBackground(Color color) {
        if (color != null) {
            cellFocusBackground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the background color for a focused cell.
     * <p>
     * @return The background color for a focused cell.
     ****************************************************************************************************/
    public Color getCellFocusBackground() {
        return cellFocusBackground;
    }

    /******************************************************************************************
     * Assigns the cell default background color.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setCellDefaultBackground(Color color) {
        if (color != null) {
            cellDefaultBackground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the background color for a non-focused cell.
     * <p>
     * @return The background color for a non-focused cell.
     ****************************************************************************************************/
    public Color getCellDefaultBackground() {
        return cellDefaultBackground;
    }

    /******************************************************************************************
     * Assigns the cell focus foreground color.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setCellFocusForeground(Color color) {
        if (color != null) {
            cellFocusForeground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the foreground color for a focused cell.
     * <p>
     * @return The foreground color for a focused cell.
     ****************************************************************************************************/
    public Color getCellFocusForeground() {
        return cellFocusForeground;
    }

    /******************************************************************************************
     * Assigns the cell default foreground color.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setCellDefaultForeground(Color color) {
        if (color != null) {
            cellDefaultForeground = color;
        }
    }

    /****************************************************************************************************
     * Retrieves the foreground color for a non-focused cell.
     * <p>
     * @return The foreground color for a non-focused cell.
     ****************************************************************************************************/
    public Color getCellDefaultForeground() {
        return cellDefaultForeground;
    }

    /****************************************************************************************************
     * Sets the table's auto resize mode when the table is resized.
     * <p>
     * @param resizeMode The mode (see JTable).
     ****************************************************************************************************/
    public void setAutoResizeMode(int resizeMode) {
        table.setAutoResizeMode(resizeMode);
    }

    /****************************************************************************************************
     * Sets the table's selection mode to allow only single selections, a single contiguous interval, or
     * multiple intervals. Note (see JTable).
     * <p>
     * @param selectionMode The mode to assign (see JTable)
     ****************************************************************************************************/
    public void setSelectionMode(int selectionMode) {
        table.setSelectionMode(selectionMode);
    }

    /****************************************************************************************************
     * Sets the allow row height for rows within the table.
     * <p>
     * @param height The height of rows in pixels.
     ****************************************************************************************************/
    public void setRowHeight(int height) {
        table.setRowHeight(height);
        rowTitleList.setFixedCellHeight(table.getRowHeight());
    }

    /****************************************************************************************************
     * Assigns the row headers to the table. The array of headers is in the order that the user wants
     * the rows to appear. This table does not allow the ordering of its rows to be altered (ie. no sorting).
     * <p>
     * @param headers An array of row headers in the order the developer wishes them to appear.
     ****************************************************************************************************/
    public void setRowHeaders(String[] headers) {
        headerModel.setRowTitles(headers);
        buildRowHeader();
    }

    /****************************************************************************************************
     * Assigns the column headers to the table. These headers do NOT include the row header column.
     * <p>
     * @param headers The column headers for the table.
     ****************************************************************************************************/
    public void setColumnHeaders(String[] headers) {
        table.setModel(new RDisplayTableModel(headers, 0));
        validateTableHeaderRenderer();
    }

    /****************************************************************************************************
     * This builds the row header area of the application. It must be called after the row headers are
     * known as it uses the display information to calculate the size of the column.
     ****************************************************************************************************/
    private void buildRowHeader() {
        if (rowTitleList == null) {
            rowTitleList = new JList();
        }

        rowTitleList.setModel(headerModel);
        rowTitleList.setBackground(CustomSwanLookAndFeel.getSwanBaseBackgroundColor());
        rowTitleList.setCellRenderer(new RowTitleTableTitleRenderer(this));
        rowTitleList.setFixedCellWidth(calculateCellWidth());
        rowTitleList.setFixedCellHeight(cellHeight);

        if (headerModel.getSize() > 0) {
            scrollView.setRowHeaderView(rowTitleList);
        } else {
            scrollView.setRowHeaderView(null);
        }
    }

    /****************************************************************************************************
     * Calculates the required cell width of the row headers.
     ****************************************************************************************************/
    private int calculateCellWidth() {
        FontMetrics fontMetrics = rowTitleList.getFontMetrics(rowTitleList.getFont());
        int cellWidth = 0;
        for (int i = 0; i < headerModel.getSize(); i++) {
            String text = (String) headerModel.getElementAt(i);

            if (text == null) {
                text = StringConstants.EMPTY;
            }
            int textSize = fontMetrics.stringWidth(text) + 15;
            if (textSize > cellWidth) {
                cellWidth = textSize;
            }
        }
        return cellWidth;
    }

    /****************************************************************************************************
     * Assigns all the column headers to the table. This is also called whenever the column headers are
     * altered as the preferred width of each column is determined by its header.
     ****************************************************************************************************/
    private void validateTableHeaderRenderer() {
        FontMetrics fontMetrics = table.getTableHeader().getFontMetrics(table.getTableHeader().getFont());
        TableColumnModel columnModel = table.getTableHeader().getColumnModel();
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = columnModel.getColumn(i);
            column.setHeaderRenderer(new RowTitleTableHeaderRenderer(this));
            column.setCellRenderer(new RowTitleTableCellRenderer(this));
            column.setPreferredWidth(fontMetrics.stringWidth(column.getHeaderValue().toString()) + headerPad);
        }
    }

    /****************************************************************************************************
     * MOUSE LISTENER METHODS
     ****************************************************************************************************/

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mouseClicked(MouseEvent event) {
        if (event.getClickCount() == 2) {
            doDoubleClickAction();
        }
    }

    /****************************************************************************************************
     * Empty implementation of the focus listener method.
     ****************************************************************************************************/
    public void focusGained(FocusEvent event) {
    }

    /****************************************************************************************************
     * Implement the focus listener method to store the selected cell if the table looses focus.
     ****************************************************************************************************/
    public void focusLost(FocusEvent event) {
        if (table.getSelectedRow() != -1) {
            selectedRow = table.getSelectedRow();
        }
        if (table.getSelectedColumn() != -1) {
            selectedCol = table.getSelectedColumn();
        }
    }

    /****************************************************************************************************
     * Retrieves the data object within the selected cell.
     * <p>
     * @return The data object within the selected cell.
     ****************************************************************************************************/
    private Object getSelectedData() {
        int row = table.getSelectedRow();
        int col = table.getSelectedColumn();

        if (row == -1 || col == -1) {
            row = selectedRow;
            col = selectedCol;
        }

        if (row == -1 || col == -1) {
            return null;
        }
        return ((RowTitleTableCell) table.getValueAt(row, col)).getData();
    }

    /****************************************************************************************************
     * Completely refreshes the table with the information contained within the parameter. All previous
     * data is removed from the table. Each cell of the parameter grid represents the matching coordinate
     * cell in the table the information will be displayed.
     * <p>
     * @param tableCellGrid A two-dimensional array representing all the data to be displayed in the table.
     ****************************************************************************************************/
    public void refreshTable(RowTitleTableCell[][] tableCellGrid) {
        RDisplayTableModel model = (RDisplayTableModel) table.getModel();
        model.clear();

        if (tableCellGrid == null) {
            return;
        }

        for (int x = 0; x < tableCellGrid.length; x++) {
            for (int y = 0; y < tableCellGrid[x].length; y++) {
                if (tableCellGrid[x][y] == null) {
                    tableCellGrid[x][y] = new RowTitleTableCell(getEmptyArray(), null);
                }
            }
            model.addRow(tableCellGrid[x]);
        }
    }

    /****************************************************************************************************
     * Build an empty cell array as a placeholder within the table for empty cell data.
     ****************************************************************************************************/
    private String[] getEmptyArray() {
        String[] array = new String[table.getColumnCount()];
        for (int i = 0; i < array.length; i++) {
            array[i] = StringConstants.EMPTY;
        }
        return array;
    }

    /****************************************************************************************************
     * Notifies all lsiteners when a cell becomes selected or de-selected.
     ****************************************************************************************************/
    protected void sendCellSelectionNotification() {
        Object data = getSelectedData();
        if (data != null) {
            notifyREventListeners(new RActionEvent(this, CELL_ACTIVATED, data));
        } else {
            notifyREventListeners(new RActionEvent(this, CELL_DEACTIVATED));
        }
    }

    /****************************************************************************************************
     * Notify all listeners when a cell is double-clicked.
     ****************************************************************************************************/
    private void doDoubleClickAction() {
        Object data = getSelectedData();
        if (data != null) {
            notifyREventListeners(new RActionEvent(this, CELL_ACTION, data));
        }
    }

    /****************************************************************************************************
     * Requests focus from the window and attempts to assign it to the last selected cell.
     ****************************************************************************************************/
    public void requestFocus() {
        if (selectedRow >= 0 && selectedCol >= 0) {
            ((JComponent) table.getCellRenderer(selectedRow, selectedCol)).requestFocusInWindow();
        }
    }

    /****************************************************************************************************
     * Clears the table of all values.
     ****************************************************************************************************/
    public void clearTable() {
        rowTitleList.removeAll();
        table.removeAll();
        validate();
        repaint();
    }
}
