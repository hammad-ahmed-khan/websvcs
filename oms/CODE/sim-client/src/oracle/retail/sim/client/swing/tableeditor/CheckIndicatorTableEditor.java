package oracle.retail.sim.client.swing.tableeditor;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.table.SimPopupTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RCheckBox;

/********************************************************************************************************
 * Table editor that handles a boolean true/false value, displays a non-editable check box. If the cell
 * is clicked twice, a popup dialog is triggered and the data model is passed to the dialog.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CheckIndicatorTableEditor extends RCheckBox implements SimPopupTableEditor {
    private static final long serialVersionUID = -4236326754891359417L;

    private PopupTableEditorListener listener;
    private SimTableEditorEventAdaptor eventAdaptor;
    private Object model;

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public CheckIndicatorTableEditor(PopupTableEditorListener listener) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        addMouseListener(buildMouseListener());
        setHorizontalAlignment(CENTER);
        setBackgroundPaintActivated(false);
        setOpaque(true);
        setEnabled(false);
        setListener(listener);
    }

    /****************************************************************************************************
     * Basic Property Methods of a Table Editor
     ***************************************************************************************************/

    public Class getValueClass() {
        return Boolean.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public JComponent getComponent() {
        return this;
    }

    private void setListener(PopupTableEditorListener listener) {
        this.listener = listener;
    }

    public void setCoordinates(int row, int column) {
        // Ignore
    }

    /****************************************************************************************************
     * Get, Set and Check the data value of the editor
     ***************************************************************************************************/

    public Object getValue() {
        return isSelected();
    }

    public Boolean getBoolean() {
        return isSelected();
    }

    public void setValue(Object value) {
        if (value instanceof Boolean) {
            setSelected((Boolean) value);
            if (isSelected()) {
                popupDialog();
            }
            return;
        }
        setSelected(false);
    }

    public boolean checkValue() {
        return true;
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

    /****************************************************************************************************
     * Determines if keystroke is invalid for field. Always false for a boolean editor.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }

    /****************************************************************************************************
     * Mouse Listener That Pops The Dialog
     ***************************************************************************************************/
    private MouseListener buildMouseListener() {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    if (isSelected()) {
                        popupDialog();
                    }
                }
            }
        };
    }

    /****************************************************************************************************
     * Display Dialog
     ***************************************************************************************************/

    private void popupDialog() {
        if (listener != null) {
            listener.popupDialog(model);
        }
    }
}
