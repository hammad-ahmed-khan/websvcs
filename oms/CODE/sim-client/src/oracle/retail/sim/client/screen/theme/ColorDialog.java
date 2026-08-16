package oracle.retail.sim.client.screen.theme;

import java.awt.BorderLayout;
import java.util.List;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.plaf.ColorUIResource;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Color Selection Dialog allows the user to preview and select a color.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ColorDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -9087704682035834403L;

    private JColorChooser colorChooser = new JColorChooser();

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private List<ColorWrapper> colorWrappers;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public ColorDialog(JFrame frame) {
        super(frame);
        setStatusBarVisible(false);
        setTitle("Customize Color");
        setSize(400, 400);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(cancelButton);

        RPanel mainPanel = new RPanel(new BorderLayout());
        mainPanel.add(colorChooser);

        setContentPane(mainPanel);
    }

    public void setColors(List<ColorWrapper> wrappers) {
        colorWrappers = wrappers;

        ColorWrapper wrapper = wrappers.get(0);
        colorChooser.setColor(wrapper.getColor());
        colorChooser.setLocale(LocaleManager.getLanguageLocale());
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doSaveColor();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancelWindow();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSaveColor() {
        for (ColorWrapper wrapper : colorWrappers) {
            wrapper.setColor(new ColorUIResource(colorChooser.getColor()));
            wrapper.setCustom(true);
        }
        closeWindow();
    }

    private void doCancelWindow() {
        closeWindow();
    }
}
