package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentFromArea;
import oracle.retail.sim.common.shelfreplenishment.ShelfReplenishmentLineItem;

/********************************************************************************************************
 * Formats a string that represents a shelfReplenishment from option within a shelfReplenishment list. Basically, the translated shelfReplenishment
 * list code plus the shipment id.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ShelfReplenishmentPickFromDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value, Object model) {
        if (value instanceof ShelfReplenishmentFromArea) {
            ShelfReplenishmentFromArea location = (ShelfReplenishmentFromArea) value;
            String text = Translator.getText(location.toString());

            if (location == ShelfReplenishmentFromArea.DELIVERY_BAY) {
                if (model instanceof ShelfReplenishmentLineItem) {
                    String shipmentId = ((ShelfReplenishmentLineItem) model).getShipmentId();

                    if (!StringUtility.isNullOrEmpty(shipmentId)) {
                        text = text + " - " + shipmentId;
                    }
                }
            }
            return text;
        }
        return StringConstants.EMPTY;
    }

    public String getDisplayText(Object value) {
        return getDisplayText(value, null);
    }
}
