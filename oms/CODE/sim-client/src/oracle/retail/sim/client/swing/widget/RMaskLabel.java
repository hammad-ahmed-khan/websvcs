package oracle.retail.sim.client.swing.widget;

import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.Displayable;
import oracle.retail.sim.common.format.Mask;

/******************************************************************************************
 * Label that allows a mask that formats the text. This type of label will not automatically
 * translate the data.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *****************************************************************************************/

public class RMaskLabel extends RLabel {
    private static final long serialVersionUID = 6583074059700330961L;

    private static final String UID = "MaskLabelUI";

    protected Mask currentMask;

    /******************************************************************************************
     * Returns new RMaskLabel object.
     * <p>
     * @param mask The mask the label will use to format the text.
     *****************************************************************************************/
    public RMaskLabel(Mask mask) {
        setMask(mask);
    }

    /******************************************************************************************
     * Returns new RMaskLabel object with text.
     * <p>
     * @param mask The mask the label will use to format the text.
     * @param text The display text to assign to the label.
     ******************************************************************************************/
    public RMaskLabel(Mask mask, String text) {
        super(text);
        setMask(mask);
    }

    /******************************************************************************************
     * Returns a string that specifies the name of the L&F class that renders this component.
     * <p>
     * @return The string "MaskLabelUI"
    /******************************************************************************************/
    public String getUIClassID() {
        return UID;
    }

    /******************************************************************************************
     * Assigns the mask to this mask label.
     * <p>
     * @param mask The mask to assign.
     ******************************************************************************************/
    public void setMask(Mask mask) {
        currentMask = mask;
        refresh();
    }

    /******************************************************************************************
     * Retrieves the mask assigned tot his mask label.
     * <p>
     * @return The mask.
     ******************************************************************************************/
    public Mask getMask() {
        return currentMask;
    }

    /******************************************************************************************
     * Sets the data to display within the label.
     * <p>
     *@param data The data to display within the label.
     ******************************************************************************************/
    public void setData(Object data) {
        if (currentMask != null) {
            super.setText(currentMask.formatData(data), false);
        } else if (data instanceof Displayable) {
            super.setText(((Displayable) data).toDisplayString(), false);
        } else if (data != null) {
            super.setText(data.toString(), false);
        } else {
            super.setText(StringConstants.EMPTY);
        }
    }

    /******************************************************************************************
     * Sets the text to display within the label. Overrides the superclass method of RLabel to
     * not translate the text. This label is meant for displaying data only.
     * <p>
     *@param text The text to display within the label.
     ******************************************************************************************/
    public void setText(String text) {
        if (currentMask != null) {
            super.setText(currentMask.format(text), false);
        } else {
            super.setText(text, false);
        }
    }

    /******************************************************************************************
     * Sets the text to display. This method ignores the translate parameter!
     * <p>
     * @param text The text to display with the label.
     * @param translate True if the text should be translated, false if not.
    /******************************************************************************************/
    public void setText(String text, boolean translate) {
        if (currentMask != null) {
            super.setText(currentMask.format(text), false);
        } else {
            super.setText(text, false);
        }
    }

    /******************************************************************************************
     * Refreshes the text in this lael.
     ******************************************************************************************/
    private void refresh() {
        setText(getText());
    }
}
