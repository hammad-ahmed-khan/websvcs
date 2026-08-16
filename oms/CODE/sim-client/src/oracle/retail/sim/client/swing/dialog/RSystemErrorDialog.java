package oracle.retail.sim.client.swing.dialog;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.frame.RFrame;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RDisplayTextArea;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.client.swing.widget.RTextArea;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * This class is a popup modal window that displays a system exception. The details button will display
 * the entire system exception chain. Please note that messages are not translated by this dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RSystemErrorDialog extends RDialog {
    private static final long serialVersionUID = -2798535885567399867L;

    protected RPanel primaryPanel = new RPanel();
    protected RLabel iconLabel = new RLabel();
    protected RDisplayTextArea primaryArea = new RDisplayTextArea();

    protected RTextArea detailArea = new RTextArea();
    protected RScrollPane scrollPane = new RScrollPane(detailArea);

    protected static final String OKAY = "OK";
    protected static final String DETAILS = "Details";
    protected static final String COPY_TO_CLIPBOARD = "Copy to Clipboard...";

    protected RButton okayButton = new RButton(OKAY);
    protected RButton detailButton = new RButton(DETAILS);
    protected RButton copyToClipboardButton = new RButton(COPY_TO_CLIPBOARD);

    /****************************************************************************************************
     * Constructs new RErrorDialog around a system exception.
     * <p>
     * @param frame The frame that should own this dialog.
     * @param exception A Throwable object to display.
     ***************************************************************************************************/
    public RSystemErrorDialog(JFrame frame, Throwable exception) {
        this(frame, exception, null);
    }

    /****************************************************************************************************
     * Constructs new RErrorDialog around a system exception.
     * <p>
     * @param frame The frame that should own this dialog.
     * @param exception A Throwable object to display.
     * @param message The primary message to display.
     ***************************************************************************************************/
    public RSystemErrorDialog(JFrame frame, Throwable exception, MessageText message) {
        super(frame);
        buildDialog();
        initialize(exception, message);
    }

    /****************************************************************************************************
     * Constructs new RErrorDialog around a system exception.
     * <p>
     * @param dialog The dialog that should own this dialog.
     * @param exception A Throwable object to display.
     * @param message The primary message to display.
     ***************************************************************************************************/
    public RSystemErrorDialog(RFrame frame, Throwable exception, String message) {
        super(frame);
        buildDialog();
        initialize(exception, message);
    }

    /****************************************************************************************************
     * Constructs new RErrorDialog around a system exception.
     * <p>
     * @param dialog The dialog that should own this dialog.
     * @param exception A Throwable object to display.
     ***************************************************************************************************/
    public RSystemErrorDialog(JDialog dialog, Throwable exception) {
        this(dialog, exception, null);
    }

    /****************************************************************************************************
     * Constructs new RErrorDialog around a system exception.
     * <p>
     * @param dialog The dialog that should own this dialog.
     * @param exception A Throwable object to display.
     * @param message The primary message to display.
     ***************************************************************************************************/
    public RSystemErrorDialog(JDialog dialog, Throwable exception, MessageText message) {
        super(dialog);
        buildDialog();
        initialize(exception, message);
    }

    /****************************************************************************************************
     * Builds the default dialog settings.
     ***************************************************************************************************/
    private void buildDialog() {
        setTitle("Severe Error");
        setResizable(false);
        setStatusBarVisible(false);
        setSize(500, 225);

        iconLabel.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_ERROR_ICON));

        detailArea.setLength(Integer.MAX_VALUE);
        detailArea.setInactiveForeground(UIManager.getColor(UIThemeName.DISPLAY_TEXTAREA_FOREGROUND));
        detailArea.setFocusable(false);

        scrollPane.setLoweredBorder();
        scrollPane.setVisible(false);

        okayButton.addActionListener(createCloseListener());
        detailButton.addActionListener(createDetailsListener());
        copyToClipboardButton.addActionListener(createCopyToClipboardListener());
        copyToClipboardButton.setEnabled(false);
    }

    /****************************************************************************************************
     * Initializes the RErrorDialog and prepares it to be shown. It handles splitting it up into lines
     * for multi-line display.
     ***************************************************************************************************/
    private void initialize(Throwable exception, MessageText message) {
        String displayMessage = null;
        if (message != null) {
            displayMessage = Translator.getMessage(message.getText());
        } else if (exception instanceof UIException) {
            UIException uie = (UIException) exception;
            displayMessage = Translator.getMessage(uie.getPrimaryMessageText().getText(), uie.getPrimaryMessageValues());
        } else if (exception instanceof BusinessException) {
            BusinessException be = (BusinessException) exception;
            displayMessage = Translator.getMessage(be.getPrimaryMessageText().getText(), be.getPrimaryMessageValues());
        } else {
            displayMessage = Translator.getMessage(UIMessageText.DEFAULT_FATAL_MESSAGE.getText());
        }
        initialize(exception, displayMessage);
    }

    private void initialize(Throwable exception, String message) {
        if (message == null) {
            message = UIMessageText.DEFAULT_FATAL_MESSAGE.getText();
        }
        primaryArea.setText(message);
        primaryArea.setFont(UIManager.getFont(UIThemeName.OPTIONPANE_FONT));

        fillStackTrace(exception);
        layoutDialog();
        WindowPlacer.centerOnOwnerOrWindow(this);
    }

    /****************************************************************************************************
     * Lays out the dialog.
     ***************************************************************************************************/
    private void layoutDialog() {
        Container container = getContentPane();

        addButton(okayButton);
        addButton(detailButton);
        addButton(copyToClipboardButton);

        primaryPanel.setLayout(new BorderLayout());
        primaryPanel.add(iconLabel, BorderLayout.WEST);
        primaryPanel.add(primaryArea, BorderLayout.CENTER);

        container.setLayout(new BorderLayout(0, 10));
        container.add(primaryPanel, BorderLayout.NORTH);
        container.add(scrollPane, BorderLayout.CENTER);
    }

    /****************************************************************************************************
     * Fills the stack trace of the exception into the details box and into the system clipboard.
     ***************************************************************************************************/
    private void fillStackTrace(Throwable exception) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(stream);
        exception.printStackTrace(writer);
        writer.flush();
        detailArea.setText(stream.toString());
        writer.close();
    }

    /****************************************************************************************************
     * Creates action listener for copy to clipboard button.
     ***************************************************************************************************/
    private ActionListener createCopyToClipboardListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                String selection = detailArea.getText();
                StringSelection data = new StringSelection(selection);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(data, data);
                copyToClipboardButton.setEnabled(false);
            }
        };
    }

    /****************************************************************************************************
     * Creates action listener for details button.
     ***************************************************************************************************/
    private ActionListener createDetailsListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setSize(500, 425);
                scrollPane.setVisible(true);
                detailButton.setEnabled(false);
                copyToClipboardButton.setEnabled(true);
                setVisible(true);
            }
        };
    }

    /****************************************************************************************************
     * Creates action listener for close button.
     ***************************************************************************************************/
    private ActionListener createCloseListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                dispose();
            }
        };
    }
}
