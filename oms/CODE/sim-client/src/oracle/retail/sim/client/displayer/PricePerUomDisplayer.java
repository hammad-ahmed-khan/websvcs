package oracle.retail.sim.client.displayer;

import java.util.Currency;
import java.util.Locale;
import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.format.MoneyMaskFactory;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.locale.StringHelper;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.format.MoneyMask;
import oracle.retail.sim.common.itemticket.ItemTicket;

/********************************************************************************************************
 * Displays price per uom that has money object formatted in the current locale along with the
 * standard selling uom.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class PricePerUomDisplayer extends AbstractDisplayer {

    private Object displayerModel;

    public void setModel(Object model) {
        displayerModel = model;
    }

    public String getDisplayText(Object value, Object model) {
        return getPricePerUomDescription(value, model);
    }

    public String getDisplayText(Object value) {
        return getPricePerUomDescription(value, displayerModel);
    }

    private String getPricePerUomDescription(Object value, Object model) {
        if (value == null || model == null) {
            return StringConstants.EMPTY;
        }
        if (model instanceof ItemTicket) {
            ItemTicket itemTicket = (ItemTicket) model;
            String ticketUom = itemTicket.getStandardSellingUom();
            String itemUom = itemTicket.getRetailItem().getSellingUom();
            String sellingUom = StringHelper.isNullOrEmpty(itemUom) ? StringConstants.EMPTY : itemUom.trim();
            String displayUom = StringHelper.isNullOrEmpty(ticketUom) ? sellingUom : ticketUom;

            Currency currency = itemTicket.getPricePerUom().getCurrency();
            Locale locale = LocaleManager.getNumericLocale();
            MoneyMask mask = MoneyMaskFactory.createMoneyMask(locale, currency);
            String price = mask.formatData(itemTicket.getPricePerUom().getAmount());

            StringBuilder pricePerUom = new StringBuilder();
            pricePerUom.append(price);
            pricePerUom.append(StringConstants.SPACE);
            pricePerUom.append(StringConstants.FORWARD_SLASH);
            pricePerUom.append(StringConstants.SPACE);
            pricePerUom.append(displayUom);

            return pricePerUom.toString();
        }
        return StringConstants.EMPTY;
    }
}
