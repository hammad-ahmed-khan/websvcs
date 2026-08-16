package oracle.retail.sim.client.screen.theme;

import oracle.retail.sim.client.swing.plaf.custom.CustomLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.CustomSwanLookAndFeel;
import oracle.retail.sim.common.theme.CustomTheme;

/********************************************************************************************************
 * Custom Theme Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomThemeWrapper {
    private CustomTheme customTheme;
    private CustomLookAndFeel lookAndFeel;

    public CustomThemeWrapper(CustomTheme customTheme) {
        this.customTheme = customTheme;
    }

    public CustomTheme getCustomTheme() {
        return customTheme;
    }

    public String getName() {
        return customTheme.getName();
    }

    public String getDescription() {
        return customTheme.getDescription();
    }

    public boolean isActive() {
        return customTheme.isActive();
    }

    public String getLookAndFeelName() {
        return getLookAndFeel().getDescription();
    }

    public void setLookAndFeel(CustomLookAndFeel lookAndFeel) {
        customTheme.doSetLookAndFeel(lookAndFeel.getClass().getName());
    }

    public CustomLookAndFeel getLookAndFeel() {
        if (lookAndFeel == null) {
            try {
                lookAndFeel = (CustomLookAndFeel) Class.forName(customTheme.getLookAndFeel()).newInstance();
            } catch (Throwable et) {
                lookAndFeel = new CustomSwanLookAndFeel();
            }
        }
        return lookAndFeel;
    }
}
