package oracle.retail.sim.client.swing.widget;

import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class sub-classes JMenuItem in order to supply additional functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RMenuItem extends JMenuItem {
    private static final long serialVersionUID = 5302221658499746780L;

    /******************************************************************************************
     * Constructs a new RMenuItem.
     *****************************************************************************************/
    public RMenuItem() {
    }

    /******************************************************************************************
     * Constructs a new RMenuItem with the given title.
     * <p>
     * @param title The title to display in the menu item.
     *****************************************************************************************/
    public RMenuItem(String title) {
        setText(title);
    }

    /******************************************************************************************
     * Assigns the display text in the menu item.
     * <p>
     * @param text The text to display in the menu item.
     * @param suffix The suffix to display in the menu item.
     *****************************************************************************************/
    public void setText(String text) {
        super.setText(Translator.getText(text));
    }

    /******************************************************************************************
     * Assigns the display text in the menu item with a suffix. The regular text is translated
     * but the suffix is not.
     * <p>
     * @param text The text to display in the menu item.
     * @param suffix The suffix to display in the menu item.
     ******************************************************************************************/
    public void setText(String text, String suffix) {
        super.setText(Translator.getText(text) + StringConstants.SPACE + suffix);
    }

    /******************************************************************************************
     * Registers an action with the menu item.
     * <p>
     * @param listener The action listener.
     * @param command The action command.
     *****************************************************************************************/
    public void registerAction(ActionListener listener, String command) {
        removeActionListener(listener);
        addActionListener(listener);
        setActionCommand(command);
    }
}
