package oracle.retail.sim.client.swing.editor;

import java.text.ParseException;
import javax.swing.SwingConstants;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.format.NumericIdMask;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.format.Mask;

/********************************************************************************************************
 * This is a subclass of RTextFieldEditor that helps with numeric IDs, which may be retrieved as Integers
 * or Long.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RNumericIdEditor extends RNumberFieldEditor {
    private static final long serialVersionUID = -5361910829705947680L;

    private NumericIdMask mask = new NumericIdMask();

    /****************************************************************************************************
     * Constructor creates new numer ID editor with a title.
     * <p>
     * @param title The title to assign to the editor.
     ***************************************************************************************************/
    public RNumericIdEditor(String title, String idType) {
        super(title);
        super.setMask(mask);
        mask.setIdType(idType);
    }

    /****************************************************************************************************
     * Creates a new RNumericIDEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RNumericIdEditor(String title, String idType, boolean required) {
        super(title, required);
        super.setMask(mask);
        mask.setIdType(idType);
    }

    /****************************************************************************************************
     * Sets entry alignment to the right.
     ***************************************************************************************************/
    public void setEntryAlignmentRight() {
        getTextField().setHorizontalAlignment(SwingConstants.RIGHT);
    }

    /****************************************************************************************************
     * Sets entry alignment to the left.
     ***************************************************************************************************/
    public void setEntryAlignmentLeft() {
        getTextField().setHorizontalAlignment(SwingConstants.LEFT);
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
     * Retrieves the value of the editor as an Integer object.
     * <p>
     * @return An Integer representing the value in the editor.
     * @throws OldUIException
     ***************************************************************************************************/
    public Integer getInteger() throws UIException {
        return getIntegerValue();
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
     * Retrieves the value of the editor as an integer. Return 0 for an empty field.
     * <p>
     * @return An integer representing the value in the editor.
     * @throws Number Format Excpetion for an invalid integer or empty field.
     ***************************************************************************************************/
    public int getIntegerValue() throws UIException {
        try {
            String text = getText();
            if (StringUtility.isNullOrEmpty(text)) {
                return 0;
            }
            return LocaleManager.getIntegerFormatter().parse(text).intValue();
        } catch (ParseException exception) {
            throw getInvalidNumberException();
        }
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
     * Sets the value of the field from an Long object.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setLong(Long value) {
        if (value == null) {
            clear();
            return;
        }
        setLong(value.longValue());
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as an Long object.
     * <p>
     * @return An Long representing the value in the editor.
     * @throws OldUIException
     ***************************************************************************************************/
    public Long getLong() throws UIException {
        return getLongValue();
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as an Long object.
     * <p>
     * @return An Long representing the value in the editor. Null if the field is blank.
     * @throws OldUIException
     ***************************************************************************************************/
    public Long getLongOrNull() throws UIException {
        if (isEmpty()) {
            return null;
        }
        return getLongValue();
    }

    /****************************************************************************************************
     * Sets the value of the field from an long.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setLong(long value) {
        setText(String.valueOf(value));
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as an long. Return 0 for an empty field.
     * <p>
     * @return A long representing the value in the editor.
     * @throws Number Format Exception for an invalid long or empty field.
     ***************************************************************************************************/
    public long getLongValue() throws UIException {
        try {
            String text = getText();
            if (StringUtility.isNullOrEmpty(text)) {
                return 0;
            }
            return LocaleManager.getIntegerFormatter().parse(text).longValue();
        } catch (ParseException exception) {
            throw getInvalidNumberException();
        }
    }
}
