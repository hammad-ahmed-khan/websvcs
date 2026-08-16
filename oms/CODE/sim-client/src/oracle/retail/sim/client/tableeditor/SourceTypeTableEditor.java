package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.common.source.SourceType;

/*******************************************************************************
 * A table editor for Source Type objects that displays a combo box.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************/
public class SourceTypeTableEditor extends RComboBox implements SimTableEditor, ItemListener {
    private static final long serialVersionUID = -3639342051260672162L;

    private SimTableEditorEventAdaptor eventAdaptor;

    public SourceTypeTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new TranslatedObjectDisplayer());
        setSelectionRequired(true);
        setItems(Arrays.asList(SourceType.values()));
        removeEmptySelection();
        addItemListener(this);
    }

    public Class getValueClass() {
        return SourceType.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignored
    }

    public void setModel(Object model) {
        // Ignored
    }

    public void setCoordinates(int row, int column) {
        // Ignored
    }

    public SourceType getSourceType() {
        return (SourceType) getValue();
    }

    public Object getValue() {
        if (getSelectedIndex() > -1) {
            return getSelectedItem();
        }
        return null;
    }

    public void setValue(Object value) {
        if (getSelectedIndex() > -1) {
            setSelectedItem(value);
            eventAdaptor.fireTypeEditorEvent();
        }
    }

    public JComponent getComponent() {
        return this;
    }

    public boolean checkValue() {
        return true;
    }

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            eventAdaptor.fireTypeEditorEvent();
        }
    }

    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }
}
