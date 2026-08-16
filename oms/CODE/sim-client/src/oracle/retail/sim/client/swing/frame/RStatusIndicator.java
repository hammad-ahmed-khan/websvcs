package oracle.retail.sim.client.swing.frame;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.widget.RLabel;

/******************************************************************************************
 * This class is a small indicator that can placed inside the status bar. It has an active
 * state and an off state.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RStatusIndicator extends RLabel {
    private static final long serialVersionUID = 8721135794792610221L;

    private Color activeBackground;
    private Color activeForeground;
    private Color inactiveBackground;
    private Color inactiveForeground;
    private Color iconBackground = Color.darkGray;

    private Icon activeIcon;
    private Icon inactiveIcon;

    private boolean isActive;

    /******************************************************************************************
     * Returns new RStatusIndicator object.
     *****************************************************************************************/
    public RStatusIndicator() {
        initialize();
    }

    /******************************************************************************************
     * Returns new RStatusIndicator object.
     * <p>
     * @param name The name to assign to the indicator.
     *****************************************************************************************/
    public RStatusIndicator(String name) {
        initialize();
        setName(name);
    }

    /******************************************************************************************
     * Returns new RStatusIndicator object.
     * <p>
     * @param name The name to assign to the indicator.
     * @param title The title to display in the status indicator.
     *****************************************************************************************/
    public RStatusIndicator(String name, String title) {
        super(title);
        initialize();
        setName(name);
    }

    /******************************************************************************************
     * Returns new RStatusIndicator object.
     * <p>
     * @param name The name to assign to the indicator.
     *@param activeIcon An icon to display when the button is active.
     *@param inactiveIcon An icon to display when the button is inactive.
     *****************************************************************************************/
    public RStatusIndicator(String name, Icon activeIcon, Icon inactiveIcon) {
        initialize();
        setName(name);
        setActiveIcon(activeIcon);
        setInactiveIcon(inactiveIcon);
    }

    /******************************************************************************************
     * Returns new RStatusIndicator object.
     * <p>
     *@param name The name to assign to the indicator.
     *@param title The title to display in the status indicator.
     *@param activeIcon An icon to display when the button is active.
     *@param inactiveIcon An icon to display when the button is inactive.
     *****************************************************************************************/
    public RStatusIndicator(String name, String title, Icon activeIcon, Icon inactiveIcon) {
        super(title);
        initialize();
        setName(name);
        setActiveIcon(activeIcon);
        setInactiveIcon(inactiveIcon);
    }

    /******************************************************************************************
     * Initializes the borders by loading the colors, setting the border and turning the
     * indicator active by default.
     *****************************************************************************************/
    protected void initialize() {
        setActiveBackground(UIManager.getColor(UIThemeName.STATUSBAR_INDICATOR_ACTIVE_BACKGROUND));
        setActiveForeground(UIManager.getColor(UIThemeName.STATUSBAR_INDICATOR_ACTIVE_FOREGROUND));
        setInactiveBackground(UIManager.getColor(UIThemeName.STATUSBAR_INDICATOR_INACTIVE_BACKGROUND));
        setInactiveForeground(UIManager.getColor(UIThemeName.STATUSBAR_INDICATOR_INACTIVE_FOREGROUND));
        setIconBackground(UIManager.getColor(UIThemeName.CHROME_PANEL_BACKGROUND));
        setHorizontalTextPosition(LEFT);
        setBorder(BorderFactory.createLoweredBevelBorder());
        setOpaque(true);
        setActive(true);
    }

    /******************************************************************************************
     * Assigns the active state of the indicator.
     * <p>
     *@param isActive True if the indicator should be active, false if not.
     *****************************************************************************************/
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        if (isActive) {
            setActiveState();
        } else {
            setInactiveState();
        }
    }

    /******************************************************************************************
     * Retrieves whether or not the indicator is currently active.
     *<p>
     *@return True if the indicator is active, false if not.
     *****************************************************************************************/
    public boolean isActive() {
        return isActive;
    }

    /******************************************************************************************
     * Sets the indicator to its active state.
     *****************************************************************************************/
    private void setActiveState() {
        setBackground(activeBackground);
        setForeground(activeForeground);

        if (activeIcon != null) {
            setIcon(activeIcon, getText());
        }
        validateBorder();
    }

    /******************************************************************************************
     * Validates the color of the border around the indicator.
     *****************************************************************************************/
    private void validateBorder() {
        if (getIcon() != null && getText().trim().length() == 0) {
            setBackground(iconBackground);
        }
    }

    /******************************************************************************************
     * Sets the indicator to its inactive state.
     *****************************************************************************************/
    private void setInactiveState() {
        setBackground(inactiveBackground);
        setForeground(inactiveForeground);

        if (inactiveIcon != null) {
            setIcon(inactiveIcon, getText());
        }
        validateBorder();
    }

    /******************************************************************************************
     * Sets the background color of the indicator in its active state.
     *<p>
     *@param color The color to assign.
     *****************************************************************************************/
    public void setActiveBackground(Color color) {
        if (color != null) {
            activeBackground = color;
        }
    }

    /******************************************************************************************
     * Sets the foreground color of the indicator in its active state.
     *<p>
     *@param color The color to assign.
     *****************************************************************************************/
    public void setActiveForeground(Color color) {
        if (color != null) {
            activeForeground = color;
        }
    }

    /******************************************************************************************
     * Sets the background color of the indicator in its inactive state.
     *<p>
     *@param color The color to assign.
     *****************************************************************************************/
    public void setInactiveBackground(Color color) {
        if (color != null) {
            inactiveBackground = color;
        }
    }

    /******************************************************************************************
     * Sets the foreground color of the indicator in its inactive state.
     *<p>
     *@param color The color to assign.
     *****************************************************************************************/
    public void setInactiveForeground(Color color) {
        if (color != null) {
            inactiveForeground = color;
        }
    }

    /******************************************************************************************
     * Sets the icon of the indicator in its active state.
     *<p>
     *@param icon The icon to assign.
     *****************************************************************************************/
    public void setActiveIcon(Icon icon) {
        if (icon != null) {
            activeIcon = icon;
        }
    }

    /******************************************************************************************
     * Sets the icon of the indicator in its inactive state.
     *<p>
     *@param icon The icon to assign.
     *****************************************************************************************/
    public void setInactiveIcon(Icon icon) {
        if (icon != null) {
            inactiveIcon = icon;
        }
    }

    /******************************************************************************************
     * Assigns the icon background color for the case where there is no text.
     *<p>
     *@param color The color to assign.
     *****************************************************************************************/
    public void setIconBackground(Color color) {
        if (color != null) {
            iconBackground = color;
        }
    }
}
