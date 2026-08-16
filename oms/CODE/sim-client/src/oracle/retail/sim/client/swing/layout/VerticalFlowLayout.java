package oracle.retail.sim.client.swing.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.io.Serializable;
import javax.swing.SwingConstants;

/******************************************************************************************
 * This class will allow components to be placed and weighted vertically within a container.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class VerticalFlowLayout implements LayoutManager, Serializable {
    private static final long serialVersionUID = 3317188714143822451L;

    /** VerticalFlowLayout components will be added above existing ones */
    public static final int ASCENDING = 0;

    /** VerticalFlowLayout components will be added below exsisting ones */
    public static final int DESCENDING = 1;

    private int align;
    private int order;

    /** Gap between each componetnt */
    private int hgap;
    private int vgap;

    /** Flag whether a component will stretch to the layout's edge */
    private boolean hfill;
    private boolean vfill;

    /******************************************************************************************
     * Constructors
     ******************************************************************************************/
    public VerticalFlowLayout() {
        this(SwingConstants.TOP, DESCENDING, 5, 5, true, false);
    }

    public VerticalFlowLayout(boolean hfill, boolean vfill) {
        this(SwingConstants.TOP, DESCENDING, 5, 5, hfill, vfill);
    }

    public VerticalFlowLayout(int align) {
        this(align, DESCENDING, 5, 5, true, false);
    }

    public VerticalFlowLayout(int align, boolean hfill, boolean vfill) {
        this(align, DESCENDING, 5, 5, hfill, vfill);
    }

    public VerticalFlowLayout(int align, int order, boolean hfill, boolean vfill) {
        this(align, order, 5, 5, hfill, vfill);
    }

    /******************************************************************************************
     * Construct a new VerticalFlowLayout.
     * <p>
     * @param align the alignment value
     * @param order order components will be added
     * @param hgap the horizontal gap variable
     * @param vgap the vertical gap variable
     * @param fill the fill to edge flag
     ******************************************************************************************/
    public VerticalFlowLayout(int align, int order, int hgap, int vgap, boolean hfill, boolean vfill) {
        this.align = align;
        this.order = order;
        this.hgap = hgap;
        this.vgap = vgap;
        this.hfill = hfill;
        this.vfill = vfill;
    }

    /******************************************************************************************
     * Adds the specified component to the layout. Not used by this class.
     * <P>
     * @param name The name of the component
     * @param comp The component to be added
     ******************************************************************************************/
    public void addLayoutComponent(String name, Component comp) {
    }

    /******************************************************************************************
     * Removes the specified component from the layout. Not used by this class.
     * <p>
     * @param comp the component to remove
     ******************************************************************************************/
    public void removeLayoutComponent(Component comp) {
    }

    /******************************************************************************************
     * Returns the preferred dimensions given the components in the target container.
     * <p>
     * @param target The component to lay out
     ******************************************************************************************/
    public Dimension preferredLayoutSize(Container target) {
        Dimension dimension = new Dimension(0, 0);
        Dimension tempDim = null;
        Component component = null;
        for (int i = 0; i < target.getComponentCount(); i++) {
            component = target.getComponent(i);

            if (component.isVisible()) {
                tempDim = component.getPreferredSize();
                dimension.width = Math.max(dimension.width, tempDim.width);
                if (i > 0) {
                    dimension.height = dimension.height + vgap;
                }
                dimension.height = dimension.height + tempDim.height;
            }
        }
        Insets insets = target.getInsets();
        dimension.width = dimension.width + insets.left + insets.right + hgap * 2;
        dimension.height = dimension.height + insets.top + insets.bottom + vgap * 2;
        return dimension;
    }

    /******************************************************************************************
     * Returns the minimum size needed to layout the target container
     * <p>
     * @param target The component to lay out
     ******************************************************************************************/
    public Dimension minimumLayoutSize(Container target) {
        Dimension dimension = new Dimension(0, 0);
        Dimension tempDim = null;
        Component component = null;
        for (int i = 0; i < target.getComponentCount(); i++) {
            component = target.getComponent(i);

            if (component.isVisible()) {
                tempDim = component.getMinimumSize();
                dimension.width = Math.max(dimension.width, tempDim.width);
                if (i > 0) {
                    dimension.height = dimension.height + vgap;
                }
                dimension.height = dimension.height + tempDim.height;
            }
        }
        Insets insets = target.getInsets();
        dimension.width = dimension.width + insets.left + insets.right + hgap * 2;
        dimension.height = dimension.height + insets.top + insets.bottom + vgap * 2;
        return dimension;
    }

    /******************************************************************************************
     * Getters/Setters
     ******************************************************************************************/

    public int getAlignment() {
        return align;
    }

    public void setAlignment(int align) {
        this.align = align;
    }

    public int getHoriztonalGap() {
        return hgap;
    }

    public void setHorizontalGap(int hgap) {
        this.hgap = hgap;
    }

    public int getVerticalGap() {
        return vgap;
    }

    public void setVerticalGap(int vgap) {
        this.vgap = vgap;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setVerticalFill(boolean vfill) {
        this.vfill = vfill;
    }

    public boolean getVerticalFill() {
        return vfill;
    }

    public void setHorizontalFill(boolean hfill) {
        this.hfill = hfill;
    }

    public boolean getHorizontalFill() {
        return hfill;
    }

    /******************************************************************************************
     * Lays out the container.
     * <p>
     * @param target the container to lay out.
     ******************************************************************************************/
    public void layoutContainer(Container target) {
        Insets insets = target.getInsets();
        int maxheight = target.getSize().height - (insets.top + insets.bottom + vgap * 2);
        int maxwidth = target.getSize().width - (insets.left + insets.right + hgap * 2);
        int numcomponents = target.getComponentCount();
        int x = insets.left + hgap;
        int y = 0;
        int colwidth = 0;
        int start = 0;
        Component component = null;
        for (int i = 0; i < numcomponents; i++) {
            component = target.getComponent(i);

            if (component.isVisible()) {
                Dimension tempDim = component.getPreferredSize();
                // fit last component to remaining height
                if (vfill && i == numcomponents - 1) {
                    tempDim.height = Math.max(maxheight - y, component.getPreferredSize().height);
                }
                // fit componenent size to container width
                if (hfill) {
                    component.setSize(maxwidth, tempDim.height);
                    tempDim.width = maxwidth;
                } else {
                    component.setSize(tempDim.width, tempDim.height);
                }
                if (y + tempDim.height > maxheight) {
                    placeComponents(target, x, insets.top + vgap, colwidth, maxheight - y, start, i);
                    y = tempDim.height;
                    x = x + hgap + colwidth;
                    colwidth = tempDim.width;
                    start = i;
                } else {
                    if (y > 0) {
                        y = y + vgap;
                    }
                    y = y + tempDim.height;
                    colwidth = Math.max(colwidth, tempDim.width);
                }
            }
        }
        placeComponents(target, x, insets.top + vgap, colwidth, maxheight - y, start, numcomponents);
    }

    /******************************************************************************************
     * Places the components defined by first to last within the target container using the bounds box
     * defined
     * <p>
     * @param target the container
     * @param x the x coordinate of the area
     * @param y the y coordinate of the area
     * @param width the width of the area
     * @param height the height of the area
     * @param first the first component of the container to place
     * @param last the last component of the container to place
     ******************************************************************************************/
    private void placeComponents(Container target, int x, int y, int width, int height, int first, int last) {
        Component component = null;
        Dimension dim = null;
        int px = 0;

        switch (align) {
            case SwingConstants.CENTER:
                y = y + height / 2;
                break;
            case SwingConstants.BOTTOM:
                y = y + height;
            default:
        }

        if (order == DESCENDING) {
            for (int i = first; i < last; i++) {
                component = target.getComponent(i);
                dim = component.getSize();
                if (component.isVisible()) {
                    px = x + (width - dim.width) / 2;
                    component.setLocation(px, y);
                    y = y + vgap + dim.height;
                }
            }
        } else {
            for (int i = last - 1; i >= first; i--) {
                component = target.getComponent(i);
                dim = component.getSize();
                if (component.isVisible()) {
                    px = x + (width - dim.width) / 2;
                    component.setLocation(px, y);
                    y = y + vgap + dim.height;
                }
            }
        }
    }
}
