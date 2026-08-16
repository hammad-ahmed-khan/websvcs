package oracle.retail.sim.client.security;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.TranslatorManager;
import oracle.retail.sim.client.screen.login.SimLoginDialog;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.util.ClientDataCacheUtility;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.core.DeviceType;
import oracle.retail.sim.common.core.UniversalContext;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.logging.LogNames;
import oracle.retail.sim.common.logging.LogService;
import oracle.retail.sim.common.security.ClientSecurityAuditUtility;
import oracle.retail.sim.common.security.JndiCredentialProvider;
import oracle.retail.sim.common.security.PermissionSet;
import oracle.retail.sim.common.security.User;
import oracle.retail.sim.common.security.UserInputJndiCredentialProvider;
import oracle.retail.sim.common.security.UserLoginVO;
import oracle.retail.sim.common.store.Store;
import oracle.retail.sim.common.util.ArrayUtility;
import oracle.retail.sim.common.util.JndiServiceManager;
import oracle.retail.sim.service.core.ClientServiceFactory;

/**
 * Copyright 2004, 2013, Oracle. All rights reserved.
 */
public final class SimLoginManager {
    public static final String DEFAULT_PC_USER_ID = "[PC_CLIENT]";
    public static final String SSO_CREDENTIAL = "SSO_CREDENTIAL";
    public static final String SSO_USER = "SSO_USER";

    private static Integer daysUntilPasswordExpires;
    private static String ssoUserName;
    private static char[] ssoCredentials;
    private static boolean autoLogout;

    private SimLoginManager() {
    }

    public static Integer getDaysUntilPasswordExpires() {
        return daysUntilPasswordExpires;
    }

    public static void setSsoUserName(String userName) {
        ssoUserName = userName;
    }

    public static void setSsoCredentials(char[] credentials) {
        ssoCredentials = credentials;
    }

    public static boolean isSsoLogin() {
        return !StringHelper.isNullOrEmpty(ssoUserName);
    }

    public static boolean isSessionActive() {
        return SimRepository.getUser() != null;
    }

    public static boolean isAutoLogout() {
        return autoLogout;
    }

    public static void clearSession() {
        daysUntilPasswordExpires = null;
        JndiServiceManager.clearState();
        JndiServiceManager.closeSingleContext();
        PermissionManager.setPermissions(null);
        ClientDataCacheUtility.clearCache();
        SimRepository.clearRepository();
        RepositoryManager.clearStateObjects();
        UniversalContext.startSession(DEFAULT_PC_USER_ID, DeviceType.PC);
    }

    public static boolean login() {
        clearSession();
        if (isSsoLogin()) {
            return ssoLogin();
        }
        return dialogLogin();
    }

    public static void loginStore(Store store) throws Exception {
        UserLoginVO userLoginVO = BOFactory.createUserLoginVO();
        userLoginVO.doSetUser(SimRepository.getUser());
        userLoginVO.doSetAuthorizedStores(SimRepository.getAllowedStores());
        userLoginVO.doSetDaysUntilPasswordExpires(daysUntilPasswordExpires);
        userLoginVO.doSetSelectedStore(store);
        processUserLogin(userLoginVO);
    }

    private static boolean ssoLogin() {
        UserLoginVO userLoginVO = null;
        try {
            userLoginVO = loginUser(ssoUserName, ssoCredentials, true);
            processUserLogin(userLoginVO);
        } catch (Exception e) {
            UIStatusUtility.displayException(LogNames.SECURITY, e);
            //No audit if service call failed, or error message returned
            if (userLoginVO != null && StringHelper.isNullOrEmpty(userLoginVO.getLoginErrorMessage())) {
                ClientSecurityAuditUtility.auditLoginFailureOther(userLoginVO.getUserName(), userLoginVO.getSelectedStoreId(), DeviceType.PC);
            }
            return false;
        }
        autoLogout = true;
        return true;
    }

    private static boolean dialogLogin() {
        SimLoginDialog dialog = new SimLoginDialog(Application.getFrame());
        while (true) {
            UserLoginVO userLoginVO = null;
            try {
                dialog.clearState();
                dialog.setVisible(true);
                if (dialog.isCancelled()) {
                    dialog.closeWindow();
                    return false;
                }
                userLoginVO = loginUser(dialog.getUserName(), dialog.getPassword(), false);
                processUserLogin(userLoginVO);
                dialog.closeWindow();
                return true;
            } catch (Exception e) {
                UIStatusUtility.displayException(LogNames.SECURITY, e);
                //No audit if service call failed, or error message returned
                if (userLoginVO != null && StringHelper.isNullOrEmpty(userLoginVO.getLoginErrorMessage())) {
                    ClientSecurityAuditUtility.auditLoginFailureOther(userLoginVO.getUserName(), userLoginVO.getSelectedStoreId(), DeviceType.PC);
                }
                //Reset session
                clearSession();
            }
        }
    }

    private static UserLoginVO loginUser(String userName, char[] password, boolean singleSignOn) throws Exception {
        userName = StringHelper.trimToNull(userName);
        if (userName == null) {
            throw new BusinessException(CommonMessageText.LOGIN_USERNAME_REQUIRED);
        }
        if (ArrayUtility.isNullOrEmpty(password)) {
            throw new BusinessException(CommonMessageText.LOGIN_PASSWORD_ERROR);
        }
        JndiServiceManager.clearCache();
        JndiServiceManager.closeSingleContext();
        JndiCredentialProvider credentialProvider = JndiServiceManager.getCredentialProvider();
        if (credentialProvider instanceof UserInputJndiCredentialProvider) {
            ((UserInputJndiCredentialProvider) credentialProvider).setCredentials(userName, password);
        }
        try {
            return ClientServiceFactory.getSecurityServices().loginUser(userName, password, singleSignOn, DeviceType.PC);
        } catch (Exception e) {
            if (LogService.isDebugEnabled(LogNames.SECURITY)) {
                LogService.debug(LogNames.SECURITY, "Login failed.", e);
            }
            throw new BusinessException(CommonMessageText.LOGIN_FAILED);
        }
    }

    private static void processUserLogin(UserLoginVO userLoginVO) throws Exception {
        String errorMessage = userLoginVO.getLoginErrorMessage();
        if (!StringHelper.isNullOrEmpty(errorMessage)) {
            if (LogService.isDebugEnabled(LogNames.SECURITY)) {
                throw new BusinessException(CommonMessageText.LOGIN_FAILED_WITH_MESSAGE, errorMessage);
            }
            throw new BusinessException(CommonMessageText.LOGIN_FAILED);
        }
        User user = userLoginVO.getUser();
        if (user == null) {
            throw new BusinessException(CommonMessageText.LOGIN_FAILED);
        }
        List<Store> stores = userLoginVO.getAuthorizedStores();
        if (stores.isEmpty()) {
            throw new BusinessException(CommonMessageText.LOGIN_NOT_ALLOWED);
        }
        if (userLoginVO.getSelectedStore() == null) {
            userLoginVO.doSetSelectedStore(stores.get(0));
        }
        Store store = userLoginVO.getSelectedStore();
        if (store == null || !stores.contains(store)) {
            throw new BusinessException(CommonMessageText.LOGIN_FAILED);
        }
        TimeZone timeZone = store.getTimeZone();
        if (timeZone == null) {
            throw new BusinessException(CommonMessageText.LOGIN_STORE_TIMEZONE_FAILED, store.toDisplayString());
        }
        Locale locale = user.getLocale();
        if (locale == null) {
            locale = store.getLocale();
            if (locale == null) {
                throw new BusinessException(CommonMessageText.LOGIN_USER_LOCALE_FAILED, user.getUserName());
            }
        }

        PermissionSet permissions = userLoginVO.getAuthorizedPermissions();
        if (permissions.isEmpty()) {
            permissions = ClientServiceFactory.getSecurityServices().readUserAuthorizedPermissions(user.getUserName(), store.getId(), DeviceType.PC);
        }

        SimRepository.setUser(user);
        SimRepository.setStore(store);
        SimRepository.setAllowedStores(userLoginVO.getAuthorizedStores());
        PermissionManager.setPermissions(permissions);
        daysUntilPasswordExpires = userLoginVO.getDaysUntilPasswordExpires();

        TranslatorManager.cacheLocale(locale);
        LocaleManager.setLanguageLocale(locale);
        LocaleManager.setNumericLocale(store.getLocale());
        LocaleManager.setTimeZone(timeZone);

        UniversalContext.startSession(user.getUserName(), DeviceType.PC, locale);
        RepositoryManager.addStateObject(SimClientStateKey.LOGIN_USER, true);
    }
}
