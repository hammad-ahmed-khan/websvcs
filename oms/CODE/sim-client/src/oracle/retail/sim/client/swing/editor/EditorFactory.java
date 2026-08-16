package oracle.retail.sim.client.swing.editor;

import oracle.retail.sim.client.swing.lov.RListOfValuesEditor;

/******************************************************************************************
 * This class contains static factory methods for creating new editors. All editors should
 * subclass JPanel. This allows them to be creative with their internal layouts, but also
 * allows broad usage of the editor once it is retrieved.
 *
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class EditorFactory {

    /******************************************************************************************
     * Static constructor. This class cannot be instantiated.
     ******************************************************************************************/
    private EditorFactory() {
    }

    /******************************************************************************************
     * Creates a new checkbox editor with no associated title.
     ******************************************************************************************/
    public static RCheckBoxEditor createCheckBoxEditor() {
        return new RCheckBoxEditor();
    }

    /******************************************************************************************
     * Creates a new checkbox editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RCheckBoxEditor createCheckBoxEditor(String title) {
        return new RCheckBoxEditor(title);
    }

    /******************************************************************************************
     * Creates a new checkbox editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RCheckBoxEditor createCheckBoxEditor(String title, boolean required) {
        RCheckBoxEditor editor = new RCheckBoxEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new combobox editor with no associated title.
     ******************************************************************************************/
    public static RComboBoxEditor createComboBoxEditor() {
        return new RComboBoxEditor();
    }

    /******************************************************************************************
     * Creates a new combobox editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RComboBoxEditor createComboBoxEditor(String title) {
        return new RComboBoxEditor(title);
    }

    /******************************************************************************************
     * Creates a new combobox editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RComboBoxEditor createComboBoxEditor(String title, boolean required) {
        RComboBoxEditor editor = new RComboBoxEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new money field editor with no associated title.
     ******************************************************************************************/
    public static RMoneyFieldEditor createMoneyFieldEditor() {
        return new RMoneyFieldEditor();
    }

    /******************************************************************************************
     * Creates a new money field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RMoneyFieldEditor createMoneyFieldEditor(String title) {
        return new RMoneyFieldEditor(title);
    }

    /******************************************************************************************
     * Creates a new money field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RMoneyFieldEditor createMoneyFieldEditor(String title, boolean required) {
        RMoneyFieldEditor editor = new RMoneyFieldEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new date field editor with no associated title.
     ******************************************************************************************/
    public static RDateFieldEditor createDateFieldEditor() {
        return new RDateFieldEditor();
    }

    /******************************************************************************************
     * Creates a new date field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RDateFieldEditor createDateFieldEditor(String title) {
        return new RDateFieldEditor(title);
    }

    /******************************************************************************************
     * Creates a new date field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RDateFieldEditor createDateFieldEditor(String title, boolean required) {
        RDateFieldEditor editor = new RDateFieldEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new decimal field editor with no associated title.
     ******************************************************************************************/
    public static RDecimalFieldEditor createDecimalFieldEditor() {
        return new RDecimalFieldEditor();
    }

    /******************************************************************************************
     * Creates a new decimal field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RDecimalFieldEditor createDecimalFieldEditor(String title) {
        return new RDecimalFieldEditor(title);
    }

    /******************************************************************************************
     * Creates a new decimal field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RDecimalFieldEditor createDecimalFieldEditor(String title, boolean required) {
        RDecimalFieldEditor editor = new RDecimalFieldEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new integer field editor with no associated title.
     ******************************************************************************************/
    public static RIntegerFieldEditor createIntegerFieldEditor() {
        return new RIntegerFieldEditor();
    }

    /******************************************************************************************
     * Creates a new integer field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RIntegerFieldEditor createIntegerFieldEditor(String title) {
        return new RIntegerFieldEditor(title);
    }

    /******************************************************************************************
     * Creates a new integer field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RIntegerFieldEditor createIntegerFieldEditor(String title, boolean required) {
        RIntegerFieldEditor editor = new RIntegerFieldEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new list editor with no associated title.
     ******************************************************************************************/
    public static RListEditor createListEditor() {
        return new RListEditor();
    }

    /******************************************************************************************
     * Creates a new list editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RListEditor createListEditor(String title) {
        return new RListEditor(title);
    }

    /******************************************************************************************
     * Creates a new list of values editor with no associated title.
     ******************************************************************************************/
    public static RListOfValuesEditor createListOfValuesEditor() {
        return new RListOfValuesEditor();
    }

    /******************************************************************************************
     * Creates a new list of values editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RListOfValuesEditor createListOfValuesEditor(String title) {
        return new RListOfValuesEditor(title);
    }

    /******************************************************************************************
     * Creates a new list of values editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RListOfValuesEditor createListOfValuesEditor(String title, boolean required) {
        RListOfValuesEditor editor = new RListOfValuesEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new longfield editor with no associated title.
     ******************************************************************************************/
    public static RLongFieldEditor createLongFieldEditor() {
        return new RLongFieldEditor();
    }

    /******************************************************************************************
     * Creates a new longfield editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RLongFieldEditor createLongFieldEditor(String title) {
        return new RLongFieldEditor(title);
    }

    /******************************************************************************************
     * Creates a new longfield editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RLongFieldEditor createLongFieldEditor(String title, boolean required) {
        RLongFieldEditor editor = new RLongFieldEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new password field editor with no associated title.
     ******************************************************************************************/
    public static RPasswordFieldEditor createPasswordFieldEditor() {
        return new RPasswordFieldEditor();
    }

    /******************************************************************************************
     * Creates a new password field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RPasswordFieldEditor createPasswordFieldEditor(String title) {
        return new RPasswordFieldEditor(title);
    }

    /******************************************************************************************
     * Creates a new password field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RPasswordFieldEditor createPasswordFieldEditor(String title, boolean required) {
        RPasswordFieldEditor editor = new RPasswordFieldEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new percent field editor with no associated title.
     ******************************************************************************************/
    public static RPercentFieldEditor createPercentFieldEditor() {
        return new RPercentFieldEditor();
    }

    /******************************************************************************************
     * Creates a new percent field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RPercentFieldEditor createPercentFieldEditor(String title) {
        return new RPercentFieldEditor(title);
    }

    /******************************************************************************************
     * Creates a new percent field editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RPercentFieldEditor createPercentFieldEditor(String title, boolean required) {
        RPercentFieldEditor editor = new RPercentFieldEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new textfield editor with no associated title.
     ******************************************************************************************/
    public static RTextFieldEditor createTextFieldEditor() {
        return new RTextFieldEditor();
    }

    /******************************************************************************************
     * Creates a new textfield editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RTextFieldEditor createTextFieldEditor(String title) {
        return new RTextFieldEditor(title);
    }

    /******************************************************************************************
     * Creates a new textfield editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RTextFieldEditor createTextFieldEditor(String title, boolean required) {
        RTextFieldEditor editor = new RTextFieldEditor(title);
        editor.setRequired(required);
        return editor;
    }

    /******************************************************************************************
     * Creates a new text area editor with no associated title.
     ******************************************************************************************/
    public static RTextAreaEditor createTextAreaEditor() {
        return new RTextAreaEditor();
    }

    /******************************************************************************************
     * Creates a new text area editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     ******************************************************************************************/
    public static RTextAreaEditor createTextAreaEditor(String title) {
        return new RTextAreaEditor(title);
    }

    /******************************************************************************************
     * Creates a new text area editor with associated title.
     * <p>
     * @param title The title to assign to the editor.
     * @param required True if the field is considered required, false if not.
     ******************************************************************************************/
    public static RTextAreaEditor createTextAreaEditor(String title, boolean required) {
        RTextAreaEditor editor = new RTextAreaEditor(title);
        editor.setRequired(required);
        return editor;
    }
}
