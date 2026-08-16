package oracle.retail.sim.client.application;

import java.awt.Container;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import oracle.retail.sim.client.swing.frame.RFrame;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.plaf.custom.CustomLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.CustomMetalLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.CustomMotifLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.CustomOceanLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.CustomSwanLookAndFeel;
import oracle.retail.sim.client.swing.plaf.custom.CustomWindowsLookAndFeel;
import oracle.retail.sim.common.configutil.ResourceManager;
import oracle.retail.sim.common.theme.CustomColor;
import oracle.retail.sim.common.theme.CustomFont;
import oracle.retail.sim.common.theme.CustomIcon;
import oracle.retail.sim.common.theme.CustomTheme;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * This class handles a few simple utility methods for performing theme and look and feel functions.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ThemeUtility {
    private ThemeUtility() {
    }

    /****************************************************************************************************
     * Retrieves a list of the available look and feels (parent to themes).
     ***************************************************************************************************/
    public static List<LookAndFeel> getLookAndFeelList() {
        List<LookAndFeel> list = new ArrayList<>(5);
        list.add(new CustomSwanLookAndFeel());
        list.add(new CustomMetalLookAndFeel());
        list.add(new CustomMotifLookAndFeel());
        list.add(new CustomWindowsLookAndFeel());
        list.add(new CustomOceanLookAndFeel());
        return list;
    }

    /****************************************************************************************************
     * Retrieves the system default look and feel.
     ***************************************************************************************************/
    public static LookAndFeel getDefaultLookAndFeel() {
        return new CustomSwanLookAndFeel();
    }

    /****************************************************************************************************
     * Applies a custom theme / look and feel to the application. It builds up the appropriate look and
     * feel, update the UIManager and then updates the component tree.
     * <p>
     * @param theme The custom theme to apply.
     ***************************************************************************************************/
    public static void applyCustomTheme(CustomTheme theme) {
        try {
            CustomLookAndFeel lookAndFeel = getLookAndFeel(theme);
            lookAndFeel.setCustomDefaults(getUIDefaults(theme));
            UIManager.setLookAndFeel((LookAndFeel) lookAndFeel);
            RFrame frame = Application.getFrame();
            if (frame != null) {
                SwingUtilities.updateComponentTreeUI(frame);
            }
        } catch (Throwable ex) {
            UILog.error(ThemeUtility.class, ex);
        }
    }

    /****************************************************************************************************
     * Applies a custom theme / look and feel to the application. It builds up the appropriate look and
     * feel, update the UIManager and then updates the component tree.
     * <p>
     * @param theme The custom theme to apply.
     * @param container A container to update the component tree on. It may be null.
     ***************************************************************************************************/
    public static void applyCustomTheme(CustomTheme theme, Container container) {
        try {
            CustomLookAndFeel lookAndFeel = getLookAndFeel(theme);
            lookAndFeel.setCustomDefaults(getUIDefaults(theme));
            UIManager.setLookAndFeel((LookAndFeel) lookAndFeel);
            RFrame frame = Application.getFrame();
            if (frame != null) {
                SwingUtilities.updateComponentTreeUI(frame);
            }
            if (container != null) {
                SwingUtilities.updateComponentTreeUI(container);
            }
        } catch (Throwable ex) {
            UILog.error(ThemeUtility.class, ex);
        }
    }

    /****************************************************************************************************
     * Instantiates the actual Look and Feel class for the custom theme. Defaults back to Swan if an
     * error occurs.
     * <p>
     * @param theme The custom theme to apply.
     * @param container A container to update the component tree on. It may be null.
     ***************************************************************************************************/
    public static CustomLookAndFeel getLookAndFeel(CustomTheme theme) {
        try {
            return (CustomLookAndFeel) Class.forName(theme.getLookAndFeel()).newInstance();
        } catch (Throwable t) {
            return new CustomSwanLookAndFeel();
        }
    }

    /****************************************************************************************************
     * Instantiates the actual Look and Feel class for the custom theme. Defaults back to Swan if an
     * error occurs.
     * <p>
     * @param theme The custom theme to apply.
     * @param container A container to update the component tree on. It may be null.
     ***************************************************************************************************/
    public static Map<String, Object> getUIDefaults(CustomTheme theme) throws Exception {
        Map<String, Object> finalDefaults = new HashMap<>();
        Map<String, Object> themeDetailMap = ClientServiceFactory.getCustomThemeServices().findThemeConfiguration(theme.getId());
        for (Map.Entry<String, Object> entry : themeDetailMap.entrySet()) {
            String tempName = entry.getKey();
            Object tempValue = entry.getValue();
            if (tempValue instanceof CustomFont) {
                finalDefaults.put(tempName, convertFont((CustomFont) tempValue));
            } else if (tempValue instanceof CustomColor) {
                finalDefaults.put(tempName, convertColor((CustomColor) tempValue));
            } else if (tempValue instanceof CustomIcon) {
                finalDefaults.put(tempName, convertIcon((CustomIcon) tempValue));
            } else {
                finalDefaults.put(tempName, tempValue);
            }
        }
        return finalDefaults;
    }

    /****************************************************************************************************
     * Converts custom color into a Color UI resource.
     ***************************************************************************************************/
    private static ColorUIResource convertColor(CustomColor color) {
        return new ColorUIResource(color.getRed(), color.getGreen(), color.getBlue());
    }

    /****************************************************************************************************
     * Converts custom font into a Font UI resource.
     ***************************************************************************************************/
    private static FontUIResource convertFont(CustomFont font) {
        return new FontUIResource(font.getFamily(), font.getStyle(), font.getSize());
    }

    /****************************************************************************************************
     * Converts custom icon into a Icon UI resource.
     ***************************************************************************************************/
    private static ImageIcon convertIcon(CustomIcon icon) {
        return ResourceManager.getImageIcon(icon.getIconPath());
    }
}
