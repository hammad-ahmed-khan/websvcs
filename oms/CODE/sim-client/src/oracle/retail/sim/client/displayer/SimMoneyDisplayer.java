package oracle.retail.sim.client.displayer;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.format.MoneyMaskFactory;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.core.type.AbstractDisplayer;
import oracle.retail.sim.common.currency.SimMoney;
import oracle.retail.sim.common.format.MoneyMask;

/********************************************************************************************************
 * Displays a money object formatted in the current locale.
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class SimMoneyDisplayer extends AbstractDisplayer {

    public String getDisplayText(Object value) {
        if (value == null) {
            return StringConstants.EMPTY;
        }
        if (value instanceof SimMoney) {
            SimMoney money = (SimMoney) value;
            MoneyMask mask = MoneyMaskFactory.createMoneyMask(LocaleManager.getNumericLocale(), money.getCurrency());
            return mask.formatData(money.getAmount());
        }
        return StringConstants.EMPTY;
    }
}
