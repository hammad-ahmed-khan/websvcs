package oracle.retail.sim.client.swing.editor;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.util.RequiredTranslator;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * All editors should extends abstract editor.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public abstract class AbstractEditor extends JPanel implements RetailEditor {

    private UIPermissionManager permissionManager;
    private NavigationPermission permission = NavigationPermission.FULL;
    private String identifier = StringConstants.EMPTY;

    protected int sizeType = -1;
    protected boolean requiredValueAssigned;
    protected boolean isActionEnabled = true;

    protected RLabel errorLabel = new RLabel();
    protected RLabel spaceLabel = new RLabel();
    protected RLabel innerLabel = new RLabel();

    protected ImageIcon errorIcon;
    protected boolean isErrorState;

    /****************************************************************************************************
     * Constructor.
     ***************************************************************************************************/
    protected AbstractEditor() {
        setOpaque(true);
    }

    /****************************************************************************************************
     * Assigns an identifier to the editor. This is used in order to make setName() useable by
     * developers. This is the means by which the framework identifiers an editor.
     * <p>
     * @param identifier The identifier to assign to the editor.
     ***************************************************************************************************/
    public void setIdentifier(String identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("The id parameter cannot be null!");
        }
        this.identifier = identifier;
        doIdentifierAltered(identifier);
    }

    /****************************************************************************************************
     * Retrieves the identifer to the editor.
     * <p>
     * @return The identifier.
     ***************************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /****************************************************************************************************
     * This method is defined by all subclasses and handles specific editor code for an alteration of the
     * identifier.
     ***************************************************************************************************/
    protected abstract void doIdentifierAltered(String identifier);

    /****************************************************************************************************
     * Marks that the required value has already been assigned.
     ***************************************************************************************************/
    protected void markRequiredAssigned() {
        requiredValueAssigned = true;
    }

    /****************************************************************************************************
     * Retrieves the size type of the editor.
     * <p>
     * @return The size type (SMALL, MEDIUM, or LARGE) or -1 if none is assigned.
     ***************************************************************************************************/
    public int getSizeType() {
        return sizeType;
    }

    /****************************************************************************************************
     * Retrieves the vertical weight of the editor (used with REditorPanel)
     * <p>
     * @return The vertical weight of the editor.
     ***************************************************************************************************/
    public int getVerticalWeight() {
        return 0;
    }

    /****************************************************************************************************
     * Retrieves the horiztonal weight of the editor (used with REditorPanel)
     * <p>
     * @return The horiztonal weight of the editor.
     ***************************************************************************************************/
    public int getHorizontalWeight() {
        return 1;
    }

    /****************************************************************************************************
     * Retrieves the fill of the editor (used with REditorPanel);
     * <p>
     * @return The fill of the editor.
     ***************************************************************************************************/
    public int getFill() {
        return 1;
    }

    /****************************************************************************************************
     * Enables or disabled the registered action triggers of the editor.
     * <p>
     * @param enabled True if actions should be sent, false if not.
     ***************************************************************************************************/
    public void setActionsEnabled(boolean enabled) {
        isActionEnabled = enabled;
    }

    /****************************************************************************************************
     * Retrieves whether or not the registered action triggers of the editor.
     * <p>
     * @return True if actions are enabled, false if not.
     ***************************************************************************************************/
    public boolean isActionsEnabled() {
        return isActionEnabled;
    }

    /****************************************************************************************************
     * Retrieves whethor or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     ***************************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /****************************************************************************************************
     * Retrieves the error message stored with the editor, if one currently exists.
     * <p>
     * @return The error message or an empty string if none exists.
     ***************************************************************************************************/
    public String getErrorMessage() {
        return errorLabel.getToolTipText();
    }

    /****************************************************************************************************
     * Uses the ownerPrefix and the identifier to determine whether or not the field should be required
     * by looking in the properties file.
     * <p>
     * @param ownerPrefix The owner class name to attach to the identifier to find permission.
     ***************************************************************************************************/
    public void validateRequiredState(String ownerPrefix) {
        if (StringUtility.isNullOrEmpty(ownerPrefix) || StringUtility.isNullOrEmpty(identifier)) {
            return;
        }
        if (requiredValueAssigned) {
            return;
        }
        setRequired(RequiredTranslator.isRequired(ownerPrefix + StringConstants.DOT + identifier));
        markRequiredAssigned();
    }

    /****************************************************************************************************
     * Validates the permission of the object based on its identifier. If no identifier exists, then the
     * permission is true. If an identifier exists and the permission returns as false, the component
     * will not be able to be enabled()
     * <p>
     * @param ownerPrefix The owner class name to attach to the identifier to find permission. /
     ***************************************************************************************************/
    public void validatePermission(String ownerPrefix) throws UIException {
        if (permissionManager == null) {
            permissionManager = new UIPermissionManager();
        }
        if (StringUtility.isNullOrEmpty(identifier)) {
            return;
        }
        permission = permissionManager.getComponentPermission(identifier, ownerPrefix);
        if (permission.equals(NavigationPermission.FULL)) {
            return;
        }
        if (permission.equals(NavigationPermission.NONE)) {
            setVisible(false);
        }
        setEnabled(false);
    }

    /****************************************************************************************************
     * Returns true if the abstract editor should not be made enabled under any circumstance.
     ***************************************************************************************************/
    protected boolean isEnabledDenied() {
        return permission.equals(NavigationPermission.NONE) || permission.equals(NavigationPermission.VIEW);
    }

    /****************************************************************************************************
     * Returns true if the abstract editor should not be made visible under any circumstance.
     ***************************************************************************************************/
    protected boolean isVisibleDenied() {
        return permission.equals(NavigationPermission.NONE);
    }

    /****************************************************************************************************
     * Displays an exception on the appropriate container. It checks first an exception displayer, then
     * for the proper parent container. If nothing is found, the exception is not displayed.
     * <p>
     * @param exception The exception to display.
     ***************************************************************************************************/
    protected abstract void displayException(UIException exception);

    /****************************************************************************************************
     * Creates a focus listener that can listener to a component and fire the correct property change so
     * that the RetailEditorManager can pick it up and handle displaying exceptions appropriately.
     ***************************************************************************************************/
    protected FocusListener createManagerFocusListener() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
                firePropertyChange(UIPropertyName.EDITOR_FOCUS_GAINED, false, true);
            }

            public void focusLost(FocusEvent event) {
                firePropertyChange(UIPropertyName.EDITOR_FOCUS_LOST, true, false);
            }
        };
    }
}
