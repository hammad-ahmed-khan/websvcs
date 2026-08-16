package oracle.retail.sim.client.swing.panel;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.ChromeUtility;
import oracle.retail.sim.client.swing.util.ColorUtility;

/*****************************************************************************************************
 * Create an instance of RPanel that paints itself with a chrome look and feel.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************************/

public class RChromePanel extends JPanel {
    private static final long serialVersionUID = -2498589111592794654L;

    private int chromeOrientation = ChromeUtility.TOP_TO_BOTTOM;

    /*****************************************************************************************************
     * Create a new RChromePanel with the default color scheme.
     *****************************************************************************************************/
    public RChromePanel() {
        setBackground(UIManager.getColor(UIThemeName.CHROME_PANEL_BACKGROUND));
    }

    /*****************************************************************************************************
     * Retrieve the orientation of the chrome painting in the screen. This matches the values available
     * in ChromeUtility.
     * <p>
     * @return The chrome orientiation of the painting.
     *****************************************************************************************************/
    public int getChromeOrientation() {
        return chromeOrientation;
    }

    /*****************************************************************************************************
     * Sets the orientation of the chrome painting in the screen. This matches the values available
     * in ChromeUtility. This must be one of four values LEFT_TO_RIGHT, RIGHT_TO_LEFT, TOP_TO_BOTTOM,
     * BOTTOM_TO_TOP.
     * <p>
     * @param orientation The chrome orientiation of the painting.
     *****************************************************************************************************/
    public void setChromeOrientation(int orientation) {
        switch (orientation) {
            case ChromeUtility.LEFT_TO_RIGHT:
            case ChromeUtility.RIGHT_TO_LEFT:
            case ChromeUtility.TOP_TO_BOTTOM:
            case ChromeUtility.BOTTOM_TO_TOP:
                break;
            default:
                throw new IllegalArgumentException("Orientiation contains an invalid argument");
        }
        chromeOrientation = orientation;
    }

    /*****************************************************************************************************
     * Paints the background of the ChromePanel.
     * <p>
     * @param graphics The Graphics object to paint.
     *****************************************************************************************************/
    public void paintComponent(Graphics graphics) {
        int width = (int) getSize().getWidth();
        int height = (int) getSize().getHeight();

        Color backgroundColor = getBackground();
        Color startColor = ColorUtility.getGradientStart(backgroundColor);

        ChromeUtility.paintChrome(graphics, 0, 0, width, height, startColor, backgroundColor, chromeOrientation);
    }
}
