package oracle.retail.sim.client.screen.printer;

import oracle.retail.sim.common.report.SessionPrinter;
import oracle.retail.sim.common.report.StorePrinter;

/********************************************************************************************************
 * Session Printer Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SessionPrinterWrapper {

    private SessionPrinter sessionPrinter;
    private StorePrinter storePrinter;

    public SessionPrinterWrapper(SessionPrinter sessionPrinter, StorePrinter storePrinter) {
        this.sessionPrinter = sessionPrinter;
        this.storePrinter = storePrinter;
    }

    public String getFormatName() {
        return sessionPrinter.getFormatName();
    }

    public void setPrinter(StorePrinter storePrinter) {
        this.storePrinter = storePrinter;
        if (storePrinter != null) {
            sessionPrinter.setPrinterId(storePrinter.getId());
            sessionPrinter.setDescription(storePrinter.getDescription());
            sessionPrinter.setPrinterUri(storePrinter.getUri());
        }
    }

    public StorePrinter getPrinter() {
        return storePrinter;
    }

    public boolean isPropertyModifiable(String property) {
        return true;
    }
}