package oracle.retail.sim.client.swing.util;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/********************************************************************************************************
 * This utility assists in painting a graphics object with the chrome look and feel. It contains two
 * paintChrome() methods that allow the object to be painted.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ********************************************************************************************************/

public class ChromeUtility {

    public static final int LEFT_TO_RIGHT = 0;
    public static final int RIGHT_TO_LEFT = 1;
    public static final int TOP_TO_BOTTOM = 2;
    public static final int BOTTOM_TO_TOP = 3;

    /********************************************************************************************************
     * This static method paints a graphics object using the start and end color to create a chrome effect.
     * <p>
     * @param graphics The graphics object to paint.
     * @param x The x coordinate in the graphics object to start painting at.
     * @param y The y coordinate in the graphics object to start painting at.
     * @param width The width of the graphics object in pixels.
     * @param height The height of the graphics object in pixels.
     * @param startColor The starting color of the chrome pattern.
     * @param endColor The end color of the chrome pattern.
     * <p>
     * @throws IllegalArgumentException Thrown if any parameter is null.
     ********************************************************************************************************/
    public static void paintChrome(Graphics graphics, int x, int y, int width, int height, Color startColor, Color endColor) {

        paintChrome(graphics, x, y, width, height, startColor, endColor, TOP_TO_BOTTOM);
    }

    /********************************************************************************************************
     * This static method paints a graphics object using the start and end color to create a chrome effect.
     * <p>
     * @param graphics The graphics object to paint.
     * @param x The x coordinate in the graphics object to start painting at.
     * @param y The y coordinate in the graphics object to start painting at.
     * @param width The width of the graphics object in pixels.
     * @param height The height of the graphics object in pixels.
     * @param startColor The starting color of the chrome pattern.
     * @param finalColor The end color of the chrome pattern.
     * @param orientation The orientation to paint the information.
     * <p>
     * @throws IllegalArgumentException Thrown if any parameter is null.
     ********************************************************************************************************/
    public static void paintChrome(Graphics graphics, int x, int y, int width, int height, Color startColor, Color finalColor, int orientation) {

        if (graphics == null) {
            throw new IllegalArgumentException("Graphics cannot be null!");
        }
        if (startColor == null) {
            throw new IllegalArgumentException("Start color cannot be null!");
        }
        if (finalColor == null) {
            throw new IllegalArgumentException("End color cannot be null!");
        }
        GradientPaint gradientPaint;

        switch (orientation) {
            case TOP_TO_BOTTOM:
                gradientPaint = new GradientPaint(x, y, startColor, x, height, finalColor);
                break;
            case BOTTOM_TO_TOP:
                gradientPaint = new GradientPaint(x, y, finalColor, x, height, startColor);
                break;
            case LEFT_TO_RIGHT:
                gradientPaint = new GradientPaint(x, y, startColor, width, y, finalColor);
                break;
            case RIGHT_TO_LEFT:
                gradientPaint = new GradientPaint(x, y, finalColor, width, y, startColor);
                break;
            default:
                throw new IllegalArgumentException("Invalid orientation for painting!");
        }

        Graphics2D graphics2 = (Graphics2D) graphics;
        graphics2.setPaint(gradientPaint);
        graphics2.fill(new Rectangle2D.Double(x, y, width, height));
    }

    /********************************************************************************************************
     * This static method paints a graphics object using the start and end color to create a chrome effect.
     * This will paint up to the middle with one gradient and then paint in reverse to the remainder of the area.
     * <p>
     * @param graphics The graphics object to paint.
     * @param x The x coordinate in the graphics object to start painting at.
     * @param y The y coordinate in the graphics object to start painting at.
     * @param width The width of the graphics object in pixels.
     * @param height The height of the graphics object in pixels.
     * @param startColor The starting color of the chrome pattern.
     * @param finalColor The end color of the chrome pattern.
     ********************************************************************************************************/
    public static void paintCenteredChrome(Graphics graphics, int x, int y, int width, int height, Color startColor, Color finalColor) {
        paintCenteredChrome(graphics, x, y, width, height, startColor, finalColor, TOP_TO_BOTTOM);
    }

    /********************************************************************************************************
     * This static method paints a graphics object using the start and end color to create a chrome effect.
     * This will paint up to the middle with one gradient and then paint in reverse to the remainder of the area.
     * <p>
     * @param graphics The graphics object to paint.
     * @param x The x coordinate in the graphics object to start painting at.
     * @param y The y coordinate in the graphics object to start painting at.
     * @param width The width of the graphics object in pixels.
     * @param height The height of the graphics object in pixels.
     * @param startColor The starting color of the chrome pattern.
     * @param finalColor The end color of the chrome pattern.
     ********************************************************************************************************/
    public static void paintCenteredChrome(Graphics graphics, int x, int y, int width, int height, Color startColor, Color finalColor, int orientation) {
        Graphics2D graphics2 = (Graphics2D) graphics;
        switch (orientation) {
            case TOP_TO_BOTTOM:
                int middle = height / 2;
                graphics2.setPaint(new GradientPaint(x, y, startColor, x, y + middle, finalColor));
                graphics2.fill(new Rectangle2D.Double(x, y, width, middle));
                y = y + middle;
                graphics2.setPaint(new GradientPaint(x, y, finalColor, x, height, startColor));
                graphics2.fill(new Rectangle2D.Double(x, y, width, height - y));
                break;
            case BOTTOM_TO_TOP:
                break;
            case LEFT_TO_RIGHT:
                break;
            case RIGHT_TO_LEFT:
                break;
            default:
                break;
        }
    }
}
