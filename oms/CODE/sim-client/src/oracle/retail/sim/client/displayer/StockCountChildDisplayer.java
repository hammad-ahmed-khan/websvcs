package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.StringUtility;
import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.stockcount.StockCountChild;

/********************************************************************************************************
 * Stock Count Child Displayer
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class StockCountChildDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value instanceof StockCountChild) {
            StockCountChild location = (StockCountChild) value;
            String part1 = location.getBreakdownDescription();
            String part2 = location.getDescription();
            String part3 = location.getArea().toString();
            StringBuilder buffer = new StringBuilder();
            if (!StringUtility.isNullOrEmpty(part1)) {
                buffer.append(part1);
                buffer.append(" - ");
            }
            if (!StringUtility.isNullOrEmpty(part2)) {
                buffer.append(part2);
                buffer.append(" - ");
            }
            buffer.append(part3);
            return buffer.toString();
        }
        return Translator.getText("No Location");
    }
}
