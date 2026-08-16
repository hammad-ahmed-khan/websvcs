package oracle.retail.sim.client.screen.mps;

import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.mps.MpsStagedMessage;
import org.xml.sax.SAXParseException;

/********************************************************************************************************
 * This dialog handles modifying of staged messages.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MpsStagedMessageDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 6176954058168810466L;

    private MpsStagedMessageDialogModel model = new MpsStagedMessageDialogModel();

    private RLabel messageLabel = new RLabel("Message Contents");
    private JTextArea messageEditor = new JTextArea();
    private RScrollPane messagePane = new RScrollPane(messageEditor);

    private RLabel errorLabel = new RLabel("Error Details");
    private JTextArea errorEditor = new JTextArea();
    private RScrollPane errorPane = new RScrollPane(errorEditor);

    private RButton applyButton = new RButton(SimNavigation.DIALOG_APPLY);
    private RButton cancelButton = new RButton(SimNavigation.DIALOG_CANCEL);

    /****************************************************************************************************
     * Build Dialog
     ***************************************************************************************************/
    public MpsStagedMessageDialog() {
        super(Application.getFrame());
        setStatusBarVisible(false);
        setTitle("MPS Staged Messages");
        setSize(780, 580);
        setResizable(true);
        initContent();
        layoutContent();
        centerOnOwner();
    }

    private void initContent() {
        applyButton.registerAction(this, SimNavigation.DIALOG_APPLY);
        cancelButton.registerAction(this, SimNavigation.DIALOG_CANCEL);

        messageLabel.setFont(UIManager.getFont(UIThemeName.THEME_LARGE_FONT));
        messageLabel.setHorizontalAlignment(RLabel.LEFT);

        messageEditor.setFont(UIManager.getFont(UIThemeName.THEME_LARGE_FONT));
        messageEditor.setEditable(true);

        errorLabel.setFont(UIManager.getFont(UIThemeName.THEME_LARGE_FONT));
        errorLabel.setHorizontalAlignment(RLabel.LEFT);

        errorEditor.setFont(UIManager.getFont(UIThemeName.THEME_LARGE_FONT));
        errorEditor.setEditable(false);

        setDefaultButton(cancelButton);
    }

    private void layoutContent() {
        addButton(applyButton);
        addButton(cancelButton);

        RPanel messagePanel = new RPanel(new GridBagLayout());
        messagePanel.add(messageLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 1, 1, 0, 5, 0, 0));
        messagePanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 1, 0, 1, 1, 0, 5, 0, 0));
        messagePanel.add(messagePane, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 5, 0, 0));
        messagePanel.add(errorPane, GridTool.constraints(1, 1, 1, 1, 1, 1, 0, 3, 0, 5, 0, 0));

        setContentPane(messagePanel);
    }

    public void setStagedMessage(MpsStagedMessage stagedMessage) {
        model.setStagedMessage(stagedMessage);
        messageEditor.setText(model.getMessageData());
        errorEditor.setText(model.getMessageError());
    }

    /****************************************************************************************************
     * Handle Actions
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        try {
            if (command.equals(SimNavigation.DIALOG_APPLY)) {
                doApply();
            } else if (command.equals(SimNavigation.DIALOG_CANCEL)) {
                doCancel();
            }
        } catch (Throwable t) {
            displayException(t);
        }
    }

    /****************************************************************************************************
     * Apply Action
     ***************************************************************************************************/
    private void doApply() {
        try {
            if (model.saveMessageData(messageEditor.getText())) {
                notifyREventListeners(new RActionEvent(this, SimClientStateKey.STAGED_MESSAGE_MODIFIED, null));
            }
            closeWindow();
        } catch (Exception exception) {
            //TODO: implement XML validation that doesn't rely on specific server side exception
            Throwable rootCause = exception.getCause();
            while (rootCause.getCause() != null) {
                rootCause = rootCause.getCause();
            }
            if (rootCause instanceof SAXParseException) {
                displayException(this, new UIException(CommonMessageText.XML_INVALID, rootCause.getMessage()));
            } else {
                displayException(this, CommonMessageText.XML_SAVE_PROBLEM_ENCOUNTERED, rootCause);
            }
        }
    }

    /****************************************************************************************************
     * Cancel Action
     ***************************************************************************************************/
    private void doCancel() {
        closeWindow();
    }
}
