package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.lineitem.StockLineItemWrapper;

/********************************************************************************************************
 * Displays a quantity in the format of the current locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class LineItemInventoryDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        return getDisplayText(value, null);
    }

    public String getDisplayText(Object value, Object model) {
        if (value instanceof Quantity) {
            Quantity quantity = (Quantity) value;
            if (model instanceof StockLineItemWrapper) {
                StockLineItemWrapper wrapper = (StockLineItemWrapper) model;
                if (wrapper.isEstimatedStockOnHandQuantities()) {
                    return StringConstants.TILDE_CHAR + LocaleManager.getNumberFormatter().format(quantity.getBigDecimal());
                }
            }
            return LocaleManager.getNumberFormatter().format(quantity.getBigDecimal());
        }
        return StringConstants.EMPTY;
    }
}
