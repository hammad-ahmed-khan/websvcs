package oracle.retail.sim.client.swing.frame;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displaytable.RDisplayTable;
import oracle.retail.sim.client.swing.displaytable.RDisplayTablePane;
import oracle.retail.sim.client.swing.panel.RButtonPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.UIProblem;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RDisplayTextArea;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.common.core.locale.StringConstants;

/**********************************************************************************************
 * Creates a new RStatusWindow.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 **********************************************************************************************/

public class RStatusWindow extends JDialog implements ActionListener {
    private static final long serialVersionUID = -7509073815468862810L;

    private static final int SOURCE_PAD = 10;
    private static final String SOURCE = "Source";
    private static final String REASON = "Reason";
    private static final String[] HEADERS = { SOURCE, REASON };

    private RButtonPanel buttonPanel = new RButtonPanel();
    private RButton okayButton = new RButton("OK");

    private RLabel iconLabel = new RLabel();
    private RDisplayTextArea primaryArea = new RDisplayTextArea();

    private RDisplayTable messageTable = new RDisplayTable("Status Window Table");
    private RDisplayTablePane messageTablePane = new RDisplayTablePane(messageTable);

    private RDisplayTextArea messageArea = new RDisplayTextArea();
    private RScrollPane messageAreaPane = new RScrollPane(messageArea);

    private RLabel fillerLabel = new RLabel();

    //    private String problemTitle;

    /*****************************************************************************************
     * Creates a new RStatusWindow.
     *****************************************************************************************/
    public RStatusWindow() {
        initialize();
        layoutWindow();
    }

    /*****************************************************************************************
     * Creates a new RStatusWindow.
     * <p>
     * @param frame The parent frame to associate this dialog window to.
     *****************************************************************************************/
    public RStatusWindow(Frame frame) {
        super(frame, true);
        initialize();
        layoutWindow();
    }

    /*****************************************************************************************
     * Creates a new RStatusWindow.
     * <p>
     * @param dialog The parent dialog to associate this dialog window to. If the parent
     * dialog is modal, then this window will be modal as well (otherwise not).
     *****************************************************************************************/
    public RStatusWindow(Dialog dialog) {
        super(dialog, dialog.isModal());
        initialize();
        layoutWindow();
    }

    /*****************************************************************************************
     * Builds the default values of the window.
     *****************************************************************************************/
    private void initialize() {
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setBackground(UIManager.getColor(UIThemeName.STATUSWINDOW_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.STATUSWINDOW_FOREGROUND));
        setTitle(Translator.getText("Exception Details"));
        setSize(550, 300);

        messageAreaPane.setLoweredBorder();

        messageTable.setColumnHeaders(HEADERS);
        messageTable.buildColumn(SOURCE, DataTypeConstants.TEXT, 0, 0, 0);
        messageTable.buildColumn(REASON, DataTypeConstants.TEXT_FULL, -1, -1, -1);

        buttonPanel.setCenterLayout();

        okayButton.addActionListener(this);
    }

    /*****************************************************************************************
     * Lays out the widgets within the window.
     *****************************************************************************************/
    private void layoutWindow() {
        getRootPane().setDefaultButton(okayButton);

        buttonPanel.add(okayButton);

        Container container = getContentPane();
        container.setLayout(new GridBagLayout());
        container.add(iconLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
        container.add(primaryArea, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
        container.add(buttonPanel, GridTool.constraints(0, 2, 2, 1, 0, 0, 0, 1, 0, 0, 0, 0));
    }

    /*****************************************************************************************
     * Centers the window.
     *****************************************************************************************/
    public void centerWindow() {
        WindowPlacer.centerWindow(this);
    }

    /*****************************************************************************************
     * Displays the formatted messages for the exception.
     * <p>
     * @param exception The UIException to display the error messages from.
     *****************************************************************************************/
    public void displayException(UIException exception) {
        if (exception == null) {
            dispose();
            return;
        }
        //        if (problemTitle == null) {
        //            problemTitle = Translator.getText("Problem #");
        //        }

        if (exception.isError()) {
            iconLabel.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_ERROR_ICON));
        } else if (exception.isWarning()) {
            iconLabel.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_WARNING_ICON));
        } else {
            iconLabel.setIcon(null);
        }

        // Primary Area
        if (exception == null || exception.getPrimaryMessageText() == null) {
            exception = new UIException(UIMessageText.DEFAULT_EXCEPTION_MESSAGE);
        }
        if (exception.getPrimaryMessageValues() != null) {
            primaryArea.setText(Translator.getMessage(exception.getPrimaryMessageText().getText(), exception.getPrimaryMessageValues()));
        } else {
            primaryArea.setText(Translator.getMessage(exception.getPrimaryMessageText().getText()));
        }

        // Remaining Area
        Throwable cause = exception.getCause();
        if (cause != null) {
            displayMessageArea(cause);
        } else {
            displayMessageTable(exception);
        }
    }

    /*****************************************************************************************
     * Displays the stack trace of an inner exception within a text area and places the text
     * area in the status window.
     * <p>
     * @param innerException The Exception to display the stack trace from.
     *****************************************************************************************/
    private void displayMessageArea(Throwable cause) {
        if (cause != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            PrintWriter writer = new PrintWriter(stream);
            cause.printStackTrace(writer);
            writer.flush();
            messageArea.setText(stream.toString());
            writer.close();
        }
        if (!messageArea.isEmpty()) {
            displayMessageComponent(messageAreaPane);
        } else {
            displayMessageComponent(fillerLabel);
        }
    }

    /*****************************************************************************************
     * Displays a table full of problems containing the source identity and the reason for the failure.
     * <p>
     * @param exception The UIException to display within the problem table.
     *****************************************************************************************/
    private void displayMessageTable(UIException exception) {
        messageTable.clearTable();

        FontMetrics metrics = messageTable.getFontMetrics(messageTable.getFont());
        int maxSourceLength = 0;
        int sourceLength = 0;

        List<UIProblem> problems = exception.getProblems();
        problems.remove(0);

        for (UIProblem problem : problems) {
            String[] row = new String[2];

            if (problem.getIdentifier() != null) {
                row[0] = StringUtility.getRemainingText(problem.getIdentifier(), StringConstants.DOT);
            } else {
                row[0] = StringConstants.EMPTY;
            }

            if (problem.getMessageValues() != null) {
                row[1] = Translator.getMessage(problem.getMessageText().getText(), problem.getMessageValues());
            } else {
                row[1] = Translator.getMessage(problem.getMessageText().getText());
            }

            sourceLength = metrics.stringWidth(row[0]);

            if (sourceLength > maxSourceLength) {
                maxSourceLength = sourceLength;
            }
            messageTable.addRow(row);
        }

        if (!messageTable.isEmpty()) {
            maxSourceLength = maxSourceLength + SOURCE_PAD;
            messageTable.buildColumn(SOURCE, DataTypeConstants.TEXT, 0, maxSourceLength, maxSourceLength);
            displayMessageComponent(messageTablePane);
        } else {
            displayMessageComponent(fillerLabel);
        }
    }

    /*****************************************************************************************
    * Displays the message component on the content pane. If it is a filler label only, then
    * the size of the window is shrunk.
    *****************************************************************************************/
    private void displayMessageComponent(JComponent component) {
        getContentPane().add(component, GridTool.constraints(1, 1, 1, 1, 1, 1, 0, 3, 10, 0, 10, 10));
        if (component == fillerLabel) {
            setSize(550, 175);
        }
    }

    /*****************************************************************************************
     * Implements the ActionListener required method and closes the window.
     *****************************************************************************************/
    public void actionPerformed(ActionEvent event) {
        dispose();
    }
}
