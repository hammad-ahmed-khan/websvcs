package oracle.retail.sim.client.swing.plaf.custom;

import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalCheckBoxUI;

/******************************************************************************************
 * This class subclasses the MetalComboBoxUI and supplies the check box look and feel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class CustomCheckBoxUI extends MetalCheckBoxUI {

    private boolean wasOpaque = true;

    /******************************************************************************************
     * Creates the new ChromeCheckBoxUI for the component.
     *****************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        return new CustomCheckBoxUI();
    }

    /******************************************************************************************
     * Installs the defaults for the AbstractButton. This automatically sets the button
     * opaqueness to false.
     *****************************************************************************************/
    public void installDefaults(AbstractButton button) {
        super.installDefaults(button);
        wasOpaque = button.isOpaque();
        button.setOpaque(false);
    }

    /******************************************************************************************
     * Uninstalls the defaults for the AbstractButton. This resets the button opaqueness to the
     * value it had prior to install defaults.
     *****************************************************************************************/
    protected void uninstallDefaults(AbstractButton button) {
        button.setOpaque(wasOpaque);
    }
}
