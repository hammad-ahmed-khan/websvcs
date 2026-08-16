package oracle.retail.sim.client.screen.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.config.ConfigurationOption;
import oracle.retail.sim.common.configutil.StoreConfigKeys;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Store Configuration Default Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StoreConfigDefaultModel extends SimScreenModel {
    private List<ConfigurationOption> configurationOptions;

    public List<String> findDefaultStoreConfigOptionTopics() throws Exception {
        return ClientServiceFactory.getConfigServices().getDefaultStoreAdminTopics();
    }

    public List<ConfigurationOption> findDefaultStoreConfigurationOptions() throws Exception {
        if (configurationOptions == null) {
            configurationOptions = ClientServiceFactory.getConfigServices().readDefaultStoreConfigOptions();
        }
        return configurationOptions;
    }

    public List<ConfigurationOption> findAvailableConfigurationOptions(String topic) throws Exception {
        List<ConfigurationOption> options = findDefaultStoreConfigurationOptions();
        if (StringHelper.isNullOrEmpty(topic)) {
            return options;
        }
        List<ConfigurationOption> availableOptions = new ArrayList<ConfigurationOption>();
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

        Map<String, Object> options = getConfigurationOptionsMap();
        validateConfigurationOptions(options);

        ClientServiceFactory.getConfigServices().updateDefaultStoreConfigOptions(configurationOptions);
    }

    private void validateConfigurationOptions(Map<String, Object> options) throws BusinessException {
        Double doubleValue = (Double) options.get(StoreConfigKeys.REPLENISHMENT_END_OF_DAY_MAX_FILL_PCT);
        if (doubleValue == null || doubleValue < 0d) {
            throw buildNumericException(CommonMessageText.ADMIN_EOD_MAX_FILL_ERROR, 0d);
        }
        doubleValue = (Double) options.get(StoreConfigKeys.REPLENISHMENT_ITEM_OUT_OF_STOCK_PCT);
        if (doubleValue == null || doubleValue < 0d) {
            throw buildNumericException(CommonMessageText.ADMIN_OUT_OF_STOCK_PERCENT_ERROR, 0d);
        }
        doubleValue = (Double) options.get(StoreConfigKeys.REPLENISHMENT_WITHIN_DAY_MAX_FILL_PCT);
        if (doubleValue == null || doubleValue < 0d) {
            throw buildNumericException(CommonMessageText.ADMIN_WITHIN_DAY_MAX_FILL_ERROR, 0d);
        }
        Integer integerValue = (Integer) options.get(StoreConfigKeys.REPLENISHMENT_ITEM_OUT_OF_STOCK_STANDARD_UOM);
        if (integerValue == null || integerValue < 0) {
            throw buildNumericException(CommonMessageText.ADMIN_OUT_OF_STOCK_UOM_ERROR, 0);
        }
    }

    private Map<String, Object> getConfigurationOptionsMap() {
        Map<String, Object> options = new HashMap<String, Object>(configurationOptions.size());
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
