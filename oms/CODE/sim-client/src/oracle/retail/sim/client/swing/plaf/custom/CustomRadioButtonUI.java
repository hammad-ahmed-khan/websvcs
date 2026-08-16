package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.metal.MetalRadioButtonUI;
import javax.swing.text.View;

/******************************************************************************************
 * This class subclasses the metal look and feel radio button to create a chrome look
 * and feel radio button.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class CustomRadioButtonUI extends MetalRadioButtonUI implements Serializable {
    private static final long serialVersionUID = 3048844364567722109L;

    protected static MetalRadioButtonUI buttonUI;

    private static ImageIcon selectedIcon;
    private static ImageIcon unselectedIcon;

    /******************************************************************************************
     * Constructs new ChromeRadioButtonUI.
     *****************************************************************************************/
    public CustomRadioButtonUI() {
    }

    /******************************************************************************************
     * Creates a UI for the component.
     *****************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        if (buttonUI == null) {
            buttonUI = new CustomRadioButtonUI();
        }
        return buttonUI;
    }

    /******************************************************************************************
     * Installs the UI for the component.
     *****************************************************************************************/
    public void installUI(JComponent component) {
        super.installUI(component);
    }

    /******************************************************************************************
     * Paints the radio button.
     *****************************************************************************************/
    public void paint(Graphics graphics, JComponent component) {
        AbstractButton button = (AbstractButton) component;
        ButtonModel model = button.getModel();
        Dimension size = component.getSize();

        graphics.setFont(button.getFont());

        FontMetrics fontMetrics = graphics.getFontMetrics();

        Rectangle viewRect = new Rectangle(size);
        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();

        String text = SwingUtilities.layoutCompoundLabel(component, fontMetrics, button.getText(), icon, button.getVerticalAlignment(), button.getHorizontalAlignment(),
                button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewRect, iconRect, textRect, getDefaultTextIconGap(button));

        // Paint Background
        if (component.isOpaque()) {
            graphics.setColor(button.getBackground());
            graphics.fillRect(0, 0, size.width, size.height);
        }

        if (model.isSelected()) {
            icon = getSelectedIcon();
        } else {
            icon = getUnselectedIcon();
        }

        // Paint Icon
        if (icon != null) {
            icon.paintIcon(component, graphics, iconRect.x, iconRect.y);
        }

        // Paint Text
        if (text != null) {
            View view = (View) component.getClientProperty(BasicHTML.propertyKey);

            if (view != null) {
                view.paint(graphics, textRect);
            } else {
                int ascent = fontMetrics.getAscent();
                if (model.isEnabled()) {
                    graphics.setColor(button.getForeground());
                } else {
                    graphics.setColor(button.getBackground().darker());
                }
                BasicGraphicsUtils.drawString(graphics, text, model.getMnemonic(), textRect.x, textRect.y + ascent);
            }
        }

        //Note: Alter for system to get focus changed....
        if (button.hasFocus() && button.isFocusPainted() && textRect.width > 0 && textRect.height > 0) {
            paintFocus(graphics, textRect, size);
        }
    }

    /******************************************************************************************
     * Retrieves the selected icon.
     *****************************************************************************************/
    private static ImageIcon getSelectedIcon() {
        if (selectedIcon == null) {
            selectedIcon = (ImageIcon) UIManager.getIcon(UIThemeName.RADIOBUTTON_SELECTED_ICON);
        }
        return selectedIcon;
    }

    /******************************************************************************************
     * Retrieves the unselected icon.
     *****************************************************************************************/
    private static ImageIcon getUnselectedIcon() {
        if (unselectedIcon == null) {
            unselectedIcon = (ImageIcon) UIManager.getIcon(UIThemeName.RADIOBUTTON_UNSELECTED_ICON);
        }
        return unselectedIcon;
    }
}
