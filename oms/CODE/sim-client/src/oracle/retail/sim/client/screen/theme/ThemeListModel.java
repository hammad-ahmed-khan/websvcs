package oracle.retail.sim.client.screen.theme;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.theme.CustomTheme;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Theme List Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ThemeListModel extends SimScreenModel {
    public List<CustomThemeWrapper> findThemes() throws Exception {
        List<CustomThemeWrapper> wrappers = new ArrayList<>();
        for (CustomTheme theme : ClientServiceFactory.getCustomThemeServices().findAllCustomThemes()) {
            wrappers.add(ClientWrapperFactory.createCustomThemeWrapper(theme));
        }
        return wrappers;
    }

    public void storeTheme(CustomThemeWrapper theme) throws Exception {
        Map<String, Object> themeDetails = ClientServiceFactory.getCustomThemeServices().findThemeConfiguration(theme.getCustomTheme().getId());
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_THEME, theme);
        RepositoryManager.addStateObject(SimClientStateKey.SELECTED_THEME_DETAIL, themeDetails);
    }
}
