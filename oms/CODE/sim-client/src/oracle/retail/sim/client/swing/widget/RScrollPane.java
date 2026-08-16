package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.border.Border;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/********************************************************************************************************
 * This class subclasses the standard JScrollPane class in the Swing package to provide custom
 * functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RScrollPane extends JScrollPane {
    private static final long serialVersionUID = -5401865561565312216L;

    private Color lineColor;

    private boolean verticalWeight = true;
    private boolean horiztonalWeight = true;

    /****************************************************************************************************
     * Returns new RScrollPane object.
     ***************************************************************************************************/
    public RScrollPane() {
        initialize();
    }

    /****************************************************************************************************
     * Returns new RScrollPane object for a particular component.
     * <p>
     * @param view The component to build the scrollpane around.
     ***************************************************************************************************/
    public RScrollPane(Component view) {
        super(view);
        initialize();
    }

    /****************************************************************************************************
     * Initializes the scroll bars of the scroll pane to the scrollbar width property.
     ***************************************************************************************************/
    private void initialize() {
        setExtendedBackground(UIManager.getColor(UIThemeName.SCROLLPANE_BACKGROUND));
    }

    /****************************************************************************************************
     * Assigns whether or not the panel contains horizontal and vertical weight. This value is used
     * exclusively by the framework tool RAssemblePanel, but could have other usages.
     * <p>
     * @param weight If true, this will assign weight both vertically and horizontally.
     ***************************************************************************************************/
    public void setExpandable(boolean weight) {
        setExpandable(weight, weight);
    }

    /****************************************************************************************************
     * Assigns whether or not the panel contains horizontal and vertical weight. These values is used
     * exclusively by the framework tool RAssemblePanel, but could have other usages.
     * <p>
     * @param verticalWeight True if the panel should have vertical weight, false otherwise.
     * @param horizontalWeight True if the panel should have vertical weight, false otherwise.
     ***************************************************************************************************/
    public void setExpandable(boolean verticalWeight, boolean horizontalWeight) {
        this.verticalWeight = verticalWeight;
        horiztonalWeight = horizontalWeight;
    }

    /****************************************************************************************************
     * Retrieves whether or not the panel has vertical weight.
     * <p>
     * @return True if the panel has vertical weight, false otherwise.
     ***************************************************************************************************/
    public boolean hasVerticalWeight() {
        return verticalWeight;
    }

    /****************************************************************************************************
     * Retrieves whether or not the panel has horizontal weight.
     * <p>
     * @return True if the panel has horizontal weight, false otherwise.
     ***************************************************************************************************/
    public boolean hasHorizontalWeight() {
        return horiztonalWeight;
    }

    /****************************************************************************************************
     * Assigns a minimum height to the scrollpane.
     * <p>
     * @param height The height of the scrollpane in pixels.
     ***************************************************************************************************/
    public void setMinimumHeight(int height) {
        setMinimumSize(new Dimension(1, height));
        setPreferredSize(new Dimension(1, height));
    }

    /****************************************************************************************************
     * Assigns a locked size to the scrollpane, making it so that the scrollpane cannot be resized by
     * most layouts.
     * <p>
     * @param width The width of the scrollpane in pixels.
     * @param height The height of the scrollpane in pixels.
     ***************************************************************************************************/
    public void setLockedSize(int width, int height) {
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
    }

    /****************************************************************************************************
     * Sets a lowered bevel border on the scrollpane.
     ***************************************************************************************************/
    public void setLoweredBorder() {
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    /****************************************************************************************************
     * Sets a line border for the scrollpane using default system settings.
     ***************************************************************************************************/
    public void setLineBorder() {
        setBorder(BorderFactory.createLineBorder(getLineColor()));
    }

    /****************************************************************************************************
     * Builds a scrollpane with a titled border.
     * <p>
     * @param title The title to assign to the scrollpane.
     ***************************************************************************************************/
    public void setTitleBorder(String title) {
        Border border1 = BorderFactory.createLineBorder(getLineColor());
        setBorder(BorderFactory.createTitledBorder(border1, Translator.getText(title)));
    }

    /****************************************************************************************************
     * Retrieves the line color used by the panel to paint line borders and titles.
     * <p>
     * @return The line color.
     ***************************************************************************************************/
    public Color getLineColor() {
        if (lineColor == null) {
            lineColor = UIManager.getColor(UIThemeName.SCROLLPANE_LINE_COLOR);
        }
        return lineColor;
    }

    /****************************************************************************************************
     * Assigns the line color used by the panel to paint line borders and titles.
     * <p>
     * @param lineColor The color to assign.
     ***************************************************************************************************/
    public void setLineColor(Color lineColor) {
        if (lineColor != null) {
            this.lineColor = lineColor;
        }
    }

    /****************************************************************************************************
     * Sets the background color of the scrollpane in the scrollpane, the viewport and the viewport view.
     * <p>
     * @param color The color to assign to the scrollpane background.
     ***************************************************************************************************/
    public void setExtendedBackground(Color color) {
        setExtendedBackground(color, true);
    }

    /****************************************************************************************************
     * Sets the background color of the scrollpane in the scrollpane, the viewport and the viewport view.
     * <p>
     * @param color The color to assign to the scrollpane background.
     * @param includeViewport True if the viewport background should be set as well.
     ***************************************************************************************************/
    public void setExtendedBackground(Color color, boolean includeViewport) {
        JViewport viewport = getViewport();

        if (includeViewport && viewport != null) {
            viewport.setBackground(color);

            if (viewport.getView() != null) {
                viewport.getView().setBackground(color);
            }
        }
        getHorizontalScrollBar().setBackground(color);
        getVerticalScrollBar().setBackground(color);

        setBackground(color);
    }

    /****************************************************************************************************
     * Sets the foreground color of the scrollpane in the scrollpane, the viewport and the viewport view.
     * <p>
     * @param color The color to assign to the scrollpane background.
     ***************************************************************************************************/
    public void setExtendedForeground(Color color) {
        JViewport viewport = getViewport();

        if (viewport != null) {
            viewport.setForeground(color);

            if (viewport.getView() != null) {
                viewport.getView().setForeground(color);
            }
        }
        setForeground(color);
    }

    /****************************************************************************************************
     * Turns the horizontal scroll bar on.
     ***************************************************************************************************/
    public void turnHorizontalScrollBarOn() {
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_ALWAYS);
    }

    /****************************************************************************************************
     * Turns the horizontal scroll bar off.
     ***************************************************************************************************/
    public void turnHorizontalScrollBarOff() {
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }

    /****************************************************************************************************
     * Turns the horizontal scroll bar off.
     ***************************************************************************************************/
    public void turnVerticalScrollBarOff() {
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }
}
