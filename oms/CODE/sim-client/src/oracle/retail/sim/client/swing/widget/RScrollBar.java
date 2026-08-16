package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import javax.swing.JScrollBar;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.ColorUtility;

/******************************************************************************************
 * This class subclasses the standard JScrollBar class in the Swing package to provide
 * custom functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RScrollBar extends JScrollBar {
    private static final long serialVersionUID = 280904260743954790L;

    private Color originalColor;
    private Color shadowColor;
    private Color thumbColor;
    private Color trackColor;
    private Color thumbHighlightColor;
    private Color thumbDarkshadowColor;

    /******************************************************************************************
     * Creates a scrollbar with the specified orientation, value, extent, mimimum, and maximum.
     * The "extent" is the size of the viewable area. It is also known as the "visible amount".
     * <p>
     * Note: Use <code>setBlockIncrement</code> to set the block increment to a size slightly
     * smaller than the view's extent. That way, when the user jumps the knob to an adjacent
     * position, one or two lines of the original contents remain in view.
     * <p>
     * @throws IllegalArgumentException Thrown if orientation is not VERTICAL or HORIZONTAL.
     * <p>
     * @see #setOrientation
     * @see #setValue
     * @see #setVisibleAmount
     * @see #setMinimum
     * @see #setMaximum
     *****************************************************************************************/
    public RScrollBar(int orientation, int value, int extent, int min, int max) {
        super(orientation, value, extent, min, max);
        resetDefaultColors();
    }

    /******************************************************************************************
     * Creates a scrollbar with the specified orientation and the following initial values:
     * <p>
     * @see #setOrientation
     * <pre>
     * minimum = 0
     * maximum = 100
     * value   = 0
     * extent  = 10
     * </pre>
     *****************************************************************************************/
    public RScrollBar(int orientation) {
        super(orientation);
        resetDefaultColors();
    }

    /******************************************************************************************
     * Creates a vertical scrollbar with the following initial values:
     * <pre>
     * minimum = 0
     * maximum = 100
     * value   = 0
     * extent  = 10
     * </pre>
     *****************************************************************************************/
    public RScrollBar() {
        resetDefaultColors();
    }

    /******************************************************************************************
     * Resets the default colors of the scrollbar from the UIManager. These default colors
     * are established by the look and feel, but can be altered by the theme properties files.
     *****************************************************************************************/
    public void resetDefaultColors() {
        if (originalColor != null) {
            super.setBackground(originalColor);
        }
        originalColor = getBackground();
        shadowColor = UIManager.getColor(UIThemeName.SCROLLBAR_SHADOW);
        thumbColor = UIManager.getColor(UIThemeName.SCROLLBAR_THUMB);
        trackColor = UIManager.getColor(UIThemeName.SCROLLBAR_TRACK);
        thumbHighlightColor = UIManager.getColor(UIThemeName.SCROLLBAR_THUMB_HIGHLIGHT);
        thumbDarkshadowColor = UIManager.getColor(UIThemeName.SCROLLBAR_THUMB_DARKSHADOW);
    }

    /******************************************************************************************
     * Assigns the background color to the scroll bar. It also assigns a thumb, track and
     * shadow colors based on the background color.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setBackground(Color color) {
        if (color != null) {
            super.setBackground(color);

            thumbColor = ColorUtility.getGradientStart(color.darker());
            trackColor = ColorUtility.disabledTint(color);
            shadowColor = color.darker().darker();
            thumbHighlightColor = thumbColor.brighter();
            thumbDarkshadowColor = thumbColor.darker().darker();
        }
    }

    /******************************************************************************************
     * Retrieves scroll bar shadow color.
     *****************************************************************************************/
    public Color getShadowColor() {
        return shadowColor;
    }

    /******************************************************************************************
     * Retrieves scroll bar thumb color. The starting point of the chrome colors...
     * <p>
     * @return The thumb color.
     *****************************************************************************************/
    public Color getThumbColor() {
        return thumbColor;
    }

    /******************************************************************************************
     * Retrieves scroll bar thumb higlight color.
     * <p>
     * @return The thumb highlight color.
     *****************************************************************************************/
    public Color getThumbHighlightColor() {
        return thumbHighlightColor;
    }

    /******************************************************************************************
     * Retrieves scroll bar thumb darkshadow color.
     * <p>
     * @return The thumb darkshadow color.
     *****************************************************************************************/
    public Color getThumbDarkShadowColor() {
        return thumbDarkshadowColor;
    }

    /******************************************************************************************
     * Retrieves scroll bar track color.
     * <p>
     * @return The scroll bar track color.
     *****************************************************************************************/
    public Color getTrackColor() {
        return trackColor;
    }
}
