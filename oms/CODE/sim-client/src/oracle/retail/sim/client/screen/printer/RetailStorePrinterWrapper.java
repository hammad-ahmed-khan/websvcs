package oracle.retail.sim.client.screen.printer;

import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.report.StorePrinter;

/********************************************************************************************************
 * Retail Store Printer Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class RetailStorePrinterWrapper {
    private StorePrinter printer;
    private boolean newRow;
    private boolean deletedRow;
    private boolean dirty;

    public RetailStorePrinterWrapper(StorePrinter printer) {
        this(printer, true);
    }

    public RetailStorePrinterWrapper(StorePrinter printer, boolean newRow) {
        this.printer = printer;
        this.newRow = newRow;
    }

    public String getDescription() {
        return printer.getDescription();
    }

    public void setDescription(String desc) {
        if (StringHelper.isNullOrEmpty(desc)) {
            return;
        }
        if (printer.getDescription() != null && desc.equals(printer.getDescription())) {
            return;
        }
        printer.setDescription(desc);
        setDirty(true);
    }

    public long getType() {
        return printer.getType();
    }

    public void setType(long type) {
        printer.setType(type);
        setDirty(true);
    }

    public String getUri() {
        return printer.getUri();
    }

    public void setUri(String uri) {
        if (StringHelper.isNullOrEmpty(uri)) {
            return;
        }
        if (printer.getUri() != null && uri.equals(printer.getUri())) {
            return;
        }
        printer.setUri(uri);
        setDirty(true);
    }

    public StorePrinter getRetailStorePrinter() {
        return printer;
    }

    public boolean isNew() {
        return newRow;
    }

    public void setNew(boolean newRow) {
        this.newRow = newRow;
        setDirty(true);
    }

    public boolean isDeleted() {
        return deletedRow;
    }

    public void setDeleted(boolean deletedRow) {
        this.deletedRow = deletedRow;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
