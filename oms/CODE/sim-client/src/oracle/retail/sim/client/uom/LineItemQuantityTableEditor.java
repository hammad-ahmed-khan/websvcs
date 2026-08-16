package oracle.retail.sim.client.uom;

import oracle.retail.sim.client.swing.tableeditor.QuantityTableEditor;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.lineitem.OrderLineItemWrapper;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;
import oracle.retail.sim.common.lineitem.UOMConstants;
import oracle.retail.sim.common.lineitem.UnitOfMeasureWrapper;

/********************************************************************************************************
 * A table editor that edits line item quantities. It checks the standard uom of the item to determine
 * what characters are allowed into the editor. It inherets from the NumberTableEditor and thus contains
 * all the same features.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class LineItemQuantityTableEditor extends QuantityTableEditor {
    private static final long serialVersionUID = 3244280177383309238L;

    /****************************************************************************************************
     * Constructors
     ***************************************************************************************************/

    public LineItemQuantityTableEditor() {
    }

    public LineItemQuantityTableEditor(boolean isNullAllowed) {
        super(isNullAllowed);
    }

    /****************************************************************************************************
     * Returns the full quantity entered. This is abstracted as a method to allow for subclasses to
     * alter logic.
     ***************************************************************************************************/
    protected Number getFullQuantity(Number enteredQty) {
        if (enteredQty != null) {
            Object dataModel = getModel();
            if (dataModel instanceof StockLineItemWrapper) {
                StockLineItemWrapper lineItem = (StockLineItemWrapper) dataModel;
                if (lineItem.isCasesMode()) {
                    Quantity value = new Quantity(enteredQty.doubleValue());
                    Quantity total = value.multiply(lineItem.getCaseSize());
                    return total.doubleValue();
                }
            } else if (dataModel instanceof OrderLineItemWrapper) {
                OrderLineItemWrapper lineItem = (OrderLineItemWrapper) dataModel;
                if (lineItem.isCasesMode()) {
                    Quantity value = new Quantity(enteredQty.doubleValue());
                    Quantity total = value.multiply(lineItem.getCaseSize());
                    return total.doubleValue();
                }
            }
        }
        return enteredQty;
    }

    /****************************************************************************************************
     * Method that returns whether or not a decimal is allowed.
     ***************************************************************************************************/
    protected boolean isDecimalAllowed() {
        Object dataModel = getModel();
        if (dataModel instanceof UnitOfMeasureWrapper) {
            UnitOfMeasureWrapper wrapper = (UnitOfMeasureWrapper) dataModel;
            if (wrapper.isStandardMode()) {
            	return !UOMConstants.getUnitUOMs().contains(wrapper.getStandardUnitOfMeasure());
            }
            if (wrapper.isPreferredMode()) {
                return !UOMConstants.getUnitUOMs().contains(wrapper.getPreferredUnitOfMeasure());
            }
            return true;
        }
        return false;
    }
}
