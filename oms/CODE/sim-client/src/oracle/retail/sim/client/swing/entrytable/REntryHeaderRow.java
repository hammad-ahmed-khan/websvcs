package oracle.retail.sim.client.swing.entrytable;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.UIManager;
import javax.swing.border.Border;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIPropertyName;

/********************************************************************************************************
 * REntryHeaderRow
 * <p>
 * This class contains the entire header row for the REntryTable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

class REntryHeaderRow extends RPanel implements MouseListener {
    private static final long serialVersionUID = -4880824025001857009L;

    private REntryTableTraits tableInfo;
    private REntryHeader[] headers = new REntryHeader[0];
    private MouseEvent oldMouseEvent;

    /****************************************************************************************************
     * Constructor
     * <p>
     * @param tableInfo A data object containing the traits of the table.
     ***************************************************************************************************/
    public REntryHeaderRow(REntryTableTraits tableInfo) {
        setLayout(new GridBagLayout());
        this.tableInfo = tableInfo;
        setBackground(tableInfo.getHeaderBackgroundColor());
        setForeground(tableInfo.getHeaderForegroundColor());
        setFont(tableInfo.getHeaderFont());
        buildHeaderRow();
        layoutHeaderRow();
    }

    /****************************************************************************************************
     * Helper method that builds all the headers for the header row. Only headers marked visible are
     * displayed.
     ***************************************************************************************************/
    private void buildHeaderRow() {
        REntryColumn[] columns = tableInfo.getColumns();

        List visibleColumns = new ArrayList<>(columns.length + 1);
        for (REntryColumn column : columns) {
            if (column.isVisible()) {
                visibleColumns.add(column);
            }
        }
        headers = new REntryHeader[visibleColumns.size() + 1];
        int i = 0;
        for (Iterator iterator = visibleColumns.iterator(); iterator.hasNext();) {
            headers[i] = buildHeader((REntryColumn) iterator.next(), i, false);
            i++;
        }
        headers[i] = buildHeader(tableInfo.getConfigurationColumn(), i, true);
        headers[i].setWidth(20); // Width of scrollbar
    }

    /****************************************************************************************************
     * Builds a specific header for the column.
     * <p>
     * @param column The column object the header is for.
     * @param index The index in the header row.
     * @param isConfiguration True if this is the configuration header, false otherwise.
     ***************************************************************************************************/
    private REntryHeader buildHeader(REntryColumn column, int index, boolean isConfiguration) {
        REntryHeader header = new REntryHeader(column.getTitle(), index);
        header.setBorder(tableInfo.getHeaderBorder());
        header.setFont(getFont());
        header.setForeground(getForeground());
        header.setBackground(getBackground());
        header.addMouseListener(this);
        header.setConfiguration(isConfiguration);
        header.setWidth(column.getWidth());
        if (isConfiguration) {
            header.setIcon(UIManager.getIcon(UIThemeName.RDISPLAYTABLE_CONFIG_ICON));
        }
        return header;
    }

    /****************************************************************************************************
     * Assigns the font to all the headers in the header row.
     * <p>
     * @param font The font.
     ***************************************************************************************************/
    public void setFont(Font font) {
        if (font != null) {
            super.setFont(font);
            if (headers != null) {
                for (REntryHeader header : headers) {
                    header.setFont(font);
                }
            }
        }
    }

    /****************************************************************************************************
     * Assigns the background color to all the headers in the header row.
     * <p>
     * @param background The color.
     ***************************************************************************************************/
    public void setBackground(Color background) {
        if (background != null) {
            super.setBackground(background);
            if (headers != null) {
                for (REntryHeader header : headers) {
                    header.setBackground(background);
                }
            }
        }
    }

    /****************************************************************************************************
     * Assigns the foreground color to all the headers in the header row.
     * <p>
     * @param foregound The color.
     ***************************************************************************************************/
    public void setForeground(Color foreground) {
        if (foreground != null) {
            super.setForeground(foreground);
            if (headers != null) {
                for (REntryHeader header : headers) {
                    header.setForeground(foreground);
                }
            }
        }
    }

    /****************************************************************************************************
     * Assigns whether or not a border should be visible on the headers within the row.
     * <p>
     * @param visible True if the border should be visible, false otherwise.
     ***************************************************************************************************/
    protected void setBorderVisible(boolean visible) {
        Border border = null;
        if (visible) {
            border = tableInfo.getHeaderBorder();
        }
        for (REntryHeader header : headers) {
            header.setBorder(border);
        }
    }

    /****************************************************************************************************
     * Assigns the header justification value to the header of the column.
     * <p>
     * @param column The column index.
     * @param justification The justification (EditorConstants.LEFT/RIGHT/CENTER).
     ***************************************************************************************************/
    protected void setJustification(int column, int justification) {
        headers[column].setJustification(justification);
    }

    /****************************************************************************************************
     * Assigns the mnemonic to a particular header within the row.
     * <p>
     * @param title The title of the header to assign the mnemonic to.
     * @param character The mnemonic character.
     ***************************************************************************************************/
    protected void setMnemonic(String title, char character) {
        REntryColumn[] columns = tableInfo.getColumns();
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getTitle().equals(title)) {
                headers[i].setMnemonic(character);
            }
        }
    }

    /****************************************************************************************************
     * Retrieves the column index for a particular title.
     * <p>
     * @param title The title of the column.
     * @return The index of the column.
     ***************************************************************************************************/
    protected int getColumnIndex(String title) {
        REntryColumn[] columns = tableInfo.getColumns();
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getTitle().equals(title)) {
                return i;
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Retrieves the column index for a particular attribute.
     * <p>
     * @param attribute The attribute of the column.
     * @return The index of the column.
     ***************************************************************************************************/
    protected int getColumnIndexByAttribute(String attribute) {
        REntryColumn[] columns = tableInfo.getColumns();
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getAttribute().equals(attribute)) {
                return i;
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Retrieves the column index for a particular mnemonic.
     * <p>
     * @param character The mnemonic character to find a column for.
     * @param The column index of the column displaying that particular mnemonic character.
     ***************************************************************************************************/
    protected int getColumnIndexByMnemonic(String character) {
        for (int i = 0; i < headers.length; i++) {
            int mnemonic = headers[i].getMnemonic();
            if (mnemonic > 0) {
                char[] array = character.toCharArray();
                if (array[0] == mnemonic) {
                    return i;
                }
            }
        }
        return -1;
    }

    /****************************************************************************************************
     * Assigns the column width (in pixels) to a particular column. This triggers a re-display of the
     * header (which in turn redisplays the entire table).
     * <p>
     * @param column The column index to assign the width to.
     * @param width The width (in pixels).
     ***************************************************************************************************/
    protected void setColumnWidth(int column, int width) {
        headers[column].setWidth(width);
        layoutHeaderRow();
    }

    /****************************************************************************************************
     * Retrieves an array of header widths. Each index in the array represents the column index for which
     * the int value is the width of teh column in pixels.
     * <p>
     * @return An integer array of column header widths.
     ***************************************************************************************************/
    protected int[] getColumnWidths() {
        int[] array = new int[headers.length];
        for (int i = 0; i < array.length; i++) {
            if (headers[i].isResizable()) {
                array[i] = -1;
            } else {
                array[i] = headers[i].getPreferredSize().width;
            }
        }
        return array;
    }

    /****************************************************************************************************
     * Retrieves whether or not the given column should be sorted ascending or not.
     * <p>
     * @param column The column index of the header.
     * @return True if the column should sort ascending, false if not.
     ***************************************************************************************************/
    protected boolean shouldSortAscending(int column) {
        return headers[column].shouldSortAscending();
    }

    /****************************************************************************************************
     * Assigns the arrow image to the header as specified by column and ascending flag.
     ***************************************************************************************************/
    protected void setSortedHeader(int column, boolean ascending) {
        for (int i = 0; i < headers.length; i++) {
            if (i != column) {
                headers[i].setNoArrow();
            } else if (ascending) {
                headers[i].setDownArrow();
            } else {
                headers[i].setUpArrow();
            }
        }
    }

    /****************************************************************************************************
     * Helper method to handle the layout of the headers in the header row.
     ***************************************************************************************************/
    private void layoutHeaderRow() {
        removeAll();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].isResizable()) {
                add(headers[i], GridTool.constraints(i, 0, 1, 1, 1, 0, 1, 3, 0, 0, 0, 0));
            } else {
                add(headers[i], GridTool.constraints(i, 0, 1, 1, 0, 0, 1, 3, 0, 0, 0, 0));
            }
        }
        validate();
        repaint();
    }

    /****************************************************************************************************
     * Implements the mouselistener interface to handle when the mouse is clicked. If the header that is
     * clicked is the configuraton header, the entry table configuration event is fired, otherwise a sort
     * event is fired.
     ***************************************************************************************************/
    public void mouseClicked(MouseEvent mouseEvent) {
        if (oldMouseEvent != null) {
            long oldTime = oldMouseEvent.getWhen();
            long newTime = mouseEvent.getWhen();
            if (oldTime == newTime) {
                return;
            }
        }
        oldMouseEvent = mouseEvent;

        REntryHeader header = (REntryHeader) mouseEvent.getSource();

        if (header.isConfiguration()) {
            firePropertyChange(UIPropertyName.ENTRY_TABLE_CONFIGURATION, null, header);
        } else {
            firePropertyChange(UIPropertyName.ENTRY_TABLE_SORT_TRIGGERED, null, header.getIndex());
        }
    }

    /****************************************************************************************************
     * Empty implementation of the remaining mouse listener methods.
     ***************************************************************************************************/
    public void mouseEntered(MouseEvent event) {
    }

    public void mouseExited(MouseEvent event) {
    }

    public void mousePressed(MouseEvent event) {
    }

    public void mouseReleased(MouseEvent event) {
    }

    /****************************************************************************************************
     * Returns formatted string for debugging purposes.
     ***************************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder("RHeaderRow [");
        buffer.append("Header Count = ").append(headers.length).append("]");
        return buffer.toString();
    }
}
