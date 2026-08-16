package oracle.retail.sim.client.screen.theme;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.application.ThemeUtility;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.theme.CustomColor;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Font Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ColorDetailModel extends SimScreenModel {
    private CustomThemeWrapper themeWrapper;
    private UIDefaults lookAndFeelValues;
    private String[] themeKeys;

    public void loadTheme() {
        themeWrapper = (CustomThemeWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_THEME);
    }

    public String getThemeDescription() {
        return themeWrapper.getDescription();
    }

    public List<ColorWrapper> getColors() {
        Map<String, Object> customDefaults = getCustomDefaults();
        UIDefaults lookAndFeelDefaults = getLookAndFeelDefaults();
        String[] colorKeys = getThemeKeys(lookAndFeelDefaults);
        List<ColorWrapper> colorWrappers = new ArrayList<>();
        for (String colorKey : colorKeys) {
            Object value = lookAndFeelDefaults.get(colorKey);
            Object customValue = customDefaults.get(colorKey);

            if (customValue instanceof CustomColor) {
                colorWrappers.add(new ColorWrapper(colorKey, (CustomColor) customValue));
            } else if (value instanceof ColorUIResource) {
                colorWrappers.add(new ColorWrapper(colorKey, (ColorUIResource) value));
            } else if (value instanceof Color) {
                colorWrappers.add(new ColorWrapper(colorKey, new ColorUIResource((Color) value)));
            }
        }
        return colorWrappers;
    }

    public void resetColor(ColorWrapper wrapper) {
        wrapper.setColor(new ColorUIResource(getLookAndFeelDefaults().getColor(wrapper.getName())));
        wrapper.setCustom(false);
    }

    public void saveColors(List<ColorWrapper> colorWrappers) throws Exception {
        List<CustomColor> customColors = new ArrayList<>();
        for (ColorWrapper wrapper : colorWrappers) {
            if (wrapper.isCustom()) {
                customColors.add(wrapper.getCustomColor());
            }
        }
        ClientServiceFactory.getCustomThemeServices().saveColors(themeWrapper.getCustomTheme().getId(), customColors);
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
