package oracle.retail.sim.client.application;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

/********************************************************************************************************
 * MNEMONIC LETTER
 * <p>
 * A class responsible for dynamically selecting the appropriate letter for mnemonics to a series of
 * buttons.
 * <p>
 * If the flag is set to native letter, then the first letter of each button title not already used will
 * be selected as the mnemonic. Using this option will require a keyboard with native keys.
 * <p>
 * The letter chosen must be part of the English alphabet and a unique letter will be chosen for
 * each button. In the case of a foreign label that does NOT contain an English letter, the next
 * available letter in the alphabet is chosen and assigned to the button along with appending the new
 * letter at the end of the title.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class MnemonicLetter {
    private static final char[] upperAlphabet = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
    private static final char[] lowerAlphabet = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

    private Set<Character> usedLetterSet = new HashSet<>();

    /**
     * Construct a new MnemonicLetter.
     */
    public MnemonicLetter() {
    }

    /**
     * Apply mnemonics to each button in array for most appropriate character.
     */
    public static void applyDynamicMnemonics(JButton[] buttons) {
        applyDynamicMnemonics(buttons, false);
    }

    /**
     * Apply mnemonics to each button in array for most appropriate character.
     */
    public static void applyDynamicMnemonics(JButton[] buttons, boolean isNativeLetterLanguage) {
        if (buttons == null) {
            return;
        }
        MnemonicLetter mnemonicLetter = new MnemonicLetter();
        for (JButton button : buttons) {
            mnemonicLetter.setNextMnemonic(button, isNativeLetterLanguage);
        }
    }

    /**
     * Method applies a determined mnemonic to button assigning a keystroke action.
     */
    private void setNextMnemonic(AbstractButton button, boolean isNativeLetterLanguage) {
        Character character = isNativeLetterLanguage ? getNextNativeMnemonicLetter(button) : getNextMnemonicLetter(button);
        if (character == null) {
            return;
        }
        KeyStroke keyStroke = KeyStroke.getKeyStroke(character, InputEvent.ALT_MASK, false);
        button.setMnemonic(character);
        button.registerKeyboardAction(getButtonAction(button), keyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    /**
     * This method performs the grunt work behind determining the most appropriate mnemonic. It checks
     * first for a valid character in the label that is not used and if none is found, picks the next
     * unused letter of the alphabet and appends it to the title.
     */
    private Character getNextMnemonicLetter(AbstractButton button) {
        for (char character : button.getText().toCharArray()) {
            if (isValidCharacter(character)) {
                character = Character.toUpperCase(character);
                if (!usedLetterSet.contains(character)) {
                    usedLetterSet.add(character);
                    return character;
                }
            }
        }
        for (char character : upperAlphabet) {
            if (!usedLetterSet.contains(character)) {
                usedLetterSet.add(character);
                button.setText(button.getText() + "(" + character + ")");
                return character;
            }
        }
        return null;
    }

    /**
     * This method performs the grunt work behind determining the most appropriate mnemonic. It checks
     * first for a valid character in the label that is not used and if none is found, picks the next
     * unused letter of the alphabet and appends it to the title.
     */
    private Character getNextNativeMnemonicLetter(AbstractButton button) {
        for (char character : button.getText().toCharArray()) {
            character = Character.toUpperCase(character);
            if (Character.isLetter(character) && !usedLetterSet.contains(character)) {
                usedLetterSet.add(character);
                return character;
            }
        }
        return null;
    }

    /**
     * Ensures the character is in the basic English character set as they are the only ones usable as mnemonics.
     */
    private boolean isValidCharacter(char c) {
        for (int i = 0; i < upperAlphabet.length; i++) {
            if (upperAlphabet[i] == c || lowerAlphabet[i] == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates button action for button. Simply clicks the button.
     */
    private ActionListener getButtonAction(final AbstractButton button) {
        return new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button.doClick();
            }
        };
    }
}
