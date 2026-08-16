package oracle.retail.sim.client.screen.reportformat;

import java.util.List;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;
import oracle.retail.sim.common.reportformat.ReportTypeId;
import oracle.retail.sim.common.reportformat.ReportTypeProperty;

/********************************************************************************************************
 * Reports Format Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ReportFormatWrapper {
    private ReportTypeFormat reportTypeFormat;
    private List<StorePrinter> printers;
    private boolean newRow;
    private boolean deletedRow;
    private boolean dirty;

    public ReportFormatWrapper(List<StorePrinter> printers) {
        this(BOFactory.createReportTypeFormat(), printers, true);
    }

    public ReportFormatWrapper(ReportTypeFormat reportTypeFormat, List<StorePrinter> printers, boolean newRow) {
        this.reportTypeFormat = reportTypeFormat;
        this.printers = printers;
        this.newRow = newRow;
    }

    public ReportTypeFormat getReportTypeFormat() {
        return reportTypeFormat;
    }

    public void setReportTypeFormat(ReportTypeFormat reportTypeFormat) {
        this.reportTypeFormat = reportTypeFormat;
        setDirty(true);
    }

    public String getReportType() {
        return reportTypeFormat.getReportType();
    }

    public void setReportType(String reportType) {
        reportTypeFormat.doSetReportType(reportType);
        setDirty(true);
    }

    public boolean isReportTypeManifest() {
        return ReportTypeId.MANIFEST.equalsIgnoreCase(getReportType());
    }

    public boolean isReportTypePreShipment() {
        return ReportTypeId.PRE_SHIPMENT.equalsIgnoreCase(getReportType());
    }

    public String getFormat() {
        return reportTypeFormat.getFormatName();
    }

    public void setFormat(String format) throws BusinessException {
        if (StringHelper.isNullOrEmpty(format)) {
            return;
        }
        if (format.equals(reportTypeFormat.getFormatName())) {
            return;
        }
        reportTypeFormat.setFormatName(format);
        setDirty(true);
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

    public StorePrinter getDefaultPrinter() {
        Long printerId = reportTypeFormat.getDefaultPrinterId();
        if (printerId == null) {
            return null;
        }
        for (StorePrinter printer : printers) {
            if (printerId.equals(printer.getId())) {
                return printer;
            }
        }
        return null;
    }

    public void setDefaultPrinter(StorePrinter printer) {
        reportTypeFormat.setDefaultPrinterId(printer != null ? printer.getId() : null);
        setDirty(true);
    }

    public String getTemplateURL() {
        return reportTypeFormat.getTemplateURL();
    }

    public void setTemplateURL(String templateURL) throws BusinessException {
        if (StringHelper.isNullOrEmpty(templateURL) || templateURL.equals(reportTypeFormat.getTemplateURL())) {
            return;
        }
        reportTypeFormat.setTemplateURL(templateURL);
        setDirty(true);
    }

    public boolean isDefaultFormat() {
        return reportTypeFormat.isDefault();
    }

    public void setDefaultFormat(boolean setAsDefault) {
        reportTypeFormat.setIsDefault(setAsDefault);
        setDirty(true);
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    // Determines if the given property is modifiable. If this returns false, then this property is not modifiable.
    public boolean isPropertyModifiable(String propertyName) {
        switch (propertyName) {
            case ReportTypeProperty.DEFAULT_PRINTER:
            case ReportTypeProperty.DEFAULT_FORMAT:
            case ReportTypeProperty.FORMAT:
            case ReportTypeProperty.TEMPLATE_URL:
                return true;
            default:
                return newRow;
        }
    }
}
