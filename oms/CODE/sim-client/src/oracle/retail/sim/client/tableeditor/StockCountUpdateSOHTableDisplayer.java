package oracle.retail.sim.client.tableeditor;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * A table displays that displays alternate strings for a boolean value (specifically for the system
 * admin option of update all SOH.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StockCountUpdateSOHTableDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object instanceof Boolean) {
            if ((Boolean) object) {
                return Translator.getText("All Items");
            }
        }
        return Translator.getText("Discrepant Items only");
    }
}