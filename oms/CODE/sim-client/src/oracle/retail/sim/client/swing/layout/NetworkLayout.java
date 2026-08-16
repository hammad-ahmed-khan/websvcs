package oracle.retail.sim.client.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import javax.swing.SwingConstants;

/********************************************************************************************************
 * Layout manager that will place components that are in a hierarchy into a circular layout where each
 * node within its circle of siblings that has children will have its radius increased.
 *
 * WARNING: This class tries to keep circles from overlapping, but it is not yet fool-proof. It is
 * possible to create overlapping circles by adding children at every right angle and the circles will go
 * back onto themselves. As a word around, try only adding children to children that face away from their
 * parents, which would be the middle of the list.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class NetworkLayout extends CircleTreeLayout {
    private static final long serialVersionUID = -926035382062826148L;

    private Map circleMap = new HashMap<>();

    /********************************************************************************************************
     * Default constructor. Will set this layout to space components 10 pixels apart and anchor the graph
     * in the center.
     *******************************************************************************************************/
    public NetworkLayout() {
        super(10, SwingConstants.CENTER, 10);
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    public NetworkLayout(int minDistance) {
        super(minDistance, SwingConstants.CENTER, 10);
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    public NetworkLayout(int minDistance, int anchor) {
        super(minDistance, anchor, 10);
    }

    /********************************************************************************************************
     *******************************************************************************************************/
    public NetworkLayout(int minDistance, int anchor, int minRadius) {
        super(minDistance, anchor, minRadius);
    }

    /********************************************************************************************************
     * Invalidates the layout, indicating that if the layout manager has cached information it should be
     * discarded. This routine clears an inner cache of known circles.
     * @param target
     *******************************************************************************************************/
    public void invalidateLayout(Container target) {
        circleMap.clear();
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

            root.setBounds(0, 0, rootDim.width, rootDim.height);

            // recursively place all children
            placeChildren(root, 0, 0, 0);

            // move child circles off of parent circles
            expandCircles((Circle) circleMap.get(root));

            // position graph properly within target
            anchorComponents(target);
        }
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
     * @return whether any children were placed
     *******************************************************************************************************/
    private void placeChildren(Component parent, double radiusOfParent, double angleAllowedForParent, double angleParentPlacedAt) {
        // get children for root
        HierarchyConstraints cons = getConstraints(parent);
        Component[] list = cons.getChildren();
        int numNodes = list.length;
        // define a circle
        if (numNodes > 0) {
            // calculate angle between each child node
            double angleForEachChild = 2 * Math.PI / numNodes;
            // subtract arc facing parent
            if (radiusOfParent > 0) {
                angleForEachChild -= angleForEachChild / numNodes;
            }

            double radius = numNodes == 1 ? getMinRadius() : getDesiredRadius(angleForEachChild);
            /*
             * the distance of the child away from the parent is the radius plus a calculated distance
             * dependant on the size of the parent. this is the distance between 0,0 and the
             * parent.getWidth,parent.getHeight
             */
            radius += getAdditionalDistanceAcrossParentAndChildren(parent);

            // add defined circle to map
            circleMap.put(parent, new Circle(parent, radius, angleParentPlacedAt));
            // place inner circle
            for (int i = 0; i < numNodes; i++) {
                Component comp = list[i];
                Dimension dim = comp.getPreferredSize();
                /*
                 * calculate combined angle. do not allow children of this circle to be placed in arc
                 * directly facing the parent of this circle. Put first child facing away from
                 * grandparent and add one to each side afterwards.
                 */
                double theta = angleForEachChild * i + angleParentPlacedAt - angleForEachChild * numNodes / 2 + angleForEachChild / 2;
                Point point = findPointAtCircumference(parent.getLocation(), radius, theta);
                comp.setBounds(point.x, point.y, dim.width, dim.height);
                placeChildren(comp, radius, angleForEachChild, theta);
            }
        }
    }

    /********************************************************************************************************
     * Move any child circles away from this parent circle at least 1/2 * <code>min_distance</code>.
     *******************************************************************************************************/
    private void expandCircles(Circle parentCircle) {
        if (parentCircle != null) {
            HierarchyConstraints cons = getConstraints(parentCircle.getComp());
            Component[] children = cons.getChildren();
            for (int i = children.length - 1; i >= 0; i--) {
                Circle circle = (Circle) circleMap.get(children[i]);
                if (circle != null && circle.isWithin(parentCircle)) {
                    double d = circle.getRadius() + parentCircle.getRadius(); // + getMinDistance()/2
                    double x = d * Math.sin(circle.getTheta());
                    double y = -d * Math.cos(circle.getTheta());
                    circle.translate((int) x, (int) y);
                    expandCircles(circle);
                }
            }
        }
    }

    /********************************************************************************************************
     * Retrieve the maximum length, whether it be the parent or its immediate children, accross said
     * components from its top left-most corner to it bottom right-most corner.
     *
     * @param parent component which is the center of the circle
     *******************************************************************************************************/
    private double getAdditionalDistanceAcrossParentAndChildren(Component parent) {
        Point p1 = new Point();
        Point p2 = new Point(parent.getWidth(), parent.getHeight());
        double len = calculateDistance(p1, p2);
        HierarchyConstraints cons = getConstraints(parent);
        Component[] childs = cons.getChildren();
        for (int i = childs.length - 1; i >= 0; i--) {
            p2.x = childs[i].getPreferredSize().width;
            p2.y = childs[i].getPreferredSize().height;
            double result = calculateDistance(p1, p2);
            if (result > len) {
                len = result;
            }
        }
        return len;
    }

    /********************************************************************************************************
     * @return the algebraic distance between these two points
     *******************************************************************************************************/
    public static double calculateDistance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2));
    }

    /********************************************************************************************************
     *
     * CIRCLE
     *
     *******************************************************************************************************/

    private class Circle {
        Component circleComp;
        double radius;
        double theta;

        /**
         * @param comp component that is center of circle
         * @param radius one half the width of the circle
         * @param theta the angle at which this circle's owner exists
         */
        public Circle(Component comp, double radius, double theta) {
            circleComp = comp;
            this.radius = radius;
            this.theta = theta;
        }

        Component getComp() {
            return circleComp;
        }

        double getRadius() {
            return radius;
        }

        double getTheta() {
            return theta;
        }

        Point getCenter() {
            return circleComp.getLocation();
        }

        /**
         * Calculate the distance between the two circles and compare to the two radii of the two
         * circles.
         *
         * @return true if distance less than radii plus <code>min_distance</code>
         */
        boolean isWithin(Circle circle) {
            if (circle.equals(this)) {
                return false;
            }
            // calculate distance between the two centers:
            double distance = calculateDistance(circle.getCenter(), getCenter());
            // if aCircle.radius + this.radius + min_distance > distance
            return circle.getRadius() + radius > distance; // + getMinDistance()/2
        }

        /**
         * move this component and all children in its circle specified distance
         */
        void translate(int x, int y) {
            Point point = null;
            point = circleComp.getLocation();
            point.translate(x, y);
            circleComp.setLocation(point);
            HierarchyConstraints cons = getConstraints(circleComp);
            Component[] children = cons.getChildren();
            for (int i = children.length - 1; i >= 0; i--) {
                Circle child = (Circle) circleMap.get(children[i]);
                if (child != null) {
                    child.translate(x, y);
                } else {
                    point = children[i].getLocation();
                    point.translate(x, y);
                    children[i].setLocation(point);
                }
            }
        }
    }
}
