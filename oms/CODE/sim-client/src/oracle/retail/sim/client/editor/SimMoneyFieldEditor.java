package oracle.retail.sim.client.editor;

import java.math.BigDecimal;
import oracle.retail.sim.client.swing.editor.RMoneyFieldEditor;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.currency.SimMoney;

/********************************************************************************************************
 * This is sim specific currency editor that allows the developer to set() and get() a SimMoney object
 * from the editor.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimMoneyFieldEditor extends RMoneyFieldEditor {
    private static final long serialVersionUID = 5912060741844046311L;

    /****************************************************************************************************
     * Creates a new SCurrencyFieldEditor with no title.
     ***************************************************************************************************/
    public SimMoneyFieldEditor() {
    }

    /****************************************************************************************************
     * Creates a new SCurrencyFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public SimMoneyFieldEditor(String title) {
        super(title);
    }

    /****************************************************************************************************
     * Creates a new SCurrencyFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public SimMoneyFieldEditor(String title, boolean required) {
        super(title, required);
    }

    /****************************************************************************************************
     * Assigns the monetary amount to the editor.
     * <p>
     * @param money The money to assign.
     ***************************************************************************************************/
    public void setMoney(SimMoney money) {
        if (money == null) {
            clear();
            return;
        }
        setCurrency(money.getCurrency());
        setAmount(money.getAmount());
    }

    /****************************************************************************************************
     * Retrieves the monetary amount to the editor. Returns null if no monetary amount has been assigned.
     * <p>
     * @return The monetary amount of the editor.
     * @throws OldUIException Thrown if the field does not contain a valid money value.
     ***************************************************************************************************/
    public SimMoney getMoney() throws UIException {
        BigDecimal amount = getAmount();
        if (amount == null) {
            return null;
        }
        return new SimMoney(amount, getCurrency());
    }
}
