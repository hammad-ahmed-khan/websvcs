package oracle.retail.sim.client.swing.frame;

import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/*******************************************************************************************
 * This class is the progress or "busy" indicator on the status bar.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************/

public class RProgressIndicator extends JLabel {
    private static final long serialVersionUID = 1642380395907636321L;

    /*******************************************************************************************
     * Returns new RProgressIndicator object.
     *******************************************************************************************/
    public RProgressIndicator() {
        initialize();
    }

    /*******************************************************************************************
     * Initializes the progress indicator.
     *******************************************************************************************/
    private void initialize() {
        Icon disabledIcon = UIManager.getIcon(UIThemeName.STATUSBAR_PROGRESS_DISABLED_ICON);
        Icon enabledIcon = UIManager.getIcon(UIThemeName.STATUSBAR_PROGRESS_ENABLED_ICON);

        if (disabledIcon == null || enabledIcon == null) {
            return;
        }

        setDisabledIcon(disabledIcon);
        setIcon(enabledIcon);

        Dimension iconDimension = new Dimension(disabledIcon.getIconWidth(), disabledIcon.getIconHeight());

        setMinimumSize(iconDimension);
        setPreferredSize(iconDimension);
        setEnabled(false);
    }

    /*************************************************************************************************
     * Activates the progress indicator.
     *************************************************************************************************/
    public void activate() {
        setEnabled(true);
    }

    /*************************************************************************************************
     * Deactivates the progress indicator.
     *************************************************************************************************/
    public void deactivate() {
        setEnabled(false);
    }
}
