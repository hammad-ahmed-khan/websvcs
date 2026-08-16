package oracle.retail.sim.client.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.SwingConstants;

/********************************************************************************************************
 * Layout manager that will place components that are in a hierarchy into a circular layout where each
 * group of children can only exist in the angle owned by its parent. The readius of the children will
 * expand until they can fit properly within the angle dependant on the minimum distance required between
 * components.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CircleTreeLayout extends HierarchyLayout {
    private static final long serialVersionUID = -4209721878893639212L;

    /** The minimum distance between nodes */
    private int minDistance;

    /** The minimum distance of the radius between levels */
    private double minRadius;

    /** Direct the graph should anchor: CMSSwingConstants.CENTER, CMSSwingConstants.NORTH_EAST */
    private int anchor;

    /********************************************************************************************************
     * Default constructor. Will set this layout to space components 40 pixels apart and anchor the graph
     * in the center.
     *******************************************************************************************************/
    public CircleTreeLayout() {
        this(40, SwingConstants.CENTER, 40);
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    public CircleTreeLayout(int minDistance) {
        this(minDistance, SwingConstants.CENTER, 40);
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    public CircleTreeLayout(int minDistance, int anchor) {
        this(minDistance, anchor, 40);
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    public CircleTreeLayout(int minDistance, int anchor, int minRadius) {
        this.minDistance = minDistance;
        this.anchor = anchor;
        this.minRadius = minRadius;
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    public void setMinRadius(double minRadius) {
        this.minRadius = minRadius;
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    public void setAnchor(int anchor) {
        this.anchor = anchor;
    }

    /********************************************************************************************************
     * Gets the minimum radius.
     *******************************************************************************************************/
    public double getMinRadius() {
        return minRadius;
    }

    /********************************************************************************************************
     * Gets the minimum distance.
     *******************************************************************************************************/
    public double getMinDistance() {
        return minDistance;
    }

    /********************************************************************************************************
     * Gets the anchor.
     *******************************************************************************************************/
    public int getAnchor() {
        return anchor;
    }

    /********************************************************************************************************
     * Determines the preferred size of the container argument using this layout.
     * <p>
     * The preferred width of this layout will be the diameter of the circle created by all the children
     * plus the size of the outer children plus the <code>min_distance</code>. This only owrks after
     * comps have been placed.
     *
     * @param target the container in which to do the layout.
     * @return the preferred dimensions needed to lay out the subcomponents of the specified container.
     *******************************************************************************************************/
    public Dimension preferredLayoutSize(Container target) {
        Point minPoint = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
        Point maxPoint = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);

        Component[] comps = target.getComponents();
        for (Component comp : comps) {
            Point p = comp.getLocation();
            if (p.x < minPoint.x) {
                minPoint.x = p.x;
            }
            if (p.y < minPoint.y) {
                minPoint.y = p.y;
            }

            if (p.x + comp.getWidth() > maxPoint.x) {
                maxPoint.x = p.x + comp.getWidth();
            }
            if (p.y + comp.getHeight() > maxPoint.y) {
                maxPoint.y = p.y + comp.getHeight();
            }
        }
        Dimension preferredSize = new Dimension(maxPoint.x + 2 * minDistance, maxPoint.y + 2 * minDistance);
        if (minPoint.x < minDistance) {
            preferredSize.width -= minPoint.x;
        }
        if (minPoint.y < minDistance) {
            preferredSize.height -= minPoint.y;
        }
        return preferredSize;
    }

    /********************************************************************************************************
     * Lays out the specified container using this layout.
     *
     * @param target the container in which to do the layout.
     *******************************************************************************************************/
    public void layoutContainer(Container target) {
        synchronized (target.getTreeLock()) {
            int ncomponents = target.getComponentCount();
            if (ncomponents == 0) {
                return;
            }

            // first we need to clear all cached information
            invalidateLayout(target);

            // the first component is the root
            Component root = target.getComponent(0);
            Dimension rootDim = root.getPreferredSize();
            Dimension sizeDim = target.getSize();

            root.setBounds(sizeDim.width / 2, sizeDim.height / 2, rootDim.width, rootDim.height);

            // get children for root
            HierarchyConstraints cons = getConstraints(root);
            Component[] list = cons.getChildren();
            int numNodes = list.length;
            if (numNodes > 0) {
                // calculate angle between each child node
                double angleForEachChild = 2 * Math.PI / numNodes;
                double radius = numNodes == 1 ? minRadius : getDesiredRadius(angleForEachChild);
                // place inner circle
                Component component = null;
                Dimension dim = null;
                double theta = 0;
                Point point = null;

                for (int i = 0; i < numNodes; i++) {
                    component = list[i];
                    dim = component.getPreferredSize();
                    theta = angleForEachChild * i; // calculate combined/ angle
                    point = findPointAtCircumference(root.getLocation(), radius, theta);
                    component.setBounds(point.x, point.y, dim.width, dim.height);
                    placeChildren(component, root.getLocation(), radius, angleForEachChild, theta);
                }
            }
            anchorComponents(target);
        }
    }

    /********************************************************************************************************
     * Locate a point on the perimeter of a circle given the center of the circle and its radius and the
     * angle at which the point resides. Given a = <code>center.x</code> and b =<code>center.y</code>
     * and r = radius the point would be like (a + r * sin(theta), b - r * cos(theta))
     *
     * @param center the center of the circle
     * @param radius the radius (diameter / 2) of the circle
     * @param theta the angle at which the point is found
     *******************************************************************************************************/
    protected Point findPointAtCircumference(Point center, double radius, double theta) {
        Point point = new Point(center);
        point.translate((int) (radius * Math.sin(theta)), (int) (-radius * Math.cos(theta)));
        return point;
    }

    /********************************************************************************************************
     * Invalidates the layout, indicating that if the layout manager has cached information it should be
     * discarded. Note: this method does not call super and does nothing.
     *
     * @param target
     *******************************************************************************************************/
    public void invalidateLayout(Container target) {
    }

    /********************************************************************************************************
     * Method should eliminate the comp specified and all its children...
     *
     * @param component
     *******************************************************************************************************/
    public void removeLayoutComponent(Component component) {
        HierarchyConstraints constraints = getConstraints(component);
        if (constraints != null) {
            HierarchyConstraints parentConstraints = getConstraints(constraints.getParentNode());
            if (parentConstraints != null) {
                parentConstraints.removeChild(component);
            }
        }
        super.removeLayoutComponent(component);
    }

    /********************************************************************************************************
     * Place all children of specified parent. This method will recursively call itself until all
     * children have been placed.
     *
     * @param parent the hierarchal owner with children to place
     * @param center the center of the circle to place comps around
     * @param radiusOfParent the radius to the parent
     * @param angleAllowedForParent sub-angle which the parent owns in the circle
     * @param angleParentPlacedAt actual location when parent was placed
     *******************************************************************************************************/
    private void placeChildren(Component parent, Point center, double radiusOfParent, double angleAllowedForParent, double angleParentPlacedAt) {
        HierarchyConstraints cons = getConstraints(parent);
        Component[] list = cons.getChildren();
        int numNodes = list.length;
        if (numNodes > 0) {

            // and space between node (theta)
            double theta = angleAllowedForParent / (numNodes + 1);

            // determine radius to use at this level
            double radius = getDesiredRadius(theta);
            if (radius < radiusOfParent + minRadius) {
                radius = radiusOfParent + minRadius;
            }

            Component component = null;
            Dimension dim = null;
            Point point = null;

            // determine angle at which to start placing these comps
            double currentAngle = angleParentPlacedAt - angleAllowedForParent / 2;

            for (int i = 0; i < numNodes; i++) {
                currentAngle = currentAngle + theta;
                component = list[i];
                dim = component.getPreferredSize();
                point = findPointAtCircumference(center, radius, currentAngle);
                component.setBounds(point.x, point.y, dim.width, dim.height);
                placeChildren(component, center, radius, theta, currentAngle);
            }
        }
    }

    /********************************************************************************************************
     * Anchors the specified container to the specified direction.
     * <p>
     * ONLY SUPPORTS SwingConstants.CENTER, SwingConstants.NORTH_EAST CURRENTLY
     *
     * @param target the container in which to do the layout.
     * @param anchor the direction
     *******************************************************************************************************/
    protected void anchorComponents(Container target) {
        Component[] comps = target.getComponents();
        switch (anchor) {
            case SwingConstants.NORTH_EAST:
                Point distanceFromOrigin = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
                for (int i = comps.length - 1; i >= 0; i--) {
                    Point point = comps[i].getLocation();
                    if (point.x < distanceFromOrigin.x) {
                        distanceFromOrigin.x = point.x;
                    }
                    if (point.y < distanceFromOrigin.y) {
                        distanceFromOrigin.y = point.y;
                    }
                }
                // out actual intent is to place NE comp at (min_distance, min_distance)
                distanceFromOrigin.translate(-minDistance, -minDistance);
                // move comps
                moveComponents(comps, -distanceFromOrigin.x, -distanceFromOrigin.y);
                break;
            case SwingConstants.CENTER:
                Point furthestFromOrigin = new Point(Integer.MAX_VALUE, Integer.MAX_VALUE);
                Point furthestFromBounds = new Point(Integer.MIN_VALUE, Integer.MIN_VALUE);
                for (int i = comps.length - 1; i >= 0; i--) {
                    Point point = comps[i].getLocation();
                    if (point.x < furthestFromOrigin.x) {
                        furthestFromOrigin.x = point.x;
                    }
                    if (point.y < furthestFromOrigin.y) {
                        furthestFromOrigin.y = point.y;
                    }
                    //
                    if (point.x + comps[i].getWidth() > furthestFromBounds.x) {
                        furthestFromBounds.x = point.x + comps[i].getWidth();
                    }
                    if (point.y + comps[i].getHeight() > furthestFromBounds.y) {
                        furthestFromBounds.y = point.y + comps[i].getHeight();
                    }
                }
                Point middle = new Point(target.getWidth() / 2, target.getHeight() / 2);
                middle.translate(-(furthestFromBounds.x + furthestFromOrigin.x) / 2, -(furthestFromBounds.y + furthestFromOrigin.y) / 2);
                // move comps
                moveComponents(comps, middle.x, middle.y);
            default:
                break;
        }
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    protected void moveComponents(Component[] comps, int x, int y) {
        for (int i = comps.length - 1; i >= 0; i--) {
            Point p = comps[i].getLocation();
            p.translate(x, y);
            comps[i].setLocation(p);
        }
    }

    /********************************************************************************************************
     * Return <code>alpha</code>, which is the separation needed between non- related nodes to
     * designate that they are non-related, calculated on the basis of <code>min_distance</code>. If
     * <code>min_distance</code> never changes, <code>alpha</code> will always be the same.
     *******************************************************************************************************/
    protected double getAlpha(double totalRadius) {
        return 2 * Math.asin(minDistance / (2 * totalRadius));
    }

    /********************************************************************************************************
     * Return the radius desired for the specified angle. If the radus is smaller than the
     * <code>min_distance</code>, return <code>min_distance</code>.
     *******************************************************************************************************/
    protected double getDesiredRadius(double theta) {
        double radius = minDistance / (2 * Math.sin(theta / 2.0));
        return radius < minRadius ? minRadius : radius;
    }
}
