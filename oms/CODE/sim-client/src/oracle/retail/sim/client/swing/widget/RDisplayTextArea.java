package oracle.retail.sim.client.swing.widget;

import javax.swing.JTextArea;
import javax.swing.UIManager;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.Mask;

/******************************************************************************************
 * This class subclasses the standard JTextArea class in the Swing package to provide a text
 * area that by default can never be edited and has no border. This is a display area only.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RDisplayTextArea extends JTextArea {
    private static final long serialVersionUID = 1978931746597044583L;

    private Mask formatMask;

    /******************************************************************************************
     * Returns new RDisplayTextArea object.
     *****************************************************************************************/
    public RDisplayTextArea() {
        setBackground(UIManager.getColor(UIThemeName.DISPLAY_TEXTAREA_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.DISPLAY_TEXTAREA_FOREGROUND));
        setMargin(UIManager.getInsets(UIThemeName.DISPLAY_TEXTAREA_MARGIN));
        setDoubleBuffered(true);
        setWrapStyleWord(true);
        setLineWrap(true);
        setFocusable(false);
        setEnabled(false);
        setEditable(false);
    }

    /******************************************************************************************
     * Override the setEnabled() method to ensure that the widget can never be enabled.
     *****************************************************************************************/
    public void setEnabled(boolean enabled) {
        // Do Nothing
    }

    /******************************************************************************************
     * Override the setEditable() method to ensure that the widget can never be editable.
     *****************************************************************************************/
    public void setEditable(boolean editable) {
        // Do Nothing
    }

    /******************************************************************************************
     * Assigns a format mask to the text area. The specific mask is cast to the interface for
     * generic operability.
     * <p>
     *@param formatMask A format mask to assign to this text area.
     *****************************************************************************************/
    public void setMask(Mask formatMask) {
        this.formatMask = formatMask;
    }

    /******************************************************************************************
     * Removes the mask assigned to this text area.
     *****************************************************************************************/
    public void removeMask() {
        formatMask = null;
    }

    /******************************************************************************************
     * Refreshes the text area (this will trigger the formatting logic).
     *****************************************************************************************/
    public void refresh() {
        setText(getText());
    }

    /******************************************************************************************
     * Assigns the text in the area. The text will be formatted if a mask exists. Text longer
     * than the allowed length is, interestingly, not allowed.
     * <p>
     * @param text The text to place in the text area.
     *****************************************************************************************/
    public void setText(String text) {
        if (text == null) {
            text = StringConstants.EMPTY;
        }
        if (formatMask != null) {
            text = formatMask.format(text);
        }
        super.setText(text);
    }

    /******************************************************************************************
     * Assigns the text in the text field, formatted from a string array through a mask.
     * <p>
     * @param textArray An array of strings to format through a mask.
     *****************************************************************************************/
    public void setText(String[] textArray) {
        super.setText(formatMask.format(textArray));
    }

    /******************************************************************************************
     * Appends text to the text area.
     *****************************************************************************************/
    public void append(String text) {
        if (text != null) {
            setText(getText() + text);
        }
    }

    /******************************************************************************************
     * Retrieves the text string from the text field. Overrides superclass to handle
     * unformatting and trimming.
     * <p>
     * @return The unformatted and trimmed text.
     *****************************************************************************************/
    public String getText() {
        String text = super.getText();
        if (formatMask != null) {
            text = formatMask.unformat(text);
        }
        return text.trim();
    }

    /******************************************************************************************
     * Retrieves whether or not the text area is empty.
     * <p>
     * @return True if the text area is empty, false if not.
     *****************************************************************************************/
    public boolean isEmpty() {
        return getText().length() == 0;
    }

    /******************************************************************************************
     * Clears the text area of input.
     *****************************************************************************************/
    public void clear() {
        setText(StringConstants.EMPTY);
    }
}
