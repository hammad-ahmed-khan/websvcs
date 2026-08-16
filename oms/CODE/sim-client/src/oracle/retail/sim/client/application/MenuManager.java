package oracle.retail.sim.client.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.config.NavigationTabData;
import oracle.retail.sim.common.config.NavigationTaskData;
import oracle.retail.sim.common.config.NavigationTaskItemData;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.store.Store;

/********************************************************************************************************
 * MENU MANAGER
 * <p>
 * A helper class for dealing with the menus.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public final class MenuManager {
    private static final String RUSSIAN = "rus";
    private static final String GREEK = "ell";

    private final Map<String, NavigationTaskData> navigationMap = new HashMap<>(100);

    /****************************************************************************************************
     * Constructor
     ***************************************************************************************************/
    public MenuManager() {
    }

    /****************************************************************************************************
     * This method should only be called by AppBuilder when the menus and such need to be refreshed. When
     * class be reloaded at runtime, then this won't be needed and AppBuilder can reload classes of the
     * applets.
     ***************************************************************************************************/
    public void loadMenuManager() {
        loadNavigationMenus();
    }

    /****************************************************************************************************
     * Loads all the menus for navigation
     ***************************************************************************************************/
    private void loadNavigationMenus() {
        NavigationTabData[] applications = Application.getNavigationData().getTabs();
        NavigationTaskData[] menus = applications[0].getTasks();
        for (NavigationTaskData menu : menus) {
            navigationMap.put(menu.getDisplayName(), menu);
        }
    }

    /****************************************************************************************************
     * Builds a set of buttons for the appropriate parameters.
     ***************************************************************************************************/
    public JButton[] getMenu(Class<?> classType, User user, Store store) {
        NavigationTaskData menu = navigationMap.get(classType.getName());
        NavigationTaskItemData[] menuItems = menu.getTaskItems();
        List<JButton> buttonList = new ArrayList<>();

        for (NavigationTaskItemData menuItem : menuItems) {
            JButton button = buildButton(menuItem, user, store);
            if (button != null) {
                buttonList.add(button);
            }
        }
        JButton[] buttons = buttonList.toArray(new JButton[buttonList.size()]);
        MnemonicLetter.applyDynamicMnemonics(buttons, isNativeLetterLanguage());
        return buttons;
    }

    /****************************************************************************************************
     * Returns true if the current language should attempt to use native letters as mnemonics.
     * Currently only applies to Russian and Greek. Do not throw error if resource is not found in
     * JAVA.
     ***************************************************************************************************/
    private boolean isNativeLetterLanguage() {
        try {
            String language = LocaleManager.getLanguageLocale().getISO3Language();
            return RUSSIAN.equals(language) || GREEK.equals(language);
        } catch (Throwable exception) {
            LogService.error(getClass(), exception.getMessage());
            return false;
        }
    }

    /****************************************************************************************************
     * Builds a single button from the menu item object
     ***************************************************************************************************/
    private JButton buildButton(NavigationTaskItemData menuItem, User user, Store store) {
        String title = menuItem.getDisplayName();
        String command = menuItem.getActionCommand();
        if (StringUtility.isNullOrEmpty(command)) {
            return null;
        }
        try {
            NavigationPermission permission = UIPermissionManager.getNavigationPermission(menuItem.getPermissionName());
            if (permission == NavigationPermission.NONE) {
                return null;
            }
        } catch (UIException e) {
            return null;
        }
        JButton button = new JButton();
        button.setName(title);
        button.setText(Translator.getText(title));
        button.setActionCommand(command);
        button.setEnabled(true);
        return button;
    }
}
