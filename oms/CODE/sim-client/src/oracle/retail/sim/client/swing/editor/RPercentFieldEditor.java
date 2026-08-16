package oracle.retail.sim.client.swing.editor;

import java.math.BigDecimal;
import java.text.ParseException;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.format.PercentMask;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.common.format.Mask;

/********************************************************************************************************
 * This is a subclass of RTextFieldEditor that allows only percent numbers.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RPercentFieldEditor extends RNumberFieldEditor {
    private static final long serialVersionUID = -6521752329412370530L;

    /****************************************************************************************************
     * Constructor creates new percent field editor.
     ***************************************************************************************************/
    public RPercentFieldEditor() {
        initEditor();
    }

    /****************************************************************************************************
     * Constructor creates new percent field editor with a title.
     * <p>
     * @param title The title to assign to the editor.
     ***************************************************************************************************/
    public RPercentFieldEditor(String title) {
        super(title);
        initEditor();
    }

    /****************************************************************************************************
     * Creates a new RPercentFieldEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param required True if the field should be displayed as required, false otherwise.
     ***************************************************************************************************/
    public RPercentFieldEditor(String title, boolean required) {
        super(title, required);
        initEditor();
    }

    /****************************************************************************************************
     * Initializes the editor.
     ***************************************************************************************************/
    private void initEditor() {
        super.setMask(new PercentMask());
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
     * Assigns a minimum and preferred size to the editor. Values sizes include EditorConstants.SMALL,
     * EditorConstants.MEDIUM and EditorConstants.LARGE.
     * <p>
     * @param sizeType The size type (SMALL, MEDIUM, or LARGE).
     ***************************************************************************************************/
    public void setSizeType(int sizeType) {
        switch (sizeType) {
            case EditorConstants.TINY:
                setMinimumWidth(UIManager.getInt(UIThemeName.CURRENCYFIELD_TINY));
                break;
            case EditorConstants.SMALL:
                setMinimumWidth(UIManager.getInt(UIThemeName.CURRENCYFIELD_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setMinimumWidth(UIManager.getInt(UIThemeName.CURRENCYFIELD_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setMinimumWidth(UIManager.getInt(UIThemeName.CURRENCYFIELD_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Sets the value of the field from a big decimal.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setPercent(BigDecimal value) {
        if (value == null) {
            clear();
            return;
        }
        setPercent(value.doubleValue());
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as a big decimal.
     * <p>
     * @return A BigDecimal representing the value in the editor.
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
    public void setPercent(Double value) {
        if (value == null) {
            clear();
            return;
        }
        setPercent(value.doubleValue());
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
     * Sets the value of the field from an Integer object.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setPercent(Integer value) {
        if (value == null) {
            clear();
            return;
        }
        setPercent(value.doubleValue());
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as an Integer object.
     * <p>
     * @return An Integer representing the value in the editor.
     ***************************************************************************************************/
    public Integer getInteger() throws UIException {
        return getBigDecimal().intValue();
    }

    /****************************************************************************************************
     * Sets the value of the field from a double.
     * <p>
     * @param value The value to assign to the editor.
     ***************************************************************************************************/
    public void setPercent(double value) {
        setText(String.valueOf(value));
    }

    /****************************************************************************************************
     * Retrieves the value of the editor as a double.
     * <p>
     * @return A double representing the value in the editor.
     ***************************************************************************************************/
    public double getPercentValue() throws UIException {
        return getDouble();
    }
}
