package oracle.retail.sim.client.swing.editor;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;

/********************************************************************************************************
 * Utility object for determining whether or not a keystroke should be allowed.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class ValidKeystrokeUtility {

    public static final int TOTAL_NUMBER_SIZE = 14;
    public static final int BASIC_NUMBER_SIZE = 10;
    public static final int DECIM_PLACE_SIZE = 3;

    /****************************************************************************************************
     * Determines for the JTextField whether or not the keystroke is valid numeric input. This class is
     * intended for use with Swing editors only. It allows only 14 total characters, with ten basic
     * digits, the decimal separator and three decimal places.
     * @param valueField The JTextField the input is for.
     * @param keyChar The character typed.
     * @param keyCode The key code of the character type.
     * @return True if the keystroke is valid, false if not.
     ***************************************************************************************************/
    public static boolean isValidNumericKeystroke(JTextField valueField, char keyChar, int keyCode) {
        char separator = LocaleManager.getNumberDecimalSymbols().getDecimalSeparator();
        String text = valueField.getText();
        if (Character.isDigit(keyChar)) {
            if (valueField.getSelectedText() != null) {
                return true;
            }
            if (text.length() == TOTAL_NUMBER_SIZE) {
                if (valueField.getSelectedText() == null) {
                    if (keyCode != KeyEvent.VK_DELETE) {
                        return false;
                    }
                }
                return true;
            }
            int index = StringUtility.indexOf(text, separator);
            if (index > -1) {
                if (valueField.getCaretPosition() <= index) {
                    if (text.length() >= BASIC_NUMBER_SIZE) {
                        return false;
                    }
                    return true;
                }
                if (StringUtility.substring(text, index).length() <= DECIM_PLACE_SIZE) {
                    return true;
                }
                return false;
            }
            if (text.length() >= BASIC_NUMBER_SIZE) {
                return false;
            }
            return true;
        }
        if (keyChar != separator) {
            return false;
        }
        if (StringUtility.indexOf(text, separator) > -1) {
            return false;
        }
        return true;
    }
}
