package oracle.retail.sim.client.screen.theme;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.plaf.IconUIResource;
import oracle.retail.sim.common.theme.CustomIcon;

/********************************************************************************************************
 * Wrapper for a Icon to assist display in the table.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class IconWrapper {

    private String key;
    private IconUIResource icon;
    private String iconPath;
    private boolean custom;

    public IconWrapper(String key, IconUIResource icon) {
        this.key = key;
        this.icon = icon;
        custom = false;
    }

    public IconWrapper(String key, CustomIcon icon) {
        this.key = key;
        iconPath = icon.getIconPath();
        custom = true;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public void setCustom(boolean isCustom) {
        custom = isCustom;
    }

    public String getKey() {
        return key;
    }

    public String getIconPath() {
        if (iconPath != null) {
            return iconPath;
        }
        Icon tempIcon = UIManager.getIcon(key);
        if (tempIcon instanceof ImageIcon) {
            return ((ImageIcon) tempIcon).getDescription();
        }
        return "Oracle Default";
    }

    public IconUIResource getIcon() {
        return icon;
    }

    public Boolean isCustom() {
        return custom;
    }

    public CustomIcon getCustomIcon() {
        return new CustomIcon(key, iconPath);
    }
}
