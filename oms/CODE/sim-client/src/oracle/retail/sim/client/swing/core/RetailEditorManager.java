package oracle.retail.sim.client.swing.core;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JScrollPane;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.client.swing.editor.AbstractEditor;
import oracle.retail.sim.client.swing.editor.RCheckBoxEditor;
import oracle.retail.sim.client.swing.editor.RComboBoxEditor;
import oracle.retail.sim.client.swing.editor.RDateFieldEditor;
import oracle.retail.sim.client.swing.editor.RDecimalFieldEditor;
import oracle.retail.sim.client.swing.editor.RIntegerFieldEditor;
import oracle.retail.sim.client.swing.editor.RListEditor;
import oracle.retail.sim.client.swing.editor.RLongTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RMoneyFieldEditor;
import oracle.retail.sim.client.swing.editor.RPasswordFieldEditor;
import oracle.retail.sim.client.swing.editor.RPercentFieldEditor;
import oracle.retail.sim.client.swing.editor.RTextAreaEditor;
import oracle.retail.sim.client.swing.editor.RTextFieldEditor;
import oracle.retail.sim.client.swing.editor.RetailEditor;
import oracle.retail.sim.client.swing.logging.UIStatusUtility;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.util.UIProblem;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RCheckBox;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.client.swing.widget.RDateField;
import oracle.retail.sim.client.swing.widget.RHyperlink;
import oracle.retail.sim.client.swing.widget.RList;
import oracle.retail.sim.client.swing.widget.RLongTextField;
import oracle.retail.sim.client.swing.widget.RMoneyField;
import oracle.retail.sim.client.swing.widget.RPasswordField;
import oracle.retail.sim.client.swing.widget.RRadioButton;
import oracle.retail.sim.client.swing.widget.RTextArea;
import oracle.retail.sim.client.swing.widget.RTextField;
import oracle.retail.sim.client.swing.widget.RetailComponent;
import oracle.retail.sim.common.core.locale.StringConstants;

/********************************************************************************************************
 * The RetailEditorManager it a tool used to perform several. It allows the lookup of editors by
 * identifier, turning action triggers on and off across an entire workspace, display exceptions and
 * determining whether or not contents in the workspace or modified. Most methods primarily work with
 * RetailEditors, though some work with RetailComponents as well.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RetailEditorManager implements PropertyChangeListener {
    private Component parentComponent;
    private Component[] globalArray;
    private Map<Component, Object> storedDataMap = new HashMap<>();
    private boolean actionsEnabledFlag = true;
    private boolean contentsModifiedFlag;

    /****************************************************************************************************
     * Assigns an owner to the manager. This should only be either an RContentPanel or an RDialog, but
     * could be used by coders who create custom usages of this class.
     * <p>
     * @param component The owner of the manager.
     ***************************************************************************************************/
    public void setOwner(Component component) {
        parentComponent = component;
    }

    /****************************************************************************************************
     * Retrieves a component contained within the manager owner. This will search through all editors and
     * widgets of the parent component and return the first component with a matching identifier.
     * <p>
     * @param identifier The identifier of the editor or widget.
     * @return A RetailEditor or RetailComponent.
     ***************************************************************************************************/
    public Component findComponent(String identifier) {
        if (StringUtility.isNullOrEmpty(identifier)) {
            return null;
        }
        Component[] components = findComponents(identifier);
        if (components.length > 0) {
            return components[0];
        }
        return null;
    }

    /****************************************************************************************************
     * Retrieves all components contained within the manager owner. This will search through all editors
     * and widgets of the parent component and return an array of all the components with a matching
     * identifier.
     * <p>
     * @param identifier The identifier of the editors or widgets.
     * @return An array of RetailEditors or RetailComponents with the specified identifier.
     ***************************************************************************************************/
    public Component[] findComponents(String identifier) {
        if (StringUtility.isNullOrEmpty(identifier)) {
            return null;
        }
        Component[] returnArray = new Component[0];
        for (Component element : buildComponentArray(true)) {
            if (element instanceof RetailEditor) {
                RetailEditor editor = (RetailEditor) element;
                if (identifier.equals(editor.getIdentifier())) {
                    int length = returnArray.length;
                    Component[] tempArray = new Component[length + 1];
                    System.arraycopy(returnArray, 0, tempArray, 0, length);
                    returnArray = tempArray;
                    returnArray[length] = (Component) editor;
                }
            } else if (element instanceof RetailComponent) {
                RetailComponent component = (RetailComponent) element;
                if (identifier.equals(component.getIdentifier())) {
                    int length = returnArray.length;
                    Component[] tempArray = new Component[length + 1];
                    System.arraycopy(returnArray, 0, tempArray, 0, length);
                    returnArray = tempArray;
                    returnArray[length] = (Component) component;
                }
            }
        }
        return returnArray;
    }

    /****************************************************************************************************
     * Returns whether or not the contents of the owner have been modified since the last time the
     * manager has had its setContentsModified() value set to false.
     * <p>
     * @return True if the contents are modified, false if not.
     ***************************************************************************************************/
    public boolean isContentModified() {
        if (contentsModifiedFlag) {
            return true;
        }
        return isContentDataModified(storedDataMap, buildDataSnapshot());
    }

    /****************************************************************************************************
     * Sets whether or not the editors and widgets of the parent component have had their contents
     * modified. If set to "false", the widget manager will take a snapshot of the contents and hold on
     * to them. If set to "true", it will trip a flag that will return true until the next time this
     * method is called with "false".
     * <p>
     * @param modified True if the contents should be considered modified, false if not.
     ***************************************************************************************************/
    public void setContentModified(boolean modified) {
        if (!modified) {
            storedDataMap = buildDataSnapshot();
        }
        contentsModifiedFlag = modified;
    }

    /****************************************************************************************************
     * Enables or disables the action triggers within the editors belonging to the parent component.
     * Regular widgets do not allow action triggers, so developers must handle this in their specific
     * code. This method loops through all editors and sets their actions enabled (or disabled).
     * <p>
     * @param enabled True if editor actions should be sent, false if they should be ignored.
     ***************************************************************************************************/
    public void setActionsEnabled(boolean enabled) {
        for (Component element : buildComponentArray(false)) {
            ((RetailEditor) element).setActionsEnabled(enabled);
        }
        actionsEnabledFlag = enabled;
    }

    /****************************************************************************************************
     * Retrieves whether or not the editors have their actions enabled or not.
     * <p>
     * @return True if editor actions should be sent, false if they should be ignored.
     ***************************************************************************************************/
    public boolean isActionsEnabled() {
        return actionsEnabledFlag;
    }

    /****************************************************************************************************
     * Returns true if all the editors are empty of content.
     * <p>
     * @return True if the dialog contents are empty of content, false otherwise.
     ***************************************************************************************************/
    public boolean isAllContentEmpty() {
        for (Component component : buildComponentArray(false)) {
            if (isInVisibleSpace(component) && !editorIsEmpty((RetailEditor) component)) {
                return false;
            }
        }
        return true;
    }

    /****************************************************************************************************
     * Validates that all editors marked as required contain input data.
     * <p>
     * @throws UIException Thrown if an editor marked as required does not contain input data.
     ***************************************************************************************************/
    public void validateRequiredContent() throws UIException {
        Component[] components = buildComponentArray(false);
        for (Component component : components) {
            if (isInVisibleSpace(component)) {
                RetailEditor editor = (RetailEditor) component;
                if (editor.isRequired() && editorIsEmpty(editor)) {
                    editor.setErrorState(true);
                    if (editor instanceof AbstractEditor) {
                        ((AbstractEditor) editor).requestFocusInWindow();
                    }
                    throw new UIException(UIMessageText.EDITOR_CONTENT_MISSING, editor.getTitle());
                }
            }
        }
    }

    /****************************************************************************************************
     * Returns true if the editor is visible on the screen, false otherwise.
     * <p>
     * @param component The component to validate.
     * @return True if the component is currently visible, false otherwise.
     ***************************************************************************************************/
    private boolean isInVisibleSpace(Component component) {
        if (!component.isVisible()) {
            return false;
        }
        Component parent = component.getParent();
        while (parent != null) {
            if (!parent.isVisible()) {
                return false;
            }
            if (parent instanceof Window) {
                break;
            }
            parent = parent.getParent();
        }
        return true;
    }

    /****************************************************************************************************
     * Returns true if the editor contains no content data.
     * <p>
     * @param editor The editor to check for contents.
     * @return True if the editor contains no content data, false otherwise.
     ***************************************************************************************************/
    private boolean editorIsEmpty(RetailEditor editor) {
        if (editor instanceof RComboBoxEditor) {
            return ((RComboBoxEditor) editor).isEmptySelection();
        }
        if (editor instanceof RListEditor) {
            return ((RListEditor) editor).isEmptySelection();
        }
        return editor.isEmpty();
    }

    /****************************************************************************************************
     * Processes a UIException. It searches through all editors and finds one or more editors associated
     * with the exception and puts each in an error state. This method only works on editors. Usage of
     * regular widgets means the developer will have to handle their own error functionality. Processes
     * the problems within an exception. It searches through all editors of the parent components, finds
     * those associated with a problem, and puts each in an error state. This method only work on
     * editors. When finished, the first widget in error state requests the focus from focus management.
     * <p>
     * @param exception The UIException to display.
     ***************************************************************************************************/
    public void displayException(UIException exception) {
        Component[] components = buildComponentArray(false);
        List<UIProblem> problems = exception.getProblems();
        for (UIProblem problem : problems) {
            for (RetailEditor retailEditor : findRetailEditors(components, problem.getIdentifier())) {
                retailEditor.setErrorState(true, Translator.getMessage(problem.getMessageText().getText(), problem.getMessageValues()));
            }
        }
        for (Component component : components) {
            AbstractEditor editor = (AbstractEditor) component;
            editor.removePropertyChangeListener(this);
            editor.addPropertyChangeListener(this);
        }
        for (Component component : components) {
            AbstractEditor editor = (AbstractEditor) component;
            if (editor.isErrorState()) {
                editor.requestFocusInWindow();
                break;
            }
        }
    }

    /****************************************************************************************************
     * Finds all the RetekEditors within a component array with the specified identifier. While this
     * method is private, the components are guaranteed to be RetekEditor objects.
     * <p>
     * @param components An array of RetekEditors.
     * @param identifier The identifier to match on.
     ***************************************************************************************************/
    private List<RetailEditor> findRetailEditors(Component[] components, String identifier) {
        List<RetailEditor> editorList = new ArrayList<>();
        for (Component component : components) {
            RetailEditor editor = (RetailEditor) component;
            if (identifier.equals(editor.getIdentifier())) {
                editorList.add(editor);
            }
        }
        return editorList;
    }

    /****************************************************************************************************
     * Retrieves whether or not the editor manager currently contains any exceptions or problems with an
     * error or fatal severity.
     * <p>
     * @return True if the manager contains any exceptions, false otherwise.
     ***************************************************************************************************/
    public boolean hasExceptions() {
        for (Component component : buildComponentArray(false)) {
            if (component instanceof RetailEditor) {
                if (((RetailEditor) component).isErrorState()) {
                    return true;
                }
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Clears all exceptions from the editor manager.
     ***************************************************************************************************/
    public void clearExceptions() {
        for (Component component : buildComponentArray(false)) {
            if (component instanceof RetailEditor) {
                ((RetailEditor) component).setErrorState(false);
            }
        }
    }

    /****************************************************************************************************
     * Implement the property change listener method.
     * It receives notices from RetailEditors and takes the appropriate action.
     ***************************************************************************************************/
    public void propertyChange(PropertyChangeEvent event) {
        switch (event.getPropertyName()) {
            case UIPropertyName.EDITOR_FOCUS_LOST:
                clearStatusMessage((RetailEditor) event.getSource());
                break;
            case UIPropertyName.EDITOR_FOCUS_GAINED:
                displayStatusMessage((RetailEditor) event.getSource());
                break;
            case UIPropertyName.EDITOR_ERROR_OCCURRED:
                UIException exception = (UIException) event.getNewValue();
                if (exception != null) {
                    displayException(exception);
                }
                break;
        }
    }

    /****************************************************************************************************
     * Displays the primary exception message.
     ***************************************************************************************************/
    private void clearStatusMessage(RetailEditor editor) {
        UIStatusUtility.clearException(editor);
    }

    /****************************************************************************************************
     * Displays a status message for a particular component.
     * <p>
     * @param editor The RetailEditor to find and display a status message for.
     ***************************************************************************************************/
    private void displayStatusMessage(RetailEditor editor) {
        UIStatusUtility.displayException(editor);
    }

    /****************************************************************************************************
     * Validates whether or not all the RetailEditors of the parent component are considered required or
     * not and sets the property on the editor. If the property has already been assigned, this will not
     * alter the property.
     ***************************************************************************************************/
    public void validateRequiredEditors() {
        String ownerName = StringUtility.getRemainingText(parentComponent.getClass().getName(), StringConstants.DOT);
        for (Component component : buildComponentArray(true)) {
            if (component instanceof RetailEditor) {
                ((RetailEditor) component).validateRequiredState(ownerName);
            }
        }
    }

    /****************************************************************************************************
     * Validates the permissions of all the parent components RetailEditors and RetailComponents.
     * <p>
     * @exception OldUIException Thrown if an error occurs while validating the permissions.
     ***************************************************************************************************/
    public void validatePermissions() throws UIException {
        String ownerName = StringUtility.getRemainingText(parentComponent.getClass().getName(), StringConstants.DOT);
        for (Component component : buildComponentArray(true)) {
            if (component instanceof RetailEditor) {
                ((RetailEditor) component).validatePermission(ownerName);
            } else if (component instanceof RetailComponent) {
                ((RetailComponent) component).validatePermission(ownerName);
            }
        }
    }

    /****************************************************************************************************
     * This helper method builds a component array of all RetailEditors. It will add RetailComponents to
     * this array if the "findWidgets" parameter is set to true.
     * <p>
     * @param findWidgets True if it should include RetailComponents, false if only RetailEditors.
     ***************************************************************************************************/
    private Component[] buildComponentArray(boolean findWidgets) {
        globalArray = new Component[0];
        if (parentComponent instanceof Container) {
            for (Component component : ((Container) parentComponent).getComponents()) {
                processCycleComponent(component, findWidgets);
            }
        }
        return globalArray;
    }

    /****************************************************************************************************
     * This processes a single component, adding it to the global Component array or not based on the
     * type of component it is.
     * <p>
     * @param component The component to process.
     * @param findWidgets True if it should include RetailComponents, false if only RetailEditors.
     ***************************************************************************************************/
    private void processCycleComponent(Component component, boolean findWidgets) {
        if (component instanceof RetailEditor || findWidgets && component instanceof RetailComponent) {
            addComponentToArray(component);
        } else if (component instanceof JScrollPane) {
            processCycleComponent(((JScrollPane) component).getViewport().getView(), findWidgets);
        } else if (component instanceof Container) {
            for (Component element : ((Container) component).getComponents()) {
                processCycleComponent(element, findWidgets);
            }
        }
    }

    /****************************************************************************************************
     * Adds a single component to the global component array.
     ***************************************************************************************************/
    private void addComponentToArray(Component component) {
        int length = globalArray.length;
        Component[] tempArray = new Component[length + 1];
        System.arraycopy(globalArray, 0, tempArray, 0, length);
        globalArray = tempArray;
        globalArray[length] = component;
    }

    /****************************************************************************************************
     * Builds a snapshot of the data contained within the parent component.
     * <p>
     * @return A HashMap containing (key=component/value=data).
     ***************************************************************************************************/
    private Map<Component, Object> buildDataSnapshot() {
        Component[] components = buildComponentArray(true);
        Map<Component, Object> contentMap = new HashMap<>(components.length);
        for (Component component : components) {
            contentMap.put(component, getDataElement(component));
        }
        return contentMap;
    }

    /****************************************************************************************************
     * Retrieves the data element for a component. The component must be a RetailEditor or
     * RetailComponent.
     * <p>
     * @param component The component to retrieve data from.
     * @return The data object contained within the component.
     ***************************************************************************************************/
    private Object getDataElement(Component component) {
        if (component instanceof RCheckBoxEditor) {
            return ((RCheckBoxEditor) component).isSelected();
        }
        if (component instanceof RComboBoxEditor) {
            return ((RComboBoxEditor) component).getSelectedItem();
        }
        if (component instanceof RMoneyFieldEditor) {
            return ((RMoneyFieldEditor) component).getText();
        }
        if (component instanceof RDateFieldEditor) {
            return ((RDateFieldEditor) component).getText();
        }
        if (component instanceof RDecimalFieldEditor) {
            return ((RDecimalFieldEditor) component).getText();
        }
        if (component instanceof RIntegerFieldEditor) {
            return ((RIntegerFieldEditor) component).getText();
        }
        if (component instanceof RListEditor) {
            return ((RListEditor) component).getSelectedValues();
        }
        if (component instanceof RLongTextFieldEditor) {
            return ((RLongTextFieldEditor) component).getText();
        }
        if (component instanceof RPasswordFieldEditor) {
            return ((RPasswordFieldEditor) component).getPassword();
        }
        if (component instanceof RPercentFieldEditor) {
            return ((RPercentFieldEditor) component).getText();
        }
        if (component instanceof RTextAreaEditor) {
            return ((RTextAreaEditor) component).getText();
        }
        if (component instanceof RTextFieldEditor) {
            return ((RTextFieldEditor) component).getText();
        }
        if (component instanceof RCheckBox) {
            return ((RCheckBox) component).getText();
        }
        if (component instanceof RComboBox) {
            return ((RComboBox) component).getSelectedItem();
        }
        if (component instanceof RMoneyField) {
            return ((RMoneyField) component).getText();
        }
        if (component instanceof RDateField) {
            return ((RDateField) component).getText();
        }
        if (component instanceof RHyperlink) {
            return ((RHyperlink) component).getText();
        }
        if (component instanceof RList) {
            return ((RList) component).getSelectedValues();
        }
        if (component instanceof RLongTextField) {
            return ((RLongTextField) component).getText();
        }
        if (component instanceof RPasswordField) {
            return new String(((RPasswordField) component).getPassword());
        }
        if (component instanceof RRadioButton) {
            return ((RRadioButton) component).getText();
        }
        if (component instanceof RTextArea) {
            return ((RTextArea) component).getText();
        }
        if (component instanceof RTextField) {
            return ((RTextField) component).getText();
        }
        return null;
    }

    /****************************************************************************************************
     * Compares the content of an old data element map and new data element map.
     * <p>
     * @return True if the contents are different, false if they are the same.
     ***************************************************************************************************/
    private boolean isContentDataModified(Map<Component, Object> oldContentMap, Map<Component, Object> newContentMap) {
        for (Map.Entry<Component, Object> entry : newContentMap.entrySet()) {
            if (!oldContentMap.containsKey(entry.getKey())) {
                return true;
            }
            if (!areObjectsEquals(entry.getValue(), oldContentMap.get(entry.getKey()))) {
                return true;
            }
        }
        return false;
    }

    /****************************************************************************************************
     * Compares two objects and return true if they are equal, false if they are not. It is possible that
     * this method needs to be internationalized.
     * <p>
     * @return True if the objects are equal, false if they are not.
     ***************************************************************************************************/
    private boolean areObjectsEquals(Object value1, Object value2) {
        if (value1 == null && value2 == null) {
            return true;
        }
        if (value1 == null || value2 == null) {
            return false;
        }
        if (value1 instanceof Object[] && value2 instanceof Object[]) {
            return Arrays.deepEquals((Object[]) value1, (Object[]) value2);
        }
        return value1.equals(value2);
    }
}
