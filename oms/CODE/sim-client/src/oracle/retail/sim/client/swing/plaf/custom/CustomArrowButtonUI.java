package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalButtonUI;
import oracle.retail.sim.client.swing.util.ChromeUtility;
import oracle.retail.sim.client.swing.util.ColorUtility;
import oracle.retail.sim.client.swing.widget.RArrowButton;

/******************************************************************************************
 * This class subclasses the standard MetalButtonUI in order to paint the button using
 * the chrome look and feel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class CustomArrowButtonUI extends MetalButtonUI implements Serializable {
    private static final long serialVersionUID = -4158289025700148168L;

    /******************************************************************************************
     * Creates a new ChromeButtonUI for a specific component.
     * <p>
     * @param component The component to create the ChromeButtonUI for.
     * @return The ChromeButtonUI object.
     *****************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        return new CustomArrowButtonUI();
    }

    /******************************************************************************************
     * Installs a UI for an AbstractButton component. Any other component will throw a
     * ClassCastException.
     * <p>
     * @param component An AbstractButton component.
     *****************************************************************************************/
    public void installUI(JComponent component) {
        super.installUI(component);

        AbstractButton button = (AbstractButton) component;
        button.setFocusPainted(true);
        button.setRolloverEnabled(true);
    }

    /******************************************************************************************
     * Paints the component using the graphics context.
     *****************************************************************************************/
    public void paint(Graphics graphics, JComponent component) {
        RArrowButton button = (RArrowButton) component;
        int width = button.getSize().width;
        int height = button.getSize().height;

        paintDefaultBackground(graphics, button);

        if (height >= 5 && width >= 5) {
            boolean isPressed = button.getModel().isPressed();

            if (isPressed) {
                graphics.translate(1, 1);
            }

            // Draw the arrow
            int minSize = Math.min((height - 4) / 3 + 1, (width - 4) / 3 + 1);
            int size = Math.max(minSize, 2);

            paintArrow(graphics, button, (width - size) / 2 - 1, (height - size) / 2, size);

            // Reset the Graphics back to original settings
            if (isPressed) {
                graphics.translate(-1, -1);
            }
        }
    }

    /******************************************************************************************
     * Paints the default chrome button background
     ******************************************************************************************/
    private void paintDefaultBackground(Graphics graphics, AbstractButton button) {
        ButtonModel buttonModel = button.getModel();

        int width = button.getWidth();
        int height = button.getHeight();

        Color solidColor = button.getBackground();
        Color startColor = ColorUtility.getGradientStart(solidColor);

        if (!buttonModel.isEnabled()) {
            graphics.setColor(ColorUtility.disabledTint(solidColor));
            graphics.fillRect(0, 0, width, height);
        } else if (buttonModel.isPressed()) {
            graphics.setColor(solidColor);

            ChromeUtility.paintChrome(graphics, 2, 2, width - 4, height - 4, solidColor, startColor);
        } else if (buttonModel.isPressed()) {
            graphics.setColor(ColorUtility.disabledTint(solidColor));
            graphics.fillRect(0, 0, width, height);
        } else if (buttonModel.isRollover() || button.isFocusOwner()) {
            graphics.setColor(ColorUtility.slightlyBrighter(solidColor));
            graphics.fillRect(0, 0, width, height);

            ChromeUtility.paintChrome(graphics, 2, 2, width - 4, height - 4, ColorUtility.slightlyBrighter(startColor), ColorUtility.slightlyBrighter(solidColor));
        } else {
            graphics.setColor(solidColor);
            graphics.fillRect(0, 0, width, height);
            ChromeUtility.paintChrome(graphics, 2, 2, width - 4, height - 4, startColor, solidColor);
        }
    }

    /******************************************************************************************
     * Paints the default chrome arrow button.
     *****************************************************************************************/
    private void paintArrow(Graphics graphics, RArrowButton button, int x, int y, int size) {
        Color oldColor = graphics.getColor();

        int column = 0;
        int midPoint = size / 2;

        graphics.translate(x, y);
        graphics.setColor(button.getForeground());

        switch (button.getDirection()) {
            case BasicArrowButton.NORTH:
                for (int row = 0; row < size; row++) {
                    graphics.drawLine(midPoint - row, row, midPoint + row, row);
                }
                break;
            case BasicArrowButton.SOUTH:
                column = 0;
                for (int row = size - 1; row >= 0; row--) {
                    graphics.drawLine(midPoint - row, column, midPoint + row, column);
                    column++;
                }
                break;
            case BasicArrowButton.WEST:
                for (int row = 0; row < size; row++) {
                    graphics.drawLine(row, midPoint - row, row, midPoint + row);
                }
                break;
            case BasicArrowButton.EAST:
                column = 1;
                for (int row = size - 1; row >= 0; row--) {
                    graphics.drawLine(column, midPoint - row, column, midPoint + row);
                    column++;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid direction - " + button.getDirection());
        }
        graphics.translate(-x, -y);
        graphics.setColor(oldColor);
    }
}
