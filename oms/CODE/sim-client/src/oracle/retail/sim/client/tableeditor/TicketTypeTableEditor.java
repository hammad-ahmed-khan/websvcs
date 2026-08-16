package oracle.retail.sim.client.tableeditor;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.JComponent;
import oracle.retail.sim.client.swing.displayer.TranslatedObjectDisplayer;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.widget.RComboBox;
import oracle.retail.sim.common.itemticket.TicketType;
import oracle.retail.sim.common.itemticket.TicketTypeId;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * A table editor for selecting an ticket type from a combo box display.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class TicketTypeTableEditor extends RComboBox implements SimTableEditor, ItemListener {
    private static final long serialVersionUID = -1189671440999886503L;

    private SimTableEditorEventAdaptor eventAdaptor;

    /****************************************************************************************************
     * Constructs editor.
     ***************************************************************************************************/
    public TicketTypeTableEditor() throws Exception {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setDisplayer(new TranslatedObjectDisplayer());
        loadTicketTypes();
        removeEmptySelection();
        addItemListener(this);
    }

    private void loadTicketTypes() throws Exception {
        List<TicketType> ticketTypes = ClientServiceFactory.getItemTicketServices().findTicketTypes();
        for (TicketType ticketType : ticketTypes) {
            if (ticketType.getId().equals(TicketTypeId.SHELF_LABEL_ID)) {
                addItem(ticketType);
            } else if (ticketType.getId().equals(TicketTypeId.ITEM_TICKET_ID)) {
                addItem(ticketType);
            }
        }
    }

    public Class getValueClass() {
        return TicketType.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public void setCoordinates(int row, int column) {
        // Ignore
    }

    public TicketType getTicketType() {
        return (TicketType) getValue();
    }

    public Object getValue() {
        return getSelectedItem();
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
