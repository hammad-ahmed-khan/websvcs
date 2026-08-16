package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.metal.MetalButtonUI;
import oracle.retail.sim.client.swing.util.GraphicsUtility;
import oracle.retail.sim.client.swing.widget.RHyperlink;

/**************************************************************************************************
 * An extension of MetalButtonUI which produces a Button with the appearance of a 'hyperlink'.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 **************************************************************************************************/

public class CustomHyperlinkUI extends MetalButtonUI {

    private static CustomHyperlinkUI buttonUI;

    /**************************************************************************************************
     * Creates the UI for a component.
     **************************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        if (buttonUI == null) {
            buttonUI = new CustomHyperlinkUI();
        }
        return buttonUI;
    }

    /**************************************************************************************************
     * Installs the UI for a component. No margin except for pixels on the bottom for painting the
     * underline and mnemonic. Note that the cursor will appear anywhere within the borders of the
     * button.
     **************************************************************************************************/
    public void installUI(JComponent component) {
        super.installUI(component);

        AbstractButton button = (AbstractButton) component;

        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setMargin(new Insets(0, 0, 3, 0));
    }

    /**************************************************************************************************
     * Overridden so that maximum size is equal to preferred size.
     **************************************************************************************************/
    public Dimension getMaximumSize(JComponent component) {
        return getPreferredSize(component);
    }

    /**************************************************************************************************
     * Paints the text.
     **************************************************************************************************/
    protected void paintText(Graphics graphics, JComponent component, Rectangle textRect, String text) {
        RHyperlink hyperlink = (RHyperlink) component;
        ButtonModel buttonModel = hyperlink.getModel();

        if (buttonModel.isPressed() && buttonModel.isEnabled()) {
            graphics.setColor(hyperlink.getSelectedForeground());
        } else if (!buttonModel.isEnabled()) {
            graphics.setColor(hyperlink.getDisabledForeground());
        } else {
            graphics.setColor(hyperlink.getForeground());
        }

        FontMetrics metrics = graphics.getFontMetrics();
        int index = hyperlink.getDisplayedMnemonicIndex();

        if (hyperlink.isUnderlineEnabled() || hyperlink.isFocusOwner()) {
            GraphicsUtility.drawUnderlinedString(graphics, text, index, textRect.x, textRect.y + metrics.getAscent());
        } else {
            BasicGraphicsUtils.drawString(graphics, text, index, textRect.x, textRect.y + metrics.getAscent());
        }
    }

    /**************************************************************************************************
     * Paints the focus.
     **************************************************************************************************/
    protected void paintFocus(Graphics graphics, AbstractButton button, Rectangle viewRect, Rectangle textRect, Rectangle iconRect) {

        RHyperlink hyperlink = (RHyperlink) button;
        String text = button.getText();
        boolean containsIcon = button.getIcon() != null;

        Rectangle focusRect = new Rectangle();
        if (text != null && text.trim().length() > 0) {
            if (containsIcon) {
                focusRect.setBounds(iconRect.union(textRect));
            } else {
                focusRect.setBounds(textRect);
            }
            focusRect.height = focusRect.height + 1;
        } else if (containsIcon) {
            focusRect.setBounds(iconRect);
        }
        graphics.setColor(hyperlink.getBorderColor());

        GraphicsUtility.drawFocusRect(graphics, focusRect.x, focusRect.y, focusRect.width, focusRect.height);
    }
}
