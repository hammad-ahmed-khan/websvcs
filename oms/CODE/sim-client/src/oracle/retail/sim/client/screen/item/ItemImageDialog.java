package oracle.retail.sim.client.screen.item;

import java.util.List;
import javax.swing.ImageIcon;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Item Image Dialog
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemImageDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -3043036892384336904L;

    private ItemImagePanel imagePanel = new ItemImagePanel();
    private RButton closeButton = new RButton(SimNavigation.DIALOG_CLOSE);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ItemImageDialog() {
        super(Application.getFrame(), false);
        setStatusBarVisible(false);
    }

    /****************************************************************************************************
     * Assigns Images To Window
     ***************************************************************************************************/
    public void setImages(List<ImageIcon> images) {
        imagePanel.setImages(images);
        closeButton.registerAction(this, SimNavigation.DIALOG_CLOSE);
        addButton(closeButton);
        setContentPane(imagePanel);
        calculateSize(images);
        centerWindow();
    }

    /****************************************************************************************************
     * Calculate Size Of Window
     ***************************************************************************************************/
    private void calculateSize(List<ImageIcon> images) {
        if (images.isEmpty()) {
            pack();
            return;
        }
        int width = 0;
        int height = 0;
        for (ImageIcon icon : images) {
            if (icon.getIconWidth() > width) {
                width = icon.getIconWidth();
            }
            if (icon.getIconHeight() > height) {
                height = icon.getIconHeight();
            }
        }
        setSize(width, height + 125);
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_CLOSE)) {
                closeWindow();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }
}
