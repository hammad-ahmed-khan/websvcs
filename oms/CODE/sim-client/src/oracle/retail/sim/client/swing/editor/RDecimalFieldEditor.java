package oracle.retail.sim.client.swing.editor;

import java.math.BigDecimal;
import java.text.ParseException;
import javax.swing.SwingConstants;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.format.DecimalMask;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.format.Mask;

/********************************************************************************************************
 * This is a subclass of RTextFieldEditor that allows only decimal numbers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDecimalFieldEditor extends RNumberFieldEditor {
    private static final long serialVersionUID = -9141933040980466096L;

    /****************************************************************************************************
     * Constructor creates new decimal field editor.
     ***************************************************************************************************/
    public RDecimalFieldEditor() {
        initEditor();
    }

    /****************************************************************************************************
     * Constructor creates new decimal field editor with a title.
     * <p>
     * @param title The title to assign to the editor.
     ***************************************************************************************************/
    public RDecimalFieldEditor(String title) {
        super(title);
        initEditor();
    }

    /****************************************************************************************************
     * Creates a new RDecimalFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RDecimalFieldEditor(String title, boolean required) {
        super(title, required);
        initEditor();
    }

    /****************************************************************************************************
     * Initializes the editor.
     ***************************************************************************************************/
    private void initEditor() {
        super.setMask(new DecimalMask());
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
     * Sets the value of the field from a big decimal.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setDecimal(BigDecimal value) {
        if (value == null) {
            clear();
            return;
        }
        setDecimal(value.doubleValue());
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as a big decimal.
     * <p>
     * @return A BigDecimal representing the value in the editor.
     * @throws OldUIException Thrown if editor contains invalid content.
     ***************************************************************************************************/
    public BigDecimal getBigDecimal() throws UIException {
        Double value = getDouble();
        if (value == null) {
            return null;
        }
        return BigDecimal.valueOf(value);
    }

    /****************************************************************************************************
     * Sets the value of the field from a Double object.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setDecimal(Double value) {
        if (value == null) {
            clear();
            return;
        }
        setDecimal(value.doubleValue());
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as a double object.
     * <p>
     * @return A Double representing the value in the editor.
     * @throws OldUIException Thrown if editor contains invalid content.
     ***************************************************************************************************/
    public Double getDouble() throws UIException {
        String text = getText();
        if (StringUtility.isNullOrEmpty(text)) {
            return null;
        }
        try {
            Number number = LocaleManager.getNumberFormatter().parse(text);
            return number.doubleValue();
        } catch (ParseException exception) {
            throw getInvalidNumberException();
        }
    }

    /****************************************************************************************************
     * Sets the value of the field from a double.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setDecimal(double value) {
        setText(LocaleManager.getNumberFormatter().format(value));
    }
}
