package oracle.retail.sim.client.screen.theme;

import javax.swing.JFrame;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.widget.RButton;

/********************************************************************************************************
 * Icon Selection Dialog allows the user to define a font location.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class IconDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 1886301370417804932L;

    private RTextFieldEditor iconNameEditor = new RTextFieldEditor("Icon Key");
    private RTextFieldEditor iconPathEditor = new RTextFieldEditor("Icon Path");

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    private IconWrapper iconWrapper;

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public IconDialog(JFrame frame) {
        super(frame);
        setStatusBarVisible(false);
        setTitle("Customize Icon");
        setSize(600, 120);
        initContent();
        layoutContent();
        centerWindow();
    }

    private void initContent() {
        iconPathEditor.setIdentifier(SimName.THEME_ICON_PATH);
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(cancelButton);

        REditorPanel iconPanel = new REditorPanel(2);
        iconPanel.add(iconNameEditor);
        iconPanel.add(iconPathEditor);

        setContentPane(iconPanel);
    }

    public void setIcon(IconWrapper wrapper) {
        iconWrapper = wrapper;
        ThemeKeyDisplayer displayer = new ThemeKeyDisplayer();
        iconNameEditor.setText(displayer.getDisplayText(wrapper.getKey()));
        iconPathEditor.setText(wrapper.getIconPath());
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doSaveIcon();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancelWindow();
            }
        } catch (Throwable exception) {
            displayException(exception);
        }
    }

    private void doSaveIcon() {
        iconWrapper.setIconPath(iconPathEditor.getText());
        iconWrapper.setCustom(true);
        closeWindow();
    }

    private void doCancelWindow() {
        closeWindow();
    }
}
