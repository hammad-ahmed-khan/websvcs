package oracle.retail.sim.client.core;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.panel.RBackgroundPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;

/********************************************************************************************************
 * SimBackgroundPanel
 * <p>
 * Background panel for empty screens.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimBackgroundPanel extends RBackgroundPanel {
    private static final long serialVersionUID = 3568701702120265245L;

    public SimBackgroundPanel() {
        Icon icon = UIManager.getIcon(UIThemeName.LARGE_ORACLE_ICON);
        if (icon instanceof ImageIcon) {
            setBackgroundImageIcon((ImageIcon) icon);
        }
    }
}
