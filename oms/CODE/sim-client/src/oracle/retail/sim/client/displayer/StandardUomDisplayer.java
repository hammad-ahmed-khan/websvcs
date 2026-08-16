package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.lineitem.UOMConstants;
import oracle.retail.sim.common.lineitem.UOMMode;

/********************************************************************************************************
 * Displays "Standard UOM" instead of Units and is NOT concerned with Item standard UOM.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class StandardUomDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value instanceof Integer) {
            if (UOMMode.STANDARD.getCode().equals(value)) {
                return Translator.getText(UOMConstants.STANDARD_UOM);
            }
        }
        if (UOMMode.STANDARD.equals(value)) {
            return Translator.getText(UOMConstants.STANDARD_UOM);
        }
        return Translator.getText(UOMConstants.CASES);
    }
}
