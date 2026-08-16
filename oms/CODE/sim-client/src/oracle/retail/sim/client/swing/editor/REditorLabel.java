package oracle.retail.sim.client.swing.editor;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class subclasses JLabel and adds to its functionality the ability to have a required indicator
 * symbol in front of the label and a suffix behind the label. This is the label that is used inside ALL
 * Editors.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class REditorLabel extends JLabel {

    protected static final String RIGHT_TEXT = "right";
    protected static final String CENTER_TEXT = "center";
    protected static final String LEFT_TEXT = "left";

    protected int titleAlignment = EditorConstants.LEFT;

    protected Color enabledForegroundColor;
    protected Color disabledForegroundColor;
    protected boolean isControllingColor;
    protected boolean isRequired;

    protected String requiredSymbol = StringConstants.EMPTY;

    /****************************************************************************************************
     * Returns new REditorLabel object.
     ***************************************************************************************************/
    protected REditorLabel() {
        initialize();
    }

    /****************************************************************************************************
     * Returns new REditorLabel object with display text assigned.
     * <p>
     * @param text The display text to assign to the label. /
     ***************************************************************************************************/
    protected REditorLabel(String text) {
        initialize();
        setText(text);
    }

    /****************************************************************************************************
     * Initializes properties of REditorLabel from the UIManager (namely suffix to display after label
     * and the justification).
     ***************************************************************************************************/
    protected void initialize() {
        setBackground(UIManager.getColor(UIThemeName.LABEL_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.LABEL_FOREGROUND));
        requiredSymbol = UIManager.getString(UIThemeName.EDITOR_LABEL_REQUIRED_SYMBOL);
        initializeJustification();
        setOpaque(false);
        setFocusable(false);
    }

    /****************************************************************************************************
     * Resets label justification to the UIManager property.
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Assigns whether or not the label represents required information.
     * <p>
     * @param required True if the label represents required information, false if not.
     ***************************************************************************************************/
    public void setRequired(boolean required) {
        isRequired = required;
        setText(getOriginalText(), false);
    }

    /****************************************************************************************************
     * Retrieves whether or not the label represents required information.
     * <p>
     * @return True if the label represents required information, false if not.
     ***************************************************************************************************/
    public boolean isRequired() {
        return isRequired;
    }

    /****************************************************************************************************
     * Assigns the alignment of the title to the remainder of the editor. Valid alignments are
     * EditorConstants.LEFT, EditorConstants.RIGHT, EditorConstants.TOP, EditorConstants.BOTTOM.
     * <p>
     * @param alignment The alignment to assign.
     ***************************************************************************************************/
    public void setTitleAlignment(int alignment) {
        if (alignment < EditorConstants.TOP || alignment > EditorConstants.RIGHT) {
            throw new IllegalArgumentException("Invalid alignment for title!");
        }
        if (alignment == EditorConstants.TOP) { // Not Desired Long Term Functionality
            setHorizontalAlignment(LEFT);
        }
        titleAlignment = alignment;
    }

    /****************************************************************************************************
     * Retrieves the title alignment. This returns the integer that matches the title alignment (see
     * EditorConstants).
     * <p>
     * @return The title alignment.
     ***************************************************************************************************/
    public int getTitleAlignment() {
        return titleAlignment;
    }

    /****************************************************************************************************
     * Locks the size of the label to the width using the preferred height.
     * <p>
     * @param width The width of the label in pixels.
     ***************************************************************************************************/
    public void setLockedSize(int width) {
        setLockedSize(width, getPreferredSize().height);
    }

    /****************************************************************************************************
     * Locks the size of the label to the width using the preferred height.
     * <p>
     * @param width The width of the label in pixels.
     * @param height The height of the label in pixels.
     ***************************************************************************************************/
    public void setLockedSize(int width, int height) {
        setMaximumSize(new Dimension(width, height));
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width of the label.
     * <p>
     * @param width The width of the label in pixels.
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Sets the minimum and preferred height of the label.
     * <p>
     * @param height The height of the label in pixels.
     ***************************************************************************************************/
    public void setMinimumHeight(int height) {
        int width = 0;

        if (getPreferredSize().width > width) {
            width = getPreferredSize().width;
        } else if (getMinimumSize().width > width) {
            width = getMinimumSize().width;
        }
        setMinimumSize(width, height);
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width and height of the button.
     * <p>
     * @param width The width of the button in pixels.
     * @param height The height of the button in pixels.
     ***************************************************************************************************/
    public void setMinimumSize(int width, int height) {
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
    }

    /****************************************************************************************************
     * This method has been overwritten from the superclass to return a string containing information
     * about the required symbol and suffix. Since the required symbol is on the left hand side, all
     * centered and right aligned labels that are not required must remove their requiredSymbol from the
     * text when this is called. Left aligned labels ALWAYS have their required symbol.
     * <p>
     * @return The text of the label including the required symbol and suffix.
     ***************************************************************************************************/
    public String getText() {
        String text = super.getText();
        if (text.equals(StringConstants.NULL)) {
            return text;
        }
        int alignment = getHorizontalAlignment();
        if (!isRequired && (alignment == RIGHT || alignment == CENTER)) {
            int index = StringUtility.indexOf(text, requiredSymbol);
            text = StringUtility.substring(text, index + requiredSymbol.length());
        }
        return text;
    }

    /****************************************************************************************************
     * Returns the original text string assigned to the label (translated of course).
     * <p>
     * @return The original entered text.
     ***************************************************************************************************/
    public abstract String getOriginalText();

    /****************************************************************************************************
     * Sets the text to display.
     * <p>
     * @param text The text to display with the label.
     * @param translate True if the text should be translated, false if not.
     ***************************************************************************************************/
    public abstract void setText(String text, boolean translate);

    /****************************************************************************************************
     * Clears the text of the label.
     ***************************************************************************************************/
    public void clear() {
        setText(StringConstants.EMPTY);
    }

    /****************************************************************************************************
     * Sets the font style for the label.
     * <p>
     * @param style The style to assign to the font. /
     ***************************************************************************************************/
    public void setFontStyle(int style) {
        setFont(getFont().deriveFont(style));
    }

    /****************************************************************************************************
     * Sets the font style and size for the label.
     * <p>
     * @param style The style to assign to the font.
     * @param size The size to assign to the font.
     ***************************************************************************************************/
    public void setFontStyle(int style, float size) {
        setFont(getFont().deriveFont(style, size));
    }

    /****************************************************************************************************
     * Sets the font color, style and size for the label.
     * <p>
     * @param color The color to assign to the foreground of the label.
     * @param style The style to assign to the font.
     * @param size The size to assign to the font.
     ***************************************************************************************************/
    public void setFontStyle(Color color, int style, float size) {
        setFont(getFont().deriveFont(style, size));
        setForeground(color);
    }

    /****************************************************************************************************
     * @see setForeground() in JLabel.
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * @see setEnabled() in JLabel.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        updateColorState();
    }

    /****************************************************************************************************
     * @see setEnabled() in JLabel.
     ***************************************************************************************************/
    protected void updateColorState() {
        isControllingColor = true;
        if (isEnabled()) {
            setForeground(enabledForegroundColor);
        } else {
            setForeground(disabledForegroundColor);
        }
        isControllingColor = false;
    }
}
