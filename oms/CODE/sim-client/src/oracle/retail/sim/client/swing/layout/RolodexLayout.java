package oracle.retail.sim.client.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager2;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/********************************************************************************************************
 * Rolodex Layout
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RolodexLayout implements LayoutManager2, Serializable {
    private static final long serialVersionUID = -9111435568088947093L;

    private Map cardMap = new HashMap<>();
    private int hgap;
    private int vgap;

    /********************************************************************************************************
     * Constructor
     *******************************************************************************************************/
    public RolodexLayout() {
        this(0, 0);
    }

    /********************************************************************************************************
     * Creates a new rolodex layout with the specified horizontal and vertical gaps.
     *
     * @param hgap
     * @param vgap
     *******************************************************************************************************/
    public RolodexLayout(int hgap, int vgap) {
        this.hgap = hgap;
        this.vgap = vgap;
    }

    /********************************************************************************************************
     * @return The horizontal gap between components.
     *******************************************************************************************************/
    public int getHoriztonalGap() {
        return hgap;
    }

    /********************************************************************************************************
     * Sets the horizontal gap between components.
     *
     * @param hgap the horizontal gap between components.
     *******************************************************************************************************/
    public void setHoriztonalGap(int hgap) {
        this.hgap = hgap;
    }

    /********************************************************************************************************
     * @return the vertical gap between components.
     *******************************************************************************************************/
    public int getVerticalGap() {
        return vgap;
    }

    /********************************************************************************************************
     * Sets the vertical gap between components.
     *
     * @param vgap the vertical gap between components.
     *******************************************************************************************************/
    public void setVerticalGap(int vgap) {
        this.vgap = vgap;
    }

    /********************************************************************************************************
     * Adds the specified component with the specified name to the layout.
     *
     * @param name the component name
     * @param comp the component to be added
     *******************************************************************************************************/
    public void addLayoutComponent(String name, Component comp) {
        addLayoutComponent(comp, name);
    }

    /********************************************************************************************************
     * Adds the specified component to this layout's internal hashtable. The
     *
     * @param comp the component to be added.
     * @param constraints a tag that identifies a particular card in the layout.
     *******************************************************************************************************/
    public void addLayoutComponent(Component comp, Object constraints) {
        synchronized (comp.getTreeLock()) {
            if (cardMap.size() > 0) {
                comp.setVisible(false);
            }
            cardMap.put(constraints, comp);
        }
    }

    /********************************************************************************************************
     * Removes the specified component from the layout.
     *
     * @param comp the component to be removed.
     *******************************************************************************************************/
    public void removeLayoutComponent(Component comp) {
        synchronized (comp.getTreeLock()) {
            Object key = null;
            for (Iterator iterator = cardMap.keySet().iterator(); iterator.hasNext();) {
                key = iterator.next();

                if (cardMap.get(key) == comp) {
                    cardMap.remove(key);
                }
            }
        }
    }

    /********************************************************************************************************
     * Determines the preferred size of the container argument using this layout.
     *
     * @param parent the name of the parent container.
     * @return the preferred dimensions to lay out the subcomponents
     *******************************************************************************************************/
    public Dimension preferredLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = 0;
            int height = 0;
            Component component = null;
            Dimension dim = null;
            for (int i = parent.getComponentCount() - 1; i >= 0; i--) {
                component = parent.getComponent(i);
                dim = component.getPreferredSize();
                if (dim.width > width) {
                    width = dim.width;
                }
                if (dim.height > height) {
                    height = dim.height;
                }
            }
            width = width + insets.left + insets.right;
            height = height + insets.top + insets.bottom;
            return new Dimension(width + hgap * 2, height + vgap * 2);
        }
    }

    /********************************************************************************************************
     * Calculates the minimum size for the specified panel.
     *
     * @param parent the name of the parent container
     * @return the minimum dimensions required to lay out the subcomponents.
     *******************************************************************************************************/
    public Dimension minimumLayoutSize(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            int width = 0;
            int height = 0;
            Component component = null;
            Dimension dim = null;
            for (int i = parent.getComponentCount() - 1; i >= 0; i--) {
                component = parent.getComponent(i);
                dim = component.getMinimumSize();
                if (dim.width > width) {
                    width = dim.width;
                }
                if (dim.height > height) {
                    height = dim.height;
                }
            }
            width = width + insets.left + insets.right;
            height = height + insets.top + insets.bottom;
            return new Dimension(width + hgap * 2, height + vgap * 2);
        }
    }

    /********************************************************************************************************
     * Returns the maximum dimensions for this layout.
     *
     * @param target the component which needs to be laid out
     *******************************************************************************************************/
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /********************************************************************************************************
     * Returns the alignment along the x axis..
     *******************************************************************************************************/
    public float getLayoutAlignmentX(Container parent) {
        return 0.5f;
    }

    /********************************************************************************************************
     * Returns the alignment along the y axis.
     *******************************************************************************************************/
    public float getLayoutAlignmentY(Container parent) {
        return 0.5f;
    }

    /********************************************************************************************************
     * Invalidates the layout. This method is not used.
     *******************************************************************************************************/
    public void invalidateLayout(Container target) {
    }

    /********************************************************************************************************
     * Lays out the specified container using this rolodex layout. Each component in the
     * <code>parent</code> container is reshaped to be the size of the container, minus space for
     * surrounding insets, horizontal gaps, and vertical gaps.
     *
     * @param parent the name of the parent container
     *******************************************************************************************************/
    public void layoutContainer(Container parent) {
        synchronized (parent.getTreeLock()) {
            Insets insets = parent.getInsets();
            Component component = null;
            for (int i = parent.getComponentCount() - 1; i >= 0; i--) {
                component = parent.getComponent(i);

                if (component.isVisible()) {
                    component.setBounds(hgap + insets.left, vgap + insets.top, parent.getWidth() - (hgap * 2 + insets.left + insets.right),
                            parent.getHeight() - (vgap * 2 + insets.top + insets.bottom));
                }
            }
        }
    }

    /********************************************************************************************************
     * Make sure that the Container really has a RolodexLayout installed.
     *******************************************************************************************************/
    void checkLayout(Container parent) {
        if (parent.getLayout() != this) {
            throw new IllegalArgumentException("Unable to use this container with a rolodex layout!");
        }
    }

    /********************************************************************************************************
     * @param parent the name of the parent container in which to do the layout.
     * @return the current component
     *******************************************************************************************************/
    public Component getCurrent(Container parent) {
        checkLayout(parent);
        Component component = null;
        for (int i = parent.getComponentCount() - 1; i > 0; i--) {
            component = parent.getComponent(i);

            if (component.isVisible()) {
                return component;
            }
        }
        return parent.getComponentCount() > 0 ? parent.getComponent(0) : null;
    }

    /********************************************************************************************************
     * @param constraints the object that keys this current component
     * @return the current component
     *******************************************************************************************************/
    public Component getCard(Object constraints) {
        return (Component) cardMap.get(constraints);
    }

    /********************************************************************************************************
     * Flips to the first card of the container.
     *
     * @param parent the name of the parent container in which to do the layout.
     * @return the first component
     *******************************************************************************************************/
    public Component first(Container parent) {
        synchronized (parent.getTreeLock()) {
            checkLayout(parent);
            Component component = null;
            for (int i = parent.getComponentCount() - 1; i > 0; i--) {
                component = parent.getComponent(i);

                if (component.isVisible()) {
                    component.setVisible(false);
                    component = parent.getComponent(0);
                    component.setVisible(true);
                    parent.validate();
                    return component;
                }
            }
            return parent.getComponent(0);
        }
    }

    /********************************************************************************************************
     * Flips to the next card of the specified container.
     *
     * @param parent the name of the parent container in which to do the layout.
     * @return the next component
     *******************************************************************************************************/
    public Component next(Container parent) {
        synchronized (parent.getTreeLock()) {
            checkLayout(parent);
            int count = parent.getComponentCount();
            Component component = null;
            for (int i = 0; i < count; i++) {
                component = parent.getComponent(i);

                if (component.isVisible()) {
                    component.setVisible(false);
                    component = parent.getComponent(i + 1 < count ? i + 1 : 0);
                    component.setVisible(true);
                    parent.validate();
                    return component;
                }
            }
            return null;
        }
    }

    /********************************************************************************************************
     * Flips to the previous card of the specified container.
     *
     * @param parent the name of the parent container in which to do the layout.
     * @return the previous component
     *******************************************************************************************************/
    public Component previous(Container parent) {
        synchronized (parent.getTreeLock()) {
            checkLayout(parent);
            int count = parent.getComponentCount();
            Component component = null;
            for (int i = count - 1; i >= 0; i--) {
                component = parent.getComponent(i);

                if (component.isVisible()) {
                    component.setVisible(false);
                    component = parent.getComponent(i > 0 ? i - 1 : count - 1);
                    component.setVisible(true);
                    parent.validate();
                    return component;
                }
            }
            return null;
        }
    }

    /********************************************************************************************************
     * Flips to the last card of the container.
     *
     * @param parent the name of the parent container in which to do the layout.
     * @return the last component
     *******************************************************************************************************/
    public Component last(Container parent) {
        synchronized (parent.getTreeLock()) {
            checkLayout(parent);
            int count = parent.getComponentCount();
            Component component = null;
            for (int i = 0; i < count; i++) {
                component = parent.getComponent(i);

                if (component.isVisible()) {
                    component.setVisible(false);
                    component = parent.getComponent(count - 1);
                    component.setVisible(true);
                    parent.validate();
                    return component;
                }
            }
            return count > 0 ? parent.getComponent(count - 1) : null;
        }
    }

    /********************************************************************************************************
     * Flips to the component that was added to this layout with the specified <code>name</code>,
     * using <code>addLayoutComponent</code>.
     *
     * @param parent the name of the parent container in which to do the layout.
     * @param name the component name.
     * @return the component to show
     *******************************************************************************************************/
    public Component show(Container parent, Object constaints) {
        synchronized (parent.getTreeLock()) {
            checkLayout(parent);
            Component next = (Component) cardMap.get(constaints);
            if (next != null && !next.isVisible()) {
                int ncomponents = parent.getComponentCount();
                Component component = null;
                for (int i = 0; i < ncomponents; i++) {
                    component = parent.getComponent(i);

                    if (component.isVisible()) {
                        component.setVisible(false);
                        break;
                    }
                }
                next.setVisible(true);
                parent.validate();
            }
            return next;
        }
    }

    /********************************************************************************************************
     * @return a string representation of this rolodex layout.
     *******************************************************************************************************/
    public String toString() {
        StringBuilder buffer = new StringBuilder(getClass().getName());
        for (Iterator iterator = cardMap.keySet().iterator(); iterator.hasNext();) {
            buffer.append(iterator.next());
        }
        return buffer.toString();
    }
}
