package oracle.retail.sim.client.swing.event;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.util.UIPropertyName;

/******************************************************************************************
 * Listens for changes in empty or full state in a text field and then sends the appropriate
 * registered action.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public abstract class EmptyStateActionAdaptor implements KeyListener {

    private REventListener emptyEventListener;
    private String identifier;

    /******************************************************************************************
     * Constructs a new empty state action adapter.
     ******************************************************************************************/
    protected EmptyStateActionAdaptor() {
    }

    /******************************************************************************************
     * Registers an event listener with the action adaptor. In order for the action adaptor
     * to be enabled, both the listener and the identifier must contain values.
     * <p>
     * @param listener The REventListener to assign to the action adaptor.
     * @param identifier The identifier to return in the action event.
     ******************************************************************************************/
    public void registerListener(REventListener listener, String identifier) {
        emptyEventListener = listener;
        this.identifier = identifier;
    }

    /******************************************************************************************
     * Implements the key released method to do nothing.
     ******************************************************************************************/
    public void keyReleased(KeyEvent event) {
    }

    /******************************************************************************************
     * Implements the key pressed method to watch for delete and back space keystrokes. The
     * method to check if the value is empty is called.
     ******************************************************************************************/
    public void keyPressed(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_BACK_SPACE:
                doEmptyStateTextRemoved();
                break;
            default:
                break;
        }
    }

    /******************************************************************************************
     * Implements the key typed method to call an non-empty state check is any key is typed
     * except for back space, delete, space and tab.
     ******************************************************************************************/
    public void keyTyped(KeyEvent event) {
        switch (event.getKeyChar()) {
            case KeyEvent.VK_BACK_SPACE:
            case KeyEvent.VK_DELETE:
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_TAB:
                break;
            default:
                doEmptyStateTextType();
                break;
        }
    }

    /******************************************************************************************
     * If the adaptor is enabled and the field is empty, send the event.
     ******************************************************************************************/
    private void doEmptyStateTextType() {
        if (isEnabled() && getTextLength() == 0) {
            RActionEvent event = new RActionEvent(this, UIPropertyName.TEXT_COMPONENT_NOT_EMPTY, identifier);
            emptyEventListener.performActionEvent(event);
        }
    }

    /******************************************************************************************
     * If the adaptor is enabled and the field is not empty, send the event.
     ******************************************************************************************/
    private void doEmptyStateTextRemoved() {
        if (isEnabled()) {
            int length = getTextLength();
            int selectedLength = getSelectedTextLength();

            if (selectedLength > 0) {
                length = length - selectedLength;
            }
            if (length < 2) {
                RActionEvent event = new RActionEvent(this, UIPropertyName.TEXT_COMPONENT_EMPTY, identifier);
                emptyEventListener.performActionEvent(event);
            }
        }
    }

    /******************************************************************************************
     * Return true if the listener and identifier contain values.
     ******************************************************************************************/
    private boolean isEnabled() {
        return emptyEventListener != null && !StringUtility.isNullOrEmpty(identifier);
    }

    /******************************************************************************************
     * Returns the length of the text in the field where this adaptor is assigned.
     ******************************************************************************************/
    public abstract int getTextLength();

    /******************************************************************************************
     * Returns the length of the selected text in the field where this adaptor is assigned.
     ******************************************************************************************/
    public abstract int getSelectedTextLength();
}
