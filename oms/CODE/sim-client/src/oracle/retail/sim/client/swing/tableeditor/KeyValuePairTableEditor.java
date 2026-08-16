package oracle.retail.sim.client.swing.tableeditor;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEvent;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.common.config.ConfigurationOption;

/********************************************************************************************************
 * A table editor for KeyValuePair objects. This table editors figures out if the value is a Boolean,
 * Date, Number, String or Object and display it appropriately.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class KeyValuePairTableEditor implements SimTableEditor, SimTableEditorListener {

    private SimTableEditor currentEditor;
    private SimTableEditorEventAdaptor eventAdaptor;

    private Map classEditorMap = new HashMap<>();
    private Map keyEditorMap = new HashMap<>();

    /****************************************************************************************************
     * Constructor.
     *
     * @param columnName The identifer of the value that will be edited (if the value is of type String).
     *            The column names maps to the textlength.properties to determine the allowed length of
     *            the entry field.
     ***************************************************************************************************/
    public KeyValuePairTableEditor(String columnName) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        addClassEditor(Boolean.class, new BooleanTableEditor());
        addClassEditor(Date.class, new DateTableEditor(true));
        addClassEditor(Integer.class, new IntegerTableEditor());
        addClassEditor(Number.class, new NumberTableEditor());
        addClassEditor(String.class, new StringTableEditor(columnName));
    }

    /****************************************************************************************************
     * Basic Property Methods of a Table Editor
     ***************************************************************************************************/

    public Class getValueClass() {
        return currentEditor.getValueClass();
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public void setCoordinates(int row, int column) {
        if (currentEditor != null) {
            currentEditor.setCoordinates(row, column);
        }
    }

    public JComponent getComponent() {
        if (currentEditor == null) {
            return null;
        }
        return currentEditor.getComponent();
    }

    /**
     * Assigns a table editor to be used for the particular class type.
     */
    public void addClassEditor(Class classType, SimTableEditor editor) {
        classEditorMap.put(classType, editor);
        editor.addTableEditorListener(this);
    }

    /**
     * Assigns a table editor to be used for the particlar object.
     */
    public void addKeyEditor(Object key, SimTableEditor editor) {
        keyEditorMap.put(key, editor);
        editor.addTableEditorListener(this);
    }

    /****************************************************************************************************
     * Get, Set and Check the data value of the editor
     ***************************************************************************************************/

    public Object getValue() {
        return currentEditor.getValue();
    }

    /**
     * If the value is a KeyValuePair object, then the key will be used to find an editor, and then the
     * value will be assigned to the editor.
     */
    public void setValue(Object value) {
        if (value != null && value instanceof ConfigurationOption) {
            ConfigurationOption configOption = (ConfigurationOption) value;

            if (configOption.isPropertyModifiable(configOption.getConfigKey())) {
                // Retrieve editor associated with the config key
                currentEditor = getEditor(configOption.getConfigKey());

                // no editor found, so get editor based on the Data Class Type of config value
                if (currentEditor == null) {
                    currentEditor = getEditor(configOption.getConfigValue().getClass());
                }

                if (currentEditor != null) {
                    Object configValue = configOption.getConfigValue();
                    if (configValue != null) {
                        currentEditor.setValueClass(configValue.getClass());
                    }
                    currentEditor.setValue(configValue);
                }
            } else {
                currentEditor = null; // config value is non-editable, do not assign any editors
            }
        }
    }

    public boolean checkValue() {
        return currentEditor.checkValue();
    }

    /****************************************************************************************************
     * Retrieves the correct editor for the Class object passed in.
     ***************************************************************************************************/
    private SimTableEditor getEditor(Class classType) {
        if (classEditorMap.containsKey(classType)) {
            return (SimTableEditor) classEditorMap.get(classType);
        }
        Class checkClass = null;
        for (Iterator iterator = classEditorMap.keySet().iterator(); iterator.hasNext();) {
            checkClass = (Class) iterator.next();

            if (checkClass.isAssignableFrom(classType)) {
                SimTableEditor editor = (SimTableEditor) classEditorMap.get(checkClass);
                addClassEditor(classType, editor);
                return editor;
            }
        }
        return null;
    }

    /****************************************************************************************************
     * Retrieves the correct editor for the Object passed in.
     ***************************************************************************************************/
    private SimTableEditor getEditor(Object key) {
        return (SimTableEditor) keyEditorMap.get(key);
    }

    /****************************************************************************************************
     * Table Editor Listener
     ***************************************************************************************************/
    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    public void performTableEditorEvent(SimTableEditorEvent event) {
        eventAdaptor.fireTypeEditorEvent();
    }
}
