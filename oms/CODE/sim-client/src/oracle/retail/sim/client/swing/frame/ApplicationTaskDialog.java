package oracle.retail.sim.client.swing.frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.border.MatteBorder;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.event.EnterKeyAdapter;
import oracle.retail.sim.client.swing.event.MouseDoubleClickAdapter;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.ColorUtility;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RList;
import oracle.retail.sim.client.swing.widget.RScrollPane;

/******************************************************************************************
 * Popup dialog that displays a list of suspended tasks to be chosen from. The colors and
 * fonts of this window are configured to match the color and font theme of the navigation
 * area.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class ApplicationTaskDialog extends JDialog {
    private static final long serialVersionUID = -984342322787152453L;

    private RPanel titlePanel = new RPanel(new BorderLayout());
    private RPanel tasksPanel = new RPanel(new BorderLayout());
    private RPanel mainPanel = new RPanel(new BorderLayout());

    private RLabel titleLabel = new RLabel();
    private RList taskList = new RList();
    private RScrollPane taskPane = new RScrollPane(taskList);
    private RTitleButton closeButton = new RTitleButton(RTitleButton.CLOSE);

    private REventListener eventListener;
    private String eventCommand;

    /******************************************************************************************
     * Constructs and returns a new ApplicationTaskDialog widget.
     * <p>
     * @param frame The parent frame of the dialog (the application frame).
     * @param suspendedTasks A collection of suspended tasks.
     ******************************************************************************************/
    public ApplicationTaskDialog(JFrame frame, Collection suspendedTasks) {
        super(frame, true);
        initTaskDialog(suspendedTasks);
        layoutTaskDialog();
    }

    /******************************************************************************************
     * Initializes the suspended tasks dialog.
     ******************************************************************************************/
    private void initTaskDialog(Collection suspendedTasks) {
        setUndecorated(true);
        setTitle("Suspended Tasks");
        setSize(250, 200);

        Color background = UIManager.getColor(UIThemeName.NAVIGATION_MENU_BACKGROUND);

        titleLabel.setBackground(ColorUtility.slightlyBrighter(background));
        titleLabel.setForeground(UIManager.getColor(UIThemeName.NAVIGATION_MENU_FOREGROUND));
        titleLabel.setFont(UIManager.getFont(UIThemeName.NAVIGATION_MENU_FONT));
        titleLabel.setOpaque(true);

        tasksPanel.setOpaque(true);

        taskPane.setExtendedBackground(background, false);
        taskPane.setBorder(null);

        taskList.setItems(suspendedTasks);
        taskList.setBackground(UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_BACKGROUND));
        taskList.setForeground(UIManager.getColor(UIThemeName.NAVIGATION_MENU_ITEM_FOREGROUND));
        taskList.addMouseListener(createActionMouseListener());
        taskList.addKeyListener(createActionKeyListener());

        mainPanel.setBorder(new MatteBorder(1, 1, 1, 1, Color.BLACK));
        titlePanel.setBorder(new MatteBorder(0, 0, 1, 0, Color.BLACK));

        closeButton.addActionListener(createCloseAction());
    }

    /******************************************************************************************
     * Lays out the widgets within the Application Task Dialog.
     ******************************************************************************************/
    private void layoutTaskDialog() {
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(closeButton, BorderLayout.EAST);
        tasksPanel.add(taskPane, BorderLayout.CENTER);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(tasksPanel, BorderLayout.CENTER);

        Container container = getContentPane();

        container.setLayout(new BorderLayout());
        container.add(mainPanel, BorderLayout.CENTER);
    }

    /******************************************************************************************
     * Assigns a title to the ApplicationTaskDialog title bar. This title is automatically language
     * translated.
     * <p>
     * @param text The title to assign to the ApplicationTaskDialog title bar.
     ******************************************************************************************/
    public void setTitle(String text) {
        super.setTitle(Translator.getText(text));
        titleLabel.setText(text);
    }

    /******************************************************************************************
     * Registers an event listener and command. When a task is selected, the listener will be
     * sent the command inside an RActionEvent along with the selected task.
     * <p>
     * @param listener The REventListener to receive the action.
     * @param command The command to send.
     ******************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        eventListener = listener;
        eventCommand = command;
    }

    /******************************************************************************************
     * Creates a key listener that listens for the enter key within the list.
     ******************************************************************************************/
    private KeyListener createActionKeyListener() {
        return new EnterKeyAdapter() {
            public void enterKeyPressed(KeyEvent event) {
                doTaskChosen();
            }
        };
    }

    /******************************************************************************************
     * Create a mouse listener that listens for double clicks within the list.
     ******************************************************************************************/
    private MouseListener createActionMouseListener() {
        return new MouseDoubleClickAdapter() {
            public void mouseDoubleClicked(MouseEvent event) {
                doTaskChosen();
            }
        };
    }

    /******************************************************************************************
     * Creates the action that closes the dialog.
     ******************************************************************************************/
    private ActionListener createCloseAction() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        };
    }

    /******************************************************************************************
     * Notifies concerned listeners that a task has been chosen and then closes the dialog.
     ******************************************************************************************/
    private void doTaskChosen() {
        if (eventListener != null && eventCommand != null) {
            eventListener.performActionEvent(new RActionEvent(this, eventCommand, taskList.getSelectedValue()));
        }
        dispose();
    }
}
