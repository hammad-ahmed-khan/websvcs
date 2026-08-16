package oracle.retail.sim.client.uom;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.screen.fulfillmentorderpick.ItemSubstitutionWrapper;
import oracle.retail.sim.client.screen.item.RelatedItemWrapper;
import oracle.retail.sim.common.business.Quantity;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.item.OrderItem;
import oracle.retail.sim.common.item.RelatedItem;
import oracle.retail.sim.common.lineitem.OrderLineItemWrapper;

/********************************************************************************************************
 * Formats a string that represents a pick from option within a pick list.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class OrderItemEstimatedQuantityDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value, Object model) {
        if (value instanceof Quantity) {
            Quantity quantity = (Quantity) value;
            if (model instanceof OrderLineItemWrapper) {
                OrderItem orderItem = ((OrderLineItemWrapper) model).getOrderItem();
                if (orderItem.isInventoryAtComponentLevel()) {
                    return StringConstants.TILDE_CHAR + LocaleManager.getNumberFormatter().format(quantity.getBigDecimal());
                }
            } else if (model instanceof ItemSubstitutionWrapper) {
                RelatedItem relatedItem = ((ItemSubstitutionWrapper) model).getRelatedItem();
                if (relatedItem.isInventoryEstimated()) {
                    return StringConstants.TILDE_CHAR + LocaleManager.getNumberFormatter().format(quantity.getBigDecimal());
                }
            } else if (model instanceof RelatedItemWrapper) {
                RelatedItem relatedItem = ((RelatedItemWrapper) model).getRelatedItem();
                if (relatedItem.isInventoryEstimated()) {
                    return StringConstants.TILDE_CHAR + LocaleManager.getNumberFormatter().format(quantity.getBigDecimal());
                }
            }
            return LocaleManager.getNumberFormatter().format(quantity.getBigDecimal());
        }
        return null;
    }

    public String getDisplayText(Object value) {
        return getDisplayText(value, null);
    }
}
