package oracle.retail.sim.client.swing.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/******************************************************************************************
 * This method represents an RPanel containing a background image.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RImagePanel extends RPanel {
    private static final long serialVersionUID = -8018696942544009240L;

    private Image backgroundImage;

    /******************************************************************************************
     * Creates a new image panel without a background image.
     *****************************************************************************************/
    public RImagePanel() {
        super(new BorderLayout());
    }

    /******************************************************************************************
     * Creates a new image panel with a background image.
     * <p>
     * @param image The background image.
     *****************************************************************************************/
    public RImagePanel(Image image) {
        super(new BorderLayout());
        setBackgroundImage(image);
    }

    /******************************************************************************************
     * Assigns the background image to the panel.
     * <p>
     * @param image The image to assign.
     *****************************************************************************************/
    public void setBackgroundImage(Image image) {
        backgroundImage = image;
    }

    /******************************************************************************************
     * Retrieves the background image of the panel.
     * <p>
     * @return The image.
     *****************************************************************************************/
    public Image getBackgroundImage() {
        return backgroundImage;
    }

    /******************************************************************************************
     * Paints the component.
     *****************************************************************************************/
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (backgroundImage != null) {
            graphics.drawImage(backgroundImage, 0, 0, this);
        }
    }

    /******************************************************************************************
     * Retrieves the preferredSize() of the panel. If the superclass preferredSize() is less
     * than the background image, the image size is returned.
     *****************************************************************************************/
    public Dimension getPreferredSize() {
        Dimension dimension = super.getPreferredSize();
        if (backgroundImage != null) {
            dimension.width = Math.max(dimension.width, backgroundImage.getWidth(this));
            dimension.height = Math.max(dimension.height, backgroundImage.getHeight(this));
        }
        return dimension;
    }
}
