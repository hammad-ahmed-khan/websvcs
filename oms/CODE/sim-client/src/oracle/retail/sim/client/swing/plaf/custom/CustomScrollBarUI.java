package oracle.retail.sim.client.swing.plaf.custom;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalScrollBarUI;

/********************************************************************************************************
 * This class subclasses the standard BasicScrollBarUI in order to paint the scrollbar using the chrome
 * look and feel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomScrollBarUI extends MetalScrollBarUI {

    /****************************************************************************************************
     * Creates a new ChromeScrollBarUI for a specific component.
     * <p>
     * @param component The component to create the ChromeButtonUI for.
     * @return The ChromeButtonUI object.
     ***************************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        return new CustomScrollBarUI();
    }

    /****************************************************************************************************
     * Installs a UI for an scrollbar component.
     * <p>
     * @param component A component to install this UI for.
     ***************************************************************************************************/
    public final void installUI(JComponent component) {
        super.installUI(component);
    }

    /****************************************************************************************************
     * Uninstalls the UI for a scrollbar component.
     * <p>
     * @param component A component to uninstall this UI for.
     ***************************************************************************************************/
    public final void uninstallUI(JComponent component) {
        super.uninstallUI(component);
    }
}
