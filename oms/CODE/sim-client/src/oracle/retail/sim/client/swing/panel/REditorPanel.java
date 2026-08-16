package oracle.retail.sim.client.swing.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JLabel;
import oracle.retail.sim.client.swing.editor.REditorLabel;
import oracle.retail.sim.client.swing.editor.RetailEditor;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RList;
import oracle.retail.sim.client.swing.widget.RTextArea;

/********************************************************************************************
 * This panel is meant to assist in laying out editors, but can also be used to layout widgets
 * other than editors. The default settings of this panel support a 6 pixel gap between each
 * column and a (3 on each end of the panel) and a 4 pixel gap between rows (2 on the top
 * and bottom of the panel).
 * <p>
 * The user must specify the maximum number of rows. The maximum number of columns can be
 * left unspecified. Widgets will be added until the end of the first column is reached and
 * then continue expanding until maximum columns are reached. The user may also specify
 * the precise row and column when placing a new editor or widget into the panel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class REditorPanel extends RPanel implements PropertyChangeListener {
    private static final long serialVersionUID = -5365060913870179598L;

    private static final int COL_LIMIT = 500;

    private RLabel fillerLabel1 = new RLabel();
    private RLabel fillerLabel2 = new RLabel();

    private int maxRows = Integer.MAX_VALUE;
    private int maxColumns = Integer.MAX_VALUE;

    private int lastRow = -1;
    private int lastColumn;

    private int verticalGap = 3;
    private int horizontalGap = 3;

    private boolean isSpacedAlignment;
    private boolean isLocked;
    private boolean needsHorizontalFill = true;

    private List componentWrapperList = new ArrayList<>();

    /******************************************************************************************
     * Returns new REditorPanel object.
     * <p>
     * @param rows The maximum number of rows available in the panel.
     *****************************************************************************************/
    public REditorPanel(int rows) {
        this(rows, COL_LIMIT);
    }

    /******************************************************************************************
     * Returns new REditorPanel object.
     * <p>
     * @param rows The maximum number of rows available in the panel.
     * @param columns The maximum number of columns available in the panel.
     *****************************************************************************************/
    public REditorPanel(int rows, int columns) {
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
     * Assigns layout manager to the panel. This method is overridden to only accept GridBagLayout.
     * <p>
     * @param manager A GridBagLayout object.
     *****************************************************************************************/
    public void setLayout(LayoutManager manager) {
        if (!(manager instanceof GridBagLayout)) {
            throw new IllegalArgumentException("REditorPanel can only accept GridBagLayout as a layout.");
        }
        super.setLayout(manager);
    }

    /******************************************************************************************
     * Retrieves the last column used within the panel.
     * <p>
     * @return The last column used within the panel.
     *****************************************************************************************/
    public int getLastColumn() {
        return lastColumn;
    }

    /******************************************************************************************
     * Retrieves the last row used within the panel.
     * <p>
     * @return The last row used within the panel.
     *****************************************************************************************/
    protected int getLastRow() {
        return lastRow;
    }

    /******************************************************************************************
     * Sets whether or not the widgets are spaced over the entire panel. If the panel has extra
     * space and spaced alignment is true, the widgets will spread out across the panel. If the
     * spaced alignment is false, the widgets will move as close to the top of the panel as they
     * can.
     * <p>
     * @param isSpaced True if the components should be spaced out, false if not.
     *****************************************************************************************/
    public void setAutoSpacing(boolean autoSpacing) {
        isSpacedAlignment = autoSpacing;
        refreshSpaceAlignment();
    }

    /******************************************************************************************
     * Sets the gap between columns in the panel. This value cannot be altered after components
     * have been added to the panel.
     * <p>
     * @param gap The number of pixels to place between columns.
     * @exception IllegalStateException Thrown if the method is called after components have
     * been added to the panel.
     *****************************************************************************************/
    public void setHorizontalGap(int gap) {
        if (isLocked) {
            throw new IllegalStateException("Unable to change horizontal gap after widgets have been added!");
        }
        if (Math.IEEEremainder(gap, 2) != 0) {
            gap++;
        }
        horizontalGap = gap / 2;
    }

    /******************************************************************************************
     * Sets the gap between rows in the panel. This value cannot be altered after components
     * have been added to the panel.
     * <p>
     * @param gap The number of pixels to place between rows.
     * @exception IllegalStateException Thrown if the method is called after components have
     * been added to the panel.
     *****************************************************************************************/
    public void setVerticalGap(int gap) {
        if (isLocked) {
            throw new IllegalStateException("Unable to change vertical gap after widgets have been added!");
        }
        if (Math.IEEEremainder(gap, 2) != 0) {
            gap++;
        }
        verticalGap = gap / 2;
    }

    /******************************************************************************************
     * Skips a single cell when placing widgets in sequence on the panel.
     *****************************************************************************************/
    public void skip() {
        skip(1);
    }

    /******************************************************************************************
     * Skips a number of cells when placing widgets in sequence on the panel.
     * <p>
     * @param cells The number of cells to skip.
     *****************************************************************************************/
    public void skip(int cells) {
        for (int i = 0; i < cells; i++) {
            lastRow = lastRow + 1;
            if (lastRow == maxRows) {
                lastColumn++;
                lastRow = 0;
            }
        }
    }

    /************************************************************************************
     * Retrieves the maximum label width of the panel by column. This scans all RetailEditor
     * labels within the column and returns the maximum required size.
     * <p>
     * @param column The column to retrieve label widths from.
     * @return The maximum label size within the panel.
     ************************************************************************************/
    public int getMaximumLabelWidth(int column) {
        ComponentLayoutWrapper wrapper;
        RetailEditor editor;
        int maxWidth = 0;
        int tmpWidth = 0;
        for (Iterator iterator = componentWrapperList.iterator(); iterator.hasNext();) {
            wrapper = (ComponentLayoutWrapper) iterator.next();
            if (wrapper.column == column && wrapper.component instanceof RetailEditor) {
                editor = (RetailEditor) wrapper.component;
                tmpWidth = editor.getLabel().getPreferredSize().width;
                if (tmpWidth > maxWidth) {
                    maxWidth = tmpWidth;
                }
            }
        }
        return maxWidth;
    }

    /************************************************************************************
     * Assigns the label width to all RetailEditor labels within a column.
     * <p>
     * @param labelWidth The label width to assign in pixels.
     * @param column The column of editors to assign it to.
     ************************************************************************************/
    public void setLabelWidth(int labelWidth, int column) {
        ComponentLayoutWrapper wrapper;
        RetailEditor editor;
        REditorLabel label;
        Dimension dimension;
        for (Iterator iterator = componentWrapperList.iterator(); iterator.hasNext();) {
            wrapper = (ComponentLayoutWrapper) iterator.next();
            if (wrapper.column == column && wrapper.component instanceof RetailEditor) {
                editor = (RetailEditor) wrapper.component;
                label = editor.getLabel();
                dimension = new Dimension(labelWidth, label.getPreferredSize().height);
                label.setMinimumSize(dimension);
                label.setPreferredSize(dimension);
                label.setMaximumSize(dimension);
            }
        }
    }

    /******************************************************************************************
     * Adds the specified component to the end of this container. The component is added to the
     * panel at the next available cell.
     * <p>
     * @param component The component to add to the panel.
     *****************************************************************************************/
    public Component add(Component component) {
        int nextRow = lastRow + 1;
        int nextColumn = lastColumn;
        if (nextRow == maxRows) {
            nextColumn++;
            nextRow = 0;
        }
        return add(component, nextRow, nextColumn);
    }

    /******************************************************************************************
     * Adds a new component to the panel at the specified row and column.
     * <p>
     * @param component The component to add to the panel.
     * @param row The row to place the component in.
     * @param column The column to place the component in.
     * @throw IllegalArgumentException Thrown if the row or column is not valid or the
     * component is null.
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
        addEditor(component, column, row);

        lastColumn = column;
        lastRow = row;
        isLocked = true;

        return component;
    }

    /******************************************************************************************
     * This method will replace an existing component within the panel with a new component.
     * If the original component is not found in the panel, nothing will occur.
     * <p>
     * @param originalComponent The component to be replaced.
     * @param replacementComponent The new component.
     *****************************************************************************************/
    public void replace(Component originalComponent, Component replacementComponent) {
        if (originalComponent == null || replacementComponent == null) {
            return;
        }
        ComponentLayoutWrapper wrapper = null;
        for (Iterator iterator = componentWrapperList.iterator(); iterator.hasNext();) {
            wrapper = (ComponentLayoutWrapper) iterator.next();
            if (wrapper.component == originalComponent) {
                remove(originalComponent);
                wrapper.component = replacementComponent;
                add(wrapper.component, wrapper.row, wrapper.column);
                invalidate();
                revalidate();
                repaint();
                break;
            }
        }
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
     * this container's layout using the specified constraints object. This method has
     * been overridden and made unavailable
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
     * Adds a component to the matrix panel. It first figures out whether or not it is
     * an editor component or a regular general component. Seperate methods are called
     * for each type of component. For RetailEditors, the method first insures that all
     * labels are of the same size if within the same column.
     ************************************************************************************/
    private void addEditor(Component component, int column, int row) {
        componentWrapperList.add(new ComponentLayoutWrapper(component, column, row));

        if (component instanceof RetailEditor) {
            setEditorLabelWidth((RetailEditor) component, column);
            addRetailEditor(component, column, row);
        } else {
            addGeneralComponent(component, column, row);
        }
        component.addPropertyChangeListener(this);
    }

    /************************************************************************************
     * Assigns the label width to the new RetailEditor based on its column. If the new
     * label is larger than the others, it will reset ALL of the labels.
     * <p>
     * @param editor The new RetailEditor.
     * @param column The column it is placed in.
     ************************************************************************************/
    private void setEditorLabelWidth(RetailEditor editor, int column) {
        ComponentLayoutWrapper wrapper;
        int maxSize = getMaximumLabelWidth(column);
        int tmpSize = editor.getLabel().getPreferredSize().width;
        if (tmpSize > maxSize) {
            maxSize = tmpSize;
        }

        REditorLabel label = editor.getLabel();
        Dimension dimension = new Dimension(maxSize, label.getPreferredSize().height);
        label.setMinimumSize(dimension);
        label.setPreferredSize(dimension);
        label.setMaximumSize(dimension);

        for (Iterator iterator = componentWrapperList.iterator(); iterator.hasNext();) {
            wrapper = (ComponentLayoutWrapper) iterator.next();
            if (wrapper.column == column) {
                if (wrapper.component instanceof RetailEditor) {
                    label = ((RetailEditor) wrapper.component).getLabel();
                    dimension = new Dimension(maxSize, label.getPreferredSize().height);
                    label.setMinimumSize(dimension);
                    label.setPreferredSize(dimension);
                    label.setMaximumSize(dimension);
                }
            }
        }
    }

    /************************************************************************************
     * Adds a general component to the matrix panel at the specified column and row.
     ************************************************************************************/
    private void addGeneralComponent(Component component, int column, int row) {
        int vert = 0;
        int fill = 1;
        int horz = 1;

        if (component instanceof RList || component instanceof RTextArea || component instanceof JLabel) {
            vert = 1;
            fill = 3;
        } else if (component instanceof RButton) {
            horz = 0;
            fill = 0;
        }

        int align = 0;
        int vgap = verticalGap;
        int hgap = horizontalGap;

        super.add(component, GridTool.constraints(column, row, 1, 1, horz, vert, align, fill, vgap, hgap, vgap, hgap));
    }

    /************************************************************************************
     * Adds a retail editor to the matrix panel at specified column and row.
     ************************************************************************************/
    private void addRetailEditor(Component component, int column, int row) {
        RetailEditor editor = (RetailEditor) component;
        int vert = editor.getVerticalWeight();
        int horz = editor.getHorizontalWeight();
        int fill = editor.getFill();
        int align = 0;
        int vgap = verticalGap;
        int hgap = horizontalGap;

        if (horz > 0) {
            needsHorizontalFill = false;
            fillerLabel2.setVisible(false);
        }
        super.add(component, GridTool.constraints(column, row, 1, 1, horz, vert, align, fill, vgap, hgap, vgap, hgap));
    }

    /************************************************************************************
     * This code is executed when an editor is realigned internally and needs to inform
     * the panel of its new state.
     ************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(UIPropertyName.EDITOR_REALIGNMENT)) {
            doRealignment();
        }
    }

    /************************************************************************************
     * Rebuild the entire panel because the layout of a sub-widget changed. NOTES: This
     * max columns portion of the entire panel could be made smarter. Secondly, removeAll()
     * looses the focus. This is not yet a problem because realignment should not occur
     * while it has focus, but if it should in the future, we have to peel out the inner
     * component of an editor with focus and reset that after the layout.
     ************************************************************************************/
    private void doRealignment() {
        removeAll();

        super.add(fillerLabel1, GridTool.constraints(0, maxRows, 1, 1, 0, 1, 0, 2, 0, 0, 0, 0));
        super.add(fillerLabel2, GridTool.constraints(maxColumns, maxRows - 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));

        refreshSpaceAlignment();

        List tempLayoutList = new ArrayList<>();
        tempLayoutList.addAll(componentWrapperList);
        componentWrapperList.clear();

        ComponentLayoutWrapper wrapper = null;
        for (Iterator iterator = tempLayoutList.iterator(); iterator.hasNext();) {
            wrapper = (ComponentLayoutWrapper) iterator.next();
            add(wrapper.component, wrapper.row, wrapper.column);
        }
    }

    /************************************************************************************
     * Refreshing the space alignment of the matrix panel.
     ************************************************************************************/
    private void refreshSpaceAlignment() {
        fillerLabel1.setVisible(!isSpacedAlignment);
        fillerLabel2.setVisible(needsHorizontalFill);
    }

    /************************************************************************************
     *
     * Component Layout Wrapper
     *
     ************************************************************************************/
    private class ComponentLayoutWrapper {

        protected Component component;
        protected int column;
        protected int row;

        public ComponentLayoutWrapper(Component wComponent, int wColumn, int wRow) {
            component = wComponent;
            column = wColumn;
            row = wRow;
        }
    }
}
