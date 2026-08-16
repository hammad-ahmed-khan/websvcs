package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.storesequence.StoreSequenceConstants;

/*******************************************************************************
 * Store Sequence Description Displayer
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 ******************************************************************************/

public class StoreSequenceAreaDescDisplayer extends AbstractDisplayer {

    private static final String DOTS = ":::";
    private static final String DASH = " - ";

    public String getDisplayText(Object value) {
        if (value == null) {
            return Translator.getText(StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION);
        }
        if (value instanceof String) {
            String valueText = (String) value;

            if (StringUtility.isNullOrEmpty(valueText)) {
                return Translator.getText(StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION);
            }
            if (StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION.equals(valueText)) {
                return Translator.getText(StoreSequenceConstants.NO_SEQUENCE_DESCRIPTION);
            }
            return StringUtility.replace(valueText, DOTS, DASH);
        }
        return StringConstants.EMPTY;
    }
}