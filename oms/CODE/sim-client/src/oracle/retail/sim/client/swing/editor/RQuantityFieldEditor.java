package oracle.retail.sim.client.swing.editor;

import java.text.ParseException;
import javax.swing.SwingConstants;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.format.QuantityMask;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.format.Mask;

/********************************************************************************************************
 * This is a subclass of RDecimalFieldEditor that allows only quantites.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RQuantityFieldEditor extends RNumberFieldEditor {
    private static final long serialVersionUID = 2397874131345803563L;

    /****************************************************************************************************
     * Constructor creates new Quantity field editor.
     ***************************************************************************************************/
    public RQuantityFieldEditor() {
        initEditor();
    }

    /****************************************************************************************************
     * Constructor creates new Quantity field editor with a title.
     * <p>
     * @param title The title to assign to the editor.
     ***************************************************************************************************/
    public RQuantityFieldEditor(String title) {
        super(title);
        initEditor();
    }

    /****************************************************************************************************
     * Creates a new Quantity field editor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RQuantityFieldEditor(String title, boolean required) {
        super(title, required);
        initEditor();
    }

    /****************************************************************************************************
     * Initializes the editor.
     ***************************************************************************************************/
    private void initEditor() {
        super.setMask(new QuantityMask());
        getTextField().setHorizontalAlignment(SwingConstants.RIGHT);
    }

    /****************************************************************************************************
     * This method has been overriden to do nothing. The mask cannot be set.
     ***************************************************************************************************/
    public void setMask(Mask formatMask) {
    }

    /****************************************************************************************************
     * This method has been overridden to do nothing. The mask cannot be removed.
     ***************************************************************************************************/
    public void removeMask() {
    }

    /****************************************************************************************************
     * Sets the value of the field from a quantity.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setQuantity(Quantity value) {
        if (value == null) {
            clear();
            return;
        }
        setText(String.valueOf(value.doubleValue()));
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as a Quantity.
     * <p>
     * @return A Quantity representing the value in the editor.
     * @throws OldUIException Thrown if editor contains invalid content.
     ***************************************************************************************************/
    public Quantity getQuantity() throws UIException {
        String value = getText();
        if (StringUtility.isNullOrEmpty(value)) {
            return null;
        }
        try {
            return new Quantity(LocaleManager.getNumberFormatter().parse(value).doubleValue());
        } catch (ParseException exception) {
            throw getInvalidNumberException();
        }
    }
}
