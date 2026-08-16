package oracle.retail.sim.client.swing.editor;

import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;
import java.util.Currency;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.EmptyStateActionAdaptor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RMoneyField;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.MoneyMask;

/******************************************************************************************
 * This class represents a money field with a title label.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RMoneyFieldEditor extends AbstractEditor implements FocusListener, PropertyChangeListener {
    private static final long serialVersionUID = -4547927072076510200L;

    private JPanel innerPanel = new JPanel();
    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RMoneyField valueField = new RMoneyField();

    private EmptyStateActionAdaptor emptyAdaptor;
    private REventListener eventListener;
    private String eventCommand;
    private String lastTextValue;

    /******************************************************************************************
     * Creates a new RCurrencyFieldEditor with no title.
     ******************************************************************************************/
    public RMoneyFieldEditor() {
        initialize();
    }

    /******************************************************************************************
     * Creates a new RCurrencyFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     ******************************************************************************************/
    public RMoneyFieldEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /******************************************************************************************
     * Creates a new RCurrencyFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ******************************************************************************************/
    public RMoneyFieldEditor(String title, boolean required) {
        titleLabel.setText(title);
        setRequired(required);
        initialize();
    }

    /******************************************************************************************
     * Initializes the editor.
     ******************************************************************************************/
    private void initialize() {
        errorIcon = (ImageIcon) UIManager.getIcon(UIThemeName.ERROR_ALERT);
        errorLabel.setLockedSize(errorIcon.getIconWidth(), errorIcon.getIconHeight());
        errorLabel.setOpaque(false);
        spaceLabel.setOpaque(false);

        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setOpaque(false);

        titleLabel.setEnabled(false);

        valueField.addKeyListener(createErrorKeyListener());
        valueField.addFocusListener(this);
        valueField.addFocusListener(createManagerFocusListener());
        valueField.addPropertyChangeListener(this);

        validateTitle();

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
    }

    /******************************************************************************************
     * Retrieves the error state key listener.
     ******************************************************************************************/
    private KeyListener createErrorKeyListener() {
        return new KeyListener() {
            public void keyReleased(KeyEvent event) {
            }

            public void keyPressed(KeyEvent event) {
            }

            public void keyTyped(KeyEvent event) {
                if (isErrorState) {
                    setErrorState(false);
                }
            }
        };
    }

    /******************************************************************************************
     * Retrieves the label widget associated with this editor.
     * <p>
     * @param The label widget.
     ******************************************************************************************/
    public REditorLabel getLabel() {
        return titleLabel;
    }

    /******************************************************************************************
     * Retrieves the RCurrencyField object of the editor. Many helpful methods have been added to
     * the editor, but the developer can always retrieve the actual RCurrencyField widget to get
     * access to methods that are not provided.
     * <p>
     * @return The RCurrencyField widget.
     ******************************************************************************************/
    public RMoneyField getCurrencyField() {
        return valueField;
    }

    /******************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal
     * component rather than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ******************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        valueField.addKeyListener(listener);
    }

    /******************************************************************************************
     * This method is called when the identifer is altered in an editor. Subclasses should
     * override this for any desired custom functionality.
     ******************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        valueField.setIdentifier(identifier);
    }

    /******************************************************************************************
     * Retrieves the title of the editor.
     * <p>
     * @return The title.
     ******************************************************************************************/
    public String getTitle() {
        return titleLabel.getOriginalText();
    }

    /******************************************************************************************
     * Assigns the title to the editor.
     * <p>
     * @param title The title to assign.
     ******************************************************************************************/
    public void setTitle(String title) {
        if (title == null) {
            titleLabel.clear();
        } else {
            titleLabel.setText(title);
        }
        validateTitle();
    }

    /******************************************************************************************
     * Assigns the alignment of the title to the remainder of the editor. Valid alignments are
     * EditorConstants.LEFT, EditorConstants.RIGHT, EditorConstants.TOP, EditorConstants.BOTTOM.
     * The label suffix feature is turned off for all alignments except for LEFT.
     * <p>
     * @param alignment The alignment to assign.
     *****************************************************************************************/
    public void setTitleAlignment(int alignment) {
        titleLabel.setTitleAlignment(alignment);
        validateInnerLayout();
    }

    /******************************************************************************************
     * Retrieves the title alignment. This returns the integer that matches the title
     * alignment (see EditorConstants).
     * <p>
     * @return The title alignment.
     *****************************************************************************************/
    public int getTitleAlignment() {
        return titleLabel.getTitleAlignment();
    }

    /******************************************************************************************
     * Assigns whether or not the editor represents required information.
     * <p>
     * @param required True if the editor represents required information, false if not.
     *****************************************************************************************/
    public void setRequired(boolean required) {
        titleLabel.setRequired(required);
        markRequiredAssigned();
    }

    /******************************************************************************************
     * Retrieves whether or not the editor represents required information.
     * <p>
     * @return True if the editor represents required information, false if not.
     *****************************************************************************************/
    public boolean isRequired() {
        return titleLabel.isRequired();
    }

    /******************************************************************************************
     * Validates the layout of the editor.
     ******************************************************************************************/
    private void validateInnerLayout() {
        removeAll();

        validateInnerPanelLayout();

        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.TOP:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.LEFT:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 3, 0, 0, 0, 2));
                add(innerPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 5, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 0, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.BOTTOM:
                add(titleLabel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.RIGHT:
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 5, 1, 0, 0, 0, 0));
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 2, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RCurrencyFieldEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /******************************************************************************************
     * Validates the inner panel widget/error label.
     ******************************************************************************************/
    private void validateInnerPanelLayout() {
        innerPanel.removeAll();

        if (sizeType == -1) {
            switch (titleLabel.getTitleAlignment()) {
                case EditorConstants.RIGHT:
                    innerPanel.add(valueField, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
                default:
                    innerPanel.add(valueField, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
            }
            return;
        }
        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(valueField, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(valueField, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
        }
    }

    /******************************************************************************************
     * Validates the visible of the title.
     ******************************************************************************************/
    private void validateTitle() {
        if (isVisibleDenied()) {
            return;
        }
        titleLabel.setVisible(!StringUtility.isNullOrEmpty(titleLabel.getText()));
    }

    /******************************************************************************************
     * Assigns a minimum and preferred size to the editor. Values sizes include
     * EditorConstants.SMALL, EditorConstants.MEDIUM and EditorConstants.LARGE.
     * <p>
     *@param sizeType The size type (SMALL, MEDIUM, or LARGE).
     *****************************************************************************************/
    public void setSizeType(int sizeType) {
        switch (sizeType) {
            case EditorConstants.SMALL:
                setMinimumWidth(UIManager.getInt(UIThemeName.CURRENCYFIELD_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setMinimumWidth(UIManager.getInt(UIThemeName.CURRENCYFIELD_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setMinimumWidth(UIManager.getInt(UIThemeName.CURRENCYFIELD_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /******************************************************************************************
     * Sets the minimum and preferred width of the currency field.
     * <p>
     *@param width The width in pixels.
     *****************************************************************************************/
    public void setMinimumWidth(int width) {
        valueField.setMinimumWidth(width);
    }

    /******************************************************************************************
     * Sets the allowable text length based on the currency field name. This will hook through
     * the framework into a properties file that defines the appropriate length of the name.
     * <p>
     *@param name The name to assign to the currency field and look up the appropriate field size.
     *****************************************************************************************/
    public void setLength(String name) {
        valueField.setLength(name);
    }

    /******************************************************************************************
     * Sets the allowable text length. The currency field will only allow users to enter up to this
     * length in characters before it stops processing keystrokes.
     * <p>
     *@param allowedLength The length in characters that the currency field should allow.
     *****************************************************************************************/
    public void setLength(int length) {
        valueField.setLength(length);
    }

    /******************************************************************************************
     * Retrieves the maximum value allowed by the editr.
     * <p>
     * @return The maximum value.
     *****************************************************************************************/
    public BigDecimal getMaximumValue() {
        return getCurrencyMask().getMaximumValue();
    }

    /******************************************************************************************
     * Assigns the maximum value allowed by the editor. The default is 99999999999.99.
     * <p>
     * @param maxValue The maximum value.
     *****************************************************************************************/
    public void setMaximumValue(BigDecimal maxValue) {
        getCurrencyMask().setMaximumValue(maxValue);
    }

    /******************************************************************************************
     * Retrieves the minimum value allowed.
     * <p>
     * @return The minimum value.
     *****************************************************************************************/
    public BigDecimal getMinimumValue() {
        return getCurrencyMask().getMinimumValue();
    }

    /******************************************************************************************
     * Assigns the minimum value allowed by the editor. The default is -9999999999.99.
     * <p>
     * @param minValue The minimum value.
     *****************************************************************************************/
    public void setMinimumValue(BigDecimal minValue) {
        getCurrencyMask().setMinimumValue(minValue);
    }

    /******************************************************************************************
     * Retrieves whether or not a negative value is allowed.
     * <p>
     * @return True if a negative value is allowed, false otherwise.
     *****************************************************************************************/
    public boolean isNegativeValueAllowed() {
        return getCurrencyMask().isNegativeValueAllowed();
    }

    /******************************************************************************************
     * Assigns whether or not a negative value is allowed.
     * <p>
     * @param allowed True if a negative value is allowed, false otherwise.
     *****************************************************************************************/
    public void setNegativeValueAllowed(boolean allowed) {
        getCurrencyMask().setNegativeValueAllowed(allowed);
    }

    /******************************************************************************************
     * Retrieves the mask of the value field as a CurrencyMask.
     * <p>
     * @return The currency mask.
     *****************************************************************************************/
    private MoneyMask getCurrencyMask() {
        return (MoneyMask) valueField.getMask();
    }

    /******************************************************************************************
     * Retrieves the text string from the currency field and returns the unformatted and trimmed
     * version of the text.
     * <p>
     * @return The unformatted and trimmed text in the currency field.
     *****************************************************************************************/
    public String getText() {
        return valueField.getText();
    }

    /******************************************************************************************
     * Assigns the text in the field. The text will be formatted if a mask exists. Text longer
     * than the allowed length is, interestingly, not allowed.
     * <p>
     * @param text The text to place in the currency field. This should represent an amount.
     *****************************************************************************************/
    public void setText(String text) {
        valueField.setText(text);
        if (isErrorState) {
            setErrorState(false);
        }
    }

    /******************************************************************************************
     * Assigns the amount to the field. In conjunction with the currency, this will create
     * the correctly formatted text.
     * <p>
     * @param amount The amount of the money.
     *****************************************************************************************/
    public void setAmount(BigDecimal amount) {
        valueField.setAmount(amount);
        if (isErrorState) {
            setErrorState(false);
        }
    }

    /******************************************************************************************
     * Retrieves the amount of the money from the editor.
     * <p>
     * @return The amount.
     * @throws OldUIException Thrown if the field does not contain a valid money value.
     *****************************************************************************************/
    public BigDecimal getAmount() throws UIException {
        return valueField.getAmount();
    }

    /***********************************************************************************
     * Assigns currency to the mask.
     * <p>
     * @param currency The currency to assign.
     ************************************************************************************/
    public void setCurrency(Currency currency) {
        valueField.setCurrency(currency);
    }

    /***********************************************************************************
     * Returns the currency assigned to the mask.
     * <p>
     * @return The currency
     ************************************************************************************/
    public Currency getCurrency() {
        return valueField.getCurrency();
    }

    /******************************************************************************************
     * Clears the currency field of input.
     *****************************************************************************************/
    public void clear() {
        valueField.clear();
    }

    /******************************************************************************************
     * Retrieves whether or not the currency field is empty.
     * <p>
     * @return True if the currency field is empty, false if not.
     *****************************************************************************************/
    public boolean isEmpty() {
        return valueField.isEmpty();
    }

    /******************************************************************************************
     * Override the setVisible() of JPanel to call through to all widgets.
     *****************************************************************************************/
    public void setVisible(boolean visible) {
        if (isVisibleDenied()) {
            visible = false;
        }
        titleLabel.setVisible(visible);
        innerPanel.setVisible(visible);
    }

    /******************************************************************************************
     * Override the setEnabled() of JPanel to call through to the label and to the RCurrencyField.
     *****************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        titleLabel.setEnabled(enabled);
        valueField.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        if (isEnabledDenied()) {
            fieldEnabled = false;
        }
        titleLabel.setEnabled(labelEnabled);
        valueField.setEnabled(fieldEnabled);
    }

    /******************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     *****************************************************************************************/
    public boolean isEnabled() {
        return valueField.isEnabled();
    }

    /******************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     *****************************************************************************************/
    public void setErrorState(boolean errorState) {
        setErrorState(errorState, StringConstants.EMPTY);
    }

    /******************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     * @param errorText The text to display along with the editor.
     *****************************************************************************************/
    public void setErrorState(boolean errorState, String errorText) {
        isErrorState = errorState;

        if (isErrorState) {
            errorLabel.setIcon(errorIcon);
            errorLabel.setToolTipText(errorText);
        } else {
            errorLabel.setIcon(null);
            errorLabel.setToolTipText(StringConstants.EMPTY);
        }
    }

    /******************************************************************************************
     * Retrieves whethor or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     *****************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /******************************************************************************************
     * Registers to receive actions from a widget when keystrokes occur in the widget. This
     * sends the commands UIPropertyName.TEXT_COMPONENT_NOT_EMPTY or
     * UIPropertyName.TEXT_COMPONENT_NOT_EMPTY, when the widget changes state.
     * <p>
     * @param listener The object that should receive the RActionEvent.
     ******************************************************************************************/
    public void registerEmptyStateAction(REventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("REventListener cannot be null!");
        }
        EmptyStateActionAdaptor adaptor = getEmptyStateKeyListener();
        adaptor.registerListener(listener, getIdentifier());
        valueField.addKeyListener(adaptor);
    }

    /******************************************************************************************
     * Removes an empty state from the editor. An action will no longer be triggered for this
     * widget when keys are pressed.
     ******************************************************************************************/
    public void unregisterEmptyStateAction() {
        valueField.removeKeyListener(getEmptyStateKeyListener());
    }

    /******************************************************************************************
     * Retrieves the empty state key listener.
     ******************************************************************************************/
    private EmptyStateActionAdaptor getEmptyStateKeyListener() {
        if (emptyAdaptor == null) {
            emptyAdaptor = new EmptyStateActionAdaptor() {
                public int getTextLength() {
                    return valueField.getText().length();
                }

                public int getSelectedTextLength() {
                    String text = valueField.getSelectedText();
                    if (text != null) {
                        return text.length();
                    }
                    return 0;
                }
            };
        }
        return emptyAdaptor;
    }

    /******************************************************************************************
     * Registers an action with the editor. When the focus is lost and the state of the text
     * field has changed, the command will be sent back to the registered listener.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the noficiation.
     *****************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("Listener and Command parameters cannot be null!");
        }
        eventListener = listener;
        eventCommand = command;
    }

    /******************************************************************************************
     * Implements the focus gained method to store the text within the widget as a comparative
     * point to when the focus is lost.
     *****************************************************************************************/
    public void focusGained(FocusEvent event) {
        lastTextValue = valueField.getText();
    }

    /******************************************************************************************
     * Implements the focus lost method to compare the current text to the starting point and
     * trigger an action if it has changed.
     *****************************************************************************************/
    public void focusLost(FocusEvent event) {
        BusinessException exception = valueField.getMask().validate(getText());
        if (exception != null) {
            if (!isErrorState) {
                displayException(exception);
                setErrorState(true, exception.getLocalizedMessage());
                requestFocusInWindow();
            }
            return;
        }
        if (isActionEnabled && eventListener != null && eventCommand != null) {
            String value = valueField.getText();
            if (!lastTextValue.equals(value)) {
                RActionEvent actionEvent = new RActionEvent(this, eventCommand);
                actionEvent.setNextFocusComponent(event.getOppositeComponent());
                actionEvent.setEventText(value);

                eventListener.performActionEvent(actionEvent);
            }
        }
    }

    /******************************************************************************************
     * Implements the property change listener method to disable and enable the title along
     * with the actual currency field.
     *****************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String name = event.getPropertyName();
        if (name.equals(UIPropertyName.TEXT_COMPONENT_ENABLED)) {
            titleLabel.setEnabled(((Boolean) event.getNewValue()).booleanValue());
        } else if (name.equals(UIPropertyName.TEXT_COMPONENT_PASTE) && isErrorState) {
            setErrorState(false);
        }
    }

    /******************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     *****************************************************************************************/
    public boolean isFocusOwner() {
        return valueField.isFocusOwner();
    }

    /******************************************************************************************
     * Requests the focus move the currency field within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely
     * to succeed.
     *****************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueField.requestFocusInWindow();
    }

    /******************************************************************************************
     * Handles displaying a business exception on the application.
     *****************************************************************************************/
    protected void displayException(BusinessException exception) {
        UIStatusUtility.displayException(valueField, exception);
    }

    /******************************************************************************************
     * Handles displaying an exception on the application.
     *****************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(valueField, exception);
    }
}
