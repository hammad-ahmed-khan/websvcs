package oracle.retail.sim.client.swing.format;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.format.MaskAdaptor;

/******************************************************************************************
 * This class represents basic mask for a boolean value. It returns the language translated
 * true/false for a boolean value.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************************/

public class BooleanMask extends MaskAdaptor {

    private boolean isYesNo;

    /******************************************************************************************
     * Constructs a new BooleanMask().
     ******************************************************************************************/
    public BooleanMask() {
    }

    /******************************************************************************************
     * Constructs a new BooleanMask().
     ******************************************************************************************/
    public BooleanMask(boolean isYesNo) {
        this.isYesNo = isYesNo;
    }

    /******************************************************************************************
     * Formats data into a language senstive string. This returns "false" unless the data is
     * of type boolean and is true.
     ******************************************************************************************/
    public String formatData(Object data) {
        if (data == null) {
            return StringConstants.EMPTY;
        }
        if (isYesNo) {
            return formatYesNo(data);
        }
        if (data instanceof Boolean) {
            return Translator.getText(data.toString());
        }
        return Translator.getText(Boolean.FALSE.toString());
    }

    /******************************************************************************************
     * Formats data into yes/no.
     ******************************************************************************************/
    private String formatYesNo(Object data) {
        if (data instanceof Boolean) {
            if ((Boolean) data) {
                return Translator.getText("Yes");
            }
        }
        return Translator.getText("No");
    }
}
