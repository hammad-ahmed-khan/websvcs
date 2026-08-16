package oracle.retail.sim.client.swing.lov;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.displaytable.TableRowDisplayer;
import oracle.retail.sim.client.swing.editor.AbstractEditor;
import oracle.retail.sim.client.swing.editor.EditorConstants;
import oracle.retail.sim.client.swing.editor.REditorLabel;
import oracle.retail.sim.client.swing.editor.RPlainEditorLabel;
import oracle.retail.sim.client.swing.event.EmptyStateActionAdaptor;
import oracle.retail.sim.client.swing.event.RActionEvent;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.panel.RPanel;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.TextLengthTranslator;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RIconButton;
import oracle.retail.sim.client.swing.widget.RLabel;
import oracle.retail.sim.client.swing.widget.RTextField;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.logging.LogService;

/******************************************************************************************
 * The list of values editor consists of a text field that may contain one or more
 * identifier values, a popup window to select values from and a display area that
 * displays some additional text.
 * <p>
 * The primary functionality of the List of Values editor is around the ListOfValuesModel
 * and ListOfValuesPageModel, both of which are implemented by the developer. Only one of
 * these models is used by the editor at a time. If both are set, the ListOfValuesPageModel
 * is given preference. The ListOfValuesModel is used when ALL the data that will be loaded
 * is done so at once. The ListOfValuesPageModel is used when the data will be loaded in
 * small pieces from the server, one page at a time. Either way, paging takes place.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class RListOfValuesEditor extends AbstractEditor implements ActionListener, PropertyChangeListener {
    private static final long serialVersionUID = -7902340195104758757L;

    private RPanel innerPanel = new RPanel();
    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RTextField valueField = new RTextField();
    private RTextField descField = new RTextField();
    private RIconButton popupButton = new RIconButton();
    private RLabel plusLabel = new RLabel("+");
    private RLabel holdLabel = new RLabel();

    private LovEmptyStateAdaptor emptyStateAdaptor = new LovEmptyStateAdaptor();
    private REventListener eventListener;
    private String eventCommand;
    private String lastTextValue = StringConstants.EMPTY;

    private int popupWidth = 350;
    private int pageThreshold = -1;
    private int pageSize = -1;

    private boolean isActionEnabled = true;
    private boolean isErrorState;
    private boolean isHorizontalAlignment;
    private boolean isSingleValueMode;

    private static final String POPUP = "Popup";
    private static String DELIMETER = ";";

    private RListOfValuesMultipleDialog multipleSelectionDialog;
    private RListOfValuesSingleDialog singleSelectionDialog;

    private ListOfValuesModel selectionModel;
    private ListOfValuesPageModel selectionPageModel;
    private ListOfValuesDisplayer selectionDisplayer = new SimpleListOfValuesDisplayer();
    private TableRowDisplayer tableRowDisplayer;

    private Object[] selectedValues = new Object[0];

    /******************************************************************************************
     * Creates a new RListOfValuesEditor with no title.
     ******************************************************************************************/
    public RListOfValuesEditor() {
        initialize();
    }

    /******************************************************************************************
     * Creates a new RListOfValuesEditor with a title.
     * <p>
     * @param title The title to assign.
     ******************************************************************************************/
    public RListOfValuesEditor(String title) {
        titleLabel.setText(title);
        initialize();
    }

    /******************************************************************************************
     * Creates a new RListOfValuesEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ******************************************************************************************/
    public RListOfValuesEditor(String title, boolean required) {
        titleLabel.setText(title);
        setRequired(required);
        initialize();
    }

    /******************************************************************************************
     * Initializes all the components of the list of values editor.
     ******************************************************************************************/
    private void initialize() {
        installLookAndFeel();

        Dimension plusDim = plusLabel.getPreferredSize();
        holdLabel.setLockedSize(plusDim.width, plusDim.height);

        innerPanel.setLayout(new GridBagLayout());

        errorLabel.setLockedSize(errorIcon.getIconWidth(), errorIcon.getIconHeight());
        errorLabel.setOpaque(false);
        spaceLabel.setOpaque(false);

        popupButton.setIcon(UIManager.getIcon(UIThemeName.LIST_OF_VALUES_ICON));
        popupButton.addActionListener(this);
        popupButton.setActionCommand(POPUP);

        valueField.addKeyListener(createErrorKeyListener());
        valueField.addFocusListener(createTriggerFocusListener());
        valueField.addFocusListener(createManagerFocusListener());
        valueField.addPropertyChangeListener(this);
        valueField.setLength(Integer.MAX_VALUE);
        valueField.setMinimumWidth(150);

        validateTitle();

        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
    }

    /******************************************************************************************
     * Installs the look and feel for the list of values editor.
     ******************************************************************************************/
    private void installLookAndFeel() {
        DELIMETER = UIManager.getString(UIThemeName.LIST_OF_VALUES_DELIMETER);
        errorIcon = (ImageIcon) UIManager.getIcon(UIThemeName.ERROR_ALERT);
        try {
            setPageThreshold(Integer.parseInt(UIManager.getString(UIThemeName.LIST_OF_VALUES_PAGE_SIZE)));
        } catch (Throwable exception) {
            LogService.debug(this, "ignoring Excepton");
        }
        try {
            setPageThreshold(Integer.parseInt(UIManager.getString(UIThemeName.LIST_OF_VALUES_PAGE_THRESHOLD)));
        } catch (Throwable exception) {
            LogService.debug(this, "ignoring Excepton");
        }
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
     * Retrieves the RTextField object of the editor. Many helpful methods have been added to
     * the editor, but the developer can always retrieve the actual RTextField widget to get
     * access to methods that are not provided. Note that care should be taken when altering
     * the properties of this text field as some properties may deactivate some functionality
     * of the list of values editor.
     * <p>
     * @return The RTextField widget.
     ******************************************************************************************/
    public RTextField getTextField() {
        return valueField;
    }

    /******************************************************************************************
     * This method is called when the identifer is altered in an editor.
     ******************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
        int[] values = TextLengthTranslator.getLengths(identifier);
        if (values != null && values.length > 1) {
            setPageThreshold(values[0]);
            setPageSize(values[1]);
        }
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
     * @param suffixEnabled True if the editor should use the label suffix feature, otherwise false.
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
        validateInnerPanelLayout();

        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.TOP:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 1, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.LEFT:
                add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 3, 0, 0, 0, 2));
                add(innerPanel, GridTool.constraints(1, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 0, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.BOTTOM:
                add(titleLabel, GridTool.constraints(0, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 2, 1, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            case EditorConstants.RIGHT:
                add(titleLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 3, 0, 2, 0, 0));
                add(innerPanel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 0, 0, 0, 0, 0));
                add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 1, 1, 5, 3, 0, 0, 0, 0));
                break;
            default:
                throw new IllegalArgumentException("Invalid RTableOfValuesEditor alignment!");
        }
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /******************************************************************************************
     * Validates the layout of the inner panel.
     ******************************************************************************************/
    private void validateInnerPanelLayout() {
        innerPanel.removeAll();

        RLabel tempLabel = plusLabel;
        if (isSingleValueMode) {
            tempLabel = holdLabel;
        }

        if (sizeType == -1) {
            switch (titleLabel.getTitleAlignment()) {
                case EditorConstants.RIGHT:
                    innerPanel.add(valueField, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(popupButton, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    innerPanel.add(tempLabel, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1));
                    innerPanel.add(descField, GridTool.constraints(4, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
                default:
                    innerPanel.add(valueField, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(popupButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    innerPanel.add(tempLabel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1));
                    innerPanel.add(descField, GridTool.constraints(3, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                    innerPanel.add(errorLabel, GridTool.constraints(4, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                    break;
            }
            return;
        }
        switch (titleLabel.getTitleAlignment()) {
            case EditorConstants.RIGHT:
                innerPanel.add(valueField, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(popupButton, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(tempLabel, GridTool.constraints(4, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1));
                innerPanel.add(descField, GridTool.constraints(5, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(0, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
            default:
                innerPanel.add(valueField, GridTool.constraints(0, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(popupButton, GridTool.constraints(1, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(tempLabel, GridTool.constraints(2, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1));
                innerPanel.add(descField, GridTool.constraints(3, 0, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0));
                innerPanel.add(errorLabel, GridTool.constraints(4, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0));
                innerPanel.add(innerLabel, GridTool.constraints(5, 0, 1, 1, 1, 0, 0, 1, 0, 0, 0, 0));
                break;
        }
    }

    /******************************************************************************************
     * Validates the visability of the title.
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
                setFieldWidths(UIManager.getInt(UIThemeName.LIST_OF_VALUES_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setFieldWidths(UIManager.getInt(UIThemeName.LIST_OF_VALUES_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setFieldWidths(UIManager.getInt(UIThemeName.LIST_OF_VALUES_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /******************************************************************************************
     * Assigns minimum widths to the text fields.
     *****************************************************************************************/
    private void setFieldWidths(int width) {
        valueField.setMinimumWidth(width);
        descField.setMinimumWidth(width);
    }

    /******************************************************************************************
     * Assigns the popup width of the popup window. This value has a minimum of 350.
     * <p>
     * @param width The width in pixels.
     *****************************************************************************************/
    public void setPopupWidth(int width) {
        if (width < 350) {
            width = 350;
        }
        popupWidth = width;
    }

    /******************************************************************************************
     * Retrieves true if the editor allows only one value, false otherwise.
     * <p>
     * @return True if in single selection mode, otherwise false.
     *****************************************************************************************/
    public boolean isSingleValueMode() {
        return isSingleValueMode;
    }

    /******************************************************************************************
     * Assigns a single selection mode to the list of values. This means only one value may
     * be chosen from the list of values.
     *****************************************************************************************/
    public void setSingleValueMode() {
        isSingleValueMode = true;
        if (selectedValues.length > 1) {
            Object value = getSelectedValue();
            selectedValues = new Object[1];
            selectedValues[0] = value;
        }
        setTitleAlignment(titleLabel.getTitleAlignment());
    }

    /******************************************************************************************
     * Assigns a multi value mode to the list of values. This means any number of values
     * may be chosen from the list of values.
     *****************************************************************************************/
    public void setMultiValueMode() {
        isSingleValueMode = false;
        setTitleAlignment(titleLabel.getTitleAlignment());
    }

    /******************************************************************************************
     * Assigns the popup chooser a horizontal alignment.
     *****************************************************************************************/
    public void setHorizontalAlignment() {
        isHorizontalAlignment = true;
    }

    /******************************************************************************************
     * Assigns the popup chooser a vertical alignment.
     *****************************************************************************************/
    public void setVerticalAlignment() {
        isHorizontalAlignment = false;
    }

    /******************************************************************************************
     * Retrieves the number of data elements allowed per page.
     *****************************************************************************************/
    public int getPageSize() {
        return pageSize;
    }

    /******************************************************************************************
     * Assigns the number of data elements allowed per page. A negative values means ignore
     * page size.
     * <p>
     * @param pageSize umber of data elements allowed per page.
     *****************************************************************************************/
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /******************************************************************************************
     * Retrieves the number of data elements necessary to trigger paging in the popup window.
     * <p>
     * @return The number of data elements necessary to trigger paging in the popup window.
     *****************************************************************************************/
    public int getPageThreshold() {
        return pageThreshold;
    }

    /******************************************************************************************
     * Assigns the number of data elements necessary to trigger paging in the popup window.
     * <p>
     * @param pageThreshold The number of data elements necessary to trigger paging.
     *****************************************************************************************/
    public void setPageThreshold(int pageThreshold) {
        this.pageThreshold = pageThreshold;
    }

    /******************************************************************************************
     * Assigns the selection model that will retrieve the selectable values when the
     * popup dialog is triggered. This object is also responsible for setting selected values
     * outside of this editor. If the editor does not have either a ListOfValuesModel or
     * ListOfValuesPageModel, an IllegalStateException() will be thrown when attempting to
     * use the editor.
     * <p>
     *@param param The ListOfValuesModel to install.
     *****************************************************************************************/
    public void setSelectionModel(ListOfValuesModel model) {
        selectionModel = model;
    }

    /******************************************************************************************
     * Assigns the selection model that will retrieve the selectable values when the
     * popup dialog is triggered. This object is also responsible for setting selected values
     * outside of this editor. If the editor does not have either a ListOfValuesModel or
     * ListOfValuesPageModel, an IllegalStateException() will be thrown when attempting to
     * use the editor.
     * <p>
     *@param param The ListOfValuesPageModel to install.
     *****************************************************************************************/
    public void setSelectionPageModel(ListOfValuesPageModel model) {
        selectionPageModel = model;
    }

    /******************************************************************************************
     * Assigns the selection displayer that will display information about selected values
     * in the editor text fields after they are selected from the popup dialog. This value
     * cannot be set to null.
     * <p>
     *@param param The ListOfValuesDisplayer to install.
     *****************************************************************************************/
    public void setSelectionDisplayer(ListOfValuesDisplayer displayer) {
        if (displayer != null) {
            selectionDisplayer = displayer;
        }
    }

    /******************************************************************************************
     * Assigns the table row displayer that will display the table rows within the popup dialog.
     * This value cannot be set to null.
     * <p>
     *@param param The TableRowDisplayer to install.
     *****************************************************************************************/
    public void setTableRowDisplayer(TableRowDisplayer displayer) {
        if (displayer != null) {
            tableRowDisplayer = displayer;
        }
    }

    /******************************************************************************************
     * Returns the currently selected value of the list of values editor. If multiple values
     * are selected, this returns the first value in the ilist.
     * <p>
     *@return The currently selected value (null if no value is selected).
     *****************************************************************************************/
    public Object getSelectedValue() {
        if (selectedValues.length > 0) {
            return selectedValues[0];
        }
        return null;
    }

    /******************************************************************************************
     * Returns all the selected values for the editor.
     * <p>
     *@return All the selected values for the editor.
     *****************************************************************************************/
    public Object[] getSelectedValues() {
        return selectedValues;
    }

    /******************************************************************************************
     * Clears the text field and all selected values.
     *****************************************************************************************/
    public void clear() {
        selectedValues = new Object[0];
        lastTextValue = StringConstants.EMPTY;
        valueField.clear();
        descField.clear();
        setErrorState(false);
    }

    /******************************************************************************************
     * Retrieves whether or not the editor contains selected values.
     * <p>
     * @return True if the editor does not contain selected values, false if it does.
     *****************************************************************************************/
    public boolean isEmpty() {
        return selectedValues.length == 0;
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
     * Override the setEnabled() of JPanel to call through to all widgets.
     * <p>
     * @param enabled True if the editor should be enabled, false otherwise.
     *****************************************************************************************/
    public void setEnabled(boolean enabled) {
        if (isEnabledDenied()) {
            enabled = false;
        }
        titleLabel.setEnabled(enabled);
        valueField.setEnabled(enabled);
        popupButton.setEnabled(enabled);
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
        popupButton.setEnabled(fieldEnabled);
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
     * Enables or disables the registered action triggers of the editor.
     * <p>
     * @param enabled True if actions should be sent, false if not.
     *****************************************************************************************/
    public void setActionsEnabled(boolean enabled) {
        isActionEnabled = enabled;
    }

    /******************************************************************************************
     * Retrieves whether or not the registered action triggers of the editor are enabled.
     * <p>
     * @return True if actions are enabled, false if not.
     *****************************************************************************************/
    public boolean isActionsEnabled() {
        return isActionEnabled;
    }

    /******************************************************************************************
     * Sets the error state of the editor.
     * <p>
     * @param exception The UIException that caused the error state.
     *****************************************************************************************/
    private void setErrorState(UIException exception) {
        displayException(exception);
        setErrorState(true);
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
     * Retrieves whether or not the editor is in error state.
     * <p>
     * @return True if the editor is in error state, false if not.
     *****************************************************************************************/
    public boolean isErrorState() {
        return isErrorState;
    }

    /******************************************************************************************
     * Registers to receive actions from a widget when keystrokes occur in the widget. This
     * sends the commands UIPropertyName.TEXT_COMPONENT_EMPTY or
     * UIPropertyName.TEXT_COMPONENT_NOT_EMPTY, when the widget changes state.
     * <p>
     * @param listener The object that should receive the RActionEvent.
     ******************************************************************************************/
    public void registerEmptyStateAction(REventListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("REventListener cannot be null!");
        }
        emptyStateAdaptor.registerListener(listener, getIdentifier());
        valueField.addKeyListener(emptyStateAdaptor);
    }

    /******************************************************************************************
     * Removes an empty state action from the editor. An empty state action will no longer be
     * triggered for this widget when keys are pressed.
     ******************************************************************************************/
    public void unregisterEmptyStateAction() {
        valueField.removeKeyListener(emptyStateAdaptor);
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
     * Implements the property change listener method to disable and enable the title along
     * with the actual text field or to control the error state under certain conditions.
     *****************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        String name = event.getPropertyName();
        if (name.equals(UIPropertyName.TEXT_COMPONENT_ENABLED)) {
            titleLabel.setEnabled(((Boolean) event.getNewValue()).booleanValue());
        } else if (name.equals(UIPropertyName.TEXT_COMPONENT_PASTE) && isErrorState) {
            setErrorState(false);
        } else if (name.equals(UIPropertyName.LIST_OF_VALUES_SELECTION)) {
            setInternalSelectedValues((Object[]) event.getNewValue());
            notifySelectedValuesModified();
        }
    }

    /******************************************************************************************
     * Assigns selected values to the list of values. This will automatically call through
     * and set the values on the assigned list of values model as well.
     * <p>
     * @param values The selected values.
     *****************************************************************************************/
    public void setSelectedValues(Collection values) {
        setInternalSelectedValues(values.toArray());
    }

    /******************************************************************************************
     * Assigns selected values to the list of values. This will automatically call through
     * and set the values on the assigned list of values model as well.
     * <p>
     * @param values The selected values.
     *****************************************************************************************/
    public void setSelectedValues(Object[] values) {
        setInternalSelectedValues(values);
    }

    /******************************************************************************************
     * Internally assigns the selected values. This clears the editor if the values are empty.
     * It builds up the entry text and description text and assigns it to the appropriate
     * text fields. It clears the error state if one exists.
     *****************************************************************************************/
    private void setInternalSelectedValues(Object[] values) {
        if (values == null || values.length == 0) {
            clear();
            return;
        }
        selectedValues = values;

        validateSelectionModel();

        try {
            if (selectionPageModel != null) {
                selectionPageModel.setSelectedValues(Arrays.asList(selectedValues));
            } else {
                selectionModel.setSelectedValues(Arrays.asList(selectedValues));
            }
        } catch (UIException exception) {
            setErrorState(exception);
            return;
        }

        StringBuilder entryText = new StringBuilder(selectionDisplayer.getEntryText(selectedValues[0]));
        StringBuilder descText = new StringBuilder(selectionDisplayer.getDescriptionText(selectedValues[0]));

        if (selectedValues.length > 1) {
            descText = new StringBuilder(Translator.getText("Multiple"));
            for (int i = 1; i < selectedValues.length; i++) {
                entryText.append(DELIMETER);
                entryText.append(selectionDisplayer.getEntryText(selectedValues[i]));
            }
        }
        descField.setText(descText.toString());
        valueField.setText(entryText.toString());
        lastTextValue = entryText.toString();
        setErrorState(false);
    }

    /******************************************************************************************
     * Validates that a selection model exists.
     *****************************************************************************************/
    private void validateSelectionModel() {
        if (selectionModel == null && selectionPageModel == null) {
            throw new IllegalStateException(UIMessageText.LOV_NO_SELECTION_MODEL.getText());
        }
    }

    /******************************************************************************************
     * Implement the action listener method to respond when the popup button is clicked.
     ******************************************************************************************/
    public void actionPerformed(ActionEvent event) {
        if (isSingleValueMode) {
            popupSingleSelectionDialog();
        } else {
            popupMultipleSelectionDialog();
        }
    }

    /******************************************************************************************
     * Pops up a single selection mode list of values selection dialog.
     ******************************************************************************************/
    private void popupSingleSelectionDialog() {
        try {
            Container container = valueField.getTopLevelAncestor();

            if (singleSelectionDialog == null) {
                if (container instanceof JFrame) {
                    singleSelectionDialog = new RListOfValuesSingleDialog((JFrame) container);
                } else if (container instanceof JDialog) {
                    singleSelectionDialog = new RListOfValuesSingleDialog((JDialog) container);
                } else {
                    return;
                }
                singleSelectionDialog.addPropertyChangeListener(this);
            }
            initializePopupWindow(singleSelectionDialog);
            if (isHorizontalAlignment) {
                singleSelectionDialog.setPopupWidth(popupWidth / 2);
            }
            singleSelectionDialog.refreshSelectableValues();
            singleSelectionDialog.centerOnOwner();
            singleSelectionDialog.setVisible(true);
        } catch (UIException exception) {
            setErrorState(exception);
        }
        valueField.requestFocusInWindow();
    }

    /******************************************************************************************
     * Pops up a multiple selection mode list of values selection dialog.
     ******************************************************************************************/
    private void popupMultipleSelectionDialog() {
        try {
            Container container = valueField.getTopLevelAncestor();

            if (multipleSelectionDialog == null) {
                if (container instanceof JFrame) {
                    multipleSelectionDialog = new RListOfValuesMultipleDialog((JFrame) container);
                } else if (container instanceof JDialog) {
                    multipleSelectionDialog = new RListOfValuesMultipleDialog((JDialog) container);
                } else {
                    return;
                }
                multipleSelectionDialog.addPropertyChangeListener(this);
            }

            initializePopupWindow(multipleSelectionDialog);
            multipleSelectionDialog.setLayoutAlignment(isHorizontalAlignment);
            multipleSelectionDialog.refreshSelectableValues();
            multipleSelectionDialog.setSelectedValues(retrieveSelectedValues());
            multipleSelectionDialog.centerOnOwner();
            multipleSelectionDialog.setVisible(true);
        } catch (UIException exception) {
            setErrorState(exception);
        }
        valueField.requestFocusInWindow();
    }

    /******************************************************************************************
     * Initializes the popup window with the basic default information.
     * <p>
     * @param dialog The generic dialog superclass.
     ******************************************************************************************/
    private void initializePopupWindow(RListOfValuesPopupDialog dialog) {
        dialog.setTitle(getTitle());
        dialog.setPopupWidth(popupWidth);
        dialog.setPageInformation(pageThreshold, pageSize);
        dialog.setRowDisplayer(tableRowDisplayer);
        dialog.setSelectionModel(selectionModel);
        dialog.setSelectionPageModel(selectionPageModel);
    }

    /******************************************************************************************
     * Retrieves the values that have been selected from the selection model.
     ******************************************************************************************/
    private Object[] retrieveSelectedValues() throws UIException {
        validateSelectionModel();

        String value = valueField.getText();
        if (lastTextValue.equals(value)) {
            return selectedValues;
        }
        lastTextValue = value;

        String[] inputArray = StringUtility.getStringArray(value, DELIMETER);
        Collection dataCollection;
        if (selectionPageModel != null) {
            dataCollection = selectionPageModel.getSelectedValues(inputArray);
        } else {
            dataCollection = selectionModel.getSelectedValues(inputArray);
        }
        if (dataCollection != null) {
            return dataCollection.toArray();
        }
        return new Object[0];
    }

    /******************************************************************************************
     * Creates the focus trigger action which causes the internal selected values to be loaded
     * when the text field focus is lost and someone has typed in values.
     ******************************************************************************************/
    private FocusListener createTriggerFocusListener() {
        return new FocusListener() {
            public void focusGained(FocusEvent event) {
                lastTextValue = valueField.getText();
            }

            public void focusLost(FocusEvent event) {
                String value = valueField.getText();
                if (!lastTextValue.equals(value)) {
                    try {
                        setInternalSelectedValues(retrieveSelectedValues());
                        notifySelectedValuesModified();
                    } catch (UIException exception) {
                        setErrorState(exception);
                    }
                }
            }
        };
    }

    /******************************************************************************************
     * Notifies listeners that the selected values in the field have been modified.
     ******************************************************************************************/
    private void notifySelectedValuesModified() {
        if (isActionEnabled && eventListener != null && eventCommand != null) {
            eventListener.performActionEvent(new RActionEvent(this, eventCommand));
        }
    }

    /******************************************************************************************
     * Handles displaying an exception on the application.
     *****************************************************************************************/
    protected void displayException(UIException exception) {
        UIStatusUtility.displayException(valueField, exception);
    }

    /******************************************************************************************
     * Retrieves whether or not this component is the focus owner.
     * <p>
     * @return True if this Component is the focus owner, false otherwise.
     *****************************************************************************************/
    public boolean isFocusOwner() {
        return valueField.isFocusOwner() || popupButton.isFocusOwner();
    }

    /******************************************************************************************
     * Requests the focus move to the values field within the editor.
     * <p>
     * @return False if the focus change request is guaranteed to fail; True if it is likely
     * to succeed.
     *****************************************************************************************/
    public boolean requestFocusInWindow() {
        return valueField.requestFocusInWindow();
    }

    /******************************************************************************************
     *
     * INNER CLASS - Lov implementation of the empty state action adaptor.
     *
     *****************************************************************************************/
    private class LovEmptyStateAdaptor extends EmptyStateActionAdaptor {

        public int getTextLength() {
            return valueField.getText().length();
        }

        public int getSelectedTextLength() {
            return valueField.getSelectedText().length();
        }
    }
}
