package oracle.retail.sim.client.screen.theme;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;
import oracle.retail.sim.client.application.RepositoryManager;
import oracle.retail.sim.client.application.ThemeUtility;
import oracle.retail.sim.client.core.ClientWrapperFactory;
import oracle.retail.sim.client.core.SimScreenModel;
import oracle.retail.sim.client.util.SimClientStateKey;
import oracle.retail.sim.common.theme.CustomFont;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Font Detail Model
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class FontDetailModel extends SimScreenModel {
    private CustomThemeWrapper themeWrapper;
    private UIDefaults lookAndFeelValues;
    private String[] themeKeys;

    public void loadTheme() {
        themeWrapper = (CustomThemeWrapper) RepositoryManager.getStateObject(SimClientStateKey.SELECTED_THEME);
    }

    public String getThemeDescription() {
        return themeWrapper.getDescription();
    }

    public List<FontWrapper> getFonts() {
        Map<String, Object> customDefaults = getCustomDefaults();
        UIDefaults lookAndFeelDefaults = getLookAndFeelDefaults();
        String[] fontKeys = getThemeKeys(lookAndFeelDefaults);

        List<FontWrapper> fontWrappers = new ArrayList<>();
        for (String fontKey : fontKeys) {
            Object value = lookAndFeelDefaults.get(fontKey);
            Object customValue = customDefaults.get(fontKey);

            if (customValue instanceof CustomFont) {
                fontWrappers.add(ClientWrapperFactory.createFontWrapper(fontKey, (CustomFont) customValue));
            } else if (value instanceof FontUIResource) {
                fontWrappers.add(ClientWrapperFactory.createFontWrapper(fontKey, (FontUIResource) value));
            } else if (value instanceof Font) {
                fontWrappers.add(ClientWrapperFactory.createFontWrapper(fontKey, new FontUIResource((Font) value)));
            }
        }
        return fontWrappers;
    }

    public void resetFont(FontWrapper wrapper) {
        wrapper.setFont(new FontUIResource(getLookAndFeelDefaults().getFont(wrapper.getName())));
        wrapper.setCustom(false);
    }

    public void applyToAll(FontWrapper wrapper, List<FontWrapper> fontWrappers) {
        for (FontWrapper fontWrapper : fontWrappers) {
            FontUIResource font = fontWrapper.getFont();
            fontWrapper.setFont(new FontUIResource(wrapper.getFamily(), font.getStyle(), font.getSize()));
            fontWrapper.setCustom(true);
        }
    }

    public void saveFonts(List<FontWrapper> fontWrappers) throws Exception {
        List<CustomFont> customFonts = new ArrayList<>();
        for (FontWrapper wrapper : fontWrappers) {
            if (wrapper.isCustom()) {
                customFonts.add(wrapper.getCustomFont());
            }
        }
        ClientServiceFactory.getCustomThemeServices().saveFonts(themeWrapper.getCustomTheme().getId(), customFonts);
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
