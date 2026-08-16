package oracle.retail.sim.client.swing.plaf.custom;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

/******************************************************************************************
 * This class subclasses the basic split pane UI to supply the Oracle Retail look and feel for
 * a split pane.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class CustomSplitPaneUI extends BasicSplitPaneUI {

    private CustomSplitPaneDivider chromeDivider;

    /******************************************************************************************
     * Creates a split pane UI object for the given component.
     *****************************************************************************************/
    public static ComponentUI createUI(JComponent component) {
        return new CustomSplitPaneUI();
    }

    /******************************************************************************************
     * Override install defaults to handle customer functionality.
     *****************************************************************************************/
    protected void installDefaults() {
        super.installDefaults();
        divider.setBorder(null);
    }

    /******************************************************************************************
     * Overrides the creation of the divider to supply the chrome divider.
     *****************************************************************************************/
    public BasicSplitPaneDivider createDefaultDivider() {
        if (chromeDivider == null) {
            chromeDivider = new CustomSplitPaneDivider(this);
        }
        return chromeDivider;
    }
}
