package oracle.retail.sim.client.swing.util;

import java.awt.Font;

/******************************************************************************************
 * The static font utility class provides a set of convenience methods for working with
 * fonts.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class FontUtility {

    /*****************************************************************************************
     * Private constructor forces a static class.
     *****************************************************************************************/
    private FontUtility() {
    }

    /*****************************************************************************************
     * Converts a font to its bold value if it is not already bold.
     * <p>
     * @param font The original font.
     * @param font The bold version of the original font.
     *****************************************************************************************/
    public static Font getBoldFont(Font font) {
        return font.deriveFont(Font.BOLD);
    }
}
