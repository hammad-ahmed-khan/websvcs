package oracle.retail.sim.client.screen.reportformat;

import oracle.retail.sim.client.locale.Translator;
import oracle.retail.sim.common.core.type.AbstractDisplayer;

public class RetailPrinterTableDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value instanceof RPrinterDialogWrapper) {

        }
        return Translator.getText("-Select-");
    }

}