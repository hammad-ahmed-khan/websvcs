package oracle.retail.sim.client.screen.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.config.ConfigurationOption;
import oracle.retail.sim.common.configutil.SimConfigManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.security.PasswordConfiguration;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Password Config Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PasswordConfigModel extends SimScreenModel {
    private static final String CONFIG_TOPIC_PASSWORD = "PASSWORD";

    private Map<String, ConfigurationOption> configurationOptions;
    private PasswordConfiguration passwordConfiguration;

    public PasswordConfiguration getPasswordConfiguration() throws Exception {
        if (configurationOptions == null) {
            configurationOptions = new HashMap<String, ConfigurationOption>();
            for (ConfigurationOption configOption : ClientServiceFactory.getConfigServices().readConfigOptions()) {
                if (CONFIG_TOPIC_PASSWORD.equalsIgnoreCase(configOption.getConfigTopic())) {
                    configurationOptions.put(configOption.getConfigKey(), configOption);
                }
            }
        }
        if (passwordConfiguration == null) {
            passwordConfiguration = BOFactory.createPasswordConfiguration();
            Boolean booleanValue = getConfigurationOptionBoolean(SimConfigManager.PASSWORD_INITIAL_CHANGE);
            if (booleanValue != null) {
                passwordConfiguration.doSetChangeInitialPassword(booleanValue);
            }
            booleanValue = getConfigurationOptionBoolean(SimConfigManager.PASSWORD_ASSIGNMENT_EMAIL_USER);
            if (booleanValue != null) {
                passwordConfiguration.doSetEmailPasswordAssignment(booleanValue);
            }
            booleanValue = getConfigurationOptionBoolean(SimConfigManager.PASSWORD_MUST_CONTAIN_CAPITAL);
            if (booleanValue != null) {
                passwordConfiguration.doSetPasswordMustContainCapital(booleanValue);
            }
            booleanValue = getConfigurationOptionBoolean(SimConfigManager.PASSWORD_MUST_CONTAIN_ALHPA_NUMERIC);
            if (booleanValue != null) {
                passwordConfiguration.doSetPasswordMustContainLetter(booleanValue);
            }
            booleanValue = getConfigurationOptionBoolean(SimConfigManager.PASSWORD_MUST_CONTAIN_NUMERIC);
            if (booleanValue != null) {
                passwordConfiguration.doSetPasswordMustContainNumeric(booleanValue);
            }
            booleanValue = getConfigurationOptionBoolean(SimConfigManager.PASSWORD_MUST_CONTAIN_SPECIAL_CHARACTER);
            if (booleanValue != null) {
                passwordConfiguration.doSetPasswordMustContainSpecial(booleanValue);
            }
            Integer integerValue = getConfigurationOptionInteger(SimConfigManager.PASSWORD_MINIMUM_LENGTH);
            if (integerValue != null) {
                passwordConfiguration.doSetMinimumPasswordLength(integerValue);
            }
            integerValue = getConfigurationOptionInteger(SimConfigManager.PASSWORD_MAXIMUM_LENGTH);
            if (integerValue != null) {
                passwordConfiguration.doSetMaximumPasswordLength(integerValue);
            }
            integerValue = getConfigurationOptionInteger(SimConfigManager.PASSWORD_DAYS_UNTIL_EXPIRES);
            if (integerValue != null) {
                passwordConfiguration.doSetDaysUntilPasswordExpires(integerValue);
            }
            integerValue = getConfigurationOptionInteger(SimConfigManager.PASSWORD_DAYS_BEFORE_EXPIRES_TO_NOTIFY);
            if (integerValue != null) {
                passwordConfiguration.doSetDaysBeforePasswordExpiresToNotify(integerValue);
            }
            integerValue = getConfigurationOptionInteger(SimConfigManager.PASSWORD_NUMBER_OF_PREVIOUS_TO_DISALLOW);
            if (integerValue != null) {
                passwordConfiguration.doSetPreviousPasswordsDisallowCount(integerValue);
            }
            String stringValue = getConfigurationOptionString(SimConfigManager.PASSWORD_SPECIAL_CHARACTERS);
            if (stringValue != null) {
                passwordConfiguration.doSetSpecialCharacters(stringValue.trim());
            }
        }
        return passwordConfiguration;
    }

    public void savePasswordConfiguration() throws Exception {
        if (configurationOptions == null || configurationOptions.isEmpty() || passwordConfiguration == null) {
            return;
        }

        ConfigurationOption configOption = configurationOptions.get(SimConfigManager.PASSWORD_INITIAL_CHANGE);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.isChangeInitialPassword());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_ASSIGNMENT_EMAIL_USER);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.isEmailPasswordAssignment());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_MUST_CONTAIN_CAPITAL);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.isPasswordMustContainCapital());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_MUST_CONTAIN_ALHPA_NUMERIC);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.isPasswordMustContainLetter());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_MUST_CONTAIN_NUMERIC);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.isPasswordMustContainNumeric());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_MUST_CONTAIN_SPECIAL_CHARACTER);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.isPasswordMustContainSpecial());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_MINIMUM_LENGTH);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.getMinimumPasswordLength());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_MAXIMUM_LENGTH);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.getMaximumPasswordLength());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_DAYS_UNTIL_EXPIRES);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.getDaysUntilPasswordExpires());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_DAYS_BEFORE_EXPIRES_TO_NOTIFY);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.getDaysBeforePasswordExpiresToNotify());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_NUMBER_OF_PREVIOUS_TO_DISALLOW);
        if (configOption != null) {
            configOption.setConfigValue(passwordConfiguration.getPreviousPasswordsDisallowCount());
        }
        configOption = configurationOptions.get(SimConfigManager.PASSWORD_SPECIAL_CHARACTERS);
        if (configOption != null) {
            String specialCharacters = StringHelper.trimToNull(passwordConfiguration.getSpecialCharacters());
            configOption.doSetConfigValue(specialCharacters != null ? specialCharacters : StringConstants.EMPTY);
        }

        ClientServiceFactory.getConfigServices().updateConfigOptions(new ArrayList<ConfigurationOption>(configurationOptions.values()));
        SimConfigManager.reloadConfigMap();
    }

    private Boolean getConfigurationOptionBoolean(String key) {
        ConfigurationOption configOption = configurationOptions.get(key);
        if (configOption == null) {
            return null;
        }
        try {
            return (Boolean) configOption.getConfigValue();
        } catch (Throwable t) {
            return null;
        }
    }

    private Integer getConfigurationOptionInteger(String key) {
        ConfigurationOption configOption = configurationOptions.get(key);
        if (configOption == null) {
            return null;
        }
        try {
            return (Integer) configOption.getConfigValue();
        } catch (Throwable t) {
            return null;
        }
    }

    private String getConfigurationOptionString(String key) {
        ConfigurationOption configOption = configurationOptions.get(key);
        if (configOption == null) {
            return null;
        }
        try {
            return (String) configOption.getConfigValue();
        } catch (Throwable t) {
            return null;
        }
    }
}
