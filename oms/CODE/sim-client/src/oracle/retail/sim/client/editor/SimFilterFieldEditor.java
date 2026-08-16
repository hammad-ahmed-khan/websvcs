package oracle.retail.sim.client.editor;

import java.util.Map;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.editor.RButtonTextFieldEditor;

/********************************************************************************************************
 * This is a specific filter text field editor for SIM filter popup triggers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimFilterFieldEditor extends RButtonTextFieldEditor {
    private static final long serialVersionUID = -5566323665382309297L;

    /****************************************************************************************************
     * Creates a new SimFilterFieldEditor with the title 'Filter'.
     ***************************************************************************************************/
    public SimFilterFieldEditor() {
        super("Filter");
    }

    /****************************************************************************************************
     * Assigns text to the display area and tool tip by displaying key = value in a sequence broken by
     * the '|' character.
     * @param descriptionMap A map containing key value pairs to display.
     ***************************************************************************************************/
    public void setText(Map<String, String> descriptionMap) {
        StringBuilder textBuffer = new StringBuilder();
        StringBuilder tippBuffer = new StringBuilder("<html>");

        for (Map.Entry<String, String> entry : descriptionMap.entrySet()) {
            String value = Translator.getText(entry.getKey()) + " = " + Translator.getText(entry.getValue());
            if (textBuffer.length() != 0) {
                textBuffer.append(" | ");
                tippBuffer.append("<p>");
            }
            tippBuffer.append(value);
            textBuffer.append(value);
        }
        tippBuffer.append("</html>");
        super.setText(textBuffer.toString());
        super.setToolTipText(tippBuffer.toString());
    }

    /****************************************************************************************************
     * Assigning text also assigns tooltip text. This assumes a sequence of text broken by "|" characters
     * to determine the separation of key-value pairs.
     ***************************************************************************************************/
    public void setText(String text) {
        super.setText(text);
        super.setToolTipText(formatToolTipText(text));
    }

    /****************************************************************************************************
     * Assigning tooltip text also assigns text. This assumes a sequence of text broken by "|" characters
     * to determine the separation of key-value pairs.
     ***************************************************************************************************/
    public void setToolTipText(String text) {
        super.setText(text);
        super.setToolTipText(formatToolTipText(text));
    }

    /****************************************************************************************************
     * Return formatted toolip text as an html string with line breaks instead of "|"
     ***************************************************************************************************/
    private String formatToolTipText(String text) {
        StringBuilder tippBuffer = new StringBuilder("<html>");
        String[] array = StringUtility.getStringArray(text, "\\|");
        boolean needLineBreak = false;
        for (String element : array) {
            if (needLineBreak) {
                tippBuffer.append("<p>");
            }
            tippBuffer.append(element);
            needLineBreak = true;
        }
        tippBuffer.append("</html>");
        return tippBuffer.toString();
    }
}
