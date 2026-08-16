package oracle.retail.sim.client.screen.theme;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

/********************************************************************************************************
 * Theme Key Displayer
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ThemeKeyDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object object) {
        if (object instanceof String) {
            String text = (String) object;
            text = StringUtility.replace(text.trim(), "Font", "");
            text = StringUtility.replace(text, "font", "");
            text = StringUtility.replace(text, "Color", "");
            text = StringUtility.replace(text, "color", "");
            text = StringUtility.replace(text, "Icon", "");
            text = StringUtility.replace(text, "icon", "");
            text = StringUtility.replace(text, ".", " ");
            int index = text.lastIndexOf(" ");
            StringBuilder displayText = new StringBuilder();
            if (index > -1) {
                displayText.append(text.substring(0, index));
                String remainingText = text.substring(index);
                if (remainingText.length() > 1) {
                    displayText.append(remainingText.substring(0, 2).toUpperCase());
                    displayText.append(remainingText.substring(2));
                }
            } else {
                displayText.append(text.substring(0, 1).toUpperCase());
                displayText.append(text.substring(1));
            }
            return displayText.toString();
        }
        return StringConstants.EMPTY;
    }
}
