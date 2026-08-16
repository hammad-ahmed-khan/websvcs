package oracle.retail.sim.client.swing.panel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/********************************************************************************************************
 * This class represents a divider that can be placed between panels. RDivider is not a container and
 * cannot contain other components.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDivider extends JComponent {
    private static final long serialVersionUID = -929407288501762323L;

    private static String METHOD_UNAVAILABLE = "Cannot alter the size with this method! Use setPad() and setIndent()";

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;

    private int dividerType = HORIZONTAL;
    private int dividerIndent = 7;
    private int dividerPad = 7;

    /****************************************************************************************************
     * Constructs a new divider.
     * <p>
     * @param type Either HORIZONTAL or VERTICAL.
     ***************************************************************************************************/
    public RDivider(int type) {
        if (type < HORIZONTAL || type > VERTICAL) {
            throw new IllegalArgumentException("Type must be HORIZONTAL or VERTICAL");
        }
        if (type == HORIZONTAL) {
            setHorizontal();
        } else {
            setVertical();
        }
        setBackground(UIManager.getColor(UIThemeName.DIVIDER_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.DIVIDER_FOREGROUND));
        setOpaque(false);
    }

    /****************************************************************************************************
     * Sets the divider to its horizontal type.
     ***************************************************************************************************/
    public void setHorizontal() {
        dividerType = HORIZONTAL;
        int size = dividerPad * 2 + 1;
        super.setMinimumSize(new Dimension(0, size));
        super.setPreferredSize(new Dimension(0, size));
    }

    /****************************************************************************************************
     * Retrieves whether or not the divider is horizontal.
     * <p>
     * @return True if the divider is horizontal, false if it is vertical.
     ***************************************************************************************************/
    public boolean isHorizontal() {
        return dividerType == HORIZONTAL;
    }

    /****************************************************************************************************
     * Sets the divider to its vertical type.
     ***************************************************************************************************/
    public void setVertical() {
        int size = dividerPad * 2 + 1;
        super.setMinimumSize(new Dimension(size, 0));
        super.setPreferredSize(new Dimension(size, 0));
        dividerType = VERTICAL;
    }

    /****************************************************************************************************
     * Assigns the indent of the divider. This is the number of pixels the line is indented on both
     * sides. The default value is 7.
     * <p>
     * @param indent The number of pixels the divider line is indented.
     ***************************************************************************************************/
    public void setIndent(int indent) {
        if (indent < 0) {
            throw new IllegalArgumentException("Indent cannot be a negative value!");
        }
        dividerIndent = indent;
        refresh();
    }

    /****************************************************************************************************
     * Assigns the pad of the divider. This is the number of pixels appearing on both sides of the
     * divider line. The default value is 7. The minimum value is 1.
     * <p>
     * @param pad The number of pixels appearing on both sides of the divider line.
     ***************************************************************************************************/
    public void setPad(int pad) {
        if (pad < 1) {
            throw new IllegalArgumentException("Pad cannot be a negative value!");
        }
        dividerPad = pad;
        refresh();
    }

    /****************************************************************************************************
     * Refreshes the divider.
     ***************************************************************************************************/
    private void refresh() {
        if (isHorizontal()) {
            setHorizontal();
        } else {
            setVertical();
        }
    }

    /****************************************************************************************************
     * Override method to disallow it. Must use setPad() and setIndent() to set size properties of the
     * divider.
     ***************************************************************************************************/
    public void setSize(Dimension dimension) {
        throw new UnsupportedOperationException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Override method to disallow it. Must use setPad() and setIndent() to set size properties of the
     * divider.
     ***************************************************************************************************/
    public void setMinimumSize(Dimension dimension) {
        throw new UnsupportedOperationException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Override method to disallow it. Must use setPad() and setIndent() to set size properties of the
     * divider.
     ***************************************************************************************************/
    public void setPreferredSize(Dimension dimension) {
        throw new UnsupportedOperationException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Override method to disallow it. Must use setPad() and setIndent() to set size properties of the
     * divider.
     ***************************************************************************************************/
    public void setMaximumSize(Dimension dimension) {
        throw new UnsupportedOperationException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * RDivider is not a container and cannot contain other components.
     ***************************************************************************************************/
    protected void addImpl(Component component, Object constraints, int index) {
        throw new UnsupportedOperationException("addImpl() is not available in RDivider!");
    }

    /****************************************************************************************************
     * Paints the background of the ChromePanel.
     * <p>
     * @param graphics The Graphics object to paint.
     ***************************************************************************************************/
    public void paintComponent(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        if (isOpaque()) {
            graphics.setColor(getBackground());
            graphics.fillRect(0, 0, width, height);
        }

        graphics.setColor(getForeground());

        int indent = dividerIndent;
        if (indent * 2 > width) {
            indent = 0;
        }
        if (isHorizontal()) {
            graphics.drawLine(indent, dividerPad + 1, width - indent, dividerPad + 1);
        } else {
            graphics.drawLine(dividerPad + 1, indent, dividerPad + 1, height - indent);
        }
    }
}
