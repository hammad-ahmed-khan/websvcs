package oracle.retail.sim.client.swing.widget;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.configutil.ResourceManager;

/******************************************************************************************
 * This class subclasses RButton and adds some icon features.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RIconButton extends RButton {
    private static final long serialVersionUID = -1626238984761112334L;

    /*****************************************************************************************
     * Returns new RIconButton object.
     *****************************************************************************************/
    public RIconButton() {
        setMargin(new Insets(0, 0, 0, 0));
        setBorder(null);
        setOpaque(true);
        setBackground(UIManager.getColor(UIThemeName.PANEL_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.PANEL_FOREGROUND));
    }

    /******************************************************************************************
     * Attempts to load an icon from a filename and places it in the button.
     * <p>
     *@param filename The filename of the icon to place in the button.
     *@param text The replacement text if the icon could not be found.
     *****************************************************************************************/
    public void setIcon(String filename, String text) {
        try {
            setIcon(ResourceManager.getImageIcon(filename));
        } catch (Throwable t) {
            setText(text);
        }
    }

    /******************************************************************************************
     * Attempts to load an icon from a filename and places it as the disabled icon of the
     * button.
     * <p>
     *@param filename The filename of the icon to place in the button.
     *@param text The replacement text if the icon could not be found.
     *****************************************************************************************/
    public void setDisabledIcon(String filename, String text) {
        try {
            setDisabledIcon(ResourceManager.getImageIcon(filename));
        } catch (Throwable t) {
            setText(text);
        }
    }

    /******************************************************************************************
     * Assigns the icon to the button.
     * <p>
     *@param icon The icon to assign to the button.
     *****************************************************************************************/
    public void setIcon(Icon icon) {
        resizeToIcon(icon);
        super.setIcon(icon);
    }

    /******************************************************************************************
     * Assigns the icon to the button.
     * <p>
     *@param icon The icon to assign to the button.
     *****************************************************************************************/
    public void setIcon(Icon icon, String text) {
        resizeToIcon(icon);
        if (icon == null) {
            setText(text);
        }
        super.setIcon(icon);
    }

    /******************************************************************************************
     * Assigns the disabled icon to the button.
     * <p>
     *@param icon The icon to assign to the button as the disabled icon.
     *****************************************************************************************/
    public void setDisabledIcon(Icon icon) {
        resizeToIcon(icon);
        super.setDisabledIcon(icon);
    }

    /******************************************************************************************
     * Assigns the disabled icon to the button.
     * <p>
     *@param icon The icon to assign to the button as the disabled icon.
     *****************************************************************************************/
    public void setDisabledIcon(Icon icon, String text) {
        resizeToIcon(icon);
        if (icon == null) {
            setText(text);
        }
        super.setDisabledIcon(icon);
    }

    /******************************************************************************************
     * Private method to resize the button to the size of the icon (if the icon is larger than
     * the preferred size).
     * <p>
     *@param icon The icon to resize the button to.
     *****************************************************************************************/
    private void resizeToIcon(Icon icon) {
        if (icon != null) {
            int width = icon.getIconWidth();
            int height = icon.getIconHeight();

            Dimension oldSize = getMinimumSize();

            if (oldSize.width > width) {
                width = oldSize.width;
            }
            if (oldSize.height > height) {
                height = oldSize.height;
            }
            setMinimumSize(new Dimension(width, height));
        }
    }
}
