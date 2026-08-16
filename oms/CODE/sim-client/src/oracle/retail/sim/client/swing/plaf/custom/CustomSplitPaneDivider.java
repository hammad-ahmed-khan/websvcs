package oracle.retail.sim.client.swing.plaf.custom;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import oracle.retail.sim.client.swing.util.ChromeUtility;
import oracle.retail.sim.client.swing.util.ColorUtility;

/*****************************************************************************************************
 * Chrome split pane divider. This overrides the paint methods of BasicSplitPaneDivider.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************************/

public class CustomSplitPaneDivider extends BasicSplitPaneDivider {
    private static final long serialVersionUID = 4597209742507870549L;

    private static final int PIXELS_PER_BUTTON = 5;
    private static final int NUMBER_OF_BUTTONS = 8;

    // ONE TOUCH COLORS
    private static final Color NEAR_WHITE = new Color(222, 223, 230);
    private static final Color DARK_GRAY = new Color(114, 116, 130);
    private static final Color DARK_RED = new Color(123, 0, 0);

    /*****************************************************************************************************
     * Constructs a split pane divider.
     *****************************************************************************************************/
    public CustomSplitPaneDivider(BasicSplitPaneUI splitPaneUI) {
        super(splitPaneUI);
        setLayout(new DividerLayout());
        setBackground(UIManager.getColor(UIThemeName.SLIDER_BACKGROUND));
    }

    /*****************************************************************************************************
     * Retrieves the split pane.
     *****************************************************************************************************/
    protected JSplitPane getSplitPane() {
        return splitPane;
    }

    /*****************************************************************************************************
     * Paints the split pane divider. Paint sthe chrome background and then draws the little bumps.
     *****************************************************************************************************/
    public void paint(Graphics graphics) {
        int x = 0;
        int y = 0;
        int width = (int) getSize().getWidth();
        int height = (int) getSize().getHeight();

        Color finalColor = getBackground();
        Color startColor = ColorUtility.getGradientStart(finalColor);
        int repeat = width / PIXELS_PER_BUTTON;

        if (orientation == JSplitPane.HORIZONTAL_SPLIT) {
            ChromeUtility.paintChrome(graphics, x, y, width, height, startColor, finalColor, ChromeUtility.LEFT_TO_RIGHT);

            int xloc = -1;
            int startY = 0;
            for (int repeater = 0; repeater < repeat; repeater++) {
                startY = height / 2 - PIXELS_PER_BUTTON * NUMBER_OF_BUTTONS / 2;
                for (int yoff = 0; yoff < PIXELS_PER_BUTTON * NUMBER_OF_BUTTONS; yoff = yoff + PIXELS_PER_BUTTON) {
                    paintMarks(graphics, xloc, yoff + startY, orientation);
                }
                xloc = xloc + 6;
            }
        } else {
            ChromeUtility.paintChrome(graphics, x, y, width, height, startColor, finalColor);

            int yloc = -1;
            int startX = 0;
            for (int repeater = 0; repeater < repeat; repeater++) {
                startX = width / 2 - PIXELS_PER_BUTTON * NUMBER_OF_BUTTONS / 2;
                for (int i = 0; i < PIXELS_PER_BUTTON * NUMBER_OF_BUTTONS; i = i + PIXELS_PER_BUTTON) {
                    paintMarks(graphics, i + startX, yloc, orientation);
                }
                yloc = yloc + 6;
            }
        }
        super.paint(graphics);
    }

    /*****************************************************************************************************
     * Paints the bump marks on the split pane divider
     *****************************************************************************************************/
    private void paintMarks(Graphics graphics, int x, int y, int markOrientation) {
        if (markOrientation == JSplitPane.VERTICAL_SPLIT) {
            graphics.setColor(UIManager.getColor(UIThemeName.SLIDER_DARK));
            graphics.drawLine(x + 1, y + 2, x + 1, y + 4);
            graphics.drawLine(x + 2, y + 2, x + 2, y + 2);
            graphics.setColor(UIManager.getColor(UIThemeName.SLIDER_MEDIUM_DARK));
            graphics.drawLine(x, y + 5, x + 4, y + 5);
            graphics.setColor(UIManager.getColor(UIThemeName.SLIDER_MEDIUM_LIGHT));
            graphics.drawLine(x, y + 2, x, y + 5);
            graphics.drawLine(x + 3, y + 2, x + 3, y + 4);
            graphics.drawLine(x + 4, y + 3, x + 4, y + 4);
            graphics.setColor(UIManager.getColor(UIThemeName.SLIDER_LIGHT));
            graphics.drawLine(x + 1, y + 5, x + 1, y + 5);
            graphics.drawLine(x + 2, y + 3, x + 2, y + 5);
            graphics.drawLine(x + 4, y + 2, x + 4, y + 2);
        } else {
            graphics.setColor(UIManager.getColor(UIThemeName.SLIDER_DARK));
            graphics.drawLine(x + 2, y + 1, x + 4, y + 1);
            graphics.drawLine(x + 2, y + 2, x + 2, y + 2);
            graphics.setColor(UIManager.getColor(UIThemeName.SLIDER_MEDIUM_DARK));
            graphics.drawLine(x + 5, y, x + 5, y + 4);
            graphics.setColor(UIManager.getColor(UIThemeName.SLIDER_MEDIUM_LIGHT));
            graphics.drawLine(x + 2, y, x + 5, y);
            graphics.drawLine(x + 2, y + 3, x + 4, y + 3);
            graphics.drawLine(x + 3, y + 4, x + 4, y + 4);
            graphics.setColor(UIManager.getColor(UIThemeName.SLIDER_LIGHT));
            graphics.drawLine(x + 5, y + 1, x + 5, y + 1);
            graphics.drawLine(x + 3, y + 2, x + 5, y + 2);
            graphics.drawLine(x + 2, y + 4, x + 2, y + 4);
        }
    }

    /*****************************************************************************************************
     * Create Right One Touch Button
     *****************************************************************************************************/
    protected JButton createRightOneTouchButton() {
        JButton button = new OneTouchButton(false);
        button.repaint();
        return button;
    }

    /*****************************************************************************************************
     * Create Left One Touch Button
     *****************************************************************************************************/
    protected JButton createLeftOneTouchButton() {
        JButton button = new OneTouchButton(true);
        button.repaint();
        return button;
    }

    /*****************************************************************************************************
     *
     * INNER CLASS ONE TOUCH BUTTON
     *
     *****************************************************************************************************/
    private class OneTouchButton extends JButton {
        private static final long serialVersionUID = -1887971194929944818L;

        private boolean isLeft;

        /*****************************************************************************************************
         * Constructs a one touch button.
         *****************************************************************************************************/
        public OneTouchButton(boolean isLeft) {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
            this.isLeft = isLeft;
        }

        /*****************************************************************************************************
         * No border may be assigned.
         *****************************************************************************************************/
        public void setBorder(Border border) {
        }

        /*****************************************************************************************************
         * One touch buttons are never focusable
         *****************************************************************************************************/
        public boolean isFocusable() {
            return false;
        }

        /*****************************************************************************************************
         * Paints a one touch button
         *****************************************************************************************************/
        public void paint(Graphics graphics) {
            if (isLeft) {
                paintLeft((Graphics2D) graphics);
            } else {
                paintRight((Graphics2D) graphics);
            }
        }

        /*****************************************************************************************************
         * Handles painting a left one touch button.
         *****************************************************************************************************/
        private void paintLeft(Graphics2D graphics) {
            int x = 0;
            int y = 0;
            if (getSplitPane().getOrientation() == JSplitPane.VERTICAL_SPLIT) {
                int[] xPos = { x - 1, x + 5, x + 11 };
                int[] yPos = { y + 5, y - 1, y + 5 };

                graphics.setColor(DARK_RED);
                graphics.fillPolygon(xPos, yPos, 3);
                graphics.setColor(DARK_GRAY);
                graphics.drawLine(x + 5, y - 1, x + 11, y + 5);
                graphics.setColor(NEAR_WHITE);
                graphics.drawLine(x - 1, y + 5, x + 5, y - 1);
            } else {
                int[] xPos = { x, x + 5, x + 5 };
                int[] yPos = { y + 5, y - 1, y + 11 };

                graphics.setColor(DARK_RED);
                graphics.fillPolygon(xPos, yPos, 3);
                graphics.setColor(DARK_GRAY);
                graphics.drawLine(x + 4, y, x, y + 4);
                graphics.setColor(NEAR_WHITE);
                graphics.drawLine(x, y + 6, x + 4, y + 10);
            }
        }

        /*****************************************************************************************************
         * Handles painting a right one touch button.
         *****************************************************************************************************/
        private void paintRight(Graphics2D graphics) {
            int x = 0;
            int y = 0;
            if (getSplitPane().getOrientation() == JSplitPane.VERTICAL_SPLIT) {
                int[] xPos = { x - 1, x + 11, x + 5 };
                int[] yPos = { y - 1, y - 1, y + 5 };

                graphics.setColor(DARK_RED);
                graphics.fillPolygon(xPos, yPos, 3);
                graphics.setColor(DARK_GRAY);
                graphics.drawLine(x + 11, y - 1, x + 5, y + 5);
                graphics.setColor(NEAR_WHITE);
                graphics.drawLine(x - 1, y - 1, y + 5, y + 5);
            } else {
                int[] xPos = { x + 5, x, x };
                int[] yPos = { y + 5, y - 1, y + 11 };

                graphics.setColor(DARK_RED);
                graphics.fillPolygon(xPos, yPos, 3);
                graphics.setColor(DARK_GRAY);
                graphics.drawLine(x, y, x + 4, y + 4);
                graphics.setColor(NEAR_WHITE);
                graphics.drawLine(x + 4, y + 6, x, y + 10);
            }
        }
    }

    /*****************************************************************************************************
     *
     * INNER CLASS LAYOUT MANAGER. Handles the layout of the one touch buttons on the divider.
     *
     *****************************************************************************************************/

    protected class DividerLayout implements LayoutManager {

        /*****************************************************************************************************
         * Lays out the container. Determines if the container is one touch expandable and alters the layout
         * based on that property.
         *****************************************************************************************************/
        public void layoutContainer(Container container) {
            if (splitPane.isOneTouchExpandable()) {
                if (leftButton == null) {
                    leftButton = createLeftOneTouchButton();
                }
                if (rightButton == null) {
                    rightButton = createRightOneTouchButton();
                }
                setButtonsVisible(true);
                layoutOneTouch(container);
            } else {
                setButtonsVisible(false);
            }
        }

        private void setButtonsVisible(boolean visible) {
            if (leftButton != null) {
                leftButton.setVisible(visible);
            } else if (rightButton != null) {
                rightButton.setVisible(visible);
            }
        }

        /*****************************************************************************************************
         * Specific code for laying out the one touch option.
         *****************************************************************************************************/
        private void layoutOneTouch(Container container) {
            Insets insets = getInsets();

            if (orientation == JSplitPane.VERTICAL_SPLIT) {
                int dividerWidth = getDividerSize();
                if (insets != null) {
                    dividerWidth = dividerWidth - (insets.top + insets.bottom);
                }
                int yloc = (dividerWidth - ONE_TOUCH_SIZE) / 2;
                if (yloc < 0) {
                    yloc = 0;
                }
                int xloc = container.getWidth() / 5;
                if (xloc < 2) {
                    xloc = 2;
                }
                dividerWidth = Math.min(dividerWidth, ONE_TOUCH_SIZE);
                leftButton.setBounds(xloc, yloc, 12, 6);
                rightButton.setBounds(xloc + ONE_TOUCH_SIZE * 2, yloc, 12, 6);
            } else {
                int dividerWidth = getDividerSize();
                if (insets != null) {
                    dividerWidth = dividerWidth - (insets.left + insets.right);
                }
                dividerWidth = Math.min(dividerWidth, ONE_TOUCH_SIZE);
                int xloc = (container.getSize().width - dividerWidth) / 2;
                int yloc = container.getSize().height / 4 + ONE_TOUCH_OFFSET;
                leftButton.setBounds(xloc, yloc, 6, 12);
                rightButton.setBounds(xloc, yloc + ONE_TOUCH_SIZE * 2, 6, 12);
            }
        }

        public Dimension minimumLayoutSize(Container container) {
            return new Dimension(0, 0);
        }

        public Dimension preferredLayoutSize(Container container) {
            return new Dimension(0, 0);
        }

        public void addLayoutComponent(String string, Component container) {
        }

        public void removeLayoutComponent(Component container) {
        }
    }
}
