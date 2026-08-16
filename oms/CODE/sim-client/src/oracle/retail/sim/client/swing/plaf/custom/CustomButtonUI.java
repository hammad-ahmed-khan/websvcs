package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.metal.MetalButtonUI;
import oracle.retail.sim.client.swing.util.ColorUtility;
import oracle.retail.sim.client.swing.util.FontUtility;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * This class subclasses the standard MetalButtonUI in order to paint the button using the chrome look
 * and feel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomButtonUI extends MetalButtonUI implements Serializable {
    private static final long serialVersionUID = -2144207676944277260L;
    private static final int BRIGHTER = 1;
    private static final int DARKER = 2;

    /****************************************************************************************************
     * Creates a new ChromeButtonUI for a specific component.
     * <p>
     * @param component The component to create the ChromeButtonUI for.
     * @return The ChromeButtonUI object.
     ***************************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        return new CustomButtonUI();
    }

    /****************************************************************************************************
     * Installs a UI for an AbstractButton component. Any other component will throw a
     * ClassCastException.
     * <p>
     * @param component An AbstractButton component.
     ***************************************************************************************************/
    public void installUI(JComponent component) {
        super.installUI(component);

        AbstractButton button = (AbstractButton) component;
        button.setFocusPainted(true);
        button.setRolloverEnabled(true);
    }

    /****************************************************************************************************
     * Paints the component using the graphics context.
     ***************************************************************************************************/
    public void paint(Graphics graphics, JComponent component) {
        AbstractButton button = (AbstractButton) component;
        FontMetrics fontMetrics = graphics.getFontMetrics();

        Rectangle viewRect = new Rectangle(button.getSize());
        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();

        String text = SwingUtilities.layoutCompoundLabel(fontMetrics, button.getText(), button.getIcon(), button.getVerticalAlignment(), button.getHorizontalAlignment(),
                button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewRect, iconRect, textRect, 5);

        if (button.isOpaque() && button.isEnabled()) {
            paintDefaultBackground(graphics, button);
        }
        paintDefaultIcon(graphics, button, iconRect);
        paintDefaultText(graphics, button, textRect, text);
    }

    /****************************************************************************************************
     * Paints the default chrome button icon
     ***************************************************************************************************/
    private void paintDefaultIcon(Graphics graphics, AbstractButton button, Rectangle iconRect) {
        paintIcon(graphics, button, iconRect);
    }

    /****************************************************************************************************
     * Paints the default chrome button background
     ***************************************************************************************************/
    private void paintDefaultBackground(Graphics graphics, AbstractButton button) {
        ButtonModel buttonModel = button.getModel();

        int width = button.getWidth();
        int height = button.getHeight();

        boolean isChromeEnabled = true;

        if (button instanceof RButton) {
            isChromeEnabled = ((RButton) button).isChromeActivated();
        }

        Color solidColor = button.getBackground();

        if (!buttonModel.isEnabled()) {
            graphics.setColor(ColorUtility.disabledTint(solidColor));
            graphics.fillRect(0, 0, width, height);
        } else if (buttonModel.isPressed()) {
            graphics.setColor(solidColor);
            paintChrome((Graphics2D) graphics, DARKER, width, height);
        } else if (buttonModel.isRollover() || button.isFocusOwner()) {
            graphics.setColor(ColorUtility.slightlyBrighter(solidColor));
            graphics.fillRect(0, 0, width, height);
            if (isChromeEnabled) {
                paintChrome((Graphics2D) graphics, BRIGHTER, width, height);
            }
        } else {
            graphics.setColor(solidColor);
            graphics.fillRect(0, 0, width, height);
            if (isChromeEnabled) {
                paintChrome((Graphics2D) graphics, 0, width, height);
            }
        }
    }

    private void paintChrome(Graphics2D graphics2D, int darkValue, int width, int height) {
        List gradientList = (List) UIManager.get("Button.gradient");
        drawVerticalGradient(graphics2D, gradientList, darkValue, width, height);
    }

    /****************************************************************************************************
     * Paints the default chrome button text
     ***************************************************************************************************/
    private void paintDefaultText(Graphics graphics, AbstractButton button, Rectangle textRect, String text) {
        if (text == null || text.length() == 0) {
            return;
        }
        ButtonModel buttonModel = button.getModel();
        int shiftOffset = 0;
        int textOffset = 0;

        if (buttonModel.isPressed() || buttonModel.isSelected()) {
            shiftOffset = 1;
        }

        if (button.getHorizontalAlignment() == SwingConstants.LEFT) {
            textOffset = textOffset + 4;
        } else if (button.getHorizontalAlignment() == SwingConstants.RIGHT) {
            textOffset = textOffset - 4;
        }

        int xCoordinate = textRect.x + shiftOffset + textOffset;
        int yCoordinate = textRect.y + graphics.getFontMetrics().getAscent() + shiftOffset;

        graphics.setColor(button.getForeground());
        graphics.setFont(button.getFont());

        if (!buttonModel.isEnabled()) {
            graphics.setColor(ColorUtility.disabledTint(button.getForeground()));
        } else if (buttonModel.isPressed() || buttonModel.isRollover() || button.isFocusOwner()) {
            graphics.setColor(Color.BLACK);
            graphics.setFont(FontUtility.getBoldFont(button.getFont()));
        }

        BasicGraphicsUtils.drawString(graphics, text, buttonModel.getMnemonic(), xCoordinate, yCoordinate);
    }

    /**
     * Gradiant code taken from MetalButtonUI!
     */
    private void drawVerticalGradient(Graphics2D graphics, List gradientList, int tone, int width, int height) {
        List gradient = (List) UIManager.get("Button.gradient");
        float ratio1 = ((Number) gradient.get(0)).floatValue();
        float ratio2 = ((Number) gradient.get(1)).floatValue();
        Color color1 = (Color) gradient.get(2);
        Color color2 = (Color) gradient.get(3);
        Color color3 = (Color) gradient.get(4);

        int mid = (int) (ratio1 * height);
        int mid2 = (int) (ratio2 * height);
        float mid3 = (float) mid * 2 + mid2;
        float zero = 0f;

        if (tone == DARKER) {
            color1 = ColorUtility.slightlyDarker(color1);
            color2 = ColorUtility.slightlyDarker(color2);
            color3 = ColorUtility.slightlyDarker(color3);
        } else if (tone == BRIGHTER) {
            color1 = ColorUtility.slightlyBrighter(color1);
            color2 = ColorUtility.slightlyBrighter(color2);
            color3 = ColorUtility.slightlyBrighter(color3);
        }

        if (mid > 0) {
            graphics.setPaint(new GradientPaint(zero, zero, color1, zero, mid, color2, true));
            graphics.fillRect(0, 0, width, mid);
        }
        if (mid2 > 0) {
            graphics.setColor(color2);
            graphics.fillRect(0, mid, width, mid2);
        }
        if (mid > 0) {
            graphics.setPaint(new GradientPaint(zero, (float) mid + mid2, color2, zero, mid3, color1, true));
            graphics.fillRect(0, mid + mid2, width, mid);
        }
        if (height - mid * 2 - mid2 > 0) {
            graphics.setPaint(new GradientPaint(zero, mid3, color1, zero, height, color3, true));
            graphics.fillRect(0, mid * 2 + mid2, width, height - mid * 2 - mid2);
        }
    }
}
