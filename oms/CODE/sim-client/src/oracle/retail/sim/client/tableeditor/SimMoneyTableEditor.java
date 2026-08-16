package oracle.retail.sim.client.tableeditor;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.util.Currency;
import javax.swing.JComponent;
import oracle.retail.sim.client.application.Application;
import oracle.retail.sim.client.swing.dialog.RErrorDialog;
import oracle.retail.sim.client.swing.logging.UILog;
import oracle.retail.sim.client.swing.table.SimTable;
import oracle.retail.sim.client.swing.table.SimTableEditor;
import oracle.retail.sim.client.swing.table.SimTableEditorEventAdaptor;
import oracle.retail.sim.client.swing.table.SimTableEditorListener;
import oracle.retail.sim.client.swing.table.SimTableException;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.widget.RMoneyField;
import oracle.retail.sim.common.business.CommonMessageText;
import oracle.retail.sim.common.business.MessageText;
import oracle.retail.sim.common.currency.SimMoney;

/********************************************************************************************************
 * A table editor to editor a SimMoney object using the RMoneyField widget.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimMoneyTableEditor extends RMoneyField implements SimTableEditor {
    private static final long serialVersionUID = 7639639868321826798L;

    private SimTableEditorEventAdaptor eventAdaptor;
    private boolean isNullable;
    private int row = -1;
    private int column = -1;

    public SimMoneyTableEditor() {
        this(false);
    }

    public SimMoneyTableEditor(boolean isNullable) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setIdentifier("Item.unitCost");
        setIsNullable(isNullable);
        addFocusListener(createFocusListener());
    }

    public SimMoneyTableEditor(Currency currency) {
        this(false, currency);
    }

    public SimMoneyTableEditor(boolean isNullable, Currency currency) {
        eventAdaptor = new SimTableEditorEventAdaptor(this);
        setIdentifier("Item.unitCost");
        addFocusListener(createFocusListener());
        setCurrency(currency);
        setIsNullable(isNullable);
    }

    public void setIsNullable(boolean isNullable) {
        this.isNullable = isNullable;
    }

    public Class getValueClass() {
        return SimMoney.class;
    }

    public void setValueClass(Class valueClass) {
        // Ignore
    }

    public void setModel(Object model) {
        // Ignore
    }

    public Object getValue() {
        try {
            BigDecimal amount = getAmount();
            if (amount == null) {
                if (isNullable) {
                    return null;
                }
                amount = BigDecimal.ZERO;
            }
            return new SimMoney(amount, getCurrency());
        } catch (Throwable exception) {
            throw new SimTableException(exception);
        }
    }

    /**
     * By default, RMoneyField is designed to have a currency set upon creation and never changed after
     * that. Because we extend from RMoneyField and do not know what the value Object is going to be,
     * currency must be set before amount or there will be no context for the amount when attempting to
     * format it.
     */
    public void setValue(Object value) {
        if (value instanceof SimMoney) {
            SimMoney money = (SimMoney) value;
            setCurrency(money.getCurrency());
            setAmount(money.getAmount());
        } else if (isNullable) {
            setAmount(null);
        } else {
            setAmount(BigDecimal.ZERO);
        }
    }

    public JComponent getComponent() {
        return this;
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

    /****************************************************************************************************
     * Check The Value For Validity
     ***************************************************************************************************/

    public boolean checkValue() {
        try {
            BigDecimal amount = getAmount();
            if (amount == null) {
                if (!isNullable) {
                    displayError(CommonMessageText.UNIT_COST_BLANK_ERROR);
                }
                return isNullable;
            }
            new SimMoney(amount, getCurrency());
            return true;
        } catch (UIException exception) {
            displayError(exception);
        }
        return false;
    }

    public void addTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.addTableEditorListener(listener);
    }

    public void removeTableEditorListener(SimTableEditorListener listener) {
        eventAdaptor.removeTableEditorListener(listener);
    }

    private FocusListener createFocusListener() {
        return new FocusAdapter() {
            public void focusLost(FocusEvent event) {
                if (event.isTemporary()) {
                    return;
                }
                if (checkValue()) {
                    eventAdaptor.fireTypeEditorEvent();
                    return;
                }
                reactivateEditing(event.getOppositeComponent());
            }
        };
    }

    public boolean isInvalidKeystroke(KeyEvent event) {
        return false;
    }

    protected void displayError(MessageText message) {
        RErrorDialog dialog = new RErrorDialog(Application.getFrame());
        dialog.setTitle("Table Editor Error");
        dialog.setMessage(message);
        dialog.activate();
    }

    private void displayError(UIException exception) {
        RErrorDialog dialog = new RErrorDialog(Application.getFrame());
        dialog.setTitle("Table Editor Error");
        dialog.setMessage(exception);
        dialog.activate();
    }
}
