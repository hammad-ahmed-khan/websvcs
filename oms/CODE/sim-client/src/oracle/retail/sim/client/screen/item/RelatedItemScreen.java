package oracle.retail.sim.client.screen.item;

import oracle.retail.sim.client.core.SimScreen;
import oracle.retail.sim.client.swing.frame.ScreenPanel;

/********************************************************************************************************
 * Related Item Screen
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RelatedItemScreen extends SimScreen {
    private static final long serialVersionUID = 4727526236454053597L;

    private RelatedItemPanel panel = new RelatedItemPanel();

    public RelatedItemScreen() {
        add(panel);
    }

    public String getScreenName() {
        return "Related Item List";
    }

    public ScreenPanel getScreenPanel() {
        return panel;
    }

    public void start() {
        showMenu();
        panel.start();
    }
}
