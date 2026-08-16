package oracle.retail.sim.client.screen.customuin;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import javax.swing.JComponent;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.core.SimName;
import oracle.retail.sim.client.core.SimRepository;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.screen.uin.SerialNumberWrapper;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.displayer.AttributeDisplayer;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.util.UIMessageText;
import oracle.retail.sim.client.swing.widget.RTextField;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.business.FunctionalArea;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.item.ItemMessageText;
import oracle.retail.sim.common.uin.SerialNumberValue;
import oracle.retail.sim.common.uin.UINMessageText;
import oracle.retail.sim.service.core.ClientServiceFactory;

/********************************************************************************************************
 * Table Editor for editing a CustomerSerialNumber business object. The model for the table row that uses this editor
 * must be a UINWrapper.
 * <p>
 * This is future work-in-progress code for future SIM 14.0 or later release.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class CustomUINCreateDialogTableEditor extends RTextField implements SimTableEditor, FocusListener {
    private static final long serialVersionUID = -1463881160730583097L;

    private AbstractDisplayer displayer;
    private SimTableEditorEventAdaptor eventAdaptor;
    private SerialNumberWrapper wrapper;
    private SerialNumberValue serialNumberValue;
    private String lastCheckedSerialNumber = StringConstants.EMPTY;
    private FocusEvent lastFocusEvent;
    private boolean isErrorState;
    private int row = -1;
    private int column = -1;

    public CustomUINCreateDialogTableEditor() {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        displayer = new AttributeDisplayer("uin");
        setIdentifier(SimName.SERIAL_NUMBER);
        setMargin(null);
        setBorder(null);
        addFocusListener(this);
    }

    public Class getValueClass() {
        return SerialNumberValue.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        wrapper = (SerialNumberWrapper) model;
    }

    /****************************************************************************************************
     * Assign coordinates to the editor.
     ***************************************************************************************************/
    public void setCoordinates(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /****************************************************************************************************
     * Reactivate editing within the table cell.
     ***************************************************************************************************/
    private void reactivateEditing(Object object) {
        if (object instanceof SimTable) {
            SimTable table = (SimTable) object;
            try {
                if (table.getSelectedRow() != row) {
                    table.setRowSelectionInterval(row, row);
                }
                table.editCellAt(row, column);
            } catch (Throwable ex) {
                UILog.debug(getClass(), ex);
            }
        }
    }

    public Object getValue() {
        if (isErrorState()) {
            return null;
        }
        return serialNumberValue;
    }

    public void setData(Object value) {
        if (value instanceof SerialNumberValue) {
            setValue(value);
            return;
        }
        throw new IllegalArgumentException("CustomSerialNumberTableEditor only edits UINValue!");
    }

    public void setValue(Object value) {
        if (value instanceof String) {
            setText((String) value);
            checkValue();
        } else if (value instanceof SerialNumberValue) {
            serialNumberValue = (SerialNumberValue) value;
            setText(displayer.getDisplayText(serialNumberValue));
        } else if (value == null) {
            serialNumberValue = null;
            clear();
        }
        eventAdaptor.fireTypeEditorEvent();
    }

    public JComponent getComponent() {
        return this;
    }

    public boolean checkValue() {
        String enteredSerialNumber = getText();

        setErrorState(false);

        if (StringUtility.isNullOrEmpty(enteredSerialNumber)) {
            if (serialNumberValue == null) {
                return true;
            }
            displayErrorWithReset(ItemMessageText.ITEM_BLANK_ERROR, null);
            return false;
        }

        try {
            if (serialNumberValue != null && enteredSerialNumber.equals(serialNumberValue.getUin()) && enteredSerialNumber.equals(lastCheckedSerialNumber)) {
                return true;
            }
            SerialNumberValue tempValue = ClientServiceFactory.getUINServices().findSerialNumberValueOrCreate(SimRepository.getStoreId(), wrapper.getItemId(), enteredSerialNumber, wrapper.getType(), FunctionalArea.MANUAL);
            if (tempValue == null) {
                Object[] values = new Object[3];
                values[0] = wrapper.getType().toString();
                values[1] = enteredSerialNumber;
                values[2] = wrapper.getItemId();
                displayErrorWithReset(UINMessageText.UIN_NOT_FOUND_FOR_ITEM, values);
                return false;
            }
            serialNumberValue = tempValue;
            lastCheckedSerialNumber = enteredSerialNumber;
            return true;
        } catch (BusinessException businessException) {
            displayError(businessException.getPrimaryMessageText(), businessException.getPrimaryMessageValues());
        } catch (Throwable t) {
            displayError(UIMessageText.DEFAULT_FATAL_MESSAGE, null);
        }
        lastCheckedSerialNumber = StringConstants.EMPTY;
        serialNumberValue = null;
        clear();
        return false;
    }

    /****************************************************************************************************
     * Add Remove Table Editor Listeners
     ***************************************************************************************************/

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    /****************************************************************************************************
     * Error State
     ***************************************************************************************************/
    protected void setErrorState(boolean errorState) {
        isErrorState = errorState;
    }

    protected boolean isErrorState() {
        return isErrorState;
    }

    /****************************************************************************************************
     * Determines if keystroke is invalid for input into the field.
     ***************************************************************************************************/
    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }

    /****************************************************************************************************
     * Implement the focus listener methods to call do value modified when focus is lost.
     ***************************************************************************************************/
    public void focusGained(FocusEvent event) {
    }

    public void focusLost(FocusEvent event) {
        if (event.isTemporary()) {
            return;
        }
        if (event != lastFocusEvent) {
            lastFocusEvent = event;
            if (checkValue()) {
                eventAdaptor.fireTypeEditorEvent(true);
                return;
            }
            reactivateEditing(event.getOppositeComponent());
        }
    }

    /****************************************************************************************************
     * Display Error Methods
     ***************************************************************************************************/

    protected void displayErrorWithReset(MessageText message, Object[] messageValues) {
        if (displayer != null) {
            setText(displayer.getDisplayText(serialNumberValue));
        }
        setErrorState(true);
        displayError(message, messageValues);
    }

    protected void displayError(MessageText message, Object[] messageValues) {
        RErrorDialog dialog = new RErrorDialog(Application.getFrame());
        dialog.setTitle("Table Editor Error");
        if (messageValues == null) {
            dialog.setMessage(message);
        } else {
            dialog.setMessage(message, messageValues);
        }
        dialog.activate();
    }
}
