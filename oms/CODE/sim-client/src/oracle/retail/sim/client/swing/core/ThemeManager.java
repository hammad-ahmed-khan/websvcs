package oracle.retail.sim.client.swing.core;

import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import oracle.retail.sim.client.application.RPropertyBundle;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.common.configutil.ConfigManager;
import oracle.retail.sim.common.configutil.ResourceManager;
import oracle.retail.sim.common.configutil.SimConfigFiles;

/******************************************************************************************
 * This class is responsible for handling the installment of the look and feel selection
 * as well as the creation of the RCOM default themes (mostly font and color schemes).
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class ThemeManager {

    /******************************************************************************************
     * Returns new ThemeManager object.
     ******************************************************************************************/
    public ThemeManager() {
    }

    /******************************************************************************************
     * Assigns the look and feel based on the style input name (pluggable look and feel name).
     * If the four basics are not discovered, then it assumes that the style name is a fully
     * qualified classpath name to the class to instantiate.
     * <p>
     * @param styleName The name of the look and feel to install.
     * @throws Exception Thrown if the method fails to install the look and feel.
     ******************************************************************************************/
    public void setLookAndFeel(LookAndFeel theme) throws Exception {
        try {
            UIManager.setLookAndFeel(theme);
        } catch (Exception exception) {
            throw new Exception(Translator.getMessage(UIMessageText.LOOK_AND_FEEL_ERROR.getText(), theme.getName()));
        }
    }

    /******************************************************************************************
     * Assigns the color scheme based on the base filename of a series of properties files.
     * Each property file is loaded and the key are placed in the UIManager as a name and
     * the value in the property file is placed in the UIManager as a property.
     * <p>
     * @param filename The name of the color theme properties file.
     * @throws Exception Thrown if the method fails to install the theme.
     ******************************************************************************************/
    //    public void setColorTheme(String filename) throws Exception {
    //    	RPropertyBundle themeBundle = new RPropertyBundle(filename);
    //
    //    	String[] keyArray = themeBundle.getKeys();
    //
    //    	for (int i = 0; i < keyArray.length; i++) {
    //   			 assignColor(keyArray[i], themeBundle.getStringArray(keyArray[i]));
    //		}
    //	}
    /******************************************************************************************
     * Assigns the font scheme based on the base filename of a series of properties files.
     * Each property file is loaded and the key are placed in the UIManager as a name and
     * the value in the property file is placed in the UIManager as a property.
     * <p>
     * @param filename The name of the font theme properties file.
     * @throws Exception Thrown if the method fails to install the theme.
     ******************************************************************************************/
    //    public void setFontTheme(String filename) throws Exception {
    //    	RPropertyBundle themeBundle = new RPropertyBundle(filename);
    //
    //    	String[] keyArray = themeBundle.getKeys();
    //
    //    	for (int i = 0; i < keyArray.length; i++) {
    //   			 assignFont(keyArray[i], themeBundle.getStringArray(keyArray[i]));
    //		}
    //	}
    /******************************************************************************************
     * Assigns the margin scheme based on the base filename of a series of properties files.
     * Each property file is loaded and the key are placed in the UIManager as a name and
     * the value in the property file is placed in the UIManager as a property.
     * <p>
     * If a key in this file ends with the character string "Dimension", it creates a Dimension
     * value instead of a Insets value.
     * <p>
     * @param filename The name of the margin theme properties file.
     * @throws Exception Thrown if the method fails to install the theme.
     ******************************************************************************************/
    public void setMarginTheme(String filename) throws Exception {
        RPropertyBundle themeBundle = new RPropertyBundle(filename);

        String[] keyArray = themeBundle.getKeys();

        for (String element : keyArray) {
            if (element.endsWith("Dimension")) {
                assignDimension(element, themeBundle.getStringArray(element));
            } else {
                assignInsets(element, themeBundle.getStringArray(element));
            }
        }
    }

    /******************************************************************************************
     * Assigns the icon scheme based on the base filename of a series of properties files.
     * Each property file is loaded and the key are placed in the UIManager as a name and
     * the value in the property file is placed in the UIManager as a property. An icon theme
     * file contains the icon identifier as a key and the filename of the icon as a value.
     * <p>
     * @param filename The name of the icon theme properties file.
     * @throws Exception Thrown if the method fails to install the theme.
     ******************************************************************************************/
    public void setIconTheme(String filename) throws Exception {
        RPropertyBundle themeBundle = new RPropertyBundle(filename);

        String[] keyArray = themeBundle.getKeys();

        for (String element : keyArray) {
            String iconName = themeBundle.getString(element);
            ImageIcon icon = ResourceManager.getImageIcon(iconName);
            if (icon == null) {
                UILog.info(getClass(), UIMessageText.UNABLE_TO_LOAD_ICON, iconName);
            }
            UIManager.put(element, icon);
        }
    }

    /******************************************************************************************
     * Assigns the widget scheme based on the base filename of a series of properties files.
     * Each property file is loaded and the key are placed in the UIManager as a name and
     * the value in the property file is placed in the UIManager as a property. A widget theme
     * file contains the widget property name as a key and the widget property value.
     * <p>
     * @param filename The name of the widget theme properties file.
     * @throws RPropertyBundleException Thrown if the method fails to install the theme.
     ******************************************************************************************/
    public void setWidgetTheme(String filename) throws Exception {
        RPropertyBundle themeBundle = new RPropertyBundle(filename);

        String[] keyArray = themeBundle.getKeys();

        for (String element : keyArray) {
            UIManager.put(element, themeBundle.getString(element));
        }

        ConfigManager manager = new ConfigManager(SimConfigFiles.DATE_CONFIG);
        for (String key : manager.getKeys()) {
            UIManager.put(key, manager.getString(key));
        }
    }

    /******************************************************************************************
     * Assigns a specific font to a particular resource in the UIManager. The font information
     * is captured in an array in the properties. Location 0 = Font Name. Location 1 = Font Style.
     * Location 2 = Font Size.
     * <p>
     * @param resourceName The name of the resource to assign the font to.
     * @param fontArray An array containing the font information.
     ******************************************************************************************/
    //	private void assignFont(String resourceName, String[] fontArray) {
    //		try {
    //			String fontName = new String(fontArray[0]);
    //			int fontType = Font.PLAIN;
    //			int fontSize = Integer.parseInt(fontArray[2]);
    //
    //			if (fontArray[1].equalsIgnoreCase("bold")) {
    //				 fontType = Font.BOLD;
    //			} else if (fontArray[1].equalsIgnoreCase("italic")) {
    //			     fontType = Font.ITALIC;
    //			} else if (fontArray[1].equalsIgnoreCase("bold-italic")) {
    //				 fontType = Font.BOLD + Font.ITALIC;
    //			}
    //
    //			UIManager.put(resourceName, new Font(fontName, fontType, fontSize));
    //		} catch (Exception exception) {
    //			UILog.error(getClass(), "Failed to load font resource for " + resourceName, exception);
    //		}
    //	}
    /******************************************************************************************
     * Assigns a specific color to a particular resource in the UIManager. The color is captured
     * captured in an array from the property file. Location 0 = RGB red value. Location 1 = RGB
     * green value. Location 2 = RGB blue value.
     * <p>
     * @param resourceName The name of the resource to assign the color to.
     * @param colorArray An array containing the color information.
     ******************************************************************************************/
    //	private void assignColor(String resourceName, String[] colorArray) {
    //		try {
    //			int rvalue = Integer.parseInt(colorArray[0]);
    //			int gvalue = Integer.parseInt(colorArray[1]);
    //			int bvalue = Integer.parseInt(colorArray[2]);
    //
    //			UIManager.put(resourceName, new Color(rvalue, gvalue, bvalue));
    //		} catch (Exception exception) {
    //			UILog.error(getClass(), "Failed to load color resource for " + resourceName, exception);
    //		}
    //	}
    /******************************************************************************************
     * Assigns a specific margin to a particular resource in the UIManager. The margin
     * information is captured in an array from the properties file. Location 0 = top.
     * Location 1 = left. Location 2 = bottom. Location 3 = right.
     * <p>
     * @param resourceName The name of the resource to assign the margin to.
     * @param insertArray An array containing the insert information.
     ******************************************************************************************/
    private void assignInsets(String resourceName, String[] insetArray) {
        try {
            int top = Integer.parseInt(insetArray[0]);
            int left = Integer.parseInt(insetArray[1]);
            int bottom = Integer.parseInt(insetArray[2]);
            int right = Integer.parseInt(insetArray[3]);

            UIManager.put(resourceName, new Insets(top, left, bottom, right));
        } catch (Exception exception) {
            UILog.error(getClass(), UIMessageText.UNABLE_TO_LOAD_INSET, resourceName, exception);
        }
    }

    /******************************************************************************************
     * Assigns a specific dimension to a particular resource in the UIManager. The margin
     * information is captured in an array in the properties file. Location 0 = horizontal.
     * Location 1 = vertical.
     * <p>
     * @param resourceName The name of the resource to assign the font to.
     * @param insertArray An array containing the dimension information.
     ******************************************************************************************/
    private void assignDimension(String resourceName, String[] dimensionArray) {
        try {
            int x = Integer.parseInt(dimensionArray[0]);
            int y = Integer.parseInt(dimensionArray[1]);

            UIManager.put(resourceName, new Dimension(x, y));
        } catch (Exception exception) {
            UILog.error(getClass(),  UIMessageText.UNABLE_TO_LOAD_DIMENSION, resourceName, exception);
        }
    }
}
