package oracle.retail.sim.client.swing.editor;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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
import oracle.retail.sim.client.swing.widget.RScrollPane;
import oracle.retail.sim.client.swing.widget.RTextArea;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * This class represents a text area with a title label.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RTextAreaEditor extends AbstractEditor implements FocusListener, PropertyChangeListener {
    private static final long serialVersionUID = -131715317067083161L;

    private JPanel innerPanel = new JPanel();
    private REditorLabel titleLabel = new RPlainEditorLabel();
    private RTextArea valueArea = new RTextArea();
    private RScrollPane scrollPane = new RScrollPane(valueArea);

    private EmptyStateActionAdaptor emptyAdaptor;
    private REventListener eventListener;
    private String eventCommand;
    private String lastTextValue;
    private boolean cursorToFront = true;

    /****************************************************************************************************
     * Creates a new RTextAreaEditor with no title.
     ***************************************************************************************************/
    public RTextAreaEditor() {
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RTextAreaEditor with a title.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public RTextAreaEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RTextAreaEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RTextAreaEditor(String title, boolean required) {
        titleLabel.setText(title);
        setRequired(required);
        initialize();
    }

    /****************************************************************************************************
     * Initializes the editor.
     ***************************************************************************************************/
    private void initialize() {
        errorIcon = (ImageIcon) UIManager.getIcon(UIThemeName.ERROR_ALERT);
        errorLabel.setLockedSize(errorIcon.getIconWidth(), errorIcon.getIconHeight());
        errorLabel.setOpaque(false);

        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setOpaque(false);

        titleLabel.setEnabled(false);

        valueArea.addPropertyChangeListener(this);
        valueArea.addKeyListener(createErrorKeyListener());
        valueArea.addFocusListener(createManagerFocusListener());

        validateTitle();

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
    }

    /****************************************************************************************************
     * Retrieves the error state key listener.
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Retrieves the label widget associated with this editor.
     * <p>
     * @param The label widget.
     ***************************************************************************************************/
    public REditorLabel getLabel() {
        return titleLabel;
    }

    /****************************************************************************************************
     * Retrieves the RTextArea object of the editor. Many helpful methods have been added to the editor,
     * but the developer can always retrieve the actual RTextArea widget to get access to methods that
     * are not provided.
     * <p>
     * @return The RTextArea widget.
     ***************************************************************************************************/
    public RTextArea getTextArea() {
        return valueArea;
    }

    /****************************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal component rather
     * than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ***************************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        valueArea.addKeyListener(listener);
    }

    /****************************************************************************************************
     * This method is called when the identifer is altered in an editor. It passes the identifier on to
     * the actual value area.
     ***************************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        valueArea.setIdentifier(identifier);
    }

    /****************************************************************************************************
     * Retrieves the title of the editor.
     * <p>
     * @return The title.
     ***************************************************************************************************/
    public String getTitle() {
        return titleLabel.getOriginalText();
    }

    /****************************************************************************************************
     * Assigns the title to the editor.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public void setTitle(String title) {
        if (title == null) {
            titleLabel.clear();
        } else {
            titleLabel.setText(title);
        }
        validateTitle();
    }

    /****************************************************************************************************
     * Assigns the alignment of the title to the remainder of the editor. Valid alignments are
     * EditorConstants.LEFT, EditorConstants.RIGHT, EditorConstants.TOP, EditorConstants.BOTTOM. The
     * label suffix feature is turned off for all alignments except for LEFT.
     * <p>
     * @param alignment The alignment to assign.
     ***************************************************************************************************/
    public void setTitleAlignment(int alignment) {
        titleLabel.setTitleAlignment(alignment);
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Retrieves the title alignment. This returns the integer that matches the title alignment (see
     * EditorConstants).
     * <p>
     * @return The title alignment.
     ***************************************************************************************************/
    public int getTitleAlignment() {
        return titleLabel.getTitleAlignment();
    }

    /****************************************************************************************************
     * Assigns whether or not the editor represents required information.
     * <p>
     * @param required True if the editor represents required information, false if not.
     ***************************************************************************************************/
    public void setRequired(boolean required) {
        titleLabel.setRequired(required);
        markRequiredAssigned();
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor represents required information.
     * <p>
     * @return True if the editor represents required information, false if not.
     ***************************************************************************************************/
    public boolean isRequired() {
        return titleLabel.isRequired();
    }

    /****************************************************************************************************
     * Validates the layout of the editor.
     ***************************************************************************************************/
    private void validateInnerLayout() {
        removeAll();

        validateInnerPanelLayout();

        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.TOP:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1));
                add(innerPanel, GridTool.constraints(0, 1, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.LEFT:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 3, 0, 0, 1, 0));
                add(innerPanel, GridTool.constraints(1, 0, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.BOTTOM:
                add(titleLabel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 1));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.RIGHT:
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 0, 1, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RTextAreaEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /****************************************************************************************************
     * Validates the inner panel widget/error label.
     ***************************************************************************************************/
    private void validateInnerPanelLayout() {
        innerPanel.removeAll();

        if (sizeType == -1) {
            switch (titleLabel.getTitleAlignment()) {
                case EditorConstants.RIGHT:
                    innerPanel.add(scrollPane, GridTool.constraints(1, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
                default:
                    innerPanel.add(scrollPane, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
            }
            return;
        }
        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(scrollPane, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(scrollPane, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 3, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(2, 0, 1, 1, 1, 1, 0, 3, 0, 0, 0, 0));
                break;
        }
    }

    /****************************************************************************************************
     * Validates the visible of the title.
     ***************************************************************************************************/
    private void validateTitle() {
        if (isVisibleDenied()) {
            return;
        }
        titleLabel.setVisible(!StringUtility.isNullOrEmpty(titleLabel.getText()));
    }

    /****************************************************************************************************
     * Assigns a minimum and preferred size to the editor. Values sizes include EditorConstants.SMALL,
     * EditorConstants.MEDIUM and EditorConstants.LARGE.
     * <p>
     * @param sizeType The size type (SMALL, MEDIUM, or LARGE).
     ***************************************************************************************************/
    public void setSizeType(int sizeType) {
        switch (sizeType) {
            case EditorConstants.SMALL:
                setAreaSize(UIManager.getInt(UIThemeName.TEXTAREA_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setAreaSize(UIManager.getInt(UIThemeName.TEXTAREA_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setAreaSize(UIManager.getInt(UIThemeName.TEXTAREA_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Assigns a size to the text area.
     * <p>
     * @return The number of pixels to assign as the dimensions of the area
     ***************************************************************************************************/
    private void setAreaSize(int size) {
        scrollPane.setMinimumSize(new Dimension(size, size));
        scrollPane.setPreferredSize(new Dimension(size, size));
    }

    /****************************************************************************************************
     * Retrieves the vertical weight of the editor (used with REditorPanel)
     * <p>
     * @return The vertical weight of the editor.
     ***************************************************************************************************/
    public int getVerticalWeight() {
        return 1;
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
        return 3;
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width of the text area.
     * <p>
     * @param width The width in pixels.
     ***************************************************************************************************/
    public void setMinimumWidth(int width) {
        valueArea.setMinimumWidth(width);
    }

    /****************************************************************************************************
     * Sets the minimum and preferred height of the text area.
     * <p>
     * @param width The height in pixels.
     ***************************************************************************************************/
    public void setMinimumHeight(int height) {
        setMinimumSize(new Dimension(0, height));
        setPreferredSize(new Dimension(0, height));
    }

    /****************************************************************************************************
     * Sets the allowable text length based on the text area name. This will hook through the framework
     * into a properties file that defines the appropriate length of the name.
     * <p>
     * @param name The name to assign to the text area and look up the appropriate field size.
     ***************************************************************************************************/
    public void setLength(String name) {
        valueArea.setLength(name);
    }

    /****************************************************************************************************
     * Sets the allowable text length. The text area will only allow users to enter up to this length in
     * characters before it stops processing keystrokes.
     * <p>
     * @param allowedLength The length in characters that the text area should allow.
     ***************************************************************************************************/
    public void setLength(int length) {
        valueArea.setLength(length);
    }

    /****************************************************************************************************
     * Returns true if the default cursor text position is at the beginning of the text (after setText()
     * is called.
     * <p>
     * @return true if default position is beginning, false if end.
     ***************************************************************************************************/
    public void setDefaultCursorPositionToFront(boolean cursorToFront) {
        this.cursorToFront = cursorToFront;
        setText(getText());
    }

    /****************************************************************************************************
     * Returns true if the default cursor text position is at the beginning of the text (after setText()
     * is called.
     * <p>
     * @return true if default position is beginning, false if end.
     ***************************************************************************************************/
    public boolean isDefaultCursorPositionToFront() {
        return cursorToFront;
    }

    /****************************************************************************************************
     * Retrieves the text string from the text area and returns the unformatted and trimmed version of
     * the text.
     * <p>
     * @return The unformatted and trimmed text in the text area.
     ***************************************************************************************************/
    public String getText() {
        return valueArea.getText();
    }

    /****************************************************************************************************
     * Assigns the text in the area. The text will be formatted if a mask exists. Text longer than the
     * allowed length is, interestingly, not allowed.
     * <p>
     * @param text The text to place in the text area.
     ***************************************************************************************************/
    public void setText(String text) {
        valueArea.setText(text);
        if (isErrorState) {
            setErrorState(false);
        }
        if (cursorToFront) {
            valueArea.setCaretPosition(0);
        }
    }

    /****************************************************************************************************
     * Appends text to the text area.
     ***************************************************************************************************/
    public void append(String text) {
        valueArea.append(text);
        if (isErrorState) {
            setErrorState(false);
        }
    }

    /****************************************************************************************************
     * Clears the text area of input.
     ***************************************************************************************************/
    public void clear() {
        valueArea.clear();
    }

    /****************************************************************************************************
     * Retrieves whether or not the text area is empty.
     * <p>
     * @return True if the text area is empty, false if not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return valueArea.isEmpty();
    }

    /****************************************************************************************************
     * Override the setVisible() to validate against permissions first.
     ***************************************************************************************************/
    public void setVisible(boolean visible) {
        if (isVisibleDenied()) {
            visible = false;
        }
        titleLabel.setVisible(visible);
        innerPanel.setVisible(visible);
    }

    /****************************************************************************************************
     * Override the setEnabled() of JPanel to call through to the label and to the RTextArea. It also
     * validates the text rea permission allows it to be enabled.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        titleLabel.setEnabled(enabled);
        valueArea.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        if (isEnabledDenied()) {
            fieldEnabled = false;
        }
        titleLabel.setEnabled(labelEnabled);
        valueArea.setEnabled(fieldEnabled);
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     ***************************************************************************************************/
    public boolean isEnabled() {
        return valueArea.isEnabled();
    }

    /****************************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState) {
        setErrorState(errorState, StringConstants.EMPTY);
    }

    /****************************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param errorState True if editor should be in error state, false if not.
     * @param errorText The text to display along with the editor.
     ***************************************************************************************************/
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

    /****************************************************************************************************
     * Retrieves whethor or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     ***************************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /****************************************************************************************************
     * Registers to receive actions from a widget when keystrokes occur in the widget. This sends the
     * commands UIPropertyName.TEXT_COMPONENT_NOT_EMPTY or UIPropertyName.TEXT_COMPONENT_NOT_EMPTY, when
     * the widget changes state.
     * <p>
     * @param listener The object that should receive the RActionEvent.
     ***************************************************************************************************/
    public void registerEmptyStateAction(REventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("REventListener cannot be null!");
        }
        EmptyStateActionAdaptor adaptor = getEmptyStateKeyListener();
        adaptor.registerListener(listener, getIdentifier());
        valueArea.addKeyListener(adaptor);
    }

    /****************************************************************************************************
     * Removes an empty state from the editor. An action will no longer be triggered for this widget when
     * keys are pressed.
     ***************************************************************************************************/
    public void unregisterEmptyStateAction() {
        valueArea.removeKeyListener(getEmptyStateKeyListener());
    }

    /****************************************************************************************************
     * Retrieves the empty state key listener.
     ***************************************************************************************************/
    private EmptyStateActionAdaptor getEmptyStateKeyListener() {
        if (emptyAdaptor == null) {
            emptyAdaptor = new EmptyStateActionAdaptor() {
                public int getTextLength() {
                    return valueArea.getText().length();
                }

                public int getSelectedTextLength() {
                    String text = valueArea.getSelectedText();
                    if (text != null) {
                        return text.length();
                    }
                    return 0;
                }
            };
        }
        return emptyAdaptor;
    }

    /****************************************************************************************************
     * Registers an action with the editor. When the focus is lost and the state of the text field has
     * changed, the command will be sent back to the registered listener.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the noficiation.
     ***************************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("Listener and Command parameters cannot be null!");
        }
        eventListener = listener;
        eventCommand = command;

        valueArea.addFocusListener(this);
    }

    /****************************************************************************************************
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     ***************************************************************************************************/
    public boolean isFocusOwner() {
        return valueArea.isFocusOwner();
    }

    /****************************************************************************************************
     * Implements the focus gained method to store the text within the widget as a comparative point to
     * when the focus is lost.
     ***************************************************************************************************/
    public void focusGained(FocusEvent event) {
        lastTextValue = valueArea.getText();
    }

    /****************************************************************************************************
     * Implements the focus lost method to compare the current text to the starting point and trigger an
     * action if it has changed.
     ***************************************************************************************************/
    public void focusLost(FocusEvent event) {
        if (isActionEnabled && eventListener != null && eventCommand != null) {
            String value = valueArea.getText();
            if (!StringUtility.isEqual(lastTextValue, value)) {
                RActionEvent actionEvent = new RActionEvent(this, eventCommand);
                actionEvent.setNextFocusComponent(event.getOppositeComponent());
                actionEvent.setEventText(value);

                eventListener.performActionEvent(actionEvent);
            }
        }
    }

    /****************************************************************************************************
     * Implements the property change listener method to disable and enable the title along with the
     * actual text field.
     ***************************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String name = event.getPropertyName();
        if (name.equals(UIPropertyName.TEXT_COMPONENT_ENABLED)) {
            titleLabel.setEnabled(((Boolean) event.getNewValue()).booleanValue());
        } else if (name.equals(UIPropertyName.TEXT_COMPONENT_PASTE) && isErrorState) {
            setErrorState(false);
        }
    }

    /****************************************************************************************************
     * Requests the focus move the text area within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely to succeed.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueArea.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Handles displaying an exception on the application.
     ***************************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(valueArea, exception);
    }
}
