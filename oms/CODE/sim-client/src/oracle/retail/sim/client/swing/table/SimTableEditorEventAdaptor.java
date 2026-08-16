package oracle.retail.sim.client.swing.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.SwingUtilities;

/********************************************************************************************************
 * A table editor event adaptor helps fire events to the appropriate listeners.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimTableEditorEventAdaptor {

    private List<SimTableEditorListener> listeners = new ArrayList<>();
    private SimTableEditor editor;

    /****************************************************************************************************
     * Creates a table editor adaptor around the specified table editor.
     * @param editor The table editor.
     ***************************************************************************************************/
    public SimTableEditorEventAdaptor(SimTableEditor editor) {
        this.editor = editor;
    }

    /****************************************************************************************************
     * Adds a TableEditorListener to the listener collection.
     * @param listener The listener
     ***************************************************************************************************/
    public void addTableEditorListener(SimTableEditorListener listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(0, listener);
    }

    /****************************************************************************************************
     * Removes the specified listener from the listener list.
     * @param listener The listener
     ***************************************************************************************************/
    public void removeTableEditorListener(SimTableEditorListener listener) {
        if (listeners.remove(listener)) {
            return;
        }
        else if (listener instanceof DisplayerTableCellEditor) {
            for (Iterator<SimTableEditorListener> iter = listeners.iterator(); iter.hasNext(); ) {
                SimTableEditorListener nextListener = iter.next();
                if(nextListener instanceof DisplayerTableCellEditor) {
                    iter.remove();
                }
            }
        }

    }

    /****************************************************************************************************
     * Notifies all listeners of the event.
     ***************************************************************************************************/
    public void fireTypeEditorEvent() {
        for (SimTableEditorListener listener : listeners) {
            listener.performTableEditorEvent(new SimTableEditorEvent(editor));
        }
    }

    /****************************************************************************************************
     * Notifies all listeners of the event.
     * @param isFocusLostEvent True if editor event was fired because the focus was lost.
     ***************************************************************************************************/
    public void fireTypeEditorEvent(boolean isFocusLostEvent) {
        for (SimTableEditorListener listener : listeners) {
            listener.performTableEditorEvent(new SimTableEditorEvent(editor, isFocusLostEvent));
        }
    }

    /****************************************************************************************************
     * Notifies all listeners of the event later.
     ***************************************************************************************************/
    public void fireTypeEditorEventLater() {
        SwingUtilities.invokeLater(new TableEditorFireEvent());
    }

    /****************************************************************************************************
     * Table Editor Fire Event
     ***************************************************************************************************/
    private class TableEditorFireEvent implements Runnable {
        public void run() {
            fireTypeEditorEvent();
        }
    }
}
