package oracle.retail.sim.client.application;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import javax.swing.JFrame;
import javax.swing.border.EmptyBorder;
import oracle.retail.sim.client.core.SimApplicationConfig;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.security.PermissionManager;
import oracle.retail.sim.client.swing.dialog.RDialog;
import oracle.retail.sim.client.swing.displayer.DateDisplayer;
import oracle.retail.sim.client.swing.displayer.DateTimeLongDisplayer;
import oracle.retail.sim.client.swing.displayer.HoursMinutesDisplayer;
import oracle.retail.sim.client.swing.displayer.TimeLongDisplayer;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RDisplayLabelEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.panel.RButtonPanel;
import oracle.retail.sim.client.swing.panel.REditorPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableAttribute;
import oracle.retail.sim.client.swing.table.SimTableDefinition;
import oracle.retail.sim.client.swing.table.SimTablePane;
import oracle.retail.sim.client.swing.table.SimTableSortAttribute;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RTab;
import oracle.retail.sim.client.swing.widget.RTabbedPane;
import oracle.retail.sim.common.date.SimDateUtil;
import oracle.retail.sim.common.security.PermissionKey;
import oracle.retail.sim.common.theme.CustomTheme;
import oracle.retail.sim.service.core.ClientServiceFactory;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/********************************************************************************************************
 * CLIENT STATUS DIALOG
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ClientStatusDialog extends RDialog implements REventListener {
    private static final long serialVersionUID = -9164604242188208747L;

    public static final String NATIVE_COMMANDS = "NATIVE.COMMANDS";
    public static final String TCP_PORT = "TCP_PORT";

    private static final String DONE = "Done";

    private RButton cancelButton = new RButton(DONE);
    private RTabbedPane tabbedPanel = new RTabbedPane();
    private StatsPanel statsPanel = new StatsPanel();
    private VersionPanel versionPanel = new VersionPanel();
    private LookFeelPanel lookFeelPanel = new LookFeelPanel();
    private StatePanel statePanel = new StatePanel();
    private AboutPanel aboutPanel = new AboutPanel();

    public ClientStatusDialog(JFrame frame) {
        super(frame, true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle("Client Information");
        setStatusBarVisible(false);
        setSize(700, 500);
        initInfoDialog();
        layoutInfoDialog();
        centerOnOwner();
    }

    private void initInfoDialog() {
        cancelButton.registerAction(this, DONE);
    }

    private void layoutInfoDialog() {
        addButton(cancelButton);

        tabbedPanel.addTab("Stats", statsPanel);
        tabbedPanel.addTab("Version", versionPanel);
        tabbedPanel.addTab("Look & Feel", lookFeelPanel);
        if (hasActivateDebuggingPermission()) {
            tabbedPanel.addTab("Repository", statePanel);
        }
        tabbedPanel.addTab("About", aboutPanel);

        RPanel mainPanel = new RPanel(new BorderLayout());
        mainPanel.add(tabbedPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    public void load() {
        statsPanel.loadData();
        versionPanel.loadData();
        if (hasActivateDebuggingPermission()) {
            statePanel.loadData();
        }
        lookFeelPanel.loadData();

        aboutPanel.loadAboutInformation();

        loadNativeCommands();
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(DONE)) {
            closeWindow();
        }
    }

    private boolean hasActivateDebuggingPermission() {
        return PermissionManager.hasPermission(PermissionKey.PC_ACTIVATE_DEBUGGING);
    }

    /****************************************************************************************************
     * STATS PANEL
     ***************************************************************************************************/

    private class StatsPanel extends RTab implements REventListener {
        private static final long serialVersionUID = 5067626805281446684L;

        private static final String LOG_LEVEL_MODIFIED = "LogLevel.modified";

        private RDisplayLabelEditor processDateEditor = new RDisplayLabelEditor("Current Date", true);
        private RDisplayLabelEditor dateStartedEditor = new RDisplayLabelEditor("Date Started", true);
        private RDisplayLabelEditor timeStartedEditor = new RDisplayLabelEditor("Time Started", true);
        private RDisplayLabelEditor timeUpEditor = new RDisplayLabelEditor("Time Up", true);
        private RDisplayLabelEditor internetAddressEditor = new RDisplayLabelEditor("IP Address", true);
        private RDisplayLabelEditor managePortEditor = new RDisplayLabelEditor("Manage Port", true);
        private RDisplayLabelEditor totalMemoryEditor = new RDisplayLabelEditor("Total Memory", true);
        private RDisplayLabelEditor freeMemoryEditor = new RDisplayLabelEditor("Free Memory", true);
        private RDisplayLabelEditor screenNameEditor = new RDisplayLabelEditor("Screen Name");
        private RDisplayLabelEditor screenClassEditor = new RDisplayLabelEditor("Screen Class");
        private RDisplayLabelEditor localeEditor = new RDisplayLabelEditor("Locale");
        private RDisplayLabelEditor timeZoneEditor = new RDisplayLabelEditor("Time Zone");
        private RCheckBoxEditor logLevelEditor = new RCheckBoxEditor("Debug Activated");

        private StatsPanel() {
            buildPanel();
            layoutPanel();
        }

        private void buildPanel() {
            processDateEditor.setDisplayer(new DateTimeLongDisplayer());
            dateStartedEditor.setDisplayer(new DateDisplayer());
            timeStartedEditor.setDisplayer(new TimeLongDisplayer());
            timeUpEditor.setDisplayer(new HoursMinutesDisplayer());
            logLevelEditor.registerAction(this, LOG_LEVEL_MODIFIED);
        }

        private void layoutPanel() {
            REditorPanel timePanel = new REditorPanel(6);
            timePanel.setTitleBorder("Time");
            timePanel.add(processDateEditor);
            timePanel.add(dateStartedEditor);
            timePanel.add(timeStartedEditor);
            timePanel.add(timeUpEditor);

            REditorPanel internetPanel = new REditorPanel(2);
            internetPanel.setTitleBorder("IP Address");
            internetPanel.add(internetAddressEditor);
            internetPanel.add(managePortEditor);

            REditorPanel memoryPanel = new REditorPanel(2);
            memoryPanel.setTitleBorder("Memory");
            memoryPanel.add(totalMemoryEditor);
            memoryPanel.add(freeMemoryEditor);

            REditorPanel screenPanel = new REditorPanel(4);
            screenPanel.setTitleBorder("Current");
            screenPanel.add(screenNameEditor);
            screenPanel.add(screenClassEditor);
            screenPanel.add(localeEditor);
            screenPanel.add(timeZoneEditor);

            REditorPanel loggerPanel = new REditorPanel(1);
            loggerPanel.setTitleBorder("Client Log File");
            loggerPanel.add(logLevelEditor);

            setLayout(new GridBagLayout());
            add(timePanel, GridTool.constraints(0, 0, 1, 2, 1, 0, 0, 3, 0, 0, 5, 0));
            add(internetPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
            add(memoryPanel, GridTool.constraints(1, 1, 1, 1, 1, 0, 0, 3, 0, 0, 5, 0));
            add(screenPanel, GridTool.constraints(0, 2, 2, 1, 1, 0, 0, 1, 0, 0, 5, 0));
            add(loggerPanel, GridTool.constraints(0, 3, 2, 1, 1, 0, 0, 1, 0, 0, 5, 0));
            add(new RLabel(), GridTool.constraints(0, 4, 2, 1, 1, 1, 0, 3, 0, 0, 5, 0));
        }

        public void loadData() {
            try {
                internetAddressEditor.setData(InetAddress.getLocalHost().getHostAddress());
                managePortEditor.setData(Application.getConfigManager().getString(TCP_PORT));
            } catch (Exception e) {
                internetAddressEditor.setData(Translator.getText("Unknown"));
            }

            processDateEditor.setData(SimDateUtil.getCurrentDate());
            totalMemoryEditor.setData(Runtime.getRuntime().totalMemory());
            dateStartedEditor.setData(Application.getTimeStarted());
            timeStartedEditor.setData(Application.getTimeStarted());

            Long currentTime = SimDateUtil.getCurrentDate().getTime();
            Long elapsedTime = currentTime - Application.getTimeStarted().getTime();
            timeUpEditor.setData(elapsedTime);

            Screen screen = Application.getNavigationManager().getCurrentScreen();
            if (screen != null) {
                screenNameEditor.setData(screen.getScreenName());
                screenClassEditor.setData(screen.getClass().getName());
            }

            Locale locale = LocaleManager.getLanguageLocale();
            if (locale != null) {
                localeEditor.setData(locale.toString());

                TimeZone timeZone = LocaleManager.getTimeZone();
                if (timeZone != null) {
                    StringBuilder timeZoneText = new StringBuilder();
                    timeZoneText.append(timeZone.getDisplayName(locale));
                    timeZoneText.append(" (").append(timeZone.getID());
                    int timeZoneOffset = timeZone.getOffset(currentTime);
                    if (timeZoneOffset >= 0) {
                        timeZoneText.append(", GMT+");
                    } else {
                        timeZoneText.append(", GMT");
                    }
                    timeZoneText.append(new HoursMinutesDisplayer().getDisplayText(timeZoneOffset));
                    timeZoneText.append(")");
                    timeZoneEditor.setData(timeZoneText.toString());
                }
            }

            if (hasActivateDebuggingPermission()) {
                Enumeration<Logger> loggers = LogManager.getLoggerRepository().getCurrentLoggers();
                boolean isAllDebugLevel = true;
                while (loggers.hasMoreElements()) {
                    Logger logger = loggers.nextElement();
                    if (Level.ERROR.equals(logger.getLevel())) {
                        isAllDebugLevel = false;
                        break;
                    }
                }
                logLevelEditor.setActionsEnabled(false);
                logLevelEditor.setSelected(isAllDebugLevel);
                logLevelEditor.setActionsEnabled(true);
            } else {
                logLevelEditor.setEnabled(false);
            }
        }

        public void performActionEvent(RActionEvent event) {
            doLogLevelModified();
        }

        private void doLogLevelModified() {
            Enumeration<Logger> loggers = LogManager.getLoggerRepository().getCurrentLoggers();
            while (loggers.hasMoreElements()) {
                Logger logger = loggers.nextElement();
                if (logLevelEditor.isSelected()) {
                    logger.setLevel(Level.DEBUG);
                } else {
                    logger.setLevel(Level.ERROR);
                }
            }
        }
    }

    /****************************************************************************************************
     * VERSION PANEL
     ***************************************************************************************************/

    private class VersionPanel extends RTab {
        private static final long serialVersionUID = -7496575293780224319L;

        private SimTable versionTable = new SimTable(new KeyValueDefinition("System Property", "Version"));
        private SimTablePane versionPane = new SimTablePane(versionTable);

        private VersionPanel() {
            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(5, 5, 5, 5));
            add(versionPane, BorderLayout.CENTER);
        }

        public void loadData() {
            List<KeyValueData> dataList = new ArrayList<>();
            for (Enumeration<?> enumeration = System.getProperties().propertyNames(); enumeration.hasMoreElements(); ) {
                String key = (String) enumeration.nextElement();
                if (key.endsWith("version") || key.endsWith("dir")) {
                    dataList.add(new KeyValueData(key, System.getProperty(key)));
                }
                if (key.endsWith("name") || key.endsWith("path")) {
                    dataList.add(new KeyValueData(key, System.getProperty(key)));
                }
            }
            versionTable.setRows(dataList);
        }
    }

    /****************************************************************************************************
     * LOOK AND FEEL PANEL
     ***************************************************************************************************/

    private class LookFeelPanel extends RTab implements REventListener {
        private static final long serialVersionUID = 5488477648852042108L;

        private SimTable customThemeTable = new SimTable(new CustomThemeDefinition());
        private SimTablePane customThemePane = new SimTablePane(customThemeTable);

        private static final String SELECTED = "Selected";
        private static final String APPLY = "Apply";

        private RButton applyButton = new RButton(APPLY);

        private LookFeelPanel() {
            initPanel();
            layoutRTab();
        }

        private void initPanel() {
            applyButton.setEnabled(false);
            applyButton.registerAction(this, APPLY);

            customThemeTable.setSingleRowSelectionMode();
            customThemeTable.registerSingleClickAction(this, SELECTED);
        }

        private void layoutRTab() {
            RButtonPanel panel = new RButtonPanel();
            panel.addButton(applyButton);

            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(5, 5, 5, 5));
            add(customThemePane, BorderLayout.CENTER);
            add(panel, BorderLayout.SOUTH);
        }

        public void loadData() {
            try {
                customThemeTable.setRows(ClientServiceFactory.getCustomThemeServices().findActiveCustomThemes());
            } catch (Throwable e) {
                displayException(e);
            }
        }

        public void performActionEvent(RActionEvent event) {
            String command = event.getEventCommand();
            if (command.equals(SELECTED)) {
                doRowSelected();
            } else if (command.equals(APPLY)) {
                doApplyAction();
            }
        }

        private void doRowSelected() {
            applyButton.setEnabled(customThemeTable.getSelectedRowCount() > 0);
        }

        private void doApplyAction() {
            CustomTheme theme = (CustomTheme) customThemeTable.getSelectedRowData();
            ThemeUtility.applyCustomTheme(theme, getTopLevelAncestor());
            SimApplicationConfig.cacheLastThemeName(theme.getName());
        }
    }

    private class CustomThemeDefinition extends SimTableDefinition {
        public Class<?> getDataClass() {
            return CustomTheme.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(2);
            attributes.add(new SimTableAttribute("Name", "name"));
            attributes.add(new SimTableAttribute("Description", "description"));
            return attributes;
        }
    }

    /****************************************************************************************************
     * STATE PANEL
     ***************************************************************************************************/

    private class StatePanel extends RTab {
        private static final long serialVersionUID = 5125195362540024023L;

        private SimTable stateTable = new SimTable(new KeyValueDefinition("Key", "Value"));
        private SimTablePane statePane = new SimTablePane(stateTable);

        private StatePanel() {
            stateTable.setColumnSize("key", 175);

            setLayout(new BorderLayout());
            setBorder(new EmptyBorder(5, 5, 5, 5));
            add(statePane, BorderLayout.CENTER);
        }

        public void loadData() {
            List<KeyValueData> dataList = new ArrayList<>();
            String[] keys = RepositoryManager.getStateKeys();
            for (String key : keys) {
                Object value = RepositoryManager.getStateObject(key);
                if (value != null) {
                    dataList.add(new KeyValueData(key, value.toString()));
                }
            }
            for (String key : SimRepository.getStateKeys()) {
                Object value = SimRepository.getStateObject(key);
                if (value != null) {
                    dataList.add(new KeyValueData(key, value.toString()));
                }
            }
            stateTable.setRows(dataList);
            stateTable.sort(Collections.singletonList(new SimTableSortAttribute("key", true)));
        }
    }

    /****************************************************************************************************
     * KEY VALUE DEFINITION
     ***************************************************************************************************/

    private class KeyValueDefinition extends SimTableDefinition {
        private String keyTitle;
        private String valueTitle;

        private KeyValueDefinition(String kTitle, String vTitle) {
            keyTitle = kTitle;
            valueTitle = vTitle;
        }

        public Class<?> getDataClass() {
            return KeyValueData.class;
        }

        public List<SimTableAttribute> getAttributes() {
            List<SimTableAttribute> attributes = new ArrayList<>(2);
            attributes.add(new SimTableAttribute(keyTitle, "key"));
            attributes.add(new SimTableAttribute(valueTitle, "value"));
            return attributes;
        }
    }

    /****************************************************************************************************
     * LOAD NATIVE COMMANDS
     ***************************************************************************************************/

    private void loadNativeCommands() {
        try {
            String commands = Application.getConfigManager().getString(NATIVE_COMMANDS);
            String osName = System.getProperty("os.name");
            if (osName != null && !osName.toLowerCase(Locale.US).contains("windows")) {
                commands += ",ifconfig -a";
            } else {
                commands += ",ipconfig /all";
            }
            String[] tokens = commands.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
            for (String command : tokens) {
                if (command.isEmpty()) {
                    continue;
                }
                NativeCommandPanel panel = new NativeCommandPanel();
                panel.loadCommand(command);
                tabbedPanel.addTab(command, panel);
            }
        } catch (Exception e) {
            UILog.error(getClass(), UIMessageText.NATIVE_COMMANDS_ERROR, e);
        }
    }
}
