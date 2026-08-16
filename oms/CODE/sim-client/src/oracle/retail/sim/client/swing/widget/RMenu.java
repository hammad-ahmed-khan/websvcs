package oracle.retail.sim.client.swing.widget;

import javax.swing.JMenu;
import oracle.retail.sim.client.locale.Translator;

/******************************************************************************************
 * This class sub-classes JMenu in order to supply additional functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RMenu extends JMenu {
    private static final long serialVersionUID = -9049650393868567814L;

    /******************************************************************************************
     * Constructs a new RMenu.
     *****************************************************************************************/
    public RMenu() {
    }

    /******************************************************************************************
     * Constructs a new RMenu with the given title.
     * <p>
     * @param title The title to display in the menu.
     *****************************************************************************************/
    public RMenu(String title) {
        setText(title);
    }

    /******************************************************************************************
     * Assigns the display text in the menu.
     * <p>
     * @param text The text to display in the menu.
     *****************************************************************************************/
    public void setText(String text) {
        super.setText(Translator.getText(text));
    }
}
