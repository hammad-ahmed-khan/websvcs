package oracle.retail.sim.client.screen.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.swing.dialog.RConfirmDialog;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.config.ConfigurationOption;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.security.SecurityUtility;
import oracle.retail.sim.common.security.UserSecurityMode;
import oracle.retail.sim.common.stockcount.StockCountMessageText;
import oracle.retail.sim.common.stockcount.StockCountSalesProcess;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * System Administration Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SystemAdminModel extends SimScreenModel {
    private static final String CONFIG_TOPIC_PASSWORD = "PASSWORD";
    private static final String CONFIG_TOPIC_SECURITY = "SECURITY";

    private List<ConfigurationOption> configurationOptions;

    /**
     * Return the maximum transaction line items for UI display.
     */
    public int getMaxProductGroupLineItems() {
        return SimConfigManager.MAX_PRODUCT_GROUP_ITEMS;
    }

    /**
     * This method retrieves the List of System Admin Topics of the configuration options.
     * Password options are modified using PasswordConfigScreen
     */
    public List<String> getSystemAdminTopics() throws Exception {
        List<String> topics = ClientServiceFactory.getConfigServices().getSystemAdminTopics();
        topics.remove(CONFIG_TOPIC_PASSWORD);
        if (SecurityUtility.getUserSecurityMode() == UserSecurityMode.EXTERNAL) {
            topics.remove(CONFIG_TOPIC_SECURITY);
        }
        return topics;
    }

    public List<ConfigurationOption> findConfigurationOptions() throws Exception {
        if (configurationOptions == null) {
            List<ConfigurationOption> options = ClientServiceFactory.getConfigServices().readConfigOptions();
            UserSecurityMode securityMode = SecurityUtility.getUserSecurityMode();
            configurationOptions = new ArrayList<>(options.size());
            for (ConfigurationOption option : options) {
                if (isConfigurationOptionDisplayed(option, securityMode)) {
                    configurationOptions.add(option);
                }
            }
        }
        return configurationOptions;
    }

    private boolean isConfigurationOptionDisplayed(ConfigurationOption configurationOption, UserSecurityMode securityMode) {
        String configKey = configurationOption.getConfigKey();
        String configTopic = configurationOption.getConfigTopic();
        if (configTopic.equalsIgnoreCase(CONFIG_TOPIC_PASSWORD)) {
            return false;
        }
        if (securityMode == UserSecurityMode.EXTERNAL) {
            if (configTopic.equalsIgnoreCase(CONFIG_TOPIC_SECURITY)) {
                return false;
            }
            if (configKey.equalsIgnoreCase(SimConfigManager.SECURITY_DAYS_TO_HOLD_DELETED_USERS)) {
                return false;
            }
            if (configKey.equalsIgnoreCase(SimConfigManager.SECURITY_DAYS_TO_HOLD_EXPIRED_USER_ROLES)) {
                return false;
            }
        }
        return true;
    }

    public List<ConfigurationOption> findAvailableConfigurationOptions(String topic) throws Exception {
        List<ConfigurationOption> options = findConfigurationOptions();
        if (StringHelper.isNullOrEmpty(topic)) {
            return options;
        }
        List<ConfigurationOption> availableOptions = new ArrayList<>();
        for (ConfigurationOption option : options) {
            if (topic.equals(option.getConfigTopic())) {
                availableOptions.add(option);
            }
        }
        return availableOptions;
    }

    public void saveConfigurationOptions() throws Exception {
        if (configurationOptions == null || configurationOptions.isEmpty()) {
            return;
        }

        ClientServiceFactory.getConfigServices().updateConfigOptions(configurationOptions);
        //Update the config cache
        SimConfigManager.reloadConfigMap();
    }

    public boolean validateConfigurationOptions() throws BusinessException {
        if (configurationOptions == null || configurationOptions.isEmpty()) {
            return false;
        }

        Map<String, Object> options = getConfigurationOptionsMap();

        String stringValue1 = (String) options.get(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UNIT);
        String stringValue2 = (String) options.get(SimConfigManager.STOCK_COUNT_SALES_PROCESS_UA);

        if (StockCountSalesProcess.TIMESTAMP.getCode().equals(stringValue1) || StockCountSalesProcess.TIMESTAMP.getCode().equals(stringValue2)) {
            RConfirmDialog dialog = new RConfirmDialog(Application.getFrame(), "Confirmation");
            dialog.setMessage(StockCountMessageText.TIMESTAMP_PROCESSING_WARNING);
            dialog.setYesNoType();
            if (!dialog.getConfirmation()) {
                return false;
            }
        }

        Integer integerValue = (Integer) options.get(SimConfigManager.STOCK_COUNT_LOCKOUT_DAYS);
        if (integerValue == null || integerValue < SimConfigManager.STOCK_COUNT_LOCKOUT_DAYS_DEFAULT) {
            throw buildNumericException(CommonMessageText.ADMIN_LOCKOUT_DAYS_ERROR, SimConfigManager.STOCK_COUNT_LOCKOUT_DAYS_DEFAULT);
        }
        return true;
    }

    private Map<String, Object> getConfigurationOptionsMap() {
        Map<String, Object> options = new HashMap<>(configurationOptions.size());
        for (ConfigurationOption option : configurationOptions) {
            options.put(option.getConfigKey(), option.getConfigValue());
        }
        return options;
    }

    private BusinessException buildNumericException(MessageText message, Number value) {
        String valueText = LocaleManager.getNumberFormatter().format(value);
        return new BusinessException(message, valueText);
    }
}
