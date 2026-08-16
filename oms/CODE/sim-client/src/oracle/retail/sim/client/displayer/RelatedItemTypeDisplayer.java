package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.item.RelatedItemType;

/********************************************************************************************************
 * Related Item Type Displayer
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RelatedItemTypeDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value instanceof String) {
            String type = (String) value;
            if (type.equals(RelatedItemType.RELATED)) {
                return Translator.getText("Related");
            } else if (type.equals(RelatedItemType.SUBSTITUTE)) {
                return Translator.getText("Substitute");
            } else if (type.equals(RelatedItemType.UPSELL)) {
                return Translator.getText("Upsell");
            } else if (type.equals(RelatedItemType.CROSSSELL)) {
                return Translator.getText("Crosssell");
            }
            return Translator.getText(type);
        }
        return StringConstants.EMPTY;
    }
}
