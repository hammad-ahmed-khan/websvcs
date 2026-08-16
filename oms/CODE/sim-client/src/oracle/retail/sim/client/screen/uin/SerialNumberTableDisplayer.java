package oracle.retail.sim.client.screen.uin;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.screen.fulfillmentorderdelivery.FulfillmentOrderDeliveryLineItemWrapper;
import oracle.retail.sim.client.screen.stockcount.StockCountLineItemWrapper;
import oracle.retail.sim.client.screen.transfer.TransferLineItemWrapper;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.BasicDisplayer;
import oracle.retail.sim.common.lineitem.SerialNumberLineItemWrapper;
import oracle.retail.sim.common.stockcount.StockCountRejectedLineItem;

/********************************************************************************************************
 * Displays the number of UINs found on its model. The model instanceof if logic should be in the order
 * of the most frequently encountered.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SerialNumberTableDisplayer implements BasicDisplayer {

    public String getDisplayText(Object value, Object model) {
        if (model instanceof SerialNumberLineItemWrapper) {
            SerialNumberLineItemWrapper lineItem = (SerialNumberLineItemWrapper) model;
            if (lineItem.isSerialNumberRequired()) {
                return formatValue(value);
            }
        } else if (model instanceof TransferLineItemWrapper) {
            TransferLineItemWrapper lineItem = (TransferLineItemWrapper) model;
            if (lineItem.isSerialNumberRequired()) {
                return formatValue(value);
            }
        } else if (model instanceof StockCountLineItemWrapper) {
            StockCountLineItemWrapper lineItem = (StockCountLineItemWrapper) model;
            if (lineItem.isSerialNumberRequired()) {
                return formatValue(value);
            }
        } else if (model instanceof StockCountRejectedLineItem) {
            StockCountRejectedLineItem lineItem = (StockCountRejectedLineItem) model;
            if (lineItem.isSerialNumberRequired()) {
                return formatValue(value);
            }
        } else if (model instanceof FulfillmentOrderDeliveryLineItemWrapper) {
            FulfillmentOrderDeliveryLineItemWrapper lineItem = (FulfillmentOrderDeliveryLineItemWrapper) model;
            if (lineItem.isSerialNumberRequired()) {
                return formatValue(value);
            }
        }
        return StringConstants.EMPTY;
    }

    private String formatValue(Object value) {
        if (value instanceof Number) {
            return LocaleManager.getIntegerFormatter(false).format(value);
        }
        return StringConstants.EMPTY;
    }

    public String getDisplayText(Object value) {
        return getDisplayText(value, null);
    }
}
