package oracle.retail.sim.client.swing.editor;

import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class subclasses JLabel and adds to its functionality the ability to have a required indicator
 * symbol in front of the label and a suffix behind the label. This is the label that is used inside ALL
 * Editors.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RPlainEditorLabel extends REditorLabel {
    private static final long serialVersionUID = 5594440567525873431L;

    private static final String UID = "EditorLabelUI";
    protected String suffixSymbol = StringConstants.EMPTY;

    /****************************************************************************************************
     * Returns new REditorLabel object.
     ***************************************************************************************************/
    public RPlainEditorLabel() {
        initializePlainLabel();
    }

    /****************************************************************************************************
     * Returns new REditorLabel object with display text assigned.
     * <p>
     * @param text The display text to assign to the label. /
     ***************************************************************************************************/
    public RPlainEditorLabel(String text) {
        super(text);
        initializePlainLabel();
    }

    /****************************************************************************************************
     * Initializes the suffix symbol for the label
     ***************************************************************************************************/
    protected void initializePlainLabel() {
        suffixSymbol = UIManager.getString(UIThemeName.EDITOR_LABEL_SUFFIX);
    }

    /****************************************************************************************************
     * Returns a string that specifies the name of the L&F class that renders this component.
     * <p>
     * @return The string "EditorLabelUI" /
     ***************************************************************************************************/
    public String getUIClassID() {
        return UID;
    }

    /****************************************************************************************************
     * Returns the original text string assigned to the label (translated of course).
     * <p>
     * @return The original entered text.
     ***************************************************************************************************/
    public String getOriginalText() {
        String text = getText();
        if (text.equals(StringConstants.NULL)) {
            return StringConstants.EMPTY;
        }
        if (!StringUtility.isNullOrEmpty(suffixSymbol)) {
            int index = StringUtility.lastIndexOf(text, suffixSymbol);
            if (index != -1) {
                text = StringUtility.substring(text, 0, index);
            }
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

    /****************************************************************************************************
     * Sets the text to display within the label. Overrides the superclass method to supply language
     * translation to the text string.
     * <p>
     * @param text The text to display within the label.
     ***************************************************************************************************/
    public void setText(String text) {
        if (StringUtility.isNullOrEmpty(text)) {
            super.setText(StringConstants.EMPTY);
        } else if (StringUtility.isNullOrEmpty(suffixSymbol)) {
            super.setText(requiredSymbol + Translator.getText(text));
        } else {
            super.setText(requiredSymbol + Translator.getText(text) + suffixSymbol);
        }
    }

    /****************************************************************************************************
     * Sets the text to display.
     * <p>
     * @param text The text to display with the label.
     * @param translate True if the text should be translated, false if not.
     ***************************************************************************************************/
    public void setText(String text, boolean translate) {
        if (translate) {
            setText(text);
        } else if (StringUtility.isNullOrEmpty(text)) {
            super.setText(StringConstants.EMPTY);
        } else if (StringUtility.isNullOrEmpty(suffixSymbol)) {
            super.setText(requiredSymbol + text);
        } else {
            super.setText(requiredSymbol + text + suffixSymbol);
        }
    }
}
