package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;
import oracle.retail.sim.client.swing.widget.RCheckBox;

/******************************************************************************************
 * This class contains a set of static icons for drawing.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class CustomIcons {

    private static Icon checkBoxIcon;

    /******************************************************************************************
     * Creates the icon for painting the box and check for a CheckBox widget.
     * <p>
     * @return The check box icon.
     *****************************************************************************************/
    public static Icon createCheckBoxIcon() {
        if (checkBoxIcon == null) {
            checkBoxIcon = new CheckBoxIcon();
        }
        return checkBoxIcon;
    }

    /******************************************************************************************
     * Creates the icon to display for a tree expanded node.
     * <p>
     * @return The tree expanded icon.
     *****************************************************************************************/
    public static Icon createTreeExpandIcon() {
        return new TreeExpandedIcon();
    }

    /******************************************************************************************
     * Creates the icon to display for a tree collapsed node.
     * <p>
     * @return The tree collapsed icon.
     *****************************************************************************************/
    public static Icon createTreeCollapsedIcon() {
        return new TreeCollapsedIcon();
    }

    /******************************************************************************************
     * Creates the icon for a tree leaf.
     * <p>
     * @return The tree leaf icon.
     *****************************************************************************************/
    public static Icon createTreeLeafIcon() {
        return new TreeLeafIcon();
    }

    /******************************************************************************************
     *
     * CHECK BOX ICON
     *
     *****************************************************************************************/
    private static class CheckBoxIcon implements Icon, UIResource {

        /******************************************************************************************
         * Paints the icon.
         * <p>
         * @param component The component to paint.
         * @param graphics The Graphics object of the component to use to paint.
         * @param x The x coordinate.
         * @param y The y coordinate.
         ******************************************************************************************/
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            if (component instanceof RCheckBox) {
                paintValidIcon(component, graphics, x, y);
            } else {
                paintInvalidIcon(component, graphics, x, y);
            }
        }

        /******************************************************************************************
         * Paints the valid icon.
         * <p>
         * @param component The component to paint.
         * @param graphics The Graphics object of the component to use to paint.
         * @param x The x coordinate.
         * @param y The y coordinate.
         ******************************************************************************************/
        public void paintValidIcon(Component component, Graphics graphics, int x, int y) {
            RCheckBox checkBox = (RCheckBox) component;
            ButtonModel model = checkBox.getModel();
            int controlSize = getControlSize();

            Color background;
            Color foreground;

            if (model.isEnabled() && checkBox.isFocusOwner()) {
                background = UIManager.getColor(UIThemeName.CHECKBOX_FOCUSED_BOX_BACKGROUND);
                foreground = UIManager.getColor(UIThemeName.CHECKBOX_FOCUSED_BOX_FOREGROUND);
            } else if (model.isEnabled()) {
                background = UIManager.getColor(UIThemeName.CHECKBOX_DEFAULT_BOX_BACKGROUND);
                foreground = UIManager.getColor(UIThemeName.CHECKBOX_DEFAULT_BOX_FOREGROUND);
            } else if (UIManager.getBoolean(UIThemeName.SYSTEM_DISABLED_VALUE_ACTIVE)) {
                background = UIManager.getColor(UIThemeName.CHECKBOX_BACKGROUND);
                foreground = UIManager.getColor(UIThemeName.CHECKBOX_DISABLED_FOREGROUND);
            } else {
                background = UIManager.getColor(UIThemeName.CHECKBOX_BACKGROUND);
                foreground = UIManager.getColor(UIThemeName.CHECKBOX_DEFAULT_BOX_FOREGROUND);
            }

            int length = checkBox.getText().length();
            boolean translate = checkBox.getHorizontalTextPosition() == SwingConstants.RIGHT || length == 0;

            if (translate) {
                graphics.translate(-2, 0);
            }
            if (checkBox.isBackgroundPaintActivated()) {
                graphics.setColor(background);
                graphics.fillRect(x, y, controlSize - 1, controlSize - 1);
                graphics.setColor(UIManager.getColor(UIThemeName.CHECKBOX_DEFAULT_BOX_FOREGROUND));
                graphics.drawRect(x, y, controlSize - 1, controlSize - 1);
            }

            if (model.isSelected()) {
                drawCheck(checkBox, graphics, foreground, x, y);
            }
            if (translate) {
                graphics.translate(2, 0);
            }
        }

        /******************************************************************************************
         * Paints the invalid icon. This is a hack to solve the painting issues in platform.
         * Remove this code as soon as possible.
         ******************************************************************************************/
        private void paintInvalidIcon(Component component, Graphics graphics, int x, int y) {
            JCheckBox checkBox = (JCheckBox) component;
            ButtonModel model = checkBox.getModel();
            int controlSize = getControlSize();

            if (model.isEnabled()) {
                graphics.setColor(UIManager.getColor(UIThemeName.CHECKBOX_DEFAULT_BOX_BACKGROUND));
                graphics.fillRect(x, y, controlSize - 1, controlSize - 1);
                graphics.setColor(UIManager.getColor(UIThemeName.CHECKBOX_DEFAULT_BOX_FOREGROUND));
            } else {
                Color color = UIManager.getColor(UIThemeName.CHECKBOX_BACKGROUND);
                if (color == null) {
                    color = checkBox.getBackground();
                }
                graphics.setColor(color);
                graphics.fillRect(x, y, controlSize - 1, controlSize - 1);
                graphics.setColor(UIManager.getColor(UIThemeName.CHECKBOX_DISABLED_FOREGROUND));
            }
            if (checkBox.isEnabled()) {
                graphics.setColor(UIManager.getColor(UIThemeName.CHECKBOX_DEFAULT_BOX_FOREGROUND));
            } else {
                graphics.setColor(UIManager.getColor(UIThemeName.CHECKBOX_DISABLED_FOREGROUND));
            }
            graphics.drawRect(x, y, controlSize - 1, controlSize - 1);

            if (model.isSelected()) {
                Color fgColor = UIManager.getColor(UIThemeName.CHECKBOX_DISABLED_FOREGROUND);
                if (checkBox.isEnabled()) {
                    fgColor = UIManager.getColor(UIThemeName.CHECKBOX_DEFAULT_BOX_FOREGROUND);
                }
                drawCheck(checkBox, graphics, fgColor, x, y);
            }
        }

        /******************************************************************************************
         * Draw the "checkmark" for the icon.
         * <p>
         * @param checkBox The component to paint.
         * @param graphics The Graphics object of the component to use to paint.
         * @param foreground The color to paint the check.
         * @param x The x coordinate.
         * @param y The y coordinate.
         ******************************************************************************************/
        private void drawCheck(JCheckBox checkBox, Graphics graphics, Color foreground, int x, int y) {
            graphics.setColor(foreground);
            graphics.translate(-1, -1);
            graphics.drawLine(x + 4, y + 7, x + 6, y + 9);
            graphics.drawLine(x + 4, y + 8, x + 6, y + 10);
            graphics.drawLine(x + 4, y + 9, x + 6, y + 11);
            graphics.drawLine(x + 7, y + 8, x + 11, y + 4);
            graphics.drawLine(x + 7, y + 9, x + 11, y + 5);
            graphics.drawLine(x + 7, y + 10, x + 11, y + 6);
            graphics.translate(1, 1);
        }

        /******************************************************************************************
         * Retrieves the width of the icon.
         ******************************************************************************************/
        public int getIconWidth() {
            return getControlSize();
        }

        /******************************************************************************************
         * Retrieves the height of the icon.
         ******************************************************************************************/
        public int getIconHeight() {
            return getControlSize();
        }

        /******************************************************************************************
         * Retrieves the size of this control
         ******************************************************************************************/
        private int getControlSize() {
            return 13;
        }
    }

    /******************************************************************************************
     *
     * TREE EXPANDED ICON
     *
     *****************************************************************************************/

    private static class TreeExpandedIcon implements Icon, UIResource {

        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            graphics.setColor(Color.lightGray);
            graphics.fillRect(x + 1, y + 3, 7, 2);
            graphics.setColor(Color.black);
            graphics.fillRect(x, y + 2, 7, 2);
        }

        public int getIconWidth() {
            return 7;
        }

        public int getIconHeight() {
            return 7;
        }
    }

    /******************************************************************************************
     *
     * TREE EXPANDED ICON
     *
     *****************************************************************************************/

    private static class TreeCollapsedIcon implements Icon, UIResource {

        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            graphics.setColor(Color.lightGray);
            graphics.fillRect(x + 1, y + 3, 7, 2);
            graphics.fillRect(x + 3, y + 5, 2, 2);
            graphics.fillRect(x + 4, y + 1, 2, 2);
            graphics.setColor(Color.black);
            graphics.fillRect(x, y + 2, 7, 2);
            graphics.fillRect(x + 3, y, 2, 2);
            graphics.fillRect(x + 2, 4 + y, 2, 2);
        }

        public int getIconWidth() {
            return 7;
        }

        public int getIconHeight() {
            return 7;
        }
    }

    /******************************************************************************************
     * TREE LEAF ICON
     *****************************************************************************************/

    private static class TreeLeafIcon implements Icon, UIResource {

        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            x = x + 2;
            graphics.setColor(UIManager.getColor("Tree.selectionBackground").darker());
            graphics.drawLine(x + 2, y + 4, x + 3, y + 4);
            graphics.drawLine(x + 3, y + 3, x + 4, y + 3);
            graphics.drawLine(x + 4, y + 2, x + 4, y + 2);
            graphics.setColor(Color.lightGray);
            graphics.fillRect(x, y + 1, 4, 2);
            graphics.fillRect(x + 1, y, 2, 4);
            graphics.setColor(Color.white);
            graphics.fillRect(x + 1, y + 1, 2, 2);
        }

        public int getIconWidth() {
            return 8;
        }

        public int getIconHeight() {
            return 4;
        }
    }
}
