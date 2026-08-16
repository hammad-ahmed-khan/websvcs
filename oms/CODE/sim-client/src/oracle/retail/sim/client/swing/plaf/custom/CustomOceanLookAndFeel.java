package oracle.retail.sim.client.swing.plaf.custom;

import javax.swing.plaf.metal.OceanTheme;

/********************************************************************************************************
 * This class subclasses the metal look and feel and then adds the ocean theme to the metal look and
 * feel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomOceanLookAndFeel extends CustomMetalLookAndFeel {
    private static final long serialVersionUID = 9060945115934835857L;

    public CustomOceanLookAndFeel() {
        setCurrentTheme(new OceanTheme());
    }

    /****************************************************************************************************
     * Return a string that identifies this look and feel. This string will be used by
     * applications/services that want to recognize well known look and feel implementations. Presently
     * the well known names are "Motif", "Windows", "Mac", "Metal". Note that a LookAndFeel derived from
     * a well known superclass that doesn't make any fundamental changes to the look or feel shouldn't
     * override this method.
     ***************************************************************************************************/
    public String getID() {
        return "Ocean";
    }

    /****************************************************************************************************
     * Return a short string that identifies this look and feel, e.g. "CDE/Motif". This string should be
     * appropriate for a menu item. Distinct look and feels should have different names, e.g. a subclass
     * of MotifLookAndFeel that changes the way a few components are rendered should be called "CDE/Motif
     * My Way"; something that would be useful to a user trying to select a L&F from a list of names.
     ***************************************************************************************************/
    public String getName() {
        return "Ocean";
    }

    /****************************************************************************************************
     * Return a one line description of this look and feel implementation, e.g. "The CDE/Motif Look and
     * Feel". This string is intended for the user, e.g. in the title of a window or in a ToolTip
     * message.
     ***************************************************************************************************/
    public String getDescription() {
        return "Ocean Look and Feel";
    }

    /****************************************************************************************************
     * Equals and HashCode
     ***************************************************************************************************/

    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (object == null || object.getClass() != getClass()) {
            return false;
        }
        CustomOceanLookAndFeel that = (CustomOceanLookAndFeel) object;
        return getID().equals(that.getID());
    }

    public int hashCode() {
        return getID().hashCode();
    }
}
