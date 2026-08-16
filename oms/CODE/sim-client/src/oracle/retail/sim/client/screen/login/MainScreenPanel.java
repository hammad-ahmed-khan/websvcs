package oracle.retail.sim.client.screen.login;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.util.List;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.editor.SimEditorFactory;
import oracle.retail.sim.client.security.SimLoginManager;
import oracle.retail.sim.client.swing.dialog.RInfoDialog;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ScreenPanel;
import oracle.retail.sim.client.swing.panel.RBackgroundPanel;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.security.SecurityUtility;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserSecurityMode;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * Main Screen Panel
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MainScreenPanel extends ScreenPanel implements REventListener {
    private static final long serialVersionUID = 426013502670884608L;

    private static final String STORE_SELECTED = "Store.selected";

    private RBackgroundPanel backgroundPanel = new RBackgroundPanel();
    private RComboBoxEditor storeEditor = SimEditorFactory.createStoreComboEditor("Store");

    /****************************************************************************************************
     * CONSTRUCTOR
     ***************************************************************************************************/

    public MainScreenPanel() {
        initializePanel();
        layoutPanel();
    }

    /****************************************************************************************************
     * INITIALIZE PANEL
     ***************************************************************************************************/

    private void initializePanel() {
        Icon icon = UIManager.getIcon(UIThemeName.LARGE_ORACLE_ICON);
        if (icon instanceof ImageIcon) {
            backgroundPanel.setBackgroundImageIcon((ImageIcon) icon);
        }
        storeEditor.setSizeType(EditorConstants.MEDIUM);
        storeEditor.registerAction(this, STORE_SELECTED);
    }

    private void layoutPanel() {
        RPanel topPanel = new RPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(storeEditor);

        RPanel mainPanel = new RPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(backgroundPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    public SimScreenModel getScreenModel() {
        return null;
    }

    public SimTable getScreenTable() {
        return null;
    }

    /****************************************************************************************************
     * GET AND SET DATA
     ***************************************************************************************************/

    public void start() {
        displayStores();
        handlePostLogin();
    }

    public void assignFocusInScreen() {
        if (SimRepository.getAllowedStores().size() > 1) {
            assignFocusInScreen(storeEditor);
            return;
        }
        super.assignFocusInScreen();
    }

    public void performActionEvent(RActionEvent event) {
        String command = event.getEventCommand();
        if (command.equals(STORE_SELECTED)) {
            try {
                Store store = (Store) storeEditor.getSelectedItem();
                if (store != null) {
                    SimLoginManager.loginStore(store);
                    notifyREventListeners(new RActionEvent(this, SimClientStateKey.LOGIN_STORE));
                }
            } catch (Exception e) {
                displayException(e);
            }
            displayStores();
        }
    }

    private void displayStores() {
        storeEditor.setActionsEnabled(false);
        storeEditor.setTitle("Store");
        List<Store> stores = SimRepository.getAllowedStores();
        if (stores.size() > 1) {
            storeEditor.setItems(stores);
            storeEditor.removeEmptySelection();
            storeEditor.setSelectedItem(SimRepository.getStore());
            storeEditor.setVisible(true);
        } else {
            storeEditor.clear();
            storeEditor.setVisible(false);
        }
        storeEditor.setActionsEnabled(true);
    }

    private void handlePostLogin() {
        if (RepositoryManager.getStateObject(SimClientStateKey.LOGIN_USER) == null) {
            return;
        }
        RepositoryManager.removeStateObject(SimClientStateKey.LOGIN_USER);
        handlePasswordState();
    }

    private void handlePasswordState() {
        if (SecurityUtility.getUserSecurityMode() == UserSecurityMode.EXTERNAL) {
            return;
        }
        User user = SimRepository.getUser();
        if (user.isCached()) {
            return;
        }
        //Check if user is required to change password
        if (SimConfigManager.getBoolean(SimConfigManager.PASSWORD_INITIAL_CHANGE) && user.isChangePassword()) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    handleChangeInitialPassword();
                }
            });
            return;
        }
        //Check password expiration for notification
        if (SimLoginManager.getDaysUntilPasswordExpires() != null) {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    handlePasswordExpiration();
                }
            });
        }
    }

    private void handleChangeInitialPassword() {
        ChangePasswordDialog dialog = new ChangePasswordDialog();
        dialog.setVisible(true);
        //Logout if password change failed
        if (SimRepository.getUser().isChangePassword()) {
            handleLogout();
        }
    }

    public void handleLogout() {
        if (SimLoginManager.isAutoLogout()) {
            Application.getFrame().closeWindow();
            return;
        }
        Application.getApplicationFrame().clearSession();
        SimLoginManager.clearSession();
        Application.getNavigationManager().showLogoutScreen();
    }

    private void handlePasswordExpiration() {
        Integer daysUntilPasswordExpires = SimLoginManager.getDaysUntilPasswordExpires();
        if (daysUntilPasswordExpires == null) {
            return;
        }
        Integer passwordDurationDays = SimConfigManager.getInteger(SimConfigManager.PASSWORD_DAYS_UNTIL_EXPIRES);
        if (passwordDurationDays == null || passwordDurationDays <= 0) {
            return;
        }
        Integer passwordExpirationNotificationDays = SimConfigManager.getInteger(SimConfigManager.PASSWORD_DAYS_BEFORE_EXPIRES_TO_NOTIFY);
        if (passwordExpirationNotificationDays == null || passwordExpirationNotificationDays <= 0) {
            return;
        }
        if (passwordExpirationNotificationDays >= daysUntilPasswordExpires) {
            RInfoDialog dialog = new RInfoDialog(Application.getFrame(), "Password Expiration");
            dialog.displayMessage(CommonMessageText.SECURITY_PASSWORD_EXPIRATION_NOTIFICATION, daysUntilPasswordExpires.toString());
        }
    }
}
