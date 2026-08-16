package oracle.retail.sim.client.swing.dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.event.EnterKeyAdapter;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.BusinessExceptionDisplayer;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIExceptionDisplayer;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RDisplayTextArea;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.MessageText;

/********************************************************************************************************
 * This class is a popup modal window that displays an exception.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RErrorDialog extends JDialog {
    private static final long serialVersionUID = 8550194583663599975L;

    private RPanel iconPanel = new RPanel(new GridBagLayout());
    private RPanel buttonPanel = new RPanel(new FlowLayout(FlowLayout.CENTER));

    protected RLabel iconLabel = new RLabel();
    protected RDisplayTextArea primaryArea = new RDisplayTextArea();

    protected RDisplayTextArea dataListArea = new RDisplayTextArea();
    protected RScrollPane dataListPane = new RScrollPane(dataListArea);
    protected static final String OKAY = "OK";
    protected RButton okayButton = new RButton(OKAY);

    /****************************************************************************************************
     * Constructs new RErrorDialog around a system exception.
     * <p>
     * @param frame The frame that should own this dialog.
     ***************************************************************************************************/
    public RErrorDialog(JFrame frame) {
        super(frame, true);
        buildErrorDialog();
    }

    /****************************************************************************************************
     * Constructs new RErrorDialog around a system exception.
     * <p>
     * @param dialog The dialog that should own this dialog.
     * @param exception A Throwable object to display.
     ***************************************************************************************************/
    public RErrorDialog(JDialog dialog) {
        super(dialog, true);
        buildErrorDialog();
    }

    /****************************************************************************************************
     * Builds the default dialog settings.
     ***************************************************************************************************/
    private void buildErrorDialog() {
        setTitle(Translator.getText("Error"));
        setResizable(false);

        iconLabel.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_ERROR_ICON));
        primaryArea.setFont(UIManager.getFont(UIThemeName.RERRORDIALOG_FONT));
        dataListArea.setFont(UIManager.getFont(UIThemeName.RERRORDIALOG_FONT));
        dataListArea.setFocusable(true);
        dataListArea.setLineWrap(false);

        dataListPane.setLoweredBorder();
        dataListPane.setVisible(false);
        dataListPane.setExtendedBackground(getBackground());

        okayButton.addActionListener(createCloseListener());
        okayButton.addKeyListener(createCloseKeyListener());

        iconPanel.add(iconLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
        iconPanel.add(new RLabel(), GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));

        buttonPanel.add(okayButton);

        getRootPane().setDefaultButton(okayButton);
    }

    /****************************************************************************************************
    * Assigns title to the dialog, but translating it first.
    * <p>
    * @param message The message to display.
    ***************************************************************************************************/
    public void setTitle(String title) {
        super.setTitle(Translator.getText(title));
    }

    /****************************************************************************************************
     * Set a message to display in the primary area of the error window. It will default to a fatal
     * system error message if null or empty message is passed in.
     * <p>
     * @param message The message to display.
     ***************************************************************************************************/
    public void setMessage(MessageText message) {
        if (message == null) {
            message = UIMessageText.DEFAULT_FATAL_MESSAGE;
        }
        primaryArea.setText(Translator.getMessage(message.getText()));
    }

    /****************************************************************************************************
     * Set a message to display in the primary area of the error window. It will default to a fatal
     * system error message if null or empty message is passed in.
     * <p>
     * @param message The message to display.
     ***************************************************************************************************/
    public void setMessage(MessageText message, Object[] messageValues) {
        if (message == null) {
            message = UIMessageText.DEFAULT_FATAL_MESSAGE;
        }
        primaryArea.setText(Translator.getMessage(message.getText(), messageValues));
    }

    /****************************************************************************************************
     * Sets the information in the exception in the window
     * <p>
     * @param message The message to display.
     ***************************************************************************************************/
    public void setMessage(Throwable exception) {
        if (exception instanceof UIException) {
            displayMessage((UIException) exception);
        } else if (exception instanceof BusinessException) {
            displayMessage((BusinessException) exception);
        } else {
            displayMessage(exception);
        }
    }

    /****************************************************************************************************
     * Helper method to display UIException information
     * <p>
     * @param message The message to display.
     ***************************************************************************************************/
    private void displayMessage(UIException exception) {
        if (exception.isFatal() || exception.isError()) {
            setTitle("Error");
            iconLabel.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_ERROR_ICON));
        } else {
            setTitle("Warning");
            iconLabel.setIcon(UIManager.getIcon(UIThemeName.OPTIONPANE_WARNING_ICON));
        }
        primaryArea.setText(new UIExceptionDisplayer().getDisplayText(exception));
    }

    /****************************************************************************************************
     * Sets the information in the exception in the window
     * <p>
     * @param message The message to display.
     ***************************************************************************************************/
    private void displayMessage(BusinessException exception) {
        primaryArea.setText(new BusinessExceptionDisplayer().getDisplayText(exception));
        setDataList(exception.getPrimaryDataList());
    }

    /****************************************************************************************************
     * Helper method displays default fatal message in the case of an unexpected error.
     * <p>
     * @param message The message to display.
     ***************************************************************************************************/
    private void displayMessage(Throwable exception) {
        primaryArea.setText(Translator.getMessage(UIMessageText.DEFAULT_FATAL_MESSAGE.getText()));
    }

    /****************************************************************************************************
     * Assigns a data list to display in a special area of the window beneath the primary error message.
     * It displays each element of the parameter on its own row.
     * <p>
     * @param dataList A List of strings to display in the data display area of the error dialog.
     ***************************************************************************************************/
    private void setDataList(List<Object> dataList) {
        if (dataList != null && !dataList.isEmpty()) {
            StringBuilder textBuffer = new StringBuilder();
            for (Object description : dataList) {
                textBuffer.append(description.toString());
                textBuffer.append("\n");
            }
            dataListArea.setText(textBuffer.toString());
            dataListPane.setVisible(true);
        }
    }

    /****************************************************************************************************
     * Activate the dialog and displays the error. This is the only method by which the error dialog
     * should be displayed.
     ***************************************************************************************************/
    public void activate() {
        if (dataListPane.isVisible()) {
            RPanel mainPanel = new RPanel(new GridBagLayout());
            mainPanel.add(iconPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 10));
            mainPanel.add(primaryArea, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 10));
            mainPanel.add(dataListPane, GridTool.constraints(1, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 10));
            mainPanel.add(buttonPanel, GridTool.constraints(0, 2, 2, 1, 1, 0, 0, 1, 0, 0, 0, 0));
            setContentPane(mainPanel);
            setSize(500, 400);
        } else {
            RPanel mainPanel = new RPanel(new BorderLayout(0, 10));
            mainPanel.setEmptyBorder(15, 10, 10, 10);
            mainPanel.add(iconPanel, BorderLayout.WEST);
            mainPanel.add(primaryArea, BorderLayout.CENTER);
            mainPanel.add(buttonPanel, BorderLayout.SOUTH);
            setContentPane(mainPanel);
            setSize(500, 175);
        }
        WindowPlacer.centerOnOwnerOrWindow(this);
        setVisible(true);
    }

    /****************************************************************************************************
     * Creates action listener for close button.
     ***************************************************************************************************/
    private ActionListener createCloseListener() {
        return new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                setVisible(false);
                dispose();
            }
        };
    }

    /****************************************************************************************************
     * Creates key listener for close button.
     ***************************************************************************************************/
    private KeyListener createCloseKeyListener() {
        return new EnterKeyAdapter() {
            public void enterKeyPressed(KeyEvent event) {
                setVisible(false);
                dispose();
            }
        };
    }
}
