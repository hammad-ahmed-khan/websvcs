package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.report.StorePrinter;

/********************************************************************************************************
 * Displays Printer types.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RetailStorePrinterTypeDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
    	if(null == value) {
    		return StringConstants.EMPTY;
    	}
    	Long type = (Long) value;
    	if(type.equals(StorePrinter.TYPE_POSTSCRIPT)) {
    		return Translator.getText("PostScript");
    	} else if(type.equals(StorePrinter.TYPE_TICKET)) {
    		return Translator.getText("Ticket");
    	}
    	return StringConstants.EMPTY;
    }
}
