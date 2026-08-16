package oracle.retail.sim.client.swing.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Displays a quantity in the format of the current locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class EstimatedQuantityDisplayer extends AbstractDisplayer {

    private boolean isEstimatedQuantity;

    public EstimatedQuantityDisplayer(boolean isEstimatedQuantity) {
        this.isEstimatedQuantity = isEstimatedQuantity;
    }
    
    public void setIsEstimatedQuantity(boolean isEstimatedQuantity) {
        this.isEstimatedQuantity = isEstimatedQuantity;
    }

    public String getDisplayText(Object object) {
        if (object instanceof Quantity) {
            Quantity quantity = (Quantity) object;
            if (isEstimatedQuantity) {
                return StringConstants.TILDE_CHAR + LocaleManager.getNumberFormatter().format(quantity.getBigDecimal());
            }
            return LocaleManager.getNumberFormatter().format(quantity.getBigDecimal());
        }
        return StringConstants.EMPTY;
    }
}
