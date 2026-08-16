package oracle.retail.sim.client.swing.editor;

import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.border.Border;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.swing.event.REventListener;
import oracle.retail.sim.client.swing.format.BasicDisplayerMask;
import oracle.retail.sim.client.swing.format.BooleanMask;
import oracle.retail.sim.client.swing.format.DateMask;
import oracle.retail.sim.client.swing.format.DecimalMask;
import oracle.retail.sim.client.swing.format.IntegerMask;
import oracle.retail.sim.common.format.MoneyMaskFactory;
import oracle.retail.sim.client.swing.format.PercentMask;
import oracle.retail.sim.client.swing.format.QuantityMask;
import oracle.retail.sim.client.swing.plaf.custom.UIThemeName;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.client.swing.util.GridTool;
import oracle.retail.sim.client.swing.util.UIException;
import oracle.retail.sim.client.swing.util.UIPropertyName;
import oracle.retail.sim.client.swing.widget.RMaskLabel;
import oracle.retail.sim.common.core.type.BasicDisplayer;

/********************************************************************************************************
 * This class represents a label/label editor where the left hand is the label of the field and the right
 * hand side display some type of data.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RDisplayLabelEditor extends AbstractEditor {
    private static final long serialVersionUID = 7687771981844524809L;

    private RPlainEditorLabel titleLabel = new RPlainEditorLabel();
    private RMaskLabel valueLabel = new RMaskLabel(null);
    private int dataType = -1;

    /****************************************************************************************************
     * Creates a new RDisplayLabelEditor with no title.
     ***************************************************************************************************/
    public RDisplayLabelEditor() {
        initialize(false);
    }

    /****************************************************************************************************
     * Creates a new RDisplayLabelEditor with a title. Defaults the data type to text.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public RDisplayLabelEditor(String title) {
        this(title, DataTypeConstants.TEXT);
    }

    /****************************************************************************************************
     * Creates a new RDisplayLabelEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param dataType The type of data represented by the editor
     ***************************************************************************************************/
    public RDisplayLabelEditor(String title, int dataType) {
        titleLabel.setText(title);
        setDataType(dataType);
        initialize(false);
    }

    /****************************************************************************************************
     * Creates a new RDisplayLabelEditor with a title.
     * <p>
     * @param title The title to assign.
     * @param boxOutline True if the field section should contain a box outline, false otherwise
     ***************************************************************************************************/
    public RDisplayLabelEditor(String title, boolean boxOutline) {
        titleLabel.setText(title);
        initialize(boxOutline);
    }

    /****************************************************************************************************
     * Initializes the editor.
     ***************************************************************************************************/
    private void initialize(boolean boxOutline) {
        spaceLabel.setOpaque(false);
        titleLabel.setEnabled(true);
        valueLabel.setEnabled(false);
        valueLabel.setHorizontalAlignment(EditorConstants.LEFT);

        if (boxOutline) {
            Color color = UIManager.getColor(UIThemeName.RDISPLAYLABELEDITOR_BORDER_COLOR);
            Insets insets = UIManager.getInsets(UIThemeName.RDISPLAYLABELEDITOR_MARGIN);
            Border border1 = BorderFactory.createLineBorder(color, 1);
            Border border2 = BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
            valueLabel.setBorder(BorderFactory.createCompoundBorder(border1, border2));
        }

        validateTitle();
        setOpaque(false);
        setLayout(new GridBagLayout());
        setTitleAlignment(EditorConstants.LEFT);
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Retrieves the label widget associated with this editor.
     * <p>
     * @param The label widget.
     ***************************************************************************************************/
    public REditorLabel getLabel() {
        return titleLabel;
    }

    /****************************************************************************************************
     * Retrieves the display label (the label that displays the value) associated with this editor.
     * <p>
     * @param The value label widget.
     ***************************************************************************************************/
    public RMaskLabel getDisplayLabel() {
        return valueLabel;
    }

    /****************************************************************************************************
     * Assigns the data type. This will automatically select a mask for the value label.
     * <p>
     * @param type A DataTypeConstant value.
     ***************************************************************************************************/
    public void setDataType(int type) {
        if (type < DataTypeConstants.TEXT || type > DataTypeConstants.ICON) {
            throw new IllegalArgumentException("Data type must be a valid DateTypeConstant.");
        }

        dataType = type;

        switch (dataType) {
            case DataTypeConstants.BOOLEAN:
                valueLabel.setMask(new BooleanMask());
            case DataTypeConstants.INTEGER:
            case DataTypeConstants.INTEGER_LEFT:
            case DataTypeConstants.INTEGER_RIGHT:
                valueLabel.setMask(new IntegerMask());
                break;
            case DataTypeConstants.DECIMAL:
            case DataTypeConstants.DECIMAL_LEFT:
            case DataTypeConstants.DECIMAL_RIGHT:
                valueLabel.setMask(new DecimalMask());
                break;
            case DataTypeConstants.QUANTITY:
            case DataTypeConstants.QUANTITY_LEFT:
            case DataTypeConstants.QUANTITY_RIGHT:
                valueLabel.setMask(new QuantityMask());
                break;
            case DataTypeConstants.CURRENCY:
            case DataTypeConstants.CURRENCY_LEFT:
            case DataTypeConstants.CURRENCY_RIGHT:
                valueLabel.setMask(MoneyMaskFactory.createMoneyMask(LocaleManager.getNumericLocale()));
                break;
            case DataTypeConstants.PERCENT:
                valueLabel.setMask(new PercentMask());
                break;
            case DataTypeConstants.DATE:
            case DataTypeConstants.DATE_SHORT:
                valueLabel.setMask(new DateMask(DataTypeConstants.DATE_SHORT));
                break;
            case DataTypeConstants.DATE_MEDIUM:
                valueLabel.setMask(new DateMask(DataTypeConstants.DATE_MEDIUM));
                break;
            case DataTypeConstants.DATE_LONG:
                valueLabel.setMask(new DateMask(DataTypeConstants.DATE_LONG));
                break;
            case DataTypeConstants.DATE_FULL:
                valueLabel.setMask(new DateMask(DataTypeConstants.DATE_FULL));
                break;
            default:
                valueLabel.setMask(null);
        }
    }

    /****************************************************************************************************
     * Retrieves the data type assigned to this label editor.
     * <p>
     * @return The DataTypeConstant value assign to this editor.
     ***************************************************************************************************/
    public int getDataType() {
        return dataType;
    }

    /****************************************************************************************************
     * Assigns a mask to the editor based on a basic displayer.
     * <p>
     * param The displayer to use as a mask.
     ***************************************************************************************************/
    public void setDisplayer(BasicDisplayer displayer) {
        valueLabel.setMask(new BasicDisplayerMask(displayer));
        dataType = -1;
    }

    /****************************************************************************************************
     * This method is called when the identifer is altered in an editor. In the case of a display label
     * editor, this method performs no function.
     ***************************************************************************************************/
    protected void doIdentifierAltered(String identifier) {
    }

    /****************************************************************************************************
     * Retrieves the title of the editor.
     * <p>
     * @return The title.
     ***************************************************************************************************/
    public String getTitle() {
        return titleLabel.getOriginalText();
    }

    /****************************************************************************************************
     * Assigns the title to the editor.
     * <p>
     * @param title The title to assign.
     ***************************************************************************************************/
    public void setTitle(String title) {
        if (title == null) {
            titleLabel.clear();
        } else {
            titleLabel.setText(title);
        }
        validateTitle();
    }

    /****************************************************************************************************
     * Assigns the alignment of the title to the remainder of the editor. This is overridden with an
     * empty implementation. RDisplayLabelEditor ONLY supports a LEFT title alignment.
     ***************************************************************************************************/
    public void setTitleAlignment(int alignment) {
    }

    /****************************************************************************************************
     * Returns LEFT title alignment.
     ***************************************************************************************************/
    public int getTitleAlignment() {
        return EditorConstants.LEFT;
    }

    /****************************************************************************************************
     * Set required is overridden to always assign false to the editor. RDisplayLabelEditors can not be
     * required.
     ***************************************************************************************************/
    public void setRequired(boolean required) {
        titleLabel.setRequired(false);
        markRequiredAssigned();
    }

    /****************************************************************************************************
     * Returns false
     ***************************************************************************************************/
    public boolean isRequired() {
        return false;
    }

    protected void validateInnerLayout() {
        removeAll();
        add(titleLabel, GridTool.constraints(0, 0, 1, 1, 0, 0, 5, 3, 3, 0, 0, 5));
        add(valueLabel, GridTool.constraints(1, 0, 1, 1, 1, 0, 5, 3, 3, 0, 0, 0));
        add(spaceLabel, GridTool.constraints(0, 1, 2, 1, 0, 1, 5, 3, 0, 0, 0, 0));
        firePropertyChange(UIPropertyName.EDITOR_REALIGNMENT, false, true);
    }

    /****************************************************************************************************
     * Validates the visibiltiy of the title.
     ***************************************************************************************************/
    private void validateTitle() {
        if (isVisibleDenied()) {
            return;
        }
        titleLabel.setVisible(!StringUtility.isNullOrEmpty(titleLabel.getText()));
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
                setMinimumWidth(UIManager.getInt(UIThemeName.TEXTFIELD_TINY));
                break;
            case EditorConstants.SMALL:
                setMinimumWidth(UIManager.getInt(UIThemeName.TEXTFIELD_SMALL));
                break;
            case EditorConstants.MEDIUM:
                setMinimumWidth(UIManager.getInt(UIThemeName.TEXTFIELD_MEDIUM));
                break;
            case EditorConstants.LARGE:
                setMinimumWidth(UIManager.getInt(UIThemeName.TEXTFIELD_LARGE));
                break;
            default:
                throw new IllegalArgumentException("Invalid sizeType argument: " + sizeType);
        }
        this.sizeType = sizeType;
        validateInnerLayout();
    }

    /****************************************************************************************************
     * Sets the minimum and preferred width of the display label.
     * <p>
     * @param width The width in pixels.
     ***************************************************************************************************/
    public void setMinimumWidth(int width) {
        valueLabel.setMinimumWidth(width);
    }

    /****************************************************************************************************
     * Retrieves the text string from the value label.
     * <p>
     * @return The unformatted and trimmed text of the value label.
     ***************************************************************************************************/
    public String getText() {
        return valueLabel.getText();
    }

    /****************************************************************************************************
     * Assigns the data to be displayed within the display label editor.
     * <p>
     * param data The data object to display. This will formatted for the appropriate date type.
     ***************************************************************************************************/
    public void setData(Object data) {
        valueLabel.clear();
        if (data != null) {
            valueLabel.setData(data);
        }
    }

    /****************************************************************************************************
     * Clears the value/data of the editor.
     ***************************************************************************************************/
    public void clear() {
        valueLabel.clear();
    }

    /****************************************************************************************************
     * Retrieves whether or not the value/data is empty
     * <p>
     * @return True if the text field is empty, false if not.
     ***************************************************************************************************/
    public boolean isEmpty() {
        return StringUtility.isNullOrEmpty(getText());
    }

    /****************************************************************************************************
     * Override the setVisible() to validate against permissions first.
     ***************************************************************************************************/
    public void setVisible(boolean visible) {
        if (isVisibleDenied()) {
            visible = false;
        }
        titleLabel.setVisible(visible);
        valueLabel.setVisible(visible);
    }

    /****************************************************************************************************
     * Override the setEnabled() of JPanel to always assign false. These editors cannot be enabled.
     ***************************************************************************************************/
    public void setEnabled(boolean enabled) {
        super.setEnabled(false);
    }

    /****************************************************************************************************
     * Returns false. These editors cannot be enabled.
     ***************************************************************************************************/
    public boolean isEnabled() {
        return true;
    }

    /****************************************************************************************************
     * Empty implementation. No actions can be registered on these editors.
     ***************************************************************************************************/
    public void registerAction(REventListener listener, String command) {
    }

    /****************************************************************************************************
     * Returns false. This editor can never be the focus owner.
     ***************************************************************************************************/
    public boolean isFocusOwner() {
        return false;
    }

    /****************************************************************************************************
     * Overrides the superclass method to not allow this editor to receive focus on this call.
     ***************************************************************************************************/
    public boolean requestFocusInWindow() {
        return false;
    }

    /****************************************************************************************************
     * Empty implementation. This editor can not be in an error state.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState) {
    }

    /****************************************************************************************************
     * Empty implementation. This editor can not be in an error state.
     ***************************************************************************************************/
    public void setErrorState(boolean errorState, String errorText) {
    }

    /****************************************************************************************************
     * Always returns false. This editor can not be in an error state.
     ***************************************************************************************************/
    public boolean isErrorState() {
        return false;
    }

    /****************************************************************************************************
     * Empty implementation. Does not need to display an exception.
     ***************************************************************************************************/
    protected void displayException(UIException exception) {
    }
}
