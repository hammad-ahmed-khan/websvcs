package oracle.retail.sim.client.swing.widget;

import java.awt.Color;
import javax.swing.JCheckBox;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.core.UIPermissionManager;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.config.NavigationPermission;
import oracle.retail.sim.common.core.locale.StringConstants;

/******************************************************************************************
 * This class sublcasses the standard JCheckBox class in the Swing package to provide custom
 * functionality for the Rcom client application. An arrow character in unicode is used to
 * indicate that the checkbox is currently focused, which is otherwise very difficult to see
 * in a standard JAVA implementation.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RCheckBox extends JCheckBox implements RetailComponent {
    private static final long serialVersionUID = 8419713018693295431L;

    private String identifier = StringConstants.EMPTY;
    private NavigationPermission permission = NavigationPermission.FULL;
    private UIPermissionManager permissionManager;
    private boolean isBackgroundPaintActivated = true;

    /******************************************************************************************
     * Returns new RCheckBox object.
     *****************************************************************************************/
    public RCheckBox() {
        initialize();
    }

    /******************************************************************************************
     * Return new RCheckBox object with an established display text.
     * <p>
     * @param text The label text to display near the check box.
     *****************************************************************************************/
    public RCheckBox(String text) {
        setText(text);
        initialize();
    }

    /******************************************************************************************
     * Initializes the default check box state.
     *****************************************************************************************/
    private void initialize() {
        setAlignment(LEFT, LEFT);
    }

    /******************************************************************************************
     * Assigns an identifier to the component. This is used in order to make setName() useable
     * by developers. This is the means by which the framework identifiers a component.
     * <p>
     * @param identifier The identifier to assign to the component.
     ******************************************************************************************/
    public void setIdentifier(String identifier) {
        if (identifier == null) {
            identifier = StringConstants.EMPTY;
        }
        this.identifier = identifier;
    }

    /******************************************************************************************
     * Retrieves the identifer to the component.
     * <p>
     * @return The identifier.
     ******************************************************************************************/
    public String getIdentifier() {
        return identifier;
    }

    /******************************************************************************************
     * Builds a check box with a specified alignement.
     * <p>
     *@param boxAlign The horizontal positioning of the check box.
     *@param textAlign The horizontal positioning of the label text
     *****************************************************************************************/
    public void setAlignment(int boxAlign, int textAlign) {
        setHorizontalAlignment(boxAlign);
        setHorizontalTextPosition(textAlign);
    }

    /******************************************************************************************
     * Sets the text to display within the check box. Automatic language translation occurs
     * for non-empty text.
     * <p>
     *@param text The text to display within the check box.
     *****************************************************************************************/
    public void setText(String text) {
        super.setText(Translator.getText(text));
    }

    /******************************************************************************************
     * Sets the font style for the checkbox.
     * <p>
     * @param style The style to assign to the font.
    /******************************************************************************************/
    public void setFontStyle(int style) {
        setFont(getFont().deriveFont(style));
    }

    /******************************************************************************************
     * Sets the font style and size for the checkbox.
     * <p>
     * @param style The style to assign to the font.
     * @param size The size to assign to the font.
    /******************************************************************************************/
    public void setFontStyle(int style, float size) {
        setFont(getFont().deriveFont(style, size));
    }

    /******************************************************************************************
     * Sets the font color, style and size for the checkbox.
     * <p>
     * @param color The color to assign to the foreground of the checkbox.
     * @param style The style to assign to the font.
     * @param size The size to assign to the font.
    /******************************************************************************************/
    public void setFontStyle(Color color, int style, float size) {
        setFont(getFont().deriveFont(style, size));
        setForeground(color);
    }

    /******************************************************************************************
     * If set to true, the check box will paint its background box, otherwise not. The initial
     * default value is true.
     /******************************************************************************************/
    public void setBackgroundPaintActivated(boolean activated) {
        isBackgroundPaintActivated = activated;
    }

    /******************************************************************************************
     * Returns true if the check box should paint its background box, false otherwise.
     /******************************************************************************************/
    public boolean isBackgroundPaintActivated() {
        return isBackgroundPaintActivated;
    }

    /******************************************************************************************
     * Overrides the superclass setVisible() to check permissions first.
     /******************************************************************************************/
    public void setVisible(boolean visible) {
        if (permission.equals(NavigationPermission.NONE)) {
            visible = false;
        }
        super.setVisible(visible);
    }

    /******************************************************************************************
     * Overrides the superclass setEnabled() to check permissions first.
     /******************************************************************************************/
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
        if (permissionManager == null) {
            permissionManager = new UIPermissionManager();
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
}
