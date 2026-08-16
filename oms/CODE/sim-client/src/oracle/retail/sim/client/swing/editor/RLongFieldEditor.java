package oracle.retail.sim.client.swing.editor;

import java.text.ParseException;
import javax.swing.SwingConstants;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.format.IntegerMask;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.format.Mask;

/********************************************************************************************************
 * This is a subclass of RTextFieldEditor that helps with long numbers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RLongFieldEditor extends RNumberFieldEditor {
    private static final long serialVersionUID = -5361910829705947680L;

    private IntegerMask mask = new IntegerMask();

    /****************************************************************************************************
     * Constructor creates new long field editor.
     ***************************************************************************************************/
    public RLongFieldEditor() {
        initEditor();
    }

    /****************************************************************************************************
     * Constructor creates new long field editor with a title.
     * <p>
     * @param title The title to assign to the editor.
     ***************************************************************************************************/
    public RLongFieldEditor(String title) {
        super(title);
        initEditor();
    }

    /****************************************************************************************************
     * Creates a new RLongFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RLongFieldEditor(String title, boolean required) {
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
