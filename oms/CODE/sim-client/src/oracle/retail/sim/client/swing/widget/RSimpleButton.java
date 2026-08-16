package oracle.retail.sim.client.swing.widget;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class subclasses the standard JButton class in the Swing package to provide custom
 * functionality.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RSimpleButton extends JButton implements RetailComponent, ActionListener {
    private static final long serialVersionUID = 5572955421143802397L;

    private UIPermissionManager permissionManager = new UIPermissionManager();
    private NavigationPermission permission = NavigationPermission.FULL;
    private String identifier = StringConstants.EMPTY;
    private REventListener eventListener;

    /******************************************************************************************
     * Returns new RButton object.
     *****************************************************************************************/
    public RSimpleButton() {
        setOpaque(true);
        addActionListener(this);
    }

    /******************************************************************************************
     * Returns new RButton with a title to display.
     * <p>
     * @param title The title to assign to the button.
     *****************************************************************************************/
    public RSimpleButton(String title) {
        setText(title);
        setActionCommand(title);
        addActionListener(this);
    }

    /******************************************************************************************
     * Assigns an identifier to the button. This is used in order to make setName() useable
     * by developers. This is the means by which the framework identifiers an button.
     * <p>
     * @param identifier The identifier to assign to the button.
     ******************************************************************************************/
    public void setIdentifier(String identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("The id parameter cannot be null!");
        }
        this.identifier = identifier;
    }

    /******************************************************************************************
     * Retrieves the identifer to the button.
     * <p>
     * @return The identifier.
     ******************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /******************************************************************************************
     * Sets the minimum, maximum and preferred width of the button.
     * <p>
     *@param width The width of the button in pixels.
     *****************************************************************************************/
    public void setLockedWidth(int width) {
        setLockedSize(width, getPreferredSize().height);
    }

    /******************************************************************************************
     * Sets the minimum, maximum and preferred width of the button.
     * <p>
     *@param width The width of the button in pixels.
     *@param height The height of the button in pixels.
     ******************************************************************************************/
    public void setLockedSize(int width, int height) {
        Dimension dimension = new Dimension(width, height);
        setMaximumSize(dimension);
        setMinimumSize(dimension);
        setPreferredSize(dimension);
    }

    /******************************************************************************************
     * Sets the minimum and preferred width of the button.
     * <p>
     *@param width The width of the button in pixels.
     *****************************************************************************************/
    public void setMinimumWidth(int width) {
        setMinimumSize(width, getPreferredSize().height);
    }

    /******************************************************************************************
     * Sets the minimum and preferred width and height of the button.
     * <p>
     *@param width The width of the button in pixels.
     *@param height The height of the button in pixels.
     *****************************************************************************************/
    public void setMinimumSize(int width, int height) {
        setMinimumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
    }

    /******************************************************************************************
     * Sets the action the button should trigger. This does not create an actual Action object.
     * Using this method, the RButton will deliver its actions as an RActionEvent to the
     * REventListener.
     * <p>
     *@param eventListener The REventListener to receive the action from this button.
     *@param actionCommand The action command to assign to the action.
     *****************************************************************************************/
    public void registerAction(REventListener eventListener, String actionCommand) {
        this.eventListener = eventListener;
        setActionCommand(actionCommand);
    }

    /******************************************************************************************
     * Sets the action the button should trigger. This does not create an actual Action object.
     * Using this method, the RButton will deliver its actions as an RActionEvent to the
     * REventListener.
     * <p>
     *@param eventListener The REventListener to receive the action from this button.
     *@param actionCommand The action command to assign to the action.
     *@param mnemonic The character keystroke the will trigger the button action.
     *****************************************************************************************/
    public void registerAction(REventListener eventListener, String actionCommand, char mnemonic) {
        this.eventListener = eventListener;
        setActionCommand(actionCommand);
        setMnemonic(mnemonic);
    }

    /******************************************************************************************
     * Sets the text to display within the button. The superclass is overridden to supply
     * language translation.
     * <p>
     *@param text The text to display within the button.
     *****************************************************************************************/
    public void setText(String text) {
        super.setText(Translator.getText(text));
        setMnemonic(Translator.getMnemonic(text));
        setPreferredSize(null);
    }

    /******************************************************************************************
     * Overrides the setVisible() method to guarantee that the button has permission.
     *****************************************************************************************/
    public void setVisible(boolean visible) {
        if (permission.equals(NavigationPermission.NONE)) {
            visible = false;
        }
        super.setVisible(visible);
    }

    /******************************************************************************************
     * Overrides the setEnabled() method to guarantee that the button has permission.
     *****************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (!permission.equals(NavigationPermission.FULL)) {
            enabled = false;
        }
        super.setEnabled(enabled);
    }

    /******************************************************************************************
     * Validates the permission of the object based on its identifier. If no identifier
     * exists, then the permission is true. If an identifier exists and the permission returns
     * as false, the component will not be able to be enabled()
     * <p>
     * @param ownerPrefix The owner class name to attach to the identifier to find permission.
    /******************************************************************************************/
    public void validatePermission(String ownerPrefix) throws UIException {
        permission = permissionManager.getComponentPermission(identifier, ownerPrefix);

        if (permission.equals(NavigationPermission.FULL)) {
            return;
        }
        if (permission.equals(NavigationPermission.NONE)) {
            setVisible(false);
        }
        setEnabled(false);
    }

    /******************************************************************************************
     * Implements the action performed method to notify the existing event listener of the
     * action. This is done to conform to the RActionEvent pattern for those who choose to do so.
     /******************************************************************************************/
    public void actionPerformed(ActionEvent event) {
        if (eventListener != null) {
            eventListener.performActionEvent(new RActionEvent(this, getActionCommand()));
        }
    }
}
