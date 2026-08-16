package oracle.retail.sim.client.swing.editor;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This is a box style label with a line border around it. It may be used as a substitute
 * for the editor label.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RBoxEditorLabel extends REditorLabel {
    private static final long serialVersionUID = -2346551411739671395L;

    private static final String UID = "BoxEditorLabelUI";
    private Color borderColor = Color.BLACK;
    private int borderThickness = 1;

    /******************************************************************************************
     * Returns new RBoxLabel object.
     *****************************************************************************************/
    public RBoxEditorLabel() {
        setOpaque(true);
        initializeBoxLabel();
    }

    /******************************************************************************************
     * Returns new RBoxLabel object with display text assigned.
     * <p>
     * @param text The display text to assign to the label.
    /*****************************************************************************************/
    public RBoxEditorLabel(String text) {
        setText(text);
        setOpaque(true);
        initializeBoxLabel();
    }

    /******************************************************************************************
     * Initializes colors
     *****************************************************************************************/
    protected void initializeBoxLabel() {
        setBackground(UIManager.getColor(UIThemeName.BOXLABEL_BACKGROUND));
        setForeground(UIManager.getColor(UIThemeName.BOXLABEL_FOREGROUND));
        setBorderColor(UIManager.getColor(UIThemeName.BOXLABEL_LINE_COLOR));
        setHorizontalAlignment(CENTER);
    }

    /******************************************************************************************
     * Returns a string that specifies the name of the L&F class that renders this component.
     * <p>
     * @return The string "BoxEditorLabelUI"
    /******************************************************************************************/
    public String getUIClassID() {
        return UID;
    }

    /******************************************************************************************
     * Assigns a color the line border around the box label. This will automatically revert
     * the label to a line border if the border has been altered.
     * <p>
     * @param color The color to assign.
     *****************************************************************************************/
    public void setBorderColor(Color color) {
        if (color == null) {
            color = Color.BLACK;
        }
        borderColor = color;
        resetLineBorder();
    }

    /******************************************************************************************
     * Retrieves the border color.
     * <p>
     * @return The border color.
     *****************************************************************************************/
    public Color getBorderColor() {
        return borderColor;
    }

    /******************************************************************************************
     * Assigns a thickness to line border around the box label. This will automatically revert
     * the label to a line border if the border has been altered.
     * <p>
     * @param thickness The thickness to assign.
     *****************************************************************************************/
    public void setLineThickness(int thickness) {
        if (thickness < 1) {
            thickness = 1;
        }
        borderThickness = thickness;
        resetLineBorder();
    }

    /******************************************************************************************
     * Retrieves the border thickness.
     * <p>
     * @return The border thickness.
     *****************************************************************************************/
    public int getBorderThickness() {
        return borderThickness;
    }

    /******************************************************************************************
     * Resets the box label to a full line border.
     *****************************************************************************************/
    public void resetLineBorder() {
        setBorder(BorderFactory.createLineBorder(borderColor, borderThickness));
    }

    /******************************************************************************************
     * Returns the original text string assigned to the label (translated of course).
     * <p>
     * @return The original entered text.
     ******************************************************************************************/
    public String getOriginalText() {
        String text = getText();
        if (text.equals(StringConstants.NULL)) {
            return StringConstants.EMPTY;
        }
        if (text.equals(StringConstants.NULL)) {
            return StringConstants.EMPTY;
        }
        if (requiredSymbol != null) {
            int index = StringUtility.indexOf(text, requiredSymbol);
            if (index != -1) {
                text = StringUtility.substring(text, index + requiredSymbol.length());
            }
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
            super.setText(requiredSymbol + Translator.getText(text));
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
            super.setText(requiredSymbol + text);
        }
    }
}
