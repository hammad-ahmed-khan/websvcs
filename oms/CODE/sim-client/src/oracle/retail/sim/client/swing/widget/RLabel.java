package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class sublcasses the standard JLabel class in the Swing package to provide custom
 * functionality for the label. This should always be used in place of the JLabel.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RLabel extends JLabel {
    private static final long serialVersionUID = 7833349765747765049L;

    protected static final String RIGHT_TEXT = "right";
    protected static final String CENTER_TEXT = "center";
    protected static final String LEFT_TEXT = "left";

    protected Color enabledForegroundColor;
    protected Color disabledForegroundColor;

    protected boolean isControllingColor;

    /******************************************************************************************
     * Returns new RLabel object.
     *****************************************************************************************/
    public RLabel() {
        initialize();
    }

    /******************************************************************************************
     * Returns new RLabel object with display text assigned.
     * <p>
     * @param text The display text to assign to the label.
    /*****************************************************************************************/
    public RLabel(String text) {
        initialize();
        setText(text);
    }

    /******************************************************************************************
     * Initializes properties of RLabel from the UIManager (namely suffix to display after
     * label and the justification).
     *****************************************************************************************/
    protected void initialize() {
        initializeColors();
        initializeJustification();
        setFocusable(false);
    }

    /******************************************************************************************
     * Initialize colors.
     *****************************************************************************************/
    protected void initializeColors() {
        setBackground(UIManager.getColor(UIThemeName.LABEL_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.LABEL_FOREGROUND));
    }

    /******************************************************************************************
     * Resets label justification to the UIManager property.
     *****************************************************************************************/
    protected void initializeJustification() {
        String justification = (String) UIManager.get(UIThemeName.LABEL_JUSTIFICATION);

        if (justification != null) {
            if (justification.equalsIgnoreCase(RIGHT_TEXT)) {
                setHorizontalAlignment(RIGHT);
            } else if (justification.equalsIgnoreCase(CENTER_TEXT)) {
                setHorizontalAlignment(CENTER);
            } else if (justification.equalsIgnoreCase(LEFT_TEXT)) {
                setHorizontalAlignment(LEFT);
            }
        }
    }

    /******************************************************************************************
     * Locks the size of the label to the width using the preferred height.
     * <p>
     *@param width The width of the label in pixels.
     *****************************************************************************************/
    public void setLockedSize(int width) {
        setLockedSize(width, getPreferredSize().height);
    }

    /******************************************************************************************
     * Locks the size of the label to the width using the preferred height.
     * <p>
     *@param width The width of the label in pixels.
     *@param height The height of the label in pixels.
     ******************************************************************************************/
    public void setLockedSize(int width, int height) {
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
    }

    /******************************************************************************************
     * Sets the minimum and preferred width of the label.
     * <p>
     *@param width The width of the label in pixels.
     *****************************************************************************************/
    public void setMinimumWidth(int width) {
        int height = UIManager.getInt(UIThemeName.LABEL_HEIGHT);
        if (height < 1) {
            height = getPreferredSize().height;
        }
        if (height < 1) {
            height = getFontMetrics(getFont()).getHeight();
        }
        setMinimumSize(width, height);
    }

    /******************************************************************************************
     * Sets the minimum and preferred height of the label.
     * <p>
     *@param height The height of the label in pixels.
     *****************************************************************************************/
    public void setMinimumHeight(int height) {
        int width = 0;

        if (getPreferredSize().width > width) {
            width = getPreferredSize().width;
        } else if (getMinimumSize().width > width) {
            width = getMinimumSize().width;
        }
        setMinimumSize(width, height);
    }

    /******************************************************************************************
     * Sets the minimum and preferred width and height of the button.
     * <p>
     *@param width The width of the button in pixels.
     *@param height The height of the button in pixels.
     *****************************************************************************************/
    public void setMinimumSize(int width, int height) {
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
    }

    /******************************************************************************************
     * Returns the original text string assigned to the label (translated of course).
     * <p>
     * @return The original entered text.
     ******************************************************************************************/
    public String getDisplayText() {
        String text = getText();
        if (text.equals(StringConstants.NULL)) {
            return StringConstants.EMPTY;
        }
        return text;
    }

    /******************************************************************************************
     * Sets the text to display within the label. Overrides the superclass method to supply
     * language translation to the text string.
     * <p>
     *@param text The text to display within the label.
     ******************************************************************************************/
    public void setText(String text) {
        if (StringUtility.isNullOrEmpty(text)) {
            super.setText(StringConstants.EMPTY);
        } else {
            super.setText(Translator.getText(text));
        }
    }

    /******************************************************************************************
     * Sets the text to display.
     * <p>
     * @param text The text to display with the label.
     * @param translate True if the text should be translated, false if not.
     ******************************************************************************************/
    public void setText(String text, boolean translate) {
        if (translate) {
            setText(text);
        } else if (StringUtility.isNullOrEmpty(text)) {
            super.setText(StringConstants.EMPTY);
        } else {
            super.setText(text);
        }
    }

    /******************************************************************************************
     * Clears the text of the label.
     ******************************************************************************************/
    public void clear() {
        setText(StringConstants.EMPTY);
    }

    /******************************************************************************************
     * Attempts to set the icon within the label. If it fails to set the icon, it will set
     * the text instead.
     * <p>
     *@param icon The icon to be placed in the label.
     *@param text The replacement text if the icon could not be found.
     *****************************************************************************************/
    public void setIcon(Icon icon, String text) {
        try {
            setIcon(icon);

            int width = icon.getIconWidth();
            int height = icon.getIconHeight();

            Dimension dimension = new Dimension(width, height);
            setMinimumSize(dimension);
            setPreferredSize(dimension);
            setMaximumSize(dimension);
        } catch (Throwable exception) {
            setText(text);
        }
    }

    /******************************************************************************************
     * Sets the font style for the label.
     * <p>
     * @param style The style to assign to the font.
    /******************************************************************************************/
    public void setFontStyle(int style) {
        setFont(getFont().deriveFont(style));
    }

    /******************************************************************************************
     * Sets the font style and size for the label.
     * <p>
     * @param style The style to assign to the font.
     * @param size The size to assign to the font.
     ******************************************************************************************/
    public void setFontStyle(int style, float size) {
        setFont(getFont().deriveFont(style, size));
    }

    /******************************************************************************************
     * Sets the font color, style and size for the label.
     * <p>
     * @param color The color to assign to the foreground of the label.
     * @param style The style to assign to the font.
     * @param size The size to assign to the font.
     ******************************************************************************************/
    public void setFontStyle(Color color, int style, float size) {
        setFont(getFont().deriveFont(style, size));
        setForeground(color);
    }

    /******************************************************************************************
     *@see setForeground() in JLabel.
     *****************************************************************************************/
    public void setForeground(Color color) {
        if (!isControllingColor) {
            enabledForegroundColor = color;
            if (UIManager.getBoolean(UIThemeName.SYSTEM_DISABLED_LABEL_ACTIVE)) {
                disabledForegroundColor = UIManager.getColor(UIThemeName.LABEL_DISABLED_FOREGROUND);
            } else {
                disabledForegroundColor = color;
            }
        }
        super.setForeground(color);
    }

    /******************************************************************************************
     *@see setEnabled() in JLabel.
     *****************************************************************************************/
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateColorState();
    }

    /******************************************************************************************
     * RLabels are not focusable.
     *****************************************************************************************/
    public boolean isFocusable() {
        return false;
    }

    /******************************************************************************************
     *@see setEnabled() in JLabel.
     *****************************************************************************************/
    protected void updateColorState() {
        isControllingColor = true;
        if (isEnabled()) {
            super.setForeground(enabledForegroundColor);
        } else {
            super.setForeground(disabledForegroundColor);
        }
        isControllingColor = false;
    }
}
