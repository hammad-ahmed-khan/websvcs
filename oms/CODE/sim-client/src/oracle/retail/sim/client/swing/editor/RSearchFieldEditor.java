package oracle.retail.sim.client.swing.editor;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.EmptyStateActionAdaptor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.RErrorEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RButton;
import oracle.retail.sim.client.swing.widget.RTextField;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.logging.LogService;

/********************************************************************************************************
 * This class represents an entry text field and description display field with a title label and a
 * search button.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RSearchFieldEditor extends AbstractEditor implements FocusListener, PropertyChangeListener, REventListener, SearchReceiver {
    private static final long serialVersionUID = 8439269063743665919L;

    private int entryWidth = 100;
    private int buttonWidth = 20;

    private JPanel innerPanel = new JPanel();
    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RTextField entryField = new RTextField();
    private RButton searchButton = new RButton();
    private RTextField valueField = new RTextField();

    private EmptyStateActionAdaptor emptyAdaptor;
    private REventListener eventListener;
    private int eventKeyCode = -1;
    private String eventCommand;
    private String lastTextValue;
    private boolean cursorToFront = true;

    private SearchProcessor searchProcessor;
    private SearchListener searchListener;
    private static final String SEARCH_TRIGGER = "RSearchFieldEditor.search";

    private Object editorValue;

    private Map valueCache = new HashMap(7);

    /****************************************************************************************************
     * Creates a new RSearchFieldEditor with no title.
     ***************************************************************************************************/
    public RSearchFieldEditor() {
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RSearchFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public RSearchFieldEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /****************************************************************************************************
     * Creates a new RSearchFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RSearchFieldEditor(String title, boolean required) {
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
        spaceLabel.setOpaque(false);

        innerPanel.setLayout(new GridBagLayout());
        innerPanel.setOpaque(false);

        titleLabel.setEnabled(true);

        entryWidth = UIManager.getInt(UIThemeName.RSEARCHFIELD_ENTRY_WIDTH);
        entryField.setMinimumWidth(entryWidth);
        entryField.addKeyListener(new InternalKeyListener(this));
        entryField.addPropertyChangeListener(this);
        entryField.addFocusListener(this);
        entryField.addFocusListener(createManagerFocusListener());

        validateTitle();

        buttonWidth = UIManager.getInt(UIThemeName.RSEARCHFIELD_BUTTON_WIDTH);
        searchButton.setText(UIManager.getString(UIThemeName.RSEARCHFIELD_BUTTON_LABEL));
        searchButton.setLockedSize(buttonWidth, entryField.getPreferredSize().height);
        searchButton.registerAction(this, SEARCH_TRIGGER);

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
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
     * Retrieves the RTextField object of the editor. Many helpful methods have been added to the editor,
     * but the developer can always retrieve the actual RTextfield widget to get access to methods that
     * are not provided. This is the entry portion of the editor.
     * <p>
     * @return The RTextField widget.
     ***************************************************************************************************/
    public RTextField getTextField() {
        return entryField;
    }

    /****************************************************************************************************
     * Retrieves the RTextField object of the editor. Many helpful methods have been added to the editor,
     * but the developer can always retrieve the actual RTextfield widget to get access to methods that
     * are not provided. This is the value portion of the editor.
     * <p>
     * @return The RTextField widget.
     ***************************************************************************************************/
    public RTextField getValueTextField() {
        return valueField;
    }

    /****************************************************************************************************
     * Retrieves the search button object of the editor. Many helpful methods have been added to the
     * editor, but the developer can always retrieve the actual RButton widget to get access to methods
     * that are not provided.
     * <p>
     * @return The RButton widget.
     ***************************************************************************************************/
    public RButton getSearchButton() {
        return searchButton;
    }

    /****************************************************************************************************
     * Overrides the superclass addKeyListener() to add the key listener to the internal component rather
     * than the editor itself.
     * @param listener The key listener (no action is taken if the listener is null).
     ***************************************************************************************************/
    public void addKeyListener(KeyListener listener) {
        entryField.addKeyListener(listener);
        searchButton.addKeyListener(listener);
    }

    /****************************************************************************************************
     * This method is called when the identifier is altered in an editor. Subclasses should override this
     * for any desired custom functionality.
     ***************************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        entryField.setIdentifier(identifier);
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
    protected void validateInnerLayout() {
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
                throw new IllegalArgumentException("Invalid RTextFieldEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /****************************************************************************************************
     * Validates the layout of the inner panel.
     ***************************************************************************************************/
    private void validateInnerPanelLayout() {
        innerPanel.removeAll();

        if (sizeType == -1) {
            switch (titleLabel.getTitleAlignment()) {
                case EditorConstants.RIGHT:
                    innerPanel.add(entryField, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(searchButton, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    innerPanel.add(valueField, GridTool.constraints(3, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
                default:
                    innerPanel.add(entryField, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(searchButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    innerPanel.add(valueField, GridTool.constraints(2, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
            }
            return;
        }
        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(entryField, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(searchButton, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(valueField, GridTool.constraints(4, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(entryField, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(searchButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(valueField, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(4, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
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
            case EditorConstants.TINY:
                setMinimumWidth(UIManager.getInt(UIThemeName.TEXTFIELD_TINY));
                break;
            case EditorConstants.SMALL:
                setMinimumWidth(UIManager.getInt(UIThemeName.TEXTFIELD_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setMinimumWidth(UIManager.getInt(UIThemeName.TEXTFIELD_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setMinimumWidth(UIManager.getInt(UIThemeName.TEXTFIELD_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width of the text field.
     * <p>
     * @param width The width in pixels.
     ***************************************************************************************************/
    public void setMinimumWidth(int width) {
        valueField.setMinimumWidth(width - buttonWidth - entryWidth);
    }

    /****************************************************************************************************
     * Sets the allowable text length based on the text field name. This will hook through the framework
     * into a properties file that defines the appropriate length of the name.
     * <p>
     * @param name The name to assign to the text field and look up the appropriate field size.
     ***************************************************************************************************/
    public void setLength(String name) {
        entryField.setLength(name);
    }

    /****************************************************************************************************
     * Sets the allowable text length. The text field will only allow users to enter up to this length in
     * characters before it stops processing keystrokes.
     * <p>
     * @param allowedLength The length in characters that the text field should allow.
     ***************************************************************************************************/
    public void setLength(int length) {
        entryField.setLength(length);
    }

    /****************************************************************************************************
     * Returns true if the default cursor text position is at the beginning of the text (after setText()
     * is called.
     * <p>
     * @return true if default position is beginning, false if end.
     ***************************************************************************************************/
    public void setDefaultCursorPositionToFront(boolean cursorToFront) {
        this.cursorToFront = cursorToFront;
        setEntryText(entryField.getText());
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
     * Assigns the search listener interested in when the search button is processed.
     * <p>
     * @param listener The search listener.
     ***************************************************************************************************/
    public void setSearchListener(SearchListener listener) {
        searchListener = listener;
    }

    /****************************************************************************************************
     * Assigns the search processor that handles the search by id aspect of the editor.
     * <p>
     * @param processor The search processor.
     ***************************************************************************************************/
    public void setSearchProcessor(SearchProcessor processor) {
        searchProcessor = processor;
    }

    /****************************************************************************************************
     * Listen for search trigger and call appropriate method.
     ***************************************************************************************************/
    public void performErrorEvent(RErrorEvent event) {
    }

    public void performActionEvent(RActionEvent event) {
        if (event.getEventCommand().equals(SEARCH_TRIGGER) && searchListener != null) {
            searchListener.search();
        }
    }

    /****************************************************************************************************
     * Assigns the data object that this editor represents.
     ***************************************************************************************************/
    public synchronized void setData(Object value) {
        try {
            value = searchProcessor.validateData(value);
        } catch (Throwable exception) {
            displayException(exception);
            requestFocusInWindow();
            value = null;
        }
        editorValue = value;
        cacheEditorValue();
        displayValue();
        fireDataChangedAction();
    }

    /****************************************************************************************************
     * Retrieves the data that the search editor represents
     ***************************************************************************************************/
    public synchronized Object getData() {
        return editorValue;
    }

    /****************************************************************************************************
     * Helper method to display the data.
     ***************************************************************************************************/
    private void displayValue() {
        if (editorValue == null) {
            clear();
            return;
        }

        BasicDisplayer displayer = searchProcessor.getEntryDisplayer();

        if (displayer != null) {
            setEntryText(displayer.getDisplayText(editorValue));
        } else {
            setEntryText(StringConstants.EMPTY);
        }

        displayer = searchProcessor.getValueDisplayer();

        if (displayer != null) {
            valueField.setText(displayer.getDisplayText(editorValue));
            valueField.setCaretPosition(0);
        } else {
            valueField.setText(String.valueOf(editorValue));
            valueField.setCaretPosition(0);
        }
    }

    /****************************************************************************************************
     * Retrieves the text in the entry portion of the search field.
     ***************************************************************************************************/
    public String getText() {
        return entryField.getText();
    }

    /****************************************************************************************************
     * Assigns the text in the field. The text will be formatted if a mask exists. Text longer than the
     * allowed length is, interestingly, not allowed.
     * <p>
     * @param text The text to place in the text field.
     ***************************************************************************************************/
    public void setText(String text) {
        setEntryText(text);
        if (cursorToFront) {
            entryField.setCaretPosition(0);
        }
        performSearch(text);
    }

    /****************************************************************************************************
     * Internal set text to avoid permanent searches
     ***************************************************************************************************/
    private void setEntryText(String text) {
        entryField.setText(text);
        if (isErrorState) {
            setErrorState(false);
        }
        lastTextValue = text;
    }

    /****************************************************************************************************
     * Clears the text field of input.
     ***************************************************************************************************/
    public void clear() {
        editorValue = null;
        entryField.clear();
        valueField.clear();
    }

    /****************************************************************************************************
     * Retrieves whether or not the text field is empty.
     * <p>
     * @return True if the text field is empty, false if not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return entryField.isEmpty();
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
     * Override the setEnabled() of JPanel to call through to the label and to the RTextField. It also
     * validates the text field permission allows it to be enabled.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        titleLabel.setEnabled(enabled);
        entryField.setEnabled(enabled);
        searchButton.setEnabled(enabled);
    }

    /****************************************************************************************************
     * Override the setEnabled() of this class to call through to the label only.
     ***************************************************************************************************/
    public void setEnabled(boolean labelEnabled, boolean fieldEnabled) {
        if (isEnabledDenied()) {
            fieldEnabled = false;
        }
        titleLabel.setEnabled(labelEnabled);
        entryField.setEnabled(fieldEnabled);
        searchButton.setEnabled(fieldEnabled);
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor is enabled.
     * <p>
     * @return True if the editor is enabled, false otherwise.
     ***************************************************************************************************/
    public boolean isEnabled() {
        return entryField.isEnabled();
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
     * Retrieves whether or not the editor is in error state.
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
        entryField.addKeyListener(adaptor);
    }

    /****************************************************************************************************
     * Removes an empty state from the editor. An action will no longer be triggered for this widget when
     * keys are pressed.
     ***************************************************************************************************/
    public void unregisterEmptyStateAction() {
        entryField.removeKeyListener(getEmptyStateKeyListener());
    }

    /****************************************************************************************************
     * Retrieves the empty state key listener.
     ***************************************************************************************************/
    private EmptyStateActionAdaptor getEmptyStateKeyListener() {
        if (emptyAdaptor == null) {
            emptyAdaptor = new EmptyStateActionAdaptor() {
                public int getTextLength() {
                    return entryField.getText().length();
                }

                public int getSelectedTextLength() {
                    String text = entryField.getSelectedText();
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
     * Registers an action with the editor. When the data within the search editor is altered, the
     * listener will be informed with the command. The new data will be sent inside the action.
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the notification.
     ***************************************************************************************************/
    public void registerAction(REventListener listener, String command) {
        if (listener == null || command == null) {
            throw new IllegalArgumentException("Listener and Command parameters cannot be null!");
        }
        eventListener = listener;
        eventCommand = command;
    }

    /****************************************************************************************************
     * Registers an action with the editor. When the data within the search editor is altered, the
     * listener will be informed with the command. The new data will be sent inside the action. The key
     * code is defined in KeyEvent.Something (ex: KeyEvent.VK_ENTER);
     * <p>
     * @param listener The listener to notify.
     * @param command The command to send in the notification.
     * @param keyCode The key that if pressed in the field, will trigger the action.
     ***************************************************************************************************/
    public void registerAction(REventListener listener, String command, int keyCode) {
        registerAction(listener, command);
        eventKeyCode = keyCode;
    }

    /****************************************************************************************************
     * Notify listener that data has been altered.
     * Note: Recently altered to send out null values. Listeners should be just as interested when the
     * field is cleared as they are when it is populated.
     ***************************************************************************************************/
    private void fireDataChangedAction() {
        if (isActionEnabled && isEnabled() && eventListener != null && !StringUtility.isNullOrEmpty(eventCommand)) {
            eventListener.performActionEvent(new RActionEvent(this, eventCommand, editorValue));
        }
    }

    /****************************************************************************************************
     * Implements the focus gained method to store the text within the widget as a comparative point to
     * when the focus is lost.
     ***************************************************************************************************/
    public void focusGained(FocusEvent event) {
        lastTextValue = entryField.getText();
    }

    /****************************************************************************************************
     * Implements the focus lost method to compare the current text to the starting point and trigger an
     * action if it has changed.
     ***************************************************************************************************/
    public void focusLost(FocusEvent event) {
        processEventAction(event.getOppositeComponent());
    }

    /****************************************************************************************************
     * Process the event action of the editor. Will only trigger an event if a listener and command are
     * assigned.
     * <p>
     * @param oppositeComponent The component to assign as the action event next focus component.
     ***************************************************************************************************/
    private void processEventAction(Component oppositeComponent) {
        if (oppositeComponent != null) {
            String value = entryField.getText();
            if (StringUtility.isEqual(lastTextValue, value)) {
                return;
            }
            lastTextValue = value;
            if (searchListener != null) {
                performSearch(value);
            }
        }
    }

    /****************************************************************************************************
     * Attempts to find the data object based on the text that was entered in the entry field.
     ***************************************************************************************************/
    private boolean performSearch(String text) {
        if (searchProcessor == null || StringUtility.isNullOrEmpty(text)) {
            setData(null);
            return true;
        }
        try {
            Object value = valueCache.get(text);
            if (value == null) {
                value = searchProcessor.searchById(text);
            }
            setData(value);
        } catch (Throwable exception) {
            UIStatusUtility.displayException(entryField, exception);
            if (editorValue != null) {
                editorValue = null;
                cacheEditorValue();
                displayValue();
                fireDataChangedAction();
            } else {
                displayValue();
                requestFocusInWindow();
            }
            return false;
        }
        return true;
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
     * Retrieves whether or not this Component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner; false otherwise.
     ***************************************************************************************************/
    public boolean isFocusOwner() {
        return entryField.isFocusOwner();
    }

    /****************************************************************************************************
     * Requests the focus move the text field within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely to succeed.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return entryField.requestFocusInWindow();
    }

    /****************************************************************************************************
     * Handles displaying a UI exception on the application.
     ***************************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(entryField, exception);
    }

    /****************************************************************************************************
     * Handles displaying an exception on the application.
     ***************************************************************************************************/
    protected void displayException(Throwable exception) {
        UIStatusUtility.displayException(entryField, exception);
    }

    /****************************************************************************************************
     * Caches old editor values for the life cycle of the editor
     ***************************************************************************************************/
    private void cacheEditorValue() {
        if (editorValue != null) {
            try {
                Method method = editorValue.getClass().getMethod("getId", (Class[]) null);
                Object value = method.invoke(editorValue, (Object[]) null);
                if (value != null) {
                    valueCache.put(value, editorValue);
                }
            } catch (Throwable t) {
                // Really Do Nothing
                LogService.debug(this, "ignoring Exception");
            }
        }
    }

    /****************************************************************************************************
     * Internal Key Listener - It checks for an enter key or event key code and processes the event
     ***************************************************************************************************/
    private class InternalKeyListener implements KeyListener {
        private Component component;

        private InternalKeyListener(Component comp) {
            component = comp;
        }

        public void keyReleased(KeyEvent event) {
        }

        public void keyPressed(KeyEvent event) {
            int key = event.getKeyCode();
            if (key == KeyEvent.VK_ENTER || key == eventKeyCode) {
                processEventAction(component);
                event.consume();
            }
        }

        public void keyTyped(KeyEvent event) {
            if (isErrorState) {
                setErrorState(false);
            }
        }
    }
}
