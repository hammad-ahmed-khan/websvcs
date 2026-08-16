package oracle.retail.sim.client.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager2;
import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingConstants;

/********************************************************************************************************
 * HierarchyConstraints
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class HierarchyLayout implements LayoutManager2, Serializable {
    private static final long serialVersionUID = 3599211470224133611L;

    /** HierarchyLayout components use this setting to determine flow */
    public static final int TOP_BOTTOM = 0;
    public static final int BOTTOM_TOP = 1;
    public static final int LEFT_RIGHT = 2;
    public static final int RIGHT_LEFT = 4;

    private int direction;
    private int align;
    private int hgap;
    private int vgap;

    /** This Map maintains the association between a component and its hierarchy constraints. The
     * keys in compatible are the components and the values are <code>HierarchyConstraints</code>.
     */
    private Map constraintsMap = new HashMap<>();

    /**
     * All the rows cached. The values are <code>List</code> keyed by the the row number as an
     * <code>Integer</code> object.
     */
    private Map rowMap = new HashMap<>();

    /** The one and only constraint used for the root component. */
    private final HierarchyConstraints rootConstraints = new HierarchyConstraints(null);

    /** An array to track the row sizes so the next row will know where to start. */
    private int[] rowheight = new int[10];

    /********************************************************************************************************
     * Default constructor. Lays components out from top to bottom, with a leading alignment and default
     * horizontal and vertical gaps.
     *******************************************************************************************************/
    public HierarchyLayout() {
        this(TOP_BOTTOM, SwingConstants.LEADING, 15, 15);
    }

    /********************************************************************************************************
     * Constructs a HierarchyLayout with the specified direction. Lays components out in the specified
     * direction, with a leading alignment and default horizontal and vertical gaps.
     * <p>
     * @param dir whether layout will be placed from left to right, top to bottom, right to left, ot
     *            bottom to top.
     *******************************************************************************************************/
    public HierarchyLayout(int dir) {
        this(dir, SwingConstants.LEADING, 15, 15);
    }

    /********************************************************************************************************
     * Constructs a HierarchyLayout with the specified horizontal and vertical gaps. Lays components out
     * from top to bottom, with a leading alignment.
     *
     * @param hgap the amount of space to put between components horizontally
     * @param vgap the amount of space to put between components vertically
     *******************************************************************************************************/
    public HierarchyLayout(int hgap, int vgap) {
        this(TOP_BOTTOM, SwingConstants.LEADING, hgap, vgap);
    }

    /********************************************************************************************************
     * Detailed HierarchyLayout constructor.
     *
     * @param dir whether layout will be placed from left to right, top to bottom, right to left, ot
     *            bottom to top.
     * @param align layout will align to closest corner ie. <code>LEADING</code>, or to the center ie.
     *            <code>CENTER</code>
     * @param hgap the amount of space to put between components horizontally
     * @param vgap the amount of space to put between components vertically
     * @see CMSSwingConstants
     *******************************************************************************************************/
    public HierarchyLayout(int dir, int align, int hgap, int vgap) {
        direction = dir;
        this.align = align;
        this.hgap = hgap;
        this.vgap = vgap;
    }

    /********************************************************************************************************
     * Adds the specified component with the specified name to the layout.
     *
     * @note not implemented
     * @param name the name of the component.
     * @param comp the component to be added.
     *******************************************************************************************************/
    public void addLayoutComponent(String name, Component comp) {
    }

    /********************************************************************************************************
     * Adds the specified component to the layout, using the specified constraint object.
     *
     * @param comp the component to be added.
     * @param constraints an object that determines how the component is added to the layout.
     *******************************************************************************************************/
    public void addLayoutComponent(Component comp, Object constraints) {
        if (constraints instanceof HierarchyConstraints) {
            setConstraints(comp, (HierarchyConstraints) constraints);
            return;
        }
        throw new IllegalArgumentException("cannot add to layout: constraint must be a HierarchyConstraint");
    }

    /********************************************************************************************************
     * Sets the constraints for the specified component in this layout. If setting the root, then the
     * comp values are reset. If the parent of the constraint can not be found, then an
     * IllegalArgumentException is thrown.
     *
     * @param comp the component to be modified.
     * @param constraints the constraints to be applied.
     *******************************************************************************************************/
    public void setConstraints(Component comp, HierarchyConstraints constraints) {
        if (constraints.equals(rootConstraints)) {
            setRoot(comp);
        } else if (constraintsMap.containsKey(constraints.getParentNode())) {
            constraintsMap.put(comp, constraints);
            HierarchyConstraints parentConstraints = getConstraints(constraints.getParentNode());
            parentConstraints.addChild(comp);
        } else {
            throw new IllegalArgumentException("parent node must exist in heirarchy");
        }
    }

    /********************************************************************************************************
     * Gets the constraints for the specified component. A copy of the actual
     * <code>HierarchyConstraints</code> object is returned.
     *
     * @param comp the component to be queried.
     * @return the constraint for the specified component in this heirarchy layout; a copy of the actual
     *         constraint object is returned.
     *******************************************************************************************************/
    public HierarchyConstraints getConstraints(Component comp) {
        HierarchyConstraints constraints = (HierarchyConstraints) constraintsMap.get(comp);
        if (constraints == null) {
            return null;
        }
        return (HierarchyConstraints) constraints.clone();
    }

    /********************************************************************************************************
     * Lays out the specified container using this layout.
     *
     * @param target the container in which to do the layout.
     *******************************************************************************************************/
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            int ncomponents = parent.getComponentCount();
            if (ncomponents == 0) {
                return;
            }

            // first we need to clear all cached information
            invalidateLayout(parent);

            // the first component is the root
            Component root = parent.getComponent(0);
            Dimension rootDim = root.getPreferredSize();
            Dimension parentDim = parent.getSize();
            switch (direction) {
                case LEFT_RIGHT:
                    root.setBounds(hgap, vgap, rootDim.width, rootDim.height);
                    rowheight[0] = hgap * 2 + rootDim.width;
                    break;
                case RIGHT_LEFT:
                    root.setBounds(parentDim.width - rootDim.width - 2 * hgap, vgap, rootDim.width, rootDim.height);
                    rowheight[0] = root.getX();
                    break;
                case BOTTOM_TOP:
                    root.setBounds(hgap, parentDim.height - rootDim.height - 2 * vgap, rootDim.width, rootDim.height);
                    rowheight[0] = root.getY();
                    break;
                default:
                    root.setBounds(hgap, vgap, rootDim.width, rootDim.height);
                    rowheight[0] = vgap * 2 + rootDim.height;
            }

            List firstLevel = new ArrayList<>();
            firstLevel.add(root);
            rowMap.put(0, firstLevel);

            // place them
            for (int i = 1; i < ncomponents; i++) {
                addNode(parent.getComponent(i));
            }
        }
    }

    /********************************************************************************************************
     * Returns the alignment along the x axis. This specifies how the component would like to be aligned
     * relative to other components. The value should be a number between 0 and 1 where 0 represents
     * alignment along the origin, 1 is aligned the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target
     * @return float the alignment
     *******************************************************************************************************/
    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    /********************************************************************************************************
     * Returns the alignment along the y axis. This specifies how the component would like to be aligned
     * relative to other components. The value should be a number between 0 and 1 where 0 represents
     * alignment along the origin, 1 is aligned the furthest away from the origin, 0.5 is centered, etc.
     *
     * @param target
     * @return float the alignment
     *******************************************************************************************************/
    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    /********************************************************************************************************
     * Invalidates the layout, indicating that if the layout manager has cached information it should be
     * discarded.
     *
     * @param target
     *******************************************************************************************************/
    public void invalidateLayout(Container target) {
        // the current row information should be discarded
        rowMap.clear();

        // all the children node information has to be discarded
        HierarchyConstraints tmpConstraints = null;

        int ncomponents = target.getComponentCount();
        for (int i = 0; i < ncomponents; i++) {
            tmpConstraints = (HierarchyConstraints) constraintsMap.get(target.getComponent(i));

            if (tmpConstraints != null) {
                tmpConstraints.clearChildren();
            }
        }
        switch (direction) {
            case BOTTOM_TOP:
            case RIGHT_LEFT:
                Arrays.fill(rowheight, Integer.MAX_VALUE);
                break;
            default:
                Arrays.fill(rowheight, Integer.MIN_VALUE);
        }
    }

    /********************************************************************************************************
     * Method should eliminate the comp specified and all its children...
     *
     * @param comp
     *******************************************************************************************************/
    public void removeLayoutComponent(Component comp) {
        HierarchyConstraints cons = getConstraints(comp);
        if (cons.equals(rootConstraints)) {
            constraintsMap.clear();
            return;
        }
        constraintsMap.remove(comp);
    }

    /********************************************************************************************************
     * Determines the preferred size of the container argument using this layout.
     * <p>
     * The preferred width of this layout is set by the furthest comp to the right. The height will be
     * determined by the last row in the layout.
     *
     * @param target the container in which to do the layout.
     * @return the preferred dimensions needed to lay out the subcomponents of the specified container.
     *******************************************************************************************************/
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Dimension dimension = new Dimension();

            int rows = rowMap.size();
            if (rows > 0) {
                List row = null;
                Component component = null;
                Dimension tempDim = null;
                switch (direction) {
                    case LEFT_RIGHT:
                        // determine height
                        for (int i = 0; i < rows; i++) {
                            row = (List) rowMap.get(i);
                            component = (Component) row.get(row.size() - 1);
                            tempDim = component.getPreferredSize();
                            if (component.getY() + tempDim.height + vgap > dimension.height) {
                                dimension.height = component.getY() + tempDim.width + vgap;
                            }
                        }
                        // determine width
                        row = (List) rowMap.get(rows - 1);
                        for (int i = row.size() - 1; i >= 0; i--) {
                            component = (Component) row.get(i);
                            tempDim = component.getPreferredSize();
                            if (component.getX() + tempDim.width + hgap > dimension.width) {
                                dimension.width = component.getX() + tempDim.width + hgap;
                            }
                        }
                        break;
                    case RIGHT_LEFT:
                        // determine height
                        for (int i = 0; i < rows; i++) {
                            row = (List) rowMap.get(i);
                            component = (Component) row.get(row.size() - 1);
                            tempDim = component.getPreferredSize();
                            if (component.getY() + tempDim.height + vgap > dimension.height) {
                                dimension.height = component.getY() + tempDim.width + vgap;
                            }
                        }
                        // determine width
                        row = (List) rowMap.get(0);
                        component = (Component) row.get(0);
                        tempDim = component.getPreferredSize();
                        if (component.getX() + tempDim.width + hgap > dimension.width) {
                            dimension.width = component.getX() + tempDim.width + hgap;
                        }
                        int offleft = 0;
                        for (int i = rows - 1; i >= 0; i--) {
                            row = (List) rowMap.get(i);
                            component = (Component) row.get(0);
                            if (hgap - component.getX() > offleft) {
                                offleft = hgap - component.getX();
                            }
                        }
                        dimension.width += offleft;
                        break;
                    case BOTTOM_TOP:
                        // determine height
                        row = (List) rowMap.get(0);
                        component = (Component) row.get(0);
                        tempDim = component.getPreferredSize();
                        if (component.getY() + tempDim.height + vgap > dimension.height) {
                            dimension.height = component.getY() + tempDim.height + vgap;
                        }
                        int offtop = 0;
                        row = (List) rowMap.get(rows - 1);
                        for (int i = row.size() - 1; i >= 0; i--) {
                            component = (Component) row.get(i);
                            if (vgap - component.getY() > offtop) {
                                offtop = vgap - component.getY();
                            }
                        }
                        dimension.height += offtop;
                        // determine width
                        for (int i = 0; i < rows; i++) {
                            row = (List) rowMap.get(i);
                            component = (Component) row.get(row.size() - 1);
                            tempDim = component.getPreferredSize();
                            if (component.getX() + tempDim.width + hgap > dimension.width) {
                                dimension.width = component.getX() + tempDim.width + hgap;
                            }
                        }
                        break;
                    default:
                        // determine height
                        row = (List) rowMap.get(rows - 1);
                        for (int i = row.size() - 1; i >= 0; i--) {
                            component = (Component) row.get(i);
                            tempDim = component.getPreferredSize();
                            if (component.getY() + tempDim.height + vgap > dimension.height) {
                                dimension.height = component.getY() + tempDim.height + vgap;
                            }
                        }
                        // determine width
                        for (int i = 0; i < rows; i++) {
                            row = (List) rowMap.get(i);
                            component = (Component) row.get(row.size() - 1);
                            tempDim = component.getPreferredSize();
                            if (component.getX() + tempDim.width + hgap > dimension.width) {
                                dimension.width = component.getX() + tempDim.width + hgap;
                            }
                        }
                }
            }
            return dimension;
        }
    }

    /********************************************************************************************************
     * The minimum height of this layout. This is the same as prefrerredLayoutSize
     *
     * @param target the container in which to do the layout.
     * @return the minimum dimensions needed to lay out the subcomponents of the specified container.
     * @see #preferredLayoutSize
     *******************************************************************************************************/
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    /********************************************************************************************************
     * Returns the maximum dimensions for this layout given the components in the specified target
     * container.
     *
     * @param target the component which needs to be laid out
     * @return the maximum dimensions for this layout
     *******************************************************************************************************/
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /********************************************************************************************************
     * Sets the layout direction.
     *******************************************************************************************************/
    public void setLayoutDirection(int direction) {
        if (direction >= TOP_BOTTOM && direction <= RIGHT_LEFT) {
            this.direction = direction;
        }
    }

    /********************************************************************************************************
     * Gets the layout direction.
     *******************************************************************************************************/
    public int getLayoutDirection() {
        return direction;
    }

    /********************************************************************************************************
     * Sets the alignment.
     *******************************************************************************************************/
    public void setAlignment(int align) {
        this.align = align;
    }

    /********************************************************************************************************
     * Gets the alignment.
     *******************************************************************************************************/
    public int getAlignment() {
        return align;
    }

    /****************************************************************************************************
     * Sets the horizontal gap between components.
     ***************************************************************************************************/
    public void setHoriztonalGap(int gap) {
        hgap = gap;
    }

    /****************************************************************************************************
     * Gets the horizontal gap between components.
     ***************************************************************************************************/
    public int getHorizontalGap() {
        return hgap;
    }

    /****************************************************************************************************
     * Sets the vertical gap between components.
     ***************************************************************************************************/
    public void setVerticalGap(int gap) {
        vgap = gap;
    }

    /****************************************************************************************************
     * Gets the vertical gap between components.
     ***************************************************************************************************/
    public int getVerticalGap() {
        return vgap;
    }

    /****************************************************************************************************
     * Returns a string representation of this hierarchy layout's values.
     *
     * @return a string representation of this hierarchy layout.
     ***************************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getClass().getName());
        buffer.append(":rows=");
        buffer.append(rowMap.size());
        buffer.append(",comps=");
        buffer.append(constraintsMap.size());
        return buffer.toString();
    }

    /****************************************************************************************************
     * Sets the root component. Invoking this method will clear all history. Of course, the root row only
     * has to hold one component, so its not an array.
     ***************************************************************************************************/
    protected void setRoot(Component comp) {
        constraintsMap.clear();
        constraintsMap.put(comp, rootConstraints);
    }

    /****************************************************************************************************
     * Adds a child node component. Initial the comp gets placed get placed one comp.getWidth() value
     * away from it will wind up. Then when the recursion begins, it will move to its correct spot.
     *
     * @throws IndexOutOfBoundsException if level of child is above or equal to its parent.
     ***************************************************************************************************/
    private void addNode(Component component) {
        // clear cached children information
        HierarchyConstraints childConstraints = (HierarchyConstraints) constraintsMap.get(component);
        // get parent information
        Component parent = childConstraints.getParentNode();
        int requestedLevel = childConstraints.getLevel();

        HierarchyConstraints parentConstraints = (HierarchyConstraints) constraintsMap.get(parent);
        if (requestedLevel > parentConstraints.getLevel()) {
            int dist = 0;
            int x = 0;
            int y = 0;

            switch (direction) {
                case LEFT_RIGHT:
                case RIGHT_LEFT:
                    dist = component.getPreferredSize().height + vgap;
                    break;
                default:
                    dist = component.getPreferredSize().width + hgap;
            }

            Component[] children = parentConstraints.getChildren();
            if (children.length > 0) {
                // place the comp to the right of the right most brother
                Component childComp = children[children.length - 1];
                switch (direction) {
                    case RIGHT_LEFT:
                    case LEFT_RIGHT:
                        x = childComp.getX();
                        y = childComp.getY() + childComp.getHeight() + vgap - dist;
                        break;
                    default:
                        x = childComp.getX() + childComp.getWidth() + hgap - dist;
                        y = childComp.getY();
                }
            } else {
                // find a the rightmost neighbor to the left we can place after
                Component neighborComp = getNeighborAtLevel(getIndexOfParent(component), requestedLevel);
                if (neighborComp == null) {
                    // no one is to the left, insert at under my parent
                    switch (direction) {
                        case RIGHT_LEFT:
                            x = rowheight[requestedLevel - 1] - component.getPreferredSize().width - hgap;
                            y = parent.getY() - dist;
                            break;
                        case LEFT_RIGHT:
                            x = rowheight[requestedLevel - 1];
                            y = parent.getY() - dist;
                            break;
                        case BOTTOM_TOP:
                            x = parent.getX() - dist;
                            y = rowheight[requestedLevel - 1] - component.getPreferredSize().height - vgap;
                            break;
                        default:
                            x = parent.getX() - dist;
                            y = rowheight[requestedLevel - 1];
                    }
                } else {
                    // if the neighbor is in the way, then place to right, otherwise, place under parent
                    switch (direction) {
                        case RIGHT_LEFT:
                        case LEFT_RIGHT:
                            if (neighborComp.getY() + neighborComp.getHeight() + vgap > parent.getY()) {
                                x = neighborComp.getX();
                                y = neighborComp.getY() + neighborComp.getHeight() + vgap - dist;
                            } else {
                                x = neighborComp.getX();
                                y = parent.getY() - dist;
                            }
                            break;
                        default:
                            if (neighborComp.getX() + neighborComp.getWidth() + hgap > parent.getX()) {
                                x = neighborComp.getX() + neighborComp.getWidth() + hgap - dist;
                                y = neighborComp.getY();
                            } else {
                                x = parent.getX() - dist;
                                y = neighborComp.getY();
                            }
                    }
                }
            }
            placeComponent(component, new Point(x, y));

            List rowList = (List) rowMap.get(requestedLevel);
            int index = rowList.indexOf(component);

            // if the left most neighbor to the right is in the way, then start
            // at the right and move everyone over and adjust their parents if
            // needed.
            Component tmpComp = null;
            switch (direction) {
                case RIGHT_LEFT:
                case LEFT_RIGHT:
                    if (index > 0) {
                        tmpComp = (Component) rowList.get(index - 1);
                    }
                    break;
                default:
                    if (index < rowList.size() - 1) {
                        tmpComp = (Component) rowList.get(index + 1);
                    }
            }
            if (tmpComp != null) {
                boolean shouldMoveRow = false;
                switch (direction) {
                    case RIGHT_LEFT:
                    case LEFT_RIGHT:
                        shouldMoveRow = tmpComp.getY() - tmpComp.getHeight() <= component.getY() + component.getHeight();
                        break;
                    default:
                        shouldMoveRow = tmpComp.getX() - tmpComp.getWidth() <= component.getX() + component.getWidth();
                }

                // this is the rearrangement algorithm
                if (shouldMoveRow) {
                    for (int i = rowList.size() - 1; i >= index; i--) {
                        moveComponent((Component) rowList.get(i), dist);
                    }
                    return;
                }
            }
            // else, he's not in the way, just readjust the one comp
            moveComponent(component, dist);
        } else {
            throw new IndexOutOfBoundsException("Child must be below parent:" + requestedLevel + "<=" + parentConstraints.getLevel());
        }
    }

    /****************************************************************************************************
     * Return the right most neighbor to the left of the children of the parent indicated by the
     * specified index.
     *
     * @param parent the index of the parent node that is looking for an unrelated child to the left.
     * @param level at which the desired neighbor should exist
     * @return an unrelated neighbor child to left of this parent comp or <code>null</code> if no such
     *         left-handed neighbor exists at that level
     ***************************************************************************************************/
    private Component getNeighborAtLevel(int parent, int level) {
        List rowList = (List) rowMap.get(level);
        if (rowList != null) {
            int i = 0;
            for (i = rowList.size() - 1; i >= 0; i--) {
                if (getIndexOfParent((Component) rowList.get(i)) < parent) {
                    return (Component) rowList.get(i);
                }
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Here I'm only updating all the cahed information. The intention is to place the comp at
     * <code>pt</code> which should be exactly (comp.width() + hgap) away from where it should wind up
     * before the recursive algorithm begins.
     ***************************************************************************************************/
    private void placeComponent(Component comp, Point pt) {
        HierarchyConstraints childConstraints = (HierarchyConstraints) constraintsMap.get(comp);

        // update list of children
        Component parent = childConstraints.getParentNode();
        HierarchyConstraints parentConstraints = (HierarchyConstraints) constraintsMap.get(parent);
        parentConstraints.addChild(comp);

        // update list in row
        int level = childConstraints.getLevel();
        List rowList = (List) rowMap.get(level);
        if (rowList == null) {
            rowList = new ArrayList<>();
            rowList.add(comp);
            rowMap.put(level, rowList);
        } else {
            List parentRowList = (List) rowMap.get(level - 1);
            int myParent = parentRowList.indexOf(parent);
            for (int i = rowList.size() - 1; i >= 0; i--) {
                // add the child to the row after any of his brothers
                int pos = getIndexOfParent((Component) rowList.get(i));
                if (pos <= myParent) {
                    rowList.add(i + 1, comp);
                    break;
                }
                if (i == 0) {
                    rowList.add(i, comp);
                }
            }
        }

        // place comp
        Dimension dimension = comp.getPreferredSize();
        comp.setBounds(pt.x, pt.y, dimension.width, dimension.height);

        // update row height
        if (level >= rowheight.length) {
            int[] temp = new int[level + 10];
            switch (direction) {
                case BOTTOM_TOP:
                case RIGHT_LEFT:
                    Arrays.fill(temp, Integer.MAX_VALUE);
                    break;
                default:
                    Arrays.fill(temp, Integer.MIN_VALUE);
            }
            System.arraycopy(rowheight, 0, temp, 0, rowheight.length);
            rowheight = temp;
        }

        switch (direction) {
            case LEFT_RIGHT:
                if (pt.x + dimension.width + hgap > rowheight[level]) {
                    rowheight[level] = pt.x + dimension.width + hgap;
                }
                break;
            case RIGHT_LEFT:
                if (pt.x < rowheight[level]) {
                    rowheight[level] = pt.x;
                }
                break;
            case BOTTOM_TOP:
                if (pt.y < rowheight[level]) {
                    rowheight[level] = pt.y;
                }
                break;
            default:
                if (pt.y + dimension.height + vgap > rowheight[level]) {
                    rowheight[level] = pt.y + dimension.height + vgap;
                }
        }
    }

    /****************************************************************************************************
     * @note is may be needed to determine that the shift was not far enough due to the differences in
     *       the sizes of the components and move the component and little bit extra, sending the value
     *       recursively.
     ***************************************************************************************************/
    private void moveComponent(Component component, int distance) {
        switch (direction) {
            case LEFT_RIGHT:
            case RIGHT_LEFT:
                component.setLocation(component.getX(), component.getY() + distance);
                break;
            default:
                component.setLocation(component.getX() + distance, component.getY());
        }
        int centerParent = getParentOffset(component);
        if (centerParent > 0) {
            // we will have to move the parent, then move his neighbors. If the
            // neighbor has children, just center him above his kids
            HierarchyConstraints constraints = (HierarchyConstraints) constraintsMap.get(component);
            List rowList = (List) rowMap.get(constraints.getLevel() - 1);
            int index = rowList.indexOf(constraints.getParentNode());
            Component tmpComponent = null;
            HierarchyConstraints tmpConstraints = null;
            for (int i = rowList.size() - 1; i > index; i--) {
                tmpComponent = (Component) rowList.get(i);
                tmpConstraints = (HierarchyConstraints) constraintsMap.get(tmpComponent);

                if (tmpConstraints.getChildren().length > 0) {
                    moveComponent(tmpComponent, getParentOffset(tmpComponent));
                } else {
                    moveComponent(tmpComponent, centerParent); // else just move him to the same as the other
                }
            }
            moveComponent(constraints.getParentNode(), centerParent);
        }
    }

    /****************************************************************************************************
     * @return the index of the specified comp's parent in the parent's row.
     ***************************************************************************************************/
    private int getIndexOfParent(Component comp) {
        HierarchyConstraints childConstraints = (HierarchyConstraints) constraintsMap.get(comp);
        Component parent = childConstraints.getParentNode();
        List parentRow = (List) rowMap.get(childConstraints.getLevel() - 1);
        return parentRow.indexOf(parent);
    }

    /****************************************************************************************************
     * @return zero if the parent is centered, and amount other than zero if the parent is not centered.
     ***************************************************************************************************/
    private int getParentOffset(Component comp) {
        HierarchyConstraints childConstraints = (HierarchyConstraints) constraintsMap.get(comp);
        Component parent = childConstraints.getParentNode();
        if (parent != null) {
            HierarchyConstraints parentConstraints = (HierarchyConstraints) constraintsMap.get(parent);
            Component[] children = parentConstraints.getChildren();
            if (children.length > 0) {
                Component leftMost = children[0];
                Component rightMost = children[children.length - 1];

                switch (direction) {
                    case LEFT_RIGHT:
                    case RIGHT_LEFT:
                        return (leftMost.getY() + rightMost.getY()) / 2 - parent.getY();
                    default:
                        return (leftMost.getX() + rightMost.getX()) / 2 - parent.getX();
                }
            }
        }
        return 0;
    }
}
