package oracle.retail.sim.client.swing.widget;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class subclasses RPanel to provide custom functionality for tabs.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RTab extends RPanel {
    private static final long serialVersionUID = 4964779063255279332L;

    private String title = StringConstants.EMPTY;

    /******************************************************************************************
     * Returns new RTab object.
     *****************************************************************************************/
    public RTab() {
        setEmptyBorder(3);
    }

    /******************************************************************************************
     * Returns new RTab object with title assigned. RTab will attempt to access this when an
     * RTab is added to the pane.
     * <p>
     * @param title The title to assign to the RTab.
     *****************************************************************************************/
    public RTab(String title) {
        setEmptyBorder(3);
        setTitle(title);
    }

    /******************************************************************************************
     * Assigns a title to the RTab.
     * <p>
     * @param title The title to assign to the RTab.
     *****************************************************************************************/
    public void setTitle(String title) {
        if (StringUtility.isNullOrEmpty(title)) {
            throw new IllegalArgumentException("Cannot assign an empty or null tab title!");
        }
        this.title = title;
    }

    /******************************************************************************************
     * Retrieves the title of the RTab.
     * <p>
     * @return The title.
     *****************************************************************************************/
    public String getTitle() {
        return title;
    }
}
