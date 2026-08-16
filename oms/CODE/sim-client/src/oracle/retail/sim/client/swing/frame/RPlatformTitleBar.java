package oracle.retail.sim.client.swing.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.ChromeUtility;
import oracle.retail.sim.client.swing.util.ColorUtility;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * The title bar placed upon the RFrame.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RPlatformTitleBar extends JPanel {
    private static final long serialVersionUID = 4598247790504111691L;

    private static final String METHOD_UNAVAILABLE = "Method is unavilable. Please use addTitleComponent().";

    private RPanel contentPanel = new RPanel();
    private JPanel buttonPanel = new JPanel();

    private RLabel iconLabel = new RLabel();
    private RLabel titleLabel = new RLabel();

    private RPlatformTitleButton minimizeButton = new RPlatformTitleButton(RPlatformTitleButton.MINIMIZE);
    private RPlatformTitleButton maximizeButton = new RPlatformTitleButton(RPlatformTitleButton.MAXIMIZE);
    private RPlatformTitleButton closeButton = new RPlatformTitleButton(RPlatformTitleButton.CLOSE);

    private static final Color NEARLY_WHITE = new Color(235, 233, 246);
    private static final Color VERY_LIGHT_GRAY = new Color(221, 221, 233);
    private static final Color MEDIUM_DARK_GRAY = new Color(171, 167, 190);
    private static final Color MEDIUM_GRAY = new Color(175, 171, 194);
    private static final Color LIGHT_GRAY = new Color(200, 199, 217);
    private static final Color BASIC_GRAY = new Color(190, 191, 212);
    private static final Color DARK_GRAY = new Color(146, 144, 166);

    private boolean chromeOverride;

    /****************************************************************************************************
     * Creates a new title bar.
     ***************************************************************************************************/
    public RPlatformTitleBar() {
        initializeIcon();
        initializeButtons();
        initializeWidgets();
        initializeFontAndColors();

        setLayout(new GridBagLayout());
        addImpl(iconLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1), -1);
        addImpl(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 3), -1);
        addImpl(contentPanel, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 3), -1);
        addImpl(buttonPanel, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0), -1);
    }

    /****************************************************************************************************
     * Initializes the icon.
     ***************************************************************************************************/
    private void initializeIcon() {
        setIcon(UIManager.getIcon(UIThemeName.TITLEBAR_ICON));
    }

    /****************************************************************************************************
     * This method builds the button panel section of the toolbar.
     ***************************************************************************************************/
    private void initializeButtons() {
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
        buttonPanel.setOpaque(false);

        buttonPanel.add(minimizeButton);
        buttonPanel.add(maximizeButton);
        buttonPanel.add(closeButton);
    }

    /****************************************************************************************************
     * This method builds the widget section of the toolbar.
     ***************************************************************************************************/
    private void initializeWidgets() {
        contentPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 3, 0));
        contentPanel.setOpaque(false);

        iconLabel.setHorizontalAlignment(SwingConstants.LEFT);
    }

    /****************************************************************************************************
     * Initializes the fonts and colors used by the title bar.
     ***************************************************************************************************/
    private void initializeFontAndColors() {
        setBackground(UIManager.getColor(UIThemeName.TITLEBAR_BACKGROUND));

        MatteBorder outerBorder = new MatteBorder(1, 1, 0, 1, UIManager.getColor(UIThemeName.FRAME_BORDER_COLOR));
        EmptyBorder innerBorder = new EmptyBorder(2, 2, 2, 2);

        setBorder(new CompoundBorder(outerBorder, innerBorder));
    }

    /****************************************************************************************************
     * Assigns the title to the title bar.
     * <p>
     * @param title There will be no attempt to translate this title.
     ***************************************************************************************************/
    public void setTitle(String title) {
        titleLabel.setText(title);
    }

    /****************************************************************************************************
     * Adds a component to the title bar.
     * <p>
     * @param component The component to add.
     ***************************************************************************************************/
    public void addTitleComponent(JComponent component) {
        if (component != null) {
            component.setOpaque(false);
            component.setFocusable(false);
            contentPanel.add(component);
        }
    }

    /****************************************************************************************************
     * Removes a title component from the title bar.
     * <p>
     * @param component The component to remove.
     ***************************************************************************************************/
    public void removeTitleComponent(JComponent component) {
        if (component != null) {
            contentPanel.remove(component);
        }
    }

    /****************************************************************************************************
     * Sets whether or not the chrome override is on. If the chrome override is set to "true", the title
     * bar paints the Project202 patterns regardless of other settings. If the chrome override is set to
     * "false", the title bar still paints a chrome pattern but it is a generic chrome pattern that uses
     * the settings on the title bar.
     * <p>
     * @param override True if the chrome should paint hardcoded, false if the chrome should paint
     *            generic.
     ***************************************************************************************************/
    public void setChromeOverride(boolean override) {
        chromeOverride = override;
        repaint();
    }

    /****************************************************************************************************
     * Assigns the listener for the minimize button.
     ***************************************************************************************************/
    protected void addMinimizeListener(ActionListener listener) {
        minimizeButton.addActionListener(listener);
    }

    /****************************************************************************************************
     * Assigns the listener for the maximize/restore button.
     ***************************************************************************************************/
    protected void addMaximizeListener(ActionListener listener) {
        maximizeButton.addActionListener(listener);
    }

    /****************************************************************************************************
     * Assigns the listener for the close button.
     ***************************************************************************************************/
    protected void addCloseListener(ActionListener listener) {
        closeButton.addActionListener(listener);
    }

    /****************************************************************************************************
     * This method swaps the restore button state of the title bar.
     ***************************************************************************************************/
    protected void swapRestoreButtonState() {
        maximizeButton.swapRestoreButtonState();
    }

    /****************************************************************************************************
     * Assigns the icon displayed in the left hand corner of the title bar.
     * <p>
     * @param icon The icon to assign.
     ***************************************************************************************************/
    protected void setIcon(Icon icon) {
        if (icon != null) {
            iconLabel.setIcon(icon, StringConstants.EMPTY);
        }
    }

    /****************************************************************************************************
     * Assigns the title font to the title bar.
     * <p>
     * @param font The font to assign.
     ***************************************************************************************************/
    protected void setTitleFont(Font font) {
        if (font != null) {
            titleLabel.setFont(font);
        }
    }

    /****************************************************************************************************
     * Assigns the title color to the title bar.
     * <p>
     * @param color The color to assign.
     ***************************************************************************************************/
    protected void setTitleColor(Color color) {
        if (color != null) {
            titleLabel.setForeground(color);
        }
    }

    /****************************************************************************************************
     * Adds the specified component to the end of this container. This method has been overridden and
     * made unavailable.
     * <p>
     * @param component The component to be added.
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(Component component) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container. It is strongly advised to use the 1.1 method,
     * add(Component, Object), in place of this method. This method has been overridden and made
     * unavailable.
     * <p>
     * @param name A string name for the component.
     * @param component The component to be added.
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(String name, Component component) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container at the given index. This method has been overridden
     * and made unavailable.
     * <p>
     * @param component The component to be added
     * @param index Position to add the component
     * @return The component argument.
     ***************************************************************************************************/
    public Component add(Component component, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to the end of this container. This method has been overridden and
     * made unavailable.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     ***************************************************************************************************/
    public void add(Component component, Object constraints) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Adds the specified component to this container with the specified constraints at the specified
     * index. Also notifies the layout manager to add the component to the this container's layout using
     * the specified constraints object.
     * <p>
     * @param component The component to be added
     * @param constraints An object expressing layout contraints for this
     * @param index The position in the container's list at which to insert the component. -1 means
     *            insert at the end.
     ***************************************************************************************************/
    public void add(Component component, Object constraints, int index) {
        throw new RuntimeException(METHOD_UNAVAILABLE);
    }

    /****************************************************************************************************
     * Paints the background of the title bar.
     * <p>
     * @param graphics The Graphics object to paint.
     ***************************************************************************************************/
    public void paintComponent(Graphics graphics) {
        if (chromeOverride) {
            paintChromeComponent(graphics);
            return;
        }
        int width = (int) getSize().getWidth() - 1;
        int height = (int) getSize().getHeight();

        Color finalColor = getBackground();
        Color startColor = ColorUtility.getGradientStart(finalColor);

        ChromeUtility.paintCenteredChrome(graphics, 0, 0, width, height, startColor, finalColor);
    }

    /****************************************************************************************************
     * Paints the default hard-coded title bar from Project 202.
     * <p>
     * @param graphics The Graphics object to paint.
     ***************************************************************************************************/
    private void paintChromeComponent(Graphics graphics) {
        int width = (int) getSize().getWidth() - 1;
        int end = (int) getSize().getHeight() - 23;

        graphics.setColor(VERY_LIGHT_GRAY);
        graphics.drawLine(0, 0, width, 0);

        ChromeUtility.paintChrome(graphics, 0, 1, width, 3, NEARLY_WHITE, Color.WHITE);
        ChromeUtility.paintChrome(graphics, 0, 4, width, 2, Color.WHITE, NEARLY_WHITE);
        ChromeUtility.paintChrome(graphics, 0, 6, width, 12, VERY_LIGHT_GRAY, MEDIUM_DARK_GRAY);
        ChromeUtility.paintChrome(graphics, 0, 18, width, 5, MEDIUM_GRAY, LIGHT_GRAY);
        ChromeUtility.paintChrome(graphics, 0, 23, width, end, BASIC_GRAY, DARK_GRAY);
    }
}
