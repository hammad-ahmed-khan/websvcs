package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.item.InventoryDisposition;

/********************************************************************************************************
 * Displayer for Inventory Disposition object
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class InventoryDispositionDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value instanceof InventoryDisposition) {
            return Translator.getText(((InventoryDisposition) value).toString());
        }
        return StringConstants.EMPTY;
    }
}
