package oracle.retail.sim.client.screen.theme;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.IconUIResource;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.application.ThemeUtility;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.theme.CustomIcon;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Font Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class IconDetailModel extends SimScreenModel {
    private CustomThemeWrapper themeWrapper;
    private UIDefaults lookAndFeelValues;
    private String[] themeKeys;

    public void loadTheme() {
        themeWrapper = (CustomThemeWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_THEME);
    }

    public String getThemeDescription() {
        return themeWrapper.getDescription();
    }

    public List<IconWrapper> getIcons() {
        Map<String, Object> customDefaults = getCustomDefaults();
        UIDefaults lookAndFeelDefaults = getLookAndFeelDefaults();
        String[] iconKeys = getThemeKeys(lookAndFeelDefaults);
        List<IconWrapper> iconWrappers = new ArrayList<>();
        for (String iconKey : iconKeys) {
            Object value = lookAndFeelDefaults.get(iconKey);
            Object customValue = customDefaults.get(iconKey);

            if (customValue instanceof CustomIcon) {
                iconWrappers.add(ClientWrapperFactory.createIconWrapper(iconKey, (CustomIcon) customValue));
            } else if (value instanceof IconUIResource) {
                iconWrappers.add(ClientWrapperFactory.createIconWrapper(iconKey, (IconUIResource) value));
            } else if (value instanceof Icon) {
                iconWrappers.add(ClientWrapperFactory.createIconWrapper(iconKey, new IconUIResource((Icon) value)));
            }
        }
        return iconWrappers;
    }

    public void resetIcon(IconWrapper wrapper) {
        wrapper.setCustom(false);
        wrapper.setIconPath(null);
    }

    public void saveIcons(List<IconWrapper> iconWrappers) throws Exception {
        List<CustomIcon> customIcons = new ArrayList<>();
        for (IconWrapper wrapper : iconWrappers) {
            if (wrapper.isCustom()) {
                customIcons.add(wrapper.getCustomIcon());
            }
        }
        ClientServiceFactory.getCustomThemeServices().saveIcons(themeWrapper.getCustomTheme().getId(), customIcons);
    }

    private String[] getThemeKeys(UIDefaults uiDefaults) {
        if (themeKeys == null) {
            themeKeys = new String[uiDefaults.size()];
            int counter = 0;
            Enumeration keyEnumerator = uiDefaults.keys();
            while (keyEnumerator.hasMoreElements()) {
                themeKeys[counter++] = keyEnumerator.nextElement().toString();
            }
            Arrays.sort(themeKeys);
        }
        return themeKeys;
    }

    private Map<String, Object> getCustomDefaults() {
        return (Map<String, Object>) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_THEME_DETAIL);
    }

    private UIDefaults getLookAndFeelDefaults() {
        if (lookAndFeelValues == null) {
            LookAndFeel lookAndFeel = (LookAndFeel) ThemeUtility.getLookAndFeel(themeWrapper.getCustomTheme());
            lookAndFeelValues = lookAndFeel.getDefaults();
        }
        return lookAndFeelValues;
    }
}
