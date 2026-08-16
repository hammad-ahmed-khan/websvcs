package oracle.retail.sim.client.swing.format;

import java.util.Date;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.client.swing.util.DataTypeConstants;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.MaskAdaptor;

/******************************************************************************************
 * This mask formats a date.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class DateMask extends MaskAdaptor {

    private int dataType = DataTypeConstants.DATE_SHORT;

    /******************************************************************************************
     * Constructs a new DateMask.
     ******************************************************************************************/
    public DateMask() {
    }

    /******************************************************************************************
     * Constructs a new DateMask.
     ******************************************************************************************/
    public DateMask(int dataType) {
        this.dataType = dataType;
    }

    /******************************************************************************************
     * If a Date object is passed in, it will format to whatever data type was set in the
     * constuctor.
     ******************************************************************************************/
    public String formatData(Object object) {
        if (object != null) {
            if (object instanceof Date) {
                switch (dataType) {
                    case DataTypeConstants.DATE_MEDIUM:
                        return LocaleManager.getMediumDateFormatter().format((Date) object);
                    case DataTypeConstants.DATE_LONG:
                        return LocaleManager.getLongDateFormatter().format((Date) object);
                    case DataTypeConstants.DATE_FULL:
                        return LocaleManager.getFullDateFormatter().format((Date) object);
                    default:
                        return LocaleManager.getShortDateFormatter().format((Date) object);
                }
            }
        }
        return StringConstants.EMPTY;
    }
}
