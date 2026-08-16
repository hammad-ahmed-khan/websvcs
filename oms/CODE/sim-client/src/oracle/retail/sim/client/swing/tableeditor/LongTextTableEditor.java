package oracle.retail.sim.client.swing.tableeditor;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.core.SimNavigation;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.frame.ApplicationInternal;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.util.WindowPlacer;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RLongTextDialog;
import oracle.retail.sim.client.swing.widget.RLongTextListener;
import oracle.retail.sim.client.swing.widget.RTextField;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * A table editor entering long text strings - it pops up a dialog for additional help.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class LongTextTableEditor extends JPanel implements SimTableEditor, REventListener, FocusListener, MouseListener {
    private static final long serialVersionUID = -5785954896641807069L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private RTextField valueField = new RTextField();
    private RButton popupButton = new RButton(SimNavigation.LOOKUP);

    private String enteredText = StringConstants.EMPTY;

    private static final String SEARCH_TRIGGER = "RSearchFieldEditor.search";

    public LongTextTableEditor(String name) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);

        int searchWidth = UIManager.getInt(UIThemeName.RSEARCHFIELD_BUTTON_WIDTH);
        popupButton.setLockedSize(searchWidth, valueField.getPreferredSize().height);
        popupButton.registerAction(this, SEARCH_TRIGGER);
        popupButton.addMouseListener(this);

        valueField.setMargin(null);
        valueField.setBorder(null);
        valueField.addFocusListener(this);
        valueField.setIdentifier(name);

        setBorder(null);

        setLayout(new BorderLayout());
        add(popupButton, BorderLayout.EAST);
        add(valueField, BorderLayout.CENTER);
    }

    public Class getValueClass() {
        return String.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public JComponent getComponent() {
        return this;
    }

    public void setCoordinates(int row, int column) {
        // Ignore
    }

    public Object getValue() {
        if (valueField.isEnabled()) {
            return valueField.getText();
        }
        return enteredText;
    }

    public void setData(Object value) {
        if (value != null) {
            enteredText = value.toString();
        } else {
            enteredText = StringConstants.EMPTY;
        }
        valueField.setText(enteredText);
        validateValueFieldState();
    }

    private void validateValueFieldState() {
        char[] characters = enteredText.toCharArray();
        for (char character : characters) {
            if (character == '\n') {
                valueField.setEditable(false);
                valueField.setEnabled(false);
                return;
            }
        }
        valueField.setEditable(true);
        valueField.setEnabled(true);
    }

    public void setValue(Object value) {
        setData(value);
        eventAdaptor.fireTypeEditorEvent();
    }

    public boolean checkValue() {
        return true;
    }

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    /****************************************************************************************************
     * Determines if keystroke is invalid for input into the field.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }

    /****************************************************************************************************
     * Auto select the text when focus is gained.
     ***************************************************************************************************/

    public boolean requestFocusInWindow() {
        boolean returnValue = valueField.requestFocusInWindow();
        valueField.selectAll();
        return returnValue;
    }

    /****************************************************************************************************
     * Handles action events
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
    }

    public void performActionEvent(RActionEvent event) {
        Container container = valueField.getTopLevelAncestor();

        RLongTextDialog dialog = null;
        if (container instanceof JFrame) {
            dialog = new RLongTextDialog((JFrame) container, Translator.getText("Edit Text"));
        } else if (container instanceof JDialog) {
            dialog = new RLongTextDialog((JDialog) container, Translator.getText("Edit Text"));
        }
        if (dialog != null) {
            WindowPlacer.alignToComponent(ApplicationInternal.getFrame(), this, dialog, true, false);
            dialog.addTextListener(createTextListener());
            dialog.setIdentifier(valueField.getIdentifier());
            dialog.setText((String) getValue(), true);
            dialog.setVisible(true);
        }
    }

    public RLongTextListener createTextListener() {
        return new RLongTextListener() {
            public void updateText(String text) {
                setValue(text);
            }
        };
    }

    /****************************************************************************************************
     * Handles focus events
     ***************************************************************************************************/
    public void focusGained(FocusEvent event) {
    }

    /****************************************************************************************************
     * Handles focus lost events
     ***************************************************************************************************/
    public void focusLost(FocusEvent event) {
        if (event.isTemporary()) {
            return;
        }
        eventAdaptor.fireTypeEditorEvent();
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
        popupButton.requestFocusInWindow();
    }

    public void mouseExited(MouseEvent e) {
        valueField.requestFocusInWindow();
    }
}
