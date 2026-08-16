package oracle.retail.sim.client.screen.item;

import oracle.retail.sim.client.locale.LocaleManager;
import oracle.retail.sim.common.core.locale.StringConstants;
import oracle.retail.sim.common.uda.ItemUDAVO;
import oracle.retail.sim.common.uda.UDAType;

/********************************************************************************************************
 * Item UDA Wrapper
 * <p>
 * Copyright 2004, 2013, Oracle. All rights reserved.
 *******************************************************************************************************/

public class ItemUDAWrapper {

    private ItemUDAVO uda;

    public ItemUDAWrapper(ItemUDAVO uda) {
        this.uda = uda;
    }

    public String getDescription() {
        return uda.getDescription();
    }

    public String getValue() {
        if (uda.getType() == UDAType.DATE) {
            if (uda.getUdaDate() != null) {
                return LocaleManager.getShortDateFormatter().format(uda.getUdaDate());
            }
            return StringConstants.EMPTY;
        }
        if (uda.getType() == UDAType.VALUE) {
            return uda.getUdaValue();
        }
        return uda.getUdaText();
    }
}
