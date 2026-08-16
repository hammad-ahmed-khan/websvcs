package oracle.retail.sim.client.swing.panel;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;

/********************************************************************************************
 * This panel is meant to assist in laying out other panels. The default settings of this
 * panel support a 10 pixel gap between each column and a (5 on each end of the panel) and a 10
 * pixel gap between rows (5 on the top and bottom of the panel).
 * <p>
 * This panel is only meant to layout RPanels and RScrollPanes. Attempting to add other
 * types of components will through an exception.
 * <p>
 * The user must specify the maximum number of rows. The maximum number of columns can be
 * left unspecified. Panels will be added until the end of the first column is reached and
 * then continue expanding until maximum columns are reached. The user may also specify
 * row and column when placing a new panel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RMatrixPanel extends RPanel {
    private static final long serialVersionUID = 3027978542658256637L;

    private static final int COL_LIMIT = 500;

    private int maxRows = Integer.MAX_VALUE;
    private int maxColumns = Integer.MAX_VALUE;

    private RLabel fillerLabel1 = new RLabel();
    private RLabel fillerLabel2 = new RLabel();

    private int lastRow = -1;
    private int lastColumn;

    private int vgap = 5;
    private int hgap = 5;

    private boolean isLocked;
    private boolean isHorizontalFillerNeeded = true;
    private boolean isVerticalFillerNeeded = true;

    private static final String METHOD_UNAVAILABLE = "Method is unavailable. Please use add(RPanel) or add(RPanel, row, column)";

    /******************************************************************************************
     * Returns new RMatrixPanel object.
     * <p>
     * @param rows The maximum number of rows available in the panel.
     *****************************************************************************************/
    public RMatrixPanel(int rows) {
        this(rows, COL_LIMIT);
    }

    /******************************************************************************************
     * Returns new RMatrixPanel object.
     * <p>
     * @param columns The maximum number of columns available in the panel.
     * @param rows The maximum number of rows available in the panel.
     *****************************************************************************************/
    public RMatrixPanel(int rows, int columns) {
        super(new GridBagLayout());

        maxColumns = columns;
        maxRows = rows;

        if (maxColumns > COL_LIMIT) {
            maxColumns = COL_LIMIT;
        }

        super.add(fillerLabel1, GridTool.constraints(0, maxRows, 1, 1, 0, 1, 0, 2, 0, 0, 0, 0));
        super.add(fillerLabel2, GridTool.constraints(maxColumns, maxRows - 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
    }

    /******************************************************************************************
     * Assigns layout manager to the panel. This method is overridden to only accepts GridBagLayout.
     * <p>
     * @param manager A GridBagLayout object.
     *****************************************************************************************/
    public void setLayout(LayoutManager manager) {
        if (!(manager instanceof GridBagLayout)) {
            throw new IllegalArgumentException("RMatrixPanel can only accept GridBagLayout as a layout.");
        }
        super.setLayout(manager);
    }

    /******************************************************************************************
     * Sets the gap between columns in the panel. This value cannot be altered after components
     * have been added to the panel.
     * <p>
     * @param gap The number of pixels to place between columns.
     *****************************************************************************************/
    public void setHorizontalGap(int gap) {
        if (isLocked) {
            throw new IllegalStateException("Unable to change horizontal gap after panels have been added!");
        }
        if (Math.IEEEremainder(gap, 2) != 0) {
            gap++;
        }
        hgap = gap / 2;
    }

    /******************************************************************************************
     * Sets the gap between rows in the panel. This value cannot be altered after components
     * have been added to the panel.
     * <p>
     * @param gap The number of pixels to place between rows.
     *****************************************************************************************/
    public void setVerticalGap(int gap) {
        if (isLocked) {
            throw new IllegalStateException("Unable to change vertical gap after panels have been added!");
        }
        if (Math.IEEEremainder(gap, 2) != 0) {
            gap++;
        }
        vgap = gap / 2;
    }

    /******************************************************************************************
     * Adds the specified panel to the end of this container. The panel is added to the
     * container at the next available row (within the current column).
     * <p>
     * @param panel The RPanel to add to the container.
     *****************************************************************************************/
    public Component add(RPanel panel) {
        return innerAdd(panel, true, false);
    }

    /******************************************************************************************
     * Adds a new component to the panel at the specified row and column.
     * <p>
     * @param component The component to add to the panel.
     * @param row The row to place the component in.
     * @param column The column to place the component in.
     *****************************************************************************************/
    public Component add(RPanel panel, int row, int column) {
        return innerAdd(panel, true, false, row, column);
    }

    /******************************************************************************************
     * Adds a new component to the panel with the specified explandability
     * <p>
     * @param component The component to add to the panel.
     * @param horizontalWeight True if the panel should expand horizontally, false otherwise.
     * @param verticalWeight True if the panel should expand vertically, false otherwise.
     *****************************************************************************************/
    public Component add(RPanel panel, boolean horizontalWeight, boolean verticalWeight) {
        return innerAdd(panel, horizontalWeight, verticalWeight);
    }

    /******************************************************************************************
     * Adds the specified RScrollPane to the end of this container. The RScrollPane is added to the
     * container at the next available row (within the current column).
     * <p>
     * @param scrollPane The RScrollPane to add to the container.
     *****************************************************************************************/
    public Component add(RScrollPane scrollPane) {
        return innerAdd(scrollPane, scrollPane.hasHorizontalWeight(), scrollPane.hasVerticalWeight());
    }

    /******************************************************************************************
     * Adds a new component to the panel at the specified row and column.
     * <p>`
     * @param component The component to add to the panel.
     * @param row The row to place the component in.
     * @param column The column to place the component in.
     *****************************************************************************************/
    public Component add(RScrollPane scrollPane, int row, int column) {
        return innerAdd(scrollPane, scrollPane.hasHorizontalWeight(), scrollPane.hasVerticalWeight(), row, column);
    }

    /******************************************************************************************
     * Adds the specified component to the end of this container. The component is added to the
     * container at the next available row (within the current column).
     * <p>
     * @param component The component to add to the container.
     *****************************************************************************************/
    private Component innerAdd(Component component, boolean hortExpand, boolean vertExpand) {
        int nextRow = lastRow + 1;
        int nextColumn = lastColumn;
        if (nextRow == maxRows) {
            nextColumn++;
            nextRow = 0;
        }
        return innerAdd(component, hortExpand, vertExpand, nextRow, nextColumn);
    }

    /******************************************************************************************
     * Adds a new component to the panel at the specified row and column.
     * <p>
     * @param component The component to add to the panel.
     * @param hortExpand True if component should expand horizontally.
     * @param vertExpand True if the component should expand vertically.
     * @param row The row to place the component in.
     * @param column The column to place the component in.
     *****************************************************************************************/
    private Component innerAdd(Component component, boolean hortExpand, boolean vertExpand, int row, int column) {
        if (row < 0 || row >= maxRows) {
            throw new IllegalArgumentException("Requested row " + row + " does not exist.");
        }
        if (column < 0 || column >= maxColumns) {
            throw new IllegalArgumentException("Requested column " + column + " does not exist.");
        }
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
        int hwght = 0;
        int vwght = 0;
        int fill = 3;

        if (hortExpand) {
            isHorizontalFillerNeeded = false;
            hwght = 1;
            fill = 1;
        }
        if (vertExpand) {
            isVerticalFillerNeeded = false;
            vwght = 1;
            fill = 2;
        }

        super.add(component, GridTool.constraints(column, row, 1, 1, hwght, vwght, 0, fill, vgap, hgap, vgap, hgap));

        lastColumn = column;
        lastRow = row;
        isLocked = true;

        fillerLabel1.setVisible(isVerticalFillerNeeded);
        fillerLabel2.setVisible(isHorizontalFillerNeeded);

        return component;
    }

    /******************************************************************************************
     * Adds the specified component to the end of this container. The component is added to the
     * panel at the next available row (within the current column). This method has been
     * overriden and made not available.
     * <p>
     * @param component The editor to add to the panel.
     *****************************************************************************************/
    public Component add(Component component) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /******************************************************************************************
     * Adds a new editor to the panel at the specified row and column. This method has been
     * overriden and made not available.
     * <p>
     * @param component The editor to add to the panel.
     * @param row The row to place the component in.
     * @param column The column to place the component in.
     *****************************************************************************************/
    public Component add(Component component, int row, int column) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /************************************************************************************
     * Adds the specified component to this container. It is strongly advised to use the
     * 1.1 method, add(Component, Object), in place of this method. This method has been
     * overridden and made unavailable.
     * <p>
     * @param name A string name for the component.
     * @param component The component to be added.
     * @return The component argument.
     ************************************************************************************/
    public Component add(String name, Component component) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /************************************************************************************
     * Adds the specified component to this container at the given index. This method has
     * been overridden and made unavailable.
     * <p>
     * @param component The component to be added
     * @param index Position to add the component
     * @return The component argument.
     ************************************************************************************/
    public Component add(Component component, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /************************************************************************************
     * Adds the specified component to the end of this container. This method has been
     * overridden and made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     ************************************************************************************/
    public void add(Component component, Object constraints) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /************************************************************************************
     * Adds the specified component to this container with the specified constraints at
     * the specified index.  Also notifies the layout manager to add the component to the
     * this container's layout using the specified constraints object.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     * @param index The position in the container's list at which to insert the component.
     * 		-1 means insert at the end.
     ************************************************************************************/
    public void add(Component component, Object constraints, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }
}
