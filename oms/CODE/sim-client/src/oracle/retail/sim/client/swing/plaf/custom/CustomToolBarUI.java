package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.ImageObserver;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalToolBarUI;

/********************************************************************************************************
 * This class subclasses the CustomToolBarUI rather than the MetalComboBoxUI in order to paint the combo
 * box using the chrome look and feel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomToolBarUI extends MetalToolBarUI implements ImageObserver {

    /****************************************************************************************************
     * Creates the UI for a component.
     ***************************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        return new CustomToolBarUI();
    }

    /****************************************************************************************************
     * Paints the component using the graphics context.
     ***************************************************************************************************/
    public void paint(Graphics graphics, JComponent component) {
        ImageIcon backgroundIcon = (ImageIcon) UIManager.getIcon(UIThemeName.TOOLBAR_BACKGROUND_ICON);
        if (backgroundIcon != null) {
            graphics.drawImage(backgroundIcon.getImage(), 0, 0, this);
        }
        super.paint(graphics, component);
    }

    /****************************************************************************************************
     * Empty implementation of ImageObserver for painting the background Icon.
     ***************************************************************************************************/
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        return false;
    }
}
