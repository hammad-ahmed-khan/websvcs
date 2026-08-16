package oracle.retail.sim.client.swing.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import oracle.retail.sim.client.swing.editor.RPasswordFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RImagePanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;

/********************************************************************************************************
 * This class represents the basics of a login dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class RLoginDialog extends RDialog implements KeyListener, REventListener {
    private ImageIcon backgroundIcon;

    private RLabel welcomeLabel = new RLabel(UIMessageText.MESSAGE_WELCOME.getText());
    private RLabel loginLabel = new RLabel(UIMessageText.MESSAGE_LOGIN.getText());
    private RLabel copyrightLabel = new RLabel(UIMessageText.MESSAGE_ABOUT_COPYRIGHT.getText());

    protected RTextFieldEditor userNameEditor = new RTextFieldEditor("Username", true);
    protected RPasswordFieldEditor passwordEditor = new RPasswordFieldEditor("Password", true);

    protected static final String LOGIN = "Login";
    protected static final String CANCEL = "Cancel";

    protected RButton loginButton = new RButton(LOGIN);
    protected RButton cancelButton = new RButton(CANCEL);

    /****************************************************************************************************
     * Creates a new login dialog around a JFrame.
     ***************************************************************************************************/
    protected RLoginDialog(JFrame frame) {
        super(frame, true);
        buildDialog();
        layoutDialog();
        centerWindow();
    }

    /****************************************************************************************************
     * Initializes all the widgets in the window.
     ***************************************************************************************************/
    protected void buildDialog() {
        welcomeLabel.setFontStyle(Font.BOLD, 15);

        userNameEditor.setIdentifier(UIThemeName.LOGIN_USER_ID);
        passwordEditor.setIdentifier(UIThemeName.LOGIN_PASSWORD);

        userNameEditor.getTextField().addKeyListener(this);
        passwordEditor.getPasswordField().addKeyListener(this);

        loginButton.setBackground(UIManager.getColor(UIThemeName.TASKPANEL_BORDER_BACKGROUND));
        loginButton.setForeground(UIManager.getColor(UIThemeName.TASKPANEL_FOREGROUND));
        loginButton.registerAction(this, LOGIN);
        loginButton.setMinimumWidth(110);

        cancelButton.setBackground(UIManager.getColor(UIThemeName.TASKPANEL_BORDER_BACKGROUND));
        cancelButton.setForeground(UIManager.getColor(UIThemeName.TASKPANEL_FOREGROUND));
        cancelButton.registerAction(this, CANCEL);
        cancelButton.setMinimumWidth(110);

        backgroundIcon = (ImageIcon) UIManager.getIcon(UIThemeName.LOGIN_BACKGROUND);

        setButtonPanelVisible(false);
        setSize(600, backgroundIcon.getIconHeight() + 110);
        setResizable(false);
    }

    /****************************************************************************************************
     * Layout the panels and widgets within the dialog.
     ***************************************************************************************************/
    private void layoutDialog() {
        JPanel northPanel = new JPanel(new FlowLayout());
        northPanel.setOpaque(false);
        northPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        northPanel.add(new RLabel());
        northPanel.add(welcomeLabel);

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        southPanel.setOpaque(false);
        southPanel.add(loginButton);
        southPanel.add(cancelButton);

        REditorPanel matrixPanel = new REditorPanel(2);
        matrixPanel.setOpaque(false);
        matrixPanel.add(userNameEditor);
        matrixPanel.add(passwordEditor);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(0, 0, 0, 0));
        centerPanel.add(loginLabel, BorderLayout.NORTH);
        centerPanel.add(matrixPanel, BorderLayout.CENTER);

        RPanel eastPanel = new RPanel(new GridBagLayout());
        eastPanel.setLineBorder(5, 5, 5, 5);
        eastPanel.add(northPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 20, 20));
        eastPanel.add(centerPanel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 3, 0, 0, 20, 20));
        eastPanel.add(southPanel, GridTool.constraints(0, 2, 1, 1, 0, 0, 0, 3, 0, 0, 10, 20));

        RImagePanel imagePanel = new RImagePanel();
        imagePanel.setOpaque(false);
        imagePanel.setBackgroundImage(backgroundIcon.getImage());

        RPanel westPanel = new RPanel(new GridBagLayout());
        westPanel.add(imagePanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 3, 0));

        RPanel bottomPanel = new RPanel();
        bottomPanel.add(copyrightLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
        bottomPanel.add(new JLabel(), GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 1, 0, 0, 0, 0));

        RPanel mainPanel = new RPanel(new GridBagLayout());
        mainPanel.add(westPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
        mainPanel.add(eastPanel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 3, 3));
        mainPanel.add(bottomPanel, GridTool.constraints(0, 1, 2, 1, 0, 0, 0, 3, 0, 0, 0, 0));

        setContentPane(mainPanel);
    }

    /****************************************************************************************************
     * Assigns the welcome label.
     ***************************************************************************************************/
    protected void setWelcomeLabel(String text) {
        welcomeLabel.setText(text);
    }

    /****************************************************************************************************
     * Shifts focus to the username field.
     ***************************************************************************************************/
    protected void resetUserNameFocus() {
        userNameEditor.getTextField().requestFocusInWindow();
    }

    /****************************************************************************************************
     * Implement the action listener method to handle the button actions.
     ***************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(LOGIN)) {
            doAttemptLogin(userNameEditor.getText(), passwordEditor.getPassword());
        } else if (command.equals(CANCEL)) {
            doAttemptCancel();
        }
    }

    /****************************************************************************************************
     * Implement the key listener method to attempt login on the press of the 'Enter' key.
     ***************************************************************************************************/
    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            doAttemptLogin(userNameEditor.getText(), passwordEditor.getPassword());
        }
    }

    public void keyReleased(KeyEvent event) {
    }

    public void keyTyped(KeyEvent event) {
    }

    /****************************************************************************************************
     * Abstract method is implemented by subclasses to handle the actual process of login in.
     ***************************************************************************************************/
    protected abstract void doAttemptLogin(String userName, char[] password);

    protected abstract void doAttemptCancel();
}
