package oracle.retail.sim.client.screen.reportformat;

import java.util.List;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.business.BOFactory;
import oracle.retail.sim.common.business.BusinessException;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.report.StorePrinter;
import oracle.retail.sim.common.reportformat.ReportTypeFormat;

/********************************************************************************************************
 * Reports Format Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/
public class RPrinterDialogWrapper {

    private ReportTypeFormat reportTypeFormat = BOFactory.createReportTypeFormat();
    private List<StorePrinter> printers;

    public RPrinterDialogWrapper(ReportTypeFormat reportTypeFormat, List<StorePrinter> printers) {
        this.reportTypeFormat = reportTypeFormat;
        this.printers = printers;
    }

    public String getFormat() {
        return Translator.getText(reportTypeFormat.getFormatName());
    }

    public String getReportType() {
        return reportTypeFormat.getReportType();
    }

    public void setFormat(String format) throws BusinessException {
        if (StringHelper.isNullOrEmpty(format)) {
            return;
        }
        if (reportTypeFormat != null && format.equals(reportTypeFormat.getFormatName())) {
            return;
        }
        reportTypeFormat.setFormatName(format);
    }

    public StorePrinter getDefaultPrinter() {
        Long printerId = reportTypeFormat.getDefaultPrinterId();
        for (StorePrinter printer : printers) {
            if (printerId != null && printer.getId() == printerId.longValue()) {
                return printer;
            }
        }
        return null;
    }

    public void setDefaultPrinter(StorePrinter printer) {
        if (printer == null) {
            reportTypeFormat.setDefaultPrinterId(null);
        } else {
            reportTypeFormat.setDefaultPrinterId(printer.getId());
        }
    }

    public String getPrintURL() {
        return reportTypeFormat.getTemplateURL();
    }

    public boolean isSelected() {
        return reportTypeFormat.isDefault();
    }

    public void setSelected(boolean selected) {
        reportTypeFormat.setIsDefault(selected);
    }

    // Determines if the given property is modifiable. If this returns false, then this property is not modifiable.
    public boolean isPropertyModifiable(String propertyName) throws Exception {
        if ("printer".equals(propertyName)) {
            return false;
        }
        return true;
    }
}