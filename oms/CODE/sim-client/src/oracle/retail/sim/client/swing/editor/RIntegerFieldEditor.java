package oracle.retail.sim.client.swing.editor;

import java.text.ParseException;
import javax.swing.SwingConstants;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.format.IntegerMask;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.core.locale.NumberHelper;
import oracle.retail.sim.common.format.Mask;

/********************************************************************************************************
 * This is a subclass of RTextFieldEditor that allows only integer numbers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RIntegerFieldEditor extends RNumberFieldEditor {
    private static final long serialVersionUID = -5511334661253281476L;

    private IntegerMask mask = new IntegerMask();

    /****************************************************************************************************
     * Constructor creates new decimal field editor.
     ***************************************************************************************************/
    public RIntegerFieldEditor() {
        initEditor();
    }

    /****************************************************************************************************
     * Constructor creates new decimal field editor with a title.
     * <p>
     * @param title The title to assign to the editor.
     ***************************************************************************************************/
    public RIntegerFieldEditor(String title) {
        super(title);
        initEditor();
    }

    /****************************************************************************************************
     * Creates a new RIntegerFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RIntegerFieldEditor(String title, boolean required) {
        super(title, required);
        initEditor();
    }

    /****************************************************************************************************
     * Initializes the editor.
     ***************************************************************************************************/
    private void initEditor() {
        super.setMask(mask);
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
     * Assigns a maximum value the field may represent.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setMaximumValue(int value) {
        mask.setMaximumValue(value);
    }

    /****************************************************************************************************
     * Retrieves the maximum value the field may represent.
     * <p>
     * @return The maximum allowed value.
     ***************************************************************************************************/
    public int getMaximumValue() {
        return mask.getMaximumValue();
    }

    /****************************************************************************************************
     * Assigns a minimum value the field may represent.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setMinimumValue(int value) {
        mask.setMinimumValue(value);
    }

    /****************************************************************************************************
     * Retrieves the minimum value the field may represent.
     * <p>
     * @return The minimum allowed value.
     ***************************************************************************************************/
    public int getMinimumValue() {
        return mask.getMinimumValue();
    }

    /****************************************************************************************************
     * Sets the value of the field from an Integer object.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setInteger(Integer value) {
        if (value == null) {
            clear();
            return;
        }
        setInteger(value.intValue());
    }

    /****************************************************************************************************
     * Sets the value of the field from an Long object.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
     public void setInteger(Long value) {
        if (value == null) {
            clear();
            return;
        }
        setInteger(value.longValue());
    }

    /****************************************************************************************************
     * Sets the value of the field from a long. There is a disjoint between the display field and the
     * data in the database.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setInteger(long value) {
        setInteger((int) value);
    }

    /****************************************************************************************************
     * Sets the value of the field from an integer.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setInteger(int value) {
        setText(String.valueOf(value));
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as an Integer object.
     * <p>
     * @return An Integer representing the value in the editor.
     * @throws OldUIException
     ***************************************************************************************************/
    public Integer getInteger() throws UIException {
        return getIntegerValue();
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as an Integer object.
     * <p>
     * @return An Integer representing the value in the editor.
     * @throws OldUIException
     ***************************************************************************************************/
    public Integer getIntegerOrNull() throws UIException {
        String text = getText();
        if (StringUtility.isNullOrEmpty(text)) {
            return null;
        }
        try {
            return LocaleManager.getIntegerFormatter().parse(text).intValue();
        } catch (ParseException exception) {
            throw getInvalidNumberException();
        }
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as an integer. Return 0 for an empty field.
     * <p>
     * @return An integer representing the value in the editor.
     * @throws Number Format Excpetion for an invalid integer or empty field.
     ***************************************************************************************************/
    public int getIntegerValue() throws UIException {
        String text = getText();
        if (StringUtility.isNullOrEmpty(text)) {
            return 0;
        }
        try {
            return LocaleManager.getIntegerFormatter().parse(text).intValue();
        } catch (ParseException exception) {
            throw getInvalidNumberException();
        }
    }

    /****************************************************************************************************
     * Returns true if the current value of the integer editor is a positive integer.
     ***************************************************************************************************/
    public boolean isPositiveInteger() {
        return NumberHelper.isIdentifierNumeric(getText());
    }
}
