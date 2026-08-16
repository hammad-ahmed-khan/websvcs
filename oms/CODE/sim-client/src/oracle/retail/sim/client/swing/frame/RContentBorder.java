package oracle.retail.sim.client.swing.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;

/******************************************************************************************
 * Content border to be placed around the content pane container. This class should ONLY
 * be used by RPlatformFrame.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RContentBorder extends AbstractBorder {
    private static final long serialVersionUID = -1319363653222699314L;

    private static Color dark1 = new Color(241, 239, 247);
    private static Color dark2 = new Color(219, 215, 232);
    private static Color dark3 = new Color(190, 185, 211);
    private static Color dark4 = new Color(145, 142, 164);
    private static Color dark5 = new Color(93, 91, 110);

    /******************************************************************************************
     * Retrieves the border insets so other components no how big it is.
     * <p>
     * @return The insets.
     *****************************************************************************************/
    public Insets getBorderInsets(Component component) {
        return new Insets(0, 5, 0, 5);
    }

    /******************************************************************************************
     * Paints the border. Colors are hard-coded at the top. Always a chrome border.
     *****************************************************************************************/
    public void paintBorder(Component component, Graphics graphics, int x, int y, int width, int height) {
        graphics.setColor(dark1); // LEFT BORDER
        graphics.drawLine(x, y, x, height);
        graphics.setColor(dark2);
        graphics.drawLine(x + 1, y, x + 1, height);
        graphics.setColor(dark3);
        graphics.drawLine(x + 2, y, x + 2, height);
        graphics.setColor(dark4);
        graphics.drawLine(x + 3, y, x + 3, height);
        graphics.setColor(dark5);
        graphics.drawLine(x + 4, y, x + 4, height);

        graphics.setColor(dark5); // RIGHT BORDER
        graphics.drawLine(width - 1, height, width - 1, y);
        graphics.setColor(dark4);
        graphics.drawLine(width - 2, height, width - 2, y);
        graphics.setColor(dark3);
        graphics.drawLine(width - 3, height, width - 3, y);
        graphics.setColor(dark2);
        graphics.drawLine(width - 4, height, width - 4, y);
        graphics.setColor(dark1);
        graphics.drawLine(width - 5, height, width - 5, y);
    }
}
