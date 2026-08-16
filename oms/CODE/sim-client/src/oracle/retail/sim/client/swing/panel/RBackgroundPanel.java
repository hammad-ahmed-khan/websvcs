package oracle.retail.sim.client.swing.panel;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;

/********************************************************************************************************
 * Background Panel
 * <p>
 * BackgroundPanel is an extension of JPanel that makes use of BackgroundPainter implementations to paint
 * a background image behind all contained compontents.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RBackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1566674878808270369L;

    public static final int CENTER = 0;
    public static final int TILE = 1;
    public static final int SCALE = 2;

    private ImageIcon backgroundImageIcon;
    private int backgroundStyle = CENTER;

    private ImageIcon scaledImageIcon;
    private int scaledComponentWidth = -1;
    private int scaledComponentHeight = -1;

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public RBackgroundPanel() {
    }

    /****************************************************************************************************
     * Retrieves the background style of the panel (CENTER, TILE, or SCALE).
     ***************************************************************************************************/
    public int getBackgroundStyle() {
        return backgroundStyle;
    }

    /****************************************************************************************************
     * Assigns the background style of the panel (CENTER, TILE, or SCALE).
     ***************************************************************************************************/
    public void setBackgroundStyle(int style) {
        if (style < 0 || style > SCALE) {
            throw new IllegalArgumentException("Illegal style argument. Values are CENTER (0), TILE (1), SCALE (2)");
        }
        if (backgroundStyle != style) {
            backgroundStyle = style;
            repaint();
        }
    }

    /****************************************************************************************************
     * Retrieves the background image of the panel.
     ***************************************************************************************************/
    public ImageIcon getBackgroundImageIcon() {
        return backgroundImageIcon;
    }

    /****************************************************************************************************
     * Assigns the background image to the panel.
     ***************************************************************************************************/
    public void setBackgroundImageIcon(ImageIcon icon) {
        if (icon == null || icon.getIconHeight() < 0 || icon.getIconWidth() < 0) {
            throw new IllegalArgumentException("ImageIcon not valid (" + icon + ").");
        }
        if (backgroundImageIcon != icon) {
            backgroundImageIcon = icon;
            scaledComponentHeight = -1;
            scaledComponentWidth = -1;
            scaledImageIcon = null;
            repaint();
        }
    }

    /****************************************************************************************************
     * Paints the component
     ***************************************************************************************************/
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if (backgroundImageIcon != null) {
            switch (backgroundStyle) {
                case TILE:
                    paintTile(graphics);
                    break;
                case SCALE:
                    paintScale(graphics);
                    break;
                default:
                    paintCenter(graphics);
            }
        }
    }

    /****************************************************************************************************
     * Centers and paints an icon image.
     ***************************************************************************************************/
    private void paintCenter(Graphics graphics) {
        int x = (getWidth() - backgroundImageIcon.getIconWidth()) / 2;
        int y = (getHeight() - backgroundImageIcon.getIconHeight()) / 2;

        graphics.drawImage(backgroundImageIcon.getImage(), x, y, null, null);
    }

    /****************************************************************************************************
     * Paint icon in a tiled fashion.
     ***************************************************************************************************/
    private void paintTile(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();

        int imageWidth = backgroundImageIcon.getIconWidth();
        int imageHeight = backgroundImageIcon.getIconHeight();

        Image image = backgroundImageIcon.getImage();

        for (int x = 0; x < width; x += imageWidth) {
            for (int y = 0; y < height; y += imageHeight) {
                graphics.drawImage(image, x, y, this);
            }
        }
    }

    /****************************************************************************************************
     * Paint scaled version of icon
     ***************************************************************************************************/
    private void paintScale(Graphics graphics) {
        if (scaledComponentWidth != getWidth() || scaledComponentHeight != getHeight()) {
            Image image = backgroundImageIcon.getImage();
            Image scaledImage = image.getScaledInstance(scaledComponentWidth, scaledComponentHeight, Image.SCALE_AREA_AVERAGING);
            scaledImageIcon = new ImageIcon(scaledImage);
        }
        graphics.drawImage(scaledImageIcon.getImage(), 0, 0, this);
    }

    /****************************************************************************************************
     * Displays an exception in the appropriate manner. It determines if the exception is a UIException,
     * RuntimeException or other and calls the appropriate method.
     * <p>
     * @param throwable A throwable exception.
     ***************************************************************************************************/
    public void displayException(Throwable exception) {
        UIStatusUtility.displayException(this, exception);
    }

    /****************************************************************************************************
     * Assigns a content panel to the screen.
     * <p>
     * @param panel The panel to assign.
     ***************************************************************************************************/
    public void setContentPane(RPanel panel) {
        addImpl(panel, BorderLayout.CENTER, -1);
        validate();
    }
}
