package oracle.retail.sim.client.swing.tableconfig;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import oracle.retail.sim.client.swing.widget.RMenu;
import oracle.retail.sim.client.swing.widget.RMenuItem;
import oracle.retail.sim.client.swing.widget.RPopupMenu;

/********************************************************************************************************
 * Popup menu that handles table configuration. This is triggered by right-clicking on the data within a
 * table or on the header.
 * <p>
 * Oracle Inc. Copyright (c) 1999-2005
 *******************************************************************************************************/

public class RTableConfigPopupMenu extends RPopupMenu implements ActionListener {
    private static final long serialVersionUID = -3834630373906594045L;

    // Sub-Menu Items
    private RMenuItem smallestSizeItem = new RMenuItem(RTableConfigConstants.SMALLEST_LABEL);
    private RMenuItem smallerSizeItem = new RMenuItem(RTableConfigConstants.SMALLER_LABEL);
    private RMenuItem standardSizeItem = new RMenuItem(RTableConfigConstants.STANDARD_LABEL);
    private RMenuItem largeSizeItem = new RMenuItem(RTableConfigConstants.LARGE_LABEL);
    private RMenuItem largerSizeItem = new RMenuItem(RTableConfigConstants.LARGER_LABEL);
    private RMenuItem largestSizeItem = new RMenuItem(RTableConfigConstants.LARGEST_LABEL);

    private RMenuItem allLinesItem = new RMenuItem(RTableConfigConstants.ALL_LINES_LABEL);
    private RMenuItem noLinesItem = new RMenuItem(RTableConfigConstants.NONE_LINES_LABEL);
    private RMenuItem columnLinesItem = new RMenuItem(RTableConfigConstants.COLUMN_LINES_LABEL);
    private RMenuItem rowLinesItem = new RMenuItem(RTableConfigConstants.ROW_LINES_LABEL);

    // Menus
    private RMenu sizeContentMenu = new RMenu(RTableConfigConstants.SIZE_CONTENT_LABEL);
    private RMenu gridlinesMenu = new RMenu(RTableConfigConstants.SHOW_GRIDLINES_LABEL);
    private RMenuItem configurationItem = new RMenuItem(RTableConfigConstants.TABLE_CONFIG_LABEL);

    private RTableConfigListener listener;

    /****************************************************************************************************
     * Create the popup menu.
     * @param listener The listener to receive the popup menu selection actions.
     ***************************************************************************************************/
    public RTableConfigPopupMenu(RTableConfigListener listener) {
        this.listener = listener;
        initializeComponents();
        layoutComponents();
    }

    /****************************************************************************************************
     * Initializes the components settings.
     ***************************************************************************************************/
    private void initializeComponents() {
        configurationItem.registerAction(this, RTableConfigConstants.TABLE_CONFIG_LABEL);
        smallestSizeItem.registerAction(this, RTableConfigConstants.SMALLEST_LABEL);
        smallerSizeItem.registerAction(this, RTableConfigConstants.SMALLER_LABEL);
        standardSizeItem.registerAction(this, RTableConfigConstants.STANDARD_LABEL);
        largeSizeItem.registerAction(this, RTableConfigConstants.LARGE_LABEL);
        largerSizeItem.registerAction(this, RTableConfigConstants.LARGER_LABEL);
        largestSizeItem.registerAction(this, RTableConfigConstants.LARGEST_LABEL);

        allLinesItem.registerAction(this, RTableConfigConstants.ALL_LINES_LABEL);
        noLinesItem.registerAction(this, RTableConfigConstants.NONE_LINES_LABEL);
        columnLinesItem.registerAction(this, RTableConfigConstants.COLUMN_LINES_LABEL);
        rowLinesItem.registerAction(this, RTableConfigConstants.ROW_LINES_LABEL);
    }

    /****************************************************************************************************
     * Lays out the components.
     ***************************************************************************************************/
    private void layoutComponents() {
        sizeContentMenu.add(smallestSizeItem);
        sizeContentMenu.add(smallerSizeItem);
        sizeContentMenu.add(standardSizeItem);
        sizeContentMenu.add(largeSizeItem);
        sizeContentMenu.add(largerSizeItem);
        sizeContentMenu.add(largestSizeItem);

        gridlinesMenu.add(allLinesItem);
        gridlinesMenu.add(noLinesItem);
        gridlinesMenu.add(columnLinesItem);
        gridlinesMenu.add(rowLinesItem);

        add(sizeContentMenu);
        add(gridlinesMenu);
        addSeparator();
        add(configurationItem);
    }

    /****************************************************************************************************
     * Implements the action listener method to perform the correct action.
     ***************************************************************************************************/
    public void actionPerformed(final ActionEvent event) {
        if (listener != null) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    listener.configurationPerformed(event.getActionCommand());
                }
            });
        }
    }
}
