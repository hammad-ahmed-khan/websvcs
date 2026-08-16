package oracle.retail.sim.client.swing.panel;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIMessageText;

/********************************************************************************************
 * This panel is meant to assist in laying out other panels, but can also be used to layout
 * editors and other components. This panel represents a grid where a divider is automatically
 * placed between any two components within the grid.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RDividerPanel extends RPanel {
    private static final long serialVersionUID = -4238464188711452070L;

    private int maxRows = Integer.MAX_VALUE;
    private int maxColumns = Integer.MAX_VALUE;

    private int lastRow;
    private int lastColumn = -1;

    /******************************************************************************************
     * Returns new RDividerPanel object.
     * <p>
     * @param rows The maximum number of rows available in the panel.
     * @param columns The maximum number of columns available in the panel.
     *****************************************************************************************/
    public RDividerPanel(int rows, int columns) {
        super(new GridBagLayout());
        maxColumns = columns;
        maxRows = rows;
        addDividers();
    }

    /******************************************************************************************
     * Assigns layout manager to the panel. This method is overridden to only accepts GridBagLayout.
     * <p>
     * @param manager A GridBagLayout object.
     *****************************************************************************************/
    public void setLayout(LayoutManager manager) {
        if (!(manager instanceof GridBagLayout)) {
            throw new IllegalArgumentException("RDividerPanel can only accept GridBagLayout as a layout.");
        }
        super.setLayout(manager);
    }

    /******************************************************************************************
     * Adds the specified component to the end of this container. The component is added at the
     * next available column (within the current row). It wraps to the next row when out of
     * columns. Components added using the add(component, row, column) method will be
     * overridden by this method.
     * <p>
     * @param component The component to add to the panel.
     *****************************************************************************************/
    public Component add(Component component) {
        int nextColumn = lastColumn + 1;
        int nextRow = lastRow;
        if (nextColumn == maxColumns) {
            nextRow++;
            nextColumn = 0;
        }
        lastRow = nextRow;
        lastColumn = nextColumn;
        return add(component, nextRow, nextColumn);
    }

    /******************************************************************************************
     * Adds the specified component at the given column and row.
     * <p>
     * @param component The component to add to the panel.
     * @param row The row to place the component in.
     * @param column The column to place the component in.
     *****************************************************************************************/
    public Component add(Component component, int row, int column) {
        if (row < 0 || row >= maxRows) {
            throw new IllegalArgumentException("Requested row " + row + " does not exist.");
        }
        if (column < 0 || column >= maxColumns) {
            throw new IllegalArgumentException("Requested column " + column + " does not exist.");
        }
        if (component == null) {
            throw new IllegalArgumentException("Component cannot be null");
        }
        int trueRow = row * 2;
        int trueColumn = column * 2;

        super.add(component, GridTool.constraints(trueColumn, trueRow, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        return component;
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
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
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
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds the specified component to the end of this container. This method has been
     * overridden and made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     ************************************************************************************/
    public void add(Component component, Object constraints) {
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
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
        throw new RuntimeException(UIMessageText.ADD_COMPONENT_METHOD_ERROR.getText());
    }

    /************************************************************************************
     * Adds dividers to the panel
     ************************************************************************************/
    private void addDividers() {
        for (int col = 1; col < maxColumns; col = col + 2) {
            for (int row = 0; row < maxRows * 2; row = row + 2) {
                super.add(getVerticalDivider(), GridTool.constraints(col, row, 1, 1, 0, 0, 0, 2, 0, 0, 0, 0));
            }
        }
        for (int row = 1; row < maxRows; row = row + 2) {
            for (int col = 0; col < maxColumns * 2; col = col + 2) {
                super.add(getHorizontalDivider(), GridTool.constraints(col, row, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
            }
        }
    }

    /************************************************************************************
     * Creates a new vertical divider.
     ************************************************************************************/
    private RDivider getVerticalDivider() {
        return new RDivider(RDivider.VERTICAL);
    }

    /************************************************************************************
     * Creates a new horizontal divider.
     ************************************************************************************/
    private RDivider getHorizontalDivider() {
        return new RDivider(RDivider.HORIZONTAL);
    }
}
