package oracle.retail.sim.client.swing.panel;

import java.awt.Component;
import javax.swing.JSplitPane;

/********************************************************************************************
 * Override the JSplitPane to provide custom framework functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************/

public class RSplitPane extends JSplitPane {
    private static final long serialVersionUID = -2951563982544813771L;

    private static final int BASE_DIVIDER_SIZE = 6;

    /********************************************************************************************
     * Creates a new JSplitPane configured with the specified orientation and no continuous layout.
     * <p>
     * @param orientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * <p>
     * @throws IllegalArgumentException If orientation is not one of HORIZONTAL_SPLIT or
     * VERTICAL_SPLIT.
     *****************************************************************************************/
    public RSplitPane(int orientation) {
        super(orientation);
        setOneTouchExpandable(true);
        setDividerSize(BASE_DIVIDER_SIZE);
    }

    /********************************************************************************************
     * Creates a new JSplitPane configured with the specified orientation and redrawing style.
     * <p>
     * @param orientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @param continuousLayout True for the components to redraw continuously as the divider
     * changes position, false to wait until the divider position stops changing to redraw.
     * <p>
     * @throws IllegalArgumentException If orientation is not one of HORIZONTAL_SPLIT or
     * VERTICAL_SPLIT.
     *****************************************************************************************/
    public RSplitPane(int orientation, boolean continuousLayout) {
        super(orientation, continuousLayout);
        setOneTouchExpandable(true);
        setDividerSize(BASE_DIVIDER_SIZE);
    }

    /********************************************************************************************
     * Creates a new JSplitPane with the specified orientation and with the specified components
     * that do not do continuous redrawing.
     * <p>
     * @param orientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @param leftComponent The Component that will appear on the left of a horizontally-split
     * pane, or at the top of a vertically-split pane.
     * @param rightComponent The Component that will appear on the right of a horizontally-split
     * pane, or at the bottom of a vertically-split pane.
     * <p>
     * @throws IllegalArgumentException If orientation is not one of HORIZONTAL_SPLIT or
     * VERTICAL_SPLIT.
     *****************************************************************************************/
    public RSplitPane(int orientation, Component leftComponent, Component rightComponent) {
        super(orientation, leftComponent, rightComponent);
        setOneTouchExpandable(true);
        setDividerSize(BASE_DIVIDER_SIZE);
    }

    /********************************************************************************************
     * Creates a new JSplitPane with the specified orientation and with the specified components
     * that do not do continuous redrawing.
     * <p>
     * @param orientation JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT.
     * @param continuousLayout True for the components to redraw continuously as the divider
     * changes position, false to wait until the divider position stops changing to redraw.
     * @param leftComponent The Component that will appear on the left of a horizontally-split
     * pane, or at the top of a vertically-split pane.
     * @param rightComponent The Component that will appear on the right of a horizontally-split
     * pane, or at the bottom of a vertically-split pane.
     * <p>
     * @throws IllegalArgumentException If orientation is not one of HORIZONTAL_SPLIT or
     * VERTICAL_SPLIT.
     *****************************************************************************************/
    public RSplitPane(int orientation, boolean continuousLayout, Component leftComponent, Component rightComponent) {
        super(orientation, continuousLayout, leftComponent, rightComponent);
        setOneTouchExpandable(true);
        setDividerSize(BASE_DIVIDER_SIZE);
    }
}
