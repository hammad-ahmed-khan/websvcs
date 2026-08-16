package oracle.retail.sim.client.uom;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.lineitem.UOMConstants;

/********************************************************************************************************
 * Displays the correct UOM of the object input.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class UomDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value, Object model) {
        return getDisplayText(value);
    }

    public String getDisplayText(Object value) {
        if (value instanceof String) {
            String unitOfMeasure = StringUtility.trimToNull(value.toString());
            if (UOMConstants.EACHES.equals(unitOfMeasure)) {
                return Translator.getText("Units");
            }
            return Translator.getText(unitOfMeasure);
        }
        return StringConstants.EMPTY;
    }
}
