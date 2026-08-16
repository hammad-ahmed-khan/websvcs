package oracle.retail.sim.client.swing.frame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.ChromeUtility;

/*****************************************************************************************************
 * A title button that appears on the title bar.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************************/

public class RPlatformTitleButton extends JButton {
    private static final long serialVersionUID = 7801081406773991696L;

    public static final int MINIMIZE = 0;
    public static final int MAXIMIZE = 1;
    public static final int RESTORE = 2;
    public static final int CLOSE = 3;

    private Color foreground = Color.BLACK;
    private Color startBackground = Color.WHITE;
    private Color finisBackground = Color.GRAY;

    private Color borderHighlight = Color.WHITE;
    private Color borderShadow = Color.GRAY;
    private Color borderColor = Color.BLACK;

    private int buttonType = MINIMIZE;

    /*****************************************************************************************************
     * Creates a new title button with the assigned type.
     * <p>
     * @param type The type of the button (MINIMIZE, MAXIMIZE, RESTORE, CLOSE).
     *****************************************************************************************************/
    public RPlatformTitleButton(int type) {
        setMinimumSize(new Dimension(20, 19));
        setPreferredSize(new Dimension(20, 19));
        setRolloverEnabled(true);
        setFocusable(false);
        buttonType = type;
        initializeColors();
    }

    /*****************************************************************************************************
     * Initializes the colors.
     *****************************************************************************************************/
    protected void initializeColors() {
        borderHighlight = UIManager.getColor(UIThemeName.TITLEBAR_BUTTON_BORDER_HIGHLIGHT);
        borderShadow = UIManager.getColor(UIThemeName.TITLEBAR_BUTTON_BORDER_SHADOW);
        borderColor = UIManager.getColor(UIThemeName.TITLEBAR_BUTTON_BORDER_COLOR);
        foreground = UIManager.getColor(UIThemeName.TITLEBAR_BUTTON_FOREGROUND);

        if (buttonType == CLOSE) {
            startBackground = UIManager.getColor(UIThemeName.TITLEBAR_BUTTON_CLOSE_START_BACKGROUND);
            finisBackground = UIManager.getColor(UIThemeName.TITLEBAR_BUTTON_CLOSE_FINISH_BACKGROUND);
        } else {
            startBackground = UIManager.getColor(UIThemeName.TITLEBAR_BUTTON_START_BACKGROUND);
            finisBackground = UIManager.getColor(UIThemeName.TITLEBAR_BUTTON_FINISH_BACKGROUND);
        }
    }

    /*****************************************************************************************************
     * Assigns maximize state of maximize button. If the button is maximize or restore, this sets the type
     * to the alternate type.
     *****************************************************************************************************/
    public void swapRestoreButtonState() {
        if (buttonType == MAXIMIZE) {
            buttonType = RESTORE;
        } else if (buttonType == RESTORE) {
            buttonType = MAXIMIZE;
        }
    }

    /*****************************************************************************************************
     * Paints the component by figuring out which button type it is and painting it appropriately.
     *****************************************************************************************************/
    protected void paintComponent(Graphics graphics) {
        int width = getWidth();
        int height = getHeight();
        switch (buttonType) {
            case MINIMIZE:
                paintMinimizeButton(graphics, width, height);
                break;
            case MAXIMIZE:
                paintMaximizeButton(graphics, width, height);
                break;
            case RESTORE:
                paintRestoreButton(graphics, width, height);
                break;
            case CLOSE:
                paintCloseButton(graphics, width, height);
                break;
            default:
                break;
        }
    }

    /*****************************************************************************************************
     * Paints the border around the button.
     *****************************************************************************************************/
    protected void paintBorder(Graphics graphics) {
        int width = (int) getSize().getWidth();
        int height = (int) getSize().getHeight();

        graphics.setColor(borderColor);
        graphics.drawLine(2, 1, width - 3, 1);
        graphics.drawLine(1, 2, 1, height - 2);
        graphics.drawLine(2, height - 1, width - 3, height - 1);
        graphics.drawLine(width - 2, 2, width - 2, height - 2);

        boolean isPressed = getModel().isPressed();

        if (isPressed) {
            graphics.setColor(borderHighlight);
        } else {
            graphics.setColor(borderShadow);
        }

        graphics.drawLine(1, 0, width - 2, 0);
        graphics.drawLine(0, 1, 1, 1);
        graphics.drawLine(0, 2, 0, height - 1);
        graphics.drawLine(0, height - 1, 1, height - 1);

        if (isPressed) {
            graphics.setColor(borderShadow);
        } else {
            graphics.setColor(borderHighlight);
        }

        graphics.drawLine(width - 2, 1, width - 1, 1);
        graphics.drawLine(width - 1, 1, width - 1, height - 1);
        graphics.drawLine(width - 2, height - 1, width - 2, height - 1);

        graphics.setColor(Color.WHITE);
        graphics.drawLine(0, 0, 0, 0);
        graphics.drawLine(width - 1, 0, width - 1, 0);
    }

    /*****************************************************************************************************
     * Paints the minimized button.
     *****************************************************************************************************/
    protected void paintMinimizeButton(Graphics graphics, int width, int height) {
        if (getModel().isRollover() && !getModel().isPressed()) {
            graphics.setColor(Color.white);
            graphics.fillRect(2, 2, 16, 7);
            ChromeUtility.paintChrome(graphics, 2, 9, width - 3, 9, Color.white, finisBackground);
        } else if (getModel().isPressed()) {
            ChromeUtility.paintChrome(graphics, 2, 2, width - 3, height - 2, finisBackground, startBackground);
        } else {
            ChromeUtility.paintChrome(graphics, 2, 2, width - 3, height - 2, startBackground, finisBackground);
        }
        graphics.setColor(foreground);
        graphics.fillRect(5, width - 7, 7, 3);
    }

    /*****************************************************************************************************
     * Paint maximize version of maximize button.
     *****************************************************************************************************/
    protected void paintMaximizeButton(Graphics graphics, int width, int height) {
        ButtonModel model = getModel();

        if (model.isRollover() && !model.isPressed()) {
            graphics.setColor(Color.white);
            graphics.fillRect(2, 2, 16, 7);
            ChromeUtility.paintChrome(graphics, 2, 9, width - 3, 9, Color.white, finisBackground);
        } else if (model.isPressed()) {
            ChromeUtility.paintChrome(graphics, 2, 2, width - 3, height - 2, finisBackground, startBackground);
        } else {
            ChromeUtility.paintChrome(graphics, 2, 2, width - 3, height - 2, startBackground, finisBackground);
        }
        graphics.setColor(foreground);
        graphics.drawRect(5, 5, width - 10, height - 9);
        graphics.fillRect(5, 5, width - 10, 3);
    }

    /*****************************************************************************************************
     * Paint restore version of maximize button.
     *****************************************************************************************************/
    protected void paintRestoreButton(Graphics graphics, int width, int height) {
        ButtonModel model = getModel();

        if (model.isRollover() && !model.isPressed()) {
            graphics.setColor(Color.white);
            graphics.fillRect(2, 2, 16, 7);
            ChromeUtility.paintChrome(graphics, 2, 9, width - 3, 9, Color.white, finisBackground);
        } else if (model.isPressed()) {
            ChromeUtility.paintChrome(graphics, 2, 2, width - 3, height - 2, finisBackground, startBackground);
        } else {
            ChromeUtility.paintChrome(graphics, 2, 2, width - 3, height - 2, startBackground, finisBackground);
        }
        graphics.setColor(foreground);
        graphics.fillRect(8, 4, 8, 2);
        graphics.drawLine(8, 6, 8, 6);
        graphics.drawLine(15, 6, 15, 10);
        graphics.drawLine(14, 10, 14, 10);
        graphics.drawRect(5, 8, 7, 7);
        graphics.fillRect(5, 8, 7, 2);
    }

    /*****************************************************************************************************
     * Paint close button.
     *****************************************************************************************************/
    protected void paintCloseButton(Graphics graphics, int width, int height) {
        ButtonModel model = getModel();

        if (model.isRollover() && !model.isPressed()) {
            graphics.setColor(Color.white);
            graphics.fillRect(2, 2, 16, 3);
            ChromeUtility.paintChrome(graphics, 2, 5, width - 3, 13, Color.white, finisBackground);
        } else if (model.isPressed()) {
            ChromeUtility.paintChrome(graphics, 2, 2, width - 3, height - 2, finisBackground, startBackground);
        } else {
            ChromeUtility.paintChrome(graphics, 2, 2, width - 3, height - 2, startBackground, finisBackground);
        }
        graphics.setColor(foreground);
        graphics.drawLine(4, 5, width - 6, height - 5);
        graphics.drawLine(5, 5, width - 5, height - 5);
        graphics.drawLine(6, 5, width - 4, height - 5);
        graphics.drawLine(width - 6, 5, 4, height - 5);
        graphics.drawLine(width - 5, 5, 5, height - 5);
        graphics.drawLine(width - 4, 5, 6, height - 5);
    }
}
