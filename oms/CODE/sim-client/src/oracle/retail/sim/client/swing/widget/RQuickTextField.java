package oracle.retail.sim.client.swing.widget;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import oracle.retail.sim.client.locale.StringUtility;

/******************************************************************************************
 * This class sub-classes the RTextField class in the Swing package to provide some special
 * functionality. A map of short-cuts to long text mappings can be added. The class will
 * automatically substitute the text as the user is typing.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RQuickTextField extends RTextField {
    private static final long serialVersionUID = -1825002914083183875L;

    private Map quickMap;
    private StringBuilder builtWord;
    private String[] quickList;
    private String[] validList;
    private boolean quickFlag;
    private boolean quickTemp = true;

    /******************************************************************************************
     * Returns a new RQuickTextField object.
     *****************************************************************************************/
    public RQuickTextField() {
    }

    /******************************************************************************************
     * Implements the key listener interface "key released" method. If quick entries has been
     * activated for the text area, then the current built word must be reinitialized if a
     * key was released and the SHIFT and ALT is not pressed.
     * <p>
     *@param event Details about the key event that occurred.
     *****************************************************************************************/
    public void keyReleased(KeyEvent event) {
        if (quickFlag) {
            if (event.isShiftDown() && event.isAltDown()) {
                return;
            }
            builtWord = new StringBuilder();
            quickTemp = true;
        }
    }

    /******************************************************************************************
     * Implements the key listener interface "key typed" method. It captures the key typed
     * action, checks to see if the allowable length is reached and if the key is not a
     * backspace/delete/left arrow key, then the key is ignored.
     * <p>
     * If the text in the field is selected, a new keystroke would replace the selected text
     * and by default that means we cannot allow the event to continue.
     * <p>
     * If quick entry has been activated, the system must look for SHIFT/ALT and then find
     * if the sequence of characters in the built up word exists in the quickmap. If it does
     * the information should replace the text in the text area.
     * <p>
     *@param event Details about the key event that occurred.
     *****************************************************************************************/
    public void keyTyped(KeyEvent event) {
        if (getSelectedText() != null) {
            return;
        }
        if (processKey && getUnformattedText().length() >= allowedLength) {
            event.consume();
        }
        if (quickFlag && quickTemp) {
            if (event.isShiftDown() && event.isAltDown()) {
                builtWord.append(event.getKeyChar());

                if (validList != null) {
                    validList = search(validList);
                } else {
                    validList = search(quickList);
                }

                if (validList != null && validList.length == 1) {
                    setText((String) quickMap.get(validList[0]));
                    builtWord = new StringBuilder();
                    validList = null;
                    quickTemp = false;
                }
            } else if (validList != null) {
                validList = null;
                builtWord = new StringBuilder();
            }
        }
    }

    /******************************************************************************************
     * Determines if the currently build word exists in the parameter array. If returns an
     * array of valid matches of quick entries that start with the string of characters in the
     * built work.
     * <p>
     * @param array An array of strings to check the quick entry built word against.
     * @return An array of the remaining valid matches to the built word.
     *****************************************************************************************/
    private String[] search(String[] array) {
        String key = builtWord.toString();
        List list = new ArrayList<>();
        for (String element : array) {
            if (StringUtility.startsWith(element, key)) {
                list.add(element);
            }
        }
        if (list.isEmpty()) {
            return null;
        }
        String[] varray = new String[list.size()];
        for (int i = 0; i < varray.length; i++) {
            varray[i] = (String) list.get(i);
        }
        return varray;
    }

    /******************************************************************************************
     * Assigns a map of quick keystroke entries. The keys of the map represent a set of strings
     * that if they are typed, the value in the map will instantly be assigned to the text field.
     * <p>
     * @param The keystroke string to replacement string map.
     *****************************************************************************************/
    public void setQuickEntries(Map map) {
        Set keySet = map.keySet();

        quickList = new String[keySet.size()];

        int i = 0;
        for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
            quickList[i++] = (String) iterator.next();
        }
        quickMap = map;
        quickFlag = true;
        builtWord = new StringBuilder();
        validList = new String[0];
    }
}
