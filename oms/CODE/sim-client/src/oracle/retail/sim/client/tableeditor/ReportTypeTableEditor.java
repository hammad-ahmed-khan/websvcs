package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.tableeditor.RComboBoxTableEditor;
import oracle.retail.sim.common.report.ReportFormat;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;

/********************************************************************************************************
 * A table editor for selecting an report type from a combo box display.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReportTypeTableEditor extends RComboBoxTableEditor {
    private static final long serialVersionUID = -1189671440999886503L;

    private SimTableEditorEventAdaptor eventAdaptor;

    /****************************************************************************************************
     * Constructs editor.
     ***************************************************************************************************/
    public ReportTypeTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new AttributeDisplayer("name"));
        setSortEnabled(true);
        addItemListener(this);
    }

    public Class getValueClass() {
        return ReportTypeFormat.class;
    }

    public ReportTypeFormat getReportTypeFormat() {
        return (ReportTypeFormat) getSelectedItem();
    }

    public void setReportTypeFormats(List reportTypeFormats) {
        removeItemListener(this);
        setItems(reportTypeFormats);
        addItemListener(this);
    }

    public void setValueClass(Class valueClass) {
        // Ignored
    }

    public void setModel(Object model) {
        // Ignored
    }

    public ReportTypeFormat getReportFormat() {
        return (ReportTypeFormat) getValue();
    }

    public Object getValue() {
        Object object = getSelectedItem();
        if (object instanceof ReportFormat) {
            return ((ReportFormat) object).getCode();
        }
        return null;
    }

    public void setValue(Object value) {
        if (value != null) {
            setSelectedItem(value);
        } else {
            setEmptySelection();
        }
        eventAdaptor.fireTypeEditorEvent();
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