package oracle.retail.sim.client.swing.displaytable;

import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.JDialog;
import javax.swing.JFrame;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RTabbedPane;

/*************************************************************************************************
 * Controls table configuration for RDisplayTable.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *************************************************************************************************/

public class RDisplayTableConfigDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = 4280357388642223739L;

    private static RDisplayTableConfigDialog dialogInstance;

    private RTabbedPane tabbedPane = new RTabbedPane();

    private ROrderTab orderTab = new ROrderTab("Order");
    private RFilterTab filterTab = new RFilterTab("Filter");
    private RSortTab sortTab = new RSortTab("Sort");
    private RGeneralTab generalTab = new RGeneralTab("General");

    private static final String OKAY = "Ok";
    private static final String CANCEL = "Cancel";

    private RButton okayButton = new RButton(OKAY);
    private RButton cancelButton = new RButton(CANCEL);

    private TableConfigurationData configurationData;

    /*************************************************************************************************
     * Constructs a new table configuration dialog with a frame as an owner.
     * <p>
     * @param frame The owner.
     *************************************************************************************************/
    public RDisplayTableConfigDialog(JFrame frame) {
        super(frame);
        initializeConfigDialog();
        layoutConfigDialog();
    }

    /*************************************************************************************************
     * Constructs a new table configuration dialog with a dialog as an owner.
     * <p>
     * @param dialog The owner.
     *************************************************************************************************/
    public RDisplayTableConfigDialog(JDialog dialog) {
        super(dialog);
        initializeConfigDialog();
        layoutConfigDialog();
    }

    /*************************************************************************************************
     * Initializes all the components of the dialog.
     *************************************************************************************************/
    private void initializeConfigDialog() {
        setTitle("Table Configuration");
        setSize(700, 450);
        setStatusBarVisible(false);

        dialogInstance = this;

        okayButton.registerAction(this, OKAY);
        cancelButton.registerAction(this, CANCEL);
    }

    /*************************************************************************************************
     * Lays out all the components of the dialog.
     *************************************************************************************************/
    private void layoutConfigDialog() {
        addButton(okayButton);
        addButton(cancelButton);

        tabbedPane.setBackground(Color.WHITE);
        //		tabbedPane.addTab(orderTab);
        tabbedPane.addTab(filterTab);
        tabbedPane.addTab(sortTab);
        tabbedPane.addTab(generalTab);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        centerOnOwner();
    }

    /*************************************************************************************************
     * Static access for the dialog.
     *************************************************************************************************/
    public static RDisplayTableConfigDialog getDialog() {
        return dialogInstance;
    }

    /*************************************************************************************************
     * Initializes the table configuration dialog.
     * <p>
     * @param identifier The table identifier.
     * @param headers The table headers.
     *************************************************************************************************/
    protected void initialize(String identifier, String[] headers) {
        setTableHeaders(headers);
        setIdentifier(identifier);
        initializeTabs();
    }

    /*************************************************************************************************
     * Assigns the table headers to the configuration dialog and initializes the tab.
     *************************************************************************************************/
    private void setTableHeaders(String[] originalHeaders) {
        String[] translatedHeaders = new String[originalHeaders.length];
        int index = -1;
        for (int i = 0; i < originalHeaders.length; i++) {
            index = originalHeaders[i].indexOf("|");
            if (index == -1) {
                translatedHeaders[i] = Translator.getText(originalHeaders[i]);
            } else {
                translatedHeaders[i] = Translator.getText(originalHeaders[i].substring(0, index)) + " " + Translator.getText(originalHeaders[i].substring(index + 1));
            }
        }
        filterTab.setHeaders(originalHeaders, translatedHeaders);
        orderTab.setHeaders(translatedHeaders);
        sortTab.setHeaders(originalHeaders, translatedHeaders);
    }

    /*************************************************************************************************
     * Assigns an identifier to the table configuration dialog.
     * <p>
     * @param identifier The identifier to assign.
     *************************************************************************************************/
    private void setIdentifier(String identifier) {
        if (StringUtility.isNullOrEmpty(identifier)) {
            identifier = UIPropertyName.GENERIC_TABLE_IDENTIFIER;
        }
        configurationData = TableConfigurationRepository.getTableConfigurationData(identifier);
    }

    /*************************************************************************************************
     * Initializes each tab with the configuration data.
     *************************************************************************************************/
    private void initializeTabs() {
        orderTab.initialize(configurationData);
        filterTab.initialize(configurationData);
        sortTab.initialize(configurationData);
        generalTab.initialize(configurationData);
    }

    /*************************************************************************************************
     * Implements the action listener method to handle the two button actions.
     *************************************************************************************************/
    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();

        if (command.equals(CANCEL)) {
            closeWindow();
        } else if (command.equals(OKAY)) {
            saveConfiguration();
        }
    }

    /*************************************************************************************************
     * Save the configuration information.
     *************************************************************************************************/
    private void saveConfiguration() {
        orderTab.save(configurationData);
        filterTab.save(configurationData);
        sortTab.save(configurationData);
        generalTab.save(configurationData);

        closeWindow();

        firePropertyChange(UIPropertyName.TABLE_CONFIGURATION_ALTERED, null, configurationData);
    }
}
